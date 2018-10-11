function copyTo(copy_content){
	mui.plusReady(function(){
		//判断是安卓还是ios
		if(mui.os.ios){
			//ios
			var UIPasteboard = plus.ios.importClass("UIPasteboard");
		    var generalPasteboard = UIPasteboard.generalPasteboard();
		    //设置/获取文本内容:
		    generalPasteboard.plusCallMethod({
		        setValue:copy_content,
		        forPasteboardType: "public.utf8-plain-text"
		    });
		    generalPasteboard.plusCallMethod({
		        valueForPasteboardType: "public.utf8-plain-text"
		    });
		}else{
		//安卓
		  var context = plus.android.importClass("android.content.Context");
		  var main = plus.android.runtimeMainActivity();
		  var clip = main.getSystemService(context.CLIPBOARD_SERVICE);
		  plus.android.invoke(clip,"setText",copy_content);
		}
	});
}
(function($, doc) {
	$.init({
		statusBarStyle: "light",
		statusBarBackground: '#fe5a2f'
	});
	 
	
	$.plusReady(function() {
		
		// 设置顶部状态栏样式
		plus.navigator.setStatusBarStyle("light");
		plus.navigator.setStatusBarBackground("#FE532A");
		
		// 禁止屏幕翻转
		plus.screen.lockOrientation("portrait-primary");
		
		
		// 复制链接
		doc.getElementById("copy").addEventListener("tap", function(){
			var code = doc.getElementById("code").textContent;
			copyTo(code);
			plus.nativeUI.alert("复制成功,快去发给朋友吧~");
		})
		
	});
}(mui, document));

