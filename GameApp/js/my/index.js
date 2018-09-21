(function($, owner) {
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
	
	
	/**
	 * 修改密码
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.editPassword = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		 
		if (param.password.length < 6) {
			return errorCallback("密码最短需要6位字符");
		}
		if (param.password.length > 32) {
			return errorCallback("密码最长需要32位字符");
		}
		if (param.password == param.newPassword) {
			return errorCallback("两次密码不能一样");
		}
		
		$.ajax(toUrl("api/user/editPassword"), {
			type: "post",
			data: param,	
			success: function(data){
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	/**
	 * 绑定支付宝账户
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.bindFinanceAccount = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/user/bindFinanceAccount"), {
			type: "post",
			data: param,	
			success: function(data){
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	/**
	 * 绑定熊猫麻将账户
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.bindPandaAccount = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/user/bindPandaAccount"), {
			type: "post",
			data: param,	
			success: function(data){
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	
	
		
}(mui, window.my = {}));