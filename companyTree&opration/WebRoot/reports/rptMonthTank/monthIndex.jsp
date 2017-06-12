<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/bootstrap/datepicker/datepicker.css" rel="stylesheet" />
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<script src="<%=basePath %>/resources/my97DatePicker/WdatePicker.js"></script>
<script src="<%=basePath %>/resources/scripts/rpt/rptMonthTank.js"></script>
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
                        <h4><i class="icon-align-left"></i>月报表</h4>
                         <div class="update-btn">
                         	   <button id="findBut" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
                         	   <button id="rt_export" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-download"></i>&nbsp;<span style="font-size: 12px;">导出</span></button>
							   <button id="clearAll" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;<span style="font-size: 12px;">清空</span></button> 
                           	   <input type="text" id="start_date" placeholder="选择开始时间点" value="${month}" style="width:100px" onFocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM',alwaysUseStartDate:true})"/>
                           	   <input type="text" id="end_date" placeholder="选择结束时间点" value="${month}"  style="width:100px" onFocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM',alwaysUseStartDate:true})"/>
	                           <select id="site" name="site" tabindex="-1" onchange="reloadRecord();" style="width:100px" >
	                           		<option value="">请选择站点...</option>
	                           		<c:forEach items="${siteList}" var="item">
	                           			<option value="${item.id}">${item.name}</option>
	                           		</c:forEach>
	                           </select>
                         </div>
                    </div>
                     <div class="widget-body">
                     	<table class='table table-bordered table-hover'>
                    		<thead>
			                      <tr>
			                          <th nowrap style="width: 10%">公司名称</th>
			                          <th nowrap style="width: 10%">站点名称</th>
			                          <th nowrap style="width: 10%">周期</th>
			                          <th nowrap style="width: 10%">累计充装量（kg）</th>
			                          <th nowrap style="width: 10%">流量计累计流量(m³）</th>
			                          <th nowrap style="width: 10%">卡控表总用量（m³）</th>
			                          <th nowrap style="width: 10%">气化率 m³|kg</th>
			                          <th nowrap style="width: 10%">在线率n%</th>
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
    </script>	
</body>	
