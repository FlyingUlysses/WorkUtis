<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/include.jsp"%>
<link href="<%=basePath %>/resources/bootstrap/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" />
<script src="<%=basePath %>/resources/bootstrap/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<style>
<!--
	.chzn-container-single .chzn-single{ margin-top: 0px !important; }
-->
</style>
<script type="text/javascript">
</script>
<body>
	
		
	<c:forEach items="${specsList}" var="item">
	   	 <div class="input-wrap">
				<div class="tLable"><span style="width: 44%;">${item.name}：</span>
				<c:choose>
                        <c:when test="${!empty item.name2}">
							<span style="width: 48%; float: right;">${item.name2}：</span>
						</c:when>
				</c:choose>
				</div>
				<input type="text"  value="${item.value}"  style="width: 46%;" required  disabled="disabled"/>
				<c:choose>
                        <c:when test="${!empty item.value2}">
							<input type="text" value="${item.value2}"   style="width: 46%; float: right;" required  disabled="disabled"/>
						</c:when>
				</c:choose>
		</div>
    </c:forEach>
	<script type="text/javascript">
    </script>
</body>	