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
	 * 获取游戏大区列表
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getLevelSubareas = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/home/getLevelSubareas"), {
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
	 * 获取游戏分区列表
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getSubareas = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/home/getSubareas"), {
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
	 * 获取聚合信息
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getGroupInformation = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/home/getGroupInformation"), {
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
	 * 创建房间
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.createRoom = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/room/create"), {
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
	 * 领取房卡
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getRoomCard = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/home/getRoomCard"), {
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
	 * 充值
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.recharge = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/home/recharge"), {
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
	 * 提现
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.withdraw = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/home/withdraw"), {
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
	 * 获取可用余额
	 * @param {Object} param
	 * @param {Object} callback
	 * @param {Object} errorCallback
	 */
	owner.getWithdrawAmount = function(param, callback, errorCallback){
		callback = callback || $.noop; 
		$.ajax(toUrl("api/home/getWithdrawAmount"), {
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
		
}(mui, window.home = {}));