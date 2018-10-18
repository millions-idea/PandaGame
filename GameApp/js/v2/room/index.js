var pages = window.config.pages;
var firends;
var $ = mui;
var doc = document;
var roomCode;
var externalRoomId;
var timerCountHand;

$.init({
	statusBarStyle: "light",
	statusBarBackground: '#fe5a2f'
});

$.plusReady(function() {
	
	console.log(document.documentElement.clientWidth + "," + document.documentElement.clientHeight)

	
	// 设置顶部状态栏样式
	plus.navigator.setStatusBarStyle("light");
	plus.navigator.setStatusBarBackground("#FE532A");

	// 禁止屏幕翻转
	plus.screen.lockOrientation("portrait-primary");
 

	// 绑定动态标题事件
	var self = plus.webview.currentWebview(),
		titleComponent = doc.getElementById("title");
 

	// 获取上一个页面传入的值 
	roomCode = self.room_code;
	externalRoomId = self.externalRoomId;

	self.addEventListener("show", function() {
		titleComponent.innerHTML = self.title;
	})

	if(timerCountHand != null) clearInterval(timerCountHand);
	timerCountHand = setInterval(getPersonCount, 5000);
	getPersonCount();


	// 绑定选择菜单
	var openMenu = doc.getElementById("openMenu"),
		toDisband = doc.getElementById("toDisband"),
		toCloseAccounts = doc.getElementsByName("toCloseAccounts"),
		customCloseAccounts = doc.getElementById("customCloseAccounts");

	openMenu.addEventListener("tap", function() {
		$("#sheet").popover("toggle");
	});

	toDisband.addEventListener("tap", function() {

		plus.nativeUI.confirm("您确定要退出此房间？", function(e) {
			if(e.index == 1) {
				$("#sheet").popover("toggle");
				var localCache = plus.storage.getItem('userInfo');
				if(!(localCache != null && localCache.length > 0)) return false;
				room.disband({
					"token": JSON.parse(localCache).token.toString(),
					"roomCode": roomCode
				}, function(data) {
					if(data == null || data.code == 500 || data.code == 300) return plus.nativeUI.toast("退出失败");

					if(data == null || data.code == 400) return plus.nativeUI.toast(data.msg);

					plus.nativeUI.toast("退出成功")

					var me = JSON.parse(plus.storage.getItem('userInfo'));
					sendMessageToRoom('用户' + me.phone + '退出了房间');

					$.openWindow({
						url: 'room.html',
						id: 'room',
						preload: true,
						show: {
							aniShow: 'pop-in',
							duration: 200
						},
						styles: {
							popGesture: 'hide'
						},
						waiting: {
							title: "正在加载...",
							autoShow: true
						}
					});

					for(var i = 0; i < pages.length; i++) {
						if(i != 2) {
							plus.webview.hide(pages[i].id, "fade-out", 200);
						}
					}

					exitRoom();
					plus.webview.currentWebview().close();
				}, function() {
					plus.nativeUI.toast("退出失败")
				});
			}
		}, "敏感操作", ["取消", "确定"]);
	})

	customCloseAccounts.addEventListener("tap", function(){
		mui.openWindow({
			url: "../custom-service.html",
			id: "custom-service",
			preload: false,
			waiting: {
				autoShow: true,
				title: '正在加载...'
			},
			createNew: true
		});
	})


	// 结算 
	document.getElementById("sl").addEventListener("tap", function(){
		closeAccounts(0);
	})
	document.getElementById("zb").addEventListener("tap", function(){
		closeAccounts(1);
	})
});


function closeAccounts(type){
	plus.nativeUI.confirm("您确定要提交此战绩吗?", function(e){
		if(e.index == 0){
			
			var gradeValue = document.getElementById("grade").value;
			
			// 计算格式成绩
			if(gradeValue == null || gradeValue.length == 0) {
				return plus.nativeUI.toast("请您输入您的游戏战绩!");
			}
			
			if(type == 1) gradeValue = gradeValue - (gradeValue * 2)
			
			var localCache = plus.storage.getItem('userInfo');
			if(localCache != null && localCache.length > 0){
				var param = {
					"token": JSON.parse(localCache).token.toString(),
					"roomCode": parseInt(roomCode),
					"grade": parseFloat(gradeValue)
				};
				
				room.closeAccounts(param, function(data){
					//toCloseAccountsButton.disabled = false;
					//toCloseAccountsButton.classList.remove("disabled");
			
					if(data.code == 500 || data.code == 300){
						plus.nativeUI.toast("申请结算失败, 请您联系客服!"); 
						return false;
					}
					
					if(data.code == 400){ 
						if(data.msg == "已存在") data.msg = "请您不要重复申请结算";
						plus.nativeUI.toast(data.msg);
						return false;
					}
					
					plus.nativeUI.toast("您已成功提交结算申请, 请等待客服审核!");
					$.openWindow({
						url: 'my.html',
						id: 'my',
						preload: true,
						show: {
							aniShow: 'pop-in',
							duration: 200
						},
						styles: {
							popGesture: 'hide'
						},
						waiting: {
							title: "正在加载...",
							autoShow: true
						}
					});
					
					for (var i = 0 ; i < pages.length ; i ++) {
						if (i != 3) { 
							plus.webview.hide(pages[i].id, "fade-out", 200);
						}
					}
					plus.webview.getWebviewById("room-detail-" + roomCode).close();
					plus.webview.currentWebview().close();
					
				}, function(){
					toCloseAccountsButton.disabled = false;
					toCloseAccountsButton.classList.remove("disabled");
					plus.nativeUI.toast("服务器开小差了, 请您稍后重试!");
				});
			}
		}
	})
	
	
}


// 刷新房间人数
function getPersonCount() {
	console.log("当前房间" + roomCode + "总在线人数")
	room.getPersonCount({
		"roomCode": roomCode
	}, function(data) {
		if(data.code == 200) { 
			// 控制所有人的头像状态
			var persons = document.getElementsByName("persons");
			for (var i = 0; i < parseInt(data.msg.persons); i++) {
				persons[i].classList.remove("enable");
				persons[i].classList.add("enable");
			}
 
			if(data.msg.persons >= data.msg.count){
				document.getElementById("state").textContent = "游戏中";
				document.getElementById("pandaRoomId").textContent = externalRoomId;
			}
			
			
		} else if(data.code == 300) {
			// 弹窗提醒
			var modal = document.getElementById("modal");
			if(modal == null) {
				 game.message("由于30分钟内未拼桌成功，系统已自动将该房间解散！", function() {
						var audioPlayer = plus.audio.createPlayer("/mp3/v2/message.mp3");
						audioPlayer.play();
					}, function() {
						var audioPlayer = plus.audio.createPlayer("/mp3/v2/btnClick.mp3");
						audioPlayer.play();
						plus.webview.currentWebview().close();
						plus.webview.getWebviewById("room").evalJS("refresh()");
					});
			}
			// 弹窗提醒
		}
	}, function() {})
}

function exitRoom() {
	plus.webview.getWebviewById("room").evalJS("CHAT.close()");
}

// 播放发送消息的铃声
function playSendMsgSound() {
	var audioPlayer = plus.audio.createPlayer("/mp3/send.mp3");
	audioPlayer.play();
}

// 播放接受消息的铃声
function playReceiveMsgSound() {
	var audioPlayer = plus.audio.createPlayer("/mp3/msn.mp3");
	audioPlayer.play();
}