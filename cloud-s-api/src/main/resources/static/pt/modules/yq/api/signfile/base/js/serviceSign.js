var browser = $.getBrowserVersion();
var IEVersion = browser.version;
var diag = {};
$(function(){
    var resultData=$.parseJSON(decodeURIComponent($("#result").text()));
    //处理模板字符串
    $("script[type='html']").each(function(){
        var text=$(this).text();
        text=text.replace("/* <![CDATA[ */","");
        text=text.replace("/* ]]> */","");
        $(this).text(text);
    });


    $.startSign({loadSignUrl:"/signPage/loadSignData"
        ,loadFileImageUrl:"/signPage/loadFileImage",
        createStampUrl:"/signPage/handWriteScriptSign/stampRequest",
        handWriteSignUrl:"/signPage/handWriteScriptSign/to_hand",
        addSignSrc:intl.addSignSrc,
        reduceSignSrc:intl.reduceSignSrc,
        loadImagePath:intl.loadImagePath,
        deleteSignSrc:intl.deleteSignSrc,
        blueImgSrc:intl.blueImgSrc,
        errorImgSrc:intl.errorImgSrc,
        permissionList:resultData.result ? resultData.data.permissionList : null,
        againTempleteCall:function(){
            if(intl.CERT_APPLY_STAMP){
                var service=$.startSignFunction("service");
                var responseData = service.responseData();
                if(!responseData.applyStampFlag && lmParam.keyId != ""){
                    laytpl($("#goToConfigModel").text()).render({}, function(html) {
                        $.tipCustom({
                            closeImgSrc:intl.closeImgSrc,
                            bodyDiv:html,
                            closeTime:2000*30
                        });
                    });
                }
                $("body").on("click","#goToConfig",function(){
                    parent.$.postMessage='var $a=$("a[action$=\'/user/manage/certificate/to_list\']");' +
                        '$a.trigger("click");' +
                        '$("li.layui-nav-item").removeClass("layui-this");' +
                        '$a.parent().parent().parent().addClass("layui-this")';
                    $.colosLayerIfram();
                });
            }
        }});

    //打开按钮
    var openButton=function(){
        closeButton.disabledButton.removeAttr("disabled");
        if(closeButton.button != null && closeButton.button.length > 0){
            closeButton.button.attr("disabled","disabled");
            closeButton.button=null;
        }
    }
    //打开按钮
    var closeButton=function(){
        var service=$.startSignFunction("service");
        closeButton.fileId=service.responseData().fileId;
        closeButton.filePath=service.responseData().filePath;
        closeButton.virtualPath=service.responseData().virtualPath;
        closeButton.button=$("#clickSign button[disabled]");
        closeButton.disabledButton=$("#clickSign button");
        closeButton.disabledButton.attr("disabled","disabled");
    }
    /**
     * 签名
     */
    $("body").on("click","#numberSign,#ordinarySign,#publicCerSign",function(){
        if($(this).attr("id") == "numberSign"){
            if(lmParam.keyId == null || lmParam.keyId == "" || lmParam.cerBase64Str == null || lmParam.cerBase64Str == ""){
                layer.msg(intl.pleaceAddKey);
                return;
            }
        }
        var thisele = $(this);
        $.isKeyUse(lmParam.cerBase64Str,function(){
            //检测有没有个人签名或企业签名
            var service=$.startSignFunction("service");
            var stampList=service.responseData().stampList;
            var imageListLoad_zoomRate=$("#pfdShowImgTest").data("imageListLoad").zoomRate();
            var stampBool=false;
            for(var item in stampList){
                var stamp=stampList[item];
                if(stamp.stampType == '0' || stamp.stampType == '1' || stamp.stampType == '3' || stamp.stampType == '4' || stamp.stampType == '5' || stamp.stampType == '2'){
                    stampBool=true;break;
                }
            }
            if(!stampBool){
                layer.msg(intl.WORKFLOW_UPFILETIP_PERSON);
                setTimeout(function(){
                    parent.parent.$("#userInfoSet").trigger("click");
                    $.colosLayerIfram();
                },2000);
                return;
            }

            //Adobe阅读器打开文件时的展示效果
            var OPEN_YZ_TIP_FRAME_FLAG=service.responseData().OPEN_YZ_TIP_FRAME_FLAG;
            if($(this).attr("value") == "1" && OPEN_YZ_TIP_FRAME_FLAG == "0"){
                $(this).find("img").trigger("click");
                return;
            }

            // 1.取出签章数据
            var data = service.getSignArrays();
            var bool = data.bool;
            var signatureArrays = data.signatureArrays;
            var noStampBool=true;
            if(signatureArrays != null && signatureArrays.length > 0){
                for(var index in signatureArrays){
                    if(signatureArrays[index] != null){
                        noStampBool=false;
                    }
                }
            }

            $.isNoStampSign(function(){
                if(noStampBool){
                    var stampList=service.responseData().stampList;
                    var stamp;
                    for(var stampItem in stampList){
                        stamp=stampList[stampItem];
                        if(stamp.stampType == "6"){
                            break;
                        }
                    }
                    var sign={};
                    sign.page=1;
                    sign.signType=stamp.id;
                    sign.zoomRate=1;//签章缩放倍率
                    sign.pdf_x=1;//PDF上的 X 坐标位置
                    sign.pdf_y=1;//PDF上的 Y 坐标位置
                    sign.signEleId="111231";//签章ID
                    sign.z_index=1;
                    signatureArrays.push(sign);
                    bool = true;
                }

                var signatureArraysTest=[],qifeng="",isQifengBool=false;
                if(signatureArrays != null && signatureArrays.length >= 1){
                    //WORKFLOW_WATER_QIFENG_NO_NUMBER_SIGN=水印和骑缝印章不能进行数字签名
                    var stampList=service.responseData().stampList;
                    var stampWater;
                    for(var item in stampList){
                        var stamp=stampList[item];
                        if(stamp.stampType == '3'){
                            stampWater=stampWater+","+stamp.id;
                        }else if(stamp.stampType == '4'){
                            qifeng=qifeng+","+stamp.id;
                        }
                    }

                    for(var item in signatureArrays){
                        var stampTest=signatureArrays[item];
                        if( stampTest!= null){
                            if($(this).attr("value") == "1" && (typeof(stampWater) !== 'undefined' && stampWater.indexOf(stampTest.signType) > -1)){
                                layer.msg(intl.WORKFLOW_WATER_QIFENG_NO_NUMBER_SIGN);
                                return;
                            }
                            if($(this).attr("value") == "2" && (typeof(stampWater) !== 'undefined' && stampWater.indexOf(stampTest.signType) > -1)){
                                layer.msg(intl.WORKFLOW_WATER_QIFENG_NO_PUBLIC_CER_SIGN);
                                return;
                            }
                            var signType=stampTest.signType;
                            for(var item in stampList){
                                if(signType == stampList[item].id){
                                    stampTest.imageWidth=stampList[item].imgWidth;
                                    stampTest.imageHeight=stampList[item].imgHeight;
                                    stampTest.imagePath=stampList[item].filePath;
                                    stampTest.stampType=stampList[item].stampType;
                                }
                            }

                            if(!isQifengBool && qifeng.indexOf(stampTest.signType) > -1){
                                isQifengBool=true;
                            }

                            signatureArraysTest.push(stampTest);
                        }
                    }

                    signatureArrays=signatureArraysTest;
                }
                var signValue = $(this).find(".signValue");
                //WORKFLOW_SIGN_PLEANCE_WAIT=正在签名，请稍等...
                var idsContentLoad=$.tipLoad(intl.WORKFLOW_SIGN_PLEANCE_WAIT,{
                    imgSrc:intl.timImgSrc
                });
                // 2.当前PDF上没有任何签章数据
                if (!bool) {
                    //WORKFLOW_SIGN_NO_SIGN=当前pdf上没有任何签章,请添加签章后操作！
                    idsContentLoad.close();
                    layer.msg(intl.WORKFLOW_SIGN_NO_SIGN);
                    return;
                } else {
                    var loadTimeOut=function(){
                        // 关闭手写签名请求
                        boolTime = false;
                        var qrcodeBoolean=false;//是否进行过 二维码签名
                        // 计算高度
                        var g_stamps = [];
                        $("#navigation").find("img[img_data]").each( function() {
                            var g_stamp = $.parseJSON($(this).attr("img_data"));
                            var floatImgHeight = g_stamp.floatImgHeight;
                            var floatImgWidth = g_stamp.floatImgWidth;
                            var id = g_stamp.id;
                            for ( var item in signatureArrays) {
                                var dataInfo = signatureArrays[item];
                                if (dataInfo == null
                                    || typeof (dataInfo) != "object") {
                                    continue;
                                }
                                var signType = dataInfo.signType;
                                if (signType == id) {
                                    var zoomRate = dataInfo.zoomRate;
                                    dataInfo.floatImgHeight = parseInt(floatImgHeight * zoomRate);
                                    dataInfo.floatImgWidth = parseInt(floatImgWidth * zoomRate);
                                    dataInfo.isZoomRate = false;

                                    if(qifeng != null && qifeng.indexOf(id) > -1){
                                        //骑缝章计算比率
                                        var height=$("#"+dataInfo.signEleId).parent().height();
                                        dataInfo.pdf_y = dataInfo.pdf_y +10;
                                        dataInfo.positionRate=dataInfo.pdf_y/height;
                                    }else{
                                        //计算坐标
                                        dataInfo.pdf_x=dataInfo.pdf_x/imageListLoad_zoomRate;
                                        dataInfo.pdf_y=dataInfo.pdf_y/imageListLoad_zoomRate;
                                    }
                                }

                                //判断是否是二维码
                                if(dataInfo.stampType == "5"){
                                    qrcodeBoolean=true;
                                }
                            }
                        });
                        if(true){
                            var signOpthions={};
                            signOpthions.imageListLoadDataSource=$.startSignFunction("imageListLoadDataSource");
                            signOpthions.lmParam=lmParam;
                            signOpthions.service=service;
                            signOpthions.idsContentLoad=idsContentLoad;
                            signOpthions.intl=intl;
                            signOpthions.openButton=openButton;
                            signOpthions.closeButton=closeButton;
                            signOpthions.stampsByRuleUrl="/signPage/stampsByRule";//骑缝章分割
                            signOpthions.signUrl="/signPage/sign";
                            signOpthions.tipSuccess=function(message,imgSrc){
                                laytpl($("#tipModel").text()).render({}, function(html) {
                                    $.tip({
                                        footContent:html,
                                        tipContent:message,
                                        closeTime:2000*60,
                                        closeImgSrc:intl.closeImgSrc,
                                        imgSrc:intl.imgSrc
                                    });
                                });
                                var service=$.startSignFunction("service");
                                var responseData = service.responseData();
                                responseData.signQrcodeFlag=qrcodeBoolean;
                            }
                            signOpthions.tipImgSrc=intl.tipImgSrc;
                            signOpthions.closeImgSrc=intl.closeImgSrc;
                            var signService=$.signServiceManger(signOpthions);
                            signService.sign(thisele.attr("value"),signatureArrays,isQifengBool);
                            return null;
                        }
                    }
                    setTimeout(loadTimeOut,500);
                }
            },noStampBool);
        });
    });

    /**
     * 下载
     */
    $("body").on("click","#download",function() {
        var service=$.startSignFunction("service");
        var responseData = service.responseData();
        var href= "/signfile/signdownload?id="+responseData.fileId;
        $("#downloada").attr("href",href);
        $("#downloada span").trigger("click");
    });
        /**
     * 保存
     */
    $("body").on("click","#save",function(){
        var service=$.startSignFunction("service");
        var responseData = service.responseData();
        var data={};
        data.signFileId=responseData.signFileId;
        data.newSignFileId=responseData.fileId;
        data.virtualSignFileId=responseData.virtualSignFileId;
        data.signQrcodeFlag=responseData.signQrcodeFlag;
        var idsLoad=$.idsLoad();
        $.ajax({
            type: "POST",
            url: "/signfile/saveSign",
            dataType:"json",
            data: data,
            success: function(json){
                idsLoad.close();
                layer.msg(json.message);
                if(json.result){
                    setTimeout(function(){
                        parent.$.postMessage='var $a=$("a[action$=\'/signfile/to_list\']");' +
                            '$a.trigger("click");' +
                            '$("li.layui-nav-item").removeClass("layui-this");' +
                            '$a.parent().parent().parent().addClass("layui-this")';
                        $.colosLayerIfram();
                    },1000);
                }
            }
        });
    });
    $.startSignFunction("service");
    service.againTemplete(resultData);
    setTimeout(function(){
        var $icon=$('.icon-Color');
        $icon.htmlTip({html:$('#hint').html()});
        $icon.next().eq(0).mousePositionDom()
        $icon.htmlTip("execute",function(){
            $icon.htmlTip("show");
            setTimeout(function(){
                if($icon.next().eq(0).mousePositionDom()){
                    $icon.htmlTip("execute");
                }else{
                    $icon.htmlTip("hide");
                }
            },3000);
        });
        $icon.on('mouseover',function () {
            $icon.htmlTip("execute");
        });
        $.isKeyUse(lmParam.cerBase64Str);
    },500);
    /*$('.icon-Color').on('mouseout',function () {
        $('.hint').hide();
    })*/

    $("body").on("click","#applyKey",function(){
        window.open("http://www.cncaq.com/userIndex?sysType=sz&userType=app");
    });
});

/**
 * 是否无印章签名
 */
$.isNoStampSign=function (args,noStampBool) {
    if(noStampBool){
        $.tipHint({
            bodyDiv:$("#key_no_distinguish_sign").html(),
            closeTime:2000*3000,
            width:330,
            height:280
        });
        $("body").on("click","#confirm",function(){
            if(typeof args == "function"){
                args();
            }
        });

        $("body").on("click","#cancel",function(){
            $(".icon-close").trigger("click");
        });
    }else{
        if(typeof args == "function"){
            args();
        }
    }
}

/**
 * 判断key是否能使用
 * @param keyInfo
 * @param args
 */
$.isKeyUse=function(keyInfo,args){
    /*if(true){
        args();
        return;
    }*/
    var hint=function(){
        $.tipHint({
            bodyDiv:$("#key_no_distinguish").html(),
            closeTime:2000*3000,
            width:400,
            height:330
        });
    }
    if(keyInfo == null || keyInfo == ""){
        hint();
        return;
    }
    $.ajax({
        type: "POST",
        url: "/signPage/isKeyUse",
        data: {keyInfo:keyInfo},
        dataType:"json",
        success: function(json){
            if(json.result){
                if(typeof args == "function"){
                    args();
                }
            }else{
                hint();
            }
        }
    });
}

/**
 * 鼠标是否在当前点前节点上
 * @param args
 */
$.fn.mousePositionDom=function(){
    if($.mousePositionDom == null){
        $.mousePositionDom={}
        $(document).mousemove(function(e){
            $.mousePositionDom.x = e.pageX;
            $.mousePositionDom.y = e.pageY;
        });
    }else{
        //x的值相对于文档的左边缘。y的值相对于文档的上边缘//x,y是全局变量;
        // 判断鼠标是否在某DIV中
        if($.mousePositionDom == null){
            return false;
        }
        this.x=$.mousePositionDom.x;
        this.y=$.mousePositionDom.y;
        if(this.y == null || this.x == null){
            return false;
        }
        var div = $(this);//获取你想要的DIV
        var y1 = div.offset().top;
        //div上面两个的点的y值
        var y2 = y1 + div.height();
        //div下面两个点的y值
        var x1 = div.offset().left;
        //div左边两个的点的x值
        var x2 = x1 + div.width();
        //div右边两个点的x的值
        if(!(this.x < x1 || this.x > x2 || this.y < y1 || this.y > y2)) {
            return true;
        }
        return false;
    }


}

/**
 * HTML提示插件
 */
$.fn.htmlTip=function(args,data){
    var $this=$(this);
    if(this.method == null){
        this.method={};
        this.method.init=function(options){
            var $html=$(options.html);
            $html.hide();
            var top=$this.offset().top+$this.height();
            var left=$this.offset().left+$this.width()/2;
            $html.css({
                "position":"absolute",
            });
            $html.offset({top:top,left:left});
            $this.after($html);
        }

        this.method.hide=function(){
            $this.next().eq(0).hide();
        }
        this.method.show=function(){
            $this.next().eq(0).show();
        }
    }

    if(typeof args == "string"){
        var method=this.method[args];
        //当前方法不存在 且 传入的 data 是一个方法，就默认注册这个方法后 执行
        if(method == null && typeof data == "function"){
            this.method[args]=data;
            method=data;
        }
        if(typeof method == "function"){
            method(args,data);
        }
    }else if(typeof args == "object"){
        var defaults={};
        var options=$.extend(defaults,args);
        this.method.init(options);
    }
}


$.isHandWriteStamp=function(agrs){
    var service=$.startSignFunction("service");
    var handWriteStamp=service.responseData().handWriteStamp;
    if(handWriteStamp == null){
        return false;
    }
    for(var index in handWriteStamp){
        if(agrs == handWriteStamp[index]){ return true;}
    }
    return false;
}