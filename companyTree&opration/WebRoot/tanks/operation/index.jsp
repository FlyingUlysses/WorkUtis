<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/scripts/tank/operation.js"></script>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<c:choose>
				<c:when test="${classify == 'system' or company.cnt > 0}">
					<div class="span2">
						<div class="widget green">
		                      <div class="widget-title">
		                          <h4><i class="icon-align-left"></i>公司架构</h4>
		                      </div>
		                      <div class="widget-body">
		                          <div id="jstree"></div>
		                      </div>
		                </div>
		            </div>
		            <div class="span10">
				</c:when>
				<c:otherwise>
					 <div class="span12">
				</c:otherwise>
			</c:choose>
                <div class="widget green">
                      <div class="widget-title">
                          <h4><i class="icon-align-left"></i><span id="title">
	                          <c:if test="${company.cnt == 0}">【${company.name}】</c:if></span>运维记录</h4>
                          <div class="update-btn">
		                       <input id="start_date" type="text" placeholder="选择故障发生开始时间点" style="width:130px" value="${yearmmdd}" size="7" class="m-ctrl-medium" onchange="reload();">
		                       <input id="end_date" type="text" placeholder="选择故障发生截止时间点" style="width:130px" value="${yearmmdd}" size="7" class="m-ctrl-medium" onchange="reload();">
		                       <input id="sitename" type="text" placeholder="请输入站点名称..." style="width:110px" >
		                        <select id="faulttype" name="faulttype" tabindex="-1" onchange="reload();" style="width:130px">
		                            <option value="">请选择故障类型...</option>
		                          	<option value="1">离线</option>
		                          	<option value="2">告警</option>
		                          	<option value="3">设备故障</option>
		                        </select>
                               <button id="rt_export" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">导出(分站)</span></button>
                               <button id="rtAll_export" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">汇总导出(分月)</span></button>
                               <button id="search" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
                          	   <button id="add" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-plus"></i>&nbsp;<span style="font-size: 12px;">新增</span></button>
                          </div>
                      </div>
                      <div class="widget-body">
                          <table class="table table-striped table-bordered table-hover">
			                  <thead>
			                      <tr>
			                      	  <th style="width: 15px; vertical-align: middle;"></th>
			                          <th style="width: 80px;">站点</th>
			                          <th style="width: 50px;">故障类型</th>
			                          <th style="width: 100px;">故障小类</th>
			                          <th style="width: 80px;">更换设备</th>
			                          <th style="width: 80px;">发生时间</th>
			                          <th style="width: 80px;">解决时间</th>
			                          <th style="width: 80px; vertical-align: middle;">操作</th>
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
		</div>
		
		 <script type="text/javascript">
    	 $("#start_date").datepicker({ format: "yyyy-mm-dd" });
    	 $("#end_date").datepicker({ format: "yyyy-mm-dd" });
    </script>
</body>	
