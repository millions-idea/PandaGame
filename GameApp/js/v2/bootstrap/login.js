//document.getElementById("title").innerText = window.config.title;
			
(function($, doc) {
	$.init({
		statusBarStyle: "light",
		statusBarBackground: '#fe5a2f',
		keyEventBind: {
			backbutton: false  //关闭back按键监听
		}
	});
	 
	
	$.plusReady(function() {
		
		// 设置顶部状态栏样式
		plus.navigator.setStatusBarStyle("light");
		plus.navigator.setStatusBarBackground("#FE532A");
		
		// 禁止屏幕翻转
		plus.screen.lockOrientation("portrait-primary");
		 
		var loginButton = doc.getElementById('loginButton'),
			registerButton = doc.getElementById('registerButton'),
			phoneInput = doc.getElementById('phone'),
			passwordInput = doc.getElementById('password');
		 
		
		// 检查用户是否已登录
		isLogin($);
		
		// 界面资源加载完成
		var self = plus.webview.currentWebview();
		self.addEventListener("show", function(){
			plus.nativeUI.closeWaiting();
			try{
				// 从会话中拿数据
				if(self.phone != null) phoneInput.value = self.phone;
				
				// 从缓存中拿用户数据
				var userInfo = JSON.parse(plus.storage.getItem('userInfo'));
				if(userInfo != null && userInfo.phone != "undefined"){
					phoneInput.value = JSON.parse(userInfo).phone;
				}
			}catch(e){
				//TODO handle the exception
			}
		})
		
		// 注册账号
		registerButton.addEventListener('tap', function(event) {
			$.openWindow({
				url: 'register.html',
				id: 'register',
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
		}, false);
	
		
		// 登录
		loginButton.addEventListener("tap",function(){
			loginButton.disabled = true;
			loginButton.classList.add("button-disabled");
			
			document.activeElement.blur();
			plus.nativeUI.showWaiting( "正在登录,请稍后……" );
			
			app.login({
				phone: phoneInput.value,
				password: passwordInput.value
			}, function(data){
				if(data == null || data.code == null || data.code == 500 || data.code == 300){
					plus.nativeUI.toast("登录失败");
					plus.nativeUI.closeWaiting();
					loginButton.disabled = false;
					loginButton.classList.remove("button-disabled");
					return false;
				}
				
				if(data.code == 400){
					plus.nativeUI.closeWaiting();
					loginButton.disabled = false;
					loginButton.classList.remove("button-disabled");
					plus.nativeUI.toast(data.msg);
					return false;
				}
				
				plus.nativeUI.closeWaiting();
				plus.nativeUI.toast("登录成功");
				loginButton.disabled = false;
				loginButton.classList.remove("button-disabled");
				
				plus.storage.setItem('userInfo', JSON.stringify(data.msg));
				plus.storage.setItem('isAutoLogin', "true");
				
				console.log(data.msg.token)
				
				toHome($);
				
			}, function(){
				mui.trigger(loginButton,'tap',data);
				plus.nativeUI.closeWaiting();
				loginButton.disabled = false;
				loginButton.classList.remove("button-disabled");
				plus.nativeUI.toast("登录超时,请您稍后重试!");
			})
		}, false) 
	});
}(mui, document));


/**
 * 用户是否登录
 */
function isLogin($){
	
	try{
		var localCache = plus.storage.getItem('userInfo');
		var isAutoLogin = plus.storage.getItem('isAutoLogin');
		if(isAutoLogin == null || isAutoLogin != "true") localCache =  null;
		if(localCache != null && localCache.length > 0){
			plus.nativeUI.showWaiting( "正在获取信息,请稍后……" );
			
			app.getUserInfo({
				"token": JSON.parse(localCache).token.toString()
			}, function(data){
				plus.nativeUI.closeWaiting();
				
				if(data.code == 200){
					 toHome($);
				}else{
					plus.nativeUI.toast("自动登录失败");
				}
			}, function(){ 
				plus.nativeUI.closeWaiting();
			})
		}
	}catch(e){
		//TODO handle the exception
		plus.nativeUI.closeWaiting();
	}
	
	
}


/**
 * 跳转到首页
 */
function toHome($){
	plus.nativeUI.closeWaiting();
	$.openWindow({
		url: 'index.html',
		id: 'index',
		preload: true,
		show: {
			aniShow: 'pop-in',
			duration: 200
		},
		styles: {
			popGesture: 'hide',
			/*titleNView: {
			    type:'transparent',//透明渐变样式
			}*/
		},
		waiting: {
			title: "正在加载...",
			autoShow: true
		}
	});
	plus.nativeUI.closeWaiting();
}