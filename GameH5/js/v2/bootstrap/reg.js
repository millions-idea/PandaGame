(function($, doc) {
	$.init({
		statusBarStyle: "light",
		statusBarBackground: '#fe5a2f'
	});
	$.plusReady(function() {
		//document.getElementById("title").innerText = window.config.title;

		// 设置顶部状态栏样式
		plus.navigator.setStatusBarStyle("light");
		plus.navigator.setStatusBarBackground("#FE532A");
		
		// 禁止屏幕翻转
		plus.screen.lockOrientation("portrait-primary");

		var toLoginButton = doc.getElementById('toLogin'),
			phoneInput = doc.getElementById("phone"),
			passwordInput = doc.getElementById("password"),
			password2Input = doc.getElementById("password2"),
			parentIdInput = doc.getElementById("parentId"),
			smsCodeInput = doc.getElementById("smsCode"),
			smsCodeButton = doc.getElementById("smsCodeButton"),
			registerButton = doc.getElementById("registerButton");

		// 动态绑定事件
		toLoginButton.addEventListener('tap', function(event) {
			$.openWindow({
				url: 'login.html',
				id: 'login',
				preload: false,
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

		/**
		 * 获取短信验证码
		 */
		/*smsCodeButton.addEventListener("tap", function() {
			smsCodeButton.disabled = true;
			smsCodeButton.classList.add("disabled");
			smsCodeButton.innerText = "重新获取短信";

			var initval = 120;
			var timer = setInterval(function() {
				if(initval >= 2) {
					initval--;
					smsCodeButton.innerText = "重新获取(" + initval + ")"
				} else {
					smsCodeButton.disabled = false;
					smsCodeButton.classList.remove("disabled");
					smsCodeButton.innerText = "获取短信验证码";
					initval = 120;
					clearInterval(timer);
				}
			}, 1000)

			app.getSmsCode(phoneInput.value, function(data) {
				if(data.code == 500) {
					plus.nativeUI.toast("获取短信验证码失败");
					smsCodeButton.disabled = false;
					smsCodeButton.classList.remove("disabled");
					smsCodeButton.innerText = "获取短信验证码";
					initval = 120;
					clearInterval(timer);
					return false;
				}

				if(data.code == 300) {
					smsCodeButton.disabled = false;
					smsCodeButton.classList.remove("disabled");
					smsCodeButton.innerText = "获取短信验证码";
					initval = 120;
					clearInterval(timer);
					plus.nativeUI.toast(data.msg);
					return false;
				}

				smsCodeButton.disabled = false;
				smsCodeButton.classList.remove("disabled");
				smsCodeButton.innerText = "获取短信验证码";
				initval = 120;
				clearInterval(timer);
				mui.alert(data.msg);
			}, function(msg) {
				smsCodeButton.disabled = false;
				smsCodeButton.classList.remove("disabled");
				smsCodeButton.innerText = "获取短信验证码";
				initval = 120;
				clearInterval(timer);
				plus.nativeUI.toast(msg);
			})

		}, false);*/

		/**
		 * 注册
		 */
		registerButton.addEventListener("tap", function() {
			document.activeElement.blur();
			
			registerButton.disabled = true;
			registerButton.classList.add("button-disabled");

			app.register({
				phone: phoneInput.value,
				password: passwordInput.value,
				password2: password2Input.value,
				parentId: parentIdInput.value
				/*smsCode: smsCodeInput.value,*/
			}, function(data) {
				console.log(JSON.stringify(data))

				if(data.code == 500 || data.code == 300) {
					plus.nativeUI.toast("注册失败");
					registerButton.disabled = false;
					registerButton.classList.remove("button-disabled");
					return false;
				}

				if(data.code == 400) {
					registerButton.disabled = false;
					registerButton.classList.remove("button-disabled");
					plus.nativeUI.toast(data.msg);
					return false;
				}
				registerButton.disabled = false;
				registerButton.classList.remove("button-disabled");
				
				// 手机号校验
				var phone = phoneInput.value;
				if(phone === undefined || phone === null || phone.length <= 0) phone = null;
				
				$.openWindow({
					url: 'login.html',
					id: 'login',
					preload: false,
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
					},
					extras: {
						"phone": phone
					}
				});
			}, function(msg) {
				registerButton.disabled = false;
				registerButton.classList.remove("button-disabled");
				plus.nativeUI.toast(msg);
			})
		}, false)

	});
}(mui, document));