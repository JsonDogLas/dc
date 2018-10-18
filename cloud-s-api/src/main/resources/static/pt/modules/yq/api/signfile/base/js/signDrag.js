/**
 * 系统错误打印
 */
$.eventHint=function(options){
    //Window.print(options);
	console.log(options);
}
/**
 *动态加载图片，获取高度，宽度 
 */
$.loadImg=function(imgPath,callback){
	 var oImages=new Image();
	 oImages.src=imgPath;
	 oImages.onload=function(){
        callback($(oImages),oImages.height,oImages.width);
	 } 
}
/**
 * 根据时间获取随机数
 */
$.random=function(){
    var random=Math.random()+"";
    return new Date().getTime()+random.substring(3,random.length-1);
}
/**
 * str 替换的字符串
 * replace 替换的对象
 * temp 替换的内容
 */
$.replaceAll=function(str,replace,temp){
	while(str.indexOf(replace)!=-1){
		str=str.replace(replace,temp);
	}
	return str;
}
/**
 * 按着不动
 */
$.oftenMousedown=function(options,backcall){
    var options=$.extend({time:100},options);
    $(options.parentIndex).on("mousedown",options.dynamicIndex,function(event){
        var startTime=new Date().getTime();
        var thisele=$(this);
        var flag = false;
        var stop = setTimeout(function() {//down 1s，才运行。
            flag = true;
            backcall(thisele,event);
        }, options.time);
        
        $(options.parentIndex).on("mouseup",options.dynamicIndex,function(){
        	if (!flag) {
                clearTimeout(stop);
            }
        });
    });
}
/*
 * Map容器
 * options json参数
 * 		cleanEmpty : 清空指标 
 * 		max:最大容量
 */
$.ContainerMap=function(options){
	//验证key
	var validKey=function(key){
		if(key==null){
			$.eventHint("key不能为空");
			return false;
		}
		if(typeof(key)!="string"){
			$.eventHint("key必须为字符串");
			return false;
		}
		return true;
	}
	//验证value
	var validValue=function(value){
		if(value==null){
			$.eventHint("value不能为空！如果value为空，程序会清除掉key。");
			return false;
		}
		return true;
	}
	
	var systemMaxParam=function(max){
		if(max==null || max==0){
			options.max=1000;
		}else{
			options.max=max;
		}
	}
	var systemCleanEmptyParam=function(cleanEmpty){
		if(cleanEmpty==null || cleanEmpty==0){
			options.cleanEmpty=20;
		}else{
			options.cleanEmpty=cleanEmpty;
		}
	}
	if(typeof(options) == "undefined"){
		options={};
		systemMaxParam(options.max);
		systemCleanEmptyParam(options.cleanEmpty);
	}else{
		if(typeof(options) != "object"){
			$.eventHint("options 参数必须为json格式"+typeof(options));
		}
	}
	
	
	//内部容器管理
	var container=new Object();
	var valueArray=[];//存储value的数组
	var keyObject={};//存储key的对象
	
	
	
	/**
	 * put数据
	 */
	container.put=function(key,value){
		if(!validKey(key)){
			return false;
		}
		
		if(!validValue(key)){
			return false;
		}
		
		if(keyObject.length==options.max){
			return false;
		}else{
			var index=valueArray.push(value)-1;
			keyObject[key]=index;
			return true;
		}
		
	}
	/**
	 * 根据key取得数据
	 * return value
	 */
	container.get=function(key){
		var index=keyObject[key];
        return valueArray[index];
	}
	
	/**
	 * 清空key对应的value
	 */
	container.remove=function(key){
		var index=keyObject[key];
		valueArray[index]=null;
        delete keyObject[key];
        //清空处理 当数组长度大于10的时候 自动从整数组
        if(valueArray.length>options.cleanEmpty){
            var bool=false;
            for(var item in valueArray){//发现数组有 null时 ，数据从新分配
                if(valueArray[item]==null){bool=true;break;}
            }
            if(bool){
                var newValueArray=[];
                for(var item in keyObject){
                	//取出原有的值
                    var index=keyObject[item];
                    var value=valueArray[index];
                    //将新的值放入新的数组中  存储标号
                    keyObject[item]=newValueArray.push(value)-1;
                }
                valueArray=null;
                valueArray=newValueArray;
            }
        }
	}
	/**
	 * 判断是否为空
	 * return boolean
	 */
	container.isEmpty=function(){
		if(keyObject.length==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 返回所有的key
	 */
	container.keySet=function(){
		var keys={};
		for(var item in keyObject){
			keys[item]=keyObject[item];
		}
		return keys;
	}
	/**
	 * 返回数据长度
	 */
	container.size=function(){
		var size=0;
        for(var item in keyObject){
            if(typeof keyObject[item] == "number"){
                size++;
            }
        }
        return size;
	}
	/**
	 * 清空数据
	 */
	container.clear=function(){
		valueArray=[];
		keyObject={};
	}
	
	/**
	 * 取得所有数据
	 */
	container.getAll=function(){
		var values=[];
		for(var item in valueArray){
			values[item]=valueArray[item];
		}
		return values;
	}
	
	return container;
}

$.isIEBrowserVersion=function (){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
    var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
    var isSafari = userAgent.indexOf("Safari") > -1; //判断是否Safari浏览器
    if (isIE) {
        var IE5 = IE55 = IE6 = IE7 = IE8 = false;
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        return fIEVersion;
        
        }
    return "qt";
}

$.getBrowserVersion=function(){
	 var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	 var isOpera = userAgent.indexOf("Opera") > -1;
	 var browser=null;
	 var version=null;
	 //判断是否Opera浏览器
     if (isOpera) {
    	 browser= "Opera"
     }else if (userAgent.indexOf("Firefox") > -1) {//判断是否Firefox浏览器
    	 browser= "FF";
     }else if (userAgent.indexOf("Chrome") > -1){//判断是否Chrome浏览器
    	 browser= "Chrome";
     }else if (userAgent.indexOf("Safari") > -1) {//判断是否Firefox浏览器
    	 browser= "Safari";
     }else if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {//判断是否IE浏览器
    	 browser= "IE";
    	 var IE5 = IE55 = IE6 = IE7 = IE8 = false;
         var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
         reIE.test(userAgent);
         version = parseFloat(RegExp["$1"]);
     }else{
    	 browser= "IE";
         version= 11;
     } 
     return{
    	 browser:browser,
    	 version:version
     }
}

/**
 * Sign 签章模型
 */
function SignModel(){
	/**
     * 签章类
     **/
    var Sign=function(page,signType,zoomRate,pdf_x,pdf_y,signEleId){
        this.page=page;//页数
        this.signType=signType;//签章类型
        this.zoomRate=zoomRate;//签章缩放倍率
        this.pdf_x=pdf_x;//PDF上的 X 坐标位置
        this.pdf_y=pdf_y;//PDF上的 Y 坐标位置
        this.signEleId=signEleId;//签章ID
        this.z_index=0;
    }
    var signManage=new Object();
    var containerMap=$.ContainerMap();
    var mixZ_index=0;//最底层
	var maxZ_index=0;//最高层
    /**
     * 添加签章
     * @param sign
     */
    signManage.addSign=function(sign){
    	if(sign.z_index != null && mixZ_index != 0){
    		mixZ_index=sign.z_index;
    		maxZ_index=sign.z_index;
    	}
    	sign=new Sign(sign.page,sign.signType,sign.zoomRate,sign.pdf_x,sign.pdf_y,sign.signEleId);
    	var key=sign.signEleId;
    	var bool=containerMap.put(key,sign);
    	if(bool){
    		maxZ_index++;
    		sign.z_index=maxZ_index;//新添加的节点为最高层
    	}
    	return bool;
    }
    /**
     * 根据签章Key删除签章
     * @param signKey
     */
    signManage.removeSign=function(signKey){
    	var bool=containerMap.remove(signKey);
    	return bool;
    }
    /**
     * 根据签章Key取得签章
     * @param signKey
     */
    signManage.getkeySign=function(signKey){
        return containerMap.get(signKey);
    }
    /**
     * 根据页码取得签章
     * @param page
     */
    signManage.getSign=function(page){
        var valueArray=containerMap.getAll();
        var resultJson=[];
        for(var item in valueArray){
            var sign=valueArray[item];
            if(sign==null){continue;}
            if(sign.page==page){
                resultJson.push(sign);
            }
        }
        valueArray=null;
        return resultJson;
    }
    /**
     * 修改 签章的pdf坐标
     * @param signKey
     * @param pdf_x
     * @param pdf_y
     */
    signManage.editSign=function(signKey,pdf_x,pdf_y){
        var sign=containerMap.get(signKey);
        sign.pdf_x=pdf_x;
        sign.pdf_y=pdf_y;
    }
    /**
     * 修改 签章的zoomRate
     **/
    signManage.editSignZoomRate=function(signKey,zoomRate){
        var sign=containerMap.get(signKey);
        sign.zoomRate=zoomRate;
    }
    /**
	 * 取得所有数据
	 */
    signManage.getAll=function(){
		return containerMap.getAll();
	}
    
    /**
     * 取得最高层
     */
    signManage.getMaxZ_index=function(){
    	return maxZ_index;
    }
    /**
     * 判断 signType 类型的签章是否存在
     */
    signManage.isSignType=function(signType){
    	var signs=signManage.getAll();
    	for(var item in signs){
    		var sign=signs[item];
    		if(sign == null || typeof(sign) != 'object'){
    			continue;
    		}
    		if(signType == sign.signType){
    			return true;
    		}
    	}
    	return false;
    }
    return signManage;
}
/**
 * Sign 签章操作类
 * @constructor
 */
var SignatureOperate=function(signModel){
	//外部通知回调接口
	var externalBackCall=new Object();
    /**
     * 取得签章在PDF中的位置
     * @param id 签章的ID
     * @param offsetTop 签章到顶部的距离
     * @param offsetLeft 签章到右边的距离
     * 
     * @param dragRegionScrollTop 签章拖曳区域 滚动条到顶部的距离
     * @param dragRegionOffset 签章拖曳区域 offset(绝对位置)
     */
    SignatureOperate.getPDFPostion=function(id,offsetTop,offsetLeft,scrollTop,dragRegionOffset){
        var resultJson={};
        //滚动条距离 顶部 为0
        //签章div距离图片顶部的距离   top left
        var top=offsetTop-dragRegionOffset.top;
        var left=offsetLeft-dragRegionOffset.left;
        //签章 在 当前pdf页上的 的坐标位置
        resultJson.signatureX_left=left;
        resultJson.signatureY_top=top;
        return resultJson;
    };
    /**
     * 取得签章在浏览器 绝对坐标  
     **/
    SignatureOperate.getBrowserOffset=function(offsetTop,offsetLeft,scrollTop,signatureX_left,signatureY_top){
        var resultJson={};
        var top=offsetTop+signatureY_top;
        var left=offsetLeft+signatureX_left;
        resultJson.offset_top=top;
        resultJson.offset_left=left;
        return resultJson;
    }
    /**
     * 删除签章
     **/
    externalBackCall.deleteEvent=null;
    SignatureOperate.deleteEvent=function(id){
    	//函数回调  对外开放
        if(externalBackCall.deleteEvent !=null  &&  typeof(externalBackCall.deleteEvent)=="function"){
        	externalBackCall.deleteEvent(id);
        }
        signModel.removeSign(id);
    }
    /**
     * 签章缩放
     **/
    externalBackCall.input=null;
    SignatureOperate.input=function(id,val){
    	signModel.editSignZoomRate(id,val);
        //函数回调  对外开放
        if(externalBackCall.input !=null  &&  typeof(externalBackCall.input)=="function"){
        	var offset=externalBackCall.input(id,val);
        	
        	if(typeof offset == "object" && offset.top != null && offset.left != null){
            	SignatureOperate.offsetChange(id,offset.top,offset.left);
        	}
        }
    }
    /**
     * 签章位置发生改变
     **/
    externalBackCall.offsetChange=null;
    SignatureOperate.offsetChange=function(id,top,left){
    	if($("#"+id) <= 0){return;}
		var dragRegion=$("#"+id).parent();
        var resultJson=SignatureOperate.getPDFPostion(id,top,left,dragRegion.parent().scrollTop(),dragRegion.offset());
        signModel.editSign(id,resultJson.signatureX_left,resultJson.signatureY_top);
        //函数回调  对外开放
        if(externalBackCall.offsetChange !=null  &&  typeof(externalBackCall.offsetChange)=="function"){
        	externalBackCall.offsetChange(id,top,left);
        }
    }
    
    /**
     * 签章添加
     **/
    externalBackCall.add=null;
    SignatureOperate.add=function(id,signType,zoomRate,page){
    	if($("#"+id) <= 0){return;}
        var offset=$("#"+id).offset();
		var dragRegion=$("#"+id).parent();
		
        console.log("拖动对象 offsetTop "+offset.top+"   拖动对象 offsetLeft "+offset.left);

		
        console.log("dragRegion offsetTop"+dragRegion.offset().top+"   dragRegion offsetLeft"+dragRegion.offset().left);

		
        var resultJson=SignatureOperate.getPDFPostion(id,offset.top,offset.left,dragRegion.parent().scrollTop(),dragRegion.offset());
        var sign={};
        
        console.log("   pdf_x"+resultJson.signatureX_left+"   pdf_y"+resultJson.signatureY_top);
        console.log("----------------------------------------");
        
        sign.page=page;
        sign.signType=signType;
        sign.zoomRate=zoomRate;
        sign.pdf_x=resultJson.signatureX_left;
        sign.pdf_y=resultJson.signatureY_top;
        sign.signEleId=id;
        sign.z_index=parseInt($("#"+id).css("z-index"));
        signModel.addSign(sign);
        var signNew=signModel.getkeySign(id);
        //从新定义层次结构
        $("#"+id).css("z-index",""+signNew.z_index);
        //函数回调  对外开放
        if(externalBackCall.add !=null  &&  typeof(externalBackCall.add)=="function"){
        	externalBackCall.add(id,signType,zoomRate);
        }
    }
    
    /**
     * 取得同类型的签章个数
     */
    SignatureOperate.getSignTypeCount=function(signType){
   		var signatureArrays=signModel.getAll();
   		var count=0;
   		for(var item in signatureArrays){
   			var sign=signatureArrays[item];
   			if(sign != null && sign.signType == signType){
   				count++;
   			}
   		}
   		return count;
   	}
    
    /**
     * 取得所有签章的个数
     */
    SignatureOperate.getAllSignCount=function(){
   		var signatureArrays=signModel.getAll();
   		var count=0;
   		for(var item in signatureArrays){
   			var sign=signatureArrays[item];
   			if(sign != null){
   				count++;
   			}
   		}
   		return count;
   	}
    
    
    /**
     *资质章的特殊情况 
     *  改版后水印资质章不能盖多个
     * 
     */
//    SignatureOperate.getSignCredentialTypeCount=function(signType){
//    		var signatureArrays=signModel.getAll();
//    		var count=0;
//    		for(var item in signatureArrays){
//    			var sign=signatureArrays[item];
//    			if(sign != null ){
////    			if(sign.corFlag=='2'){
//    				if( sign.signType != signType && typeof(sign.signType)!='undefined'){
//    					count++;
//    				}
////    			}
//    		  }
//    		}
//    		return count;
//    	}
//    
    
    return externalBackCall;
};



/**
 * Sign 签章组装
 */
//TODO 浏览器窗口最小化时，滑动滚动条，再最大化对于 签章在pdf上的的坐标有一定的影响--未解决
function SignAssemble(){
	//签章可视对象
	var signViewObject=new Object();
	var isMove=false;
	//签章创建
	signViewObject.createSign=function(options,signatureOperate){
		//1.默认参数
	    var settings={
	        zoom:true,//是否支持缩放
	        zoomRate:1,//缩放倍率
	        isHide:false,//是否隐藏  true 是
	        signTotalCount:1,
	        allSignCount:5,
	        isMove:true,//ture 代表能拖动，false代表不能拖动
	        isEnlarge:true,//true 支持放大  false不支持放大
	        isNarrow:true,//true 支持缩小
	        isNotOffset:false,//不用判斷坐標
			borderMove:null//沿着边 left right bottom left
	    }
	    settings.signatureOperate=signatureOperate;//外部接口
	    //2.验证参数
	    if(options.signRegion == null || options.signRegion == "" || $("#"+options.signRegion).length != 1){
	        $.eventHint("参数dragRegion 拖曳区域ID不能为空！");
	        return;
	    }
	    if(options.imgPath == null || options.imgPath == "" ){
	        $.eventHint("参数imgPath 图片路径不能为空！");
	        return;
	    }
	    if(options.signType == null || options.signType == "" ){
	        $.eventHint("参数 signType 签章类型不能为空！");
	        return;
	    }
	    
	    //签章的参数
	    options=$.extend(settings,options)
	    var signRegion=$("#"+options.signRegion);//签章存在的区域
	    var dragRegion=signRegion;//签章可以拖曳的区域
	    dragRegion.attr("dragregion",'');
	    
	    if(SignatureOperate.getAllSignCount() == options.allSignCount){
    		layer.msg("签章数量不能超过"+options.allSignCount+"个");
    		return;
	    }
	    
//	    if(options.corFlag!='2'){
	    	var signTotalCount=SignatureOperate.getSignTypeCount(options.signType);
	    	if(signTotalCount>=options.signTotalCount){
	    		layer.msg("签章数量不能超过"+options.signTotalCount+"个");
	    		return;
	    	}
	    	
	    	
//	    }else{
//	    	var signCredentialTotalCount=SignatureOperate.getSignCredentialTypeCount(options.signType);
//	    	if(signCredentialTotalCount>=options.signTotalCount){
//	    		layer.msg("一个报告只能盖一个资质章!");
//	    		return;
//	    	}
//	    }
	    
	    //签章的主要元素
		var singImgDiv=signViewObject.singImgDiv(options);
		var signOperationDiv=signViewObject.signOperationDiv(options,signatureOperate);
		
        if(!options.isEnlarge){
        	signOperationDiv.find("img[operation='add']").hide();
        }
		if(!options.isNarrow){
        	signOperationDiv.find("img[operation='reduce']").hide();
        }
		
		var signDiv=signViewObject.signDiv(singImgDiv,signOperationDiv,options);
		
		if(options.borderMove != null){
			signDiv.attr("border-move",options.borderMove);
		}
		
		//签章的父子关系
		signDiv.append(singImgDiv);
		signDiv.append(signOperationDiv);
        
        //签章绑定拖曳控件
        var parem={};
        parem.moveBegin=function(moveDom){
        	if(typeof options.moveBegin == "function"){
        		options.moveBegin(moveDom);
        	}
        	
        	if(options.isMove){
        		signViewObject.moveBegin(moveDom);
        	}
        }
        parem.moveEnd=function(moveDom){
        	if(typeof options.moveEnd == "function"){
        		options.moveEnd(moveDom);
        	}
        	
        	if(options.isMove){
        		signViewObject.moveEnd(moveDom);
        	}
        }
        parem.ismovein=function(moveDom,top,left,offsetChange){
        	if(typeof options.ismovein == "function"){
        		options.ismovein(moveDom,top,left);
        	}
        	
        	if(options.isMove){
            	return signViewObject.ismovein(moveDom,top,left,offsetChange);
            }
        }
        parem.offsetChange=options.signatureOperate.offsetChange;
        singImgDiv.drag(parem);
        if(typeof(options.offsetTop) == 'number' || typeof(options.offsetLeft) == 'number'){
		    var dragRegionOffset=dragRegion.offset();
			var c={};
			
			//拖曳区域的倍率计算 默认是 1倍
			var rate=1;
			if(typeof options.rateCount == "function"){
				rate=options.rateCount();
				signatureOperate.rateCount=options.rateCount;
			}
			
			//处理精确度问题
	        c.top=parseInt(dragRegionOffset.top+(dragRegion.height()-singImgDiv.height()*rate));
	        c.left=parseInt(dragRegionOffset.left+(dragRegion.width()-singImgDiv.width()*rate));
	        options.offsetTop=parseInt(options.offsetTop);
	        options.offsetLeft=parseInt(options.offsetLeft);
	        console.log("offsetTop"+options.offsetTop+"  offsetLeft"+options.offsetLeft);
	        //判断是否在此区域中
	        if(dragRegionOffset.top<=options.offsetTop && options.offsetTop<=c.top && dragRegionOffset.left<=options.offsetLeft && options.offsetLeft<=c.left){
	        	signRegion.append(signDiv);
	        	signDiv.offset({top:options.offsetTop,left:options.offsetLeft});
	        }else if(options.isNotOffset){
				signRegion.append(signDiv);
				signDiv.offset({top:options.offsetTop,left:options.offsetLeft});
			}else{
	        	return;
	        }
		}else{
			//拖曳区域的倍率计算 默认是 1倍
			var rate=1;
			if(typeof options.rateCount == "function"){
				rate=options.rateCount();
				signatureOperate.rateCount=options.rateCount;
			}
			if(singImgDiv.height()*rate < dragRegion.height() && singImgDiv.width()*rate < dragRegion.width()){
				//签章初始位置
				signViewObject.signPosition(dragRegion.offset(),dragRegion.height(),dragRegion.width(),signDiv);
				signRegion.append(signDiv);
			}else{
				return;
			}
		}
    	//签章
        signatureOperate.add(signDiv.attr("id"),options.signType,options.zoomRate,options.page);
        //签章默认缩放
		signViewObject.zoomChange(signDiv,signatureOperate);
		//IE7.0解决操作区域问题
        var browser=$.getBrowserVersion();
		var ie=browser.version;
    	if(browser.browser == 'IE' && ie != null && ie != '' && ie <9.0){
    		signViewObject.signOperationDivIE7(signDiv.attr("id"));
    	}
    	if(options.isHide){
			signDiv.remove();
			return;
		}
	}
	//签章的位置
	signViewObject.signPosition=function(dragRegionOffset,dragRegionHeight,dragRegionWidth,signDiv){
		//印章区域的 初始位置 
        var dragRegion_height=dragRegionHeight/2-signDiv.height()/2;
        var dragRegion_width=dragRegionWidth/2-signDiv.width()/2;
        var dragRegion_left=dragRegionOffset.left+dragRegion_width;
        var dragRegion_top=dragRegionOffset.top+dragRegion_height;
        signDiv.offset({top:dragRegion_top,left:dragRegion_left});
	}
	//签章DOM
	signViewObject.signDiv=function(signImgDiv,signOperationDiv,options){
		//生成印章图片包裹的DIV
        var id=$.random();
        var div=$("<div/>");
        div.attr("id",id);
        div.attr("signType",options.signType);
        var width=signImgDiv.width()+signOperationDiv.width();
        var height=signImgDiv.height();
        if(height<signOperationDiv.height()){
        	height=signOperationDiv.height();
        }
        div.css({
        	"heigth":"auto",
            "width":"auto",
        	"position":"absolute",
        	"z-index":"2",
        	"width":width+"px",
        	"height":height+"px"
        	/*"border-color":"#999",
    		"border-style":"solid",
    		"border-width":"1px"*/
        });
        return div;
	}
	//签章图片DOM
	signViewObject.singImgDiv=function(options){
		
    	//放入默认高度到 节点
    	var imgDiv=$("<div sign=''/>");
    	var heightdata={};
    	heightdata.height=options.imgHeight;
    	heightdata.width=options.imgWidth;
    	imgDiv.attr("heightdata",JSON.stringify(heightdata));
    	//根据缩放倍率  从新计算印章图片大小
    	var imgHeight=options.imgHeight*options.zoomRate;
    	var imgWidth=options.imgWidth*options.zoomRate;
    	imgDiv.data("zoomRate",options.zoomRate);
    	imgDiv.data("zoom",options.zoom);
    	//设置印章图片的大小
    	imgDiv.css({
        	/*"border-color":"red",
    		"border-style":"solid",
    		"border-width":"1px",*/
    		"height":imgHeight+"px",
    		"width":imgWidth+"px",
    		"float":"left",
			"cursor":"move"
        });
    	imgDiv.height(imgHeight);
    	imgDiv.width(imgWidth);
    	
    	//设置图片背景
    	var url=$.replaceAll(options.imgPath,"\\","/");	
    	var browser=$.getBrowserVersion();
    	var ie=browser.version;
    	if(browser.browser == 'IE' && ie != null && ie != '' && ie <9.0){
    		//img
    		var img=$("<img/>");
        	img.height(imgHeight);
        	img.width(imgWidth);
        	img.attr("src",url);
        	imgDiv.append(img);
    	}else{
        	imgDiv.css("background-image",('url("'+url+'")'));
    		imgDiv.css("background-positions-x",'0px');
    		imgDiv.css("background-positions-y",'0px');
        	imgDiv.css("background-size",imgWidth+"px "+imgHeight+"px");
        	imgDiv.css("background-repeat","no-repeat");
    	}
    	imgDiv.bind("mouseover",function(){
    		$(this).css("cursor","move");
    	});
    	return imgDiv;
	}
	//解决IE7的问题
	signViewObject.signOperationDivIE7=function(id){
		SignAssemble.IE78createImg();
		var nodeList =document.getElementById(id).childNodes;
		var node=null;
		var operationList=null;
		for(var item=0;item<nodeList.length;item++){
			node=nodeList[item];
			if(node.getAttribute("operation")=="operation"){
				operationList=node.childNodes;
				break;
			}
		}
		if(node != null){
			for(var item=0;item<operationList.length;item++){
				var oldSign=operationList[item];
				var operation=oldSign.getAttribute("operation");
				var newSign=document.getElementById(operation+"Sign").cloneNode(true);
				if(newSign.height != 20){
					window.location.reload();
				}
				newSign.style.display="";
				newSign.id=null;
				//document.getElementById("test").appendChild(node);
				node.replaceChild(newSign,oldSign);
			}
		}
	}
	signViewObject.signOperationImg=function(height,width,src,operation){
		var img=$("<img/>");
     	var browser=$.getBrowserVersion();
		if(browser.browser == 'IE' && browser.version > 0){
			img.css("cursor","hand"); 
		}else{
			img.css("cursor","pointer");
		}
    	img.height(height);
    	img.width(width);
    	img.attr("src",src);
    	img.attr("operation",operation);
    	return img;
	}
	//签章操作区域DOM
	signViewObject.signOperationDiv=function(options,signatureOperate){
		var div=$("<div style='float:right;margin-top:auto;margin-bottom:auto;' operation='operation'/>");
		var height=20;
		var width=20;
		// +  operation='add' 必须有的属性add
		var addInput=signViewObject.signOperationImg(height,width,options.addSignSrc,"add");
		// -  operation='reduce' 必须有的属性
		var reduceInput=signViewObject.signOperationImg(height,width,options.reduceSignSrc,"reduce");
		// 删   operation='delete' 必须有的属性
		var deleteInput=signViewObject.signOperationImg(height,width,options.deleteSignSrc,"delete");
		if(options.zoom){
			div.append(addInput);
			div.append(reduceInput);
		}
		div.append(deleteInput);
		var float={"float":"left","margin-top":"2px"};
		//设置样式
		addInput.css(float);
		reduceInput.css(float);
		deleteInput.css(float);
		
		var inputWidth=0;
		if(addInput.width()>reduceInput.width()){
			inputWidth=addInput.width();
		}else{
			inputWidth=reduceInput.width();
		}
		
		if(deleteInput.width()>inputWidth){
			inputWidth=deleteInput.width();
		}
		
		var inputHeight=0;
		if(addInput.height()>reduceInput.height()){
			inputHeight=addInput.height();
		}else{
			inputHeight=reduceInput.height();
		}
		if(deleteInput.height()>inputHeight){
			inputHeight=deleteInput.height();
		}
		inputHeight=inputHeight;
		div.css({"width":inputWidth+"px"});
		if(options.zoom){
			div.css({"height":inputHeight*3+"px"});
		}else{
			div.css({"height":inputHeight*1+"px"});
		}
		
		//绑定事件
		div.on("click","img[operation='add']",function(){
			var img=$(this).parent().parent().find("div[sign]");
			var zoomRate=img.data("zoomRate");
			zoomRate=zoomRate+0.1;
			zoomRate=zoomRate.toFixed(1);
			zoomRate=parseFloat(zoomRate);
			img.data("zoomRate",zoomRate);
			signViewObject.zoomChange($(this).parent().parent(),signatureOperate);
            return false;//阻止冒泡事件
        });
		div.on("click","img[operation='reduce']",function(){
			var img=$(this).parent().parent().find("div[sign]");
			var zoomRate=img.data("zoomRate");
			zoomRate=zoomRate-0.1;
			zoomRate=zoomRate.toFixed(1);
			zoomRate=parseFloat(zoomRate);
			img.data("zoomRate",zoomRate);
			signViewObject.zoomChange($(this).parent().parent(),signatureOperate);
            return false;//阻止冒泡事件
        });
		//绑定事件
		div.on("click","img[operation='delete']",function(){
            $(this).parent().parent().remove();//页面删除
            
            if(options.signatureOperate.deleteEvent !=null  &&  typeof(options.signatureOperate.deleteEvent)=="function"){
            	options.signatureOperate.deleteEvent($(this).parent().parent().attr("id"));//逻辑删除
            }//逻辑删除
            return false;//阻止冒泡事件
        });
		return div;
	}
	
	//签章放大缩小
	signViewObject.zoomChange=function(signDiv,signatureOperate) {
		var zoom=signDiv.find("div[sign]").data("zoom");
        if (zoom && !isMove) {
        	var zoomRate=signDiv.find("div[sign]").data("zoomRate");
            if (isNaN(zoomRate) || zoomRate == null || zoomRate == "") {
            	signDiv.attr("title", intl.signDrag_inputNumber);
                return;
            } else {
            	signDiv.attr("title", intl.signDrag_zoomRate(zoomRate));
            }
            
            var img=signDiv.find("div[sign]");
            var heightdata=$.parseJSON(img.attr("heightdata"));
            
            
            var img_height=heightdata.height;
            var img_width=heightdata.width;
            
            var signDiv_height=signDiv.height();
            var signDiv_width=signDiv.width();
            
            var img_zoomRate_height=img_height*zoomRate;
            var img_zoomRate_width=img_width*zoomRate;
            // 索引操作区域的高度宽度
            var signOperationDiv_height=signDiv.find("div[operation]").height();
            var signOperationDiv_width=signDiv.find("div[operation]").width();
            
    		var dragRegion=signDiv.parent();
    		
    		//拖曳区域的倍率计算 默认是 1倍
			var rate=1;
			if(typeof signatureOperate.rateCount == "function"){
				rate=signatureOperate.rateCount();
			}
    		
            var dragImg_width=dragRegion.width()/rate;
            var dragImg_height=dragRegion.height()/rate;
            if(img_zoomRate_height<signOperationDiv_height){
            	signDiv_height=img_zoomRate_height+signOperationDiv_height;
            }else{
            	signDiv_height=img_zoomRate_height; 
            }
            signDiv_width=img_zoomRate_width+signOperationDiv_width;
            if(signDiv_width>dragImg_width || signDiv_height>dragImg_height){
            	//水印特殊處理
            	if(!(img_height > (dragImg_height -4) && img_height<=dragImg_height && img_width<=dragImg_width && img_width > (dragImg_width -4))){
                	layer.msg(intl.signDrag_imageBig);
            	}
            	signDiv.attr("title", intl.signDrag_imageBig);
            	zoomRate=zoomRate-0.1;
            	img.data("zoomRate",zoomRate);
            	return;
            }
            if(zoomRate<0.3){
            	layer.msg(intl.signDrag_imageSmail);
            	signDiv.attr("title", intl.signDrag_imageSmail);
            	zoomRate=zoomRate+0.1;
            	img.data("zoomRate",zoomRate);
            	return;
            }
            img.data("zoomRate",zoomRate);
            
            img.height(img_zoomRate_height);
            img.width(img_zoomRate_width);
            img.css({
        		"height":img_zoomRate_height+"px",
        		"width":img_zoomRate_width+"px"
            });
            signDiv.css({
        		"height":signDiv_height+"px",
        		"width":signDiv_width+"px"
            });
        	var browser=$.getBrowserVersion();
            var ie=browser.version;
        	if(browser.browser == 'IE' && ie != null && ie != '' && ie <9.0){
        		var img=signDiv.find("img");
        		img.width(img_zoomRate_width);
        		img.height(img_zoomRate_height);
        	}else{
                img.css("background-size",img_zoomRate_width+"px "+img_zoomRate_height+"px");
        	}
            //放大背景
            //宽高
            signDiv.width(signDiv_width+5);
            signatureOperate.input(signDiv.attr("id"),zoomRate);
            
            //IE7.0解决操作区域问题
       	    var browser=$.getBrowserVersion();
            var ie=browser.version;
        	if(browser.browser == 'IE' && ie != null && ie != '' && ie <9.0){
        		signViewObject.signOperationDivIE7(signDiv.attr("id"));
        	}
        	
        	//放大缩小后解决越界问题
        	var signImgDiv=signDiv.find("div[sign]");
        	var top=signDiv.offset().top;
        	var left=signDiv.offset().left;
        	
        	var dragRegion=signDiv.parent();
        	var dragRegionOffset=dragRegion.offset();
        	var c={};
	        c.top=dragRegionOffset.top+(dragRegion.height()-signImgDiv.height());
	        c.left=dragRegionOffset.left+(dragRegion.width()-signImgDiv.width());
	        if(!(dragRegionOffset.top<=top && top<=c.top && dragRegionOffset.left<=left && left<=c.left)){
	        	
	        	//top 不符合
	        	if(!(dragRegionOffset.top<=top && top<=c.top)){
	        		var _top=(top+signImgDiv.height())-(dragRegionOffset.top+dragRegion.height());
	        		top=top-_top;
	        	}
	        	
	        	//left 不符合
	        	if(!(dragRegionOffset.left<=left && left<=c.left)){
	        		var _left=(left+signImgDiv.width())-(dragRegionOffset.left+dragRegion.width());
	        		left=left-_left;
	        	}
	        	
	        	signatureOperate.offsetChange(signDiv.attr("id"),top,left);
	        	signDiv.offset({top:top,left:left});
            	/*signDiv.css("top",top+"px");
            	signDiv.css("left",left+"px");*/
	        	return false;
	        }
        }
    }
	/*
	 * 签章开始移动 
	 * moveDom 移动的DOM
	 */
	signViewObject.moveBegin=function(moveDom){
		moveDom=moveDom.parent();
		isMove=true;
		moveDom.addClass("boxShow");
		
        var imgDiv=moveDom.find("div[sign]");
        var operationDiv=moveDom.find("div[operation]");
        moveDom.width(imgDiv.width()+operationDiv.width()+7);
	}
	
	/*
	 * 签章结束移动
	 * moveDom 移动的DOM
	 */
	signViewObject.moveEnd=function(moveDom){
		moveDom=moveDom.parent();
		isMove=false;
		moveDom.removeClass("boxShow");
        var imgDiv=moveDom.find("div[sign]");
        var operationDiv=moveDom.find("div[operation]");
        moveDom.width(imgDiv.width()+operationDiv.width()+5);
        $("div[cover]").remove();
	}
	
	/*
	 * 签章 正在移动 
	 * 		参数
	 * 		moveDom 移动dom
	 *		top 下一次要移动的绝对位置
	 *		left 下一次要移动的绝对位置
	 *return boolean 
	 */
	signViewObject.ismovein=function(moveDom,top,left,offsetChange){
		moveDom=moveDom.parent();
		if(isMove){
			var dragRegion=moveDom.parent();
			var c={},signImgDiv=moveDom.find("div[sign]");
	        //thisimg A dragRegion B
	        var dragRegionOffset=dragRegion.offset();
	        c.top=dragRegionOffset.top+(dragRegion.height()-signImgDiv.height());
	        c.left=dragRegionOffset.left+(dragRegion.width()-signImgDiv.width());
	        
	        
	        //特殊处理
			var borderMove=moveDom.attr("border-move");
	        if(typeof borderMove == "string"){
	        	if(borderMove == "right"){
	        		dragRegionOffset.left=dragRegionOffset.left - 20;
	        		c.left=c.left+20;
	        	}
	        }
	        
	        if(dragRegionOffset.top<=top && top<=c.top && dragRegionOffset.left<=left && left<=c.left){
	        	signImgDiv.css("cursor","move");
	        	//特殊处理
				var borderMove=moveDom.attr("border-move");
	        	if(borderMove == "left"){
	        		left=dragRegion.offset().left;
	        	}else if(borderMove == "right"){
	        		left=dragRegion.offset().left+dragRegion.width()-signImgDiv.width();
	        	}else if(borderMove == "top"){
	        		top=dragRegion.offset().top;
	        	}else if(borderMove == "bottom"){
	        		top=dragRegion.offset().top+dragRegion.height()-signImgDiv.height();
	        	}
	        	
	        	if(offsetChange !=null && typeof(offsetChange) == "function"){
	            	offsetChange(moveDom.attr("id"),top,left);
	            	moveDom.offset({top:top,left:left});
	        	}
	        	//moveDom.css("top",top+"px");
	        	//moveDom.css("left",left+"px");
            	//moveDom.css("position","absolute");
	        	return false;
	        }else{
	        	var signImgDiv=moveDom.find("div[sign]");
	        	signImgDiv.css("cursor","not-allowed");
	        	signImgDiv.bind("mousemove",function(){
	        		return false;
	        	});
	        	//event.preventDefault();
	        }
	        
		}else{
//			console.log("不在拖曳状态");
			//IE7.0 8.0解决图片能拖动问题
//        	var ie=$.isIEBrowserVersion();
//        	if(ie != null && ie != '' && ie <9.0){
//        		signViewObject.signOperationDivIE78(signDiv.attr("id"));
//        	}
			
		}
        return false;
	}
	return signViewObject;
}
SignAssemble.IE78createImg=function(){
	//IE7.0解决操作区域问题
  	var browser=$.getBrowserVersion();
    var ie=browser.version;
	if(browser.browser== 'IE' && ie != null && ie != '' && ie <9.0 && document.getElementById("addSign") == null){
		var img=document.createElement("img");
		img.height=20;
		img.width=20;
		img.style.display="none";
		img.style.cursor="hand";
		img.src="img/addSign.png";
		img.id="addSign";
		img.setAttribute("operation","add");
		var reduce=img.cloneNode(true);
		var deleteSign=img.cloneNode(true);
		reduce.src="img/reduceSign.png";
		reduce.id="reduceSign";
		reduce.setAttribute("operation","reduce");
		deleteSign.src="img/deleteSign.png";
		deleteSign.id="deleteSign";
		deleteSign.setAttribute("operation","delete");
		document.getElementById("signatureHead").appendChild(img);
		document.getElementById("signatureHead").appendChild(reduce);
		document.getElementById("signatureHead").appendChild(deleteSign);
	}
}
window.onload = function() {
	SignAssemble.IE78createImg();
};

