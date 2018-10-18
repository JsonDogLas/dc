<%@ include file="../../../pt/decorators/cli/base_header_cssjs.jsp"%>
<%@ include file="../../../pt/decorators/taglib.jsp"%>
<%@ taglib uri="/WEB-INF/tld/lm_function.tld" prefix="mt" %>
<script type="text/javascript" src="/libs/ids/common/js/base64.js"></script>
<script type="text/javascript" src="/libs/ids/common/js/md5.js"></script>

<script type="text/javascript" src="/libs/ids/lm/base/mToken.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/clmCer.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/clmkey.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/lmMessage_zh_cn.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/lmParamConstants.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/lmInit.js"></script>

<script type="text/javascript" src="/libs/ids/lm/socketsign/lmSocketSign.js"></script>
<script type="text/javascript" src="/libs/ids/lm/socketsign/lmDoSign.js"></script>

<input type="hidden" id="clientVersion"  name="clientVersion" value=${mt:clientVersion()} >
<input type="hidden" id="clientHashType"  name="clientHashType" value=${mt:clientHashType()}>
<input type="hidden" id="clientResponseP1RSASignedType"  name="clientResponseP1RSASignedType" value=${mt:clientResponseP1RSASignedType()}>
<input type="hidden" id="clientResponseP1SM2SignedType"  name="clientResponseP1SM2SignedType" value=${mt:clientResponseP1SM2SignedType()}>

<input type="hidden" id="clientHashMethod"  name="clientHashMethod" value=${mt:clientMethod('3')}>

<input type="hidden" id="clientSignFileId"  name="clientSignFileId" >
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" style="height:100%;width:100%;">
<head>
	<script>
	</script>
</head>
</html>
