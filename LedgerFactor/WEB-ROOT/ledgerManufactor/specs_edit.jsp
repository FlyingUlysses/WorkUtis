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
		<input type="hidden" name="specs.id" value="${specs.id}">
		 <div class="input-wrap">
	    	 <div class="tLable"><span style="width: 46%;">规格类型：</span></div>
             <select  name="specs.code"  data-placeholder="规格类型" class="chzn-select" tabindex="-1" required>
                 <c:forEach items="${codeList}" var="item">
					<c:choose>
                        <c:when test="${!empty specs.code && specs.code == item.code}">
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
		    <div class="tLable"><span style="width: 46%;">所属设备名称：</span></div>
	            <input type="text"   class="txt" value="${equip.name}"  class="txt" style="width: 87%;required="" disabled="disabled">
		    </div>
	 </div>
	   <div class="input-wrap">
		    <div class="tLable"><span style="width: 46%;">所属设备型号：</span></div>
	            <input type="text"   class="txt" value="${equip.model}"  class="txt" style="width: 87%;required="" disabled="disabled">
	            <input type="hidden" id ="equip_id" name="specs.equip_id" class="txt" value="${equip.id}">
		    </div>
	    </div>
		<div class="input-wrap">
	    	 <div class="tLable"><span style="width: 42%;">规格数值：</span></div>
	    	 <input type="text" name="specs.value" class="txt" value="${specs.value}" style="width: 87%;" required="">
	    </div>
		<div class="input-wrap">
	    	 <div class="tLable"><span style="width: 46%;">单位：</span></div>
	    	 <input type="text"  name="specs.unit" class="txt" value="${specs.unit}"  class="txt" style="width: 87%;required="">
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
    	 initFormValid("<%=basePath%>/ledgerManufactor/specsUpdate",$("#equip_id").val());
    	 
  function initFormValid(url,callback){
	$("#editForm").validate({
		highlight: function(element,errorClass){
			$(element).css("border-color","#B94A48");
			$("html,body").animate({scrollTop:0},200);
		},
		unhighlight: function(element,errorClass){
			$(element).css("border-color","#ccc");
		},
		errorPlacement: function(error,element){
			true;
		}
	});
	if(("#ok").length){
		var index = parent.layer.getFrameIndex(window.name);
		var clickTag = 0; 
		$("#ok").click(function(){
			var valid = $('#editForm').valid();
			if(valid && window.otherValidate != undefined)
				valid = otherValidate();
			if(!valid)
				return false;
			if(Utils.isEmpty(url)){
				layer.alert("未获取提交路径，请重试!");
				return false;
			}
			if(clickTag == 0){
				clickTag = 1;
				var loadding = layer.load(1, { shade: [0.1,'#fff'] });
				var ladda = Ladda.create(document.querySelector('#ok'));
				ladda.start();
				$.post(url, $('#editForm').serialize(), function(data, status) {
					ladda.stop();
					layer.close(loadding);
					clickTag = 0;
					layer.alert(data.message,function(mIndex){
						if(data.success){
							if(callback != undefined){
								parent.reloadRecord2(callback);
							}
							parent.layer.close(index);
						}else
							layer.close(mIndex);	
					});
				});
			}
		});
	}
}

    	 
    </script>
</body>