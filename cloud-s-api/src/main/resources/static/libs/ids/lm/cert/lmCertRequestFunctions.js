var LMCertRequestUrls={
    getSocketIpAndPortUrl:"/user/manage/certificate/getSocketIpAndPort",
    requestSocketAppInfoForP10:"/user/manage/certificate/request_socket_app_info_for_p10"
}

//业务相关请求方法
var CertRequestFunctions = {
    checkKeyCanUse:function () {
        if( !cLMKey.hasEngine ){
            showLmMessage(LmMessage.KEY_LOAD);
            return false;
        }
        return true;
    },
    /**
     * 获取Socket服务器Ip和端口，请求socket连接
     */
    getSocketIpAndPort: function () {
        $.ajax({
            type: "get",
            dataType: "json", // 发送的参数
            url: LMCertRequestUrls.getSocketIpAndPortUrl, //  连接地址
            success: function (r) {
                if (r.result) {
                    if (r.attributes) {
                        for (var k = 0; k < r.attributes.length; k++) {
                            if (r.attributes[k].SERVER_IP) {
                                lmParam.socketIp = r.attributes[k].SERVER_IP;
                            }
                            if (r.attributes[k].SERVER_PORT) {
                                lmParam.socketPort = r.attributes[k].SERVER_PORT;
                            }
                        }
                        requestCertStart();
                    } else {
                        console.log(LmMessage.SOCKET_GETINFO_FAIL);
                        showLmMessage(LmMessage.SOCKET_SERVER_INFO);
                    }
                }
            }
        });
    },
    //同步请求后台,获取生成p10需要的参数
    requestAppInfoForP10Function : function (appId,enSn) {
        $.ajax({
            type: "GET",
            dataType: "json",
            async: false,
            url: LMCertRequestUrls.requestSocketAppInfoForP10 + "?appId=" + appId+"&enSn="+enSn,
            success: function (r) {
                if (r.result) {
                    lmParam.lmP10ParamOptions = r.attributes[0].requestParamForP10;
                    lmParam.lmP10ParamOptions = _Base64decode(lmParam.lmP10ParamOptions);
                    lmParam.lmP10ParamOptions = JSON.parse(lmParam.lmP10ParamOptions);
                    lmParam.lmP10ParamOptions.deviceName = lmParam.deviceName;
                } else {
                    closeLayerLoading();
                    showLmMessage(r.message);
                }
            },
            error:function(){
                closeLayerLoading();
            }
        });
    },
    //写入证书
    writeSignCertToKeyFunction : function(certInfo){
       return writeSignCertToKey(certInfo);
    }
}