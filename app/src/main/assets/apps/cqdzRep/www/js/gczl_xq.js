//单选
mui('#xmlb').on('tap', '.checkborder', function() {
	var spans = document.getElementById("xmlb").querySelectorAll(".checkborder");
	Array.prototype.forEach.call(spans, function(e, i, a) {
		e.style.border = '';
	});
	this.style.border = '1px solid red';
	document.activeElement.blur();
});
mui('#zhlx').on('tap', '.checkborder', function() {
	var spans = document.getElementById("zhlx").querySelectorAll(".checkborder");
	Array.prototype.forEach.call(spans, function(e, i, a) {
		e.style.border = '';
	});
	this.style.border = '1px solid red';
	document.activeElement.blur();
});
mui('#zhdj').on('tap', '.checkborder', function() {
	var spans = document.getElementById("zhdj").querySelectorAll(".checkborder");
	Array.prototype.forEach.call(spans, function(e, i, a) {
		e.style.border = '';
	});
	this.style.border = '1px solid red';
	document.activeElement.blur();
});
mui('#fzdj').on('tap', '.checkborder', function() {
	var spans = document.getElementById("fzdj").querySelectorAll(".checkborder");
	Array.prototype.forEach.call(spans, function(e, i, a) {
		e.style.border = '';
		document.activeElement.blur();
	});
	this.style.border = '1px solid red';
});
mui('#sfwg').on('tap', '.checkborder', function() {
	var spans = document.getElementById("sfwg").querySelectorAll(".checkborder");
	Array.prototype.forEach.call(spans, function(e, i, a) {
		e.style.border = '';
	});
	this.style.border = '1px solid red';
	document.activeElement.blur();
});

//项目类别
var content_xmlb = "";
$.ajax({
	url: gczl_ipconfig + "findStaticName.do?staticName=项目类别",
	async: false, //同步请求	
	type: "get",
	dataType: "json",
	success: function(json) {
		var data = json.data;
		content_xmlb +=
			'<span class="celltitle">项目类别</span>' +
			'<div class="boxflex">' +
			'<span id=' + data[3].id + ' class="checkborder">' + data[3].static_name + '</span>' +
			'<span id=' + data[1].id + ' class="checkborder">' + data[1].static_name + '</span>' +
			'</div>' +
			'<div class="boxflex">' +
			'<span id=' + data[4].id + ' class="checkborder">' + data[4].static_name + '</span>' +
			'</div>' +
			'<div class="boxflex">' +
			'<span id=' + data[0].id + ' class="checkborder">' + data[0].static_name + '</span>' +
			'</div>' +
			'<div class="boxflex">' +
			'<span id=' + data[2].id + ' class="checkborder">' + data[2].static_name + '</span>' +
			'</div>';
		$('#xmlb').html(content_xmlb);
	}
});
//灾害类型
var content_zhlx = "";
$.ajax({
	url: gczl_ipconfig + "findStaticName.do?staticName=灾害类型",
	async: false, //同步请求	
	type: "get",
	dataType: "json",
	success: function(json) {
		var data = json.data;
		content_zhlx +=
			'<span class="celltitle">灾害类型</span>' +
			'<div class="boxflex">' +
			'<span id=' + data[0].id + ' class="checkborder">' + data[0].static_name + '</span>' +
			'<span id=' + data[1].id + ' class="checkborder">' + data[1].static_name + '</span>' +
			'<span id=' + data[2].id + ' class="checkborder">' + data[2].static_name + '</span>' +
			'</div>' +
			'<div class="boxflex">' +
			'<span id=' + data[3].id + ' class="checkborder">' + data[3].static_name + '</span>' +
			'<span id=' + data[4].id + ' class="checkborder">' + data[4].static_name + '</span>' +
			'<span id=' + data[5].id + ' class="checkborder">' + data[5].static_name + '</span>' +
			'</div>' +
			'<div class="boxflex">' +
			'<span id=' + data[6].id + ' class="checkborder">' + data[6].static_name + '</span>' +
			'<span id=' + data[7].id + ' class="checkborder">' + data[7].static_name + '</span>' +
			'<span id=' + data[8].id + ' class="checkborder">' + data[9].static_name + '</span>' +
			'</div>' +
			'<div class="boxflex">' +
			'<span id=' + data[9].id + ' class="checkborder">' + data[8].static_name + '</span>' +
			'</div>';
		$('#zhlx').html(content_zhlx);
	}
});
//灾害等级
var content_zhdj = "";
$.ajax({
	url: gczl_ipconfig + "findStaticName.do?staticName=灾害等级",
	async: false, //同步请求	
	type: "get",
	dataType: "json",
	success: function(json) {
		var data = json.data;
		content_zhdj +=
			'<span class="celltitle">灾害等级</span>' +
			'<div class="boxflex">' +
			'<span id=' + data[0].id + ' class="checkborder">' + data[0].static_name + '</span>' +
			'<span id=' + data[1].id + ' class="checkborder">' + data[1].static_name + '</span>' +
			'<span id=' + data[2].id + ' class="checkborder">' + data[2].static_name + '</span>' +
			'<span id=' + data[3].id + ' class="checkborder">' + data[3].static_name + '</span>' +
			'</div>';
		$('#zhdj').html(content_zhdj);
	}
});
//防灾等级
var content_fzdj = "";
$.ajax({
	url: gczl_ipconfig + "findStaticName.do?staticName=防灾等级",
	async: false, //同步请求	
	type: "get",
	dataType: "json",
	success: function(json) {
		var data = json.data;
		content_fzdj +=
			'<span class="celltitle">防灾等级</span>' +
			'<div class="boxflex">' +
			'<span id=' + data[0].id + ' class="checkborder">' + data[0].static_name + '</span>' +
			'<span id=' + data[1].id + ' class="checkborder">' + data[1].static_name + '</span>' +
			'<span id=' + data[2].id + ' class="checkborder">' + data[2].static_name + '</span>' +
			'</div>';
		$('#fzdj').html(content_fzdj);
	}
});

var inserthtml = '';

function openme(arr) {
	var arrUrl = '';
	
	arrUrl = (arr.areaId?'&areaId='+arr.areaId:'')+
					 (arr.xmtype?'&xmtype='+arr.xmtype:'')+
					 (arr.sgdw?'&sgdw='+arr.sgdw:'')+
					 (arr.zhtype?'&zhtype='+arr.zhtype:'')+
					 (arr.zhlevel?'&zhlevel='+arr.zhlevel:'')+
					 (arr.fzlevel?'&fzlevel='+arr.fzlevel:'')+
					 (arr.stime?'&stime='+arr.stime:'')+
					 (arr.etime?'&etime='+arr.etime:'')+
					 (arr.sfwg?'&sfwg='+arr.sfwg:'')+
					 (arr.sxx?'&sxx='+arr.sxx:'')+
					 (arr.exx?'&exx='+arr.exx:'')+
					 (arr.xmmc?'&xmmc='+arr.xmmc:'');
					 
	arrUrl&&(arrUrl = '?' + arrUrl.slice(1));
	

	inserthtml = '<iframe src="table.html'+arrUrl+'" id="table" width="100%" height="100%" scrolling="auto" frameborder="0"></iframe>'
	$(".content_iframe").append(inserthtml);
	
	$('.checkborder').css('border','');
	$('#sgdw').val("");
	$('#sxx').val("");
	$('#exx').val("");
	$('#xmmc').val("");
	$('#start_time').html("");
	$('#end_time').html("");
}
window.onload = openme;

function leaveme() {
	$(".content_iframe :first-child").remove();
}