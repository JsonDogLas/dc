/**
 * 证书申请用
 */
//先初始化lmInit
$(function(){
    lmParam.clientVersion = $("#clientVersion").val();
    if( !cLMKey.hasEngine ){
        return;
    }
    if( cLMKey.getLastError() !=""){
        return;
    }
    initSign();
})

function initSign(){
    if(cLMKey.hasEngine) {
        if (cLMKey.keys.length > 0){
            lmParam.deviceName = cLMKey.keys[0].deviceName;
        }
        lmParam.verFlag = true;
    }else{
        lmParam.verFlag = false;
    }
}

//请求socket连接
function requestCertStart(){
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
                return true;
            }else{
                console.log(LmMessage.SOCKET_REQUEST_FAIL);
                return false;
            }
        }
    }else{
        console.log(LmMessage.SOCKET_REQUEST_FAIL);
        return false;
    }

}

/**
 * 申请证书
 * @param options
 * @returns {*}
 */
function requestSignCert(certInfo){
    var options = lmParam.lmP10ParamOptions;
    if(!lmParam.connectSuccess){
        console.log(LmMessage.SOCKET_REQUEST_FIRST);
        return null;
    }
    //申请P10
    if(cLMKey.hasEngine) {
        var p10 = cLMKey.clmCer.requestP10(options);
        var publicKey = cLMKey.clmCer.exportPubKey(options);
        if(p10 == null){
            showLmMessage(LmMessage.APPLY_P10_FAIL);
            return null;
        }
        //请求通信，P10到后台获取证书
        var requestType = $("#clientRequestFromP10ForSignCertType").val();
        //certAppId 证书申请Id == json中的fileId，用于标识
        var paramJson = SignParams.generateApplyCertParam(requestType,$("#clientRequestFromP10ForSignCertMethod").val(),
            certInfo.appId,p10, lmParam.socketIp,lmParam.socketPort,lmParam.clientVersion);
        var result = cLMKey.clmCer.requestFromP10ForSignCert(paramJson);
        if(result != "" && result.data) {
            //接收服务端返回数据
            var responseData = JSON.parse(result.data);
            if (result.type == requestType) {
                console.log(LmMessage.APPLY_FROM_P10_FOR_CER_SUCCESS)
                return responseData.signCert;
            }
        }
        showLmMessage(LmMessage.APPLY_FROM_P10_FOR_CER_FAIL);
        return null;
    }else{
        showLmMessage(LmMessage.KEY_LOAD);
        return null;
    }
}

/**
 * 申请证书
 * 1、先申请P10
 * 2、通过P10,socket通信到后台，通过凯塞服务器获取证书，打入key
 */
function writeSignCertToKey(certInfo){
    var signCert = requestSignCert(certInfo);
    if(signCert != null){
       var re = cLMKey.clmCer.importSignCert(signCert,lmParam.lmP10ParamOptions.container);
       return re;
    }
    return false;
}