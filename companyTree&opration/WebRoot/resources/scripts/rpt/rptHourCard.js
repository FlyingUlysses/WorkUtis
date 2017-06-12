var company ="";
$(function() {
	if($("#jstree").length)
		buildCompJsTree();
	else
		reload();
	$("#export").click(function () {
		var start_date = $("#start_date").val();
		var end_date = $("#end_date").val();
		var tank_id = $("#site_id").val();
		var showState =$("#showState").val();
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
		var url = "site=" + $("#site_id").val() + "&start_date=" + $("#start_date").val() + "&end_date=" + $("#end_date").val()+"&showState=" + $("#showState").val()+"&company="+company;
		window.location.href = _basePath + "/rptHourCard/exportExcel?" + url;
	});
	$("#clearAll").click(function () {
		$("#start_date").attr("value","");
		$("#end_date").attr("value","");
		$("#site_id").attr("value","");
		page.page = 1;
		loadFirst();
	});
	$("#findBut").click(function () {
		loadFirst();
	});
});
var page = {page: 1,limit: 12};
function reload(){
	loadFirst();
	getSite();
}

function loadFirst(){
	var url = _basePath + "/rptHourCard/getPages";
	page.start_date = $("#start_date").val();
	page.end_date = $("#end_date").val();
	page.site = $("#site_id").val();
	page.showState =$("#showState").val();
	page.company = company;
	$.post(url, page, function(res, status) {
		$("#statBody").empty();
		renderPage("pageUL",page,res.total);
		var data = res.data;
		if(data != null && data.length > 0){
			var strs = "";
			 $.each(data,function(i,item){
				 strs += "<tr><td nowrap>" + formatNull(item.name) + "</td><td>" + formatNull(item.newdate) +"</td><td>" + formatNull(item.card_gqzl) + "</td><td>" + formatNull(item.card_syql)
				 	  + "</td><td>" + formatNull(item.card_yqzl) + "</td><td>" + formatNull(item.gas_used) + "</td><td>" + formatNull(item.total_used) + "</td></tr>";
			 });
			 $("#statBody").append(strs);
		 }
	});
}

function getSite(){
	var url = _basePath + "/rptHourCard/getSite?company="+company;
	$.get(url, function(data, status){
			 var strs ="";
			 $("#site_id").empty();
			 strs +="<option value=''>请选择站点……</option>";
			 $.each(data,function(i,item){
				 strs +="<option value='"+ item.id +"'>"+item.name+"</option>";
			 });
		 $("#site_id").append(strs);
		 $("#site_id").trigger("liszt:updated");
	 });
}

