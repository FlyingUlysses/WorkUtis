<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'index.jsp' starting page</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <script src="<%=basePath %>script/jquery-1.8.3.min.js"></script>
  </head>
  
  <body>
   	<input id="address" placeholder="请输入收件邮箱地址……"/>
   	<input id="title"placeholder="请输入标题……"/>
   	<input id="content" placeholder="请输入内容……"/>
   	<button type="button" style="margin-bottom: 10px;"  onclick="senMail();">发送邮件</button>
  </body>
  
  <script type="text/javascript">
  	function senMail(){
  		var title=$("#title").val();
  		var content=$("#content").val();
  		var address=$("#address").val();
  		var url ="<%=basePath %>send?address="+address+"&content="+content+"&title="+title;
  		alert(url);
  	}
  </script>
</html>
