<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<style>
	#sites_chzn a{width:336px;}
</style>
<link href="<%=basePath %>/resources/jsTree/themes/default/style.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/my97DatePicker/WdatePicker.js"></script>
<script src="<%=basePath %>/resources/bootstrap/datepicker/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>/resources/plugins/jquery.validate.min.js"></script>
<script src="<%=basePath %>/resources/plugins/jquery.validate.messages_cn.js"></script>
<script src="<%=basePath %>/resources/scripts/pay/cardCharge.js"></script>
<body>
	<div class="container-fluid">
	    <div class="row-fluid">
        	<div class="span3">
        		<div class="widget green" style="margin-bottom: 10px;">
                    <div class="widget-title">
                        <h4><i class="icon-align-left"></i>卡表充值</h4>
                    </div>
                    <div class="widget-body">
                    	<div class="input-wrap">
                    			<div class="tLable">选择站点：<span style="color: red;"></span></div>
                           	    <select id="sites" name="sites"  data-placeholder="请选择站点……" class="chzn-select-deselect" tabindex="-1" onchange="search();">
	                           		<option value="">请选择站点...</option>
	                           		<c:forEach items="${siteList}" var="item">
	                           			<option value="${item.id}">${item.name}</option>
	                           		</c:forEach>
	                           </select>
	                    </div>
					    <input id="site_id" style="font-size: 22px; height: 30px;" class="txt" value="100" hidden/>
                    	<div class="input-wrap">
					    	 <div class="tLable">用户名称：<span style="color: red;"></span></div>
					    	 <input type="text" id="csr_name" style="font-size: 22px; height: 30px;" class="txt" placeholder="请输入用户名称进行搜索……"/>
					    </div>
					    <div class="input-wrap">
					    	 <div class="tLable">站点名称：<span style="color: red;"></span></div>
					    	 <input type="text" id="site_name" style="font-size: 22px; height: 30px;" class="txt" readonly="readonly"/>
					    </div>
					    <div class="input-wrap">
					    	 <div class="tLable">卡表编码：</div>
					    	 <input type="text" id="car_code" style="font-size: 22px; height: 30px;" class="txt" readonly="readonly"/>
					    </div>
 					    <div class="input-wrap">
					    	 <div class="tLable">充值气量：<span style="color: red;"></span></div>
					    	 <input type="number" id="gas_count" style="font-size: 22px; height: 30px;" class="txt" value="100"/>
					    </div>
					    <div class="input-wrap" style="margin-top: 10px;">
					    	<button id="search" class="btn btn-large btn-success"><i class="icon-search"></i>&nbsp; 查询</button>
							<button id="charge" class="btn btn-large btn-success"><i class="icon-yen"></i>&nbsp; 充值</button>
					    </div>
                    </div>
                </div>
        	</div>    
        	<div class="span9">
               <div class="widget widget-tabs green">
                    <div class="widget-title">
                        <h4><i class="icon-align-left"></i>客户信息</h4>
                    </div>
                    <div class="widget-body">
                    	<div class="tabbable">
                    		<ul class="nav nav-tabs">
                                		<li><a href="#widget_tab2" data-toggle="tab" onclick="findLogCharge();">充值记录</a></li>
                                		<li class="active"><a href="#widget_tab1" data-toggle="tab">当前信息</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="widget_tab1">
			                         <div class="widget-body">
			                    	<table class="table table-striped table-bordered table-hover">
						                  <thead>
						                      <tr>	
						                      	  <th style="width: 25px;"></th>
						                          <th  style="text-align: center; vertical-align: middle;">站点名称</th>
						                          <th  style="width: 15%; text-align: center; vertical-align: middle;">设备名称</th>
						                          <th style=" width: 15%;text-align: center; vertical-align: middle;">卡表编码</th>
						                          <th style=" width: 11%;text-align: center; vertical-align: middle;">用户名称</th>
						                          <th style=" width: 11%;text-align: center; vertical-align: middle;">联系人</th>
						                          <th style=" width: 11%;text-align: center; vertical-align: middle;">联系电话</th>
						                          <th style=" width: 11%; text-align: center; vertical-align: middle;">购气总量</th>
						                          <th style=" width: 11%; text-align: center; vertical-align: middle;">剩余气量</th>
						                      </tr>
						                 </thead>
						                 <tbody id="rowBody1"></tbody>
						             </table>
						             <div class="pagination pagination-right">
			                             <ul id="pageUL1"></ul>
			                         </div>
                   				 </div>
                        	</div>
                        		<div class="tab-pane" id="widget_tab2">
			                         <div class="widget-body">
			                    	<table class="table table-striped table-bordered table-hover">
						                  <thead>
						                      <tr>	
						                      	  <th style="width: 25px; vertical-align: middle;"></th>
						                          <th  style="text-align: center; ">订单</th> 
						                          <th  style="width: 11%; text-align: center; ">用户名称</th> 
						                          <th  style="width: 11%; text-align: center; ">卡表编码</th> 
						                          <th style=" width: 11%;text-align: center; ">充值前</th>
						                          <th style=" width: 11%;text-align: center; ">充值气量</th>
						                          <th style=" width: 11%;text-align: center; ">充值后</th>
						                          <th style=" width: 11%; text-align: center; ">充值时间</th>
						                          <th style=" width: 11%; text-align: center; ">状态</th>
						                          <th style=" width: 11%; text-align: center; vertical-align: middle;">操作</th>
						                      </tr>
						                 </thead>
						                 <tbody id="rowBody2"></tbody>
						             </table>
						             <div class="pagination pagination-right">
			                             <ul id="pageUL2"></ul>
			                         </div>
                   				 </div>
                        		</div>
                        	</div>
                        </div>
                    </div>
                </div>
                
                </div>
        	</div>
		</div>
<script type="text/javascript">
	$(".chzn-select-deselect").chosen({ allow_single_deselect:true });
</script>
</body>	
