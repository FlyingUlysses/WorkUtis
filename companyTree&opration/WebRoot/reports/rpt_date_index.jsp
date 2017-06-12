<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<link href="<%=basePath %>/resources/bootstrap/datepicker/datepicker.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/scripts/rptTankDate.js"></script>
<style>
	.table thead th{ text-align: center;vertical-align: middle; overflow:auto}
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
                        <h4><i class="icon-align-left"></i>储罐日报表汇总</h4>
                         <div class="update-btn">
                         	   <button id="findBut" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
							   <button id="clearAll" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;<span style="font-size: 12px;">清空</span></button>
							   <button id="chart" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-bar-chart"></i>&nbsp;<span style="font-size: 12px;">图表</span></button> 
							   <button id="export" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">导出</span></button> 
							   <button id="exportAll" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">汇总导出</span></button>
							   <!-- <button id="explain" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-comment-alt"></i>&nbsp;<span style="font-size: 12px;">导出说明</span></button>-->
                           	   <input id="start_date" type="text" placeholder="选择开始时间点" style="width:160px"  size="7" class="m-ctrl-medium" onchange="reloadRecord();">
                           	   <input id="end_date" type="text" placeholder="选择结束时间点" style="width:160px" size="7" class="m-ctrl-medium" onchange="reloadRecord();">
                           	   <select id="tank_id" name="tank_id" tabindex="-1" onchange="reloadRecord();">
                           	   		<option value="">请选择站点...</option>
	                           		<c:forEach items="${tanks}" var="item">
	                           			<option value="${item.id}">${item.name}</option>
	                           		</c:forEach>
	                           </select>
                         </div>
                    </div>
                     <div class="widget-body">
                     	<table class='table table-bordered table-striped table-hover table-condensed'>
                    		<thead>
			                      <tr>
			                          <th rowspan="2">账期</th>
			                          <th rowspan="2">储罐</th>
			                          <th rowspan="2">液位 mmH₂O</th>
			                          <th rowspan="2">库存 kg</th>
			                          <th rowspan="2">库存变化 kg</th>
			                          <!--<th rowspan="2">充装 kg</th>-->
			                          <th rowspan="2">卸液 kg</th>
			                          <th rowspan="2">用液 kg</th>
			                          <th rowspan="2">累计流量 Nm3</th>
			                          <th colspan="3">用气量 Nm3</th>
			                          <!--  
			                          <th colspan="2">气损</th>
			                          <th rowspan="2">气化率</th>-->
			                      </tr>
			                      <tr>
			                      	  <th>流量计</th>
			                          <th>其它</th>
			                          <th>合计</th>
			                          <!--  
			                          <th>量 kg</th>
			                          <th>%</th>-->
			                      </tr>
			                 </thead>
			                 <tbody id="statBody"></tbody>
                    	</table>
		                <div class="pagination pagination-right">
                             <ul id="pageUL"></ul>
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
