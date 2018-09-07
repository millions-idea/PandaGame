window.wss = {
	/**
	 * 和后端的枚举对应
	 */
	CONNECT: 1, 	// 第一次(或重连)初始化连接
	CHAT: 2, 		// 聊天消息
	SIGNED: 3, 		// 消息签收
	KEEPALIVE: 4, 	// 客户端保持心跳
	PULL_FRIEND:5,	// 重新拉取好友
	
	/**
	 * 设置聊天模型
	 * @param {Object} senderId
	 * @param {Object} roomCode
	 * @param {Object} receiverId
	 * @param {Object} msg
	 * @param {Object} msgId
	 */
	ChatMsg: function(senderId, roomCode, receiverId, msg, msgId){
		this.senderId = senderId;
		this.roomCode = roomCode;
		this.receiverId = receiverId;
		this.msg = msg;
		this.msgId = msgId;
	},
	
	/**
	 * 构建消息 DataContent 模型对象
	 * @param {Object} action
	 * @param {Object} chatMsg
	 * @param {Object} extand
	 */
	DataContent: function(action, chatMsg, extand){
		this.action = action;
		this.chatMsg = chatMsg;
		this.extand = extand;
	},
	
	/**
	 * 单个聊天记录的对象
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} flag
	 */
	ChatHistory: function(myId, friendId, msg, flag){
		this.myId = myId;
		this.friendId = friendId;
		this.msg = msg;
		this.flag = flag;
	},
	
	/**
	 * 快照对象
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} isRead	用于判断消息是否已读还是未读
	 */
	ChatSnapshot: function(myId, friendId, msg, isRead){
		this.myId = myId;
		this.friendId = friendId;
		this.msg = msg;
		this.isRead = isRead;
	},
	
	

	initRoom: function(roomCode, externalRoomId, name){
		
		window.CHAT = {
			socket: null,
			init: function() {
				console.log("初始化wss");
				if (window.WebSocket) {
					
					// 如果当前的状态已经连接，那就不需要重复初始化websocket
					if (CHAT.socket != null 
						&& CHAT.socket != undefined 
						&& CHAT.socket.readyState == WebSocket.OPEN) {
							console.log("wss已连接");
						return false;
					}
					
					console.log("创建wss连接");
					
					try{
						CHAT.socket = new WebSocket(config.wss);
						CHAT.socket.onopen = CHAT.wsopen,
						CHAT.socket.onclose = CHAT.wsclose,
						CHAT.socket.onerror = CHAT.wserror,
						CHAT.socket.onmessage = CHAT.wsmessage;
					}catch(e){
						//TODO handle the exception
						console.log(e)  
					}
				} else {
					mui.alert("手机设备过旧，请升级手机设备...");
				}
			},
			close: function(){
				if(window.activeHand != null) clearInterval(window.activeHand);
				if(CHAT != null && CHAT.socket != null) CHAT.socket.close();
				console.log("主动关闭心跳和连接")
			},
			chat: function(msg) {
				console.log("发送消息：" + msg);
				
				// 如果当前websocket的状态是已打开，则直接发送， 否则重连
				if (CHAT.socket != null 
						&& CHAT.socket != undefined  
						&& CHAT.socket.readyState == WebSocket.OPEN) {
						console.log("直连websocket：" + msg);

						CHAT.socket.send(msg);
				} else {
					// 重连websocket
					console.log("重连websocket：" + msg);
					
					clearInterval(window.reChatHand);
					window.reChatHand = setTimeout("CHAT.initAndRetry('" + msg + "')", 30000);
				}
			},
			initAndRetry: function(msg) {
				console.log("重新发送消息：" + msg);
				CHAT.init();
				CHAT.socket.send(msg);
			},
			reChat: function(msg) {
				console.log("重新发送消息：" + msg);
				CHAT.socket.send(msg);
			},
			wsopen: function() {
				console.log("websocket连接已建立...");
				var me = JSON.parse(plus.storage.getItem('userInfo'));
				// 构建ChatMsg
				var chatMsg = new wss.ChatMsg(me.userId, roomCode, null, null);
				// 构建DataContent
				var dataContent = new wss.DataContent(wss.CONNECT, chatMsg, null);
				// 发送websocket
				CHAT.chat(JSON.stringify(dataContent));
				
				console.log("发送socket数据包:" + JSON.stringify(dataContent));
				
				
				// 定时发送心跳
				clearInterval(window.activeHand);
				window.activeHand = setInterval("CHAT.keepalive()", 30000);
			},
			wsmessage: function(e) {
				console.log("收到消息：" + e.data);
				
				var data = JSON.parse(e.data);
				var msg = data.chatMsg.msg;
				console.log(data.chatMsg.msg)
				
				var chatWebview = plus.webview.getWebviewById("room-detail-" + roomCode);
				
				if(chatWebview != null){
					if(msg.indexOf("系统消息") != -1){
						msg =  msg.replace("系统消息","");
						console.log("推送系统消息：" + msg);
						chatWebview.evalJS("recvMessageFromRoom('" + msg + "')");
					}else{
						chatWebview.evalJS("receiveMsg('" + msg + "')");
					}
		 			chatWebview.evalJS("resizeScreen()");
				}
				
		 		
			},
			wsclose: function() {
				console.log("连接关闭...");
			},
			wserror: function() {
				console.log("发生错误...");
			},
			keepalive: function() {
				// 构建对象
				var dataContent = new wss.DataContent(wss.KEEPALIVE, null, null);
				// 发送心跳
				console.log("发送心跳" +JSON.stringify(dataContent))
				CHAT.chat(JSON.stringify(dataContent));
			}
		};
		
		
		CHAT.init();
		
		console.log("打开聊天页面："  +  roomCode + "," + name + "," + externalRoomId);
		
		
		mui.openWindow({
            url: "/html/room-detail.html",
            id: "room-detail-" + roomCode,
            preload: false,
            waiting: {
                autoShow: true,
              title:'正在加载...'
            },
            extras:{
            	"title": roomCode + " - " + name,
				"room_code": roomCode,
				"externalRoomId": externalRoomId
		    },
		    createNew:true

       }); 
	}
}
