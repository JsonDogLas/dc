//先初始化lmInit
$(function(){
    if( !cLMKey.hasEngine ){
        lmParam.verFlag = false;
        return;
    }
    if( cLMKey.getLastError() !=""){
        return;
    }
    initKey();
});

function initKey(){
    if(cLMKey.hasEngine){
        if(cLMKey.keys.length >0 && cLMKey.keys[0].cerInfo !== undefined) {
            lmParam.isSm2 = cLMKey.keys[0].cerInfo.isSm2 ? 2 : 1;
            lmParam.DigestMethod = cLMKey.hashDigestType;
            lmParam.keyId = cLMKey.keys[0].cerInfo.cerSerialId;
            lmParam.hwType = cLMKey.hwType;
            lmParam.cerBase64Str = cLMKey.keys[0].cerInfo.cerBase64Str;
            lmParam.verFlag = true;
        }else{
            lmParam.verFlag = false;
        }
    }else{
        lmParam.verFlag = false;
    }
}

//对外方法
var LMKey= {
    //判断密码输入是否匹配
    checkInputPassword:function (pwd) {
        var returnCode = cLMKey.login(pwd);
        //登录成功
        if (returnCode == 0) {
            return true;
        } else {
            if (returnCode == 1) {
                showLmMessage(LmMessage.KEY_LOGIN_FAIL);
            } else if (returnCode == 2) {
                showLmMessage(LmMessage.KEY_LOGIN_PSW_INIT_NOT_CHANGE);
            }
            return false;
        }
    },
    resetUserPin : function(newPin){
        var adminPin = $("#adminPin").val();
        var re = cLMKey.resetUserPin(adminPin,newPin);
        if(re){
            showLmMessage(LmMessage.PSW_RESET_SUCCESS);
        }else{
            showLmMessage(LmMessage.PSW_RESET_FAIL);
        }
        return re;
    }
}
