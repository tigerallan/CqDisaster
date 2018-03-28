//主界面和侧滑菜单界面均支持区域滚动；
mui('.mui-scroll-wrapper').scroll();

function Request(strName) {
	var strHref = location.href;
	var intPos = strHref.indexOf("?");
	var strRight = strHref.substr(intPos + 1);
	var arrTmp = strRight.split("&");
	for(var i = 0; i < arrTmp.length; i++) {
		var arrTemp = arrTmp[i].split("=");
		if(arrTemp[0].toUpperCase() == strName.toUpperCase())
			return arrTemp[1];
	}
	return "";
}

var areacode = Request('id');
mui('#gjdj').on('tap', '.checkborder', function() {
	var spans = document.getElementById("gjdj").querySelectorAll(".checkborder");
	if(this.style.border == '1px solid red') {
		this.style.border = '';
	} else {
		Array.prototype.forEach.call(spans, function(e, i, a) {
			e.style.border = '';
		});
		this.style.border = '1px solid red';
	}
});
mui('#zhlx').on('tap', '.checkborder', function() {
	var spans = document.getElementById("zhlx").querySelectorAll(".checkborder");
	if(this.style.border == '1px solid red') {
		this.style.border = '';
	} else {
		Array.prototype.forEach.call(spans, function(e, i, a) {
			e.style.border = '';
		});
		this.style.border = '1px solid red';
		document.activeElement.blur();
	}
});

//灾害类型
//			var content_zhlx = "";
//			$.ajax({
//				url: ip + "tea/findStaticName.do?staticName=灾害类型",
//				async: false, //同步请求	
//				type: "get",
//				dataType: "json",
//				success: function(json) {
//					var data = json.data;
//					content_zhlx +=
//						'<span class="celltitle">灾害类型</span>' +
//						'<div class="boxflex">' +
//						'<span id=' + data[0].id + ' class="checkborder">' + data[0].static_name + '</span>' +
//						'<span id=' + data[1].id + ' class="checkborder">' + data[1].static_name + '</span>' +
//						'<span id=' + data[2].id + ' class="checkborder">' + data[2].static_name + '</span>' +
//						'</div>' +
//						'<div class="boxflex">' +
//						'<span id=' + data[3].id + ' class="checkborder">' + data[3].static_name + '</span>' +
//						'<span id=' + data[4].id + ' class="checkborder">' + data[4].static_name + '</span>' +
//						'<span id=' + data[5].id + ' class="checkborder">' + data[5].static_name + '</span>' +
//						'</div>' +
//						'<div class="boxflex">' +
//						'<span id=' + data[6].id + ' class="checkborder">' + data[6].static_name + '</span>' +
//						'<span id=' + data[7].id + ' class="checkborder">' + data[7].static_name + '</span>' +
//						'<span id=' + data[8].id + ' class="checkborder">' + data[9].static_name + '</span>' +
//						'</div>' +
//						'<div class="boxflex">' +
//						'<span id=' + data[9].id + ' class="checkborder">' + data[8].static_name + '</span>' +
//						'</div>';
//					$('#zhlx').html(content_zhlx);
//				}
//			});

//读取系统日期
var d = new Date();
var n = d.getFullYear();
var t = d.getMonth() + 1;
var m = d.getDate();
//设置结束时间
(function($, doc) {
	$.init();
	var result = document.getElementById("end_time");
	if(t < 10) {
		t = "0" + t;
	}
	if(m < 10) {
		m = "0" + m;
	}
	$.ready(function() {
		//					var choose_two = document.getElementById('end_time');
		result.addEventListener('tap', function(event) {
			var id = this.getAttribute('id');
			var picker = new $.DtPicker({
				"type": "date",
				"beginDate": new Date("1970-01-01"),
				"endDate": new Date(n + "-" + t + "-" + m)
			});
			picker.show(function(rs) {
				result.innerText = rs.text;
				picker.dispose();
			});
		}, false);
	});
})(mui, document);

//设置开始时间
(function($, doc) {
	$.init();
	var result_two = document.getElementById("start_time");
	$.ready(function() {
		//		var choose_three = document.getElementById('start_time');
		result_two.addEventListener('tap', function(event) {
			var id = this.getAttribute('id');
			var picker = new $.DtPicker({
				"type": "date",
				"beginDate": new Date("1970-01-01"),
				"endDate": new Date(n + "-" + t + "-" + m),
				"value": n + "-" + t + "-01"
			});
			picker.show(function(rs) {
				result_two.innerText = rs.text;
				picker.dispose();
			});
		}, false);
	});
})(mui, document);
var offCanvasWrapper = mui('#offCanvasWrapper');
// 监听点击遮罩关闭事件
document.getElementById("backdrop").addEventListener('tap', function() {
	document.activeElement.blur();
	mui('#offCanvasSideScroll').scroll().scrollTo(0, 0);
});
//点击取消
document.getElementById('abolish').addEventListener('tap', function() {
	offCanvasWrapper.offCanvas('close');
	document.activeElement.blur();
	mui('#offCanvasSideScroll').scroll().scrollTo(0, 0);
	$('.checkborder').css('border', '');
});
//点击确定
document.getElementById('confirm').addEventListener('tap', function() {
	mui('#offCanvasSideScroll').scroll().scrollTo(0, 0); //让筛选条件回到页面顶点
	offCanvasWrapper.offCanvas('close'); //关闭遮罩层
	document.activeElement.blur(); //关闭输入法
});