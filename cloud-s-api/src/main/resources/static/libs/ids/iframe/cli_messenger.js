/**
 * options.childFrameHeight  子页面frame高度
 * options.messageType  通信类型
 * options.recevieFrame 消息接收方Frame名称
 * options.styleContent  样式内容
 */
//父项目监听开启
PM_IFRAME_MESSENGE.mainFrameMessenger.listen(function(options){
    options = JSON.parse(options);
    if(options.messengerType == MESSAGE_TYPE.CHANGE_HEIGHT) {
        var childFrameHeight = options.childFrameHeight;
        $("#"+PM_FRAMES.mainFrame).height(childFrameHeight + 40);
    }else if(options.messengerType == MESSAGE_TYPE.CHANGE_FATHTER_FRAME_STYLE){
        eval(options.styleContent);
    }else if(options.messengerType == MESSAGE_TYPE.CHANGE_FATHTER_LOGIN){
        eval(options.styleContent);
    }else if(options.messengerType == MESSAGE_TYPE.CHANGE_CHILD_FLAY){
        eval(options.styleContent);
    }
})