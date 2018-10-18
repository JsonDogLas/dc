<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<%@ include file="../../../p/decorators/base.jsp"%>
<%@ include file="../../../p/decorators/lmLoginHeader.jsp" %>
</head>

<body>
	<div align="center">
		<input type="hidden" id="sid" name="sid">
		<font size="4"><b>Password:<input type="password"
				name="prikeypwd" id="prikeypwd" value="" size="20"></b></font> <input
			type="button" value="Enter" onclick="doKeyLogin();">
		
		<form name="form1" method="post" action="server.jsp">
			<input type="hidden" name="cCert" > 
			<input type="hidden" name="cSign" >
			<input type="hidden" name="verType" >
			<input type="hidden" name="keyType" >
			<input type="hidden" id="random"  name="random" value= "${mt:signRandom()}" >
		</form>


	</div>
</body>
</html>
