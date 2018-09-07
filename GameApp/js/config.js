(function(){
	
	window.config = {
		title: "宜乐对战",
		/*pub*/
		api: "http://132.232.61.181:59845/",
		file: "http://132.232.61.181:8090/",
		wss: "ws://132.232.61.181:8088/ws",
		
		/*test*/
		/*api: "http://9gt7zz.natappfree.cc/",
		file: "http://193.112.151.148:8090/",
		wss: "ws://193.112.151.148:8088/ws",*/
		
		/*dev*/
		/*api: "http://vwhwz6.natappfree.cc/",
		file: "http://132.232.61.181:8090/",
		wss: "ws://192.168.1.103:8088/ws",*/
		pages: [{
			id: "home",
			url: "html/home.html",
			style: {
				top: "0px",
				bottom: "50px",
				titleNView: {                       // 窗口的标题栏控件
			      titleText:"首页",                // 标题栏文字,当不设置此属性时，默认加载当前页面的标题，并自动更新页面的标题
			      titleColor:"#fff",             // 字体颜色,颜色值格式为"#RRGGBB",默认值为"#000000"
			      titleSize:"17px",                 // 字体大小,默认17px
			      backgroundColor:"#FE532A",        // 控件背景颜色,颜色值格式为"#RRGGBB",默认值为"#F7F7F7"
		      	  // type:'transparent',//透明渐变样式
			    }
			}
		},{
			id: "sala",
			url: "html/sala.html",
			style: {
				top: "0px",
				bottom: "50px",
				titleNView: {                       // 窗口的标题栏控件
			      titleText:"游戏大厅",                // 标题栏文字,当不设置此属性时，默认加载当前页面的标题，并自动更新页面的标题
			      titleColor:"#fff",             // 字体颜色,颜色值格式为"#RRGGBB",默认值为"#000000"
			      titleSize:"17px",                 // 字体大小,默认17px
			      backgroundColor:"#FE532A",        // 控件背景颜色,颜色值格式为"#RRGGBB",默认值为"#F7F7F7"

			    }
			}
		},{
			id: "room",
			url: "html/room.html",
			style: {
				top: "0px",
				bottom: "50px",
				titleNView: {                       // 窗口的标题栏控件
			      titleText:"游戏房间",                // 标题栏文字,当不设置此属性时，默认加载当前页面的标题，并自动更新页面的标题
			      titleColor:"#fff",             // 字体颜色,颜色值格式为"#RRGGBB",默认值为"#000000"
			      titleSize:"17px",                 // 字体大小,默认17px
			      backgroundColor:"#FE532A",        // 控件背景颜色,颜色值格式为"#RRGGBB",默认值为"#F7F7F7"

			    }
			}
		},{
			id: "my",
			url: "html/my.html",
			style: {
				top: "0px",
				bottom: "50px"
			}
		}]
	}
	
})()
