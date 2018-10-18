function CLMKey() {
	var self = this;
	self.keys = new Array();
	var token = new mToken();
	self.index = 0;
	self.hasEngine = false; 
	//cerType == 1 签名证书  =0 加密证书
	self.cerType = 1;
	self.hwType = token.TYPE_UKEY;
	self.hashDigestType = DIGETST_HASH_TYPE.sha1;
	self.clmCer = null;
	//加载插件
	self.initObject = function(){
		self.hasEngine = token.SOF_LoadLibrary(token.GM3000);
		if(self.hasEngine != 0 ){
			self.hasEngine = token.SOF_LoadLibrary(token.K5);
		}
		if( self.hasEngine ==0 ){
			self.load();
		}else{
			console.log("加载控件失败,错误码:" + token.SOF_GetLastError());
			self.hasEngine = false;
		}
	};
	self.getDevice = function(){
		return token.SOF_EnumDevice();
	};
	//获取设备列表，判断设备类型
	self.load = function(){
		var deviceNames = token.SOF_EnumDevice();
		if(deviceNames != null){
			for( var i = 0 ; i < deviceNames.length; i ++){
				var key = {};
				key.deviceName = deviceNames[i];
				self.keys[i] = key;
			}
			self.hasEngine = true;
			// 获取UKey的类型，并按UKey类型来提示验证身份
			self.hwType = token.SOF_GetHardwareType();
			//if (self.hwType != token.TYPE_FPKEY) {// 不是指纹用户
				self.init();
			//}
		}else{
			self.hasEngine = false;
			console.log("key不存在,错误码:" + token.SOF_GetLastError());
		}
	};
	//通过设备获取证书容器(装载证书的)
	self.init = function (){
		//根据设备名称获取证书容器
		for( var i = 0 ; i < self.keys.length ; i ++){
			var ret = token.SOF_GetDeviceInstance(self.keys[i].deviceName, "");
			if (ret != 0) {
				console.log("实例化失败,获取证书失败,错误码:" + token.SOF_GetLastError());
				return "获取证书失败";
			}

            //初始化证书操作
            self.clmCer = new CLMCer(token);

			var cerList = token.SOF_GetUserList();
			if( cerList != null){
				self.keys[i].cerName = cerList[0][0];
				self.keys[i].containerName = cerList[0][1];
				var cerBase64 = token.SOF_ExportUserCert(self.keys[i].containerName, self.cerType);
				self.getCerInfo(cerBase64,i);
			}else{
				console.log("实例化失败,无法从KEY中获取证书,错误码:" + token.SOF_GetLastError());
				return "获取证书失败";
			}
		}
		
	};
	self.getCerInfo = function(cerBase64,i){
		var cerInfo = {};
		cerInfo.cerBase64Str = cerBase64;
		cerInfo.cerAlg = token.SOF_GetCertInfo(cerBase64, token.SGD_SIGNATURE_ALG);
		cerInfo.cerSerialId = token.SOF_GetCertInfo(cerBase64, token.SGD_CERT_SERIAL);
		var DigestMethod = cerInfo.cerAlg;
		if(DigestMethod.toLowerCase().indexOf("sm3") >-1){
			cerInfo.isSm2 = true;
		}
		self.getDigestMethodType(DigestMethod);
		self.keys[i].cerInfo = cerInfo;
	};
	self.keyHasCer = function(){
		if(isNullOrEmpty(self.keys[self.index].cerName )){
			console.log("无法从KEY中获取证书,请查看KEY中是否存在证书");
			return "无法从KEY中获取证书,请查看KEY中是否存在证书";
		}	
		return true;
	};
	//0-登录成功  1-密码错误 2--初始密码未修改
	self.login = function(psw){
		var ret =  token.SOF_Login(psw);
		if (ret == 0) {
			console.log("验证用户密码成功。");
			return 0;
		} else if(ret == 167772198){
			//初始密码未修改，不能进行签名
			return 2;
		}else{
			console.log("验证用户密码失败，错误码:"+token.SOF_GetLastError());
		}
		return 1;
	};
	self.FPLogin = function(){
		// Longmai的示例提示方式
		console.log("当指纹KEY上的指示灯开始闪烁时，请按压手指以验证指纹......"); 
		ret = token.SOF_VerifyFingerprint();
		if (ret == 0) {
			console.log("验证用户指纹成功。");
			return 0;
		} else if(ret == 167772198){
            //初始密码未修改，不能进行签名
            return 2;
        } else {
			console.log("验证用户指纹失败，错误码:"+token.SOF_GetLastError());
		}
		return 1;
	};
	self.sign = function(randomData,signType,attach){
		var result = "";
		if(isNullOrEmpty(self.keys[self.index].cerName )){
			console.log("无法从KEY中获取证书,错误码:" + token.SOF_GetLastError());
			return false;
		}
		if(!isNullOrEmpty(self.keys[self.index].cerInfo.cerBase64Str)){
			//国密需要
			var DigestMethod = self.keys[self.index].cerInfo.cerAlg;
			self.getDigestMethodType(DigestMethod);
			var ret = token.SOF_SetDigestMethod(self.hashDigestType);
			if(DigestMethod.toLowerCase().indexOf("sm3") >-1){
				ret = token.SOF_SetUserID("");
			}
			//signType=1,p1签名
			if(signType == 1){
				if(typeof(randomData) !== 'undefined'){
					result = token.SOF_SignData(self.keys[self.index].containerName, self.cerType,
					randomData, randomData.length);
				}
			}else{
				if(typeof(randomData) !== 'undefined'){
					//attach =0 带原文Attach
					result = token.SOF_SignDataToPKCS7(self.keys[self.index].containerName, self.cerType,
						randomData, attach);
				}
			}
		}
		if (isNullOrEmpty(result)) {
			return false;
		}
		return result;
	};
	self.getLastError = function(){
		return token.SOF_GetLastError();
	};
	//发起socket请求连接
	self.connectSocket = function(serverIp,serverPort){
		var result = token.SocketConnect(serverIp,serverPort);
		if(result != 0){
			console.log("发起请求失败");
			return false;
		}
		return true;
	};
	self.connectSocketSend = function(json){
		var result = token.SocketSendText(json);
		if(result==null || result==""){
			console.log("请求socket连接失败");
			return "";
		}
		return result;
	}
	self.disconnectSocket = function(){
		var ret=token.SocketDisConnect();
		if(ret!=0){
			console.log("断开失败");
			return false;
		}
		return true;
	}
	//请求hash值
	self.requestHash = function(json){
		var result = token.SocketSendText(json);
		if(result==null || result==""){
			console.log("请求hash失败");
			return "";
		}
		return result;
	};
	//请求签名
	self.requestSign = function(json){
	//	json = '{"type":"6","version":"1.1","verifedCode":"6a35831738e0f0f77902e38b6a69cb19","timestamp":1504512899671,"data":"{\"signed\":\"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\",\"fileId\":\"201709041614056887535\"}","length":4794}';
		var result = token.SocketSendText(json);
		if(result==null || result==""){
			console.log("请求签名失败");
			return "";
		}
		return result;
	};
    self.checkHasEngineCtl = function(){
        if( self.hasEngine ){
            return true;
        }else{
            console.log("上海CAKEY加载失败，请查看usbkey是否插入以及驱动是否正常安装");
            return false;
        }
    };
	//写入文件
	self.writeFile = function(fileName,fileData){
		var array = token.SOF_EnumFiles();
		if(array == null || array.length <=0){
			var result = token.SOF_CreateFile(fileName,1024*15,SECURE_ACCOUNT.ANYONE,SECURE_ACCOUNT.ANYONE);
			if(result == 0){
				result = token.SOF_WriteFile(fileName,0,fileData);
				if(result !=0){
					console.log("写入文件失败");
					return false;
				}
				return true;
			}
		}else{
			result = token.SOF_WriteFile(fileName,0,fileData);
			if(result !=0){
				console.log("写入文件失败");
				return false;
			}
			return true;
		}
		
	};
	self.readFile = function(fileName){
		var result = token.SOF_ReadFile(fileName,0,1024*15);
		if(token.SOF_GetLastError() != 0){
			console.log("请求文件失败");
			return false;
		}
		if(result.indexOf("///")>-1){
			result = result.replace(/\/\/\//g, ""); 
		}
		return result;
		
	};
	self.deleteFile = function(fileName){
		var result = token.SOF_DeleteFile(fileName);
		if(self.getLastError() != ""){
			console.log("删除文件失败");
			return false;
		}
		return true;
	};
	//获取hash算法类型
	self.getDigestMethodType = function(DigestMethod){
		if(DigestMethod.toLowerCase().indexOf("sm3") >-1){
			self.hashDigestType= DIGETST_HASH_TYPE.sm3;
		}else if(DigestMethod.toLowerCase().indexOf("sha1") >-1){
			self.hashDigestType= DIGETST_HASH_TYPE.sha1;
		}else if(DigestMethod.toLowerCase().indexOf("sha256") >-1){
			self.hashDigestType= DIGETST_HASH_TYPE.sha256;
		}else if(DigestMethod.toLowerCase().indexOf("md5") >-1){
			self.hashDigestType= DIGETST_HASH_TYPE.md5;
		}else if(DigestMethod.toLowerCase().indexOf("sha384") >-1){
			self.hashDigestType= DIGETST_HASH_TYPE.sha384;
		}else if(DigestMethod.toLowerCase().indexOf("sha512") >-1){
			self.hashDigestType= DIGETST_HASH_TYPE.sha512;
		}
	},
	//修改密码
	self.changeUserPin =function (userPin,newUserPin){
        var ret = token.SOF_ChangePassWd(userPin, newUserPin);
        if(ret != 0) {
            console.log("密码修改失败,错误码:" + token.SOF_GetLastError());
            return false;
        }else {
            console.log("密码修改成功");
            return true;
        }
    },
    //重置密码
	self.resetUserPin = function(adminPin,newPin){
	    var remoteUnlockPinBase64 = "";
		var genRemoteUnlockPin = function(){
            var request = token.SOF_GenRemoteUnblockRequest();
            if(request == null || request == ""){
                console.log("生成解锁请求失败");
                return false;
            }
            remoteUnlockPinBase64 = request;
            return true;
        }
        var  genUnlockPinResponse = function(){
            var request = token.SOF_GenResetpwdResponse(remoteUnlockPinBase64, adminPin, newPin);
            if(request == null || request == ""){
                console.log("生成解锁请求失败");
                return false;
            }
            remoteUnlockPinBase64 = request;
            return true;
        }
        var remoteUnlockPin = function(){
            var request = token.SOF_RemoteUnblockPIN(remoteUnlockPinBase64);
            if(request != 0) {
                console.log("解锁失败");
                return false;
            }else{
                console.log("解锁成功");
            }
            return true;
        }
        if(genRemoteUnlockPin()){
            if(genUnlockPinResponse()){
                return remoteUnlockPin();
            }
        }
        return false;
	}

}
//文件访问权限
var SECURE_ACCOUNT={
	NEVER:0x00000000,
	ADM:  0x00000001,
	USER: 0x00000010,
	ANYONE:0x000000FF
};

//hash算法类型
var DIGETST_HASH_TYPE={
	sm3:1,
	sha1:2,
	sha256:4,
	md5:129,
	sha384:130,
	sha512:131
}