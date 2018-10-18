/**
 * 
 */
var isLogin = "1";
var errFlag =0;

var loginObject = {
	sessionId : "",
	sendParam:{
		random:"",
		s : "",
		t : "",
		password:"",
		strCer:"",
		strSign:"",
		signType:1,
		keyType:1,
		cerUniqueID:""
	}
}

function IsIE(){
	//判断是否为IE内核浏览器
	var u = window.navigator.userAgent.toLocaleLowerCase();
	var msie = /(msie) ([\d.]+)/;
	var chrome = /(chrome)\/([\d.]+)/;
	var firefox = /(firefox)\/([\d.]+)/;
	var safari = /(safari)\/([\d.]+)/;
	var opera = /(opera)\/([\d.]+)/;
	var ie11 = /(trident)\/([\d.]+)/;
	b = u.match(msie)||u.match(chrome)||u.match(firefox)||u.match(safari)||u.match(opera)||u.match(ie11);
	if(b[1] == "msie" || b[1] == 'trident'){
		return true;
	}else{
		return false;
	}
}

function isNullOrEmpty(s) {
    if (s == null || s == undefined) {
        return true;
    }
    s = s.toString();
    s = trimStr(s);
    if (s == "" || s == null || s == undefined) {
        return true;
    }
    return false;
}

//去掉首尾空格
function trimStr(str){
	return str.replace(/(^\s*)|(\s*$)/g,"");
}

(function (){  
	//ie8不识别console对象，创建空console对象，避免JS报错  
	if(!window.console)  
	    window.console = {};  
	var console = window.console;  

	var funcs = ['assert', 'clear', 'count', 'debug', 'dir', 'dirxml',  
	             'error', 'exception', 'group', 'groupCollapsed', 'groupEnd',  
	             'info', 'log', 'markTimeline', 'profile', 'profileEnd',  
	             'table', 'time', 'timeEnd', 'timeStamp', 'trace', 'warn'];  
	for(var i=0,l=funcs.length;i<l;i++) {  
	    var func = funcs[i];  
	    if(!console[func])  
	        console[func] = function(){};  
	}  
	if(!console.memory)  
	    console.memory = {};  

	})();