<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/miningtags.css"/>
<script src="javascript/jquery-1.11.2.min.js"></script>
<title>url输入</title>
</head>
<body>
<div align="center">
${requestScope.message}
<s:form action="Login" method="post">
<s:textfield name="url1" id="url1" label="url1"/><br/>
<s:textfield name="url2" id="url2" label="url2"/>
<s:submit value="提交"/>
<textarea id="text"></textarea>
</s:form>
<input type="button" name="111" id="111"/>

</div>
<script>
$("#111").click(function(){$.get("mining", function(data){
	  
	  $("#text").val(data);
});})
</script>
</body>
</html>