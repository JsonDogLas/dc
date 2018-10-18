/**
 * 
 */

var nAgt = window.navigator.userAgent.toLowerCase();
//alert(nAgt);
var JBrowser = function(){
	var self = this;
	self.name = navigator.appName;
	self.fullVersion = '' + parseFloat(navigator.appVersion);
	self.majorVersion = parseInt(navigator.appVersion, 10);
	self.versions = function(){
		var u = nAgt.toLowerCase();
		return {
			/**
			 * IE手机版和PC版皆为Trident内核的
				Opera手机版和PC版皆为Webkit内核（少部分版本仍然是Presto内核）
				Firefox手机版和PC版皆为Gecko内核
				Chrome手机版和PC版皆为Webkit内核
				Safari手机版和PC版皆为Webkit内核
				QQ手机浏览器内核为基于Webkit修改的X5内核
				UC手机浏览器内核为基于Webkit修改的U3内核
			 */
			trident: u.indexOf('trident') > -1, //IE内核
			presto: u.indexOf('presto') > -1, //opera内核
			webKit: u.indexOf('applewebkit') > -1, //苹果、谷歌内核
			gecko: u.indexOf('gecko') > -1 && u.indexOf('khtml') == -1, //火狐内核
			webApp: u.indexOf('safari') == -1 ,//是否为web应用程序，没有头部与底部 
			weixin: u.indexOf('micromessenger') == -1, //是否为微信浏览器
			ipad : u.match(/ipad/i) == "ipad",
		    iphoneOs : u.match(/iphone os/i) == "iphone os",
		    midp : u.match(/midp/i) == "midp",
		    uc7 : u.match(/rv:1.2.3.4/i) == "rv:1.2.3.4",
		    uc : u.match(/ucweb/i) == "ucweb",
		    android : u.match(/android/i) == "android",
		    ce : u.match(/windows ce/i) == "windows ce",
		    wm : u.match(/windows mobile/i) == "windows mobile"
		    
		};
	}();
	self.opera =function (){
		var nameOffset, verOffset, ix;
		if ((verOffset = nAgt.indexOf("opera")) != -1) {
			self.name = "opera";
			self.fullVersion = nAgt.substring(verOffset + 6);
			if ((verOffset = nAgt.indexOf("version")) != -1)
				self.fullVersion = nAgt.substring(verOffset + 8);
		}
	};
	self.ie = function(){
		var nameOffset, verOffset, ix;
		if ((verOffset = nAgt.indexOf("msie")) != -1) {
			self.name = "microsoft internet explorer";
			self.fullVersion = nAgt.substring(verOffset + 5);
			if ((ix = self.fullVersion.indexOf(";")) != -1)
				self.fullVersion = self.fullVersion.substring(0, ix);
			if ((ix = self.fullVersion.indexOf(" ")) != -1)
				self.fullVersion = self.fullVersion.substring(0, ix);
			self.majorVersion = parseInt('' + self.fullVersion, 10);
			if (isNaN(self.majorVersion)) {
				self.fullVersion = '' + parseFloat(navigator.appVersion);
				self.majorVersion = parseInt(navigator.appVersion, 10);
			}
			self.ieversion = self.majorVersion;
			return true;
		}
		return false;
	};
	self.chrome = function(){
		var nameOffset, verOffset, ix;
		if ((verOffset = nAgt.indexOf("chrome")) != -1) {
			self.name = "chrome";
			self.fullVersion = nAgt.substring(verOffset + 7);
			return true;
		}
		return false;
	};
	self.safira = function(){
		var nameOffset, verOffset, ix;
		if ((verOffset = nAgt.indexOf("safari")) != -1) {
			self.name = "safari";
			self.fullVersion = nAgt.substring(verOffset + 7);
			if ((verOffset = nAgt.indexOf("version")) != -1)
				self.fullVersion = nAgt.substring(verOffset + 8);
			return true;
		}
		return false;
	};
	self.firefox = function(){
		var nameOffset, verOffset, ix;
		if ((verOffset = nAgt.indexOf("firefox")) != -1) {
			self.name = "firefox";
			self.fullVersion = nAgt.substring(verOffset + 8);
			return true;
		}
		return false;
	};
	self.otherbr = function(){
		var nameOffset, verOffset, ix;
		if ((nameOffset = nAgt.lastIndexOf(' ') + 1) < (verOffset = nAgt
				.lastIndexOf('/'))) {
			self.name = nAgt.substring(nameOffset, verOffset);
			self.fullVersion = nAgt.substring(verOffset + 1);
			if (self.name.toLowerCase() == self.name.toUpperCase()) {
				self.name = navigator.appName;
			}
		}
		return self.name;
	};
	self.trident = function(){
		return self.versions.trident;
	};
	self.presto = function(){
		return self.versions.presto;
	};
	self.webKit = function(){
		return self.versions.webKit;
	};
	self.gecko = function(){
		return self.versions.gecko;
	};
	self.android = function(){
		return self.versions.android;
	};
	self.iphoneOs = function(){
		return self.versions.iphoneOs;
	};
	self.ipad = function(){
		return self.versions.ipad;
	};
	self.webApp = function(){
		return self.versions.webApp;
	};
	self.weixin = function(){
		return !self.versions.weixin;
	};
	self.mobile = function(){
		return self.versions.ipad || self.versions.iphoneOs || self.versions.midp || self.versions.uc7 
			|| self.versions.uc || self.versions.android || self.versions.ce || self.versions.wm;
	};
	self.edge = function(){
		return nAgt.indexOf("edge") > -1 ? true:false;
	};
	 //判断是否是IE浏览器，包括Edge浏览器  
	self.IEVersion=function(){  
      var userAgent = window.navigator.userAgent; //取得浏览器的userAgent字符串  
      var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器  
      var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器  
      var isEdge = self.edge();
      var isIE11 = (userAgent.toLowerCase().indexOf("trident") > -1 && userAgent.indexOf("rv") > -1);
      if(isIE) {  
           var reIE = new RegExp("MSIE (\\d+\\.\\d+);");  
           reIE.test(userAgent);  
           var fIEVersion = parseFloat(RegExp["$1"]);  
           if(fIEVersion == 7)  
           { return "IE7";}  
           else if(fIEVersion == 8)  
           { return "IE8";}  
           else if(fIEVersion == 9)  
           { return "IE9";}  
           else if(fIEVersion == 10)  
           { return "IE10";}  
           else  
           { return "0"}//IE版本过低  
      }else if(isEdge){  
	    return "Edge";  
	  }else if(isIE11){ 
		return "IE11";
	  }else{  
          return "-1";//非IE  
      }  
   }
};
(function($){
	$.jbrowser = new JBrowser();
})(jQuery);

