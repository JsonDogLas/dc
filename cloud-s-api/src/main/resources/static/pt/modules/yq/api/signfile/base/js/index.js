
/**
 * 跨域打开登录页面
 */
var toLogin = function(){
    // var webUser = $("#webUser").val();
    // if(webUser == 'null'||  webUser == null || webUser == undefined || webUser.length == 0 ){
    var content=" parent.$(\"#szLogin\").val(\"true\"); " +
        "          var bodyHH = window.outerHeight;" +
        "          var top = (bodyHH - 444)*0.5;" +
        "          parent.$.idsLayui.modelIframe({" +
        "            id: \"login-modal-layer\"," +
        "            offset: top + \"px\"," +
        "            title: \"\"," +
        "            area: ['380px', '444px']," +
        "            content:[\"/login/to_login\",\"no\"]," +
        "            cancel: function (){" +
        "            }" +
        "        })"
    sendMessageToParentFrameToExecutorLogin("childLogin",content);
}

$(function(){
    var totalLoad=$.idsLoad({shade:[1,'#FFFFFF']});
    $("#dropz").width($("#upload").width());
    $("#upload").height("360");
    $("#dropz").height($("#upload").height());
    $("#dropz").offset($("#upload").offset());

    $("#uploadSignDiv").height("600");
    sendMessageFromIdToParentFrame("uploadSignDiv",140);

	$("#upfile").change(function(){
        layer.open({
            title:"签名",
            type: 2,
            area:['100%', '100%'],
            maxmin: false,
            resizing:false,
            content:"signfile/sign/customSign.jsp" ,
            closeBtn:1,
            zIndex: layer.zIndex, //重点1
            success: function(layero){
                var keyId=parent.frames[0].lmParam.keyId;
                $.ajaxFileUpload({
                    type: "POST",
                    id:new Date().getTime()+"",
                    url: "/sign/loadSignData",
                    secureuri : false,//是否启用安全提交，默认为false
                    fileElementId:'upfile',//文件选择框的id属性
                    dataType: 'json',//服务器返回的格式
                    async : false,
                    data:{keyId:keyId},
                    success: function(data){
                        var service=parent.frames[0].$.startSignFunction("service");
                        service.againTemplete(data);
                        var $upfile=$("#upfile");
                        if($upfile[0].files != null){
                            $upfile[0].files = null;
                        }
                        $upfile.before($upfile.clone());
                        $upfile.remove();
                        $("#upfile").val("");
                    }
                });
            }
        });
    });

	var open=function(){
	    if(open.startFlag == 1){
            if(open.data != null){
                open.startFlag=null;
                // var service=$("body iframe")[0].contentWindow.$.startSignFunction("service");
                var service=$("body iframe[src='/sign/to_sign_page']")[0].contentWindow.$.startSignFunction("service");
                service.againTemplete(open.data);
                open.data=null;
            }
        }
    }

    var dropzone=function(){
        $("#dropz").dropzone({
            init: function() {
                this.on("addedfile", function(file) {
                    //判断是否登录
                    var loginFlag=false;
                    $.ajax({
                        type: "GET",
                        url: "/sign/is_login",
                        dataType:"json",
                        async:false,//误改
                        success: function(json){
                            loginFlag=json.result;
                        }
                    });
                    if(!loginFlag){
                        toLogin();
                        return loginFlag;
                    }else if($("#loginFlag").val() == ""){
                        var postMessage='var $a=$("a[action$=\'sign/to_sign\']");' +
                            '$("li.layui-nav-item").removeClass("layui-this");' +
                            '$a.parent().parent().parent().addClass("layui-this")';
                        sendMessageToParentFrameToExecutorStyle("customSign",postMessage);
                    }
                    //判断大小是否符合上传限制
                    if(file.size/1024/1024 < intl.SIGN_FILE_UPLOAD_SIZE){
                        //top.postMessage('$("div.main-container.bg-min-width").addClass("bigLayer");$("div.main-container.bg-min-width #mainFrame").css({height:"100%"});',intl.CLI_URL);
                        //$("div.main-container.bg-min-width",top.document).addClass("bigLayer");
                        var styleContent = '$("div.main-container.bg-min-width").addClass("bigLayer");$("div.main-container.bg-min-width #mainFrame").css({height:"100%"});';
                        sendMessageToParentFrameToExecutorStyle("customSign",styleContent);
                        //var height = document.documentElement.clientHeight-30;
                        //var width = document.documentElement.clientWidth;
                        layer.open({
                            title:"签名",
                            type: 2,
                            offset:[0,0],
                            area:["100%", "100%"],
                            maxmin: false,
                            resizing:false,
                            content:"/sign/to_sign_page" ,
                            closeBtn:1,
                            zIndex: layer.zIndex, //重点1
                            success: function(layero) {
                                open.startFlag=1;//页面加载完毕标识
                                open();
                            },
                            end:function () {
                                //$("div.main-container.bg-min-width",top.document).addClass("bigLayer");
                                var height=document.documentElement.clientHeight;
                                var message='$("div.main-container.bg-min-width").removeClass("bigLayer");';
                                if($.postMessage != null){
                                    message=message+" "+$.postMessage;
                                }
                                sendMessageToParentFrameToExecutorStyle("customSign",message);
                                sendMessageFromIdToParentFrame("uploadSignDiv",140);
                                intl.loadKey();
                            }
                        });
                    }
                });
                this.on('sending', function (data, xhr, formData) {
                    //向后台发送该文件的参数
                    var keyId=lmParam.keyId;
                    if(keyId == null || keyId == ""){
                        //没有插入key,重新初始化一下key
                        intl.loadKey();
                        keyId=lmParam.keyId;
                    }
                    console.log("keyId"+keyId);
                    formData.append('keyId', keyId);
                });
                this.on("success",function(file,data){
                    //上传成功触发的事件
                    if(!data.result && data.resultCode == -22){
                        layer.closeAll();
                        window.location.href=$("#loginFlag").attr("src");
                        return false;
                    }
                    open.data=data;//页面有数据标识
                    open();
                });
                this.on('error', function (files, response) {
                    //文件上传失败后的操作
                    if(response == intl.CST_SIGN_FILE_FILE_TOO_BIG){
                        idsLayer({
                            content:$("#clientDownloadTemplate").text(),
                            closeBtn:1
                        });
                    }
                });
            },
            maxFiles:100,//一次性上传的文件数量上限
            maxFilesize: intl.SIGN_FILE_UPLOAD_SIZE, //文件大小，单位：MB
            acceptedFiles: ".jpg,.gif,.png,.jpeg,.pdf,.doc,.docx", //上传的类型
            addRemoveLinks:true,
            parallelUploads: 1,//一次上传的文件数量
            //previewsContainer:"#preview",//上传图片的预览窗口
            dictDefaultMessage:intl.CST_SIGN_FILE_DICT_DEFAULT_MESSAGE,
            dictMaxFilesExceeded: intl.CST_SIGN_FILE_DICT_MAX_FILES_EXCEEDED,
            dictResponseError: intl.CST_SIGN_FILE_RESPONSE_ERROR,
            dictInvalidFileType: intl.CST_SIGN_FILE_INVALID_FILE_TYPE,
            dictFallbackMessage:intl.CST_SIGN_FILE_FALL_BACK_MESSAGE,
            dictFileTooBig:intl.CST_SIGN_FILE_FILE_TOO_BIG,
            url: "/sign/loadSignData?time="+new Date().getTime()
        });
        totalLoad.close();
    }
    dropzone();
});