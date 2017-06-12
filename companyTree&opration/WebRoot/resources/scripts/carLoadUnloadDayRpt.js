var invl = null;
var company ="";
$(function() {
	if($("#jstree").length)
		buildCompJsTree();
	else
		reload();
	$("#rt_export").click(function () {
		var start_date = $("#start_date").val();
		var end_date = $("#end_date").val();
		if(start_date == null || start_date == ""){
			showMsgModal("开始时间不能为空!");
			return;
		}
		if(end_date == null || end_date == ""){
			showMsgModal("截止时间不能为空!");
			return;
		}
		if(end_date < start_date){
			showMsgModal("截止时间不能早于开始时间!");
			return;
		}
		var param = "carid=" + $("#carid").val() + "&start_date=" + start_date + "&end_date=" + end_date;
		var url =  _basePath + "/carUnload/exportUnloadDateExcel?" + param;
		window.location.href = url;
	});
	$("#rt_export2").click(function () {
		var start_date = $("#start_date").val();
		var end_date = $("#end_date").val();
		if(start_date == null || start_date == ""){
			showMsgModal("开始时间不能为空!");
			return;
		}
		if(end_date == null || end_date == ""){
			showMsgModal("截止时间不能为空!");
			return;
		}
		if(end_date < start_date){
			showMsgModal("截止时间不能早于开始时间!");
			return;
		}
		var param = "carid=" + $("#carid").val() + "&start_date=" + start_date + "&end_date=" + end_date+"&tankid="+$("#tankid").val()+"&company="+company;
		var url =  _basePath + "/carUnload/exportExcelByTank?" + param;
		window.location.href = url;
	});
	$("#clearAll").click(function () {
		$("#start_date").attr("value","");
		$("#end_date").attr("value","");
		$("#carid").attr("value","");
		$("#tankid").attr("value","");
	});
	$("#findBut").click(function () {
		reloadRecord();
	});
});

var page = {page: 1,limit: 12};
function reload(){
	page.page = 1;
	getSite();
	$("#recordBody").empty();
	reloadRecord();
}

function reloadRecord(){
	var url = _basePath + "/carUnload/getLogPages";
	page.start_date = $("#start_date").val();
	page.end_date = $("#end_date").val();
	page.carid = $("#carid").val();
	page.tankid = $("#tankid").val();
	page.company = company;
	$.post(url, page, function(res, status) {
		$("#recordBody").empty();
		renderPage("pageUL",page,res.total,reloadRecord);
		var data = res.data;
		if(data != null && data.length > 0){
			var strs = "";
			 $.each(data,function(i,item){
				 var rowSpan = item.cnt;
				 var rowHtml = "";
				 var firstRow = "<td></td><td></td><td></td><td></td>";
				 $.each(item.subs,function(j,sub){
					 var row = "<td>" + sub.name + "</td><td>" + sub.arrive_time + " ~ " + sub.leave_time
					 		 + "</td><td>" + sub.fill_sum + "</td>";
					 if(j == 0){
						 firstRow = row;
					 }else{
						 rowHtml += "<tr>" + row + "</tr>";
					 }
				 });
				 strs += "<tr><td rowspan='" + rowSpan + "'>" + formatNull(item.stat_date) + "</td><td rowspan='" + rowSpan + "'>" + formatNull(item.plate_number) + "</td><td rowspan='" + rowSpan + "'>" + formatNull(item.driver) + "</td>" 
				 	       + firstRow + "<td rowspan='" + rowSpan + "'>" + formatNull(item.fill_sum) + "</td></tr>" + rowHtml;
			 });
			 $("#recordBody").append(strs);
		 }
	});
}

function getSite(){
	var url = _basePath + "/carUnload/getSite?company="+company;
	$.get(url, function(data, status){
			 var strs ="";
			 $("#tankid").empty();
			 strs +="<option value=''>请选择站点……</option>";
			 $.each(data,function(i,item){
				 strs +="<option value='"+ item.id +"'>"+item.name+"</option>";
			 });
		 $("#tankid").append(strs);
		 $("#tankid").trigger("liszt:updated");
	 });
}
