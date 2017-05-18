function reloadRecord(){
	var tableData = [];
	var rows= 1;
	var type="show";
	
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
				 for(var j = i+1;j <tableData.length; j++) {
					 if (tableData[i].site_name=="") {
						type="hidden";
						break;
					}else if(tableData[i].site_name!=tableData[j].site_name){
						break;
					}else if(tableData[i].site_name==tableData[j].site_name) {
						rows++;
						tableData[j].site_name="";
					}
				 }
				 strs += "<tr><td rowspan='"+rows+"' "+type+ ">" + formatNull(tableData[i].site_name) + "</td><td>" + formatNull(tableData[i].device_name) + "</td><td>" + formatNull(tableData[i].alarm_cnt) + "</td><td>"
				 + formatNull(tableData[i].off_cnt) + "</td><td>"+ formatNull(tableData[i].off_duration) + "</td><td>"+ formatNull(tableData[i].online_rate) + "</td><td>"+ formatNull(tableData[i].stat_date) + "</td></tr>"; 
			 }
			 
			 $("#recordBody").append(strs);
		 }
	});
}