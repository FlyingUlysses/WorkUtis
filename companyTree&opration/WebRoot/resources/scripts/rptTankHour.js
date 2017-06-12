var company ="";
$(function() {
	if($("#jstree").length)
		buildCompJsTree();
	else
		reload();
	
	$("#export").click(function () {
		var start_date = $("#start_date").val();
		var end_date = $("#end_date").val();
		var tank_id = $("#tank_id").val();
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
		var url = "tank=" + $("#tank_id").val() + "&start_date=" + $("#start_date").val() + "&end_date=" + $("#end_date").val()+"&company="+company;
		window.location.href = _basePath + "/hourRpt/exportExcel?" + url;
	});
	$("#clearAll").click(function () {
		$("#start_date").attr("value","");
		$("#end_date").attr("value","");
		$("#tank_id").attr("value","");
	});
	$("#findBut").click(function () {
		reloadFirst();
	});
});

var page = {page: 1,limit: 12};
function reload(){
	getSite();
	reloadFirst();
}

function reloadFirst(){
	var url = _basePath + "/hourRpt/getPages";
	page.start_date = $("#start_date").val();
	page.end_date = $("#end_date").val();
	page.tank = $("#tank_id").val();
	page.company=company;
	$.post(url, page, function(res, status) {
		$("#statBody").empty();
		renderPage("pageUL",page,res.total);
		var data = res.data;
		if(data != null && data.length > 0){
			var strs = "";
			 $.each(data,function(i,item){
				 strs += "<tr><td nowrap>" + formatNull(item.datetime) + "</td><td>" + item.name + "</td><td>" + formatNull(item.scales) + "</td><td>" + formatNull(item.pressure)
				 	  + "</td><td>" + formatNull(item.weight) + "</td><td>" + formatNull(item.ent_temp) + "</td><td>" + formatNull(item.ent_press)
				 	  + "</td><td>" + formatNull(item.ext_temp) + "</td><td>" + formatNull(item.ext_press_1) + "</td><td>" + formatNull(item.flow_bk) + "</td><td>" + formatNull(item.ext_flow) + "</td><td>" + formatNull(item.gas_used)
				 	  + "</td></tr>";
			 });
			 $("#statBody").append(strs);
		 }
	});
}

function getSite(){
	var url = _basePath + "/hourRpt/getSite?company="+company;
	$.get(url, function(data, status){
			 var strs ="";
			 $("#tank_id").empty();
			 strs +="<option value=''>请选择站点……</option>";
			 $.each(data,function(i,item){
				 strs +="<option value='"+ item.id +"'>"+item.name+"</option>";
			 });
		 $("#tank_id").append(strs);
		 $("#tank_id").trigger("liszt:updated");
	 });
}
