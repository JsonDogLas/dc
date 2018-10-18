





$(function(){
	//国际化
	$("font").each(function(){
		var id=$(this).attr("id");
		$(this).text(intl[id]);
	});



	var data={};
	data.fontFamily=[];
	for(var item in fontList){
		var font=fontList[item];
		var test={};
		test.id=font.number;
		test.name=font.name;
		test.text=font.name;
		data.fontFamily.push(test);
	}
	
	data.fontSize=[];
    for(var i=10;i<40;i++){
    	var test={};
		test.id=i+"px";
		test.name=i+"px";
		test.text=i+"px";
		data.fontSize.push(test);
    }
	
	var select2Ele=$("#fontFamilyInput").select2({
	     data: data.fontFamily,
	     language: intl.language,//汉化
	     allowClear: true,//允许清空,
		 maximumSelectionSize:10,
		 templateResult:function (state) {  
	        if (!state.id) { return state.text; }  
	        var $state = $(  
	        	'<span id="'+state.id+'">' + state.text + '</span>' 
	        ); 
	        $state.css("font-family",state.id);
	        return $state;  
	    },
	    templateSelection:function (state) {  
	        if (!state.id) { return state.text; }  
	        var $state = $(  
	        	'<span>' + state.text + '</span>'  
	        ); 
	        $state.css("font-family",state.id);
	        return $state;  
	    },
     	 placeholder:intl.placeholder
 	});
	$("#fontFamilyInput").val(data.fontFamily[data.fontFamily.length-1].id).trigger('change');
	$("body").keydown(function(event){
		 if(event.keyCode == 38 || event.keyCode == 40){
			 var fontFamilyInput=$("#fontFamilyInput").val();
			 if(fontFamilyInput == null || fontFamilyInput.trim() == ""){
				 fontFamilyInput=data.fontFamily[0].id;
			 }
				
			 var index;
			 for(var item in data.fontFamily){
				var font=data.fontFamily[item];
				if(font.id == fontFamilyInput){
					fontFamilyInput=item;
					index=parseInt(item);
				}
			}
			 
			 
			 
			 if(index == 1){
				 $("#fontFamilyInput").val(data.fontFamily[data.fontFamily.length-1].id).trigger('change');
				 return;
			 }
			 
			 var up=index-1;
			 var down=index+1;
			 var id;
			 if(event.keyCode == 38){//向上
				 if(up >= data.fontFamily.length){
					 up=0;
				 }else if(up < 0){
					 up=data.fontFamily.length-1;
				 }
				 id=data.fontFamily[up].id;
			 }else if(event.keyCode == 40){//向下
				 if(down >= data.fontFamily.length){
					 down=0;
				 }else if(down < 0){
					 down=data.fontFamily.length-1;
				 }
				 id=data.fontFamily[down].id;
			 }
			 $("#fontFamilyInput").val(id).trigger('change');
			 //$("#fontFamilyInput").select2("val",id);
			 //$("#fontFamilyInput").val(id);
		 }
	});
	
	
	
	
	/*$("#fontSizeInput").select2({
	     data: data.fontSize,
	     language: intl.language,//汉化
	     allowClear: true,//允许清空,
		 maximumSelectionSize:10,
		 placeholder:intl.placeholder
	});*/
	
	$("#fontSizeInput,#fontFamilyInput").change(function(){
		/*$("#fontInput").css("font-size",$("#fontSizeInput").val());
		$("#fontInput").css("font-family",$("#fontFamilyInput").val());*/
		
		$("#fontSizeTest").css("font-size",$("#fontSizeInput").val());
		$("#fontSizeTest").css("font-family",$("#fontFamilyInput").val());
		
		$("#fontInput").trigger("change");
	});
	$("#fontInput").bind("change input propertychange blur focus",fontInput);
	
	/*$("#fontInput").height("16px");
	$("#fontInput").width("115px");*/
	$("#fontInput").css("line-height","1.32");
	
	
	/*intl.handWriteRequest="http://"+ip+":"+port+"/qd/workflow/createStamp/handWriteRequest.do";//打开
	intl.handWriteUpload="http://"+ip+":"+port+"/qd/workflow/createStamp/handWriteUpload.do";//上传
	intl.handWriteClose="http://"+ip+":"+port+"/qd/workflow/createStamp/handWriteClose.do";//关闭
	 */	
	
	
	//关闭
	$(window).on('beforeunload', function(){  
		//createStampAjax(intl.handWriteClose,{},function(){ });
	});  
	
	$("#fontOpeareNoUpload").click(function(){
		$.colosLayerIfram();
		/*createStampAjax(intl.handWriteClose,{},function(){
			$.colosLayerIfram();
			createStampAjax(intl.handWriteClose,{},function(){ });
		});*/
		
	});
	
	
	//高清占比
	var buttonBool=true;
	//上传
	$("#fontOpeareUpload").click(function(event){
		//避免按钮重复 单机
		var nowTime = new Date().getTime();
	    var clickTime = $(this).attr("ctime");
	    if( clickTime != 'undefined' && (nowTime - clickTime < 500)){
	        return false;
	    }
	    $(this).attr("ctime",nowTime);
	    
	    
		var idsLoad=$.idsLoad();
		var data={};
		data.name=$("#fontInput").val();
		if(typeof data.name != 'string' || data.name.trim().length == 0){
			layer.msg(intl.WORKFLOA_CREATE_STAMP_NOT_EMPTY);
			idsLoad.close();
			return;
		}
		//fontSizeTest 放大字体所在框
		var rate=parent.intl.WORKFLOW_HAND_WIRTE_IMAGE_RATE;
		var width=$("#fontSizeTest").parent().width()*rate;
		var height=$("#fontSizeTest").parent().height()*rate;
		$("#fontSizeTest").parent().width(width);
		$("#fontSizeTest").parent().height(height);
		
		//放大字体
		//解决IE问题
		 $("#fontSizeTest").parent().show();
		$("#fontSizeTest").attr("width_test",$("#fontSizeTest").width()*rate);
		 $("#fontSizeTest").parent().hide();
		var fontSize=parseInt($("#fontSizeTest").css("font-size"))*rate;
		$("#fontSizeTest").css("font-size",fontSize);
		
		
		fontInput(function(base64){
			data.base64=base64;
			data.type="0";
			createStampAjax(intl.handWriteUpload,data,function(result){
				idsLoad.close();
				if(result.resultCode != -1){
					//WORKFLOW_BASE64_UPLOAD_SUCCESS=上传成功
                    parent.$.startSignFunction("service").reqeustStamp(true);
					layer.msg(intl.WORKFLOW_BASE64_UPLOAD_SUCCESS);
					//createStampAjax(intl.handWriteClose,{},function(){ });
					$.colosLayerIfram();
				}else{
					var fontSize=parseInt($("#fontSizeTest").css("font-size"))/rate;
					$("#fontSizeTest").css("font-size",fontSize);
					var width=$("#fontSizeTest").parent().width()/rate;
					var height=$("#fontSizeTest").parent().height()/rate;
					$("#fontSizeTest").parent().width(width);
					$("#fontSizeTest").parent().height(height);
					$("#fontSizeTest").removeAttr("width_test");
					$(".close").trigger("click");
					layer.msg(result.message);
				}
			});
		});
	});
	
	//打开
	createStampAjax(intl.handWriteRequest,{},function(){$(".createStamp").show(); });
	
	
	//批注容器绑定批注块选择
	var scales=new scale('btn','bar','progreess',function(progress,max){
		var test=progress/max;
		var test=parseInt(test*100/10)*100
		if(test == 0){
			test=100;
		}else if(test == 1000){
			test=900;
		}
		$("#fontSizeTest").css("font-weight",test);
		var fontWeight=$("#bar").attr("scales",test);
		fontInput();
    });
	$("#fontSizeTest").css("font-weight","900");
	scales.setProgress(50,false);
	
	$(".close").click(function(){
		$("#canvas img").remove();
		$("#fontInput").val("");
		$(".close").hide();
	});
});

var createStampAjax=function(url,data,success){
	data.signPageKey=intl.signPageKey;
    $.ajax({  
        type:"POST",  
        url:url,  
        data: {data:JSON.stringify(data)},  
        dataType: "json",  
        success:function(data) {  
	    	success(data);
        }  
    });
}

var fontInput=function(callback){
	var fontSize=$("#fontSizeTest").css("font-size");
	var fontFamily=$("#fontSizeTest").css("font-family");
	var fontLineHeight=$("#fontSizeTest").css("line-height");
	var fontWeight=$("#bar").attr("scales");
	if($("#fontInput").val() != null && $("#fontInput").val() != ""){
		$(".close").show();
		createStamp.createImage($("#canvas"),
				$("#fontInput").val(),
				$("#fontInput").width(),
				$("#fontInput").height(),
				fontWeight,fontSize,fontLineHeight,fontFamily,callback);
		/*createStamp.createHtmlImage($("#canvas"),$("#fontSizeTest"));*/
	}else{
		$(".close").trigger("click");
		$(".close").hide();
	}
}
var createStamp={};
createStamp.indexFont=function(number){
	for(var item in fontList){
		var font=fontList[item];
		if(font.number == number){
			return font;
		}
	}
}

createStamp.createFont=function(number){
	var newStyle = $("style");
	if(newStyle.length == 0){
		newStyle = $("<style />");
		$("body").append(newStyle);
	}
	newStyle=newStyle[0];
	
	var font=createStamp.indexFont(number);
	if(font != null){
		newStyle.appendChild(document.createTextNode("@font-face {font-family: '" + font.number + "';" +
				"src: url('" + font.path + "') format(truetype);}"));
	}
}

createStamp.createHtmlImage=function(positionEle,createEle){
	/*//createEle.parent().show();
	var w = createEle.width();
	var h = createEle.height();
	//要将 canvas 的宽高设置成容器宽高的 2 倍
	var canvas = document.createElement("canvas");
	canvas.width = w * 2;
	canvas.height = h * 2;
	canvas.style.width = w + "px";
	canvas.style.height = h + "px";
	var context = canvas.getContext("2d");
	//然后将画布缩放，将图像放大两倍画到画布上
	context.scale(2,2);*/
	

    html2canvas(document.getElementById(createEle.attr("id"))).then(function(canvas) {
    	 var base64 = canvas.toDataURL('images/png');//注意是canvas元素才有 toDataURL 方法
    	 var img= document.createElement('img')
    	 img.src = base64;//canvas 转换为 img
    	 positionEle.find("img").remove();
    	 positionEle.append($(img));
    });
	/*html2canvas(document.getElementById(createEle.attr("id")), {
	    canvas: canvas,
	    onrendered: function(canvas) {
	    	//createEle.parent().hide();
	    	 var base64 = canvas.toDataURL('images/png');//注意是canvas元素才有 toDataURL 方法
	    	 var img= document.createElement('img')
	    	 img.src = base64;//canvas 转换为 img
	    	 positionEle.find("img").remove();
	    	 positionEle.append($(img));
	    }
	});*/
}

/**
 * @param ele保存内容的节点
 * @param 创建图片的内容
 * @param 图片宽度
 * @param 图片高度
 * @param 图片大小
 * @param 图片字体
 */
createStamp.createImage=function(ele,content,width,height,fontWeight,fontSize,fontLineHeight,fontFamily,callback){
	 var canvas=document.createElement('canvas');
	 
	 var size=function(canvas,fontSize,fontLineHeight){
		 $("#fontSizeTest").parent().show();
		 var _width=0,_height;
		 fontSize=Number(fontSize.replace("px",""));
		 fontLineHeight=Number(fontLineHeight.replace("px",""));
		 var _content=content,test;
		 while(_content.indexOf("\n") > -1){
			 var index=_content.indexOf("\n")+1;
			 var text=_content.substring(0,index);
			 $("#fontSizeTest").append($("</br>"));
			 $("#fontSizeTest").text($("#fontSizeTest").text()+text);
			 _content=_content.substring(index,_content.length);
		 }
		 if(test == null){
			 $("#fontSizeTest").text(content);
		 }
		 
		 var width_test=parseInt($("#fontSizeTest").width()) > 0 ? $("#fontSizeTest").width() : parseInt($("#fontSizeTest").attr("width_test"))
		 canvas.width=width_test+16;
		 canvas.height=$("#fontSizeTest").parent().height();
		 $("#fontSizeTest").parent().hide();
	 }
	 size(canvas,fontSize,fontLineHeight);
	 var drawFont=function(ctx,fontSize,fontLineHeight,fontFamily){
		 ctx.font = fontWeight+" "+fontSize+" "+fontFamily;
		 fontSize=Number(fontSize.replace("px",""));
		 fontLineHeight=Number(fontLineHeight.replace("px",""));
		 var _content=content;
		 var y=0,i=1;
		 
		 while(_content.indexOf("\n") > -1){
			 var index=_content.indexOf("\n")+1;
			 var text=_content.substring(0,index);
			 if(i==1){
				 y=y+fontSize;
			 }else{
				 y=y+fontLineHeight;
			 }
			 ctx.fillText(text,0,y);
			 _content=_content.substring(index,_content.length);
			 i++;
		 }
		 
		 if(content.indexOf("\n") == -1){
			 ctx.fillText(_content,0,fontSize);
		 }else{
			 ctx.fillText(_content,0,y+fontLineHeight);
		 }
		 
	 }
	 var ctx;
	 if(typeof canvas.getContext != 'function'){
		 G_vmlCanvasManager.initElement(canvas);
		 ctx=canvas.getContext('2d');
		 alert(intl.WORKFLOW_BORDER_IMAGE_SHENGJI);
		 return;
	 }else{
		 ctx=canvas.getContext('2d'); 
	 }
	 drawFont(ctx,fontSize,fontLineHeight,fontFamily);
	 var base64 = canvas.toDataURL('images/png');//注意是canvas元素才有 toDataURL 方法
	 if(typeof callback == "function"){
		 callback(base64);
		 return;
	 }
	 var img= document.createElement('img')
	 img.src = base64;//canvas 转换为 img
	 ele.find("img").remove();
	 ele.append($(img));
	 
}