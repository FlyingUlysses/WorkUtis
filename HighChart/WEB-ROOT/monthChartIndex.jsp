<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/jsTree/jstree.min.js"></script>
<script src="<%=basePath %>/resources/Highcharts/highcharts.js"></script>
<script src="<%=basePath %>/resources/my97DatePicker/WdatePicker.js"></script>
<script src="<%=basePath %>/resources/scripts/rpt/monthChartIndex.js"></script>
<style>
	.widget { clear: none; }
</style>
<body>
	<div class="container-fluid">
		<div class="row-fluid" style="margin-top: 6px;">
			<c:choose>
				<c:when test="${company.cnt > 0}">
					<div style="float: left; display: none; width: 235px;" id="compDiv">
						<div class="widget green">
		                    <div class="widget-title">
		                        <h4><i class="icon-align-left"></i>公司架构</h4>
		                    </div>
		                    <div class="widget-body">
		                        <div id="jstree"></div>
		                    </div>
		                </div>
		            </div>
		            <div style="margin-left: 0px;" id="listDiv">
				</c:when>
				<c:otherwise>
					 <div>
				</c:otherwise>
			</c:choose>
			<div class="widget green" style="margin-bottom: 5px;">
				<div class="widget-title">
                    <h4><i class="icon-align-left"></i><span id="title">【${company.name}】</span>同比/环比月报</h4>
	            	<div class="update-btn">
	            		<c:if test="${company.cnt > 0}">
	            		 	<button id="expand" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-expand-alt"></i>&nbsp;<span style="font-size: 12px;">公司</span></button>
	            		</c:if>
	            		<button id="search" type="button" style="margin-bottom: 10px;" class="btn btn-warning"><i class="icon-search"></i>&nbsp;<span style="font-size: 12px;">查询</span></button>
                          <input type="text" id="start_date" value="${month}"  onFocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM',alwaysUseStartDate:true})"/>                        	 	 
                        <select id="site" name="site" tabindex="-1" onchange="commonReload();">
                       	   <option value="">请选择站点...</option>
                        	<c:forEach items="${siteList}" var="item">
                        		<option value="${item.id}">${item.name}</option>
                        	</c:forEach>
	                      </select> 	 	 
                        	
                         <select id="compare" name="compare" tabindex="-1" onchange="commonReload();">
                        		<option value="TB">同比</option>
                        		<option value="HB" selected="selected">环比</option>
	                      </select> 
	                      
	                      <select id="factor" name="factor" tabindex="-1" onchange="commonReload();">
                        	<c:forEach items="${factor}" var="item">
                        		<option value="${item.code}">${item.name}</option>
                        	</c:forEach>
	                      </select>  
                    </div>
                </div>
                <div class="widget-body">
                   	<div id="monthDiv" style="height: 320px;"></div>  
                </div>
           </div>
		</div>
	</div>
</body>	
