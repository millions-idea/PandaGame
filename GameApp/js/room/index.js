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
	 * 获取用户房间列表
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getRoomList = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/room/getRoomList"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data));
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	/**
	 * 匹配合适的房间
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getLimitRoom = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/room/getLimitRoom"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data));
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	
	/**
	 * 获取用户房间列表
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getAllRoomList = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/room/getAllRoomList"), {
			type: "get",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data));
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	
	/**
	 * 解散房间
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.disband = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/room/disband"), {
			type: "post",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data));
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	/**
	 * 结算
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.closeAccounts = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/room/closeAccounts"), {
			type: "post",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data));
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
	
	
	/**
	 * 加入房间
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.joinRoom = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/room/join"), {
			type: "post",
			data: param,	
			success: function(data){
				console.log(JSON.stringify(data));
				return callback(data);
			},
			error: function(xhr,type,errorThrown){
				ajaxError(xhr, type, errorThrown, errorCallback);
			}
		})
	}
}(mui, window.room = {}));