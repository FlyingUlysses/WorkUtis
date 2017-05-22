var page = { page: 1,limit: 10,device: _device };
$(function() {
	reload();
	$("#search").click(function () {
		page.page = 1;
		reload();
	});
	
});

function reload(){
	var url = _basePath + "/device/getLogPage";
	page.start_date = $("#start_date").val();
	page.end_date = $("#end_date").val();
	var loadding = layer.load(1, { shade: [0.1,'#fff'] });
	$.get(url, page, function(res, status) {
		$("#rowBody").empty();
		layer.close(loadding);
		renderPage("pageUL",page,res.total);
		var data = res.data;
		if(data != null && data.length > 0){
			$.each(data,function(i,item){
				var html = "<tr>"
						+ 	"<td nowrap='nowrap'>" + item.off_time + "</td>"
						+ 	"<td nowrap='nowrap'>" + formatNull(item.on_time) + "</td>"
						+ 	"<td nowrap='nowrap'>" + Utils.transTime(item.off_seconds) + "</td>"
						+ "</tr>";
				$("#rowBody").append(html);
			});
			getSumOffTime();
		}
	});
}

//往表格后添加统计掉线时间和掉线比率
function getSumOffTime(){
	var url = _basePath + "/device/getSumOffTime";
	var start_date = $("#start_date").val();
	var end_date = $("#end_date").val();
	$.get(url, page, function(data, status) {
		if(data != null){
			var html = "<span>总离线："+Utils.transTime(formatNull(data.sum_off_seconds))+"</span><br><span>在线率为："+data.off_rate;
		$("#rowBody").append(html);
		}
	});
}
