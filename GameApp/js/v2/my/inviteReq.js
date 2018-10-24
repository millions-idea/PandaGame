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
	 * 获取二维码
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getMerchantQRCode = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/user/getMerchantQRCode"), {
			type: "get",
			data: param,
			dataType: "image/png",
			success: function(data){
				console.log(data);
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
}(mui, window.invite = {}));