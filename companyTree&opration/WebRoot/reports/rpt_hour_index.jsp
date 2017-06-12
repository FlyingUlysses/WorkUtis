<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<link href="<%=basePath %>/resources/bootstrap/datepicker/datepicker.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/scripts/rptTankHour.js"></script>
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
                        <h4><i class="icon-align-left"></i>气化站运行记录</h4>
                         <div class="update-btn">
                         	   <button id="findBut" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
							   <button id="clearAll" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;<span style="font-size: 12px;">清空</span></button>
							   <button id="export" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">导出</span></button> 
                           	   <input id="start_date" type="text" placeholder="选择开始时间点" style="width:160px" value="${yearmmdd}"  size="7" class="m-ctrl-medium" onchange="reloadFirst();">
                           	   <input id="end_date" type="text" placeholder="选择结束时间点" style="width:160px" value="${yearmmdd}" size="7" class="m-ctrl-medium" onchange="reloadFirst();">
                           	   <select id="tank_id" name="tank_id" tabindex="-1" onchange="reloadFirst();">
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
			                          <th>时间</th>
			                          <th>储罐</th>
			                          <th>液位mmH₂O</th>
			                          <th title="此处压力是指储罐压力">压力MPa</th>
			                          <th title="根据液位换算得出的库存量">库存量kg</th>
			                          <th title="经过复热器加热后的温度">入口温度℃</th>
			                          <th title="气化后进入调压撬时的压力">入口压力MPa</th>
			                          <th title="离开调压撬时的温度">出口温度℃</th>
			                          <th title="经过调压后的压力">出口压力MPa</th>
			                          <th title="每秒用气m³数">瞬时流量Nm3</th>
			                          <th>累计流量Nm3</th>
			                          <th>库存变化量kg</th>
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
