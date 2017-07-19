<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<script src="<%=basePath %>/resources/plugins/jquery.validate.min.js"></script>
<script src="<%=basePath %>/resources/plugins/jquery.validate.messages_cn.js"></script>
<script type="text/javascript">
</script>
<style>
<!--
		.chzn-container{ width: 245px !important; }
		.chzn-container-single .chzn-single { margin-top: -1px !important; }
-->
</style>
<body>
	<form id="editForm" action="#" class="form-horizontal">
		<input type="hidden" name="equip.id" value="${equip.id}">
		 <div class="input-wrap">
	    	 <div class="tLable"><span style="width: 46%;">选择厂家：</span></div>
             <select  name="equip.manufactor"  data-placeholder="选择厂家" class="chzn-select" tabindex="-1" required>
                 <c:forEach items="${manufactors}" var="item">
					<c:choose>
                        <c:when test="${!empty factor_id && factor_id == item.id}">
                 	        <option value="${item.id}" selected="selected" >${item.name}</option>
                  	 	</c:when>
                  	 	<c:otherwise>
                 	        <option value="${item.id}" >${item.name}</option>
                  	 	</c:otherwise>
                 	</c:choose>
                 </c:forEach>
             </select>
	    </div>
	    <div class="input-wrap">
	    	 <div class="tLable"><span style="width: 46%;">设备类型：</span></div>
             <select  name="equip.deviceType"  data-placeholder="选择设备类型" class="chzn-select" tabindex="-1" required>
                 <c:forEach items="${typeList}" var="item">
					<c:choose>
                        <c:when test="${!empty equip.deviceType && equip.deviceType == item.code}">
                 	        <option value="${item.code}" selected="selected" >${item.name}</option>
                  	 	</c:when>
                  	 	<c:otherwise>
                 	        <option value="${item.code}" >${item.name}</option>
                  	 	</c:otherwise>
                 	</c:choose>
                 </c:forEach>
             </select>
	    </div>
		<div class="input-wrap">
	    	 <div class="tLable"><span style="width: 42%;">设备型号：</span></div>
	    	 <input type="text" name="equip.model" class="txt" value="${equip.model}" style="width: 87%;" required="">
	    </div>
		<div class="input-wrap">
	    	 <div class="tLable"><span style="width: 46%;">设备名称：</span></div>
	    	 <input type="text"  name="equip.name" class="txt" value="${equip.name}"  class="txt" style="width: 87%;required="">
	    </div>	    
	    <div style="text-align: right; margin-top: 10px;">
	    	<button id="ok" type="button" style="margin-right: 20px;" class="btn btn-success ladda-button" data-style="zoom-in">
	    		<span class="ladda-label" style="font-size: 12px;">保存</span>
	    	</button>
	    </div>
	</form>
    <script type="text/javascript">
    		 $(".chzn-select").chosen();
    	 $(".chzn-select-deselect").chosen({
    	  		allow_single_deselect:true
    	 });
    	 initFormValid("<%=basePath%>/ledgerManufactor/equipUpdate",parent.reloadRecord1);
    </script>
</body>