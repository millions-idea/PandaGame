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
		
		
		// 页面资源加载完成
		plus.webview.currentWebview().addEventListener("show", function(){
			// 生成邀请码(固定000+用户id)
			var session = JSON.parse(plus.storage.getItem('userInfo'));
			doc.getElementById("code").textContent = "000" + session.userId;
			
			var url = window.config.invite + "invite?code=" + "000" + session.userId;
			invite.getMerchantQRCode({
				content: url
			},function(data){
				doc.getElementById("copy").src = 
					window.config.invite  + "api/user/getMerchantQRCodeImage?content=" + url;
				console.log(doc.getElementById("copy").src)
			});
		})
		
		
		// 复制链接
		doc.getElementById("copy").addEventListener("tap", function(){
			var code = doc.getElementById("code").textContent;
			if(code == '...') return plus.nativeUI.alert("邀请码正在努力生成中……");
			copyTo(window.config.invite + "invite?code=" + code);
			plus.nativeUI.alert("复制链接成功,二维码已经保存到相册,快去发给朋友吧~");
			
			var url = window.config.invite + "invite?code=" + code;
			var dtask = plus.downloader.createDownload(
				window.config.invite  + "api/user/getMerchantQRCodeImage?content=" + url, 
				{},
				function(d, status) {
					// 下载完成
					if(status == 200) {
						plus.nativeUI.closeWaiting();
						console.log("Download success: " + d.filename);

						plus.gallery.save( d.filename, function () {
							plus.nativeUI.toast( "保存二维码成功");
						});

					} else {
						plus.nativeUI.closeWaiting();
						console.log("Download failed: " + status);
					}
				});
			dtask.start();
			
			
		})
		
	});
}(mui, document));

