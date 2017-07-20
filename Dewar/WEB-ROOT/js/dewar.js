var company = "";
var page = { page: 1,limit: 9 };
var tempSite =null;
$(function() {
	if($("#jstree").length){
		buildCompJsTree();
	}else{
		reload();
	}
	
	$("#serach_dewar").keydown(function(event) {
        if (event.keyCode == 13) { 
        	reload();
        }   
    }); 
	$("#serach_sensor").keydown(function(event) {
		if (event.keyCode == 13) { 
			reload();
		}   
	}); 
	
	$("#search").click(function () {
		reload();
	});
	
	$("#clear").click(function () {
		page.dewar_num=$("#serach_dewar").val("");
		page.sensor_num=$("#serach_sensor").val("");
		reload();
	});
	$("#add").click(function () {
		showModel({
			title : "杜瓦瓶新增",
			width : "285px",
	 		height : "345px",
			url : _basePath + "/dewar/edit?company="+company
		});
	});
});

var page = {page: 1,limit: 15};
function reload(){
	page.page = 1;
	reloadRecord();
}


function reloadRecord(){
	var url = _basePath + "/dewar/getPages";
	page.dewar_num=$("#serach_dewar").val();
	page.sensor_num=$("#serach_sensor").val();
	page.company = company;
	$.post(url, page, function(res, status) {
		$("#rowBody").empty();
		renderPage("pageUL",page,res.total,reloadRecord);
		var data = res.data;
		if(data != null && data.length > 0){
			 var strs = "";
			 $.each(data,function(i,item){
				 strs += "<tr onclick='rowClick(" + item.id + ");'>"
					 + "<td style='text-align: center;'><input class='checkboxes' name='rowRadio' type='radio' value='" + item.id + "' /></td>"
					 + "<td style='vertical-align: middle;'>" + formatNull(item.manufactor_name) + "</td>"
					 + "<td id='model_"+i+"' style='vertical-align: middle;' onclick='showSpecs("+ item.equip_id +")'>" + formatNull(item.model) + "</td>"
					 + "<td style='vertical-align: middle;'>" + formatNull(item.dewar) + "</td>"
					 + "<td style='vertical-align: middle;'>" + formatNull(item.sensor) + "</td>"
					 + "<td style='vertical-align: middle;text-align: center;'>" + formatNull(item.create_time) + "</td>"
					 + "<td style='text-align: center;'>"
					 + 		"<button style='padding: 1px 12px;' class='btn btn-primary' onclick='editDewar(" + item.id + ");'><i class='icon-pencil'></i></button>&nbsp;&nbsp;"
					 + 		"<button id='rvBtn_" + item.id + "' style='padding: 1px 12px;' class='btn btn-danger ladda-button' data-style='zoom-in'"
					 + 			" onclick='revRecord(\"" + _basePath + "/dewar/delete?id=" + item.id + "\",\"rvBtn_" + item.id + "\");'><i class='icon-trash'></i></button></td></tr>";
			 });
			 $("#rowBody").append(strs);
		 }
	});
}

function editDewar(id){
	showModel({
		title : "杜瓦瓶编辑",
		width : "285px",
 		height : "345px",
		url : _basePath + "/dewar/edit?id=" + id+"&company="+company
	});
}

function rowClick(id){
	$("input[name='rowRadio'][value='" + id + "']").attr("checked",'checked'); ;
}



function showSpecs(equip_id){
	showModel({
		title : "规格列表",
		width : "525px",
		height : "325px",
		url : _basePath + "/dewar/showSpecs?equip_id=" + equip_id
	});
}