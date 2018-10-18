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

<script type="text/javascript" src="/libs/ids/lm/cert/lmCertApp.js"></script>
<script type="text/javascript" src="/libs/ids/lm/cert/lmCertRequestFunctions.js"></script>

<input type="hidden" id="clientVersion"  name="clientVersion" value=${mt:clientVersion()} >

<input type="hidden" id="clientRequestFromP10ForSignCertType"  name="clientRequestFromP10ForSignCertType" value="10"}>
<input type="hidden" id="clientRequestFromP10ForSignCertMethod"  name="clientRequestFromP10ForSignCertMethod" value="requestFromP10ForSignCert">

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" style="height:100%;width:100%;">
<head>
	<script>

	</script>
</head>
</html>
