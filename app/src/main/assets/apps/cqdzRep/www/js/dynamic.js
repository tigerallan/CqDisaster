var arrDisplay = [];   //保存已读新闻的id
var contentTwo = "";
var result = [];
var pic_imgs = new Array();
var pic_img_index = 0;
var contentThree = "";
//天气
var tq = "";
$.ajax({
	url: ip + "getWeather.do",
	type: "get",
	async: false, //同步请求	
	dataType: "json",
	success: function(json) {
		var data = json.data;
		var status1 = data.status1;
		var src = '';
		if(status1.indexOf("阴") >= 0) {
			src = "img/yin.png";
		} else if(status1.indexOf("晴") >= 0) {
			src = "img/qing.png";
		} else if(status1.indexOf("多云") >= 0) {
			src = "img/duoyun.png";
		} else if(status1.indexOf("雨") >= 0) {
			src = "img/yu.png";
		}
		tq +=
			'<div>' +
			'<div class="weathertext">' +
			'<img src="' + src + '" />' +
			'<span class="wd">' + data.temperature2 + '°C~' + data.temperature1 + '°C</span>' +
			'<span class="tq">' + data.status1 + '</span>' +
			'</div>' +
			'</div>' +
			'<div>' +
			'<div class="weathertext">' +
			'<div style="font-size:20px;margin-top:-12px;margin-left:-12px;">重庆</div>' +
			'</div>' +
			'</div>';
		$('#tq').html(tq);
	}
});

//新闻图片轮播请求
$.ajax({
	url: ip + "getNew.do",
	async: false, //同步请求	
	type: "get",
	dataType: "json",
	success: function(json) {
		var data = json;
		for(var i = 0; i < data.length; i++) {
			if(data[i].sfPic == 1) {
				pic_img_index++;
				if(pic_img_index <= 5) {
					pic_imgs.push(data[i]);
				} else {
					break;
				}
			}
		}
		contentThree +=
			'<div class="mui-slider-item mui-slider-item-duplicate">' +
			'<a class="fishref" href="javascript:void(0);" data-id="' + pic_imgs[4].id + '">' +
			'<img src="http://' + pic_imgs[4]['pic'][0] + '">' +
			'<p class="mui-slider-title"><span>' + pic_imgs[4].title + '</span></p>' +
			'</a></div>' +
			'<div class="mui-slider-item">' +
			'<a class="fishref" href="javascript:void(0);" data-id="' + pic_imgs[0].id + '">' +
			'<img src="http://' + pic_imgs[0]['pic'][0] + '">' +
			'<p class="mui-slider-title"><span>' + pic_imgs[0].title + '</span></p>' +
			'</a></div>' +
			'<div class="mui-slider-item">' +
			'<a class="fishref" href="javascript:void(0);" data-id="' + pic_imgs[1].id + '">' +
			'<img src="http://' + pic_imgs[1]['pic'][0] + '">' +
			'<p class="mui-slider-title"><span>' + pic_imgs[1].title + '</span></p>' +
			'</a></div>' +
			'<div class="mui-slider-item">' +
			'<a class="fishref" href="javascript:void(0);" data-id="' + pic_imgs[2].id + '">' +
			'<img src="http://' + pic_imgs[2]['pic'][0] + '">' +
			'<p class="mui-slider-title"><span>' + pic_imgs[2].title + '</span></p>' +
			'</a></div>' +
			'<div class="mui-slider-item">' +
			'<a class="fishref" href="javascript:void(0);" data-id="' + pic_imgs[3].id + '">' +
			'<img src="http://' + pic_imgs[3]['pic'][0] + '">' +
			'<p class="mui-slider-title"><span>' + pic_imgs[3].title + '</span></p>' +
			'</a></div>' +
			'<div class="mui-slider-item">' +
			'<a class="fishref" href="javascript:void(0);" data-id="' + pic_imgs[4].id + '">' +
			'<img src="http://' + pic_imgs[4]['pic'][0] + '">' +
			'<p class="mui-slider-title"><span>' + pic_imgs[4].title + '</span></p>' +
			'</a></div>' +
			'<div class="mui-slider-item mui-slider-item-duplicate">' +
			'<a class="fishref" href="javascript:void(0); data-id="' + pic_imgs[0].id + '"">' +
			'<img src="http://' + pic_imgs[0] + '">' +
			'<p class="mui-slider-title"><span>' + pic_imgs[0].title + '</span></p>' +
			'</a></div>';
		$('#xwlb').html(contentThree);
	}
});

//新闻列表

$.ajax({
	url: ip + "getNew.do",
	async: false, //同步请求	
	type: "get",
	dataType: "json",
	success: function(json) {
		var data = json;
//		var b = localStorage.getItem("idPlay");
//		console.log(b);
		for(var i = 0; i < data.length; i++) {
			//是否有重复值  否 表示没有重复值
			var isDR = false;
			for(var j = 0; j < pic_imgs.length; j++) {
				if(data[i].id == pic_imgs[j].id) {
					isDR = true;
					break;
				}
			}
			if(!isDR) {
				result.push(data[i]);
			}
		}
		for(var i = 0; i < result.length; i++) {
			var pic0 = result[i]['pic'][0];
			var pic1 = result[i]['pic'][1];
			var pic2 = result[i]['pic'][2];
			var str0 = result[i]['update_time'];
			str0 = str0.substring(0, 10);
			
			if(pic0 == null) {
				contentTwo += '<li class="mui-table-view-cell mui-media">' +
					'<a class="fishref" href="javascript:;" data-id="' + result[i].id + '">' +
					'<div class="mui-media-body">' +
					'<div class="mui-ellipsis">' + result[i].title + '</div>' +
					'</div>' +
					'<div class="place">' +
					'<span>' + result[i].unit_name + '</span>' +
					'<span>' + str0 + '</span></div></a></li>';
			} else if(pic1 == null) {
				contentTwo += '<li class="mui-table-view-cell mui-media">' +
					'<a class="fishref" href="javascript:;" data-id="' + result[i].id + '">' +
					'<img class="mui-media-object mui-pull-right" src="http://' + pic0 + '">' +
					'<div class="mui-media-body">' +
					'<div class="mui-ellipsis height">' + result[i].title + '</div>' +
					'</div>' +
					'<div class="place">' +
					'<span>' + result[i].unit_name + '</span>' +
					'<span>' + str0 + '</span></div></a></li>';
			} else if(pic2 == null) {
				contentTwo += '<li class="mui-table-view-cell mui-media">' +
					'<a class="fishref" href="javascript:;" data-id="' + result[i].id + '">' +
					'<div class="mui-media-body">' +
					'<div class="mui-ellipsis">' + result[i].title + '</div>' +
					'</div><div class="dynamic_imgOne">' +
					'<img src="http://' + pic0 + '">' +
					'</div><div class="place">' +
					'<span>' + result[i].unit_name + '</span>' +
					'<span>' + str0 + '</span></div></a></li>';
			} else {
				contentTwo += '<li class="mui-table-view-cell mui-media">' +
					'<a class="fishref" href="javascript:;" data-id="' + result[i].id + '">' +
					'<div class="mui-media-body">' +
					'<div class="mui-ellipsis">' + result[i].title + '</div>' +
					'</div><div class="dynamic_imgThree">' +
					'<img class="pic0" src="http://' + pic0 + '">' +
					'<img class="pic1" src="http://' + pic1 + '">' +
					'<img class="pic2" src="http://' + pic2 + '">' +
					'</div><div class="place">' +
					'<span>' + result[i].unit_name + '</span>' +
					'<span>' + str0 + '</span></div></a></li>';
			}
		}
		$('#dynamic_sj').html(contentTwo);
		fishrefClick();
	}
});

//点击每条新闻
function fishrefClick() {
	var fishref = document.getElementsByClassName('fishref');
	var count = fishref.length;
	for(i = 0; i < count; i++) {
		fishref[i].addEventListener('tap', function() {
			//		var ellipsis = $(this).find(".mui-ellipsis");
			//		ellipsis.css("color", "#808080");
			var id = this.getAttribute("data-id");
			arrDisplay.push(id);
			localStorage.setItem("idPlay", arrDisplay);
			console.log(localStorage);
			mui.openWindow({
				url: 'html/dynamicTab/newscon.html?id=' + id,
				id: 'newscon',
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