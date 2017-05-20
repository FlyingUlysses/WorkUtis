var company = _company;
var idFields = {};
var fieldIntvals = [];
$(function() {
	loadding = layer.load(1, { shade: [0.1,'#fff'] });
	buildCompJsTree();
	reload();
	setInterval("reload()",15000);
});

function reload(){
	var url = _basePath + "/hgcx/getData";
	idFields = {};
	$.get(url, {company: company}, function(data, status) {
		clearAllIntvals();
		$("#rowBody").empty();
		var sound = null;
		if(data.length > 0){
			var index = 0;
			var llj_index = 0;
			
			var warnning = false;
			var offLine = false;
			
			var html = "";
			var warnHtml = "";
			var offHtml = "";
			var rowHtml = "";
			$.each(data, function(i, item){
				if(item.sound > 0)
					sound = true;
				if(!Utils.isEmpty(item.field))
					warnning = true;
				if(item.plc_state == 0 || item.llj_state == 0)
					offLine = true;
				rowHtml += "<tr>";
				var border = "";
				if(index == 0){
					 border = "border-top: 1px solid #666666;";
					 rowHtml += "<td nowrap='nowrap' style='vertical-align: middle;" + border + " ' rowspan='" + item.td_cnt + "'>&nbsp;" + item.name + "</td>";
				}
				rowHtml += "<td nowrap='nowrap' style='border-top: 1px solid white;" + getOffColor(item) + "' onclick='jump(\"" + item.tank + "\")'>&nbsp;" + formatNull(item.tank_name) + "</td>"
						 + "<td style='" + border + " background-color: " + getPlcOff(item) + "'><div style='width: " + item.scale_rate + "; background-color: " + getRateColor(item) + ";'>&nbsp;" + formatNull(item.scale_rate) + "</div></td>";
				if(llj_index == 0){
					var rowspan = item.llj_cnt > 1? item.llj_cnt : 1;
					rowHtml += "<td rowspan='" + rowspan + "' style='" + border + " background-color: " + getPlcOff(item) + "; text-align: center;vertical-align: middle;'>&nbsp;" + formatNull(item.forcast) + "</td>";
				}
				rowHtml += "<td id='" + item.tank + "_scale' style='" + border + " background-color: " + getBackColor(item, "scale") + "'>&nbsp;" + formatNull(item.scale) + "</td>"
						 + "<td id='" + item.tank + "_pressure' style='" + border + " background-color: " + getBackColor(item, "pressure") + "''>&nbsp;" + formatNull(item.pressure) + "</td>"
						 + "<td id='" + item.tank + "_ext_press_1' style='" + border + " background-color: " + getBackColor(item, "ext_press_1") + "''>&nbsp;" + formatNull(item.ext_press_1) + "</td>"
						 + "<td id='" + item.tank + "_ext_press_2' style='" + border + " background-color: " + getBackColor(item, "ext_press_2") + "''>&nbsp;" + formatNull(item.ext_press_2) + "</td>"
						 + "<td id='" + item.tank + "_ext_temp' style='" + border + " background-color: " + getBackColor(item, "ext_temp") + "''>&nbsp;" + formatNull(item.ext_temp) + "</td>";
			    if(llj_index == 0){
					var rowspan = item.llj_cnt > 1? item.llj_cnt : 1;
					rowHtml += "<td rowspan='" + rowspan + "' style='" + border + " background-color: " + getLljOff(item) + "; vertical-align: middle;'>&nbsp;" + formatNull(item.ext_flow) + "</td>";
				}
				rowHtml += "<td style='" + border + "'>&nbsp;" + formatNull(item.trans_time) + "</td>";
				rowHtml += "</tr>";
				index ++;
				llj_index ++;
				if(llj_index >= item.llj_cnt)
					llj_index = 0;
				if(index == item.td_cnt){
					index = 0;
					if(warnning)
						warnHtml += rowHtml;
					else if(offLine)
						offHtml += rowHtml;
					else
						html += rowHtml;
					if(!Utils.isEmpty(item.sound_field) && item.plc_state != 0)
						idFields[item.tank] = item.sound_field;
					rowHtml = "";
					offLine = false;
					warnning = false;
				}
			});
		}
		$("#rowBody").append(warnHtml + offHtml + html);
		if(loadding != null){
			layer.close(loadding);
			loadding = null;
		}
		flashAll();
		if(sound)
			playAlarm();
		else
			stopAlarm();
	});
}

function getOffColor(item){
	var strs = "";
	if(item.plc_state == 0)
		strs += "background-color: #949FB1;";
	else if(!Utils.isEmpty(item.field))
		strs += "background-color: maroon;";
	else 
		strs += "background-color: #008000;";
	if(item.llj_state != 0)
		strs += "color: white;";
	return strs;
}

function getPlcOff(item){
	if(item.plc_state == 0)
		return "#949FB1";
}

function getLljOff(item){
	if(item.llj_state == 0)
		return "#949FB1";
	return "";
}

function getRateColor(item){
	if(item.plc_state == 0)
		return "#949FB1";
	if(item.rate >= 50)
		return "#46BFBD";
	else if(item.rate >= 20)
		return "#FDB45C";
	else
		return "#DE577B";
}

function flashAll(){
	for(var ele in idFields){
		var id = ele;
		var fields = idFields[ele];
		fieldFlash(id, fields);
	}
}
function fieldFlash(id, fields){
	var ary = [];
	if(fields != null)
		ary = fields.split(",");
	if(ary.length > 0){
		$.each(ary,function(i,item){
			var ele = id + "_" + item;
			var row = { index: 0 };
			row.interval = setInterval(function(){
				var color = ["#b9d3ee","#de577b","#b8860b"]; 
				row.index++;
				$("#" + ele).css("background-color", color[row.index % 3]);
				if(row.index > 10000) row.index = 0;
			},100);
			fieldIntvals.push(row.interval);
		});
	}
}

function clearAllIntvals(){
	$.each(fieldIntvals,function(i,item){
		clearInterval(item);
	});
	fieldIntvals = []; 
}


function getBackColor(item, field){
	if(item.plc_state == 0)
		return "#949FB1";
	var ary = [];
	if(item.field != null)
		ary = item.field.split(",");
	if(ary.contains(field))
		return "#de577b";
	return "";
}

function jump(id){
	var valuew = screen.availWidth;
	var vlaueh = screen.availHeight-50;
	window.open(_basePath + "/monitor/tankV2?id=" + id, 'tankWin', 'height='+vlaueh+', width=' + valuew + ',top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
}

var show = false;
function showDiv(){
	if(show){
		$("#compDiv").hide();
		$("#listDiv").css({ "margin-left": "0px"});
		show = false;
	}else{
		$("#compDiv").show();
		$("#listDiv").css({ "margin-left": "240px"});
		show = true;
	}
}