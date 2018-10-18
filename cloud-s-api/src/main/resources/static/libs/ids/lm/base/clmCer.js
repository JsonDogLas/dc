//证书操作

function CLMCer(token){
    var self = this;
    /**
     * 请求p10，返回p10请求
     * options={
     *  container://容器,
     *  dn:DN:CN=guow0a2,O=SGCC,OU=guowang,C=CN//CN=名称，O=组织，C=中国
     *  asymAlg://算法
     *  keySpec://密钥类型
     *  keyLength://长度
     * }
     */
    self.requestP10  = function(options){
        var container = options.container;
        var dn = options.dn;
        //算法
        var asymAlg = options.asymAlg;
        //签名
        var keySpec = options.keySpec== null?1:options.keySpec + "";
        //秘钥长度
        var keyLength = options.keyLength + "";
        //设备名称
        var deviceName = options.deviceName;

        if(container.length == 0 || dn.length == 0 || asymAlg.length == 0 || keySpec.length == 0 || keyLength.length == 0
            || deviceName.length == 0 ){
            console.log("请确保各个参数是否为空！")
            return null;
        }
        var p10Req = token.SOF_GenerateP10Request(deviceName,container, dn, asymAlg, keySpec, keyLength);
        if(p10Req=="" || p10Req==null){
            console.log("生成失败！错误码："+token.SOF_GetLastError())
            return null;
        }
        return p10Req;
    }
    //导入签名证书
    self.importSignCert = function (signCertData,container){
        if(signCertData.length <256)
        {
            console.log("无效的签名证书数据。");
            return false;
        }
        var retVal = 0;
        //1--签名类型 0- 加密类型
        retVal = token.SOF_ImportCert(container, signCertData, 1); //0:OK
        if(retVal!=0){
            console.log("导入失败！错误码："+token.SOF_GetLastError());
            return false;
        }
        return true;
    },
    //P10请求证书
    self.requestFromP10ForSignCert = function(json){
        var result = token.SocketSendText(json);
        if(result==null || result==""){
            console.log("requestFromP10ForSignCert失败");
            return "";
        }
        return result;
    },
    //导出公钥
    self.exportPubKey = function(options){
        var container = options.container;
        //签名证书
        var cerType = 1;
        var strPubKey  = token.SOF_ExportPubKey(container, cerType);
        if(strPubKey != null && strPubKey != "") {
            console.log("公钥:"+strPubKey);
           return strPubKey;
        }
        else {
            console.log("获取公钥失败,错误码:" + token.SOF_GetLastError());
        }
    }

}