var KeyLocalLogin = function(options){
	var self = this;
	self.loginO = options.loginO;
	self.cLMkeyLocalLogin = options.cLMkeyLocalLogin;
	self.lastDevice = "";
	self.thisDevice = "";
	self.load = function(){
		self.cLMkeyLocalLogin.cLMKey.initObject();
		self.loginO.hwType = self.cLMkeyLocalLogin.cLMKey.hwType;
	};
	self.checkHasEngineCtl = function(){
		if( !self.cLMkeyLocalLogin.cLMKey.hasEngine ){
			showLmMessage(LmMessage.KEY_LOAD);
			return;
		}
	};
	//key登录
	self.keyLogin = function(){
		if(self.cLMkeyLocalLogin.cLMKey.hasEngine){
			return self.cLMkeyLocalLogin.login(self.loginO.sendParam);
		}else{
			showLmMessage(LmMessage.KEY_LOAD);
			return false;
		}
		
	};
	//指纹登录
	self.FPLogin = function(){
	    var result = self.cLMkeyLocalLogin.FPLogin()
        if(result){
            stopCount();
        }
		return result;
		
	};
	self.getDevice = function(){
		return self.cLMkeyLocalLogin.getDevice();
	};
	self.keyHasCer = function(){
		return self.cLMkeyLocalLogin.keyHasCer();
	}
	
};
var t;
function timedCount(){ 
	t=setTimeout("timedCount()",10000); 
	$.keyLocalLogin.lastDevice = $.keyLocalLogin.thisDevice;
	$.keyLocalLogin.thisDevice = $.keyLocalLogin.getDevice();
	if($.keyLocalLogin.lastDevice == null || $.keyLocalLogin.thisDevice  == null){
		stopCount();
	}
	if( $.keyLocalLogin.lastDevice != null && $.keyLocalLogin.lastDevice[0] != $.keyLocalLogin.thisDevice[0]
			&& $.keyLocalLogin.thisDevice != null){
		$.keyLocalLogin.load();
		$.keyLocalLogin.checkHasEngineCtl();
	}
} 
function stopCount(){ 
	// clearTimeout(t);
} 

//创建登录对象
$.keyLocalLogin = new KeyLocalLogin({
	loginO:loginObject,
	cLMkeyLocalLogin: new CLMkeyLocalLogin()
});

$(function(){
	$.keyLocalLogin.load();
	// $.keyLocalLogin.checkHasEngineCtl();
	// $.keyLocalLogin.thisDevice = $.keyLocalLogin.getDevice();
	// timedCount();
});

 
