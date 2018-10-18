/**
 * 启动签名
 * @param arsg
 */
$.startSign=function(arsg){
    var options = {}
    options.PFDNavigationName = {}
    var template = {};
    template.templateId = "PFDNavigationNameModel";
    template.templateRenderingBackCall = function(data) {

    }
    options.PFDNavigationName.template = template;

    options.signatureHead = {}
    var template = {};
    template.templateId = "signatureHeadModel";
    template.templateRenderingBackCall = function(data) {
        var call=function(){
            var width=$("#buttonContent").parent().width();
            var buttonWidth=$("#buttonContent").width();
            var titleNameWidth=$("#titleName").width();
            width=width-buttonWidth-titleNameWidth-48;
            $("#titleContent").width(width);

            var ele=$("#blueDigitalSign");
            ele.blueDigital("init",{
                status:{
                    blue:arsg.blueImgSrc,
                    error:arsg.errorImgSrc
                }
            });
            if($("#titleContent").height() > 50){
                setTimeout(function(){
                    var height=$("#titleContent").height()+10;
                    service.headHeightChange(height);
                    $("#signatureHead").height(height);
                    $("#signatureHeadTest").height(height);
                    ele.blueDigital("postion",height);
                },10);
            }
            ele.width(ele.parent().width());

            if(typeof data.blueDigital == "string" && data.blueDigital == "blue"){
                ele.blueDigital("status",data.blueDigital);
            }else if(typeof data.blueDigital == "string" && data.blueDigital == "error"){
                ele.blueDigital("status",data.blueDigital);
            }
        }
        //当浏览器大小变化时
        $(window).resize(function () {
            call();
        });
        call();
    }
    options.signatureHead.template = template;

    options.navigation = {}
    var handWriteSignCount = 0;
    var template = {};
    template.templateId = "navigationModel";
    template.templateRenderingBackCall = function(data) {
        var g_stamp = data[data.length - 1];
        setTimeout(function(){
            if(g_stamp.stampType == "0"){
                if($("#"+g_stamp.id).length > 0){
                    $("#"+g_stamp.id).trigger("click");
                }else{
                    createStamp(g_stamp)
                }
            }
        },100);
    }
    options.navigation.template = template;

    options.clickSign = {}
    var template = {};
    template.templateId = "clickSignModel";
    template.templateRenderingBackCall = function(data) {
        if(typeof data.blueDigital == "string"){
            $("#blueDigitalSign").blueDigital("show");
            if($("#titleContent").height() > 50){
                setTimeout(function(){
                    var height=$("#titleContent").height()+10;
                    service.headHeightChange(height);
                    $("#signatureHead").height(height);
                    $("#signatureHeadTest").height(height);
                    $("#blueDigitalSign").blueDigital("postion",height);
                },10);
            }
            $("#blueDigitalSign").blueDigital("status",data.blueDigital);
            $("#numberSign").attr("disabled","disabled");
            $("#ordinarySign").attr("disabled","disabled");
            $("#publicCerSign").attr("disabled","disabled");
        }else{
            $("#blueDigitalSign").hide();
        }
    }
    options.clickSign.template = template;


    $.isPermission=function(permission){
        var permissionList=arsg.permissionList;
        if(permissionList !=  null){
            for(var index in permissionList){
                if(permissionList[index]==permission){
                    return true;
                }
            }
        }
        return false;
    }
    if($.isPermission("ROLE_API_OPERATE_WRITE")){
        // 手写签名
        options.handWriteSign = {}
        var template = {};
        template.templateId = "handWriteSignModel";
        template.templateRenderingBackCall = function(data) {
            //-----------------------生成手写体----------------------- 跨域cross=1
            var options={};
            options.width="570px";
            //options.height="404px";
            options.height="600px";
            options.ele="#createStamp";
            options.type=2;
            //WORKFLOW_CREATE_STAMP_INFO=生成手写体
            options.title=intl.WORKFLOW_CREATE_STAMP_INFO;
            options.content=arsg.handWriteSignUrl;
            $.idsLayerOn(options);
            /*$.ajax({
                  type: "GET",
                  url: intl.cebaRemoteTestUrl,
                  dataType: "script",
                  success: function(msg){
                        options.content="../createstamp/createStamp.jsp?handSignKey="+service.responseData().handSignKey+"&cross=1";
                        $.idsLayerOn(options);
                  },
                  error:function(){

                  }
            });*/
        }
        options.handWriteSign.template = template;
    }

    //上传印章
    /*options.uploadStamp={};
    var template = {};
    template.templateId = "uploadStampModel";
    template.templateRenderingBackCall = function(data) {

    }
    options.uploadStamp.template = template;*/

    options.PFDNavigation = {}
    var template = {};
    template.templateId = "PFDNavigationModel";
    template.templateRenderingBackCall = function(data) {
        $("#pfdShowImgTest").data("imageListLoad").scrollChange=function(thisPage){
            $("#PFDNavigationRight").data("imageListLoad").indexToCountPage(parseInt(thisPage),function(){

            });
        }
        /*$("#PFDNavigationRight").data("imageListLoad").scrollChange=function(thisPage){
            $("#pfdShowImgTest").data("imageListLoad").indexToCountPage(parseInt(thisPage));
        }*/
        imageListLoadDataSource.totalPageCount(data.totalPageCount);
        imageListLoadDataSource.addNode($("#pfdShowImgTest"));
        imageListLoadDataSource.addNode($("#PFDNavigationRight"));
        var responseData=service.responseData();
        imageListLoadDataSource.signSuccess(responseData.virtualSignFileId,responseData.fileId,responseData.filePath);
    }
    options.PFDNavigation.template = template;

    options.url =arsg.loadSignUrl;
    var imageListLoadDataSource=$.imageListLoadDataSource({
        url:arsg.loadFileImageUrl,
        isLoadImage:function(options,self,flag,pages){
            var responseData=service.responseData();
            self.data.fileId=responseData.fileId;
            self.data.filePath=responseData.filePath;
            if(flag == "sign"){
                self.data.isDeletePage=false;//文件ID 与 NEW_FILE_ID 中的ID  不一样是否删除所有页
                self.data.isNeedNewPage=true;//是否需要新的页面  【true 删除原来的页 在转换】【false 如果页存在则 直接取】
            }else if(flag == "nosign"){
                self.data.isDeletePage=false;//文件ID 与 NEW_FILE_ID 中的ID  不一样是否删除所有页
                self.data.isNeedNewPage=false;//是否需要新的页面  【true 删除原来的页 在转换】【false 如果页存在则 直接取】
            }
            return pages;
        }
    });
    /**
     * 签名成功
     * @param fileId
     * @param filePath
     * @param pages array or array
     * @param 页面加载完毕 回调
     */
    imageListLoadDataSource.signSuccess=function(virtualSignFileId,fileId,filePath,pages,callback){
        imageListLoadDataSource.addSignPage(pages);
        var self=imageListLoadDataSource.self();
        self.data.fileId=fileId;//文件ID
        self.data.filePath=filePath;//文件路径
        self.data.virtualSignFileId=virtualSignFileId;//文件路径
        var count=0;
        if(pages == null && self.nodes != null && self.nodes[0] != null){
            return;
        }
        var node=self.nodes[0];
        var page=node.data("imageListLoad").thisPage();
        page=page==null ? "1" : page;
        node.imageListData("thisPageChage",{page:page,callback:callback});
    }
    options.addSignSrc=arsg.addSignSrc;
    options.reduceSignSrc=arsg.reduceSignSrc;
    options.deleteSignSrc=arsg.deleteSignSrc;
    options.loadImagePath=arsg.loadImagePath;
    options.againTempleteCall=arsg.againTempleteCall;
    service = LoadSignVisual(options);
    // ftp导航选择事件绑定
    $(".PFDNavigation").on("click", ".navigationFTPimg", function() {
        var nextPage = $.parseJSON($(this).parent().attr("image-data")).page;
        $("#pfdShowImgTest").data("imageListLoad").indexToCountPage(parseInt(nextPage));
    });
    var customCreateSign = function(sign,top,left,c) {
        if(c != null){
            var dragRegion=$("#"+sign.imageItemDivId);
            var dragRegionOffset = dragRegion.offset();
            var defaultsRate,_top,_left;
            var rate=1;
            if($("#pfdShowImgTest").data("imageListLoad") != null){
                rate=$("#pfdShowImgTest").data("imageListLoad").zoomRate();
            }
            var _zoomRate=(sign.zoomRate == null ? 1:sign.zoomRate) * rate
            var pageWidth= dragRegion.width()-20 * rate;
            var pageHeight= dragRegion.height()-20 * rate;
            //判断是否需要缩小
            if(sign.imgWidth*_zoomRate >= pageWidth || sign.imgHeight*_zoomRate >= pageHeight){
                if(sign.imgWidth*_zoomRate > sign.imgHeight*_zoomRate && sign.imgHeight*_zoomRate >= pageHeight){
                    //以高度为标准缩小
                    defaultsRate=pageHeight / (sign.imgHeight * _zoomRate);
                }else if(sign.imgWidth*_zoomRate == sign.imgHeight*_zoomRate){
                    if(pageWidth >= pageHeight){
                        defaultsRate=pageHeight / (sign.imgHeight * _zoomRate);
                    }else{
                        defaultsRate=pageWidth / (sign.imgWidth * _zoomRate);
                    }
                }else{
                    //以宽度为标准缩小
                    defaultsRate=pageWidth / (sign.imgWidth * _zoomRate);
                }
            }

            if(defaultsRate != null){
                sign.zoomRate= parseInt(_zoomRate * defaultsRate * 10)/10.0;
            }

            //初始坐标计算
            if (!(dragRegionOffset.top <= top)){
                top=parseInt(dragRegionOffset.top)+2;
            }else if(!(top <= c.top)){
                top=parseInt(c.top) < dragRegionOffset.top ? (dragRegionOffset.top+1) : parseInt(c.top);
            }
            if(!(dragRegionOffset.left < left)){
                left=parseInt(dragRegionOffset.left)+2;
            }else if(!(left <= c.left)){
                left=parseInt(c.left) < dragRegionOffset.left ? (dragRegionOffset.left+1) : parseInt(c.left);
            }
        }



        var isHide = false,signTotalCount = 5000,allSignCount=5000;
        /*if (sign.corFlag == '3' || sign.corFlag == '4' || sign.corFlag == '2') {
            if (sign.corFlag != '2') {
                isHide = true;
            }
        }*/
        if(sign.corFlag == '3'){
            var stampList=service.responseData().stampList;
            var stampWater;
            for(var item in stampList){
                var stamp=stampList[item];
                if(stamp.stampType == '3'){
                    stampWater=stampWater+","+stamp.id;
                }
            }


            var data = service.getSignArrays();
            var signatureArrays = data.signatureArrays;
            for(var item in signatureArrays){
                var signs=signatureArrays[item];
                if(signs != null){
                    if(stampWater.indexOf(signs.id) > -1){
                        //WORKFLOW_WATER_STAMP_NO_ADD=当前文档已经有水印请不要在添加！
                        layer.msg(intl.WORKFLOW_WATER_STAMP_NO_ADD);
                        return ;
                    }
                }
            }
        }


        var options = {
            signType : sign.id,// 签章类型
            corFlag : sign.corFlag,
            imgPath : sign.filePath,
            imgWidth : sign.imgWidth,
            imgHeight : sign.imgHeight,
            signTotalCount:signTotalCount,
            allSignCount:allSignCount,
            offsetTop : top,
            offsetLeft : left,
            isHide : isHide,
            zoomRate:sign.zoomRate,
            imageItemDivId:sign.imageItemDivId,
            rateCount:function(){
                return $("#pfdShowImgTest").data("imageListLoad").zoomRate();
            }
        }
        var pfdShowImgPngId;
        if(sign.imageItemDivId != null){
            pfdShowImgPngId=sign.imageItemDivId;
        }else{
            pfdShowImgPngId=$("#pfdShowImgTest").data("imageListLoad").thisPageImageItemDivId();
        }
        var $pfdShowImgPng=$("#"+pfdShowImgPngId);
        var pdfWidth=$pfdShowImgPng.width();
        var pdfHeight=$pfdShowImgPng.height();

        var publicInput=null;
        if(sign.corFlag == '4'){//骑缝章的初始位置
            var _zoomRate=$("#pfdShowImgTest").data("imageListLoad").zoomRate();
            var offsetLeft=$pfdShowImgPng.offset().left+pdfWidth-sign.imgWidth*(sign.zoomRate == null ? 1 : sign.zoomRate)*_zoomRate;

            if(options.offsetTop == null || options.offsetTop == 0 || typeof options.offsetTop == "string"){
                options.offsetTop=$pfdShowImgPng.offset().top+pdfHeight/2-sign.imgHeight/2;  //$pfdShowImgPng.offset().top+offsetTop-15;
            }
            options.offsetLeft=offsetLeft;
            options.isMove=true;//不能
            options.borderMove="right";//沿着边 left right bottom left

            publicInput=function(id,val,img_data){
                if(img_data.stampType == '4'){
                    var $sing=$("#"+id).find("div[sign]");

                    var pdfWidth=$pfdShowImgPng.width();
                    var pdfHeight=$pfdShowImgPng.height();



                    var length=service.responseData().pdfImageList.length;
                    //$("#"+id).offset({top:offsetTop,left:offsetLeft});
                    var marginLeft=$sing.width()-($sing.width()/length < 4 ? 4:$sing.width()/length);
                    $sing.css("background-position-x",marginLeft+"px");
                    var $operation=$("#"+id).find("div[operation]");
                    $operation.css("position","absolute");
                    marginLeft=marginLeft-($("#"+id).width() - $sing.width());
                    $operation.css("margin-left",marginLeft+"px");


                    var offsetLeft=$pfdShowImgPng.offset().left+pdfWidth-$sing.width();
                    var offsetTop=$sing.offset().top;
                    var _offset={top:offsetTop,left:offsetLeft};
                    $("#"+id).offset(_offset);

                    return _offset;
                }
            }

        }

        //骑缝章放大缩小处理
        service.input=function(id,val){
            var img_data=$("#"+$("#"+id).attr("signtype")).attr("img_data");
            img_data=$.parseJSON(img_data);

            var _img_data=$("#"+img_data.id).attr("img_data");
            _img_data=$.parseJSON(_img_data);
            _img_data.zoomRate=val;
            $("#"+img_data.id).attr("img_data",JSON.stringify(_img_data));

            if(publicInput != null){
                return publicInput(id,val,img_data);
            }
            //同比例放大签章
        }
        if(sign.corFlag == '4'){//骑缝章效果
            var length=service.responseData().pdfImageList.length;
            var signTypeBool=service.isSignType(sign.id);
            service.createSign(options);
            var qifeng=function(){
                $("div[signtype='"+sign.id+"']").each(function(){
                    var width=$(this).find("div[sign]").width();
                    var height=$(this).find("div[sign]").height();
                    var marginLeft=width-(width/length < 4 ? 4 : width/length);
                    $(this).find("div[sign]").css("background-position-x",marginLeft+"px");
                    var $operation=$(this).find("div[operation]");
                    $operation.css("position","absolute");
                    marginLeft=marginLeft-($(this).width() - width);
                    $operation.css("margin-left",marginLeft+"px");
                });
            }
            qifeng();
            if(signTypeBool){
                return;
            }

            $("#pfdShowImgTest").on("click",".opeare",function(){
                qifeng();
            });
            return;
        }else if(sign.corFlag == '3'){
            var thisPage=$("#pfdShowImgTest").data("imageListLoad").thisPage();
            var scrollTop=$("#pfdShowImgTest").scrollTop();
            var callback=function(){
                var $operation=$("div[signtype='"+sign.id+"']").find("div[operation]");
                $operation.css("position","absolute");
                $("div[signtype='"+sign.id+"'] img[operation='delete']").on("click",function(event,test){
                    if(test != 1){
                        var signtype=$(this).parent().parent().attr("signtype");
                        $("div[signtype='"+signtype+"'] img[operation='delete']").trigger("click",1);
                        $("#pfdShowImgTest").data("imageListLoad").loadNextCall=null;
                    }
                });
                var filePath=sign.filePath;
                if(filePath.substring(filePath.length-3,filePath.length) != "png"){
                    $("div[signtype='"+sign.id+"']").find("div[sign]").css("opacity","0.3");
                }
                $("div[signtype='"+sign.id+"']").css("z-index","1");
                /*$("#pfdShowImgTest").data("imageListLoad").indexToCountPage(thisPage);
                $("#pfdShowImgTest").scrollTop(scrollTop);*/
            }


            $("#pfdShowImgTest .imageItemDiv").each(function(index){
                options.imageItemDivId=$(this).attr("id");
                options.imgHeight=$(this).height()-2;
                options.imgWidth=$(this).width()-2;
                options.offsetTop=$(this).offset().top;
                options.offsetLeft=$(this).offset().left;
                options.isMove=false;//不能
                options.isEnlarge=false;//true 支持放大  false不支持放大
                options.isNarrow=false;//true 支持缩小
                options.isNotOffset=true;//不用判斷坐標
                service.createSign(options);

                if(index ==  ($("#pfdShowImgTest .imageItemDiv").length-1)){
                    callback();
                }
            });
            $("#pfdShowImgTest").data("imageListLoad").loadNextCall=function(page){
                var thiss=$("#pfdShowImgTest #"+page+" .imageItemDiv");
                options.imageItemDivId=thiss.attr("id");
                options.imgHeight=thiss.height()-2;
                options.imgWidth=thiss.width()-2;
                options.offsetTop=thiss.offset().top;
                options.offsetLeft=thiss.offset().left;
                service.createSign(options);
                callback();
            }
            return;
            /*service.createSign(options);

            //每页都添加水印
            var page=$pfdShowImgPng.attr("pdfpage");
            var thispage=$(".divBorder.selectBorder");
            var pdfImageList=service.responseData().pdfImageList;
            $(".divBorder").bind("click",function(event,options){
                if(typeof options == "object"){
                    service.createSign(options);
                }
            });

            for(var item in pdfImageList){
                var pdfImage=pdfImageList[item];
                if(!((parseInt(pdfImage.page)+1) == parseInt(page))){
                    console.log("水印改的页数"+(parseInt(pdfImage.page)+1));
                    $(".divBorder:contains('"+(parseInt(pdfImage.page)+1)+"')").trigger("click",options);
                }
            }
            $(".divBorder").unbind("click");*/

        }

        var thisPage;
        if(options.imageItemDivId != null){
            thisPage=$("#"+options.imageItemDivId).parent().attr("id");
        }else{
            thisPage=$("#pfdShowImgTest").data("imageListLoad").thisPage();
        }
        thisPage=thisPage == null ? 1:thisPage;
        if(imageListLoadDataSource.isThisPageCantSign(thisPage)){
            service.createSign(options);
        }else{
            //WORKFLOW_CUSTOM_SIGN_THIS_PAGE_LOAD=当前页正在加载，请勿拖曳签章
            layer.msg(intl.WORKFLOW_CUSTOM_SIGN_THIS_PAGE_LOAD);
        }
    }
    // --------------------------------------拖动方式创建创建签章---------------------------------------------
    var movein = null;
    var moveEnd = null;
    // 禁止图片拖曳
    $(".navigation").on("mousemove", "img[img_data]", function(event) {
        return false
    });
    // 绑定要拖动的div
    var optionsdwon = {}
    optionsdwon.parentIndex = ".navigation";
    optionsdwon.dynamicIndex = "div[img_data_div]";
    optionsdwon.time = 100;
    $.oftenMousedown(optionsdwon, function(ele, event) {
        if (!(browser.browser == 'Chrome' || browser.browser == 'IE')) {
            return true;
        }
        if (browser.browser == 'IE' && browser.version < 9.0) {
            return true;
        }
        // 创建要拖动的节点
        var thisimg = ele.find("img");
        var sign = $.parseJSON(thisimg.attr("img_data"));
        var copyele = ele.clone(true);
        copyele.css("position", "absolute");
        var z_index = service.getMaxZ_index() + 1;
        copyele.css("z-index", "" + z_index);

        var rate=1;
        if($("#pfdShowImgTest").data("imageListLoad") != null){
            rate=$("#pfdShowImgTest").data("imageListLoad").zoomRate();
        }
        var _zoomRate=(sign.zoomRate == null ? 1:sign.zoomRate) * rate;
        copyele.find("img").width(sign.imgWidth*_zoomRate);
        copyele.find("img").height(sign.imgHeight*_zoomRate);
        copyele.find("img").css("margin-top","0");
        copyele.find("img").css("border","0");
        copyele.width(sign.imgWidth*_zoomRate+3);
        copyele.height(sign.imgHeight*_zoomRate+2);
        copyele.addClass("boxShow");
        // 插入body中 并定位
        $("body").append(copyele);
        copyele.offset({
            top : ele.offset().top,
            left : ele.offset().left
        });

        var iscanmove = true,ismove=false;//是否移动过 false 没有移动   点击  移动（移动过，就不算是单击）  放下   和   单击不同
        var disX = event.pageX - ele.offset().left;
        var disY = event.pageY - ele.offset().top;
        var thisele = ele;

        // IE解决方案
        var bindEventDom = null;
        moveEnd = function() {
            var top = copyele.find("img").offset().top, left = copyele.find("img").offset().left;
            iscanmove = false;
            bindEventDom.unbind("mousemove");
            bindEventDom.unbind("mouseup");
            $(document).unbind("mouseup");//
            $(document).unbind("mousemove");//
            copyele.remove();
            var canCreateSignArray={},canCreateSignArrayLength=0;
            // 创建节点
            if (!iscanmove && ismove) {
                console.log("top"+top+"   left"+left);
                $("#pfdShowImgTest .image").each(function(){
                    var dragRegion = $(this);
                    var dragRegionOffset = dragRegion.offset();
                    var c = {};

                    c.top = dragRegionOffset.top + (dragRegion.height() - sign.imgHeight*_zoomRate);
                    c.left = dragRegionOffset.left + (dragRegion.width() - sign.imgWidth*_zoomRate);

                    // 判断是否在此区域中
                    if (dragRegionOffset.top <= top && top <= c.top && dragRegionOffset.left < left && left <= c.left) {
                        sign.imageItemDivId=dragRegion.attr("id");
                        customCreateSign(sign,top,left);
                    }else{
                        if((dragRegionOffset.top-sign.imgHeight*_zoomRate) <= top && top <= (dragRegionOffset.top+dragRegion.height())
                            && (dragRegionOffset.left-sign.imgWidth*_zoomRate) <= left  && left <= (dragRegionOffset.left+dragRegion.width())){
                            //能创建签名数组
                            canCreateSignArray[dragRegion.attr("id")]=c;
                            canCreateSignArrayLength++;
                            //判断图片是否太大
                            /*if(sign.imgHeight*_zoomRate > dragRegion.height() || sign.imgWidth*_zoomRate > dragRegion.width()){
                                //WORKFLOW_CUSTOMSIGN_IMAGE_HEIGHT_WIDTH_ERROR=签章太大了，放不下。
                                layer.msg(intl.WORKFLOW_CUSTOMSIGN_IMAGE_HEIGHT_WIDTH_ERROR);
                            }else{
                                //WORKFLOW_CUSTOMSIGN_IMAGE_POSITION_ERROR=签章超出可编辑区域，放不下。
                                layer.msg(intl.WORKFLOW_CUSTOMSIGN_IMAGE_POSITION_ERROR);
                            }*/
                        }
                    }
                });


                if(canCreateSignArrayLength > 0){
                    var dragRegion=null,dragRegionOffset=null,c=null;
                    var _id,minRange,signTopRange=top+sign.imgHeight*_zoomRate/2;
                    for(var index in canCreateSignArray){
                        var id=index;
                        var ele=$("#"+id);
                        var offset=ele.offset();
                        var topRange=signTopRange-(offset.top+ele.height()/2);
                        topRange=Math.abs(topRange);
                        if(_id != null && topRange > minRange){
                            continue;
                        }
                        _id=id;
                        minRange=topRange;
                    }
                    sign.imageItemDivId=_id;
                    customCreateSign(sign,top,left,canCreateSignArray[sign.imageItemDivId]);
                }
            }
        }

        movein = function(event) {
            // 鼠标按下才能移动
            if (!iscanmove) {
                return false;
            }
            ismove=true;
            var top = event.pageY - disY;
            var left = event.pageX - disX;
            // 移动当前COPY的节点
            copyele.offset({
                top : top,
                left : left
            });
            $("#pfdShowImgTest .image").each(function(){
                var dragRegion = $(this);
                var dragRegionOffset = dragRegion.offset();
                var c = {};
                c.top = dragRegionOffset.top
                    + (dragRegion.height() - sign.imgHeight*_zoomRate);
                c.left = dragRegionOffset.left
                    + (dragRegion.width() - sign.imgWidth*_zoomRate);
                // 判断是否在此区域中
                if (dragRegionOffset.top <= top && top <= c.top
                    && dragRegionOffset.left < left && left <= c.left) {
                    copyele.addClass("selectImgBorder");
                    return false;
                } else {
                    copyele.removeClass("selectImgBorder");
                }
            });


        }

        if (browser.browser == "IE" && browser.version < 9.0) {
            bindEventDom = $(this);
        } else {
            bindEventDom = copyele;
        }
        $(document).bind("mousemove", function(event) {
            if (movein != null) {
                movein(event);
            }
            return false;
        });

        bindEventDom.bind("mouseup", function() {
            if (moveEnd != null) {
                moveEnd();
            }
            return false;
        });

        $(document).bind("mouseup", function() {
            if (moveEnd != null) {
                moveEnd();
            }
            return false;
        });
    });
    // --------------------------------------单机方式创建创建签章---------------------------------------------
    $(".navigation").find("td[dombindevent]").find("span").css("cursor", "hand");

    var createStamp=function(sign){
        sign.imageItemDivId=$("#pfdShowImgTest").data("imageListLoad").thisPageImageItemDivId();
        var dragRegion=$("#"+sign.imageItemDivId);
        var dragRegionOffset = dragRegion.offset();
        var rate=1;
        if($("#pfdShowImgTest").data("imageListLoad") != null){
            rate=$("#pfdShowImgTest").data("imageListLoad").zoomRate();
        }
        var _zoomRate=(sign.zoomRate == null ? 1:sign.zoomRate) * rate;
        var c = {};
        c.top = dragRegionOffset.top + (dragRegion.height() - sign.imgHeight*_zoomRate);
        c.left = dragRegionOffset.left + (dragRegion.width() - sign.imgWidth*_zoomRate);
        var top=dragRegionOffset.top+dragRegion.height()/2-sign.imgHeight/2;
        var left=dragRegionOffset.left+dragRegion.width()/2-sign.imgWidth/2;
        customCreateSign(sign,top,left,c);
    }

    $(".navigation").on("click", "td[dombindevent]", function() {
        var sign = $.parseJSON($(this).find("img[img_data]").attr("img_data"));
        createStamp(sign);
        /*var isHide = false, signTotalCount = null;

        signTotalCount = 5;

        var options = {
            signType : sign.id,// 签章类型
            corFlag : sign.corFlag,
            imgPath : sign.filePath,
            imgWidth : sign.imgWidth,
            imgHeight : sign.imgHeight,
            signTotalCount : signTotalCount,
            isHide : isHide
        }
        if(sign.corFlag == '4'){//骑缝章的初始位置
            var pdfWidth=$("#pfdShowImgPng").width();
            var pdfHeight=$("#pfdShowImgPng").height();
            var offsetTop=pdfHeight/4;
            var offsetLeft=$("#pfdShowImgPng").offset().left+pdfWidth-sign.imgWidth;
            options.offsetTop=offsetTop;
            options.offsetLeft=offsetLeft;
            options.isMove=false;//不能
        }else if(sign.corFlag == '3'){
            var pdfWidth=$("#pfdShowImgPng").width();
            var pdfHeight=$("#pfdShowImgPng").height();

            options.imgWidth=pdfWidth;
            options.imgHeight=pdfHeight;

            options.offsetTop=$("#pfdShowImgPng").offset().top;
            options.offsetLeft=$("#pfdShowImgPng").offset().left;
            options.isMove=false;//不能
            options.isEnlarge=false;//true 支持放大  false不支持放大
            options.isNarrow=false;//true 支持缩小
        }
        service.createSign(options);
        if(sign.corFlag == '4'){//骑缝章效果
            $("div[signtype='"+sign.id+"']").find("div[sign]").css("background-position-x",sign.imgWidth/5*2+"px");
        }else if(sign.corFlag == '3'){
            $("div[signtype='"+sign.id+"']").find("div[sign]").css("opacity","0.3");
        }*/
    });
    /**
     * 选中删除
     */
    $(".navigation").on("click", "img[name='selectImg']", function() {
        var parentDom = $(this).parent();
        var signType = parentDom.attr("id").replace("select", "");
        service.removeSignType(signType);
    });
    //-----------------------生成手写体-----------------------
    var reqeustStamp=function(countBool){
        if(countBool){
            $.idsLoad();
        }
        var bool=true;
        var run=function(){
            setTimeout(function() {//down 1s，才运行。
                var data={};
                data.signPageKey=service.responseData().handSignKey;
                $.ajax({
                    type : "POST",
                    url :arsg.createStampUrl,
                    dataType : "json",
                    data:{data:JSON.stringify(data)},
                    success : function(json){
                        if(json.resultCode == -1){
                            bool=false;
                        }else{
                            var handWriteSignResponse=json.data;
                            var action=handWriteSignResponse.actionStr;

                            if(action == "1"){//关闭二维码页面
                                if(countBool){
                                    run();
                                }
                            }else if(action == "3"){//关闭签章请求
                                bool=false;
                            }else if(action == "5"){//接收签章
                                if(countBool){
                                    layer.closeAll('loading');
                                }
                                var stampList=handWriteSignResponse.stampList;
                                //手写签章特殊处理 intl.WORKFLOW_HAND_WIRTE_IMAGE_RATE

                                if(typeof stampList == "object" && stampList.length > 0){
                                    var count=0;
                                    for(var index in stampList){
                                        var stamp=stampList[index];
                                        stamp.imgWidth=stamp.imgWidth/intl.WORKFLOW_HAND_WIRTE_IMAGE_RATE;
                                        stamp.imgHeight=stamp.imgHeight/intl.WORKFLOW_HAND_WIRTE_IMAGE_RATE;
                                        stamp.floatImgHeight=stamp.imgHeight;
                                        stamp.floatImgWidth=stamp.imgWidth;
                                        count++;
                                    }
                                }
                                if(stampList.length == count){
                                    handWriteSignCount=handWriteSignCount+stampList.length;
                                    service.againRenderingNavigation(stampList,false);
                                }
                            }else if(action == "6"){//不做任何操作

                            }
                        }
                        if(bool && !countBool){
                            run();
                        }
                    }
                });
            }, 1000);
        }
        run();
    }
    service.reqeustStamp=reqeustStamp;
    //签名展开关闭
    $("body").on("click",".signatureMenuBodyLiOne,.signatureMenuBodyLiOneIE7",function(){
        var src=$(this).find("img").attr("src");
        if(src.indexOf("open") != -1){
            src=src.replace("open","close");
            $(this).parent().children().hide();
        }else{
            src=src.replace("close","open");
            $(this).parent().children().show();
        }
        $(this).show();
        $(this).find("img").attr("src",src);
    });
    //展开 关闭pdf导航
    var closePFDNavigation=function(ele){
        var width=$("#PFDNavigation").width();
        var openWidth=$(".openPFDNavigation").width();
        var offset=ele.offset();
        offset.left=offset.left+(width-openWidth);
        $("#PFDNavigation").offset(offset);
        $("#PFDNavigationName").offset(offset);
    }
    var openPFDNavigation=function(ele){
        var width=$("#PFDNavigation").width();
        var openWidth=$(".openPFDNavigation").width();
        var offset=ele.offset();
        offset.left=offset.left-(width-openWidth);
        $("#PFDNavigation").offset(offset);
        $("#PFDNavigationName").offset(offset);

        if($("#PFDNavigationRight").scrollTop() <= 0){
            var count=0;
            $("#PFDNavigationRight li").each(function(){
                var _count=parseInt($(this).attr("id"));
                if(_count > count){
                    count=_count;
                }
            });
            var responseData = service.responseData();
            if(responseData.totalPageCount > count){
                count=count+1;
            }
            $("#PFDNavigationRight").data("imageListLoad").indexToCountPage(count,function(){
                $("#PFDNavigationRight").data("imageListLoad").indexToCountPage(1);
            });
        }

    }
    $(".openPFDNavigation").click(function(){
        var classs=$(this).attr("class");
        var offset=$("#pfdShowImgTest").offset();
        offset.left=offset.left + $("#pfdShowImgTest").width()-6;

        if($(this).offset().left < offset.left){
            closePFDNavigation($(this));
            $(this).removeClass("openImg");
            $("div.opeare").parent().css({
                top:"90%",
                left:"95%"
            });
        }else{
            openPFDNavigation($(this));
            $(this).addClass("openImg");
            $("div.opeare").parent().css({
                top:"90%",
                left:($(this).offset().left-60)+"px"
            });
        }
    });
    closePFDNavigation($(".openPFDNavigation"));

    //对外函数
    $.startSignFunction=function(methodName,_options){
        var methodManager={
            imageListLoadDataSource:function(options){
                return imageListLoadDataSource;
            },
            service:function(options){
                return service;
            }
        }
        var method=methodManager[methodName];
        if(typeof method == "function"){
            return method(_options);
        }else{
            throw new Error("No method name de function");
        }
    }
}
/**
 * 签名业务管理
 */
$.signServiceManger=function(options){
    var defaults={},self={},service={};
    defaults.service=null;//签名页面加载成功 调用函数
    defaults.imageListLoadDataSource=null;//pdf列表加载
    defaults.lmParam=null;//socket签名客户端对象
    defaults.idsContentLoad=null;//全局锁屏层
    defaults.intl=null;//国际化对象
    defaults.openButton=null;//打开按钮
    defaults.closeButton=null;//关闭按钮
    defaults.signOrderService=null;
    defaults.SERVER_SIGN_CHANNEL=1;//服务器签名
    defaults.KEY_SIGN_CHANNEL=2;//key签名
    defaults.stampsByRuleUrl=null;//骑缝章分割url
    defaults.signUrl=null;//骑缝章分割url
    defaults.adobeVerSignFlags=true;//true 修订签  false 验证签
    var options=$.extend(defaults,options);
    /**
     * tip提示 -- 大屏提示
     * @param message 提示信息
     * @param flag 1=成功 -1=失败  图标
     */
    defaults.tip=function(message,flag){//默认提示
        var data={};
        data.tipContent=message;
        data.closeImgSrc=options.closeImgSrc;
        if(flag == -1){
            data.imgSrc=options.tipImgSrc;//"../base/img/fail.png";
        }else if(flag == 2 && typeof options.tip == "function"){
            options.tipSuccess(message,data.imgSrc);
            return;
        }
        defaults.isParent=false;
        $.tip(data);
    }
    /**
     * 单次的客户端签名
     */
    self.socketSign=function(data,success,fail){
        var lmFunctionParam = {
            defSuccessFunction : success,//签名成功
            defFailFunction : fail,//签名失败
            idsLoad:options.idsContentLoad//锁屏层
        }
        //签名失败回调
        lmParam.signFlagFunction=fail;
        //socket签名
        var responseData=options.service.responseData();
        socketSign(responseData,data,lmFunctionParam);
    }

    /**
     * 数字服务器签名
     * 服务器多签
     */
    self.serverSign=function(signPrintPdfList,successCall){
        var responseData=options.service.responseData();
        responseData.userSignType="4";
        self.ordinarySign(signPrintPdfList,successCall);
    }

    /**
     * 服务器单签
     */
    self.serverOneSign=function(data,success,fail){
        self.serverSign(data,success);
    }

    /**
     * 数字签名
     * @param sginChannel 签名渠道  1 服务器签 2key签
     */
    self.numberSign=function(sginChannel){
        //签名排序对象不能为空
        if(options.signOrderService == null){
            self.signFail();
            return null;
        }
        var signFail=function(flag){
            //关闭签名
            options.signOrderService.closeSign();
            //恢复 签名数据 ---最新文件ID，文件路径，文件Id
            options.service.responseData().fileId=options.closeButton.fileId;
            options.service.responseData().filePath=options.closeButton.filePath;
            options.service.responseData().virtualPath=options.closeButton.virtualPath;
            //签名失败
            self.signFail(null,flag);
        }

        var call=function(){
            var responseData=options.service.responseData();
            var item=options.signOrderService.next();
            var data={};
            data.signatureArrays=[];
            data.signatureArrays.push(item);
            //蓝丝带签名所需传参状态
            if(item.isVerification){
                responseData.certificationLevel = '1';//验证签
            }else{
                responseData.certificationLevel = '0';
            }
            //客户端做hash签名
            var signMethod=self.socketSign;
            if(sginChannel == defaults.SERVER_SIGN_CHANNEL){
                var signPrintPdfList=data.signatureArrays;
                self.serverOneSign(signPrintPdfList,function(json){
                    if(options.signOrderService.isNotNext()){
                        call();
                    }
                });
            }else if(sginChannel == defaults.KEY_SIGN_CHANNEL){
                self.socketSign(data,function(){
                    //普通签名 做 页面签名
                    var signPrintPdfList=data.signatureArrays;
                    self.ordinarySign(signPrintPdfList,function(json){
                        if(options.signOrderService.isNotNext()){
                            call();
                        }
                    });
                },signFail);
            }
        }

        call();
    }

    /**
     * 普通签名+公共签名
     * @param
     */
    self.ordinarySign=function(signPrintPdfList,successCall){
        var responseData=options.service.responseData();
        var data={};
        data.fileId=responseData.fileId;
        if(responseData.signFileId != null){
            data.virtualSignFileId=responseData.signFileId;
        }else{
            data.virtualSignFileId=responseData.virtualSignFileId;
        }

        data.virtualPath=responseData.virtualPath;
        data.signPageKey=responseData.signPageKey;
        data.userSignType=responseData.userSignType;
        data.signPrintPdfList=signPrintPdfList;
        data.certificationLevel=responseData.certificationLevel;//数字签名有用
        data.keyId=lmParam.keyId;
        data.keyInfo=lmParam.cerBase64Str;
        var request={};
        request.data=JSON.stringify(data);
        $.ajax({
            type: "POST",
            url: defaults.signUrl,
            dataType: "json",
            data:request,
            success: function(json){
                if(json.resultCode == 0 && json.data != null){
                    var responseData=options.service.responseData();
                    responseData.fileId=json.data.fileId;
                    responseData.filePath=json.data.filePath;
                    responseData.virtualPath=json.data.virtualPath;
                    responseData.pdfName=json.data.pdfName;
                    responseData.blueDigital=json.data.blueDigital;
                    responseData.hasSm2Signed = json.data.hasSm2Signed;
                    responseData.hasRSASigned = json.data.hasRSASigned;
                    responseData.hasPublicCerSign = json.data.hasPublicCerSign;
                    //判断是否还有下一次签名
                    if(!options.signOrderService.isNextSign()){
                        self.signSuccess(json);
                    }
                    //签名成功回调
                    if(typeof successCall == 'function'){
                        successCall(json);
                    }
                }else{
                    options.idsContentLoad.close();
                    var message=json.message;
                    if(message == null || message == ""){
                        message=options.intl.WORKFLOW_SIGN_FAIL_PLAECE;
                    }
                    layer.msg(message);
                    options.openButton();
                }
            }
        });

        var request={};
        request.data=JSON.stringify(data);
    }

    /**
     * 签名成功
     * @data 签名成功后返回数据
     * @page 签名的页码
     */
    self.signSuccess=function(data){
        var callSuccess=function(){
            //关闭锁屏
            options.idsContentLoad.close();
            defaults.tip(options.intl.sign_success,2);
            options.service.againPage(data);
            options.openButton();
        }
        var responseData=options.service.responseData();
        //取得签名页码
        var pages=options.signOrderService.getPages();
        if(responseData.signFileId == null){
            options.imageListLoadDataSource.signSuccess(responseData.virtualSignFileId,responseData.fileId,responseData.filePath,pages,callSuccess);
        }else{
            options.imageListLoadDataSource.signSuccess(responseData.signFileId,responseData.fileId,responseData.filePath,pages,callSuccess);
        }
        $("#pfdShowImgTest").data("imageListLoad").loadNextCall=null;
        $("#pfdShowImgTest").data("imageListLoad").signSuccess(pages);
        $("#PFDNavigationRight").data("imageListLoad").signSuccess(pages);
    }

    /**
     * 签名失败
     * @message 签名失败信息
     * @message flag等于空
     */
    self.signFail=function(message,flag){
        if(typeof message != "string" || message == ""){
            //intl.sign_faile 国际化后的签名失败
            message=options.intl.sign_faile;
        }
        //关闭锁屏
        options.idsContentLoad.close();
        //大屏提示
        if(flag != null && flag != "1"){
            defaults.tip(message,-1);
        }else if(flag == null){
            defaults.tip(message,-1);
        }

        //恢复按钮为 可点击状态
        if(options.openButton != null){
            options.openButton();
        }
    }
    /**
     * 签名
     * @userSignType 用户签名类型
     * @signatureArrays 签名
     */
    service.sign=function(userSignType,signatureArrays,isQifeng){
        var responseData=options.service.responseData();
        var signOrderData={};
        signOrderData.service=options.service;
        signOrderData.isVerification=!options.adobeVerSignFlags;
        signOrderData.isQifeng=isQifeng;
        //将所有数字签名换成公共签名
//		if(userSignType == '1'){
//			userSignType = '2';
//		}
        signOrderData.userSignType=userSignType;
        signOrderData.signatureArrays=signatureArrays;
        signOrderData.signUrl=options.signUrl;
        signOrderData.stampsByRuleUrl=options.stampsByRuleUrl;
        options.signOrderService=$.signOrder(signOrderData);
        options.closeButton();
        var call=function(){
            var data=options.signOrderService.nextSignData();//取得下次签名数据
            if(data != null){
                var userSignType=data.userSignType;
                var signatureArray=data.signatureArray;
                responseData.userSignType=userSignType;
                if(userSignType == "1"){
                    if(!checkCanKeySign() && intl.isThisUserServerSign){
                        //数字服务器签名 且 key无效
                        self.numberSign(defaults.SERVER_SIGN_CHANNEL,function(){
                            if(options.signOrderService.isNextSign()){
                                call();
                            }
                        });
                    }else{
                        //数字key签名
                        self.numberSign(defaults.KEY_SIGN_CHANNEL,function(){
                            if(options.signOrderService.isNextSign()){
                                call();
                            }
                        });
                    }
                }else{
                    //普通签名
                    self.ordinarySign(options.signOrderService.getSignList(),function(){
                        if(options.signOrderService.isNextSign()){
                            call();
                        }
                    });
                }
            }else{
                return;
            }
        }
        call();
    }
    return service;
}
/**
 * 试用与 修订签  与  骑缝签的抉择
 */
$.signOrder=function(options){
    var defaults={},self={};
    defaults.orderStampType=['1','0','2','5'];//排序顺序 5骑缝章
    defaults.isQifeng=false;//是否有骑缝章
    defaults.isVerification=false;//flase 修订签
    defaults.loadItem=0;
    defaults.service=null;//签名页面加载成功 调用函数
    defaults.userSignType=null;//用户签名类型
    defaults.onSignId={};//已经签名的Id
    var options=$.extend(defaults,options);
    /**
     * 骑缝章切割
     */
    self.stampsByRule=function(qifengArray){
        qifengArray=self.stampOrder(qifengArray);
        var newQifengArray=[];
        var responseData=options.service.responseData();
        var requestData={};
        requestData.fileId=responseData.fileId;
        requestData.virtualPath=responseData.virtualPath;
        requestData.stampList=qifengArray;
        var ajaxBool=false,returnBool=true;
        while(returnBool){
            if(!ajaxBool){
                $.ajax({
                    type: "POST",
                    url: options.stampsByRuleUrl,
                    dataType: "json",
                    async:false,//需要同步请求 请勿更改
                    data:{data:JSON.stringify(requestData)},
                    success: function(json){
                        if(json.resultCode != -1){
                            for(var itemData in json.data){
                                var stamp=json.data[itemData];
                                stamp.stampType="5";
                                var newstamp={};
                                newstamp.signType=stamp.id;
                                newstamp.imagePath=stamp.filePath;
                                newstamp.page=stamp.stampPage;
                                newstamp.pdf_x=Number(stamp.stampX);
                                newstamp.pdf_y=Number(stamp.stampY);
                                newstamp.imageWidth=Number(stamp.imgWidth);
                                newstamp.imageHeight=Number(stamp.imgHeight);
                                newstamp.stampType=stamp.stampType;
                                newstamp.floatImgHeight=stamp.imgHeight;
                                newstamp.floatImgWidth=stamp.imgWidth;
                                newstamp.id=stamp.id;
                                newstamp.signEleId=stamp.id;
                                responseData.stampList.push(stamp);
                                newQifengArray.push(newstamp);
                            }
                            returnBool=false;
                        }else{
                            layer.msg(json.message);
                            returnBool=false;
                        }
                    }
                });
            }
            ajaxBool=true;
        }
        return newQifengArray;
    }

    //印章 签名/手写体 日期 （最后一页做 透明图片验证签）骑缝章
    //SELF("0","签名"),COMPANY("1","印章"),DATE("2","时间"),WATERMARK("3","水印"),QIFENG("4","骑缝印章");
    self.groundBy=function(signatureArrays){
        defaults.loadItem=0;
        var groundBy={},itemCount=0,newArray=[],last;
        for(var index in signatureArrays){
            var signature=signatureArrays[index];
            var stampType=signature.stampType;

            if(typeof groundBy[stampType] != "object"){
                groundBy[stampType]=[];
                itemCount++;
            }
            var groundByArray=groundBy[stampType];
            groundByArray.push(signature);
        }

        if(itemCount == 1 && options.isQifeng){
            //只有骑缝章
            var index="4";
            if(checkCanKeySign()){//切割的骑缝章
                index="5";
            }
            var groundByArray=groundBy[index];
            last=self.newObject(groundByArray[0]);
        }else{
            for(var index in options.orderStampType){
                var item=options.orderStampType[index];
                if(last != null){break;}
                if(item == "5"){
                    continue;
                }
                var groundByArray=groundBy[item];
                if(typeof groundByArray == "object"){
                    last=groundByArray[0];
                    delete groundByArray[0];
                }
            }
        }

        for(var index in options.orderStampType){
            var item=options.orderStampType[index];
            var groundByArray=groundBy[item];
            for(var _index in groundByArray){
                var _item=groundByArray[_index];
                if(_item != null){
                    newArray.push(_item);
                }
            }
        }
        last.isVerification=true;
        newArray.push(last);
        return newArray;
    }

    /**
     * 新建一个对象
     */
    self.newObject=function(demo){
        var last=self.copy(demo);
        last.pdf_y=0;
        last.pdf_x=0;
        last.filePath=null;
        last.imagePath="null";
        return last;
    }

    /**
     * copy一个对象
     */
    self.copy=function(last){
        var _last={};
        for(var index in last){
            var item=last[index];
            _last[index]=item;
        }
        return _last;
    }

    /**
     * 骑缝章排序 z_index
     */
    self.stampOrder=function(array){
        for(var i=0;i<array.length;i++){
            for(var j=i;j<array.length;j++){
                if(array[i].z_index > array[j].z_index){
                    var item_j=array[j];
                    array[i]=item_j;
                    array[j]=array[i];
                }
            }
        }
        return array;
    }

    var service={};

    //骑缝章普通签
    //普通签名   数字签名
    /**
     * 下一次签名Data 签名业务控制
     * @return null 代表没有下次签名
     */
    service.nextSignData=function(){
        var responseData=options.service.responseData();
        var _userSignType=options.userSignType;//用户签名类型
        var hasSm2Signed = responseData.hasSm2Signed;
        var hasRSASigned = responseData.hasRSASigned; //第一页骑缝章修订签名，第二页骑缝章修订签名，第三页骑缝章修订签名，第四页骑缝章修订签名，公章验证签名
        if(hasSm2Signed || hasRSASigned){
            //文档已经有数字签名
            if(_userSignType == "2"){
                //公共证书签名
                for(var item in options.signatureArrays){
                    var sign=options.signatureArrays[item];
                    sign.isVerification=false;//修订签
                }
                options.newArray=options.signatureArrays;
            }else if(_userSignType == "1"){
                //数字证书签名
                if(options.isQifeng){
                    var qifengArray=[];
                    var newArray=[];
                    for(var index in options.signatureArrays){
                        var item=options.signatureArrays[index];
                        if(item.stampType == "4"){
                            var test={};
                            test.id = item.signType;
                            test.page = item.page;
                            test.imgWidth = item.floatImgWidth;//图片的宽度
                            test.imgHeight = item.floatImgHeight;//图片的高度
                            test.filePath =item.imagePath;//章(虚拟路径)
                            test.positionRate =item.positionRate;//占比
                            test.z_index =item.z_index;//层次
                            qifengArray.push(test);
                        }else{
                            newArray.push(item);
                        }
                    }
                    //key 签名 切割骑缝章
                    if(checkCanKeySign()){
                        var newQifengArray=self.stampsByRule(qifengArray);
                        for(var index in newQifengArray){
                            var item=newQifengArray[index];
                            newArray.push(item);
                        }
                    }else{
                        newArray=options.signatureArrays;
                    }
                    //重新定义数组
                    options.signatureArrays=newArray;
                }

                if(options.isVerification){
                    //验证签需要排序
                    options.newArray=self.groundBy(options.signatureArrays);
                }else{
                    //修订签
                    for(var item in options.signatureArrays){
                        var sign=options.signatureArrays[item];
                        sign.isVerification=options.isVerification;//修订签  或   验证签
                    }
                    options.newArray=options.signatureArrays;
                }
            }
        }else{
            //文档没有数字签名
            if(options.userSignType == '1'){//数字签名
                var signArray=[];//有无骑缝章
                for(var index in options.signatureArrays){
                    var item=options.signatureArrays[index];
                    var onSignId=options.onSignId;
                    var id=onSignId[item.signEleId];
                    if(id == null){
                        if(options.isQifeng  && item.stampType == "4"){
                            onSignId[item.signEleId]=item.signEleId;
                            signArray.push(item);
                        }else if(!options.isQifeng){
                            onSignId[item.signEleId]=item.signEleId;
                            signArray.push(item);
                        }
                    }
                }
                if(options.isQifeng){
                    //普通签
                    options.isQifeng=false;
                    options.newArray=signArray;
                    _userSignType="0";
                    //判断是否只有骑缝章
                    if(options.newArray.length == options.signatureArrays.length){
                        //空白章做数字签
                        var last=self.newObject(options.newArray[0]);
                        last.stampType="1";
                        last.signEleId=last.signEleId+"1";
                        last.isVerification=options.isVerification;//修订签  或   验证签
                        last.pdf_x=20000;
                        last.pdf_y=20000;
                        last.positionRate=0;
                        last.imageHeight=0;
                        last.imageWidth=0;
                        last.imagePath=intl.signno;
                        last.id="";
                        options.signatureArrays.push(last);
                    }
                }else{
                    //数字签
                    if(options.isVerification){
                        //验证签 需要排序
                        options.newArray=self.groundBy(signArray);
                    }else{
                        for(var item in signArray){
                            var sign=signArray[item];
                            sign.isVerification=options.isVerification;//修订签
                        }
                        //修订签 不需要排序
                        options.newArray=signArray;
                    }
                }
            }else{
                //普通签名  和  公共证书签名
                options.newArray=options.signatureArrays;
            }
        }
        if(options.newArray.length > 0){
            self.userSignType=_userSignType;
            return{
                userSignType:_userSignType,
                signatureArray:options.newArray
            }
        }
        return null;
    }

    /**
     * 取下一个签名
     */
    service.next=function(){
        var signature=options.newArray[defaults.loadItem];
        defaults.loadItem++;
        return signature;
    }

    /**
     * 关闭签名
     */
    service.closeSign=function(){
        defaults.loadItem=options.newArray.length+1;
    }

    /**
     * 判断是否继续签名
     */
    service.isSign=function(){
        if(defaults.loadItem > options.newArray.length){
            return false;
        }
        return true;
    }

    /**
     * 判断下一个是否为空
     */
    service.isNotNext=function(){
        if(defaults.loadItem < options.newArray.length){
            return true;
        }
        return false;
    }

    /**
     * 取得签名的页码
     */
    service.getPages=function(){
        var responseData=options.service.responseData();
        var pages=[];
        for(var index=0;index < options.signatureArrays.length;index++){
            var item=options.signatureArrays[index];
            if(item == null){ continue;}
            if(item.stampType == "5" ||  item.stampType == "3" || item.stampType == "4"){
                pages=[];
                for(var i=1;i <= responseData.totalPageCount;i++){
                    pages.push(i+"");
                }
                break;
            }
            pages.push(item.page);
        }
        return pages;
    }

    /**
     * 取得签名
     */
    service.getSignList=function(){
        return options.newArray;
    }

    /**
     * 判断是否有下一次签名
     */
    service.isNextSign=function(){
        var length=0;
        for(var index in options.onSignId){
            length++;
        }

        if(self.userSignType == "1"){
            //数字签名 最后一次
            return service.isNotNext();
        }else{
            //普通签名 最后一次
            if(length == 0 || options.signatureArrays.length == length){
                return false;
            }
        }
        return true;
    }
    return service;
}
/**
 * 蓝丝带
 */
$.fn.blueDigital=function(methodName,_options){
    var $ele=$(this);
    var method={
        init:function(data){
            var defaults={};
            defaults.status={};
            defaults.status.blue="../base/img/blueSign.png";
            defaults.status.error="../base/img/blueError.png";
            defaults.content={};
            defaults.content.error=intl.WORKFLOW_CUSTOMSIGN_FILE_ERROR;
            defaults.content.blue=intl.WORKFLOW_CUSTOMSIGN_FILE_NO_SIGN;
            defaults.top=64;
            defaults.left=260;
            var options=$.extend(defaults,data);
            $ele.attr("options",JSON.stringify(options));
        },
        status:function(data){
            var status,content,src;
            if(typeof data == "object"){
                status=data.status;
                content=data.content;
                src=data.src;
            }else{
                status=data;
                options=$.parseJSON($ele.attr("options"));
                content=options.content[status];
                src=options.status[status];
            }
            var img=$ele.find("img.flag");
            var span=$ele.find("span.content");
            img.attr("src",src);
            span.text(content);
        },
        postion:function(data){
            var top,left;
            var options=$ele.options;
            if(typeof data == "object"){
                top=data.top;
                left=data.left;
            }else{
                top=data;
                var options=$.parseJSON($ele.attr("options"));
                content=options.content[status];
                left=options.left;
            }
            $ele.offset({top:top,left:left});
        },
        show:function(){
            $ele.css("display","table");
        }
    }
    return method[methodName](_options);
}


