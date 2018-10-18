var gDoc;
var gallery = mui('.mui-slider');
var bindMessageClick = 0;
var getMessageTimer = null;

(function($, doc){
	
	gDoc = doc;
	
	$.init({
		pullRefresh : {
		    container:"#refreshContainer",//待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
		    down : {
		      style:'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
		      color:'#2BD009', //可选，默认“#2BD009” 下拉刷新控件颜色
		      height:'50px',//可选,默认50px.下拉刷新控件的高度,
		      range:'100px', //可选 默认100px,控件可下拉拖拽的范围
		      offset:'0px', //可选 默认0px,下拉刷新控件的起始位置
		      auto: true,//可选,默认false.首次加载自动上拉刷新一次
		      contentdown : "下拉可以刷新",//可选，在下拉可刷新状态时，下拉刷新控件上显示的标题内容
		      contentover : "释放立即刷新",//可选，在释放可刷新状态时，下拉刷新控件上显示的标题内容
		      contentrefresh : "正在刷新...",//可选，正在刷新状态时，下拉刷新控件上显示的标题内容
		      callback: refresh //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
		    }
	  	}
	});
 	
	$.plusReady(function(){
		// 设置顶部状态栏样式
		plus.navigator.setStatusBarStyle("light");
		plus.navigator.setStatusBarBackground("#FE532A");
		
		// 禁止屏幕翻转
		plus.screen.lockOrientation("portrait-primary"); 
	 
		
		// 检测更新版本
		checkVersion(plus);
		
		// 推送消息被点击
		if(bindMessageClick == 0) {
			console.log("注册推送消息事件");
			
			plus.push.addEventListener("click", function(){
				console.log("推送消息被点击!");
				bindMessageClick = 1;
				if(plus.webview.getWebviewById("messages") != null) plus.webview.getWebviewById("messages").close();
				mui.openWindow({
                    url: "/html/messages.html",
                    id: "messages",
                    preload: false,
                    waiting: {
                        autoShow: true,
                      title:'正在加载...'
                    },
                    createNew: true
                });
		});
		}

		
		
		/*doc.getElementById("recharge").addEventListener("tap", function(){
			//return plus.nativeUI.toast("暂未对外开放，正在申请审核");
			
			mui.openWindow({
                url: "recharge.html",
                id: "recharge",
                preload: false,
                waiting: {
                    autoShow: true,
                  title:'正在加载...'
                },
			    createNew:true

            });

		})*/
		
		
		
		/**
		 * 申请提现
		 */
		/*doc.getElementById("withdraw").addEventListener("tap", function(){
			if(doc.getElementById("centerRightAdButton").textContent != "抢先预览"){
				mui.openWindow({
                    url: "withdraw.html",
                    id: "withdraw",
                    preload: false,
                    waiting: {
                        autoShow: true,
                      title:'正在加载...'
                    },
				    createNew:true
    
                });
			}else{
				plus.nativeUI.toast("暂未开放");
			}
			
		})*/
		
		
		
		doc.getElementById("home_panda").addEventListener("tap", function(){
			mui.openWindow({
				 url: "/html/v2/roomList.html",
                id: "roomList",
				preload: false,
				waiting: {
					autoShow: true,
					title: '正在加载...'
				},
				createNew: true
			});
		});
		
		doc.getElementById("home_pp").addEventListener("tap", function(){
			mui.openWindow({
				 url: "/html/v2/roomList.html",
                id: "roomList",
				preload: false,
				waiting: {
					autoShow: true,
					title: '正在加载...'
				},
				createNew: true
			});
		});
		
		 
		 /**
		  * 领取房卡
		  */
		doc.getElementById("getRoomCard").addEventListener("tap", function(){
			var localCache = plus.storage.getItem('userInfo');
			if(localCache != null && localCache.length > 0){
				home.getRoomCard({
					"token": JSON.parse(localCache).token.toString()
				}, function(data){
					if(data == null || data.code == 500 || data.code == 300) return plus.nativeUI.toast("领取失败");
					if(data.code == 400) {
						if(data.msg.indexOf("未绑定") != -1){
							plus.nativeUI.alert("请您先绑定熊猫麻将账户")
							mui.openWindow({
			                    url: "bind-panda-account.html",
			                    id: "bind-panda-account",
			                    preload: false,
			                    waiting: {
			                        autoShow: true,
		                          title:'正在加载...'
			                    },
							    createNew:true
			    
			                });
			                return;
						}
						return plus.nativeUI.toast(data.msg);
					}

					plus.nativeUI.toast("已提交申请，请等待客服审核！");
				}, function(){
					plus.nativeUI.toast("服务器开小差了,请您稍后重试!"); 
				}) 
				
				
				
				/*mui.confirm("确定为熊猫麻将ID为：" + JSON.parse(localCache).pandaId + "\r\n账户领取30张房卡吗？(金币>100)", "免费房卡", ["确定", "取消"], function(e){
					if(e.index == 0){
						
						home.getRoomCard({
							"token": JSON.parse(localCache).token.toString(),
							"pandaId": JSON.parse(localCache).pandaId.toString(),
						}, function(data){
							if(data == null || data.code == 500 || data.code == 300) return plus.nativeUI.toast("领取失败");
							if(data.code == 400) return plus.nativeUI.toast(data.msg);

							plus.nativeUI.toast("已提交申请，请等待客服审核！");
						}, function(){
							plus.nativeUI.toast("服务器开小差了,请您稍后重试!"); 
						}) 
					}
				})*/
			}else{
				plus.nativeUI.toast("请重新登录");
			}
			
		})
		
	})
	
})(mui, document);
 


/**
 * 下拉刷新回调
 */
function refresh(){
	var topTextAd = gDoc.getElementById("topTextAd"),
		centerBottomAdQCode = gDoc.getElementById("centerBottomAdQCode"),
		bottomAdTitle = gDoc.getElementById("bottomAdTitle"),
		bottomAdDesc = gDoc.getElementById("bottomAdDesc"),
		centerLeftAdTitle = gDoc.getElementById("centerLeftAdTitle"),
		centerLeftAdDesc = gDoc.getElementById("centerLeftAdDesc"),
		centerRightAdTitle = gDoc.getElementById("centerRightAdTitle"),
		centerRightAdDesc = gDoc.getElementById("centerRightAdDesc"),
		centerRightAdButton = gDoc.getElementById("centerRightAdButton");
	
	readMessage();
	
	
	// 加载聚合AD信息
	home.getGroupInformation({}, function(data){
		mui('#refreshContainer').pullRefresh().endPulldown(); 
				
		if(data == null || data.code != 200) return plus.nativeUI.toast("服务器开小差了,请您稍后重试!");
		
		plus.storage.removeItem("groupInfo");
		plus.storage.setItem("groupInfo", JSON.stringify(data.msg));
		
		var html = template("runnerImageTemplate", {
			list: data.msg.topRunnerAds
		}); 
		gDoc.getElementById("runnerImage").innerHTML = html;
		 
		
		
		// 加载动态AD内容
		topTextAd.innerText = data.msg.topTextAd;
		centerBottomAdQCode.style.src = data.msg.centerBottomAdQCode;
		
		
		bottomAdTitle.innerText = data.msg.centerBottomAdTitle;
		bottomAdDesc.innerText = data.msg.centerBottomAdDesc;
		
		
		//centerLeftAdTitle.innerText = data.msg.centerLeftAdTitle;
		//centerLeftAdDesc.innerText = data.msg.centerLeftAdDesc;
		
		//centerRightAdTitle.innerText = data.msg.centerRightAdTitle;
		//centerRightAdDesc.innerText = data.msg.centerRightAdDesc;
		//centerRightAdButton.innerText = data.msg.centerRightAdButton;
		
		
		gDoc.getElementById("centerBottomAdQCode").src = data.msg.centerBottomAdQCode;
		 
		gallery.slider({
		  interval:5000//自动轮播周期，若为0则不自动播放，默认为0；
		});
		
	}, function(){
		plus.nativeUI.toast("服务器开小差了,请您稍后重试!");
		mui('#refreshContainer').pullRefresh().endPulldown();
		
	});

	// 加载游戏分区 
	home.getLevelSubareas({}, function(data){
		if(data == null || data.code != 200 || data.count == 0) return plus.nativeUI.toast("服务器开小差了,请您稍后重试!");
		var html = template("subareasTemplate", {
			list: data.data[0]
		});
		/*gDoc.getElementById("subareas").innerHTML = html;/*/
		 
		// 创建房间事件
		var items = gDoc.getElementsByName("subareas-item");
		
		/*for (var i = 0; i < items.length; i++) {
			items[i].addEventListener("tap", function(){
				var id = this.getAttribute("data-id");
				var state = this.getAttribute("data-state");
				if(state == 0) return plus.nativeUI.toast("此游戏区暂未对外开放");
				mui.openWindow({
                    url: "/html/create-room.html",
                    id: "create-room",
                    preload: false,
                    waiting: {
                        autoShow: true,
                      title:'正在加载...'
                    },
                    extras:{
						subareaId: id
				    },
				    createNew:true
    
                });
			})
		}
		items[0].addEventListener("tap", function(){
			var id = this.getAttribute("data-id");
			var state = this.getAttribute("data-state");
			if(state == 0) return plus.nativeUI.toast("此游戏区暂未对外开放");
			mui.openWindow({
                url: "/html/create-room.html",
                id: "create-room",
                preload: false,
                waiting: {
                    autoShow: true,
                  title:'正在加载...'
                },
                extras:{
					subareaId: id
			    },
			    createNew:true

            });
		})*/
		
		// 打开下载熊猫麻将app地址
		gDoc.getElementById("downloadApp").addEventListener("tap", function(){
			//http://android.myapp.com/myapp/detail.htm?apkName=com.mahjong.sichuang&ADTAG=mobile
			plus.runtime.openURL("http://android.myapp.com/myapp/detail.htm?apkName=com.mahjong.sichuang&ADTAG=mobile");
		})
		
	},function(){
		plus.nativeUI.toast("服务器开小差了,请您稍后重试!");
	});
}

function readMessage(){
	console.log("拉取短信息心跳线程");
	var localCache = plus.storage.getItem('userInfo');
	if(!(localCache != null && localCache.length > 0)) return false; 
	app.getMessageNotification({
		"token": JSON.parse(localCache).token.toString()
	}, function(data){
		if(data == null || data.code == null || data.code == 500 || data.code == 400) return console.log("拉取短消息通知列表失败");
		if(data.code == 300) return console.log("拉取短消息通知列表失败，具体原因：" + data.msg);
		for (var i = 0; i < data.data.length; i++) {
			if(data.data[i].state == 0){
				plus.push.createMessage("您有一条新的短消息: " + data.data[i].message);
			}
		} 
	}, function(){});
	
	
}
 
 
function checkVersion(plugin){
	var btn = ["确定升级", "取消"];
    plugin.runtime.getProperty(plugin.runtime.appid, function (inf) {
        ver = inf.version;
        try{
        	app.getVersion({}, function(data){
				ver = ver.trim();
				var ua = navigator.userAgent.toLowerCase();
				var groupInfo = JSON.parse(plus.storage.getItem("groupInfo"));
				var newVersion = parseInt(data.version.trim().toString().replace(".","").replace(".",""));
				var oldVersion = parseInt(ver.trim().toString().replace(".","").replace(".",""))
 				console.log("IOS:" + groupInfo.iosDownload);
 				console.log("Android:" + groupInfo.androidDownload);
 				console.log("转换版本号:" + newVersion);
 				console.log("转换版本号:" + oldVersion);
         		if (newVersion > oldVersion) {
					if (/iphone|ipad|ipod/.test(ua)) return plus.runtime.openURL(groupInfo.iosDownload);
         			console.log("update版本号" + data.update);
         			if(data.update == 0){
         				var _msg = "发现新版本:V" + data.version;
		                mui.confirm(_msg, '升级确认', btn,
		                function(e) {
		                    if (e.index == 0) { //执行升级操作
								createDownload(plugin, groupInfo.androidDownload);
		                    }
		                });
         			}else{
						if (/iphone|ipad|ipod/.test(ua)) return plus.runtime.openURL(groupInfo.iosDownload);
         				console.log("开始自动下载……");
                    	plugin.nativeUI.toast('正在为您自动下载最新版本……');
         				createDownload(plugin, groupInfo.androidDownload);
         			}
	                
	            } else {
                    plugin.nativeUI.toast('当前版本为最新版本');
                    mui("body").progressbar().hide();
	                return;
	            }
	         })
        }catch(e){
        	//TODO handle the exception
        	console.log("自动下载抛出异常: " + JSON.stringify(e))
        }
         
    });
}

function createDownload(plugin, url){
	// 初始化下载进度
	mui("body").progressbar({progress:0}).show();
    plugin.nativeUI.toast("正在准备环境，请稍后！"); 
    var dtask = plugin.downloader.createDownload(url, {}, function(d, status) {
        console.log("下载响应码:" + status)
        if (status == 200) {
            var path = d.filename; //下载apk
            plugin.runtime.install(path); // 自动安装apk文件
			mui("body").progressbar().hide();
        } else {
            plugin.nativeUI.msg('版本更新失败:' + status);
			mui("body").progressbar().hide();
        }
    });
    dtask.addEventListener( "statechanged", onStateChanged, false );
    dtask.start();
}

// 监听下载任务状态 
function onStateChanged(download, status ) {
	switch(download.state){
		case 0:
			console.log("下载任务开始调度");
			break;
		case 1:
			console.log("下载任务开始请求");
			break;
		case 2:
			console.log("下载任务请求已经接收");                   
			break;
		case 3:
			console.log("下载任务接收数据");
			 var percent = download.downloadedSize / download.totalSize * 100;
                mui("body").progressbar().setProgress(parseInt(percent));    
			break;
		case 4:
			console.log("下载任务已完成");
			break;
		case 5:
			console.log("下载任务已暂停");
			break;
	}
	if ( download.state == 4 && status == 200 ) {
		// 下载完成 
		alert( "Download success: " + download.getFileName() );  
	}  
}