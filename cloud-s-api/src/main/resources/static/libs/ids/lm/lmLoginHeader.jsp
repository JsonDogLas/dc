<%@ include file="../../../pt/decorators/taglib.jsp"%>
<%@ taglib uri="/WEB-INF/tld/lm_function.tld" prefix="mt" %>
<script type="text/javascript" src="/libs/ids/common/js/base64.js"></script>
<script type="text/javascript" src="/libs/ids/common/js/md5.js"></script>

<script type="text/javascript" src="/libs/ids/lm/base/mToken.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/clmCer.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/clmkey.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/base.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/lmParamConstants.js"></script>

<script type="text/javascript" src="/libs/ids/lm/lang/lmMessage_zh_cn.js"></script>
<script type="text/javascript" src="/libs/ids/lm/local/clmkeyLocalLogin.js"></script>
<script type="text/javascript" src="/libs/ids/lm/local/keyLocalLogin.js"></script>
<script type="text/javascript" src="/libs/ids/lm/local/lmLogin.js"></script>

<script>
	var signRandom =  '${mt:signRandom()}';
</script>