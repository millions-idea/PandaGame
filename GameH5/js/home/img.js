function imgLoad(img, callback) {
	var timer = setInterval(function() {
		if (img.complete) {
			callback(img)
			clearInterval(timer)
		}
	}, 50)
}


imgLoad(img1, function() {
	p1.innerHTML('加载完毕')
})