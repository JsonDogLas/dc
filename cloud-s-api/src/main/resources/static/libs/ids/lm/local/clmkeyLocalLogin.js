var LMConstant ={
	VER_SUSS:0,
	VER_START:1,
	VER_FAIL:2
}
function CLMkeyLocalLogin(){
	var self = this;
	self.cLMKey = new CLMKey();
	self.login = function(sendParam){
		var  returnCode = self.cLMKey.login(sendParam.password);
		//登录成功
		if(returnCode == 0){
			//获取签名值
			var randomData= _Base64encode(sendParam.random)
			var returnTip = self.cLMKey.sign(randomData,1,0);
			if(returnTip == false){
				showLmMessage(LmMessage.SIGN_RANDOM_WRONG);
				return false;
			}else{
				$.keyLocalLogin.loginO.sendParam.strSign=returnTip;
				$.keyLocalLogin.loginO.sendParam.strCer=self.cLMKey.keys[0].cerInfo.cerBase64Str;
				$.keyLocalLogin.loginO.sendParam.cerUniqueID = self.cLMKey.keys[0].cerInfo.cerSerialId;
				if(self.cLMKey.keys[0].cerInfo.isSm2 ){
					//服务器模式验证
					$.keyLocalLogin.loginO.sendParam.verType = 2;
					$.keyLocalLogin.loginO.sendParam.keyType = 2;
				}else{
					$.keyLocalLogin.loginO.sendParam.verType = 1;
					$.keyLocalLogin.loginO.sendParam.keyType = 1;
				}
				return true;
			}
		}else{
			if(returnCode == 1){
				showLmMessage(LmMessage.KEY_LOGIN_FAIL);
			}else if(returnCode == 2){
				showLmMessage(LmMessage.KEY_LOGIN_PSW_INIT_NOT_CHANGE);
			}
		}
		return false;
	};
	self.FPLogin = function(){
		showLmMessage(LmMessage.KEY_FPLOGIN_TIP);
		var  returnCode = self.cLMKey.FPLogin()
		if(returnCode == 1){
			showLmMessage(LmMessage.KEY_FPLOGIN_FAIL);
			return false;
		}
		
		//获取签名值
		var randomData= _Base64encode($.keyLocalLogin.loginO.sendParam.random)
		var returnTip = self.cLMKey.sign(randomData,1,0);
		if(returnTip == false){
			showLmMessage(LmMessage.SIGN_RANDOM_WRONG);
			return false;
		}else{
			$.keyLocalLogin.loginO.sendParam.strSign=returnTip;
			$.keyLocalLogin.loginO.sendParam.strCer=self.cLMKey.keys[0].cerInfo.cerBase64Str;
			if(self.cLMKey.cerInfo.isSm2){
				//服务器模式验证
				$.keyLocalLogin.loginO.sendParam.verType = 2;
				$.keyLocalLogin.loginO.sendParam.keyType = 2;
			}else{
				$.keyLocalLogin.loginO.sendParam.verType = 1;
				$.keyLocalLogin.loginO.sendParam.keyType = 1;
			}
			return true;
		}
	};
	self.getDevice = function(){
		return self.cLMKey.getDevice();
	};
	self.keyHasCer = function(){
		return self.cLMKey.keyHasCer();
	};
	
	//纯验证
	self.checkHasEngine = function(){
		if( !self.cLMKey.hasEngine ){
			self.verTip = LMConstant.VER_FAIL;
		}
		//自动指纹
		if(self.loginO.hwType == 2){
			self.verTip = LMConstant.VER_START;
		}
	};
	//纯验证指纹
	self.FPVeri = function(){
		var  verTip = self.cLMKey.FPLogin();
		if(verTip == true){
			self.verTip = LMConstant.VER_SUSS;
		}else{
			self.verTip = LMConstant.VER_FAIL;
		}
	};
	//前台插件js运行会堵塞前端ui，采用defered，延迟插件的运行时间，使弹出层先弹出
	self.openVer = function(title,timeC,lmParam){
		self.checkHasEngine();
		if(self.loginO.hwType != 2){
            showLmMessage("非指纹KEY,请检查KEY");
			return ;
		}
		if(title ==""){
			title = "当指纹KEY上的指示灯开始闪烁时，请按压手指以验证指纹......";
		}
        showLmMessage(title);
		self.verTip = LMConstant.VER_START;
		var dtd = $.Deferred();
		var wait = function(dtd){
			//做一些异步操作
	        setTimeout(function(){
	        	self.FPVeri();
	 	         if(self.verTip == LMConstant.VER_FAIL){
                     showLmMessage("指纹验证失败");
	 	        	dtd.reject();
	 	           	return;
	 	  		 }else{
                     showLmMessage("指纹验证成功");
	 	  			dtd.resolve(); // 改变Deferred对象的执行状态
	 	  		  }
	        }, timeC);
		};
		dtd.promise(wait);
		wait.done(function(){
			if(typeof(lmParam.defSuccessFunction) === 'function'){
				lmParam.defSuccessFunction();
			}
		}).fail(function(){
			if(typeof(lmParam.defFailFunction) === 'function'){
				lmParam.defFailFunction();
			}
		})
		wait(dtd);
		
	};

};

 
