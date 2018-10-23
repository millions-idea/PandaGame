var gDoc;

(function($, doc) {
	
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
		      callback :refreshUserInfo //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
		    }
	  	}
	});
	
	
	// 设置个人信息初始值
	var data = {
		phone: "...",
		balance: "...",
		canWithdrawAmount: "...",
		canNotWithdrawAmount: "..."
	}
	var html = template("userInfoTemplate",data);
	doc.getElementById("userInfo").innerHTML = html;
	  
	
	$.plusReady(function() {
		var customServiceButton = doc.getElementById("customServiceButton"),
			updatePasswordButton = doc.getElementById("updatePasswordButton"),
			messageButton = doc.getElementById("messageButton"),
			logoutButton = doc.getElementById("logoutButton"),
			inviteButton = doc.getElementById("inviteButton"),
			rechargeButton = doc.getElementById("recharge"),
			withdrawButton = doc.getElementById("withdraw");
		
		plus.nativeUI.closeWaiting();
		
		
		// 刷新
		plus.webview.currentWebview().addEventListener("show", function(){
			plus.nativeUI.closeWaiting(); 
			refreshUserInfo();
		})
		
		// 短消息
		messageButton.addEventListener("tap", function(){
			mui.openWindow({
                url: "/html/messages.html",
                id: "messages",
                preload: false,
                waiting: {
                    autoShow: true,
                  title:'正在加载...'
                },
			    createNew:true

            });
		}, false)
		
		
		// 修改密码
		updatePasswordButton.addEventListener("tap", function(){
			plus.webview.open('/html/update-password.html', 'new', {}, 'slide-in-right', 200);
		}, false)
		
		// 联系客服
		customServiceButton.addEventListener("tap", function(){
			mui.openWindow({
                url: "/html/custom-service.html",
                id: "custom-service",
                preload: false,
                waiting: {
                    autoShow: true,
                  title:'正在加载...'
                },
			    createNew:true

            });
		}, false)
		
		// 注销登录
		logoutButton.addEventListener("tap", function(){
			var localCache = plus.storage.getItem('userInfo');
			if(localCache != null && localCache.length > 0){ 
				app.logout({
					"token": JSON.parse(localCache).token.toString()
				}, function(data){
					toLogin($);
				}, function(){
					toLogin($);
				})
			}else{
				toLogin($);
			}
		}, false)
		
		
		// 充值
		rechargeButton.addEventListener("tap", function(){
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
		})
		
		
		// 提现
		withdrawButton.addEventListener("tap", function(){
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
			
		})
	});
}(mui, document));


/**
 * 刷新用户信息
 */
function refreshUserInfo(){ 
	var localCache = plus.storage.getItem('userInfo');
	app.getUserDetailInfo({
		"token": JSON.parse(localCache).token.toString()
	}, function(data){
		mui('#refreshContainer').pullRefresh().endPulldown();

		if(data == null || data.code == null || data.code == 500 || data.code == 300){
			plus.nativeUI.toast("刷新失败");
			return false;
		}
		
		if(data.code == 400){
			plus.nativeUI.toast(data.msg);
			return false;
		}
		
		
		// 格式化信息
		var oldJson = data.msg;
		var newJson = oldJson;
		newJson.balance =  window.moneyUtil.dealNumber(newJson.balance);
		newJson.canWithdrawAmount  = window.moneyUtil.dealNumber(newJson.canWithdrawAmount);
		newJson.canNotWithdrawAmount  = window.moneyUtil.dealNumber(newJson.canNotWithdrawAmount);
		
		var html = template("userInfoTemplate", newJson);
		gDoc.getElementById("userInfo").innerHTML = html;
		
		
		plus.storage.setItem('userInfo', JSON.stringify(data.msg));
		
		  
		
	}, function(){
		mui('#refreshContainer').pullRefresh().endPulldown();
		plus.nativeUI.toast("服务器开小差了,请您稍后重试!");
	})
	
}

/**
 * 跳转登录页
 * @param {Object} $
 */
function toLogin($){
	plus.storage.setItem('userInfo','');
	plus.storage.setItem('isAutoLogin','false');
	$.openWindow({
		url: '/login.html',
		id: 'login',
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
}