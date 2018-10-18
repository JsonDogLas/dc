






$.fn.idsLayer=function(options){
	var server={};
	$(this).click(function(){
		idsLayer(options);
	});
}

$.idsLayerOn=function(options){
	var server={};
	$("body").on("click",options.ele,function(){
		idsLayer(options);
	});
}


$.addLayerSize=function(width,height){
	if(!(typeof width == "number" && typeof height == "number")){
		return;
	}
	var _width=number(options.width.replace("px",""));
	var _height=number(options.height.replace("px",""));
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	width=(width+_width)+"px";
	height=(height+_height)+"px";
	layer.style(index,{
		width:width,
		width:height
	}); //再执行关闭   
}

//$.idsLayerMsg=function(msg,options){
//	var defaults={};
//	defaults.isParentPosition=false;//默认false 是否根据父页面的滚动条来计算位置
//	defaults.height=(parseInt(msg.length/50) * 48+48)+"px";
//	defaults.width=(msg.length>50 ? 50*2 : msg.length * 2)+"px";
//	options=$.extend(defaults,options);
//	if(options.isParentPositon){
//		var position=idsLayer.countParentPosition(options);
//		//offset: ['100px', '50px']
//		options.offset=[position.top+"px",position.left+"px"];
//	}
//	layer.msg(msg, {
//	  time: 1500+((msg.length-4)*200), //2秒关闭（如果不配置，默认是3秒）
//	  offset:options.offset
//	}, function(){
//	}); 
//}


$.idsLayerEmailMsg=function(msg,options){
	var defaults={};
	defaults.isParentPosition=false;//默认false 是否根据父页面的滚动条来计算位置
	defaults.height=(parseInt(37/20) *-400
			-48)+"px";
//	defaults.height=(parseInt($(window).height())*-1+100px)+"px";
	defaults.width=(msg.length>50 ? 50*2 : msg.length * 2)+"px";
	options=$.extend(defaults,options);
	if(options.isParentPositon){
		var position=idsLayer.countParentPosition(options);
//		offset: ['1322px', '667px']
		options.offset=[position.top+"px",position.left+"px"];
	}
	layer.msg(msg, {
	  time: 1500+((msg.length-4)*200), //2秒关闭（如果不配置，默认是3秒）
	  offset:options.offset
	}, function(){
	}); 
}

var idsLayer=function(options){
	var defaults={};
	defaults.width="500px";
	defaults.height="300px";
	defaults.content="请传入content";
	defaults.shadeClose=true;//有遮罩
	defaults.maxmin=false;//最大化
	defaults.resize=true;//拉伸
	defaults.closeBtn=false;//关闭
	defaults.title="";
	defaults.type=1;
	defaults.cancel=function(){};
	defaults.offset=null;
	//skin: 'demo-class'
	defaults.skin="demo-class";
	defaults.shade=[0.3,'#393d49'];//遮罩的透明度
	defaults.isParentPosition=false;//默认false 是否根据父页面的滚动条来计算位置
	options=$.extend(defaults,options);
	if(options.isParentPositon){
		var position=idsLayer.countParentPosition(options);
		//offset: ['100px', '50px']
		if(position.top == null){
            options.offset=[null,position.left+"px"];
        }else if(position.left == null){
            options.offset=[position.top+"px"];
        }else{
            options.offset=[position.top+"px",position.left+"px"];
        }

		layer.open({
			title:options.title,
			skin:options.skin,
	        type: options.type,
	        shade: options.shade,
	        shadeClose:options.shadeClose,
	        area: [options.width, options.height],
	        maxmin: options.maxmin,
	        content: options.content,
	        offset:options.offset,
	        closeBtn:options.closeBtn,
	        zIndex: layer.zIndex, //重点1
	        success: function(layero){
	            layer.setTop(layero); //重点2
	            if(typeof options.success == "function"){
	            	options.success();
	            }
	        },
            cancel:function(){
                console.log("cancel");
                options.cancel();
                if(typeof options.cancel == "function"){
                    options.cancel();
                }
            },
			end:function(){
				options.cancel();
				if(typeof options.end == "function"){
                    options.end();
				}
				
			}
	    });
		return;
	}else if(options.offset != null){
		layer.open({
			title:options.title,
			skin:options.skin,
	        type: options.type,
	        shade: options.shade,
	        shadeClose:options.shadeClose,
	        area: [options.width, options.height],
	        maxmin: options.maxmin,
	        content: options.content,
	        offset:options.offset,
	        closeBtn:options.closeBtn,
	        zIndex: layer.zIndex, //重点1
	        success: function(layero){
	            layer.setTop(layero); //重点2
	            if(typeof options.success == "function"){
	            	options.success();
	            }
	        },
	        cancel:function(){
	        	console.log("cancel");
				options.cancel();
				if(typeof options.cancel == "function"){
                    options.cancel();
				}
			},
			end:function(){
	        	console.log("end");
			}
	    });
		return;
	}
	
	layer.open({
		title:options.title,
		skin:options.skin,
        type: options.type,
        shade: options.shade,
        shadeClose:options.shadeClose,
        area: [options.width, options.height],
        maxmin: options.maxmin,
        content: options.content,
        closeBtn:options.closeBtn,
        zIndex: layer.zIndex, //重点1
        success: function(layero){
            layer.setTop(layero); //重点2
            if(typeof options.success == "function"){
            	options.success();
            }
        },
        cancel:function(){
        	console.log("cancel");
			options.cancel();
			if(typeof idsLayer.cancel == "function"){
				idsLayer.cancel();
			}
		},
		end:function(){
        	console.log("end");
		}
    });
	var service={};

}

//个人中心 专用
var idsLayerStamp=function(options){
	var defaults={};
	defaults.width="500px";
	defaults.height="300px";
	defaults.content="请传入content";
	defaults.shadeClose=true;//有遮罩
	defaults.maxmin=false;//最大化
	defaults.resize=true;//拉伸
	defaults.closeBtn=false;//关闭
	defaults.title="";
	defaults.type=1;
	defaults.cancel=function(){};
	//skin: 'demo-class'
	defaults.skin="demo-class";
	defaults.shade=[0.3,'#393d49'];//遮罩的透明度
	defaults.isParentPosition=false;//默认false 是否根据父页面的滚动条来计算位置
	options=$.extend(defaults,options);

	
	layer.open({
		title:options.title,
		skin:options.skin,
        type: options.type,
        shade: options.shade,
        shadeClose:options.shadeClose,
        area: [options.width, options.height],
        maxmin: options.maxmin,
        content: options.content,
        closeBtn:options.closeBtn,
        offset:options.offset,
        zIndex: layer.zIndex, //重点1
/*        offset:['500px','300px'],*/
        success: function(layero){
            layer.setTop(layero); //重点2
            if(typeof options.success == "function"){
            	options.success();
            }
        },
        cancel:function(){
        	console.log("cancel");
			options.cancel();
			if(typeof idsLayer.cancel == "function"){
				idsLayer.cancel();
			}
		},
		end:function(){
        	console.log("end");
		}
    });
	var service={};

}


idsLayer.countParentPosition=function(options){
	//获取滚动条到顶部的垂直高度 (即网页被卷上去的高度) 
	var scrollTop=parent.$("body").scrollTop();  
	//获取滚动条到左边的垂直宽度 ：
	var scrollLeft=parent.$("body").scrollLeft(); 
	
	//原始的高度
	var orignWidth=Number(options.width.replace("px",""));
	var orignHeight=Number(options.height.replace("px",""));
	var bodyWidth=screen.availWidth;
	var bodyHeight=screen.availHeight;
	console.log("bodyWidth"+bodyWidth);
	console.log("bodyHeight"+bodyHeight);
	console.log("orignWidth"+orignWidth);
	console.log("orignHeight"+orignHeight);
	//88 64
	var left=(bodyWidth-orignWidth)/2+scrollLeft-64-32;
	var top=(bodyHeight-orignHeight)/2+scrollTop-88-44;
	console.log("top"+top);
	console.log("left"+left);
	//屏幕中心位置
	if(options.isParentPositonTop != null && options.isParentPositonTop){
		return {top:top};
	}
    if(options.isParentPositonRight != null && options.isParentPositonRight){
        return {left:left};
    }
	return {top:top,left:left};
}



$.colosLayerIfram=function(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index); //再执行关闭   
}

$.layerTab=function(){
	layer.tab({
		  area: ['600px', '300px'],
		  tab: [{
		    title: 'TAB1', 
		    content: '内容1'
		  }, {
		    title: 'TAB2', 
		    content: '内容2'
		  }, {
		    title: 'TAB3', 
		    content: '内容3'
		  }]
		}); 
}

$.idsLoad=function(options){
	var defaults={};
	defaults.shadeClose=false;//有遮罩
	defaults.shade=[0.3,'#393d49'];//遮罩的透明度
	defaults.time=200*1000;
	options=$.extend(defaults,options);
	layer.load(2,options); 
	return {
		close:function(){
			return layer.closeAll('loading');
		}
	}
}

//加载层弹出内容
$.idsContentLoad=function(content,options){
	var defaults={};
	defaults.shadeClose=false;//有遮罩
	defaults.shade=[0.3,'#393d49'];//遮罩的透明度
	defaults.time=2000*1000;
	defaults.icon=900;
	options=$.extend(defaults,options);
	//layer.load(0,options); 
//	var index = $.tip();
	layer.msg(content,options);
	return {
		close:function(){
			return layer.close(index);
		},
		open:function(){
			var result= $.idsContentLoad(content,options);
			index=result.index;
			return result;
		}
//		index:index
	}
}


$.tipLoad=function(content,options){
	var defaults={};
	defaults.imgSrc="../base/img/timg.gif";
	defaults.tipContent=content;
	defaults.width=435;
	defaults.height=230;
	defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
	defaults.closeTime=1000*18000;//number  一秒钟自动关闭  为空代表自行关闭
	defaults.index=new Date().getTime();
	defaults.tipId="tip_";
	defaults.tipDivId="tipDiv_";
	options=$.extend(defaults,options);
	var open=function(){
		var body=$("body");
		if(options.isParent){
			body=parent.$("body");
		}
		var div=$("<div class='defaultTip'/>");
		//
		var bodyDiv=$("<div />");
		bodyDiv.css({
			"text-align":"center",
			"display":"inline-block"
		});
		var bodyImg=$("<img src='"+options.imgSrc+"' />");
		var bodySpan=$("<span/>");
		bodySpan.text(options.tipContent);
		bodyImg.css({
			width:"200px",
			height:"200px"
		});
		bodySpan.css({
			"margin-left":"15px",
			"font-size":"20px"
		});
		bodyDiv.append(bodySpan);
		
		var tipDiv=$("<div />");
		tipDiv.append(bodyImg);
		tipDiv.append(bodyDiv);
		tipDiv.width(options.width);
		tipDiv.height(options.height);
		var left=body.width()/2-options.width/2;
		var top=body.height()/2-options.height/2;
		
		tipDiv.css({
			"box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
			"position":"absolute",
			"z-index":2002,
			"top":top+"px",
			"left":left+"px",
			"background-color":"#fff",
			"border-radius":"2%",
			"padding":"15px"
		});
		tipDiv.addClass("tipDefaultsClass");
		div.css({
			"position":"absolute",
			"z-index":2001,
			"top":0+"px",
			"left":0+"px",
			"width":"100%",
			"height":"100%",
			"opacity":0.3,
			"filter":"alpha(opacity=30)",
			"background-color":"#393d49"
		});
		
		
		if(typeof options.closeTime == "number"){
			setTimeout(function(){
				div.remove();
				tipDiv.remove();
			},options.closeTime);
		}
		
		div.attr("id",options.tipId+options.index);
		tipDiv.attr("id",options.tipDivId+options.index);
		body.append(div);
		body.append(tipDiv);
		console.log("open options.index "+options.index);
	}
	open();
	
	var close=function(){
		console.log("close options.index "+options.index);
		$("#"+options.tipId+options.index).remove();
		$("#"+options.tipDivId+options.index).remove();
	}
	
	return {
		close:close,
		open:function(){
			close();
			var result= $.tipLoad(content,options);
			options.index=result.index;
			return result;
		},
		index:options.index
	}
}



$.tip=function(_options){
	var defaults={};
	defaults.closeImgSrc="../base/img/tipClose.png";
	defaults.imgSrc="../base/img/test.png";
	defaults.tipContent="请添加内容";
	defaults.width=535;
	defaults.height=400;
	defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
	defaults.closeTime=2000;//number  一秒钟自动关闭  为空代表自行关闭
	var options=$.extend(defaults,_options);
	var body=$("body");
	if(options.isParent){
		body=parent.$("body");
	}
	var div=$("<div class='defaultTip' id='defaultTop'/>");
	//
	var headDiv=$("<div />");
	var headImg=$("<img src='"+options.closeImgSrc+"' />");
	headDiv.append(headDiv);
	headDiv.append(headImg);
	headImg.css({
		width:"20px",
		height:"20px",
		"margin-top":"15px",
		"margin-right":"15px"
	});
	
	headDiv.css({
		"text-align":"right"
	});
	
	var bodyDiv=$("<div />");
	bodyDiv.css({
		"text-align":"center",
		"margin-top":"60px"
	});
	var bodyImg=$("<img src='"+options.imgSrc+"' />");
	var bodySpan=$("<span/>");
	bodySpan.text(options.tipContent);
	bodyImg.css({
		width:"180px",
		height:"180px",
		"margin-left":"-60px"
	});
	bodySpan.css({
		"margin-left":"15px",
		"font-size":"20px",
		"word-wrap":"break-word",
		"word-break":"break-all",  
		"display":"inline-block",
		"max-width":(options.width/2-60)+"px"
	});
	bodyDiv.append(bodyImg);
	bodyDiv.append(bodySpan);
	var tipDiv=$("<div />");
	tipDiv.append(headDiv);
	tipDiv.append(bodyDiv);
	if(options.footContent != null){
        tipDiv.append(options.footContent);
    }
	
	headImg.click(function(){
		div.remove();
		tipDiv.remove();
		if(typeof options.closeCall == "function"){
			options.closeCall();
		}
	});
	
	var bool=false;
	tipDiv.bind("click",function(){
		bool=true;
	});
	
	div.bind("click",function(){
		if(!bool){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		}else{
			bool=false;
		}
	});
	
	tipDiv.width(options.width);
	tipDiv.height(options.height);
	var left=body.width()/2-options.width/2;
	var top=body.height()/2-options.height/2;
	
	tipDiv.css({
		"box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
		"position":"absolute",
		"z-index":4002,
		"top":top+"px",
		"left":left+"px",
		"background-color":"#fff",
		"border-radius":"2%"
	});
	tipDiv.addClass("tipDefaultsClass");
	tipDiv.attr("id","tipDefaultsID");
	div.css({
		"position":"absolute",
		"z-index":4001,
		"top":0+"px",
		"left":0+"px",
		"width":"100%",
		"height":"100%",
		"opacity":0.3,
		"filter":"alpha(opacity=30)",
		"background-color":"#393d49"
	});
	
	
	if(typeof options.closeTime == "number"){
		setTimeout(function(){
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
			div.remove();
			tipDiv.remove();
		},options.closeTime);
	}
	
	body.append(div);
	body.append(tipDiv);
}



$.tipTwo=function(options){
	var defaults={};
	defaults.closeImgSrc="../../workflow/base/img/tipClose.png";
	defaults.imgSrc="../../workflow/base/img/test.png";
	defaults.tipContent="请添加内容";
	defaults.width=535;
	defaults.height=400;
	defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
	defaults.closeTime=2000;//number  一秒钟自动关闭  为空代表自行关闭
	options=$.extend(defaults,options);
	var body=$("body");
	if(options.isParent){
		body=parent.$("body");
	}
	var div=$("<div class='defaultTip' id='defaultTop'/>");
	//
	var headDiv=$("<div />");
	var headImg=$("<img src='"+options.closeImgSrc+"' />");
	headDiv.append(headDiv);
	headDiv.append(headImg);
	headImg.css({
		width:"20px",
		height:"20px",
		"margin-top":"15px",
		"margin-right":"15px"
	});
	
	headDiv.css({
		"text-align":"right"
	});
	
	var bodyDiv=$("<div />");
	bodyDiv.css({
		"text-align":"center",
		"margin-top":"60px"
	});
	var bodyImg=$("<img src='"+options.imgSrc+"' />");
	var bodySpan=$("<span/>");
	bodySpan.text(options.tipContent);
	bodyImg.css({
		width:"180px",
		height:"180px",
		"margin-left":"-60px"
	});
	bodySpan.css({
		"margin-left":"15px",
		"font-size":"20px",
		"word-wrap":"break-word",
		"word-break":"break-all",  
		"display":"inline-block",
		"max-width":(options.width/2-60)+"px"
	});
	bodyDiv.append(bodyImg);
	bodyDiv.append(bodySpan);
	var tipDiv=$("<div />");
	tipDiv.append(headDiv);
	tipDiv.append(bodyDiv);
	
	headImg.click(function(){
		div.remove();
		tipDiv.remove();
		if(typeof options.closeCall == "function"){
			options.closeCall();
		}
	});
	
	var bool=false;
	tipDiv.bind("click",function(){
		bool=true;
	});
	
	div.bind("click",function(){
		if(!bool){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		}else{
			bool=false;
		}
	});
	
	tipDiv.width(options.width);
	tipDiv.height(options.height);
	var left=body.width()/2-options.width/2;
	var top=body.height()/2-options.height/2;
	
	tipDiv.css({
		"box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
		"position":"absolute",
		"z-index":2002,
		"top":top+"px",
		"left":left+"px",
		"background-color":"#fff",
		"border-radius":"2%"
	});
	tipDiv.addClass("tipDefaultsClass");
	tipDiv.attr("id","tipDefaultsID");
	div.css({
		"position":"absolute",
		"z-index":2001,
		"top":0+"px",
		"left":0+"px",
		"width":"100%",
		"height":"100%",
		"opacity":0.3,
		"filter":"alpha(opacity=30)",
		"background-color":"#393d49"
	});
	
	
	if(typeof options.closeTime == "number"){
		setTimeout(function(){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		},options.closeTime);
	}
	
	body.append(div);
	body.append(tipDiv);
}

/**
 * 温馨提示
 */
$.tipHint=function(options){
    var defaults={};
    defaults.closeImgSrc="../base/img/tipClose.png";
    defaults.imgSrc="../base/img/test.png";
    defaults.tipContent="请添加内容";
    defaults.width=535;
    defaults.height=400;
    defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
    defaults.closeTime=2000;//number  一秒钟自动关闭  为空代表自行关闭
    options=$.extend(defaults,options);
    var body=$("body");
    if(options.isParent){
        body=parent.$("body");
    }
    var div=$("<div class='defaultTip'/>");
    var headDiv=$("<div class=\"new_title\">\n" +
        "        <span >温馨提示</span>\n" +
        "        <div class=\"close-btn bg-white\">\n" +
        "        <i class=\"iconfont icon-close\"></i>\n" +
        "        </div>\n" +
        "        </div>");

    var tipDiv=$("<div />");
    tipDiv.append(headDiv);
    tipDiv.append(options.bodyDiv);
    headDiv.find("i").click(function(){
        div.remove();
        tipDiv.remove();
        if(typeof options.closeCall == "function"){
            options.closeCall();
        }
    });

    var bool=false;
    tipDiv.bind("click",function(){
        bool=true;
    });

    div.bind("click",function(){
        if(!bool){
            div.remove();
            tipDiv.remove();
            if(typeof options.closeCall == "function"){
                options.closeCall();
            }
        }else{
            bool=false;
        }
    });

    tipDiv.width(options.width);
    tipDiv.height(options.height);
    var left=body.width()/2-options.width/2;
    var top=body.height()/2-options.height/2;

    tipDiv.css({
        "box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
        "position":"absolute",
        "z-index":2002,
        "top":top+"px",
        "left":left+"px",
        "background-color":"#fff",
        "border-radius":"2%"
    });
    tipDiv.addClass("tipDefaultsClass");
    div.css({
        "position":"absolute",
        "z-index":2001,
        "top":0+"px",
        "left":0+"px",
        "width":"100%",
        "height":"100%",
        "opacity":0.3,
        "filter":"alpha(opacity=30)",
        "background-color":"#393d49"
    });


    if(typeof options.closeTime == "number"){
        setTimeout(function(){
            div.remove();
            tipDiv.remove();
            if(typeof options.closeCall == "function"){
                options.closeCall();
            }
        },options.closeTime);
    }

    body.append(div);
    body.append(tipDiv);
}

/**
 * 自定义tip
 */
$.tipCustom=function(options){
	var defaults={};
	defaults.closeImgSrc="../base/img/tipClose.png";
	defaults.imgSrc="../base/img/test.png";
	defaults.tipContent="请添加内容";
	defaults.width=535;
	defaults.height=400;
	defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
	defaults.closeTime=2000;//number  一秒钟自动关闭  为空代表自行关闭
	options=$.extend(defaults,options);
	var body=$("body");
	if(options.isParent){
		body=parent.$("body");
	}
	var div=$("<div class='defaultTip'/>");
	//
	var headDiv=$("<div />");
	var headImg=$("<img src='"+options.closeImgSrc+"' />");


	headDiv.append(headDiv);
	headDiv.append(headImg);
	headImg.css({
		width:"20px",
		height:"20px",
		"margin-top":"15px",
		"margin-right":"15px"
	});
	
	headDiv.css({
		"text-align":"right"
	});
	
	/*var bodyDiv=$("<div />");
	bodyDiv.css({
		"text-align":"center",
		"margin-top":"60px"
	});
	var bodyImg=$("<img src='"+options.imgSrc+"' />");
	var bodySpan=$("<span/>");
	bodySpan.text(options.tipContent);
	bodyImg.css({
		width:"180px",
		height:"180px",
		"margin-left":"-60px"
	});
	bodySpan.css({
		"margin-left":"15px",
		"font-size":"20px"
	});
	bodyDiv.append(bodyImg);
	bodyDiv.append(bodySpan);*/
	
	var tipDiv=$("<div />");
	tipDiv.append(headDiv);
	tipDiv.append(options.bodyDiv);
	
	headImg.click(function(){
		div.remove();
		tipDiv.remove();
		if(typeof options.closeCall == "function"){
			options.closeCall();
		}
	});
	
	var bool=false;
	tipDiv.bind("click",function(){
		bool=true;
	});
	
	div.bind("click",function(){
		if(!bool){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		}else{
			bool=false;
		}
	});
	
	tipDiv.width(options.width);
	tipDiv.height(options.height);
	var left=body.width()/2-options.width/2;
	var top=body.height()/2-options.height/2;
	
	tipDiv.css({
		"box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
		"position":"absolute",
		"z-index":2002,
		"top":top+"px",
		"left":left+"px",
		"background-color":"#fff",
		"border-radius":"2%"
	});
	tipDiv.addClass("tipDefaultsClass");
	div.css({
		"position":"absolute",
		"z-index":2001,
		"top":0+"px",
		"left":0+"px",
		"width":"100%",
		"height":"100%",
		"opacity":0.3,
		"filter":"alpha(opacity=30)",
		"background-color":"#393d49"
	});
	
	
	if(typeof options.closeTime == "number"){
		setTimeout(function(){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		},options.closeTime);
	}
	
	body.append(div);
	body.append(tipDiv);
}


$.tipEnd=function(options){
	var defaults={};
	defaults.closeImgSrc="../base/img/tipClose.png";
	defaults.imgSrc="../base/img/test.png";
	defaults.tipContent="请添加内容";
	defaults.width=535;
	defaults.height=400;
	defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
//	defaults.closeTime=2000;//number  一秒钟自动关闭  为空代表自行关闭
	options=$.extend(defaults,options);
	var body=$("body");
	if(options.isParent){
		body=parent.$("body");
	}
	var div=$("<div class='defaultTip'/>");
	//
	var headDiv=$("<div />");
	var headImg=$("<img src='"+options.closeImgSrc+"' />");
	headDiv.append(headDiv);
	headDiv.append(headImg);
	headImg.css({
		width:"20px",
		height:"20px",
		"margin-top":"15px",
		"margin-right":"15px"
	});
	
	headDiv.css({
		"text-align":"right"
	});
	
	var bodyDiv=$("<div />");
	bodyDiv.css({
		"text-align":"center",
		"margin-top":"60px"
	});
	var bodyImg=$("<img src='"+options.imgSrc+"' />");
	var bodySpan=$("<span/>");
	bodySpan.text(options.tipContent);
	bodyImg.css({
		width:"180px",
		height:"180px",
		"margin-left":"30px"
	});
	bodySpan.css({
		"margin-left":"10px",
		"font-size":"15px",
	     width:"150px"
	});
	bodyDiv.append(bodyImg);
//	bodyDiv.append("<br/>");
	bodyDiv.append(bodySpan);

//	bodyDiv.append();
	
	var tipDiv=$("<div />");
	tipDiv.append(headDiv);
	tipDiv.append(bodyDiv);
	tipDiv.append(defaults.okHtml);
	
	headImg.click(function(){
		div.remove();
		tipDiv.remove();
		if(typeof options.closeCall == "function"){
			options.closeCall();
		}
	});
	
	var bool=false;
	tipDiv.bind("click",function(){
		bool=true;
	});
	
	div.bind("click",function(){
		if(!bool){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		}else{
			bool=false;
		}
	});
	
	tipDiv.width(options.width);
	tipDiv.height(options.height);
	var left=body.width()/2-options.width/2;
	var top=body.height()/2-options.height/2;
	
	tipDiv.css({
		"box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
		"position":"absolute",
		"z-index":2002,
		"top":top+"px",
		"left":left+"px",
		"background-color":"#fff",
		"border-radius":"2%"
	});
	tipDiv.addClass("tipDefaultsClass");
	div.css({
		"position":"absolute",
		"z-index":2001,
		"top":0+"px",
		"left":0+"px",
		"width":"100%",
		"height":"100%",
		"opacity":0.3,
		"filter":"alpha(opacity=30)",
		"background-color":"#393d49"
	});

	body.append(div);
	body.append(tipDiv);
}



$.tipSignEnd=function(options){
	var defaults={};
	defaults.closeImgSrc="../base/img/tipClose.png";
	defaults.imgSrc="../base/img/test.png";
	defaults.tipContent="请添加内容";
	defaults.width=600;
	defaults.height=550;
	defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
//	defaults.closeTime=2000;//number  一秒钟自动关闭  为空代表自行关闭
	options=$.extend(defaults,options);
	var body=$("body");
	if(options.isParent){
		body=parent.$("body");
	}
	var div=$("<div class='defaultTip'/>");
	//
	var headDiv=$("<div />");
	var headImg=$("<img src='"+options.closeImgSrc+"' />");
	headDiv.append(headDiv);
	headDiv.append(headImg);
	headImg.css({
		width:"20px",
		height:"20px",
		"margin-top":"15px",
		"margin-right":"15px"
	});
	
	headDiv.css({
		"text-align":"right"
	});
	
	var bodyDiv=$("<div />");
	bodyDiv.css({
		"margin-top":"30px"
	});

	bodyDiv.append(defaults.okHtml);
	
	var tipDiv=$("<div />");
	tipDiv.append(headDiv);
	tipDiv.append(bodyDiv);
	
	headImg.click(function(){
		div.remove();
		tipDiv.remove();
		if(typeof options.closeCall == "function"){
			options.closeCall();
		}
	});
	
	var bool=false;
	tipDiv.bind("click",function(){
		bool=true;
	});
	
	div.bind("click",function(){
		if(!bool){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		}else{
			bool=false;
		}
	});
	
	tipDiv.width(options.width);
	tipDiv.height(options.height);
	var left=body.width()/2-options.width/2;
	var top=body.height()/2-options.height/2;
	
	tipDiv.css({
		"box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
		"position":"absolute",
		"z-index":2002,
		"top":top+"px",
		"left":left+"px",
		"background-color":"#fff",
		"border-radius":"2%"
	});
	tipDiv.addClass("tipDefaultsClass");
	div.css({
		"position":"absolute",
		"z-index":2001,
		"top":0+"px",
		"left":0+"px",
		"width":"100%",
		"height":"100%",
		"opacity":0.3,
		"filter":"alpha(opacity=30)",
		"background-color":"#393d49"
	});

	body.append(div);
	body.append(tipDiv);
}



$.tipReminderSign=function(options){
	var defaults={};
	defaults.tipContent="请添加内容";
	defaults.width=500;
	defaults.height=400;
	defaults.isParent=false;//默认false 表示当前页面打开  true 表示父页面打开
//	defaults.closeTime=2000;//number  一秒钟自动关闭  为空代表自行关闭
	options=$.extend(defaults,options);
	var body=$("body");
	if(options.isParent){
		body=parent.$("body");
	}
	var div=$("<div class='defaultTip'/>");
	//
	var headDiv=$("<div />");
	var headImg=$("<img src='"+options.closeImgSrc+"' />");
	headDiv.append(headDiv);
	headDiv.append(headImg);
	headImg.css({
		width:"20px",
		height:"20px",
		"margin-top":"15px",
		"margin-right":"15px"
	});
	
	headDiv.css({
		"text-align":"right"
	});
	
	var bodyDiv=$("<div />");
	bodyDiv.css({
		"text-align":"center",
		"margin-top":"40px"
	});
	
	
	var bodyImg=$("<img src='"+options.imgSrc+"' /><br/>");
	var bodySpan=$("<span/>");
	bodySpan.text(options.tipContent);
	bodyImg.css({
		width:"180px",
		height:"180px",
		"margin-left":"-10px"
	});
	bodySpan.css({
		"margin-left":"-20px",
		"font-size":"20px"
	});
	
	
	bodyDiv.append(bodyImg);
	bodyDiv.append(bodySpan);
	
	bodyDiv.append(options.htmls);

	var tipDiv=$("<div />");
	tipDiv.append(headDiv);
	tipDiv.append(bodyDiv);
	

	
	headImg.click(function(){
		div.remove();
		tipDiv.remove();
		if(typeof options.closeCall == "function"){
			options.closeCall();
		}
	});
	
	var bool=false;
	tipDiv.bind("click",function(){
		bool=true;
	});
	
	div.bind("click",function(){
		if(!bool){
			div.remove();
			tipDiv.remove();
			if(typeof options.closeCall == "function"){
				options.closeCall();
			}
		}else{
			bool=false;
		}
	});
	
	tipDiv.width(options.width);
	tipDiv.height(options.height);
	var left=body.width()/2-options.width/2;
	var top=body.height()/2-options.height/2;
	
	tipDiv.css({
		"box-shadow":"1px 1px 50px rgba(0, 0, 0, .3)",
		"position":"absolute",
		"z-index":100,
		"top":top+"px",
		"left":left+"px",
		"background-color":"#fff",
		"border-radius":"2%"
	});
	tipDiv.addClass("tipDefaultsClass");
	div.css({
		"position":"absolute",
		"z-index":99,
		"top":0+"px",
		"left":0+"px",
		"width":"100%",
		"height":"100%",
//		"opacity":0.1,
		"filter":"alpha(opacity=30)",
		"background-color":"#fff"
	});

	body.append(div);
	body.append(tipDiv);

}





$.tipSignEndRemove = function(){
	$("div.defaultTip").remove();
	$("div.tipDefaultsClass").remove();
}


/*
 * @title 关闭弹出层
 */
var closeClidLayer = function(closeTime) {
	layer.closeAll();
	if(closeTime == null || typeof closeTime != 'number'){
		closeTime=1500;
	}
	setTimeout(function(){
		var sreach=parent.$("#appMain").contents().find("#sreach");
		if(sreach != null){
			sreach.attr("isCurrentPage","true");
			sreach.trigger("click");
		}
		
		$.colosLayerIfram();
		//parent.window.location.reload();
	},closeTime);
}
/**
 * 先关闭 layer
 * 再关闭 tip
 */
var closeClidLayerAndTip=function(){
	layer.closeAll();
	setTimeout(function(){
		$(".tipDefaultsClass,.defaultTip").remove();
	},1500);
}

var closeClidLayerAndTipEnd=function(){
	layer.closeAll();
	$(".tipDefaultsClass,.defaultTip").remove();
}



$.idsLayerMsg=function(content,options){
	var defaults={};
	options=$.extend(defaults,options);
	if(options.positionEle != null && $(options.positionEle).length != 0){
		var ele=$(options.positionEle);
		var width=ele.width();
		var height=ele.height();
		var offset=ele.offset();
		
		
		
		var hintWidth,hintHeight;
		
		var fontWidth=8;
		var fontHeight=23;
		var row=37;
		
		var topHeight=50;
		var leftWidth=24;
		
		if(typeof(content) !== 'undefined' &&content.length/row == 0){
			hintWidth=content.length*8+leftWidth;
			hintHeight=topHeight+fontHeight;
		}else{
			hintWidth=row*8+leftWidth;
			if(typeof(content) !== 'undefined'){
				hintHeight=topHeight+fontHeight*(content.length/row + 1);
			}else{
				hintHeight=topHeight+fontHeight*1;
			}
		}
		
		offset.top=offset.top+height/2-hintHeight/2;
		offset.left=offset.left+width/2-hintWidth/2;
		
		options.offset=[offset.top+"px",offset.left+"px"];
	}
	layer.msg(content,options);
}



$.idsLoadMsg=function(options){
	
	var defaults={};
	options=$.extend(defaults,options);
	if(options.positionEle != null && $(options.positionEle).length != 0){
		var ele=$(options.positionEle);
		var width=ele.width();
		var height=ele.height();
		var offset=ele.offset();
		
		
		
		var hintWidth,hintHeight;
		
		var fontWidth=8;
		var fontHeight=23;
		var row=37;
		
		var topHeight=50;
		var leftWidth=24;
		
		if(1/row == 0){
			hintWidth=1*5+leftWidth;
			hintHeight=topHeight+fontHeight;
		}else{
			hintWidth=row*5+leftWidth;
			hintHeight=topHeight+fontHeight*(1/row + 1);
		}
		
		offset.top=offset.top+height/2-hintHeight/2;
		offset.left=offset.left+width/2-hintWidth/2+100;
		
		options.offset=[offset.top+"px",offset.left+"px"];
	}
	options.shadeClose=false;//有遮罩
	options.shade=[0.3,'#393d49'];//遮罩的透明度
	options.time=200*1000;
	layer.load(2,options); 
	
	return {
		close:function(){
			return layer.closeAll('loading');
		}
	}
}