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
		var url = "site=" + $("#site").val() + "&start_date=" + $("#start_date").val() + "&end_date=" + $("#end_date").val()+ "&company=" +company;
		window.location.href = _basePath + "/rptMonthTank/exportExcel?" + url;
	});
	$("#clearAll").click(function () {
		$("#start_date").attr("value","");
		$("#end_date").attr("value","");
		$("#site").attr("value","");
	});
	$("#findBut").click(function () {
		reloadRecord();
	});
});
var page = {page: 1,limit: 10};
function reload(){
	getSite();
	reloadRecord();
}

function reloadRecord(){
	var url = _basePath + "/rptMonthTank/getPages";
	page.start_date = $("#start_date").val();
	page.end_date = $("#end_date").val();
	page.company = company;
	page.site = $("#site").val();
	$.post(url, page, function(res, status) {
		$("#recordBody").empty();
		renderPage("pageUL",page,res.total,reloadRecord);
		var data = res.data;
		if(data != null && data.length > 0){
			var strs = "";
			 $.each(data,function(i,item){
				 strs += "<tr><td nowrap>" + formatNull(item.companyname) +"</td><td>" + formatNull(item.sitename) + "</td><td>" + formatNull(item.cycle) + "</td><td>" + formatNull(item.sumfilling) + "</td><td>"
				 + formatNull(item.sumflow) + "</td><td>"+ formatNull(item.cardflow) + "</td><td>"+ formatNull(item.gasrate) + "</td><td>"+ formatNull(item.onlinerate) + "</td></tr>";
			 });
			 $("#recordBody").append(strs);
		 }
	});
}

function getSite(){
	var url = _basePath + "/rptMonthTank/getSite?company="+company;
	$.get(url, function(data, status){
			 var strs ="";
			 $("#site").empty();
			 strs +="<option value=''>请选择站点……</option>";
			 $.each(data,function(i,item){
				 strs +="<option value='"+ item.id +"'>"+item.name+"</option>";
			 });
		 $("#site").append(strs);
		 $("#site").trigger("liszt:updated");
	 });
}