<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/bootstrap/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/bootstrap/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript">
	var _device = "${device}";
</script>
<script src="<%=basePath %>/resources/scripts/tank_rpt/devices/log.js"></script>
<body>
    <div class="container-fluid">
		<div class="row-fluid">
            <div style="float: left; margin-left: 12px;" class="update-btn">
				<input id="start_date" type="text" class="form_datetime" style="margin-left: 2px;width: 110px;" size="16" value="${start}" readonly>
				<input id="end_date" type="text" class="form_datetime" style="margin-left: 2px;width: 110px;" size="16" value="${end}" readonly>
				<button id="search" type="button" style="padding: 4px 8px; margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
            </div>
            <div class="widget-body">
			    <table class="table table-hover">
			    	 <thead>
	                    <tr style="background:#bebebe;">
		                    <th>离线时间</th>
		                    <th>上线时间</th>
		                    <th>间隔时长</th>
	                    </tr>
	              	</thead>
			        <tbody id="rowBody"></tbody>
				</table>
				<div class="pagination pagination-right">
                    <ul id="pageUL"></ul>
                </div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
    	$(".form_datetime").datetimepicker({
		    autoclose: 1, 
		    minView: 1,
    		format: "yyyy-mm-dd hh时" 
    	});
    </script>
</body>	