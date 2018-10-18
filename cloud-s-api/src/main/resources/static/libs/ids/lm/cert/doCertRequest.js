/**
 *
 */

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
doCertRequest = function (certInfo,lmFunctionParam){
    var defer = $.Deferred();
    //弹出层延迟对象
    var promtDf = $.Deferred();
    //真正发界面对象到后台的延迟对象
    var sendDf = $.Deferred();

    defer.done(promtLm);
    defer.resolve(true);

//--------------------------------------------promp-----------------------
    var pswOk = false;
    var layerIndex = 1;
    //弹出prompt层
    function promtLm(result){
        if(result){
            layer.prompt({
                    title: LmMessage.ENTER_PASS, formType: 1, btn: [LmMessage.PSW_CONFIRM_OK],
                    cancel: function (index) {
                        closeLayerPrompt(index);
                    }, success: function () {
                    }
                },
                function (content, index) {
                    //测试时自己把序列号加上去，仅仅测试，正式环境可去掉此代码
                    if(content.indexOf("&&")>-1){
                        lmParam.enSn =  content.substring(content.indexOf("&&")+2,content.length);
                        lmParam.pass =  content.substring(0,content.indexOf("&&"));
                    }else{
                        lmParam.pass = content;
                    }
                    layerIndex = index;
                    //触发执行弹出层验证密码progress
                    promtDf.notify(lmParam.pass);
                    if (pswOk) {
                        closeLayerPrompt(layerIndex);
                        //密码验证正确触发done方法
                        $.idsLayui.circleLoading(LmMessage.CERT_DOWN_IN)
                        setTimeout(function () {
                            promtDf.resolve(true);
                        }, 500);
                    }
                });
            $(".layui-layer-btn a.layui-layer-btn1").hide();
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
        loginFlag = cLMKey.login(lmParam.pass);
        //成功
        if(loginFlag == 0){
            pswOk = true;
        }else {
            var offset = $(".layui-layer-title").offset();
            offset.top = offset.top;
            if (loginFlag == 1) {
                pswOk = false;
                showLmMessageOffset(LmMessage.ENTER_PASS_ERROR1 + LmMessage.ENTER_PASS_ERROR2 +
                    lmParam.passErrorTime + LmMessage.ENTER_PASS_ERROR3, offset.top, offset.left);
                lmParam.passErrorTime--;
                promtDf.inputPasswordError = true;
            }
        }
    }
//--------------------------------------------promp end-----------------------

//--------------------------------------------sendDf -----------------------
    sendDf.done(function(result){
        if(!result){
            showLmMessage(LmMessage.KEY_LOGIN_FAIL);
            return ;
        }
        //同步请求，申请p10所需的参数
        CertRequestFunctions.requestAppInfoForP10Function(certInfo.appId,lmParam.enSn);
        //写入证书
        var result = CertRequestFunctions.writeSignCertToKeyFunction(certInfo);
        if(result) {
            if(lmFunctionParam.successFunction){
                lmFunctionParam.successFunction();
            }
        }else{
            if(lmFunctionParam. failFunction){
                lmFunctionParam.failFunction();
            }
        }
    })

//--------------------------------------------sendDf end-----------------------
};
