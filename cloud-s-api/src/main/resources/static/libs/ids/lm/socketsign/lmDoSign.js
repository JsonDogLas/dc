/**
 *
 */
//customSign调用
//先保存文档相关信息到签名队列
//获取文档hash然后签名

/***
 *
 * derfer 对象
 * 		若密码已经验证过，直接触发 延迟发送对象sendDf.resolve
 * 		密码没有验证过，触发弹出层对象promtDf
 * 			输入密码验证，点击确定出发promtDf.notify，执行progress方法验证密码
 * 				验证成功触发promtDf.resolve
 * 				从而触发sendDf.resolve
 *
 */
socketSign = function (fileData,stampData,lmFunctionParam){
    if(!checkCanSign()){
        lmFunctionParam.idsLoad.close();
        return ;
    }
    var rr = RequestFunctions.checkUserKeyHasPutIn();
    var defer = $.Deferred();
    //弹出层延迟对象
    var promtDf = $.Deferred();
    //真正发界面对象到后台的延迟对象
    var sendDf = $.Deferred();

    lmParam.fileData = fileData;
    lmParam.stampData = stampData;
    lmParam.lmFunctionParam = lmFunctionParam;
    lmParam.idsLoad = lmFunctionParam.idsLoad;
    defer.done(promtLm);
    defer.resolve(rr);

//--------------------------------------------promp-----------------------
    var pswOk = false;
    var layerIndex = 1;
    //弹出prompt层
    function promtLm(result){
        if(result){
            sendDf.resolve(result)
        }else{
            //普通Key
            if(lmParam.hwType == 1 ) {
                layer.prompt({
                        title: LmMessage.ENTER_PASS, formType: 1, btn: [LmMessage.PSW_CONFIRM_OK],
                        cancel: function () {
                            if (lmParam.signFlagFunction != null) {
                                lmParam.signFlagFunction("1");
                            }
                            lmParam.idsLoad.close();
                        }, success: function () {
                            $(".layui-layer-ico").css("background-image", "url(/libs/layer/css/icon1.png)");
                        }
                    },
                    function (content, index) {
                        lmParam.pass = content;
                        layerIndex = index;
                        //触发执行弹出层验证密码progress
                        promtDf.notify(lmParam.pass);
                        if (pswOk) {
                            closeLayerPrompt(layerIndex);
                            //密码验证正确触发done方法
                            setTimeout(function () {

                                promtDf.resolve(true);
                            }, 500);
                        }
                    });
                $(".layui-layer-btn a.layui-layer-btn1").hide();
            }else  if(lmParam.hwType == 2 ) { //指纹Key
                showLmMessage(LmMessage.KEY_FPLOGIN_TIP);
                setTimeout(function(){
                    doVerify();
                    if(pswOk) {
                        setTimeout(function () {
                            promtDf.resolve(true);
                        }, 500);
                    }

                },500)
            }
        }
    }
    promtDf.progress(function () {
        doVerify();
    });

    promtDf.done(function (flag) {
        //触发真正执行发送数据到后台的方法
        sendDf.resolve(flag)
    });

    function doVerify(){
        var loginFlag = 99;
        //普通Key
        if(lmParam.hwType == 1 ) {
            loginFlag = cLMKey.login(lmParam.pass);
        }else  if(lmParam.hwType == 2 ) { //指纹Key
            showLmMessage(LmMessage.KEY_FPLOGIN_TIP);
            loginFlag = cLMKey.FPLogin();
        }
        //成功
        if(loginFlag == 0){
            pswOk = true;
            RequestFunctions.saveUserKeyInfo();
            if (promtDf.inputPasswordError) {
                var _idsLoad = lmParam.idsLoad.open();
                lmParam.idsLoad = _idsLoad;
            }
        }else {
            //普通Key
            if(lmParam.hwType == 1 ) {
                var offset = $(".layui-layer-title").offset();
                offset.top = offset.top - 50;
                //未初始化密码
                if (loginFlag == 2) {
                    showLmMessageOffset(LmMessage.PSW_INIT_NOT_CHANGE, offset.top, offset.left);
                    if (lmParam.signFlagFunction != null) {
                        lmParam.signFlagFunction();
                    }
                    pswOk = false;
                    closeLayerPrompt(layerIndex);
                    lmParam.idsLoad.close();
                } else if (loginFlag == 1) {
                    pswOk = false;
                    showLmMessageOffset(LmMessage.ENTER_PASS_ERROR1 + LmMessage.ENTER_PASS_ERROR2 +
                        lmParam.passErrorTime + LmMessage.ENTER_PASS_ERROR3, offset.top, offset.left);
                    lmParam.passErrorTime--;
                    promtDf.inputPasswordError = true;
                }
            }else  if(lmParam.hwType == 2 ) { //指纹Key
                //未初始化密码
                if (loginFlag == 2) {
                    showLmMessage(LmMessage.PSW_INIT_NOT_CHANGE);
                    if (lmParam.signFlagFunction != null) {
                        lmParam.signFlagFunction();
                    }
                    pswOk = false;
                    lmParam.idsLoad.close();
                } else if (loginFlag == 1) {
                    pswOk = false;
                    if (lmParam.signFlagFunction != null) {
                        lmParam.signFlagFunction();
                    }
                    lmParam.idsLoad.close();
                }
            }
        }
    }
//--------------------------------------------promp end-----------------------

//--------------------------------------------sendDf -----------------------
    sendDf.done(function(result){
        if(!result){
            showLmMessage(LmMessage.ENTER_PASS_ERROR4);
            window.location.href=window.location.href;
            return ;
        }
        var cert= cLMKey.keys[0].cerInfo.cerBase64Str;
        cert = encodeURI(cert);
        if(cert !=''){
            cert = cert.replace(/\+/g,"%2B");
            cert = cert.replace(/\&/g,"%26");
        }
        var sendData = {
            'fileData':JSON.stringify(lmParam.fileData),
            'stampData':JSON.stringify(lmParam.stampData),
            'cerBase64Str':cert,
            'isSm2':lmParam.isSm2,
            'digestMethod':lmParam.DigestMethod
        }
        var flag = false;
        $.ajax({
            type: "POST",
            url:  RequestUrls.saveToSignList,
            dataType: "json",
            data:sendData,
            async:false,
            success: function(r){
                if(r.result){
                    var fileId = r.data;
                    if(isNullOrEmpty(fileId)){
                        console.log(LmMessage.SOCKET_GET_SIGN_FILE_FAIL);
                        return;
                    }
                    $("#clientSignFileId").val(fileId);

                    //rsa
                    if(lmParam.isSm2 == 1){
                        flag = requestHashAndP1RSASign();
                    }else{
                        flag = requestHashAndP1SM2Sign();
                    }
                }
            }
        });
        if(flag){
            if(typeof(lmParam.lmFunctionParam.defSuccessFunction) === 'function'){
                lmParam.lmFunctionParam.defSuccessFunction(flag);
            }
        }
        else{
            if(typeof(lmParam.lmFunctionParam.defFailFunction) === 'function'){
                lmParam.lmFunctionParam.defFailFunction(flag);
            }
        }
    })

//--------------------------------------------sendDf end-----------------------
};
