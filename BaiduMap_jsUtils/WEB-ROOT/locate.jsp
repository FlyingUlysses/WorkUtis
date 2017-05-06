<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<style>
<!--
	.anchorBL{ display:none; }
	.widget-body{ padding: 5px; }
-->
</style>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=7xMGLa1Gp2aXq4GY3HFfRkVt"></script>
<script src="<%=basePath %>/resources/scripts/core/map_locate.js"></script>
<script type="text/javascript">
	
	var _location = "${location}";
</script>
<body>
    <div class="container-fluid">
		<div class="row-fluid">
            <div>
				<div style="width: 95%; display:inline-block;margin-left:3%"><input  type="text" id="input" style="width:90%;vertical-align: "middle";/><button onclick="searchMap();" type="button" style="padding: 4px 14px;float: right;" class="btn btn-info"><i class="icon-map-marker"></i></button></div>
				<div id="allmap" style="height: 450px;"></div>
			</div>
		</div>
	</div>
</body>