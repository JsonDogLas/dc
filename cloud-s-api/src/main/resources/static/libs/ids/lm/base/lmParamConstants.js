/**
 * 
 */

var SignParams={
		generateParam : function (type,method,fileId,serverIp,serverPort,version){
//			var serverIp = $("#socketIp").html();
//			var serverPort = $("#socketPort").html();
			var requestData = new Object();
			requestData.method = method;
			requestData.fileId = fileId;
			var requestDataJson = JSON.stringify(requestData);
			
			var param= SignParams.createCommonParam(requestDataJson,type,serverIp,serverPort,version);
			var paramJson = JSON.stringify(param);
			return paramJson;
		},
		createCommonParam : function(requestDataJson,type,serverIp,serverPort,version){
			var param= new Object();
			param.type = type; 
			param.version = version;//$("#clientVersion").val();
			param.verifedCode= hex_md5(serverIp+serverPort);
			param.timestamp = new Date().getTime();
			param.data = requestDataJson ;
			param.length= requestDataJson.length ;
			
			return param;
		},
		generateSendServerParam : function (type,signed,fileId,serverIp,serverPort,version){
			var requestData = new Object();
			requestData.signed = signed;
			requestData.fileId = fileId;
			var requestDataJson = JSON.stringify(requestData);
			var param= SignParams.createCommonParam(requestDataJson,type,serverIp,serverPort,version);
			var paramJson = JSON.stringify(param);
			return paramJson;
			
		},
        generateApplyCertParam : function (type,method,fileId,p10,serverIp,serverPort,version){
            var requestData = new Object();
            requestData.method = method;
            requestData.fileId = fileId;
            requestData.p10 = p10;
            var requestDataJson = JSON.stringify(requestData);
            var param= SignParams.createCommonParam(requestDataJson,type,serverIp,serverPort,version);
            var paramJson = JSON.stringify(param);
            return paramJson;
        },
}

function showLmMessage(message){
	if(layer) {
        layer.msg(message)
    }else{
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.msg(message)
        });
	}
}


function showLmMessageOffset(message,top,left){
	var offset=[top+"px",left+"px"];
    if(layer) {
        layer.msg(message, {offset: offset});
    }else {
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.msg(message, {offset: offset});
        });
    }
}

function closeLayerPrompt(index){
    if(layer) {
        layer.close(index)
    }else {
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.close(index)
        });
    }
}

function closeLayerLoading(){
    if(layer) {
        layer.closeAll('loading');
    }else {
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.closeAll('loading');
        });
    }
}

function layerLoading(){
    if(layer) {
        layer.load();
    }else {
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.load(2);
        });
    }
}