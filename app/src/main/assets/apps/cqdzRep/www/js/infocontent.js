//区县专报请求		
var content = "";
$.ajax({
	url: ip + "getZhuangbao.do",
	async: false, //同步请求	
	type: "get",
	dataType: "json",
	success: function(json) {
		var data = json.data;
		for(var i = 0; i < data.length; i++) {
			var ng = data[i].ng;
			if (ng == 1) {
				content +=
				'<li class="mui-table-view-cell mui-media">' +
				'<a class="fishref" href="javascript:void(0);" data-ng="' + data[i].ng + '" data-id="' + data[i].id + '">' +
				'<div class="mui_wd">' +
				'<div class="mui_wdyd"></div></div>' +
				'<div class="mui-media-body">' +
				'<div class="mui-ellipsis">' + data[i].title + '</div>' +
				'</div>' +
				'<div class="place">' +
				'<span>' + data[i].county + '</span>' +
				'<span>' + data[i].time + '</span>' +
				'<span>' + data[i].dispose + '</span>' +
				'<span>' + data[i].berichten + '</span>' +
				'</div></a>' +
				'</li>';
			} else {
				content +=
				'<li class="mui-table-view-cell mui-media">' +
				'<a class="fishref" href="javascript:void(0);" data-ng="' + data[i].ng + '" data-id="' + data[i].id + '">' +
				'<div class="mui_wd">' +
				'</div>' +
				'<div class="mui-media-body">' +
				'<div class="mui-ellipsis">' + data[i].title + '</div>' +
				'</div>' +
				'<div class="place">' +
				'<span>' + data[i].county + '</span>' +
				'<span>' + data[i].time + '</span>' +
				'<span>' + data[i].dispose + '</span>' +
				'<span>' + data[i].berichten + '</span>' +
				'</div></a>' +
				'</li>';
			}
		}
		$('#dynamic_qx').html(content);
		count();
		mui.plusReady(function() {
			plus.nativeUI.closeWaiting();
			mui.currentWebview.show("slide-in-bottom", 300);
		});
	}
});
var cityData;
var myArray = new Array();
$.ajax({
	url: ip + "findAreaData.do",
	async: false,
	type: "get",
	dataType: "json",
	success: function(json) {
		function compare(property) {
			return function(a, b) {
				var value1 = a[property];
				var value2 = b[property];
				return value1 - value2;
			}
		}
		myArray = json.sort(compare('id'));
		cityData = myArray;
	}
});

//所属区县选项
(function($, doc) {
	$.init();
	$.ready(function() {
		var cityPicker = new $.PopPicker({
			layer: 1
		});
		cityPicker.setData(cityData);
		var choose_one = document.getElementById('choose_one');
		var dynamic_qx = document.getElementById('dynamic_qx');

		choose_one.addEventListener('tap', function(event) {
			cityPicker.show(function(items) {
				choose_one.innerText = items[0].text;
				var areaId = items[0].id;
				mui('#qx_news').scroll().scrollTo(0, 0);
				$.ajax({
					url: ip + "getZhuangbao.do",
					async: false, //同步请求	
					type: "get",
					data: {
						areaId: areaId
					},
					dataType: "json",
					success: function(json) {
						dynamic_qx.innerHTML = "";
						content = "";
						var data = json.data;
						for(var i = 0; i < data.length; i++) {
							var berichten = data[i].berichten;
							content +=
								'<li class="mui-table-view-cell mui-media">' +
								'<a class="fishref" href="javascript:void(0);" data-ng="' + data[i].ng + '" data-id="' + data[i].id + '">' +
								'<div class="mui_wd">' +
								'<div class="mui_wdyd"></div></div>' +
								'<div class="mui-media-body">' +
								'<div class="mui-ellipsis">' + data[i].title + '</div>' +
								'</div>' +
								'<div class="place">' +
								'<span>' + data[i].county + '</span>' +
								'<span>' + data[i].time + '</span>' +
								'<span>' + data[i].dispose + '</span>' +
								'</div></a>' +
								'</li>' +
								'</div></a>' +
								'</li>';
						}
						dynamic_qx.innerHTML = content;
						count();
					}
				});
			});
		}, false);
	});
})(mui, document);

//点击每条跳转到详情页
function count() {
	var fishref = document.getElementsByClassName('fishref');
	var count = fishref.length;
	for(i = 0; i < count; i++) {
		fishref[i].addEventListener('tap', function() {
			$(this).attr("data-ng", "0");
			var wdyd = $(this).find(".mui_wdyd");
			wdyd.css("visibility", "hidden");
			var id = this.getAttribute("data-id");
			mui.openWindow({
				url: 'html/dynamicTab/infocontent_xq.html?id=' + id,
				id: 'infocontent_xq.html',
				waiting: {
					autoShow: true,
					title: '正在加载...'
				},
				show: {
					autoShow: true,
					aniShow: 'slide-in-bottom'
				}
			});
		});
	}
}