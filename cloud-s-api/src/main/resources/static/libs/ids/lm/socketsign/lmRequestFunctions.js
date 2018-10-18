var RequestUrls={
    getSocketIpAndPortUrl:"/signPage/signSocket/getSocketIpAndPort",
    checkUserKeyHasPutInUrl: "/signPage/signSocket/checkUserKeyHasPutIn?keyId=",
    saveUserKeyInfoUrl: "/signPage/signSocket/saveUserKeyInfo?keyId=",
    clearUserKeyInfoUrl: "/signPage/signSocket/clearUserKeyInfo",
    saveToSignList: "/signPage/signSocket/saveToSignList"
}

//业务相关请求方法
var RequestFunctions = {
    /**
     * 获取Socket服务器Ip和端口，请求socket连接
     */
    getSocketIpAndPort : function () {
        $.ajax({
            type: "get",
            dataType: "json", // 发送的参数
            url: RequestUrls.getSocketIpAndPortUrl , //  连接地址
            async:false,
            success: function (r) {
                if (r.result) {
                    if (r.attributes) {
                        for(var k = 0 ; k< r.attributes.length; k ++){
                            if(r.attributes[k].SERVER_IP){
                                lmParam.socketIp = r.attributes[k].SERVER_IP;
                            }
                            if(r.attributes[k].SERVER_PORT){
                                lmParam.socketPort = r.attributes[k].SERVER_PORT;
                            }
                        }
                        requestStart();
                    }else{
                        console.log(LmMessage.SOCKET_GETINFO_FAIL);
                        showLmMessage(LmMessage.SOCKET_SERVER_INFO);
                    }
                }
            }
        });
    },
    //判断用户是否输入过密码
    checkUserKeyHasPutIn :function (){
        var keyId= cLMKey.keys[0].cerInfo.cerSerialId;
        var flag = false;
        $.ajax({
            type: "get",
            dataType: "json", // 发送的参数
            url: RequestUrls.checkUserKeyHasPutInUrl+keyId, //  连接地址
            async:false,
            success: function (r) {
                if (!r.result) {
                    flag =  false;
                }else{
                    flag =  true;
                }
            }
        });
        return flag;
    },

    /**
     * 保存用户Key信息到缓存
     */
    saveUserKeyInfo:function (){
        var keyId= cLMKey.keys[0].cerInfo.cerSerialId;
        $.ajax({
            type: "get",
            dataType: "json", // 发送的参数
            url: RequestUrls.saveUserKeyInfoUrl+keyId , //  连接地址
            success: function (r) {
            }
        });
    },

    /**
     * 清除用户Key信息
     */
    clearUserKeyInfo :function (){
        $.ajax({
            type: "get",
            dataType: "json", // 发送的参数
            url: RequestUrls.clearUserKeyInfoUrl , //  连接地址
            success: function (r) {
            }
        });
    }

}