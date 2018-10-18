var cLMKey = new CLMKey();
cLMKey.initObject();
var lmParam={
    connectSuccess:false,
    isSm2 : 1,
    socketIp:"",
    socketPort :"",
    clientVersion:"",
    pass : '',
    verFlag : true, //判断是否加载Key成功
    passErrorTime : 10,
    DigestMethod:'',
    //签名失败时，判断是否有值，且值为函数，则调用  @param 1代表 关闭密码输入框
    signFlagFunction:null,
    keyId:"",
    //1; 普通UKEY  2; 指纹UKEY
    hwType:1,
    //cert的base64
    cerBase64Str:"",
    //设备名
    deviceName:"",
    //申请p10所需的参数
    lmP10ParamOptions:null
}