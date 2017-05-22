$(function() {
	reload();
	setInterval("reload()",15000);
});

function gotoQX(id){
	var name=$("#tankList").find("option:selected").text();
	var valuew=screen.availWidth;
	var vlaueh=screen.availHeight-50;
	window.open(_basePath + "/tkRpt/tk_curve_dyn?id=" + id+"&name="+name, 'newwindow', 'height='+vlaueh+', width='+valuew+',top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
}

function showValves(){
	showModel({
		title : "阀门控制",
		width : "380px",
		height: "428px",
		url : _basePath + "/monitor/valve?id=" + _tank
	});
} 

function reload(){
	var url = _basePath + "/monitor/showTankV2";
	$.get(url, { id: _tank }, function(data, status) {
		
		//添加状态灯
		if(data.sattrs != null ){
			$("#actBoard").empty();
			var tempStr = "";
			var strs ="";
				$.each(data.sattrs.light,function(i,item){
					$("#actBoard").show();
					if (item.light=="lu.gif") {
						tempStr	+="<li class='green'><span></span><p>"+item.app_name+"</p></li>"
					}else if (item.light=="red.gif") {
						tempStr	+="<li class='red'><span></span><p>"+item.app_name+"</p></li>"
					}else {
						tempStr	+="<li><span></span><p>"+item.app_name+"</p></li>"
					}
				});
				strs += "<dd><ul>" + tempStr + "</ul></dd>";
				$("#actBoard").append(strs);
		}
		
		//添加右边数据栏
			$("#monitorata").empty();
				if (data.vattrs !=null) {
					var str ="";
						$.each(data.vattrs,function(i,item){
							if (i==0) {
								str+="<div class='card clearfix'><div class='title'>"+item.classifyname+"</div><div class='txt'>";
								str+="<p><i>"+item.attr_name+"</i><span><strong>"+item.fval+"</strong><i>"+item.unit+"</i></span></p>";
							}else if (data.vattrs[i].classify != data.vattrs[i-1].classify) {
								str+="</div></div>";
								str+="<div class='card clearfix'><div class='title'>"+item.classifyname+"</div><div class='txt'>";
								str+="<p><i>"+item.attr_name+"</i><span><strong>"+item.fval+"</strong><i>"+item.unit+"</i></span></p>";
							}else if (data.vattrs[i].classify == data.vattrs[i-1].classify) {
								str+="<p><i>"+item.attr_name+"</i><span><strong>"+item.fval+"</strong><i>"+item.unit+"</i></span></p>";
							}
							if (i==(data.vattrs.length-1)) {
								str+="</div></div>";
							}
						})
						$("#monitorata").append(str);
				}
		
		//添加图片数据
			if(data.dnmc != null){
				$("#pressure").html("<strong>"+formatNull(data.dnmc.pressure)+"<strong>");
				$("#scale").html("<strong>"+formatNull(data.dnmc.scale)+"<strong>");
				$("#ext_temp").html("<strong>"+formatNull(data.dnmc.ext_temp)+"<strong>");
				$("#ext_press_1").html("<strong>"+formatNull(data.dnmc.ext_press_1)+"<strong>");
				$("#ext_press_2").html("<strong>"+formatNull(data.dnmc.ext_press_2)+"<strong>");
				$("#disRate").html("<i style='height: "+formatNull(data.disRate)+";'></i>");
				$("#detect_tyq").html("<strong>"+formatNull(data.dnmc.detect_tyq)+"<strong>");
				$("#detect_tank").html("<strong>"+formatNull(data.dnmc.detect_tank)+"<strong>");
				
			}
			if (data.llj != null) {
				$("#ext_flow").html("<strong>"+formatNull(data.llj.ext_flow)+"<strong>");
				$("#flow_bk").html("<strong>"+formatNull(data.llj.flow_bk)+"<strong>");
				$("#flow_gk").html("<strong>"+formatNull(data.llj.flow_gk)+"<strong>");
				$("#flow_temp").html("<strong>"+formatNull(data.llj.flow_temp)+"<strong>");
				$("#flow_press").html("<strong>"+formatNull(data.llj.flow_press)+"<strong>");
			}
			if (data.ckb != null) {
				$("#card_yqzl").html("<strong>"+formatNull(data.ckb.card_yqzl)+"<strong>");
			}
			
			if(data.alert.sound > 0) playAlarm();
			else stopAlarm();
			if(data.alert.cnt > 0)
			$("#alarm").show();
			
	});
}

function toggleNoice(){
	var url = _basePath + "/monitor/toggleNoice";
	$.post(url, {id: _tank}, function(data, status) {
		layer.alert(data.message,function(index){
			if(data.success)
				reload();
			layer.close(index);	
		});
	});
}

function formatNull(str, rep, format) {
	if (format == undefined)
		format = "";
	if (str == null || str == undefined) {
		if (rep == null || rep == undefined || rep == "")
			return "";
		else
			return rep;
	}
	return str + format;
}


