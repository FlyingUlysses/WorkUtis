<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<script src="<%=basePath %>/resources/plugins/jquery.validate.min.js"></script>
<script src="<%=basePath %>/resources/plugins/jquery.validate.messages_cn.js"></script>
<body>
	<form id="editForm" action="#" class="form-horizontal">
		<input type="hidden" name="data_id" value="${person.data_id}">
		<input type="hidden" name="site_id" value="${person.site_id}">
		<input type="hidden" name="device_id" value="${person.device_id}">
		<input type="hidden" name="csr_id" value="${person.csr_id}">
	    <input type="hidden"  class="txt" value="${person.card_gqzl}" readonly="readonly" />
	    <div class="input-wrap">
	    	 <div class="tLable"><span style="width: 42%;">站点名称：</span><span style="width: 48%; float: right;">用户名称：</span></div>
	    	 <input type="text" class="txt" value="${person.site_name}" readonly="readonly" style="width: 45%;"/>
	    	 <input type="text" class="txt" value="${person.name}" readonly="readonly" style="width: 45%; float: right;"/>
	    </div>
	    <div class="input-wrap">
	    	 <div class="tLable"><span style="width: 42%;">充值卡号：</span><span style="width: 48%; float: right;">联系电话：</span></div>
	    	 <input type="text" class="txt" name="cardnum" value="${person.cardnum}" readonly="readonly" style="width: 45%;"/>
	    	 <input type="text" class="txt"  value="${person.mobile}" readonly="readonly" style="width: 45%; float: right;"/>
	    </div>
	    <div class="input-wrap">
	    	 <div class="tLable">购气气量：</div>
	    	 <input type="text" class="txt" value="${person.card_gqzl}" readonly="readonly" />
	   </div>
	    <div class="input-wrap">
	    	 <div class="tLable">剩余气量：</div>
	    	 <input type="text" class="txt" value="${person.card_syql}" readonly="readonly" />
	    </div>
	    <div class="input-wrap">
	    	 <div class="tLable">充值气量：</div>
	    	 <input type="text" name="gas_count" value="${gas_count}" class="txt" readonly="readonly" />
	    </div>
	    <div style="text-align: right; margin-top: 10px;">
	    	<button id="ok" type="button" style="margin-right: 17px;" class="btn btn-success ladda-button" data-style="zoom-in">
	    		<span class="ladda-label" style="font-size: 12px;">提交订单</span>
	    	</button>
	    </div>
	</form>
    <script type="text/javascript">
    	  $(".chzn-select").chosen();
    	  initFormValid("<%=basePath%>/cardCharge/savePay");
    </script>
</body>