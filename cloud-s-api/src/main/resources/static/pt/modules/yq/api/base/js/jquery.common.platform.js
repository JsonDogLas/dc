/**
 * 公共
 * ajaxPost请求
 * @param url
 * @param data
 * @param successCall
 * @param errorCall
 */
$.ajaxCall=function(url,data,successCall,errorCall){
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        success: function(json){
            if(typeof successCall == "function"){
                successCall(json);
            }
        },
        error:function(){
            if(typeof errorCall == "function"){
                errorCall();
            }
        }
    });
}
/**
 * 公共 依赖 $.ajaxCall
 * 与后台交互
 * @param options
 */
$.webPostInteractive=function(options){
    //'use strict';严格模式下
    var defaults={};
    defaults.urlPrefix="";//url连接前缀
    defaults.managerOutFunctionArray=[];//对外接口
    options=$.extend(defaults,options);
    var ajax=function(methodName,data,successCall,errorCall){
        $.ajaxCall(options.urlPrefix+methodName,data,successCall,errorCall);
    }
    var objectToName=function(object){
        for(var name in object){
            return name;
        }
    }
    /**
     * 对外接口创建
     * @type {{}}
     */
    var service={};
    for(var item in options.managerOutFunctionArray){
        var itemValue=options.managerOutFunctionArray[item];
        service[itemValue]=function(methodName,args){
            ajax(methodName,args.data,args.successCall,args.errorCall);
        }
    }
    /**
     * 与后台交互
     * 对外函数
     * @param methodName
     * @param _options
     * @returns {*}
     */
    $.webPostInteractiveOutFunction=function(methodName,_options){
        var method=service[methodName];
        if(typeof method == "function"){
            return method(methodName,_options);
        }else{
            throw new Error("No method name de function");
        }
    }
}
/**
 * 公共 dom节点设置值
 * @param options
 */
$.domValue=function(id,value){
    $dom=$("#"+id);
    var type=$dom.attr("type");
    var tagName=$dom[0].tagName;
    var tagNameCall={
        input:{
            text:function(value){
                $dom.val(value);
            },
            radio:function(value){
                $dom.prop("checked","checked");
            },
            hidden:function (value) {
                $dom.val(value);
            }
        },
        select:function(value){
            $dom.val(value);
        },
        textarea:function(value){
            $dom.val(value);
        }
    }
    if(typeof type == "string" && type != null && type != ""){
        tagNameCall[tagName.toLowerCase()][type](value);
    }else{
        tagNameCall[tagName.toLowerCase()](value);
    }
}
/**
 * 项目下公共
 * 应用在 有 layer的环境下
 * 请求错误处理
 * @param data
 * @param call
 */
$.reqeustResultEreer=function(data,call){
    if(data == null){
        //TODO 国际化
        layui.use('layer', function(){
            var layer = layui.layer;
            layer.msg("请求超时或中断");
        });
        return;
    }
    if(data.result){
        if(typeof data.message == "string" && data.message != ""){
            layui.use('layer', function(){
                var layer = layui.layer;
                layer.msg(data.message);
            });
            setTimeout(function(){
                call(data.data);
            },1000);
            return;
        }else{
            call(data.data);
        }
    }else{
        layui.use('layer', function(){
            var layer = layui.layer;
            layer.msg(data.message);
        });
    }
}
/**
 * 公共  计时器
 * @param endCall 完成后回调
 * @param itemEndCall 每次完成后回调
 * @param count 总共次数
 * @param time 间隔时间
 */
$.countTime=function(endCall,itemEndCall,count,time){
    if(count == null){
        count=1;
    }
    var callCount=0;
    var timer=null;
    var call=function(){
        callCount++;
        timer=setTimeout(function(){
            itemEndCall(callCount);
            if(callCount == count){
                endCall();
            }else{
                call();
            }
        },time);
    }
    //第一次调用
    itemEndCall(callCount);
    call();
    return {
        stop:function(){
            clearTimeout(timer);
        }
    }
}
/**
 * 公共 局部加载控件
 * @param options
 * @returns {*}
 */

$.fn.load=function(options){
    var defaults={};
    defaults.imgSrc="";//加载的图片
    defaults.textContent="";//加载时的内容
    var $dom=$(this);
    if(typeof options == "string"){
        var methodName=options;
        var service={};
        options=$dom.data("data");
        var imgCreate=function(left,top,width,height){
            var $div=$("<div class='imgLoad'><img src='"+options.imgSrc+"'/></div>");
            $div.css({
                "position":"absolute",
                "background-color":"#ffffff",
                "z_index":100
            });
            $div.offset({
                top:top,
                left:left
            });
            $div.width(width);
            $div.height(height);
            return $div;
        }
        /**
         * 打开加载层
         */
        service.open=function(){
            var offset=$dom.offset();
            if(options.imgSrc != "" && options.imgSrc != null){
                var img=imgCreate(offset.left,offset.top,$dom.width(),$dom.height());
                $dom.append(img);
            }else if(options.textContent != "" && options.textContent != null){
                options.domText=$dom.text();
                $dom.text(options.textContent);
                $dom.attr("disabled","disabled");
            }

        }
        /**
         * 关闭加载层
         */
        service.close=function(){
            if(options.imgSrc != "" && options.imgSrc != null){
                $dom.find(".imgLoad").remove();
            }else if(options.textContent != "" && options.textContent != null) {
                $dom.text(options.domText);
                $dom.removeAttr("disabled");
            }
        }
        return service[methodName]();
    }
    options=$.extend(defaults,options);
    $dom.data("data",options);
    $dom.load("open");
}

$.fn.error=function(options){
    if(typeof options == "string"){
        var test=options;
        options={};
        options.content=test;
    }
    var defaults={};
    defaults.content="";//提示内容
    defaults.time=1000;//关闭时间
    options=$.extend(defaults,options);
    var $dom=$(this);
    $dom.text(options.content);
    setTimeout(function(){
        $dom.text("");
    },options.time);
}