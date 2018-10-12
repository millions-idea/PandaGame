(function($, owner) {
	
	var loginSession = { 
		setSession : function(obj) {
			plus.storage.setItem('userInfo', JSON.stringify(obj));
		},
		removeSession : function() {
			plus.storage.setItem('userInfo', '');
		}
	};
	
	
	
	
	/**
	 * 获取api接口
	 * @param {Object} url
	 */
	toUrl = function(url){
		console.log("网络请求: " + window.config.api + url)
		return window.config.api + url; 
	};
	
	ajaxError = function(xhr,type,errorThrown,errorCallback){
		plus.nativeUI.toast("服务器有些拥挤喔");
		errorCallback();
	};
	

	owner.createState = function(param) {
		var state = owner.getState();
		state.data = param;
		owner.setState(state); 
	};
	 
	
	/**
	 * 用户登录
	 **/
	owner.login = function(param, callback, errorCallback) {
		callback = callback || $.noop; 
		if (param.phone.length < 11) {
			return errorCallback("手机号最短为 11 个字符");
		}
		if (param.password.length < 6) {
			return errorCallback('密码最短为 6 个字符');
		}
		$.ajax(toUrl("api/bootstrap/login"), {
			type: "post",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data))
							
				if(data.code == 200){
					plus.storage.setItem('userInfo', data.msg);
					plus.storage.setItem('isAutoLogin', true);
				}
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
		
	};
	
	
	
	/**
	 * 用户登出
	 **/
	owner.logout = function(param, callback, errorCallback) {
		$.ajax(toUrl("api/bootstrap/logout"), {
			type: "get",
			data: param,
			success: function(data){
				console.log(JSON.stringify(data))
				
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
		
	};
 
	
	/**
	 * 获取登录用户信息
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getUserInfo = function(param, callback, errorCallback){
		$.ajax(toUrl("api/bootstrap/getUserInfo"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data))
				
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	/**
	 * 查询用户详细信息
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getUserDetailInfo = function(param, callback, errorCallback){
		$.ajax(toUrl("api/user/getUserInfo"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data))
				
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	/**
	 * 手机注册
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.register = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		 
		if (param.phone.length < 11) {
			return errorCallback("手机号最短需要11位数字");
		}
		if (param.phone.length > 11) {
			return errorCallback("手机号最长需要11位数字");
		}
		if (param.password.length < 6) {
			return errorCallback("密码最短需要6位字符");
		}
		if (param.password.length > 32) {
			return errorCallback("密码最长需要32位字符");
		}
		if (param.password != param.password2) {
			return errorCallback("两次密码不一致");
		}
		$.ajax(toUrl("api/bootstrap/register"), {
			type: "post",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data))
				
				if(data.code == 200){
					var users = JSON.parse(localStorage.getItem('$users') || '[]');
					users.push(param);
					localStorage.setItem('$users', JSON.stringify(users));
				}
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	/**
	 * 获取手机验证码
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getSmsCode = function(phone, callback, errorCallback){ 
		callback = callback || $.noop; 
		if (phone.length < 11) {
			return errorCallback("手机号最短需要11位数字");
		}
		
		$.ajax(toUrl("api/bootstrap/getSmsCode"), {
			type: "get",
			data: {
				"phone": phone
			},	
			success: function(data){
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
		
	} 
	
	owner.getMessageList = function(param, callback, errorCallback){ 
		callback = callback || $.noop; 
		$.ajax(toUrl("api/bootstrap/getMessageList"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data))
				if(data != null && data.code != null && data.code == 200){
					plus.storage.setItem('messageList',JSON.stringify(data.data));
				}
							
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
		
	} 

	owner.getMessageNotification = function(param, callback, errorCallback){ 
		callback = callback || $.noop; 
		$.ajax(toUrl("api/bootstrap/getMessageNotification"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data))
				if(data != null && data.code != null && data.code == 200){
					plus.storage.setItem('messageList',JSON.stringify(data.data));
				}
							
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
		
	} 



	owner.getVersion = function(param, callback, errorCallback){ 
		callback = callback || $.noop; 
		$.ajax(toUrl("api/bootstrap/version"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data))
				if(data != null && data.code != null && data.code == 200){
					plus.storage.setItem('messageList',JSON.stringify(data.data));
				}
							
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
		
	} 


	/**
	 * 获取当前状态
	 **/
	owner.getState = function() {
		var stateText = localStorage.getItem('$state') || "{}";
		return JSON.parse(stateText);
	};

	/**
	 * 设置当前状态
	 **/
	owner.setState = function(state) {
		state = state || {};
		localStorage.setItem('$state', JSON.stringify(state));
		var settings = owner.getSettings();
		settings.gestures = '';
		owner.setSettings(settings);
	};



	/**
	 * 找回密码
	 **/
	owner.forgetPassword = function(email, callback) {
		callback = callback || $.noop;
		if (!checkEmail(email)) {
			return callback('邮箱地址不合法');
		}
		return callback(null, '新的随机密码已经发送到您的邮箱，请查收邮件。');
	};

	/**
	 * 获取应用本地配置
	 **/
	owner.setSettings = function(settings) {
		settings = settings || {}; 
		localStorage.setItem('$settings', JSON.stringify(settings));
	}

	/**
	 * 设置应用本地配置
	 **/
	owner.getSettings = function() {
		var settingsText = localStorage.getItem('$settings') || "{}";
		return JSON.parse(settingsText);
	}
	
	
	/**
	 * 跳转到指定选项卡
	 * @param {Object} index
	 */
	owner.toBar = function(index){
		var items = document.getElementsByClassName("mui-tab-item");
		for (var i = 0; i < items.length; i++) {
			items[i].classList.remove("mui-active");
		}
		items[index].classList.add("mui-active");
	}
	
	owner:getUserGlobalInfo = function(){
		var json = plus.storage.getItem('userInfo');
		return JSON.parse(json);
	} 
	
	
	owner:timestampConvert = function (ts) {
        var date = new Date(ts);
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes() + ':';
        s = date.getSeconds();
        return Y+M+D+h+m+s;
    }
	
	owner:readCopy = function(){
		
		var Context = plus.android.importClass("android.content.Context");
	    var main = plus.android.runtimeMainActivity();
	    var clip = main.getSystemService(Context.CLIPBOARD_SERVICE);
	    return plus.android.invoke(clip,"getText");
	    
	}
	 
	
}(mui, window.app = {}));