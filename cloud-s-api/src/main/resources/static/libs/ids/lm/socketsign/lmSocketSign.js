/**
 * 
 */
//先初始化lmInit
$(function(){
    RequestFunctions.getSocketIpAndPort();
	lmParam.clientVersion = $("#clientVersion").val();
	if( !cLMKey.hasEngine ){
		lmParam.verFlag = false;
		//showLmMessage(LmMessage.KEY_LOAD);
		return;
	}
	if( cLMKey.getLastError() !=""){
		//showLmMessage(LmMessage.KEY_GET_FAI);
		return;
	}
	initSign();
})

function initSign(){
	if(cLMKey.hasEngine){
        if(cLMKey.keys.length >0 && cLMKey.keys[0].cerInfo !== undefined) {
            lmParam.isSm2 = cLMKey.keys[0].cerInfo.isSm2 ? 2 : 1;
            lmParam.DigestMethod = cLMKey.hashDigestType;
            lmParam.keyId = cLMKey.keys[0].cerInfo.cerSerialId;
            lmParam.cerBase64Str = cLMKey.keys[0].cerInfo.cerBase64Str;
            lmParam.hwType = cLMKey.hwType;
        }else{
            cLMKey.hasEngine = false;
            lmParam.verFlag = false;
        }
	}else{
		lmParam.verFlag = false;
	}
}

//请求socket连接
function requestStart(){
	/*if(!cLMKey.login(lmParam.pass)){
		showLmMessage(LmMessage.ENTER_PASS_ERROR1+LmMessage.ENTER_PASS_ERROR2+lmParam.passErrorTime+LmMessage.ENTER_PASS_ERROR3);
		lmParam.passErrorTime --;
	}*/
	//发起连接
	var flag =cLMKey.connectSocket(lmParam.socketIp,lmParam.socketPort);
	if(flag){
		lmParam.connectSuccess = true;
		var requestType = '1';
		var paramJson = SignParams.generateParam(requestType,'start','',lmParam.socketIp,lmParam.socketPort,lmParam.clientVersion);
		var result=cLMKey.connectSocketSend(paramJson);
		if(result != "" && result.data){
			//接收服务端返回数据
			var responseData = JSON.parse(result.data);
			if(result.type == requestType){
				console.log(LmMessage.SOCKET_REQUEST_SUCCESS);
			}else{
				console.log(LmMessage.SOCKET_REQUEST_FAIL);
				if(lmParam.signFlagFunction !=null){
					lmParam.signFlagFunction();
				}
			}
		}
		
		return;
	}else{
		console.log(LmMessage.SOCKET_REQUEST_FAIL);
		if(lmParam.signFlagFunction !=null){
			lmParam.signFlagFunction();
		}
	}
	
}

//真正处理签名
function dealSocketSign(requestType,signed){
	var paramJson = SignParams.generateSendServerParam(requestType,signed,$("#clientSignFileId").val(), lmParam.socketIp,lmParam.socketPort,lmParam.clientVersion);
	var result=cLMKey.requestSign(paramJson);
	if(result != "" && result.data){
		//接收服务端返回数据
		var responseData = JSON.parse(result.data);
		if(result.type == requestType){
			console.log(LmMessage.SOCKET_SIGN_SUCCESS)
			return true;
		}else{
			console.log(LmMessage.SOCKET_SIGN_FAIL);
			if(lmParam.signFlagFunction !=null){
				RequestFunctions.clearUserKeyInfo();
				lmParam.signFlagFunction();
			}
			return false;
		}
	}else{
		if(lmParam.signFlagFunction !=null){
			RequestFunctions.clearUserKeyInfo();
			lmParam.signFlagFunction();
		}
		return false;
	}
}

function requestHashAndSign(signKeyType,signType,attach){
	if(!lmParam.connectSuccess){
		console.log(LmMessage.SOCKET_REQUEST_FIRST);
		if(lmParam.signFlagFunction !=null){
			lmParam.signFlagFunction();
		}
		return;
	}
	var requestType = $("#clientHashType").val();
	var paramJson = SignParams.generateParam(requestType,$("#clientHashMethod").val(),$("#clientSignFileId").val(), lmParam.socketIp,lmParam.socketPort,lmParam.clientVersion);
	
	var result=cLMKey.requestHash(paramJson);
	if(result != "" && result.data){
		//接收服务端返回数据
		var responseData = JSON.parse(result.data);
		if(result.type == requestType){
			//获取签名值
			var signed=cLMKey.sign(responseData.hash,signType,attach);
			if(signed == false){
				console.log(LmMessage.SOCKET_SIGN_FAIL);
				if(lmParam.signFlagFunction !=null){
					RequestFunctions.clearUserKeyInfo();
					lmParam.signFlagFunction();
				}
				return false;
			}else{
				return dealSocketSign(signKeyType,signed);
			}
		}else{
			console.log(LmMessage.SOCKET_SIGN_FAIL);
			if(lmParam.signFlagFunction !=null){
				RequestFunctions.clearUserKeyInfo();
				lmParam.signFlagFunction();
			}
		}
	}else{
		if(lmParam.signFlagFunction !=null){
			RequestFunctions.clearUserKeyInfo();
			lmParam.signFlagFunction();
		}
	}
	return false;
}
//请求hash同时p1rsa签名
function requestHashAndP1RSASign(){
	return requestHashAndSign( $("#clientResponseP1RSASignedType").val(),1,0);
}
//请求hash同时p1sm2签名
function requestHashAndP1SM2Sign(){
	return requestHashAndSign( $("#clientResponseP1SM2SignedType").val(),1,0);
}
//关闭socket
function closeSocket(){
	if(!lmParam.connectSuccess){
		console.log(LmMessage.SOCKET_REQUEST_FIRST);
		if(lmParam.signFlagFunction !=null){
			lmParam.signFlagFunction();
		}
		return;
	}
	var res=cLMKey.disconnectSocket();
	if(res){
		lmParam.connectSuccess = false;
		console.log(LmMessage.SOCKET_UNCONNECT_SUCCESS);
	}
}

/**
 * 判断KEY是否正常
 */
function checkCanSign(){
	cLMKey.initObject();
	if(!cLMKey.hasEngine){
		RequestFunctions.clearUserKeyInfo();
		showLmMessage(LmMessage.KEY_LOAD_FAIL);
		if(lmParam.signFlagFunction !=null){
			lmParam.signFlagFunction("1");
		}
		lmParam.verFlag = false;
		return false;
	}else{
		initSign();
	}
	return true;
}

/**
 * 判断KEY是否正常
 */
function checkCanKeySign(){
	cLMKey.initObject();
	if(!cLMKey.hasEngine){
		RequestFunctions.clearUserKeyInfo();
		if(lmParam.signFlagFunction !=null){
			lmParam.signFlagFunction("1");
		}
		lmParam.verFlag = false;
		return false;
	}else{
		initSign();
	}
	return true;
}