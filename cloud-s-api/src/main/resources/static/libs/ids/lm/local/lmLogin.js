/**
 * 
 */
function doKeyLogin() {
	var returnTip = "";
	if (prikeypwd.value == "") {
		alert("请输入密码");
		return false;
	}
	$.keyLocalLogin.loginO.sendParam.password = prikeypwd.value;
	$.keyLocalLogin.loginO.sendParam.random = document.getElementById("random").value;
	
	//key登录判断，登录通过，则将证书信息和签名值都放到self.loginO.sendParam
	var keyHasCer = $.keyLocalLogin.keyHasCer();
	if(keyHasCer != true ){
		showLmMessage(LmMessage.KEY_GET_FAI);
		return ;
	}
	returnTip = $.keyLocalLogin.keyLogin();
	
	if(returnTip != true){
		alert(returnTip)
		return ;
	}
	submitForm();

}

function submitForm(){
	document.form1.cCert.value = $.keyLocalLogin.loginO.sendParam.strCer;
	document.form1.cSign.value = $.keyLocalLogin.loginO.sendParam.strSign;
	document.form1.verType.value = $.keyLocalLogin.loginO.sendParam.verType ;
	document.form1.keyType.value =$.keyLocalLogin.loginO.sendParam.keyType ;
	document.form1.random.value = $.keyLocalLogin.loginO.sendParam.random ;
	document.form1.submit();
}