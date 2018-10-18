/**
 * 区域 1 PFDNavigationName 
 * 区域 2 signatureHead 
 * 区域 3 navigation
 * 区域 4 clickSign
 * 区域 5 PFDNavigation
 * 
 * 	url	
 * 	urlRequestParam
 * 
 * map.put(PDFNAME,fileMap.get("fileName"));
		map.put("fileId",fileMap.get("fileId"));
		map.put("signFilePath", ObjectUtils.toString(resultMap.get("tempFilePath")));
		map.put("isTemplates", isTemplates);
 * 数据源返回数据格式
 * {stampList:[g_stamp1,g_stamp2],pdfImageList:[pdfImage1,pdfImage2],pdfName:"",signFilePath:"",signFilePath:"",isTemplates}
 * g_stamp后台对象
 * pdfImage后台对象
 * pdfNamePDF文件名称
 * signFilePath签章文件路径
 * fileId 文件ID  （可以使模板文件ID，g_file ID，g_file_recalation ID）
 * isTemplates true 是模板 false不是模板
 * 
 * 每个区域
 * 		模板
 * 		服务(return)
 *options 格式
 * 	PFDNavigationName
 * 		template
 * 			templateId,templateRenderingBackCall
 *  ...
 * 	url	
 * 	urlRequestParam
 */
//url
	
function LoadSignVisual(options){
	  	var browser=$.getBrowserVersion();
	    var ie=browser.version;
		if(browser.browser== 'IE' && ie != null && ie != '' && ie <7.0){
			layer.msg("不支持IE7一下的IE浏览器");
			return;
		}
		var PFDNavigationName=options.PFDNavigationName;//文件名区域
		var signatureHead=options.signatureHead;//头部区域
		var navigation=options.navigation;//签章区域
		var clickSign=options.clickSign;//触发签章区域
		var handWriteSign=options.handWriteSign;//手写签章区域
		var handBagWriteSign=options.handBagWriteSign;//手写板签章区域
		var PFDNavigation=options.PFDNavigation;//PDF图片导航区域
	    var uploadStamp=options.uploadStamp;//上传印章
		var url=options.url;//数据源链接
		var urlRequestParam=options.urlRequestParam;//数据源请求参数
		//数据模型
		var signModel=SignModel();
	    var signatureOperate=SignatureOperate(signModel);
	    var signAssemble=SignAssemble();
	    
	    //记录后台数据
	    var responseData=null;
	    
	    
	    
	    //注册回调事件  签章生成时回调
	    signatureOperate.add=function(id,signType,zoomRate){
	    	var sign=signModel.getkeySign(id);
	    	$("#select"+sign.signType).show();
	    }
	    //注册回调事件  签章删除时回调
	    signatureOperate.deleteEvent=function(id){
	    	var sign=signModel.getkeySign(id);
	    	var signArray=signModel.getAll();
	    	var count=0;
	    	for(var item in signArray){
	    		if(signArray[item] != null){
	    			if(sign.signType==signArray[item].signType){
		    			count++;
		    		}
	    		}
	    	}
	    	if(count==1){
	    		$("#select"+sign.signType).hide();
	    	}
	    }
		var signSystem=function(){}
		var utils=function(){}
		// layer.msg形式提示用户不要操作
		utils.hint=function(content){
			layer.msg(content);
		}
		utils.openCenter=function(options){
			Dialog.close();
			diag = new Dialog(options);
			diag.show();
		}
		//注册  changeSignOffset 函数
	    utils.changeSignOffset=function(){
	    	//var page=$("#pfdShowImgPng").attr("pdfPage");
	    	signSystem.countSignOffsetPosition();
	    }
		
		utils.width=1135;//最低能接受的宽度
		utils.fixedWidth=520;//固定宽度
		utils.signWidth=260;//签章导航区  和 pdf导航区  固定宽度
		utils.height=220;//最低能接受宽度
		utils.buttonHeight=50;//按钮固定高度
		if(intl.language=="en-US"){
			utils.buttonHeight=34*2+5;//按钮固定高度
		}
		utils.handWriteButtonHeight=50;//按钮固定高度
		utils.handBagWriteButtonHeight=50;//按钮固定高度
		utils.uploadStampButtonHeight=50;//按钮固定高度
		utils.scrollbarWidth=10;//滚动条的宽度
		utils.headFixedHeight=60;//头部固定高度
		utils.closePFDNavigation=12;
		utils.pdfPageMarginTop=60;
		/**
		 * 数据加载
		 */
		signSystem.load=function(urlRequestParamTest){
			if(urlRequestParamTest != null){
				urlRequestParam=urlRequestParamTest;
				$.ajax({
				   type: "POST",
				   url: url,
				   data: urlRequestParamTest,
		           contentType: false,
		           processData: false,
		           crossDomain: true,
				   success: function(json){
					   service.againTemplete(json);
				   }
				});
			}
		}
		/**
		 * @param options 四大区域之一
		 * @param templateData 模板数据
		 * @param templateParentId 模板父节点ID 
		 */
		signSystem.template=function(options,templateData,templateParentId){
			   signSystem.templateRendering(options.templateId,
					   templateData,
					   templateParentId,
					   options.templateRenderingBackCall);
		}
		
		/**
		 * @title html渲染
		 * @param templateId 模板ID
		 * @param templateData 模板数据
		 * @param templateParentId 模板父节点ID
		 * @param templateRenderingBackCall 模板渲染后的回调事件
		 **/
		signSystem.templateRendering=function(templateId,templateData,templateParentId,templateRenderingBackCall){
			var templateContent=$("#"+templateId).text();
			if(templateContent == null || templateContent == ''){
				templateContent=$("#"+templateId).html();
			}
			
			//在签章导航渲染时，如果页面存在签章这显示 勾
			if(templateParentId == "navigation"){
				for(var item in templateData){
					var template=templateData[item];
					if(template != null && typeof(template) == 'object'){
						if(signModel.isSignType(template.id)){
							template.isSignType=true;
						}
					}
				}
				
			}
			
			
			laytpl(templateContent).render(templateData, function(html){
				//$(html).load();
				//特殊处理
				if(templateParentId == "signatureHead"){
					var height=$(html).height();
					$("#"+templateParentId).height(height);
					utils.headFixedHeight=height;
                    $("#"+templateParentId).html("");
                }else if(templateParentId == "clickSign"){
					//IE 8 9 10 11 button的高度 与 css height 不一样
					utils.buttonHeight=parseInt($(html).css("height").replace("px",""));
				}else if(templateParentId == "handWriteSign"){
					utils.handWriteButtonHeight=parseInt($(html).css("height").replace("px",""));
				}else if(templateParentId == "uploadStamp"){
                    utils.uploadStampButtonHeight=parseInt($(html).css("height").replace("px",""));
                }else if(templateParentId == "handBagWriteSign"){
					utils.handBagWriteButtonHeight=parseInt($(html).css("height").replace("px",""));
				}else if(templateParentId == "PFDNavigation"){
					if(templateData != null && templateData.pdfImageList != null){
						for(var index in templateData.pdfImageList){
			            	 $("#pfdShowImgTest").imageListData("imageDataArrayChange",{index:index,data:templateData.pdfImageList[index]});
			    		}
						/*var imgPath=templateData[0].imagePath;
	                    //将图片设置为背景
	                    imgPath=$.replaceAll(imgPath,"\\","/");
	                    $("#pfdShowImgPng").css("background-image",('url('+imgPath+')'));
	                    $("#pfdShowImgPng").attr("pdfPage",parseInt(templateData[0].page)+1);
	                    $("#pfdShowImgPng").show();
	                    */
						var _options={};
                        _options.imageDataArray=templateData.pdfImageList;
                        _options.totalPageCount=templateData.totalPageCount;
                        _options.loadPageCount=templateData.totalPageCount < 3 ? templateData.totalPageCount : 3;
                        _options.isPageShow=false;
                        _options.sizeChange=function(scrollTopRate){
			            	signSystem.countSignOffsetPosition("other",scrollTopRate);
			            }
                        _options.confirmThisPageType=2;//如果有固定宽度，将不按本身的比例计算,放大缩小将无效
			            var imageListLoad=$("#pfdShowImgTest").data("imageListLoad");
			            var thisPage=null,scrollTop=null;
			            if(imageListLoad != null){
			            	scrollTop=$("#pfdShowImgTest").scrollTop();
			            	var _page=parseInt(imageListLoad.thisPage() == null ? "1" : imageListLoad.thisPage());
                            _options.indexToCountPage=_page;
                            _options.thisZoomRate=imageListLoad.zoomRate();
                            _options.loadPageCount=(_options.imageDataArray.length == _page ? _page : (_page+1))+"";//默认加载页数
                            _options.scrollTop=scrollTop;
                            _options.loadPageCall=function(self){
			            		self.indexToCountPage(_options.indexToCountPage);
			            		$("#pfdShowImgTest").scrollTop(_options.scrollTop);
			            	}
			            	imageListLoad.unbindEvent();
			            }
			            $("#pfdShowImgTest").html("");
                        _options.loadImagePath=options.loadImagePath;
			            $("#pfdShowImgTest").imageListLoad(_options);
			            
			            
			            
			           
					}else{
						return;
					}
					var _options={};
					_options.imageDataArray=templateData.pdfImageList;
		            _options.totalPageCount=templateData.totalPageCount;
		            _options.isPageShow=false;
					_options.imageItemtemplate="imagePageRightModel";
					_options.fixedWidth=173;//如果有固定宽度，将不按本身的比例计算,放大缩小将无效
					_options.confirmThisPageType=2;//如果有固定宽度，将不按本身的比例计算,放大缩小将无效
					_options.loadPageCount=templateData.totalPageCount < 3 ? templateData.totalPageCount : 3;
					_options.inputPageCall=function(page){
                        $("#pfdShowImgTest").data("imageListLoad").indexToCountPage(page);
					}
					var imageListLoad=$("#PFDNavigationRight").data("imageListLoad"); 
		            var thisPage=null,scrollTop=null;
		            if(imageListLoad != null){
		            	scrollTop=$("#PFDNavigationRight").scrollTop();
		            	var _page=parseInt(imageListLoad.thisPage() == null ? "1" : imageListLoad.thisPage());
		            	_options.indexToCountPage=_page;
		            	_options.thisZoomRate=imageListLoad.zoomRate();
		            	_options.loadPageCount=(_page == _options.imageDataArray.length  ? _page : (_page+1))+"";//默认加载页数
		            	_options.scrollTop=scrollTop;
		            	_options.loadPageCall=function(self){
		            		self.indexToCountPage(_options.indexToCountPage);
		            		$("#PFDNavigationRight").scrollTop(_options.scrollTop);
		            	}
		            	imageListLoad.unbindEvent();
		            }
                    _options.pageShowId="pdfPage";
                    _options.loadImagePath=options.loadImagePath;
                    $("#PFDNavigationRight").html("");
		            $("#PFDNavigationRight").imageListLoad(_options);
				}else if(templateParentId == "navigation"){
					$("#"+templateParentId).html("");
				}
				if(templateParentId != "PFDNavigation"){
					$("#"+templateParentId).append(html);
				}
				if(templateParentId == "navigation"){
					if(browser.browser == 'Chrome' || browser.browser == 'IE'){
				    	 $(".navigation").find("img[img_data]").css("cursor","move");
				    	 if(browser.browser == 'IE' && browser.version >9.0){
							 $(".navigation").find("img[img_data]").css("cursor","move");
						 }else{
							 $(".navigation").find("img[img_data]").css("cursor","hand");
						 }
				     }else{
						 $(".navigation").find("img[img_data]").css("cursor","hand");
					 }
				}
				if(templateRenderingBackCall != null && typeof(templateRenderingBackCall) == "function"){
					templateRenderingBackCall(templateData);
				}
				signSystem.screenChange({});
			});
		}
		/**
		 * @title 系统事件绑定
		 */
		signSystem.eventBind=function(){
			//滚轮事件监听
		    $("#pfdShowImgTest").scroll(function(){
				//utils.changeSignOffset();
		    });
		    //窗口变动时监听
			$(window).resize(function () {          //当浏览器大小变化时
				signSystem.screenChange({});
			});
		}
		/**
		 * @title 屏幕发生变化
		 */
		signSystem.screenChange=function(options){
			$(".signatureBodyDiv").css("margin-top",utils.headFixedHeight+"px");
			$(".PFDShow").css("margin-top",utils.headFixedHeight+"px");
			$("#PFDNavigation").css("margin-top",utils.headFixedHeight+"px");
			
			var defaults={};
			var options={};
			defaults.width=document.documentElement.clientWidth;//documentElement
			defaults.height=document.documentElement.clientHeight;
    		if(defaults.width<utils.width){
    			$("html").css("overflow-x","auto");
    		}else{
    			$("html").css("overflow-x","hidden");
    		}
    		
			if(defaults.height<utils.height){
    			$("html").css("overflow-y","auto");
    		}else{
    			$("html").css("overflow-y","hidden");
    		}
			options=$.extend(defaults,options);
			var height=options.height;
			var width=options.width;
			var param={}
			//宽度 解决方案
			if(width<utils.width){
				width=utils.width;
			}
			//屏幕宽度 width
			var widthTest=width-utils.signWidth-utils.closePFDNavigation;
			param.width=widthTest+"px";
			$(".PFDShow").css(param);
			$(".PFDShow").css("margin-left",utils.signWidth+"px");
			$(".PFDShowTest").css(param);
			param.width=width+"px";
			$(".signatureHead").css(param);
			//高度解决方案 height
			if(height<utils.height){
				height=utils.height;
			}
			param={}
			if(handWriteSign == null){
				utils.handWriteButtonHeight=0;
			}
			if(handBagWriteSign == null){
				utils.handBagWriteButtonHeight=0;
			}
            if(uploadStamp == null){
                utils.uploadStampButtonHeight=0;
            }

			var heightTest=height-(utils.headFixedHeight+utils.buttonHeight
					+utils.handWriteButtonHeight+utils.handBagWriteButtonHeight+utils.uploadStampButtonHeight);
			
			param.height=heightTest+"px";
			$(".navigation").css(param);
			
			param.height=height-utils.headFixedHeight+"px";
			$(".signatureBodyDiv ").css(param);
			
			heightTest=height-utils.headFixedHeight;
			param.height=heightTest+"px";
			$(".PFDShowTest").css(param);
			
			heightTest=height-utils.headFixedHeight;
			param.height=heightTest+"px";
			$(".PFDbody").css(param);
			//高度 宽度 都要改变
			param={};
			param.height=height+"px";
			param.width=width+"px";
			$(".signatureBody").css(param);
			
			//计算位置
			var countleft=width-utils.fixedWidth+utils.signWidth;
			var countTop=utils.headFixedHeight;
			param={};
			param.left=countleft;
			param.top=countTop;
			//默认关闭
			param.left=countleft+(utils.signWidth-utils.closePFDNavigation);
			$(".PFDNavigationName").offset(param);
			$(".PFDNavigation").offset(param);
			$(".openPFDNavigation").show();
			$(".openPFDNavigation > div").css("height",(height-utils.headFixedHeight)+"px");
			$("#PFDNavigationRight").css("height",(height-utils.headFixedHeight-utils.pdfPageMarginTop)+"px");
			$("#PFDNavigationRight").css("margin-top",(utils.pdfPageMarginTop)+"px");
            $(".PFDNavigationName").css("height",(utils.pdfPageMarginTop)+"px");
			//计算签章浏览器坐标
			if(utils.changeSignOffset != null && typeof(utils.changeSignOffset) == "function"){
				utils.changeSignOffset();
			}
		}
		/**
		 * 页面变化时调用
		 * @parem type
		 * 		other  页面放大缩小
		 */
		signSystem.countSignOffsetPosition=function(type,scrollTopRate){
			var startTime=new Date().getTime();
			//console.log("countSignOffsetPosition "+startTime+"   scrollTopRate"+scrollTopRate);
			//1.判断当前页是否有签章存在
	    	var signatureArray=signModel.getAll();
	    	var bool=false;
	    	for(var item in signatureArray){
	    		var sign=signatureArray[item];
	    		if(sign != null && typeof(sign) == "object"){
	    			bool=true;
	    			break;
	    		}
	    	}
	    	//2.移动当前页所有签章的位置
	    	//按倍数放大浏览器上的坐标，真实坐标改变 记录上次放大倍数
	    	var rate,uprate=1;
	    	if(signatureArray != null || signatureArray.length >0){
	    		if($("#pfdShowImgTest").data("imageListLoad") != null){
	    			rate=$("#pfdShowImgTest").data("imageListLoad").zoomRate();
		        	if(typeof $("#pfdShowImgTest").attr("uprate") == "string"){
		        		uprate=Number($("#pfdShowImgTest").attr("uprate"));
		        	}
		        	$("#pfdShowImgTest").attr("uprate",rate);
	    		}
	    	}
	    	var $pfdShowImgTest=$("#pfdShowImgTest");
	    	for(var item in signatureArray){
	    		var sign=signatureArray[item];
	    		if(sign != null && typeof(sign) == "object"){
	    			//debugger;
	    	        var pdf_x=sign.pdf_x;//PDF上的 X 坐标位置
	    	        var pdf_y=sign.pdf_y;//PDF上的 Y 坐标位置
	    	        var signEleId=sign.signEleId;//签章ID
	    	        var signEle=$("#"+signEleId);
	    	        if(signEle.length > 0){
		    	        if(type == "other"){
		    	        	//按比例滚动滚动条
		    	        	signSystem.changePosition(uprate,rate,sign);
		    	        	signSystem.signRate(rate,sign);
		    	        	var scrollTop=$pfdShowImgTest[0].scrollHeight*scrollTopRate;
		    	        	$pfdShowImgTest.scrollTop(scrollTop);
		    	        }else{
		    	        	var offset=signEle.parent().children(".imageItemDiv").offset();
			    	        var resultJson=SignatureOperate.getBrowserOffset(offset.top,offset.left,$pfdShowImgTest.scrollTop(),pdf_x,pdf_y);
			    	        //从新设置签章位置
			    	        offset={};
			    	        offset.top=resultJson.offset_top;
			    	        offset.left=resultJson.offset_left;
			    	        signEle.offset(offset);
		    	        }
	    	        }
	    	        
	    		}
	    	}
	    	
	    	var endTime=new Date().getTime();
		}
		
		/**
		 * 视觉上放大签章
		 * @param rate 页面放大倍数
		 * @param 签章属性
		 */
		signSystem.signRate=function(rate,sign){
	        var signEle=$("#"+sign.signEleId);
	        //大小 倍数放大图片，真实的大小不变
            var img=signEle.find("div[sign]");
            var heightdata=$.parseJSON(img.attr("heightdata"));
            var _height=signEle.height()-img.height();
            var _width=signEle.width()-img.width();
            
            rate=sign.zoomRate*rate;
            var height=heightdata.height*rate;
            var width=heightdata.width*rate;
            
            
            //特殊处理 --水印
            var imgData=$.parseJSON($("#"+sign.signType).attr("img_data"))
            if(imgData.stampType == "3"){
            	var imageItemDiv=signEle.parent().children(".imageItemDiv");
            	height=imageItemDiv.height();
            	width=imageItemDiv.width();
            }
            
        	img.css({
        		"height":height+"px",
        		"width":width+"px",
        		"background-size":width+"px "+height+"px"
            });
        	
        	signEle.css({
        		"height":(height+_height)+"px",
        		"width":(width+_width)+"px"
            });
		}
		
		/**
		 * 改变坐标位置
		 * @param uprate 页面上次放大倍数
		 * @param rate 当前放大的倍数
		 * @param sign 签章数据
		 */
		signSystem.changePosition=function(uprate,rate,sign){
			var pdf_x=sign.pdf_x;//PDF上的 X 坐标位置
 	        var pdf_y=sign.pdf_y;//PDF上的 Y 坐标位置
 	        pdf_x=pdf_x/uprate*rate;
 	        pdf_y=pdf_y/uprate*rate;
	        var signEle=$("#"+sign.signEleId);
	        var offset=signEle.parent().parent().offset();
	        var resultJson=SignatureOperate.getBrowserOffset(offset.top,offset.left,$("#pfdShowImgTest").scrollTop(),pdf_x,pdf_y);
	        offset={};
	        offset.top=resultJson.offset_top;
	        offset.left=resultJson.offset_left;
	        signEle.offset(offset);
	        signModel.editSign(sign.signEleId,pdf_x,pdf_y);
		}
		
		//注册服务
		var service=new Object();
	   	service.createSign=function(_options){
	   		var defaultOptions={}
	   		if(_options.imageItemDivId != null){
	   			defaultOptions.signRegion=_options.imageItemDivId;
                _options.addSignSrc=options.addSignSrc;
                _options.reduceSignSrc=options.reduceSignSrc;
                _options.deleteSignSrc=options.deleteSignSrc;
	   		}else{
	   			defaultOptions.signRegion=$("#pfdShowImgTest").data("imageListLoad").thisPageImageItemDivId();
	   		}
            _options=$.extend(defaultOptions,_options);
            _options.page=defaultOptions.signRegion.replace("image_page_","");
	    	signAssemble.createSign(_options,SignatureOperate);
	   	}
	   	
	   	//触发签章区域
	   	service.getSignArrays=function(){
	   		var signatureArrays=signModel.getAll();
			var data={};
			data.signatureArray=[];
			var bool=false;
			for(var item in signatureArrays){
				var sign=signatureArrays[item];
				if(sign != null && typeof(sign) == "object"){
					bool=true;
					sign.id=sign.signType;
					data.signatureArray.push(sign);
				}
			}
			return {signatureArrays:signatureArrays,bool:bool};
	   	}
	   	
	   	//PDF图片导航区域== 已作废
	   	service.changePDFImageShow=function(imgPath,upPage,nextPage){
	        var pfdShowImgPng=$("#pfdShowImgPng");
	        //更换背景图片路径
			pfdShowImgPng.css("background-image",('url('+imgPath+')'));
	        //隐藏上一页所有签章
	        var signArray=signModel.getSign(upPage);
	        for(var item in signArray){
	            var sign=signArray[item];
	            if($("#"+sign.signEleId).length > 0){
	            	$("#"+sign.signEleId).hide();
	            }
	        }
	        //显示下一页所有签章
	        signArray=signModel.getSign(nextPage);
	        for(var item in signArray){
	            var sign=signArray[item];
	            if($("#"+sign.signEleId).length > 0){
	            	$("#"+sign.signEleId).show();
	            }
	        }
	        //计算下一页坐标
	        signSystem.countSignOffsetPosition(nextPage);
	        //设置页数
	        pfdShowImgPng.attr("pdfPage",nextPage);
	   	}
	   	
	   	signSystem.screenChange({});
	   	
	   	/**
	   	 * 删除所有类型的sign
	   	 */
	   	service.removeSignType=function(signType){
	   		var signatureArrays=signModel.getAll();
	   		var keys=[];
	   		for(var item in signatureArrays){
	   			var sign=signatureArrays[item];
	   			if(sign != null && sign.signType == signType){
	   				SignatureOperate.deleteEvent(sign.signEleId);
	   				$("#"+sign.signEleId).remove();
	   			}
	   		}
	   	}
	   	
	   	/**
	     * 取得最高层
	     */
	   	service.getMaxZ_index=function(){
	    	return signModel.getMaxZ_index();
	    }
	   	/**
	   	 * 重新渲染导航区域
	   	 */
	   	service.againRenderingNavigation=function(newData,bool){
	   			//渲染PFDNavigationName
			   var templateData=responseData.stampList;
			   if(typeof newData != "object"){
				   return;
			   }
			   if(newData.length >= 1){
				   for(var item in newData){
					   templateData.push(newData[item]);
				   }
			   }else{
				   templateData.push(newData);
			   }
			   if(bool == null){
                   signSystem.template(navigation.template,templateData,"navigation");
			   }else if(navigation.template != null && typeof navigation.template.templateRenderingBackCall == "function" && bool != null && !bool){
                   navigation.template.templateRenderingBackCall(templateData);
			   }
	   	}
	   	
		/**
	   	 * 重新渲染导航区域
	   	 */
	   	service.againRenderingNavigationWithNoRepeat=function(newData){
	   			//渲染PFDNavigationName
			   var templateData=responseData.stampList;
			   var hasSame = false;
			   for(var item in templateData){
				   var template=templateData[item];
				   if(template != null && typeof(template) == "object"){
					   if(template.id == newData.id){
						   hasSame = true;
					   }
				   }
			   }
			   if( !hasSame){
				   templateData.push(newData);
			   }
			   signSystem.template(navigation.template,templateData,"navigation");
	   	}
	   	
	   	/**
	   	 * 判断pdf上是否已经有签章
	   	 */
		service.checkHasSign=function(options){
	   		var defaultOptions={}
	   		defaultOptions.signRegion="pfdShowImgTest";
			options=$.extend(defaultOptions,options);
			var signTotalCount=SignatureOperate.getSignTypeCount(options.signType);
			if(signTotalCount > 0 ){
				return true;
			}
			return false;
	   	}
		/**
		 * 删除在pdf上的所有章
		 */
		service.removeAllSignInPdf=function(data){
			 //保留最后一个章在pdf上
			 for(var j = 0 ; j<data.length;j++){
				 var g = data[j];
				 service.removeSignType(g.id);
				 $("#select"+g.id).hide();
			 }
		}
	   	/**
	   	 * 删除签章重新渲染导航
	   	 */
	   	service.removeSignRenderingNavigation=function(id){
	   			//渲染PFDNavigationName
			   var templateData=responseData.stampList;
			   var newData=[];
			   for(var item in templateData){
				   var template=templateData[item];
				   if(template != null && typeof(template) == "object"){
					   if(template.id != id){
						   newData.push(template);
					   }
				   }
			   }
			   responseData.stampList=newData;
			   signSystem.template(navigation.template,newData,"navigation");
	   	}
	   	
	   	
	   	/**
	   	 * 返回原始数据判断是否重复插入资质章
	   	 */
	   	service.isCreStamps=function(){
	   		var signatureArrays=signModel.getAll();
			var templateData=responseData.stampList;
			var count=0;
            	for(var item in signatureArrays){
    	   			var sign=signatureArrays[item];
    	   		 if(sign!=null){
    	   			for(var ite in templateData){
    	   				var g_Stamp=templateData[ite];
    	   				if(g_Stamp.id==sign.signType && g_Stamp.corFlag=='2'){
    	   					count++;
    	   				}
    	   			}
    	   		}
            }
	   		
	   		return count;
	   	}
	   	
	   	
		
	   	/**
	   	 * 返回原始数据判断是否重复插入水印
	   	 */
	   	service.isWatermarkStamps=function(){
	   		var signatureArrays=signModel.getAll();
			var templateData=responseData.stampList;
			var count=0;
            	for(var item in signatureArrays){
    	   			var sign=signatureArrays[item];
    	   		 if(sign!=null){
    	   			for(var ite in templateData){
    	   				var g_Stamp=templateData[ite];
    	   				if(g_Stamp.id==sign.signType && g_Stamp.corFlag=='4'){
    	   					count++;
    	   				}
    	   			}
    	   		}
            }
	   		
	   		return count;
	   	}
	   	
	   	/**
	     * 加载单个图片成功后回调
	     * @param url 图片路径
	     * @param callback 加载完成后的回调函数
	     */
	   	service.loadImage =function(url,callback,test){
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


	    var toData=function(responseData){
	   		var signFileInfo=responseData.signFileInfo;
            for(var key in signFileInfo){
            	var value=signFileInfo[key];
            	responseData[key]=value;
			}
	   		delete responseData["signFileInfo"];
            return responseData;
		}

	   	/**
	   	 * 数据重新渲染
	   	 */
	   	service.againTemplete=function(json){
	   		if(typeof(json) != "object"){
				   try { 
					   json=$.parseJSON(json);
				   } catch (e) { 
					   layer.msg(json);
					   return;
				   } 
			   }
			   if(json.resultCode == -1){
				   layer.msg(json.message);
				   setTimeout(function(){
					   $.colosLayerIfram();
				   },3000);
				   return false;
			   }
			   json=json.data;
			   responseData=toData(json);
			   
			   var call=function(){
				   $(".loadHintFont").remove();
				   //渲染PFDNavigationName
				   var templateData={};
				   templateData.pdfName=json.fileName;
				   templateData.signFilePath=json.filePath;
				   templateData.fileId=json.fileId;
				   templateData.isTemplates=false;
				   templateData.pdfNameStr=json.fileNameStr;
                   templateData.totalPageCount=json.totalPageCount;
                   signSystem.template(PFDNavigationName.template,templateData,"PFDNavigationName");
				   
				   //渲染signatureHead
				   templateData={};
				   templateData.title=json.title;
                   templateData.blueDigital=json.blueDigitalStr;
				   signSystem.template(signatureHead.template,templateData,"signatureHead");
	
				   //渲染PFDNavigationName
				   templateData=json.stampList;
				   //根据原始大小 75 计算出倍率
				   for(var index in templateData){
					   var stamp=templateData[index];
					   if(stamp != null){
						   var imgHeight=stamp.imgHeight;
						   var imgWidth=stamp.imgWidth;
						   if(imgHeight > 75 || imgWidth > 75){
							   if(imgHeight >= imgWidth){
								   stamp.zoomRate=parseInt((imgHeight/75)*10)/10;
								   stamp.imgHeight=75;
								   stamp.imgWidth=parseInt(imgWidth/(imgHeight/75));
							   }else{
								   stamp.zoomRate=parseInt((imgWidth/75)*10)/10;
								   stamp.imgWidth=75;
								   stamp.imgHeight=parseInt(imgHeight/(imgWidth/75));
							   }
						   }
						   stamp.floatImgHeight=stamp.imgHeight;
						   stamp.floatImgWidth=stamp.imgWidth;
					   }
				   }
				   
				   signSystem.template(navigation.template,templateData,"navigation");
				   
				   //渲染clickSign
				   templateData={};
				   templateData.blueDigital=json.blueDigitalStr;
				   templateData.hasSm2Signed = json.hasSm2Signed;
				   templateData.hasRSASigned = json.hasRSASigned;
				   templateData.hasPublicCerSign = false;
				   signSystem.template(clickSign.template,templateData,"clickSign");
				   $("#clickSign").on("img","mousemove",function(){return false;});
				   
				   //渲染PFDNavigation
				   templateData={};
				   templateData.pdfImageList=json.pdfImageList;
				   templateData.totalPageCount=json.totalPageCount;
				   if(json==null || templateData == null || templateData.length == 0){
					   layer.msg(intl.signTemplate_configFile);
				   }
				   signSystem.template(PFDNavigation.template,templateData,"PFDNavigation");
				   $("#PFDNavigation").on("img","mousemove",function(){return false;});
				   
				   //渲染 handWriteSignModel
				   if(handWriteSign != null && handWriteSign != ""){
					   templateData={};
					   signSystem.template(handWriteSign.template,templateData,"handWriteSign");
					   $("#handWriteSign").on("img","mousemove",function(){return false;});
				   }

				   //渲染uploadStamp
                   if(uploadStamp != null && uploadStamp != ""){
                       templateData={};
                       signSystem.template(uploadStamp.template,templateData,"uploadStamp");
                       $("#uploadStamp").on("img","mousemove",function(){return false;});
                   }

				   
				   //渲染handBagWriteSignModel
				   if(handBagWriteSign != null && handBagWriteSign != ""){
					   templateData={};
					   signSystem.template(handBagWriteSign.template,templateData,"handBagWriteSign");
					   $("#handBagWriteSign").on("img","mousemove",function(){return false;});
				   }
				   signSystem.eventBind();
			   }
			   
			   //预先加载图片在渲染
			   var pdfImageList=json.pdfImageList;
			   var loadCount=0;
			   for(var index in pdfImageList){
				   var item=pdfImageList[index];
				   if(item != null){
					   service.loadImage(item.imagePath,function(){
						   loadCount++;
						   if(pdfImageList.length == loadCount){
							   call();
						   }
					   })
				   }else{
					   loadCount++;
				   }
			   }

			if(typeof options.againTempleteCall == "function"){
                options.againTempleteCall();
			}
	   	}
	   	
	   	service.againPage=function(json){
	   		if(typeof(json) != "object"){
				   try { 
					   json=$.parseJSON(json);
				   } catch (e) { 
					   layer.msg(json);
					   return;
				   } 
			   }
			   if(json.resultCode == -1){
				   layer.msg(json.message);
				   return false;
			   }
			   json=json.data;
	   		   json=toData(json);
			   //删除所有章
			   $("img[name='selectImg']").trigger("click");
			   
			   $("#PFDNavigationName").children().remove();
			   $("#PFDNavigation .PFDbody").remove();
	           $("#clickSign").children().remove();
	           //渲染PFDNavigationName
			   var templateData={};
			   templateData.pdfName=json.fileName;
			   templateData.signFilePath=json.filePath;
			   templateData.fileId=json.fileId;
			   templateData.isTemplates=false;
			   templateData.pdfNameStr=json.fileNameStr;
			   templateData.totalPageCount=json.totalPageCount;
			   signSystem.template(PFDNavigationName.template,templateData,"PFDNavigationName");

			   //渲染clickSign
			   templateData={};
			   templateData.blueDigital=json.blueDigitalStr;
			   templateData.hasSm2Signed = json.hasSm2Signed;
			   templateData.hasRSASigned = json.hasRSASigned;
			   templateData.hasPublicCerSign = false;
			   signSystem.template(clickSign.template,templateData,"clickSign");


	   	}
	   	/**
	   	 * 取得服务器返回数据
	   	 */
	   	service.responseData=function(){
	   		return responseData;
	   	}
	   	
	   	
	   	signatureOperate.input=function(id,val){
	   		if(typeof service.input == "function"){
	   			//signSystem.countSignOffsetPosition();
	        	var rate=$("#pfdShowImgTest").data("imageListLoad").zoomRate();
	        	var sign=signModel.getkeySign(id);
	        	signSystem.signRate(rate,sign);
	   			return service.input(id,val);
	   		}
	   	}
	   	
	   	service.countSignOffsetPosition=function(type){
        	signSystem.countSignOffsetPosition(type);
	   	}
	   	
	   	/**
	   	 * 当前类型的签章是否存在  true 存在
	   	 */
	   	service.isSignType=function(signType){
	   		return signModel.isSignType(signType);
	   	}
	   	
	   	service.headHeightChange=function(height){
	   		utils.headFixedHeight=height;
	   		signSystem.screenChange({});
	   	}
	   	
	   	service.load=function(paramData){
	   		$.ajax({
			   type: "POST",
			   url: url,
			   data: paramData,
			   success: function(json){
				   service.againTemplete(json);
			   }
			});
	   	}
	   	
	   	service.signSystemLoad=function(urlRequestParam){
		   	signSystem.load(urlRequestParam);
	   	}
	   	signSystem.load(urlRequestParam);
		return service;
		
	}

