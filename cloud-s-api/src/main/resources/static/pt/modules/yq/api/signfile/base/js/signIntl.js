var intl={};
intl.deleteSign=function(fileName){
    var test='删除已添加的||所有签章';
    test=test.split("|");
    return test[0]+fileName+test[1];
};
intl.noPage=function(fileName){
    var test='不将||添加到页面中';
    test=test.split("|");
    return test[0]+fileName+test[1];
}

//signDrag.js国际化
intl.signDrag_inputNumber='请输入倍数！';
intl.signDrag_zoomRate=function(zoomRate){
    var test='当前图片缩放|倍';
    test=test.split("|");
    return test[0]+zoomRate+test[1];
}
intl.signDrag_imageBig='当前图片已放大至最大倍数，不能再放大！';
intl.signDrag_imageSmail='当前图片已缩小到最小倍数，不能在缩小！';
//signTemplate.js国际化
intl.signTemplate_configFile='没有文件数据，请设置文件数据';
//签名成功
intl.sign_success='签名成功';
intl.sign_faile='签名失败';
//WORKFLOW_IS_SAVE_SIGN_FILE=是否保存签名， 覆盖当前文件
intl.WORKFLOW_UPFILETIP_PERSON='是否保存签名， 覆盖当前文件';
intl.WORKFLOW_CREATE_STAMP_INFO='生成手写体';
//WORKFLOW_WATER_STAMP_NO_ADD=当前文档已经有水印请不要在添加！
intl.WORKFLOW_WATER_STAMP_NO_ADD='当前文档已经有水印请不要在添加！';
//WORKFLOW_WATER_QIFENG_NO_NUMBER_SIGN=水印和骑缝印章不能进行数字签名
intl.WORKFLOW_WATER_QIFENG_NO_NUMBER_SIGN="水印和骑缝印章不能进行数字签名";
//流程结束时的弹框信息
intl.WORKFLOW_SIGN_NO_SIGN='当前pdf上没有任何签/章,请添加签/章后操作！';
intl.WORKFLOW_SIGN_PLEANCE_WAIT='正在签名，请稍等...';
intl.WORKFLOW_WATER_QIFENG_NO_PUBLIC_CER_SIGN="水印不能进行公共证书签名";
//手写体的放大倍率
intl.WORKFLOW_HAND_WIRTE_IMAGE_RATE=3;
intl.WORKFLOW_CUSTOMSIGN_IMAGE_HEIGHT_WIDTH_ERROR='签章太大了，放不下';
intl.WORKFLOW_CUSTOMSIGN_IMAGE_POSITION_ERROR='签章超出可编辑区域，放不下';
intl.WORKFLOW_CUSTOM_SIGN_THIS_PAGE_LOAD='当前页正在加载，请勿拖拽签章';
intl.WORKFLOW_CUSTOMSIGN_FILE_ERROR="文件已被破坏，无法继续签名";
intl.WORKFLOW_CUSTOMSIGN_FILE_NO_SIGN="文件已被数字签名锁定，无法继续签名";
intl.WORKFLOW_END_WORKFLOW_SIGN_LOAD="正在加载签章数据...";
intl.WORKFLOW_END_WORKFLOW_PDF_LOAD="正在加载PDF文件数据...";
intl.WORKFLOW_END_WORKFLOW_PDF_IMAGE_LOAD="正在加载PDF图片数据...";
intl.WORKFLOW_CUSTOMSIGN_COMPLAYSIGN="印章";
intl.USERMANAGE_GSTAMP_SIGN_QIFENG="骑缝印章";
intl.WORKFLOW_GSTAMP_SIGN_WATER="水印";
intl.WORIFLOW_STAMP_TYPE_DATE="日期";
intl.WORKFLOW_CUSTOMSIGN_ONEPERSONSIGN="签名";
intl.WORKFLOW_CUSTOMSIGN_NUMBERSIGN="数字签名";
intl.WORKFLOW_CUSTOMSIGN_HANDSIGN="手写签名";
intl.WORKFLOW_CUSTOMSIGN_HANDBALKSIGN="手写板签名";
intl.WORKFLOW_CUSTOMSIGN_PUBLIC_CER_SIGN="公共证书签名";
intl.WORKFLOW_CUSTOMSIGN_ORDINARYSIGN="普通签名";
intl.WORKFLOW_CREATE_STAMP_INFO="添加批注";
intl.WORKFLOW_SENDEMAIL_TABLE_TITLE="主题";
intl.WORKFLOW_QR_CODE="二维码";
intl.SIGN_FILE_SAVE="保存";
intl.WORKFLOW_CUSTOMSIGN_ONEPERSONSIGN_DOWNLOAD="下载";
intl.UPLOAD_STAMP="上传印章";


intl.languageCrossBar="zh-CN";//语言
intl.languageUnderline="zh_CN";//语言
intl.ordinarySignStartFlag=false;//普通签名开启标识 true启动
intl.numberSignStartFlag=true;//数字签名开启标识
intl.publicCerSignStartFlag=false;//公共证书签名开启标识
intl.pleaceAddKey="数字签名未成功,请插入USB-KEY(数字证书),或检查KEY驱动";
intl.KEY_NO_BIND_CONFIG="Key未绑定印章，前去配置";
intl.GO_TO_CONFIG="前去配置";


$(function(){
    $("#WORKFLOW_END_WORKFLOW_SIGN_LOAD").text(intl.WORKFLOW_END_WORKFLOW_SIGN_LOAD);
    $("#WORKFLOW_END_WORKFLOW_PDF_LOAD").text(intl.WORKFLOW_END_WORKFLOW_PDF_LOAD);
    $("#WORKFLOW_END_WORKFLOW_PDF_IMAGE_LOAD").text(intl.WORKFLOW_END_WORKFLOW_PDF_IMAGE_LOAD);
});

//判断当前用户是否支持服务器签名 isThisUserServerSign
intl.isThisUserServerSign=false;
intl.signno="/fileVisit/upfile/stamp/signno.png";
intl.NO_STAMP_CLICK="您还没有电子印章哦点击";
intl.NO_STAMP_APPAY="申请";