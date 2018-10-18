$(function(){
	
	var loadFont={};
	/**
	 * 记载js文件
	 * @param srcs araay 数组
	 */
	loadFont.loadJS=function(src,callback){
		/*$.ajax({
		    dataType:'script', //或者  dataType:'script' 都无效。。
		    url:src,
		    success:callback
		})*/
		$.getScript(src, callback);
		
	}
	/**
	 * 加载字体
	 * @param families 字体集合
	 * @param cssUrls 字体文件路径
	 */
	loadFont.loadFont=function(families,cssUrls){
		WebFont.load({
            custom: {
                families: families,
                urls : cssUrls  //字体声明处，页面不需要引入该样式
            }
        });
	}
	/**
	 * 获取当前文件的绝对路径
	 * @path
	 */
	loadFont.getPath=function (path){
        /*var pathName = document.location.pathname;
        var index = pathName.substr(1).indexOf("/");
       var result = pathName.substr(0,index+1);
       return result+path;*/
        return path;
    }
	loadFont.init=function(options){
		var call=function(){
			loadFont.loadJS(loadFont.getPath("/pt/modules/yq/api/signfile/createstamp/js/fontstyle_"+options.languageUnderline+".js"),function(content){
				eval(content);
				var families=[];
	            var url=loadFont.getPath("/pt/modules/yq/api/signfile/createstamp/css/fontstyle_"+options.languageUnderline+".css");
	            if(fontList != null){
	                for(var index in fontList){
	                    var item=fontList[index];
	                    families.push(item.number);
	                }
	            }
	            loadFont.loadFont(families,[url]);
			});
		}
		if(typeof options.time == "number"){
			setTimeout(function(){
				call();
			},options.time);
		}else{
			call();
		}
	}
	loadFont.init({
        languageUnderline:"zh_CN"
	});
});