<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%
	String basePath = request.getContextPath();
%>
<link href="<%=basePath %>/resources/css/tank.css" rel="stylesheet" />
<script type="text/javascript">
	var _basePath = "<%=basePath%>";
	var _tank = "${tankid}";
</script>
<script src="<%=basePath %>/resources/plugins/jquery-1.8.3.min.js"></script>
<script src="<%=basePath %>/resources/plugins/alarm.js"></script>
<script src="<%=basePath %>/resources/layer/layer.js"></script>
<script src="<%=basePath %>/resources/scripts/base/base.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/css/mess.css"/>
<script src="<%=basePath%>/resources/scripts/tank/tank_v2.js"></script>
	<body>
		<div class="container">
			<div class="aside">
				<dl class="clearfix">
					<dt>
						<select id="tankList" data-placeholder="请选择储罐..." onchange="reloadThis(this);">
							<c:forEach items="${tankList}" var="item">
                             		<c:choose>
                             			<c:when test="${!empty tankid && tankid == item.id}">
                             				<option value="${item.id}" selected="selected">${item.name}</option>
                             			</c:when>
                             			<c:otherwise>
											<option value="${item.id}">${item.name}</option>                               			
                             			</c:otherwise>
                             		</c:choose>
                             </c:forEach>
						</select>
					</dt>
					<div id="actBoard" hidden>
					</div>
				</dl>
				<div id="monitorata">
					
				</div>
			</div>
			<div class="main">
				<div class="title"><input style="font-weight:bold; height: 40px; width: 130px;" type="button" name="" value="详细数据变化监控" onclick="gotoQX('${tankid}')"/></div>
				<img src="<%=basePath %>/resources/images/tank/mes_v2_bg0.png"/>
				<div id="box">
					<span class="num1" id =pressure name="储罐压力"></span>
					<span class="num2" id ="detect_tank" name="上泄露指示"></span>
					<span class="num3" id ="ext_temp" name="出口温度"></span>
					<span class="num4" id ="ext_press_2" name="出口压力2"></strong></span>
					<span class="num5" id ="scale" name="液体指示"></strong></span>
					<span class="num6" id ="ext_press_1" name="出口压力1"></span>
					<span class="num7" id ="detect_tyq" name="下泄露指示"></span>
					<span class="num8" id ="card_yqzl" name="卡控表用气总量"></span>
					<span class="num9" id ="ext_flow" name="出口总流量"></span>
					<span class="num10" id ="flow_bk" name="标况流量"></span>
					<span class="num11" id ="flow_gk" name="工况流量"></span>
					<span class="num12" id ="flow_temp" name="流量计温度"></span>
					<span class="num13" id ="flow_press" name="流量计压力"></span>
					<span class="temp" id ="disRate" name="流量计图片" ></span>
					<span class="alarm" id="alarm" onclick="toggleNoice();" >
						<img src="<%=basePath %>/resources/images/tank/alarm_v2.png"/>
					</span>
					<span></span>
				</div>
			</div>
		</div>			
	</body>
<script language="javascript">
	function reloadThis(obj){
		window.location.href = _basePath + "/monitor/tankV2?id=" + obj.value;
	}
	function loadDate(){
		window.location.href = _basePath + "/monitor/tankV2?id=" + ${tankid};
	}
$(function() {
	if ($("body").height() <= $(".main >img").height() + 55) {
		$(".main >img").css("height",$("body").height()- 55 +'px');
	}
	var imgW = $(".main >img").width();
    var imgH = $(".main >img").height();
   $("#box").css({
	  "width":imgW,
	  "height":imgH
   });
   	var f =false;
   
   setInterval(function() {
   		if (f) {
   			$(".alarm").hide();
   		}else{
   			$(".alarm").show();
   		}
   		f= !f;
   },300);
});
</script>