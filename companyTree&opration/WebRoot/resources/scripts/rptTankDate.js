var page = {page: 1,limit: 10};
var company ="";
$(function() {
	if($("#jstree").length)
		buildCompJsTree();
	else
		reload();
	$("#export").click(function () {
		var url = "tank=" + $("#tank_id").val() + "&start_date=" + $("#start_date").val() + "&end_date=" + $("#end_date").val()+ "&company="+company;
		window.location.href = _basePath + "/dateRpt/exportExcel?" + url;
	});
	$("#exportAll").click(function () {
		var url = "tank=" + $("#tank_id").val() + "&start_date=" + $("#start_date").val() + "&end_date=" + $("#end_date").val()+"&company="+company;
		window.location.href = _basePath + "/dateRpt/exportAll?" + url;
	});
	$("#clearAll").click(function () {
		$("#start_date").attr("value","");
		$("#end_date").attr("value","");
		$("#tank_id").attr("value","");
	});
	$("#findBut").click(function () {
		reloadRecord();
	});
	$("#chart").click(function () {
		var url = _basePath + "/dateRpt/chart";
		top.addPanel("气化站运营日报图表", url);
	});
	$("#explain").click(function () {
		showModel({
			title : "报表导出数据说明",
			width : "800px",
			height: "370px",
			url : _basePath + "/dateRpt/explainDetail"
		});
	});
});

function reload(){
	getSite();
	$("#statBody").empty();
	reloadRecord();
}

function reloadRecord(){
	//<td style='text-align: center;'>" + formatNull(item.charge) + "</td>
	//<td style='text-align: center;'>" + formatNull(item.loss_weight) + "</td>
	//<td style='text-align: center;'>" + formatNull(item.loss_rate) + "</td>
	//<td style='text-align: center;'>" + formatNull(item.gas_rate) + "</td>
	var url = _basePath + "/dateRpt/getPages";
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
				 var sumUnLoad = "";
				 var unload = item.unload;
				 var tank_unload = item.tank_unload;
				 if(unload != null || tank_unload != null){
					 unload = unload == null? 0 : parseFloat(unload);
					 tank_unload = tank_unload == null? 0 : parseFloat(tank_unload);
					 sumUnLoad = unload + tank_unload;
				 }
				 strs += "<tr><td style='text-align: center;'>" + formatNull(item.stat_date) + " " +formatNull(item.stat_time) + "</td><td style='text-align: center;'>" + item.name + "</td>"
					 	   + "<td style='text-align: center;'>" + formatNull(item.scales) + "</td><td style='text-align: center;'>" + formatNull(item.weight) + "</td><td style='text-align: center;'>" + formatNull(item.weight_vary) + "</td>"
					 	   + "<td style='text-align: center;'>" + formatNull(sumUnLoad) + "</td><td style='text-align: center;'>" + formatNull(item.liquid_used) + "</td>"
					 	   + "<td style='text-align: center;'>" + formatNull(item.ext_flow) + "</td><td style='text-align: center;'>" + formatNull(item.gas_used) + "</td><td style='text-align: center;'>" + formatNull(item.other_used) + "</td>"
					 	   + "<td style='text-align: center;'>" + formatNull(item.sum_used) + "</td>"
					 	   + "</tr>";
			 });
			 $("#statBody").append(strs);
		 }
	});
}


function getSite(){
	var url = _basePath + "/dateRpt/getSite?company="+company;
	$.get(url, function(data, status){
			 var strs ="";
			 $("#tank_id").empty();
			 strs +="<option value=''>请选择站点……</option>";
			 $.each(data,function(i,item){
				 strs +="<option value='"+ item.id +"'>"+item.name+"</option>";
			 });
		 $("#tank_id").append(strs);
		 $("tank_id").trigger("liszt:updated");
	 });
}