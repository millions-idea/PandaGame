window.game = {
	message: function(msg, beforeCallback, callback){
		var oldModal = document.getElementById("modal");
		if(oldModal != null) oldModal.parentNode.removeChild(oldModal);
		
		
		var body = document.getElementsByTagName("body");

		if(body.length != 0){
			var shade = document.createElement("div");
			shade.className = "modal-shade";
			shade.style.display = "block";
			shade.id = "shade";
			body[0].appendChild(shade);
			console.log("正常")
			
			
			var modal = document.createElement("div");
			modal.className = "modal";
			modal.id = "modal";
			
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
			modal.appendChild(titleLabel);
			modal.appendChild(titleBackground);
			modal.appendChild(closeButton);
			modal.appendChild(textLabel);
			
			body[0].appendChild(modal);
			
			beforeCallback();
			
			document.getElementById("closeButton").addEventListener("tap", function(){
				callback();
				var msgModal = document.getElementById("modal");
				var msgShade = document.getElementById("shade");
				msgModal.parentNode.removeChild(msgModal);
				msgShade.parentNode.removeChild(msgShade);
			}, false);
		}
	}
}
