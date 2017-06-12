var invl = null;
var company ="";
$(function() {
	if($("#jstree").length)
		buildCompJsTree();
	else
		reload();
	
	$("#clearAll").click(function () {
		$("#start_date").attr("value","");
		$("#site").attr("value","");
	});
	$("#findBut").click(function () {
		reload();
	});
});

var page = {page: 1,limit: 10};
function reload(){
	page.page = 1;
	reloadRecord();
}

function reloadRecord(){
	var tableData = [];
	var rows= 1;
	var type = "show";
	var url = _basePath + "/rptDailyDevice/getPages";
	page.start_date = $("#start_date").val();
	page.company = company;
	page.site = $("#site").val();
	$.post(url, page, function(res, status) {
		$("#recordBody").empty();
		renderPage("pageUL",page,res.total,reloadRecord);
		var data = res.data;
		if(data != null && data.length > 0){
			var strs = "";
			$.each(data,function(i,item){
				 tableData.push(item);
			 });
			 //遍历数组把站名相等的列站名拼接
			 for(var i = 0;i <tableData.length; i++) {
				 rows= 1;
				 type="show";
				 if (tableData[i].site_name == "") {
						type = "hidden";
				 }
				 for(var j = i+1;j <tableData.length; j++) {
					 if (tableData[i].site_name == "") {
						break;
					}else if(tableData[i].site_name!=tableData[j].site_name){
						break;
					}else if(tableData[i].site_name==tableData[j].site_name) {
						rows++;
						tableData[j].site_name="";
					}
				 }
				 strs += "<tr><td>"+ formatNull(tableData[i].stat_date) + "</td>"
				 	+ "<td style='vertical-align: middle;' rowspan='" + rows + "' "+type+ ">" + formatNull(tableData[i].site_name) + "</td>"
				 	+ "<td>" + formatNull(tableData[i].device_name) + "</td><td>" + formatNull(tableData[i].rate + "%") + "</td>"
				 	+ "<td>" + formatNull(tableData[i].off_cnt) + "</td><td>" + formatNull(Utils.transTime(tableData[i].off_duration)) + "</td>"
				 	+ "<td>"+ formatNull(tableData[i].alarm_cnt) + "</td></tr>"; 
			 }
			 
			 $("#recordBody").append(strs);
		 }
	});
}