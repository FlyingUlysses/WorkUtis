var company = "";
var page = { page: 1,limit: 9 };
$(function() {
	if($("#jstree").length){
		buildCompJsTree();
	}else{
		reload();
	}
	
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
		var url = "sitename=" + $("#sitename").val() + "&start_date=" + $("#start_date").val() + "&end_date=" + $("#end_date").val()+ "&company=" +company + "&faulttype=" +$("#faulttype").val();
		window.location.href = _basePath + "/operation/exportExcel?" + url;
	});
	
	$("#rtAll_export").click(function () {
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
		var url = "sitename=" + $("#sitename").val() + "&start_date=" + $("#start_date").val() + "&end_date=" + $("#end_date").val()+ "&company=" +company + "&faulttype=" +$("#faulttype").val();
		window.location.href = _basePath + "/operation/exportAllExcel?" + url;
	});
	
	$("#search").click(function () {
		reload();
	});
	$("#add").click(function () {
		showModel({
			title : "运维新增",
			width : "700px",
			height : "510px",
			url : _basePath + "/operation/edit?company=" + company
		});
	});
});

function reload(){
	var url = _basePath + "/operation/getPages";
	page.faulttype = $("#faulttype").val();
	page.subtype = $("#subtype").val();
	page.sitename = $("#sitename").val();
	page.start_date = $("#start_date").val();
	page.end_date = $("#end_date").val();
	page.company = company;
	$.post(url, page, function(res, status) {
		$("#rowBody").empty();
		renderPage("pageUL",page,res.total);
		var data = res.data;
		if(data != null && data.length > 0){
			 var strs = "";
			 $.each(data,function(i,item){
				 strs += "<tr onclick='rowClick(" + item.id + ");'>"
					 + "<td style='text-align: center;'><input class='checkboxes' name='rowRadio' type='radio' value='" + item.id + "' /></td>"
					 + "<td style='vertical-align: middle;'>" + item.name + "</td><td style='vertical-align: middle;'>" + item.faulttypename + "</td>"
					 + "<td style='vertical-align: middle;'>" + formatNull(item.subtypename) + "</td>"
					 + "<td style='vertical-align: middle;'>" + formatNull(item.changedevname) + "</td>"
					 + "<td style='vertical-align: middle;'>" + formatNull(item.occurdtime) + "</td>"
					 + "<td style='vertical-align: middle;'>" + formatNull(item.solvetime) + "</td>"
					 + "<td style='text-align: center;'>"
					 + 		"<button style='padding: 1px 12px;' class='btn btn-primary' onclick='editOperation(" + item.id + ");'><i class='icon-pencil'></i></button>&nbsp;&nbsp;"
					 + 		"<button id='rvBtn_" + item.id + "' style='padding: 1px 12px;' class='btn btn-danger ladda-button' data-style='zoom-in'"
					 + 			" onclick='revRecord(\"" + _basePath + "/operation/delete?id=" + item.id + "\",\"rvBtn_" + item.id + "\");'><i class='icon-trash'></i></button></td></tr>";
			 });
			 $("#rowBody").append(strs);
		 }
	});
}

function editOperation(id){
	showModel({
		title : "运维编辑",
		width : "700px",
		height : "510px",
		url : _basePath + "/operation/edit?id=" + id
	});
}

function rowClick(id){
	$("input[name='rowRadio'][value='" + id + "']").attr("checked",'checked'); ;
}