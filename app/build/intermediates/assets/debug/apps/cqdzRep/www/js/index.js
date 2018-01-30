function nav() {
	$('#nav>a').on('tap', function() {
		$(this).children().css('color', '#0075a9');
		$(this).siblings().children().css('color', '#666666');
		var n = $(this).index();
		if(n == 0) {
			var home = plus.webview.getWebviewById('dynamic.html');
			mui.fire(home, 'home');
		} else if(n == 1) {
			var category = plus.webview.getWebviewById('stats.html');
			mui.fire(category, 'category');
		} else if(n == 2) {
			var cart = plus.webview.getWebviewById('work.html')
			mui.fire(cart, 'cart');
		} else if(n == 3) {
			var me = plus.webview.getWebviewById('contingency.html')
			mui.fire(me, 'me');
		}
		if(n == '0') {
			$('.a1').attr('src', 'img/dongtai 2.png');
			$('.a2').attr('src', 'img/tongji1.png');
			$('.a3').attr('src', 'img/yingji1.png');
			$('.a4').attr('src', 'img/work1.png');
		}
		if(n == '1') {
			$('.a1').attr('src', 'img/dongtai 1.png');
			$('.a2').attr('src', 'img/tongji2.png');
			$('.a3').attr('src', 'img/yingji1.png');
			$('.a4').attr('src', 'img/work1.png');
		}
		if(n == '2') {
			$('.a1').attr('src', 'img/dongtai 1.png');
			$('.a2').attr('src', 'img/tongji1.png');
			$('.a3').attr('src', 'img/yingji2.png');
			$('.a4').attr('src', 'img/work1.png');
		}
		if(n == '3') {
			$('.a1').attr('src', 'img/dongtai 1.png');
			$('.a2').attr('src', 'img/tongji1.png');
			$('.a3').attr('src', 'img/yingji1.png');
			$('.a4').attr('src', 'img/work2.png');
		}
	});
}