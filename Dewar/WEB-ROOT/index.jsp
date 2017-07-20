<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/scripts/tank/dewar.js"></script>
<style>
<!--
.widget { clear: none; }
-->
</style>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<c:choose>
				<c:when test="${classify == 'system' or company.cnt > 0}">
					<div class="span2" style="width: 255px">
						<div class="widget green">
		                      <div class="widget-title">
		                          <h4><i class="icon-align-left"></i>公司架构</h4>
		                      </div>
		                      <div class="widget-body">
		                          <div id="jstree"></div>
		                      </div>
		                </div>
		            </div>
		            <div id="container" style="margin-left: 275px;">
				</c:when>
				<c:otherwise>
					  <div id="container" style="margin-left: 10px;">
				</c:otherwise>
			</c:choose>
                <div class="widget green">
                      <div class="widget-title">
                          <h4><i class="icon-align-left"></i><span id="title">
	                          <c:if test="${company.cnt == 0}">【${company.name}】</c:if></span>杜瓦瓶管理</h4>
                          <div class="update-btn">
                          	   <input id="serach_dewar" type="text" placeholder="容器编码搜索" style="width: 140px;">
                          	   <input id="serach_sensor" type="text" placeholder="传感器编码搜索" style="width: 140px;">
                               <button id="search" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
                          	   <button id="add" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-plus"></i>&nbsp;<span style="font-size: 12px;">新增</span></button>
                          	   <button id="clear" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;<span style="font-size: 12px;">清空</span></button> 
                          </div>
                      </div>
                      <div class="widget-body">
                          <table class="table table-striped table-bordered table-hover">
			                  <thead>
			                      <tr>
			                      	  <th style="width: 12px; vertical-align: middle;"></th>
			                          <th >生产厂家</th>
			                          <th style="width: 18%;">设备型号</th>
			                          <th style="width: 18%;">容器编码</th>
			                          <th style="width: 18%;">传感器编码</th>
			                          <th style="width: 18%;text-align: center;">创建时间</th>
			                          <th style="width: 80px; vertical-align: middle;text-align: center;">操作</th>
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
    </script>
</body>	
