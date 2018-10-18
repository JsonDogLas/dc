var PM_FRAMES ={
    mainFrame:'mainFrame',
    childFrame:'mainCertDiv',
    //数字证书
    certCompanyFrame:'certCompanyFrame',
    cerPersonalForm:'cerPersonalForm',
    cerBatchPersonalForm:'cerBatchPersonalForm',
    credentialsDiv:'credentialsDiv',
    companyStampDiv:'companyStampDiv',
    personalStampDiv:'personalStampDiv',
    childLogin:'childLogin',
    childFlay:'childFlay',
    //数字证书 -- end
    //云签
    signListDiv:'signListDiv',
    customSign:'customSign',
    uploadSignDiv:'uploadSignDiv'
}

var PM_IFRAME_MESSENGE={
    mainFrameMessenger : new Messenger(PM_FRAMES.mainFrame),
    childFrameMessenger : new Messenger(PM_FRAMES.childFrame),
    certCompanyFrameMessenger: new Messenger(PM_FRAMES.certCompanyFrame),
    cerPersonalFormMessenger : new Messenger(PM_FRAMES.cerPersonalForm),
    cerBatchPersonalFormMessenger : new Messenger(PM_FRAMES.cerBatchPersonalForm),
    credentialsDivMessenger : new Messenger(PM_FRAMES.credentialsDiv),
    companyStampDivMessenger : new Messenger(PM_FRAMES.companyStampDiv),
    personalStampDivMessenger : new Messenger(PM_FRAMES.personalStampDiv),
    signListDivMessenger : new Messenger(PM_FRAMES.signListDiv),
    customSignMessenger : new Messenger(PM_FRAMES.customSign),
    uploadSignDivMessenger : new Messenger(PM_FRAMES.uploadSignDiv),
    childLoginMessenger : new Messenger(PM_FRAMES.childLogin),
    childFlayMessenger : new Messenger(PM_FRAMES.childFlay),
    getMessengerByFrameName:function(name){
        if(name == PM_FRAMES.mainFrame){
            return PM_IFRAME_MESSENGE.mainFrameMessenger;
        }else if(name == PM_FRAMES.childFrame){
            return PM_IFRAME_MESSENGE.childFrameMessenger;
        }else if(name == PM_FRAMES.certCompanyFrame){
            return PM_IFRAME_MESSENGE.certCompanyFrameMessenger;
        }else if(name == PM_FRAMES.cerPersonalForm){
            return PM_IFRAME_MESSENGE.cerPersonalFormMessenger;
        }else if(name == PM_FRAMES.cerBatchPersonalForm){
            return PM_IFRAME_MESSENGE.cerBatchPersonalFormMessenger;
        }else if(name == PM_FRAMES.credentialsDiv){
            return PM_IFRAME_MESSENGE.credentialsDivMessenger;
        }else if(name == PM_FRAMES.companyStampDiv){
            return PM_IFRAME_MESSENGE.companyStampDivMessenger;
        }else if(name == PM_FRAMES.personalStampDiv){
            return PM_IFRAME_MESSENGE.personalStampDivMessenger;
        }else if(name == PM_FRAMES.signListDiv){
            return PM_IFRAME_MESSENGE.signListDivMessenger;
        }else if(name == PM_FRAMES.customSign){
            return PM_IFRAME_MESSENGE.customSignMessenger;
        }else if(name == PM_FRAMES.uploadSignDiv){
            return PM_IFRAME_MESSENGE.uploadSignDivMessenger;
        }else if(name == PM_FRAMES.childLogin){
            return PM_IFRAME_MESSENGE.childLoginMessenger;
        }else if(name == PM_FRAMES.childFlay){
            return PM_IFRAME_MESSENGE.childFlayMessenger;
        }
    }
}

//通信类型
var MESSAGE_TYPE={
    CHANGE_HEIGHT:'change_height',
    CHANGE_FATHTER_FRAME_STYLE:'change_father_frame_style',
    CHANGE_FATHTER_LOGIN:'change_login',
    CHANGE_CHILD_FLAY:'change_child_flay',
}

/**
 * options.childFrameHeight  子页面frame高度
 * options.messengerType  通信类型
 * options.receiveFrame 消息接收方Frame名称
 */


var IFRAME_FUNCTIONS={
    sendMessage: function (sendMessenger,options) {
        var receiveFrame = options.receiveFrame;
        options = JSON.stringify(options);
        sendMessenger.targets[receiveFrame].send(options);
    }
}