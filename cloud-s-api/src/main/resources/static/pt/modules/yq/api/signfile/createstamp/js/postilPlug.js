/**
 * 根据时间获取随机数
 */
$.random=function(){
    var random=Math.random()+"";
    return new Date().getTime()+random.substring(3,random.length-1);
}

/**
 * 批注块的选择
 * @param options
 */
$.fn.blockSelection=function(options) {
    var defaults = {};
    defaults.canmove = false;//没有按下鼠标
    defaults.z_indexCount = 2;
    defaults.startWidth=5;
    defaults.oneRegionHeight = 20;
    defaults.oneRegionWidth = 400;
    defaults.startId=$.random();
    defaults.isShift=false;
    defaults.blockSelectionFinish=null;
    defaults.isBlockSelection=true;
    defaults.oneRegionHeightChange=function(oneRegionHeight){
    	
    }
    options = $.extend(defaults, options);
    if(typeof options.oneRegionHeightChange == "function"){
    	options.oneRegionHeightChange(options.oneRegionHeight);
    }
    
    var thisDom = $(this);
    /**
     * 参数验证
     * @param options
     * @param thisDom
     */
    var paremValid = function (options, thisDom) {
        var width = thisDom.width();
        var height = thisDom.height();
        if (options.oneRegionWidth > width) {
            throw new Error("oneRegionWidth长度大于操作区域的长度！");
        } else if (options.oneRegionHeight > height) {
            throw new Error("oneRegionHeight长度大于操作区域的长度！");
        }
    }
    paremValid(options, thisDom);
    $(this).bind("mousedown", function (event) {
    	
    	var timeRun=function(thisEle){
    		thisEle.unbind("mouseup");
	        if($("#" + defaults.startId).length > 0){
	            $("#" + defaults.startId).remove();
	        }
	        defaults.canmove = true;
	        defaults.pageX = event.pageX;
	        defaults.pageY = event.pageY;
	        defaults.random = $.random();
	        defaults.z_indexCount++;
	        $(document).bind("mousemove", function (event) {
	            if (defaults.canmove) {
	                var pageX = event.pageX;
	                var pageY = event.pageY;
	                var operateDivNumber = parseInt((pageY - defaults.pageY) / options.oneRegionHeight) == 0 ? 1 : parseInt((pageY - defaults.pageY) / options.oneRegionHeight);
	                var operateDiv = $("#" + defaults.random);
	                
	                if (operateDiv == null || operateDiv.length == 0) {
	                    operateDiv = $("<div/>");
	                    operateDiv.attr("id", defaults.random);
	                    operateDiv.addClass("blockParentDiv");
	                    operateDiv.css("z_index", defaults.z_indexCount);
	                    operateDiv.css("width", options.oneRegionWidth + "px");
	                    var thisDomOffset = thisDom.offset();
	                    var thisDomWidth = thisDom.width();
	                    var left = thisDomOffset.left + (thisDomWidth - options.oneRegionWidth) / 2;//始终居中
	                    var top = defaults.pageY-(options.oneRegionHeight / 2);
	                    operateDiv.offset({top: top, left: left});
	                    operateDiv.attr("offsetTop",top);
	                    operateDiv.attr("offsetLeft",left);
	                    thisDom.append(operateDiv);
	                }
	                
	                if(operateDivNumber < 0){
	                    var offsetTop =parseInt(operateDiv.attr("offsetTop"));
	                    var top=offsetTop-(0-operateDivNumber)*options.oneRegionHeight;
	                    operateDiv.offset({top: top});
	                }else if(operateDivNumber == 1){
	                    var left=Number(operateDiv.attr("offsetLeft"));
	                    var offsetTop =parseInt(operateDiv.attr("offsetTop"));
	                    operateDiv.offset({left: left,top:offsetTop});
	                }else{
	            		var left=Number(operateDiv.attr("offsetLeft"));
	            		
	            		var offsetTop =parseInt(operateDiv.attr("offsetTop"));
	                    //var top=offsetTop+operateDivNumber*options.oneRegionHeight;
	                    operateDiv.offset({left: left,top:offsetTop});
	                }
	                
	                if(operateDiv.length >0){
	                	operateDiv.find(".postil").removeClass("postil");
	                	var first=operateDiv.children().first();
	                	var last=first.children().last();
	                	last.addClass("postil");
	                }
	                
	                blockGenerator(thisDom.find("#" + defaults.random), operateDivNumber, options.oneRegionHeight, options.oneRegionWidth, pageX, pageY);
	            }
	            return false;
	        });
	        thisEle.bind("mouseup", function () {
	            defaults.canmove = false;
	            $(document).unbind("mousemove");
	            $(this).unbind("mouseup");
	            //
	            if(typeof options.blockSelectionFinish == "function"){
	            	if($("#"+defaults.random).length > 0){
		            	options.blockSelectionFinish(defaults.random);
	            	}
	            }
	        });
    	}
    	
    	var ele=$(this);
    	var stop=setTimeout(function(){
    		if(defaults.isBlockSelection){
    			timeRun(ele);
    		}
    	},100);
    	$(this).bind("mouseup",function(){
    		clearTimeout(stop);
        });
    });
    /**
     * div判空并赋值
     * @param parentDiv 父节点div
     * @param chlidExpr 子节点索引方式
     * @param defaults 默认值
     * @param operateDivNumber 操作div个数
     */
    var isNotEmptyDiv = function (parentDiv, chlidExpr, defaults, operateDivNumber) {
        var start = parentDiv.find(chlidExpr);
        if (start != null && start.length > 0) {
            return start;
        } else {

            if (chlidExpr.indexOf("startdiv") > 0 && operateDivNumber >= 1) {
                parentDiv.append(defaults);
            }
            if (chlidExpr.indexOf("enddiv") > 0 && operateDivNumber >= 2) {
                parentDiv.append(defaults);
            }
            if (chlidExpr.indexOf("middlediv") > 0 && operateDivNumber > 2) {
                parentDiv.find("div[name='startdiv']").after(defaults);
            }

            if(operateDivNumber < 0){
                if (chlidExpr.indexOf("enddiv") > 0 && operateDivNumber <= -1) {
                    parentDiv.find("div[name='startdiv']").before(defaults);
                }
                if (chlidExpr.indexOf("middlediv") > 0 && operateDivNumber < -1) {
                    parentDiv.find("div[name='startdiv']").before(defaults);
                }
            }

            return defaults;
        }
    }
    /**
     * 块生成器
     * @param operateDiv 操作div
     * @param operateDivNumber 操作IDV数
     * @param oneRegionHeight 一个单位的高度
     * @param oneRegionWidth 一个单位宽度
     * @param pageX  停止点坐标
     * @param pageY  停止点坐标
     */
    var blockGenerator = function (operateDiv, operateDivNumber, oneRegionHeight, oneRegionWidth, pageX, pageY) {
        operateDiv.attr("operateDivNumber",operateDivNumber);
        operateDiv.attr("oneRegionHeight",oneRegionHeight);
        operateDiv.attr("oneRegionWidth",oneRegionWidth);
        var operateDivOffsetLeft = operateDiv.offset().left;
        var operateDivWidth = operateDiv.width();
        var widthLeft = pageX - operateDivOffsetLeft;
        var widthRright = operateDivWidth - widthLeft;
        var moveRrightWidth = pageX - defaults.pageX;
        var moveLeftWidth = defaults.pageX - pageX;
 
        var div = $("<div/>");
        if (operateDiv != null && operateDiv.length > 0) {
            div = operateDiv;
        }

        var startDiv = $("<div style='width:" + oneRegionWidth + "px;height:" + oneRegionHeight + "px;' name='startdiv'>" +
            "<div style='height:" + oneRegionHeight + "px;float:left;' name='startdivleft' left='false'></div>" +
            "<div style='height:" + oneRegionHeight + "px;float:left;' name='startdivright' right='false'></div>" +
            "</div>");
        startDiv = isNotEmptyDiv(div, "div[name='startdiv']", startDiv, operateDivNumber);


        //中间div
        var middleDiv = $("<div  name='middlediv' >" +
            "<div></div>" +
            "</div>");
        middleDiv = isNotEmptyDiv(div, "div[name='middlediv']", middleDiv, operateDivNumber);

        //结束div
        var endDiv = $("<div style='width:" + oneRegionWidth + "px;height:" + oneRegionHeight + "px;' name='enddiv'>" +
            "<div style='height:" + oneRegionHeight + "px;'></div>" +
            "</div>");
        endDiv = isNotEmptyDiv(div, "div[name='enddiv']", endDiv, operateDivNumber);

        var startdivleft = startDiv.find("div[name='startdivleft']");
        var startdivright = startDiv.find("div[name='startdivright']");
        if (operateDivNumber > 0) {//向下移动
            if (operateDivNumber == 1) {
                middleDiv.remove();
                endDiv.remove();
                //向右
                if (pageX > defaults.pageX) {
                    if (widthRright >= 0) {
                        startdivright.css("width", moveRrightWidth + "px");
                    }
                    //第一次
                    if (startdivleft.attr("left") == "false") {
                        startdivleft.css("width", widthLeft + "px");
                    }
                    startdivleft.attr("left", "true");
                    startdivright.attr("right", "false");
                    startdivleft.removeClass("blockStart");
                    startdivright.addClass("blockStart");
                } else {//向左
                    if (widthLeft >= 0) {
                        startdivright.css("width", moveLeftWidth + "px");
                    } else {
                        widthLeft = 0;
                    }
                    startdivleft.css("width", widthLeft + "px");
                    startdivright.addClass("blockStart");
                    startdivleft.removeClass("blockStart");
                }
                startdivleft.attr("leftright", "false");
            }
            if (operateDivNumber > 1) {
                endDiv.find("div").addClass("blockRight");
                endDiv.find("div").css("width", widthLeft + "px");

                //第一次
                if (startdivleft.attr("leftright") == "false") {
                    startdivleft.css("width", startdivleft.width() + "px");
                    var startRrightWidth=oneRegionWidth-startdivleft.width();
                    startdivright.css("width", startRrightWidth + "px");
                }
                startdivleft.attr("leftright", "true");
                startdivleft.removeClass("blockStart");
                startdivright.addClass("blockStart");

                if (operateDivNumber == 2) {
                    middleDiv.remove();
                }
            }
        } else if (operateDivNumber < 0) { //向上移动
            if (operateDivNumber == -1) {
                middleDiv.remove();
                if (widthLeft >= 0) {
                    startdivright.css("width", widthLeft+moveLeftWidth + "px");
                    widthLeft = 0;
                    startdivleft.css("width", widthLeft + "px");
                    startdivright.addClass("blockStart");
                    startdivleft.removeClass("blockStart");
                }
            }
            endDiv.find("div").addClass("blockLeft");
            endDiv.find("div").css("width", widthRright + "px");
            if (operateDivNumber < -1) {
                var countNumber = (0 - operateDivNumber)-1;
                middleDiv.find("div").css("height", oneRegionHeight * countNumber + "px");
                middleDiv.find("div").css("width", oneRegionWidth + "px");
                middleDiv.attr("operateDivNumber",countNumber);
                middleDiv.css("height", oneRegionHeight * countNumber + "px");
                middleDiv.css("width", oneRegionWidth + "px");
                middleDiv.find("div").addClass("block");
            }
        }
        if (operateDivNumber > 2) {
            var count = operateDivNumber - 2;
            middleDiv.find("div").css("height", oneRegionHeight * count + "px");
            middleDiv.find("div").css("width", oneRegionWidth + "px");
            middleDiv.attr("operateDivNumber",operateDivNumber);
            middleDiv.css("height", oneRegionHeight * count + "px");
            middleDiv.css("width", oneRegionWidth + "px");
            middleDiv.find("div").addClass("block");
        }
        return div;
    }
    $(this).bind("mousemove", function (event) {
        if (!defaults.canmove) {
            var pageX = event.pageX;
            var pageY = event.pageY;
            var div = $("#" + defaults.startId);
            var height = defaults.oneRegionHeight;
            if (div.length < 1) {
                div = $("<div />");
                var width = defaults.startWidth;
                div.attr("id", defaults.startId);
                div.css("height", height+"px");
                div.css("width", width+"px");
                div.css("position", "absolute");
                div.css("z_index", defaults.z_indexCount + 1);
                div.addClass("blockStartWidth");
                $(this).append(div);
            }
            pageY = pageY - height / 2;
            div.offset({top:pageY,left:pageX});
        }

    });
    
    /*$(this).on("click",".blockStartWidth",function(){
    	$(this).hide();
    	console.log("blockStartWidth");
    	return true;
    });*/
    $(window).bind("keydown",function(event){
        if (!defaults.canmove) {
            if (event.keyCode == 16){
                console.log("shift被按下了");
                //shift被按下了
                defaults.isShift=true;
                var scrollFunc=function(e){
                    if(defaults.isShift) {
                        e = e || window.event;
                        var count=0;
                        var oneRegionHeight=options.oneRegionHeight;
                        if (e.wheelDelta) {//IE/Opera/Chrome
                            count=e.wheelDelta;
                        } else if (e.detail) {//Firefox
                            count=e.wheelDelta;
                        }
                        if(count>0){
                            options.oneRegionHeight+=2;
                        }else{
                            options.oneRegionHeight-=2;
                        }
                        var div=$("#" + defaults.startId);
                        var offset=div.offset();
                        oneRegionHeight=options.oneRegionHeight-oneRegionHeight;
                        div.css("height", options.oneRegionHeight+"px");
                        if(typeof options.oneRegionHeightChange == "function"){
                        	options.oneRegionHeightChange(options.oneRegionHeight);
                        }
                        div.offset({top:offset.top-oneRegionHeight/2,left:offset.left});
                    }
                    defaults.isShift=false;
                }
                /*注册事件*/
                if(document.addEventListener){
                    $(this)[0].addEventListener('DOMMouseScroll',scrollFunc,false);
                }//W3C
                window.onmousewheel=document.onmousewheel=scrollFunc;//IE/Opera/Chrome
            }
        }
    });
    $(window).bind("keyup",function(event){
        if (!defaults.canmove) {
            if (event.shiftKey==1){
                defaults.isShift=false;
            }
        }
    });
    var service={};
    service.setProgress=function(progresspx){
    	var div=$("#" + defaults.startId);
        var offset=div.offset();
        options.oneRegionHeight=progresspx;
        div.css("height", options.oneRegionHeight+"px");
    }
    service.isBlockSelectionFlase=function(){
    	defaults.isBlockSelection=false;
    }
    service.isBlockSelectionTrue=function(){
    	defaults.isBlockSelection=true;
    }
    return service;
}




/**
 * 带线层
 */
$.lineLayer=function(options){
    var defaults={};
    defaults.originTop=500;
    defaults.originLeft=500;
    defaults.height=300;
    defaults.width=270;
    defaults.top=200;
    defaults.left=700;
    defaults.z_index=2;
    defaults.lineStyle="lineStyle";
    defaults.lineZ_index=1;
    defaults.content=$(".layer");
    var statics={};
    statics.lineFlag="line"+$.random();
    statics.contentFlag="content"+$.random()
    options=$.extend(defaults,options);
    /**
     *画线
     * @param id id
     * @param orignTop 起点
     * @param orignLeft
     * @param left 终点
     * @param top
     * @param lineStyle 线的class属性
     * @param lineZ_index 线层次
     * @returns DOM
     */
    var downLine=function(id,orignTop,orignLeft,left,top,lineStyle,lineZ_index){
        console.log("orignTop "+orignTop+" orignLeft "+orignLeft+" left "+left+" top "+top);
        var data={};
        data.orignTop=orignTop;
        data.orignLeft=orignLeft;
        data.left=left;
        data.top=top;
        data.lineStyle=lineStyle;
        data.lineZ_index=lineZ_index;
        var line=$("#"+id);
        if(line == null || line.length <= 0){
            line=$("<div />");
            line.attr("id",id);
            line.offset({top:orignTop,left:orignLeft});

        }
        var height=Math.abs(top-orignTop);
        var width=Math.abs(left-orignLeft);
        var lineWidth=Math.sqrt(height*height+width*width);
        //COSB＝（A的平方＋C的平方－B的平方）÷（2×A×C)
        var rotate=null;
        if(height>width){
            rotate=180*Math.asin(width/lineWidth)/Math.PI;
        }else{
            rotate=180*Math.asin(height/lineWidth)/Math.PI;
        }
        var x=left-orignLeft;
        var y=orignTop-top;
        //A区域
        if(orignLeft < left){

            //中间 宽直线
            if(y == 0){

            }else
            //上部分 斜线
            if(x >= y && y > 0){
                rotate=360-rotate;
            }else
            //下部分 反斜线
            if(y < 0 && Math.abs(y) >= x){

            }
        }else{//C区域
            //中间 宽直线
            if(y == 0){
                rotate+=180;
            }else
            //上部分 反斜线
            if(Math.abs(x) > y && y > 0){
                rotate+=180;
            }else
            //下部分 斜线
            if(x < y && y < 0){
                rotate=180-rotate;
            }
        }

        //$("body").append(downLine(500,500,left 89 ,514));

        //D区域 以 D点为基础  在Y正半轴上
        if(orignTop > top){
            if(x == 0){
                rotate+=270;
            }else
            if(x>0 && x <= y){
                rotate+=270;
            }else
            if(x<0 && Math.abs(x) <= y){
                rotate=270-rotate;
            }
        }else{//B区域
            if(x == 0){
                rotate+=90;
            }else
            if(x>0 && Math.abs(y) > x){
                rotate=90-rotate;
            }else
            if(x<0 && Math.abs(x)  < Math.abs(y)){
                rotate+=90;
            }
        }
        line.attr("data",JSON.stringify(data));
        line.addClass("rotate");
        rotate="rotate("+rotate+"deg)";
        line.css("transform",rotate);
        line.css("-ms-transform",rotate);
        line.css("-moz-transform",rotate);
        line.css("-webkit-transform",rotate);
        line.css("-o-transform",rotate);
        line.css("position","absolute");
        line.css("width",lineWidth+"px");
        if(typeof lineZ_index == "number"){
            line.css("z_index",lineZ_index);
        }
        return line;
    }
    /**
     *区域坐标运算
     * @param orignTop
     * @param orignLeft
     * @param region  A（-45度 ~ 45度） B C D 四个区域
     * @returns F {top:top,left:left}
     */
    var regionCount=function(orignTop,orignLeft,region){
        var F=null;
        var lineLength=0;
        for(var item in region){
            var test_F=region[item];
            if(typeof test_F == "object"){
                var height=Math.abs(orignTop-test_F.top);
                var width=Math.abs(orignLeft-test_F.left);
                var test_lineLength=Math.sqrt(height*height+width*width);
                if(F == null || (F != null && lineLength > test_lineLength)){
                    lineLength=test_lineLength;
                    F=test_F;
                }
            }
        }
        return F;
    }
    /**
     *生成层
     * @param contentHeight
     * @param contentWidth
     * @param orignTop
     * @param orignLeft
     * @param left 下次移动 offset left
     * @param top  下次移动 offset top
     * @param lineZ_index  下次移动 offset top
     * @returns {*}
     */
    var layerGenerator=function(contentHeight,contentWidth,orignTop,orignLeft,left,top,lineZ_index){
        //$("body").append("<div class='dian' style='background-color:crimson;top:"+orignTop+"px;left:"+orignLeft+"px'/>");
        //$("body").append("<div class='dian' style='background-color:brown;top:"+top+"px;left:"+left+"px'/>");

        var lineId=statics.lineFlag,contentDiv=$("#"+statics.contentFlag);
        if(contentDiv == null || contentDiv.length <= 0){
            contentDiv=$("<div />");
            contentDiv.css("height",contentHeight+"px");
            contentDiv.css("width",contentWidth+"px");
            contentDiv.css("position","absolute");
            var index=options.lineZ_index+2;
            contentDiv.css("z-index",index);
            contentDiv.attr("id",statics.contentFlag);
            contentDiv.append('<img class="layerClose" src="image/remove.png" width="8" height="8" style="margin-left: 269px;margin-top: 8px;position: absolute;background-color:#def9f7;">');
            contentDiv.append($(options.content));
            $("body").append(contentDiv);
            contentDiv.find(".layerClose").bind("click",function(){
            	$("#"+statics.contentFlag).remove();
                $("#"+statics.lineFlag).remove();
                if(typeof options.close == "function"){
                	options.close();
                }
            });
        }
        var height=contentHeight/2;
        var width=contentWidth/2;
        var A={},B={},C={},D={},F={};
        A.left=left;
        A.top=top+height;
        //$("body").append("<div class='dian' style='background-color:blue;top:"+A.top+"px;left:"+A.left+"px'/>");
        B.top=top;
        B.left=left+width;
        //$("body").append("<div class='dian' style='background-color:green;top:"+B.top+"px;left:"+B.left+"px'/>");
        C.top=top+height;
        C.left=left+contentWidth;
        //$("body").append("<div class='dian' style='background-color:yellow;top:"+C.top+"px;left:"+C.left+"px'/>");

        D.top=top+contentHeight;
        D.left=left+width;
        //$("body").append("<div class='dian' style='background-color:yellow;top:"+D.top+"px;left:"+D.left+"px'/>");

        F.A=A;F.B=B;F.C=C;F.D=D;
        F=regionCount(orignTop,orignLeft,F);
        ////$("body").append("<div class='dian1' style='background-color:#ffffff;top:"+F.top+"px;left:"+F.left+"px'/>");
        console.log("top:"+F.top+"left:"+left);
        var line=downLine(lineId,orignTop,orignLeft,F.left,F.top,options.lineStyle,lineZ_index);
        if($("#"+lineId).length<=0){
            $("body").append(line);
        }
        contentDiv.offset({top:top,left:left});
    }
    layerGenerator(options.height,options.width,options.originTop,options.originLeft,options.left,options.top,options.lineZ_index);
    var dragOptions={};
    dragOptions.ismovein=function(dom,top,left){
        layerGenerator(options.height,options.width,options.originTop,options.originLeft,left,top,options.lineZ_index);
        return false;
    }
    $("#"+statics.contentFlag).drag(dragOptions);
    var service={};
    service.close=function(){
        $("#"+statics.contentFlag).remove();
        $("#"+statics.lineFlag).remove();
    }
    service.isClose=function(){
    	if($("#"+statics.contentFlag).length == 0){
    		return true;
    	}
    	return false;
    }
    service.downLine=function(orignTop,orignLeft){
    	var data=$.parseJSON($("#"+statics.lineFlag).attr("data"));
        //var downLine=function(id,orignTop,orignLeft,left,top,lineStyle,lineZ_index){

        downLine(statics.lineFlag,orignTop,orignLeft,data.left,data.top,data.lineStyle,data.lineZ_index);
    }
    service.offset=function(){
    	if($("#"+statics.contentFlag).length > 0){
    		return $("#"+statics.contentFlag).offset();
    	}
    }
    return service;
}

//拖曳进度条
var scale=function (btn,bar,content,progressChange){
    this.btn=document.getElementById(btn);
    this.bar=document.getElementById(bar);
    this.content=document.getElementById(content);
    this.step=this.bar.getElementsByTagName("DIV")[0];
    this.progressChange=progressChange;
    this.init();
    var thiss=this;
    this.setProgress=function(progress,type){
        var max=thiss.bar.offsetWidth-thiss.btn.offsetWidth;
    	if(type !=null && type == "1"){
    		progress=parseInt(progress/max*100);
    		thiss.content.value=progress;
    	}
    	
    	if(progress > 100){
    		progress = 100;
    	}
    	var thisX=max*progress/100;
        thiss.btn.style.left=thisX+'px';
        thiss.step.style.width=Math.max(0,thisX)+'px';
    }
    this.content.addEventListener("input",function(){
        if(!isNaN(this.value)){
            var value=parseInt(this.value);
            if( value > 100){
                thiss.setProgress(100);
            }else if( value < 0){
                value=0;
                thiss.setProgress(0);
            }else{
            	thiss.setProgress(value);
            }
            if(typeof progressChange == "function"){
            	var max=thiss.bar.offsetWidth-thiss.btn.offsetWidth;
            	thiss.progressChange(value,max);
            }
        }else{
            thiss.setProgress(0);
            if(typeof progressChange == "function"){
            	var max=thiss.bar.offsetWidth-thiss.btn.offsetWidth;
            	thiss.progressChange(0,max);
            }
        }
    });
};
scale.prototype={
    init:function (){
        var f=this,g=document,b=window,m=Math;
        f.btn.onmousedown=function (e){
            var x=(e||b.event).clientX;
            var l=this.offsetLeft;
            var max=f.bar.offsetWidth-this.offsetWidth;
            g.onmousemove=function (e){
                var thisX=(e||b.event).clientX;
                var to=m.min(max,m.max(-2,l+(thisX-x)));
                f.btn.style.left=to+'px';
                f.ondrag(m.round(m.max(0,to/max)*100),to);
                b.getSelection ? b.getSelection().removeAllRanges() : g.selection.empty();
            };
            g.onmouseup=new Function('this.onmousemove=null');
        };
    },
    ondrag:function (pos,x){
        this.step.style.width=Math.max(0,x)+'px';
        this.content.value=pos;
        if(typeof this.progressChange == "function"){
        	var max=this.bar.offsetWidth-this.btn.offsetWidth;
        	this.progressChange(pos,max);
        }
    }
}