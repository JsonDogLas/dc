1、判断密码输入/获取证书/重置密码，引入js
<script th:src="@{/libs/ids/common/js/base64.js}"></script>
<script th:src="@{/libs/ids/common/js/md5.js}"></script>
   <script th:src="@{/libs/ids/lm/base/mToken.js}"></script>
    <script  th:src="@{/libs/ids/lm/base/clmCer.js}"></script>
  <script  th:src="@{/libs/ids/lm/base/clmkey.js}"></script>
  <script  th:src="@{/libs/ids/lm/base/base.js}"></script>
  <script  th:src="@{/libs/ids/lm/base/lmParamConstants.js}"></script>
  <script  th:src="@{/libs/ids/lm/base/lmInit.js}"></script>

  <script  th:src="@{/libs/ids/lm/lang/lmMessage_zh_cn.js}"></script>
  <script  th:src="@{/libs/ids/lm/local/lmKey.js}"></script>

2、key登录
<script th:src="@{/libs/ids/common/js/base64.js}"></script>
<script th:src="@{/libs/ids/common/js/md5.js}"></script>
 <script th:src="@{/libs/ids/lm/base/mToken.js}"></script>
 <script  th:src="@{/libs/ids/lm/base/clmCer.js}"></script>
<script  th:src="@{/libs/ids/lm/base/clmkey.js}"></script>
<script  th:src="@{/libs/ids/lm/base/base.js}"></script>
<script  th:src="@{/libs/ids/lm/base/lmParamConstants.js}"></script>

<script  th:src="@{/libs/ids/lm/lang/lmMessage_zh_cn.js}"></script>
<script  th:src="@{/libs/ids/lm/local/clmkeyLocalLogin.js}"></script>
<script  th:src="@{/libs/ids/lm/local/keyLocalLogin.js}"></script>

3、客户端签名,若是jsp页面，引用如下
<%@ taglib uri="/WEB-INF/tld/lm_function.tld" prefix="mt" %>
<script type="text/javascript" src="/libs/ids/common/js/base64.js"></script>
<script type="text/javascript" src="/libs/ids/common/js/md5.js"></script>

<script type="text/javascript" src="/libs/ids/lm/base/mToken.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/clmCer.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/clmkey.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/lmParamConstants.js"></script>
<script type="text/javascript" src="/libs/ids/lm/base/lmInit.js"></script>
<script type="text/javascript" src="/libs/ids/lm/lang/lmMessage_zh_cn.js"></script>
<script type="text/javascript" src="/libs/ids/lm/socketsign/lmSocketSign.js"></script>
<script type="text/javascript" src="/libs/ids/lm/socketsign/lmDoSign.js"></script>

<input type="hidden" id="clientVersion"  name="clientVersion" value=${mt:clientVersion()} >
<input type="hidden" id="clientHashType"  name="clientHashType" value=${mt:clientHashType()}>
<input type="hidden" id="clientResponseP1RSASignedType"  name="clientResponseP1RSASignedType" value=${mt:clientResponseP1RSASignedType()}>
<input type="hidden" id="clientResponseP1SM2SignedType"  name="clientResponseP1SM2SignedType" value=${mt:clientResponseP1SM2SignedType()}>
<input type="hidden" id="clientHashMethod"  name="clientHashMethod" value=${mt:clientMethod('3')}>
<input type="hidden" id="clientSignFileId"  name="clientSignFileId" >

4、导入证书
<script th:src="@{/libs/ids/common/js/base64.js}"></script>
<script th:src="@{/libs/ids/common/js/md5.js}"></script>
<script th:src="@{/libs/ids/lm/base/mToken.js}"></script>
<script  th:src="@{/libs/ids/lm/base/clmCer.js}"></script>
<script  th:src="@{/libs/ids/lm/base/clmkey.js}"></script>
<script  th:src="@{/libs/ids/lm/base/base.js}"></script>
<script  th:src="@{/libs/ids/lm/base/lmParamConstants.js}"></script>
<script  th:src="@{/libs/ids/lm/base/lmInit.js}"></script>

<script  th:src="@{/libs/ids/lm/cert/lmCertApp.js}"></script>
<script  th:src="@{/libs/ids/lm/cert/lmCertRequestFunctions.js}"></script>

<script  th:src="@{/libs/ids/lm/lang/lmMessage_zh_cn.js}"></script>
