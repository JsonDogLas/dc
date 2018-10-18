
/*
*@ author wuhaihua
*@ version 1.0
*@ createtime 2017-11-28
*@ updatetime   
*@ relation 1.loadImage.css
*@ explain imageListLoad 控件数据源
**/
$.imageListLoadDataSource=function(options){
    var defaults={},service={},self={};
    defaults.advancePageLoad=3;//滚动时提前两页加载
    defaults.totalPageCount=3;//默认长度
    defaults.loadPageCount=3;//每次默认加载页数
    options=$.extend(defaults,options);
    //1.将图片拖到当前页  必须等当前页加载完毕 才能接收新的 签章
    self.data={};
    self.imageDataArray=[];//图片数据
    self.signPage={};//签名后改动的页码
    self.nodes=[];
    self.loadBool=true;
    self.noLoadPage={};//未进入加载的页码
    //本地存储数据 避免304
    self.loadPage={};
    if(self.imageDataArray.length < options.totalPageCount){
        for(var index=self.imageDataArray.length;index<options.totalPageCount;index++){
            self.imageDataArray.push(null);
        }
    }
    /*
     * 滚动监听 ----解决滚动不能的BUG
     */
    var scrollFunc=function(e){
        for(var item in self.nodes){
            var node = self.nodes[item];
            var scrollTop=node.scrollTop();
            var maxScrollTop=node.find("ul").height() - node.height();
            if((maxScrollTop - 50) < scrollTop  && !node.data("imageListLoad").isLoadOrder()){
                node.data("imageListLoad").loadNext(function(){
                });
            }
        }
    }
    if(document.addEventListener){
        document.addEventListener('DOMMouseScroll',scrollFunc,false);
    }//W3C
    window.onmousewheel=document.onmousewheel=scrollFunc;//IE/Opera/Chrome
    //----------------------------------------------------------------------------

    /**
     * 需要加载的页码
     * @param pages 页码数组
     * @param callback 成功后回调
     */
    self.ajax=function(pages,callback){
        self.data.pageArray=pages;
        $.getJSON(options.url,{data:JSON.stringify(self.data)},function(_data){
            if(_data.resultCode == -1){
                self.loadBool=true;
                return;
            }
            _data=_data.data;
            var pageMap={},updatePages={},insertPages={};
            for(var index in _data.pdfImageList){
                var item=_data.pdfImageList[index];
                pageMap[item.page]=item;
            }

            var updateCount=0;
            for(var index in pages){
                var item=pages[index];
                var length=(parseInt(pages[index])-1);
                if(self.imageDataArray[length] == null){
                    self.imageDataArray[length]=pageMap[item];
                    //需要插入的页数
                    if(pageMap[item] != null){
                        insertPages[pageMap[item].id]=pageMap[item].imagePath;
                    }
                }else{
                    var imagePath=pageMap[item].imagePath;
                    self.imageDataArray[length].imagePath=imagePath;
                    self.signPage[item]=null;
                    //需要更新的页数
                    updatePages[pageMap[item].id]=imagePath;
                    updateCount++;
                }

                var _item=self.imageDataArray[length];
                for(var i in self.nodes){
                    self.nodes[i].data("imageListLoad").imageDataArrayChange(length,_item);
                }
            }
            if(typeof callback == "function"){
                self.loadBool=true;
                callback(updatePages,updateCount,insertPages);
            }else{
                self.loadBool=true;
            }
        });
    }

    /**
     * 加载决策
     */
    self.isLoadImage=function(page,callback){
        if(!self.loadBool || page == null || parseInt(page) == 0){
            if(typeof callback == "function"){
                callback();
            }
            return;
        }
        self.loadBool=false;
        var pages=[];
        var pageInt=parseInt(page);
        var length=pageInt-1;//数组中的位置
        var advancePageLoad=length+options.advancePageLoad;
        var _advancePageLoad=length-options.advancePageLoad;
        _advancePageLoad= _advancePageLoad < 0 ? 0 : _advancePageLoad;
        advancePageLoad=advancePageLoad > options.totalPageCount ?  (options.totalPageCount-1) : advancePageLoad;

        var flag=null;

        //当前页为3页 加载范围   0(_advancePageLoad)-6(advancePageLoad)之间
        //1.水印 骑缝 普通签名时  普通页还未加载  ，就需要预先加载 签名页，用签名页来填补普通页
        //2.印章 签名 普通签名时  普通页必须加载，然后再试签名页


        if(self.imageDataArray[advancePageLoad] == null){//最大页数为空
            //判断其中是否 有签名页码
            for(var index=_advancePageLoad;((index <= advancePageLoad || pages.length < options.loadPageCount) && index < options.totalPageCount);index++){
                var pageNumber=index+1+"";
                if(self.signPage[pageNumber] != null && (flag == null || flag == "sign")/* && self.isPageLoad(pageNumber)*/){
                    pages.push(pageNumber);
                    flag="sign";
                }else{
                    if(self.imageDataArray[index] == null && (flag == null || flag == "nosign")){
                        pages.push(pageNumber);
                        flag="nosign";
                    }
                }
            }
        }else{
            //判断是否加载签名页码
            for(var index=_advancePageLoad;((index <= advancePageLoad || pages.length < options.loadPageCount) && index < options.totalPageCount);index++){
                var pageNumber=index+1+"";
                if(self.signPage[pageNumber] != null && (flag == null || flag == "sign")/* && self.isPageLoad(pageNumber)*/){
                    pages.push(pageNumber);
                    flag="sign";
                }
            }
        }

        if(pages.length > 0){
            if(typeof options.isLoadImage == "function"){
                pages=options.isLoadImage(options,self,flag,pages);
            }
            self.ajax(pages,callback);
        }else{
            self.loadBool=true;
            if(typeof callback == "function"){
                callback();
            }
        }
    }

    /**
     * 加载单个图片成功后回调
     * @param url 图片路径
     * @param callback 加载完成后的回调函数
     */
    self.loadImage =function(url,callback,test){
        var img = new Image();
        img.src = url;
        // 图片存在缓存中
        if(img.complete){
            callback(url,test);
            return;
        }
        // 图片加载到浏览器的缓存中回调函数
        img.onload = function(){
            callback(url,test);
        }
    }

    self.isPageLoad=function(page){
        for(var i in self.nodes){
            var bool=self.nodes[i].data("imageListLoad").isPageLoad(page);
            if(!bool){
                return false;
            }
        }
        return true;
    }

    /**
     * 滚动加载图片数据
     */
    $.fn.imageListData=function(methodName,_options){
        var imageThis=$(this);
        var method={
            init:function(init_options){
                self.isLoadImage(init_options.page);
            },
            isWait:function(isWait_options){//是否等待
                if(self.loadBool && self.imageDataArray[(parseInt(isWait_options.page)-1)] == null){
                    var index=parseInt(isWait_options.page)-1;
                    if(self.imageDataArray[index] != null && self.signPage[isWait_options.page] == null){
                        //开通异步线程调
                        setTimeout(function(){
                            self.isLoadImage(isWait_options.page,isWait_options.callback);
                        },1);
                        return;
                    }
                    self.isLoadImage(isWait_options.page,isWait_options.callback);
                }else{
                    isWait_options.callback();
                }
            },
            thisPageChage:function(optionsChange){//当前页变动
                var thisPage,callback;
                if(typeof optionsChange == "object"){
                    thisPage=optionsChange.page;
                    callback=optionsChange.callback;
                }else{
                    thisPage=optionsChange;
                }
                if(self.loadBool){
                    self.isLoadImage(thisPage,function(updatePages,count,insertPages){
                        var updateCount=0;
                        for(var index in updatePages){
                            var item=updatePages[index];
                            if(item != null){
                                self.loadImage(item,function(url,test){
                                    for(var i in self.nodes){
                                        self.nodes[i].data("imageListLoad").closeLoad(url,test);
                                    }
                                    updateCount++;
                                    if(updateCount == count && typeof callback == "function"){
                                        callback();
                                    }
                                },index);
                            }
                        }
                        //异步预先加载图片
                        setTimeout(function(){
                            for(var index in insertPages){
                                var item=insertPages[index];
                                if(item != null){
                                    self.loadImage(item,function(url,test){
                                        self.loadPage[url]=0;
                                    },index);
                                }
                            }
                        },10);

                    });
                }
            },
            imageDataArrayChange:function(_options){
                var index,data;
                index=_options.index;
                data=_options.data;
                self.imageDataArray[index]=data;
            },
            isLoadUrl:function(url){//是否加载此URl
                if(self.loadPage[url] != null){
                    self.loadPage[url]++;
                    if(self.loadPage[url] == self.nodes.length){
                        delete self.loadPage[url];
                    }
                    return false;
                }else{
                    return true;
                }
            }
        }
        return method[methodName](_options);
    }

    /**
     * 加签名页码
     * @param pages string or array
     */
    service.addSignPage=function(pages){
        if(typeof pages == "object"){
            for(var index in pages){
                var item=pages[index];
                self.signPage[item]=item;
            }
        }else if(typeof pages == "string"){
            self.signPage[pages]=pages;
        }
    }

    service.addNode=function(ele){
        if(typeof ele == "object" || ele.length > 0){
            ele.imageListData("init",options);
            self.nodes.push(ele);
        }
    }

    /**
     * 判断当前页是否能加签章
     * @param page 页码
     */
    service.isThisPageCantSign=function(page){
        var signPage=self.signPage;
        var thisPage=signPage[page];
        if(thisPage == page){
            return false;
        }
        return true;
    }


    /**
     * 数据变化
     */
    service.imageDataArrayChange=function(index,data){
        self.imageDataArray[index]=data;
    }


    /**
     * 重新定制长度
     */
    service.totalPageCount=function(count){
        options.totalPageCount=count;
        if(self.imageDataArray.length < count){
            for(var index=0;index<=count;index++){
                self.imageDataArray.push(null);
            }
        }
    }

    service.self=function(){
        return self;
    }

    service.options=function(){
        return options;
    }
    return service;
}


/*
*@ author wuhaihua
*@ version 1.0
*@ createtime 2017-11-02
*@ updatetime
*@ relation 1.loadImage.css
*@ explain 1.android list形式加载图片 跟pdf显示一样
**/
$.fn.imageListLoad=function(options){
    var defaults={};
    defaults.loadImagePath="../base/img/loadImage.gif";
    defaults.imageDataArray=[];//id,fileName,imagePath,zoomRate,page,width,height,thisZoomRate
    defaults.thisLoadItem=0;//当前加载到第几项
    defaults.thisZoomRate=1;//当前倍数
    defaults.thisPage=null;//当前第几页
    defaults.imageItemtemplate="imagePageModel";
    defaults.top=17;
    defaults.isZoomRate=true;//是否允许放大
    defaults.isPageShow=true;//顶部是否有页数显示
    defaults.isThisPageAddBorder=true;//当前页是否加边框
    defaults.fixedWidth=null;//如果有固定宽度，将不按本身的比例计算,放大缩小将无效
    defaults.loadPageCount=null;//默认加载页数
    defaults.loadPageCall=null;//加载完成后回调
    defaults.confirmThisPageType=1;//1.默认  2.等分  根据当前页数  和 滚动条高度等分
    options=$.extend(defaults,options);
    if(options.fixedWidth != null){
        defaults.isZoomRate=false;
    }

    if(options.imageDataArray.length < options.totalPageCount){
        for(var index=options.imageDataArray.length;index<options.totalPageCount;index++){
            options.imageDataArray.push(null);
        }
    }

    var $imageListDiv=$(this);
    var self=new Object(),service=new Object();
    /**
     * 加载单个图片成功后回调
     * @param url 图片路径
     * @param callback 加载完成后的回调函数
     */
    self.loadImage =function(url,callback,test){
        var bool=$imageListDiv.imageListData("isLoadUrl",url);
        if(bool){
            callback(url,test);
        }else{
            var img = new Image();
            img.src = url;
            // 图片存在缓存中
            if(img.complete){
                callback(url,test);
                return;
            }
            // 图片加载到浏览器的缓存中回调函数
            img.onload = function(){
                callback(url,test);
            }
        }
    }

    /**
     * 模板渲染
     * @param data 数据
     * @param parentDiv 父节点
     * @param template 模板
     */
    self.templateRender = function(data,parentDiv,template){
        data.thisZoomRate=options.thisZoomRate;
        laytpl(template).render(data, function(html){
            var scrollTop=$imageListDiv.scrollTop();
            parentDiv.append(html);
            $imageListDiv.scrollTop(scrollTop);
            if(typeof service.loadNextCall == "function"){
                service.loadNextCall(data.page);
            }
        });
    }

    /**
     * 打开加载
     */
    self.openLoad=function(page){
        var $div=$("<div></div>");
        $div.width($imageListDiv.width());
        $div.append("<img>");
        $div.children("img").attr("src",defaults.loadImagePath);
        if(page != null){
            $div.addClass("loadImageShowPage");
            var height=$imageListDiv.find("#"+page).height();
            $div.find("img").css("margin-top",height/2+"px");
            $imageListDiv.find("#"+page+" .image").append($div);
        }else{
            $div.addClass("loadImageShow");
            var scrollTop=$imageListDiv.scrollTop();
            $imageListDiv.append($div);
            $div.offset({
                left:$imageListDiv.offset().left,
                top:$imageListDiv.height()-54
            })
            $imageListDiv.scrollTop(scrollTop);
        }
    }
    /**
     * 关闭加载
     */
    self.closeLoad=function(page){
        if(page != null){
            if($imageListDiv.find("#"+page+" .loadImageShowPage").length > 0){
                $imageListDiv.find("#"+page+" .loadImageShowPage").remove();
            }
            return;
        }else{
            $imageListDiv.children("div.loadImageShow").remove();
        }
    }
    /**
     * 加载下一个图片
     * @param callback 回调
     */
    self.loadNext=function(options,defaults,callback){
        self.LoadBool=false;
        if(options.imageDataArray.length == defaults.thisLoadItem){
            self.LoadBool=true;
            return;
        }

        if(options.imageDataArray.length == $imageListDiv.find("li.imageItemLi").length){
            defaults.thisLoadItem=options.imageDataArray.length;
            self.closeLoad();
            self.LoadBool=true;
            return;
        }
        var call=function(){
            var data=options.imageDataArray[defaults.thisLoadItem];
            if(data == null){
                self.LoadBool=true;
                return;
            }
            self.openLoad();
            if(defaults.thisLoadItem != 0){
                data.top=options.top;
            }else{
                data.top=0;
            }
            self.loadImage(data.imagePath,function(url,data){
                if($imageListDiv.find("#"+data.id).length == 0){
                    self.templateRender(data,$imageListDiv.children(".imageUl"),$("#"+options.imageItemtemplate).text());
                    defaults.thisLoadItem++;
                }
                self.LoadBool=true;
                if(typeof callback == "function"){
                    callback(options,defaults);
                }
                self.closeLoad();
            },data);
        }

        $imageListDiv.imageListData("isWait",{
            page:defaults.thisLoadItem+1,
            callback:call
        });
    }
    /**
     * 加载到第几页
     * @param count 数据
     * @param callback 回调函数
     */
    self.loadToPage=function(count,callback){
        if(!(options.imageDataArray.length >= count)){
            return;
        }

        if((options.imageDataArray.length-1) < defaults.thisLoadItem){
            if(typeof callback == "function"){
                callback();
            }
            return;
        }

        if(defaults.thisLoadItem > (count-1)){
            if(typeof callback == "function"){
                callback();
            }
            return;
        }

        //判断数据是否加载
        var callPage=function(){
            for(var index in options.imageDataArray){
                if(index == (parseInt(count))){
                    return;
                }
                var item=options.imageDataArray[index];
                if(item == null){
                    $imageListDiv.imageListData("isWait",{
                        page:(parseInt(index)+1),
                        callback:callPage
                    });
                    return;
                }else if(parseInt(item.page) == parseInt(count)){
                    for(var i=defaults.thisLoadItem;i<options.imageDataArray.length;i++){
                        var data1=options.imageDataArray[defaults.thisLoadItem];
                        if(data1 == null){continue;}
                        self.loadImage(data1.imagePath,function(url,test){
                            if((count-1) == test){//所有图片均已加载完成
                                self.LoadBool=true;
                                var addCount=1;
                                for(var i1=defaults.thisLoadItem;i1<options.imageDataArray.length;i1++){
                                    var data=options.imageDataArray[defaults.thisLoadItem];
                                    if(data == null){continue;}
                                    if(defaults.thisLoadItem != 0){
                                        data.top=options.top;
                                    }else{
                                        data.top=0;
                                    }
                                    self.templateRender(data,$imageListDiv.children(".imageUl"),$("#"+options.imageItemtemplate).text());
                                    defaults.thisLoadItem++;
                                    addCount++;
                                }
                                if(typeof callback == "function"){
                                    setTimeout(function(){
                                        callback();
                                        self.closeLoad();
                                    },15*addCount);
                                }else{
                                    self.closeLoad();
                                }
                            }
                        },i);
                    }
                }
            }
        }
        callPage();
        self.openLoad();


    }

    /**
     * 放大
     */
    self.enlarge=function(){
        defaults.thisZoomRate=defaults.thisZoomRate+0.1;
        self.sizeChange();
    }

    /**
     * 缩小
     */
    self.narrow=function(){
        defaults.thisZoomRate=defaults.thisZoomRate-0.1;
        if(defaults.thisZoomRate < 0.2){
            defaults.thisZoomRate=defaults.thisZoomRate+0.1;
            return;
        }
        self.sizeChange();
    }

    /**
     * 大小发生变化
     */
    self.sizeChange=function(){
        var scrollTopRate=$imageListDiv.scrollTop()/$imageListDiv[0].scrollHeight;
        for(var index in options.imageDataArray){
            if(options.imageDataArray[index] == null){
                continue;
            }
            options.imageDataArray[index].thisZoomRate=defaults.thisZoomRate;
            var item = options.imageDataArray[index];
            $imageListDiv.find("#"+item.id+" > div.imageItemDiv").css({
                "width":item.width/item.zoomRate*defaults.thisZoomRate+"px",
                "height":item.height/item.zoomRate*defaults.thisZoomRate+"px"
            });
        }

        if(typeof options.sizeChange == "function"){
            options.sizeChange(scrollTopRate);
        }

        //判断是否需要加载下一页
        var _height=$imageListDiv.children("ul.imageUl").height();
        if($imageListDiv.height() > _height){
            self.loadNext(options,defaults);
        }
    }




    if(options.confirmThisPageType == 1){
        /***
         * 索引到第几页
         * @param count 页数
         */
        self.indexToCountPage=function(count,callback){
            var call=function(){
                for(var index in options.imageDataArray){
                    var item = options.imageDataArray[index];
                    if(item == null){continue;}
                    if(item.page == count){
                        var scrollTop=$imageListDiv.scrollTop();
                        if($imageListDiv.find("#"+item.id).length == 0){continue;}
                        var top=$imageListDiv.find("#"+item.id).offset().top;
                        $imageListDiv.scrollTop(scrollTop+top);
                        if(typeof self.showThisPage == "function"){
                            self.showThisPage(item.page);
                        }
                        if(typeof callback == "function"){
                            callback();
                        }
                        break;
                    }
                }
            }
            self.loadToPage(count,call);
        }
        /**
         * 确定当前页
         */
        self.confirmThisPage=function(){
            //页面的顶部线 ，显示区的中间线 ,离显示区中间线最近，就是当前页
            var center=$imageListDiv.height();
            var itemTest,mathAbs=0;
            for(var index in options.imageDataArray){
                var item = options.imageDataArray[index];
                if(item == null){continue;}
                if($imageListDiv.scrollTop() == 0 && index == "0"){
                    itemTest=item;
                    break;
                }

                if(parseInt(index) <= (defaults.thisLoadItem-1)){
                    var $ele=$imageListDiv.find("#"+item.id);
                    if($ele.length == 0){continue;}
                    var top=$ele.offset().top;
                    var height=$ele.height();
                    if((center-height) < top && top<center){
                        itemTest=item;
                        break;
                    }
                }
            }
            if(typeof self.showThisPage == "function" && itemTest != null){
                self.showThisPage(itemTest.page);
            }
        }
    }else if(options.confirmThisPageType == 2){
        /**
         * 确定当前页
         */
        self.confirmThisPage=function(){
            //等分
            var scrollTop=$imageListDiv.scrollTop();
            var maxScrollTop=$imageListDiv.find("ul").height() - $imageListDiv.height();
            var maxHeight=$imageListDiv.find("ul").height();
            self.heightScrollRate=maxScrollTop/maxHeight;//每个高度所占的滑动比
            var itemTest,height=0;
            for(var index in options.imageDataArray){
                var item = options.imageDataArray[index];
                if(item == null){continue;}
                if($imageListDiv.scrollTop() == 0 && index == "0"){
                    itemTest=item;
                    break;
                }

                if(parseInt(index) <= (defaults.thisLoadItem-1)){
                    var $ele=$imageListDiv.find("#"+item.id);
                    if($ele.length == 0){continue;}
                    if(parseInt(item.page) == options.imageDataArray.length){
                        itemTest=item;
                        break;
                    }
                    var thisHeight=height+$ele.height()+options.top;
                    if( height/maxHeight < thisHeight/maxHeight &&  scrollTop/maxScrollTop  < thisHeight/maxHeight){
                        itemTest=item;
                        break;
                    }
                    height=height+$ele.height()+options.top;
                }
            }

            if(typeof self.showThisPage == "function" && itemTest != null){
                self.showThisPage(itemTest.page);
            }
        }

        /**
         * 计算高度与滚动距离的比率
         */
        self.count=function(){
            var maxScrollTop=$imageListDiv.find("ul").height() - $imageListDiv.height();
            var maxHeight=$imageListDiv.find("ul").height();
            self.heightScrollRate=maxScrollTop/maxHeight;//每个高度所占的滑动比
        }

        /***
         * 索引到第几页
         * @param count 页数
         */
        self.indexToCountPage=function(count,callback){
            var call=function(){
                var scrollTop=$imageListDiv.scrollTop();
                var maxScrollTop=$imageListDiv.find("ul").height() - $imageListDiv.height();
                var maxHeight=$imageListDiv.find("ul").height();
                var itemTest,height=0;
                self.count();
                for(var index in options.imageDataArray){
                    var item = options.imageDataArray[index];
                    if(item == null){continue;}
                    var $ele=$imageListDiv.find("#"+item.id);
                    if(parseInt(item.page) == options.imageDataArray.length){
                        height=height+$ele.height();
                    }else{
                        height=height+$ele.height()+options.top;
                        /*var eleTop=$ele.offset().top;
                        var top=$imageListDiv.offset().top;
                        height=height+(eleTop - top);*/
                    }
                    if(item.page == count){
                        height=height-$ele.height()/2;
                        if(parseInt(item.page) == 1){
                            $imageListDiv.scrollTop(0);
                        }else{
                            $imageListDiv.scrollTop(self.heightScrollRate*height);
                        }
                        if(typeof self.showThisPage == "function"){
                            self.showThisPage(item.page);
                        }
                        if(typeof callback == "function"){
                            callback();
                        }
                        break;
                    }
                }
            }
            self.loadToPage(count,call);
        }
    }

    /**
     * 根据固定宽度计算高度
     * @param fixedWidth 固定宽度
     * @param width 原始宽度
     * @param width 原始高度
     */
    self.fixedWidthCountHeight=function(fixedWidth,width,height){
        return fixedWidth/width*height;
    }

    /**
     * 按页数排序
     * @param imageDataArray
     */
    self.orderByPage=function(imageDataArray){
        for(var index in imageDataArray){
            for(var _index=index; _index < imageDataArray.length; _index++){
                var _item=imageDataArray[_index];
                var item=imageDataArray[index];
                if(item == null || _item == null){continue;}
                if(parseInt(item.page) > parseInt(_item.page)){
                    imageDataArray[index]=_item;
                    imageDataArray[_index]=item;
                }
            }
        }
        return imageDataArray;
    }


    self.imageScroll=function(defaults,options,ele){
        self.confirmThisPage();
        //console.log("scrollTop:"+ele.scrollTop());
        if(self.LoadBool){
            if (ele.scrollTop() + ele.height()+4 >=  ele[0].scrollHeight) {
                var $imageListDivT=ele;
                self.loadNext(options,defaults,function(options,defaults){
                    if(typeof self.showThisPage == "function"){
                        self.showThisPage(defaults.thisLoadItem-1);
                    }
                });
            }
        }

        //回调
        if(typeof service.scrollChange == "function"){
            service.scrollChange(defaults.thisPage);
        }
    }

    /**
     * 初始化
     */
    self.init = function(){
        //按页数排序
        options.imageDataArray=self.orderByPage(options.imageDataArray);
        $imageListDiv.append("<ul class='imageUl'/>");
        self.LoadBool=true;
        //能显示几页就加载几页
        var heightTotal=0,imageListDivHeight=$imageListDiv.height() == 0 ? document.documentElement.clientHeight:$imageListDiv.height();
        for(var index in options.imageDataArray){
            var item = options.imageDataArray[index];
            if(item == null){continue;}
            if(index == 0){
                heightTotal=item.height/item.zoomRate;
                if(options.fixedWidth != null){
                    heightTotal=self.fixedWidthCountHeight(options.fixedWidth,item.width,item.height);
                }
            }else{
                if(options.fixedWidth != null){
                    heightTotal=heightTotal+self.fixedWidthCountHeight(options.fixedWidth,item.width,item.height);
                }else{
                    heightTotal=heightTotal+item.height/item.zoomRate+options.top;
                }
            }

            if(options.loadPageCount != null){
                //1.按页数加载
                if(item.page == options.loadPageCount){
                    var call=function(){
                        if(defaults.thisLoadItem == index){
                            self.loadNext(options,defaults);
                            /*if(defaults.thisLoadItem == 0){
                            }*/
                            if( typeof options.loadPageCall == "function"){
                                options.loadPageCall(self);
                            }

                            //------------------滚动-----------------
                            /**/
                            /*var imageListDivScroll = new IScroll("#"+$imageListDiv.attr("id"));
                            imageListDivScroll.on("scrollStart",function(){
                                //不能拖动
                                console.log("scrollstart");
                                $(this).find("ul li").css("position","relative");
                            });
                            imageListDivScroll.on("scrollEnd",function(){
                                //不能拖动
                                console.log("scrollEnd");
                                $(this).find("ul li").css("position","initial");
                            });*/

                            //self.indexToCountPage(options.loadPageCount);
                            return;
                        }else{
                            self.indexToCountPage(1);
                        }
                        self.loadNext(options,defaults,call);
                    }
                    call();
                    break;
                }
            }else{
                //2.按高度加载
                if(imageListDivHeight < heightTotal || index == (options.imageDataArray.length-1)){
                    var call=function(){
                        if(defaults.thisLoadItem == index){
                            self.loadNext(options,defaults);
                            /*if(defaults.thisLoadItem == 0){
                            }*/
                            return;
                        }
                        self.loadNext(options,defaults,call);
                    }
                    call();
                    break;
                }
            }
        }

        $imageListDiv.bind("scroll",{options:options,defaults:defaults},function(event){
            //console.log("++++++++++++++++++"+new Date().getTime());
            if(self.imageScroll.timer != null){
                clearTimeout(self.imageScroll.timer);
            }
            //$(this).find("ul li").css("position","relative");
            var ele=$(this);
            var defaults=event.data.defaults;
            var options=event.data.options;
            self.imageScroll.timer = setTimeout(function(){
                self.imageScroll(defaults,options,ele);
                //ele.find("ul li").css("position","initial");
            },200);



            /*if(arguments.callee.timer) clearTimeout(arguments.callee.timer);
            arguments.callee.timer = setTimeout(function(){
                console.log("----------------------------"+arguments.callee.timer);
                var tops =0;
                if(document.body.scrollTop){
                    tops = document.body.scrollTop;
                }else{
                    tops = document.documentElement.scrollTop;
                }

                if(tops >= document.documentElement.scrollHeight - document.documentElement.clientHeight){

                }
            },20);*/

        });

        //放大缩小
        if(options.isZoomRate){
            var $div=$("<div></div>");

            /*var top=$imageListDiv.height()*0.90+$imageListDiv.offset().top;
            var left=$imageListDiv.width()*0.95+$imageListDiv.offset().left;*/

            $div.css({
                "float":"right",
                "height":"54px",
                "position":"absolute",
                "top":"90%",
                "left":"95%",
                "z-index":"2000"
            });

            $div.append("<div class='opeare add glyphicon glyphicon-plus-sign'/><br>");
            $div.append("<div class='opeare jian glyphicon glyphicon-minus-sign'/>");
            $imageListDiv.append($div);

            $imageListDiv.on("click",".opeare.add",function(){
                self.enlarge();
            });

            $imageListDiv.on("click",".opeare.jian",function(){
                self.narrow();
            });
        }

        //显示页数
        if(options.isPageShow){
            var $pageDiv=$("<div></div>");

            var _top=0;//$imageListDiv.height()*0.6;
            var _left=$imageListDiv.width()*0.5;

            $pageDiv.css({
                "position":"absolute",
                "text-align":"center",
                "width":"100%",
                "background-color":"#323639"
            });
            $pageDiv.append("<span contenteditable='true' class='thisPageCount' style='color:#ffffff;'>1</span>");
            $pageDiv.append("<span class='showTotal' style='color:#ffffff;'>/"+(options.imageDataArray.length)+"</span>")

            $imageListDiv.prepend($pageDiv);

            $imageListDiv.on("input",".thisPageCount",function(){
                if(!isNaN($(this).text()) && $(this).text() != "0" && $(this).text() != ""){
                    self.indexToCountPage(parseInt($(this).text()));
                }
            });
        }else if(typeof options.pageShowId == "string"){
            $("body").on("keydown","#"+options.pageShowId,function(e){
                // 兼容FF和IE和Opera
                var theEvent = e || window.event;
                var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
                if (code == 13) {
                    //回车执行查询
                    var val=$(this).val();
                    if(!isNaN(val) && val != "0" && val != "" && parseInt(val) <= options.totalPageCount){
                        defaults.renderPage=parseInt(val);
                        self.indexToCountPage(parseInt(val),function(){
                            if(typeof options.inputPageCall == "function"){
                                options.inputPageCall(parseInt(val));
                            }
                        });
                        return false;
                    }
                }
            });
        }
    }

    /***
     * 显示当前页数
     */
    self.showThisPage=function(count){
        defaults.thisPage=count;
        $imageListDiv.attr("thispage",count);

        var thisPageCount=$imageListDiv.children(".thisPageCount");
        if(thisPageCount.length == 1){
            thisPageCount.text(count);
        }else if(typeof options.pageShowId == "string"){
            $("#"+options.pageShowId).text(count);
        }
        if(options.isThisPageAddBorder){
            $imageListDiv.find("li.thisPage").removeClass("thisPage");
            for(var index in options.imageDataArray){
                var item =options.imageDataArray[index];
                if(item == null){continue;}
                if(parseInt(item.page) == parseInt(count)){
                    $imageListDiv.find("#"+item.id).addClass("thisPage");
                    break;
                }
            }
        }
        $imageListDiv.imageListData("thisPageChage",count);
        if(typeof options.pageShowId == "string"){
            $("#"+options.pageShowId).val(count);
        }

    }

    self=$.extend(self,options.self);
    self.init();

    /**
     * 取得当前页面放大的倍数
     */
    service.zoomRate=function(){
        return defaults.thisZoomRate;
    }

    /**
     * 索引到第几页
     */
    service.indexToCountPage=function(count,call){
        self.indexToCountPage(count,call);
    }

    /**
     * 取得当前页数
     */
    service.thisPage=function(){
        return defaults.thisPage;
    }

    /**
     * 取得当前页子节点的id
     */
    service.thisPageImageItemDivId=function(){
        var pageCount=defaults.thisPage;
        if(pageCount == null){
            pageCount=1;
        }

        for(var index in options.imageDataArray){
            var item = options.imageDataArray[index];
            if(item == null){continue;}
            if(parseInt(item.page) == pageCount){
                return $imageListDiv.find("#"+item.id+" div.image").attr("id");
            }
        }
    }
    /**
     * 加载所有的页面
     * @param 加载完成回调
     */
    service.loadAllPage=function(callback){
        self.indexToCountPage(options.loadPageCount,callback);
    }

    /**
     * 解除绑定事件
     */
    service.unbindEvent=function(){
        $imageListDiv.unbind("scroll",self.imageScroll);
    }


    /**
     * 签名成功
     * @param pages array
     */
    service.signSuccess=function(pages){
        for(var index in pages){
            var page=pages[index];
            self.openLoad(page);
        }
    }

    /**
     * 数据变化
     */
    service.imageDataArrayChange=function(index,data){
        options.imageDataArray[index]=data;
    }

    /**
     * 关闭当前页 更新加载
     */
    service.closeLoad=function(url,id){

        var ele=$imageListDiv.find("#"+id+" .image");
        if(ele.length > 0){
            if(ele[0].tagName == "IMG" || ele[0].tagName == "img"){
                ele.attr("src","");
                ele.attr("src",url);
                /*setTimeout(function(){
                    ele.css("background-image","url('"+url+"')");
                },100);*/
            }else{
                ele.css("background-image","url()");
                ele.css("background-image","url('"+url+"')");
                /*setTimeout(function(){
                    ele.css("background-image","url('"+url+"')");
                },100);*/
            }
            self.closeLoad(id);
        }
    }

    service.isPageLoad=function(page){
        var ele=$imageListDiv.find("#"+page);
        if(ele.length > 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 加载下一页
     */
    service.loadNext=function(callback){
        self.loadNext(options,defaults,callback);
    }

    /**
     * 判断是否加载完成
     */
    service.isLoadOrder=function(){
        if((defaults.thisLoadItem-1) == options.totalPageCount){
            return true;
        }
        return false;
    }

    //$imageListDiv.imageListLoad=service;
    $imageListDiv.data("imageListLoad",service);
    return service;
};

