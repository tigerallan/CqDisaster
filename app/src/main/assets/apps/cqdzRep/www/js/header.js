
function myfun() {
	var immersed = 0;
	var ms = (/Html5Plus\/.+\s\(.*(Immersed\/(\d+\.?\d*).*)\)/gi)
		.exec(navigator.userAgent);
	if(ms && ms.length >= 3) { // 当前环境为沉浸式状态栏模式
		immersed = parseFloat(ms[2]); // 获取状态栏的高度
	}
//	immersed = 18;
	
	h('body').animate({
		opacity: 1
	}, 100);

	var t = document.getElementById('header'),
		tHeight = t.offsetHeight;
	t && (t.style.paddingTop = immersed + 'px',
		t.style.height = tHeight + immersed + 'px',
		h('.mui-content').css({
			paddingTop: parseInt(getComputedStyle(h('.mui-content').dom[0], null)
				.paddingTop) + immersed + 1 + 'px'
		})
	); 
}

window.onload=myfun;