window.game = {
	message: function(msg, beforeCallback, callback){
		var body = document.getElementsByTagName("body");
		if(body.length != 0){
			var modal = document.createElement("div");
			modal.className = "warning-modal";
			modal.id = "warning-modal";
			
			var titleLabel = document.createElement("span");
			titleLabel.className = "titleLabel";
			var titleBackground = document.createElement("span");
			titleBackground.className = "titleBackground";
			var closeButton = document.createElement("span");
			closeButton.className = "closeButton";
			closeButton.id = "closeButton";
			var textLabel = document.createElement("span");
			textLabel.className = "textLabel";
			textLabel.textContent = msg;
			modal.append(titleLabel);
			modal.append(titleBackground);
			modal.append(closeButton);
			modal.append(textLabel);
			
			body[0].append(modal);
			
			beforeCallback();
			
			document.getElementById("closeButton").addEventListener("tap", function(){
				callback();
				var msgModal = document.getElementById("warning-modal");
				msgModal.parentNode.removeChild(msgModal);
			}, false);
		}
	}
}
