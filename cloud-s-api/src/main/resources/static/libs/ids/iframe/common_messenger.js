/**
 * options.childFrameHeight  子页面frame高度
 * options.messageType  通信类型
 * options.receiveFrame 消息接收方Frame名称
 */
//子frame发送消息
function sendMessageToParent(frameName,minHeight){
    var $frame = parent.$("#"+frameName);
    var options = {
        childFrameHeight:$frame.height()+minHeight,
        messengerType :MESSAGE_TYPE.CHANGE_HEIGHT,
        receiveFrame:PM_FRAMES.mainFrame
    }
    var sendMessenger = PM_IFRAME_MESSENGE.getMessengerByFrameName(frameName);
    sendMessenger.addTarget(top.window,PM_FRAMES.mainFrame);
    IFRAME_FUNCTIONS.sendMessage(sendMessenger,options);
}

//子标签
function sendMessageFromIdToParentFrame(idTag,minHeight){
    var $id =$("#"+idTag);
    var options = {
        childFrameHeight:$id.height()+minHeight,
        messengerType :MESSAGE_TYPE.CHANGE_HEIGHT,
        receiveFrame:PM_FRAMES.mainFrame
    }
    var sendMessenger = PM_IFRAME_MESSENGE.getMessengerByFrameName(idTag);
    sendMessenger.addTarget(top.window,PM_FRAMES.mainFrame);
    IFRAME_FUNCTIONS.sendMessage(sendMessenger,options);
}

/**
 * 发送样式给父iframe
 * @param idTag
 * @param styleContent
 */
function sendMessageToParentFrameToExecutorStyle(idTag,styleContent){
    var $id =$("#"+idTag);
    var options = {
        messengerType :MESSAGE_TYPE.CHANGE_FATHTER_FRAME_STYLE,
        receiveFrame:PM_FRAMES.mainFrame,
        styleContent:styleContent
    }
    var sendMessenger = PM_IFRAME_MESSENGE.getMessengerByFrameName(idTag);
    sendMessenger.addTarget(top.window,PM_FRAMES.mainFrame);
    IFRAME_FUNCTIONS.sendMessage(sendMessenger,options);
}

/**
 * 父类执行js
 * @param idTag
 * @param styleContent
 */
function sendMessageToParentFrameToExecutorLogin(idTag,content){
    var options = {
        messengerType :MESSAGE_TYPE.CHANGE_FATHTER_LOGIN,
        receiveFrame:PM_FRAMES.mainFrame,
        styleContent:content
    }
    var sendMessenger = PM_IFRAME_MESSENGE.getMessengerByFrameName(idTag);
    sendMessenger.addTarget(top.window,PM_FRAMES.mainFrame);
    IFRAME_FUNCTIONS.sendMessage(sendMessenger,options);
}

/**
 * 父类向子类通信
 * @param idTag
 * @param content
 */
function sendMessageToParentFrameToChildFlay(idTag,content){
    var options = {
        messengerType :MESSAGE_TYPE.CHANGE_CHILD_FLAY,
        receiveFrame:PM_FRAMES.childFrame,
        styleContent:content
    }
    var sendMessenger = PM_IFRAME_MESSENGE.getMessengerByFrameName(idTag);
    sendMessenger.addTarget(top.window,PM_FRAMES.childFrame);
    IFRAME_FUNCTIONS.sendMessage(sendMessenger,options);
}