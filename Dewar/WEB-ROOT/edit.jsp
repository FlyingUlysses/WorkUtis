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
		<input type="hidden" name="dewar.id" value="${dewar.id}">
		<input type="hidden" name="dewar.company" value="${company}">
		 <div class="input-wrap">
	    	 <div class="tLable"><span style="width: 46%;">设备型号：</span></div>
             <select name="dewar.equip_id"  data-placeholder="规格类型" class="chzn-select" tabindex="-1" required onchange="select_code();">
                 <c:forEach items="${equipList}" var="item">
					<c:choose>
                        <c:when test="${!empty dewar.equip_id && dewar.equip_id == item.id}">
                 	        <option value="${item.id}" selected="selected" >${item.model}</option>
                  	 	</c:when>
                  	 	<c:otherwise>
                 	        <option value="${item.id}" >${item.model}</option>
                  	 	</c:otherwise>
                 	</c:choose>
                 </c:forEach>
             </select>
	    </div>
	   	<div class="input-wrap">
	    	 <div class="tLable"><span style="width: 42%;">容器编码：</span></div>
             <input  type="text" name="dewar.dewar" class="txt" value="${dewar.dewar}" style="width: 87%;" required="" >
	    </div>
		<div class="input-wrap">
	    	 <div class="tLable"><span style="width: 42%;">传感器编码：</span></div>
	    	 <input type="text" name="dewar.sensor" class="txt" value="${dewar.sensor}" style="width: 87%;" required="">
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
    	 initFormValid("<%=basePath%>/dewar/update");  	 
    </script>
</body>