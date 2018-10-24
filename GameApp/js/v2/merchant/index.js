var gDoc;
var pages = window.config.pages;

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
		
		var currentWebView = plus.webview.currentWebview();
		
		plus.webview.currentWebview().addEventListener("show", function(){ 
			mui('#refreshContainer').pullRefresh().endPulldown(true);
			refresh();
		})
		
		
		// 我想成为代理
		doc.getElementById("contact").addEventListener("tap", function(){
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
		
		
		// 查看邀请会员
		doc.getElementById("invite").addEventListener("tap", function(){
			mui.openWindow({
				url: "../invite.html",
				id: "invite",
				preload: false,
				waiting: {
					autoShow: true,
					title: '正在加载...'
				},
				createNew: true
			});
		})
		
	})
	
})(mui, document);

function getMonth (ts) {
    var date = new Date(ts);
    Y = date.getFullYear() + '/';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '/';
    D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    return M +  D;
}

function getDay (ts) {
    var date = new Date(ts);
    Y = date.getFullYear() + '/';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '/';
    D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes();
    s = date.getSeconds();
    return h + m;
}

function refresh(callback){
	var localCache = plus.storage.getItem('userInfo');
	if(!(localCache != null && localCache.length > 0)) return false; 
	
	// 查询商家消息
	merchant.getMerchantBusinessList({ 
		token: JSON.parse(localCache).token.toString()
	}, function(data){
		if(data == null || data.code != 200) {
			if(data.msg.indexOf("请开通代理商权限后再来哦") != -1){
				gDoc.getElementById("notRole").style.display = "block";
				gDoc.getElementById("head").style.display = "none";
				gDoc.getElementById("list").style.display = "none";
				return plus.nativeUI.toast(data.msg);
			}
			if(data.code == 500) data.msg = "服务器有些拥挤~";
			return plus.nativeUI.toast(data.msg);
		}
		// 更新统计面板
		gDoc.getElementById("regIncome").textContent = window.moneyUtil.dealNumber(data.msg.regiIncome);
		gDoc.getElementById("returnIncome").textContent = window.moneyUtil.dealNumber(data.msg.returnIncome);
		gDoc.getElementById("friends").textContent = data.msg.friends;
		
		// 更新信息列表
		var arr = data.msg.merchantMessageList;
		for (var i = 0; i < arr.length; i++) {
			var item = arr[i]; 
			item.beforeTime = getMonth(item.addTime);
			item.afterTime = getDay(item.addTime);
			if(item.type == 1){
				item.icon = "hb.png";
			}else if(item.type == 2){
				item.icon = "chongzhi.png";
			}
		}
		
		var html = template("template", {
			list: arr
		}); 
		gDoc.getElementById("list").innerHTML = html;
	});
	mui('#refreshContainer').pullRefresh().endPulldown(true);
}