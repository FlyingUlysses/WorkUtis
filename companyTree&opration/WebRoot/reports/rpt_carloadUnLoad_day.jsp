<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/bootstrap/datepicker/datepicker.css" rel="stylesheet" />
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/scripts/carLoadUnloadDayRpt.js"></script>
<style>
<!--
.table th, .table td{
	vertical-align: middle;
}
-->
</style>
<body>
     <div class="container-fluid">
		<div class="row-fluid">
			<c:choose>
				<c:when test="${classify == 'system' or company.cnt > 0}">
					<div class="span3">
						<div class="widget green">
		                      <div class="widget-title">
		                          <h4><i class="icon-align-left"></i>公司架构</h4>
		                      </div>
		                      <div class="widget-body">
		                          <div id="jstree"></div>
		                      </div>
		                </div>
		            </div>
		            <div class="span9">
				</c:when>
				<c:otherwise>
					 <div class="span12">
				</c:otherwise>
			</c:choose>
				<div class="widget green">
					<div class="widget-title">
                        <h4><i class="icon-align-left"></i>配送装卸液月报</h4>
                         <div class="update-btn">
                         	   <button id="findBut" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
                         	   <!--<button id="rt_export" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">导出</span></button>-->
							   <button id="rt_export2" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">导出(分站)</span></button>
							   <button id="clearAll" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;<span style="font-size: 12px;">清空</span></button> 
                           	   <input id="start_date" type="text" placeholder="选择导出开始时间点" style="width:160px" value="${yearmmdd}" size="7" class="m-ctrl-medium" onchange="reloadRecord();">
                           	   <input id="end_date" type="text" placeholder="选择导出结束时间点" style="width:160px" value="${yearmmdd}" size="7" class="m-ctrl-medium" onchange="reloadRecord();">
                           	   <!--
                           	   <select id="carid" name="carid" tabindex="-1" onchange="reload();">
                           	   		<option value="">请选择车辆司机...</option>
	                           		<c:forEach items="${CARDRIVER}" var="item">
	                           			<option value="${item.code}">${item.name}</option>
	                           		</c:forEach>
	                           </select>-->
                           	   <select id="tankid" name="tankid" tabindex="-1" onchange="reloadRecord();">
                           	   		<option value="">请选择站点...</option>
	                           		<c:forEach items="${tanks}" var="item">
	                           			<option value="${item.id}">${item.name}</option>
	                           		</c:forEach>
	                           </select>
                         </div>
                    </div>
                     <div class="widget-body">
                     	<table class='table table-bordered table-hover'>
                    		<thead>
			                      <tr>
			                          <th nowrap>日期</th>
			                          <th nowrap>车号</th>
			                          <th nowrap>司机</th>
			                          <th nowrap>站点</th>
			                          <th nowrap>卸液时间</th>
			                          <th nowrap>卸液量</th>
			                          <th nowrap>充装合计</th>
			                      </tr>
			                 </thead>
			                 <tbody id="recordBody"></tbody>
                    	</table>
		                <div class="pagination pagination-right">
                             <ul id="pageUL"></ul>
                        </div>
                    </div>
                </div>
			</div>
		</div>
	</div>
	</div>
    <script type="text/javascript">
    	 $("#start_date").datepicker({ format: "yyyy-mm-dd" });
    	 $("#end_date").datepicker({ format: "yyyy-mm-dd" });
    </script>	
</body>	
