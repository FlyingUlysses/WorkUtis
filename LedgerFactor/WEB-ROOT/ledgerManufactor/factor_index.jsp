<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/bootstrap/datepicker/datepicker.css" rel="stylesheet" />
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/scripts/ledger/ledgerManufactor/ledgerManufactor.js"></script>
<style>
<!--
.widget { clear: none; }
-->
</style>
<body>
    <div class="container-fluid">
		<div class="row-fluid">
					<div   id="leftDiv" style="float: left; width: 300px;">
						<div class="widget green">
		                      <div class="widget-title">
		                          <h4><i class="icon-align-left"></i>生产厂家</h4>
		                          <input type="text" id="serchbox" placeholder="搜索厂家" style="display:block;float:left; ; height:20px; width:100px;margin: 2px 5px;">
		                          <button id="createFactor" type="button" style="float:right;margin:2px 5px;height:30px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="float:center;font-size: 12px;">新增</span></button>
		                      </div>
		                      <div class="widget-body">
		                          <div id="jstree"></div>
		                      </div>
		                </div>
		            </div>
		            
		      <div id="container" style="margin-left: 325px;">
				<div class="widget green" >
					<div class="widget-title">
                        <h4><i class="icon-align-left"></i>设备列表</h4>
                         <div class="update-btn">
                         	   <button id="createEquip" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">新增</span></button>
                         	   <button id="findBut" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
                         	   <button id="clearEquip" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;<span style="font-size: 12px;">清空</span></button> 
                         	   <input id="equip_num"  type="text" placeholder="按设备型号搜索……" style="width: 135px;">
                           	    <select id="type" name="type" tabindex="-1" style="width:135px" onchange="reload();">
	                           		<option value="">请选择设备类型</option>
	                           		<c:forEach items="${typeList}" var="item">
	                           			<option value="${item.code}">${item.name}</option>
	                           		</c:forEach>
	                           </select>

                         </div>
                    </div>
                     <div class="widget-body">
                     	<table class='table table-bordered table-hover'>
                    		<thead>
			                      <tr onclick="getRowsss()">
			                      	  <th style="width: 25px;"></th>
			                      	  <th nowrap style="width: 15%;text-align: center;">生产厂家</th>
			                      	  <th nowrap style="width: 15%;text-align: center;">设备名称</th>
			                          <th nowrap style="width: 15%;text-align: center;">设备型号</th>
			                          <th nowrap style="width: 15%;text-align: center;">设备类型</th>
			                          <th nowrap style="width: 15%;text-align: center;">创建日期</th>
			                          <th nowrap style="text-align: center;">创建人</th>
			                          <th style="width: 90px; vertical-align: middle;text-align: center;">操作</th>
			                      </tr>
			                 </thead>
			                 <tbody id="recordBody1"></tbody>
                    	</table>
		                <div class="pagination pagination-right">
                             <ul id="pageUL1"></ul>
                        </div>
                    </div>
                </div>
                <div class="widget green"  id="specs_div" hidden>
					<div class="widget-title">
                        <h4><i class="icon-align-left"></i>设备属性</h4>
                         <div class="update-btn">
                        <button id="createSpecs" type="button" style="margin-bottom: 10px;float=right;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">新增</span></button>
                         </div>
                    </div>
                     <div class="widget-body">
                     	<table class='table table-bordered table-hover'>
                    		<thead>
			                      <tr>
			                      	   <th style="width: 25px;"></th>
			                      	  <th nowrap style="width: 30%;text-align: center;">所属设备</th>
			                          <th nowrap style="width: 30%;text-align: center;">设备规格</th>
			                          <th nowrap style="text-align: center;">规格值</th>
			                          <th style="width: 90px; vertical-align: middle;text-align: center;">操作</th>
			                      </tr>
			                 </thead>
			                 <tbody id="recordBody2"></tbody>
                    	</table>
		                <div class="pagination pagination-right">
                             <ul id="pageUL2"></ul>
                        </div>
                    </div>
                </div>
                
                
                
			</div>
		</div>
	</div>
    <script type="text/javascript">
    </script>	
</body>	
