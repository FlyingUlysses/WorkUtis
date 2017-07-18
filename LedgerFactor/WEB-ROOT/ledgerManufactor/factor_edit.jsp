<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<script src="<%=basePath %>/resources/plugins/jquery.validate.min.js"></script>
<script src="<%=basePath %>/resources/plugins/jquery.validate.messages_cn.js"></script>
<style>
<!--
	.chzn-container{ width: 98% !important; }
-->
</style>
<body>
	<form id="editForm" action="#" class="form-horizontal">
		<input type="hidden" name="factor.id" value="${factor.id}">
		<div class="input-wrap">
	    	 <div class="tLable">厂家名称：</div>
	    	 <input type="text" name="factor.name" class="txt" value="${factor.name}" required />
	    </div>
	    <div class="input-wrap">
	    	 <div class="tLable">名称简写：</div>
	    	<input type="text" name="factor.abbreviation" class="txt" value="${factor.abbreviation}" required />
	    </div>
	    <div class="input-wrap">
	    	 <div class="tLable">联系人：</div>
	    	<input type="text" name="factor.contacts" class="txt" value="${factor.contacts}" required />
	    </div>
	     <div class="input-wrap">
	    	 <div class="tLable">联系电话：</div>
	    	<input type="text" name="factor.mobile" class="txt" value="${factor.mobile}" required />
	    </div>
	     <div class="input-wrap">
	    	 <div class="tLable">厂家地址：</div>
	    	<input type="text" name="factor.address" class="txt" value="${factor.address}" required />
	    </div>
	    <div style="text-align: right; margin-top: 10px;">
	    	<button id="ok" type="button" style="margin-right: 6px;" class="btn btn-success ladda-button" data-style="zoom-in">
	    		<span class="ladda-label" style="font-size: 12px;">保存</span>
	    	</button>
	    </div>
	</form>
    <script type="text/javascript">
    	initFormValid("<%=basePath%>/ledgerManufactor/update",parent.buildManufactorJsTree);
    </script>
</body>