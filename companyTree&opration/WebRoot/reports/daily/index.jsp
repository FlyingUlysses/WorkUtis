<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/bootstrap/datepicker/datepicker.css" rel="stylesheet" />
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/scripts/rpt/rptDailyDevice.js"></script>
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
                        <h4><i class="icon-align-left"></i>在线率日报</h4>
                         <div class="update-btn">
                           	   <input id="start_date" type="text" placeholder="请选择账期时间……" style="width:160px" value="${yearmmdd}" size="7" class="m-ctrl-medium" onchange="reload();">
                           	   <input id="site" type="text" placeholder="请输入站点名进行搜索……" style="width:200px"  onchange="reload();">
                         	   <button id="findBut" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
							   <button id="clearAll" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;<span style="font-size: 12px;">清空</span></button> 
                         </div>
                    </div>
                     <div class="widget-body">
                     	<table class='table table-bordered table-hover'>
                    		<thead>
			                      <tr>
			                      	  <th nowrap style="width: 10%">账期</th>
			                          <th nowrap style="width: 10%">站点名称</th>
			                          <th nowrap style="width: 10%">设备名称</th>
			                          <th nowrap style="width: 10%">在线率</th>
			                          <th nowrap style="width: 10%">离线次数</th>
			                          <th nowrap style="width: 10%">离线时长</th>
			                          <th nowrap style="width: 10%">告警次数</th>
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
    <script type="text/javascript">
    	 $("#start_date").datepicker({ format: "yyyy-mm-dd" });
    </script>	
</body>	
