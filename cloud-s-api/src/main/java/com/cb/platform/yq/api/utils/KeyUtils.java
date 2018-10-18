package com.cb.platform.yq.api.utils;
import com.ceba.forpdf.pdf.OCertUtils;
import org.owasp.esapi.codecs.Base64;
import java.security.cert.X509Certificate;

/**
 * 关于key操作的工具类
 * @author whh
 */
public class KeyUtils {
    public static boolean isKeyUse(String keyInfo)throws Exception {
        X509Certificate certificate = OCertUtils.readBytesToX509Certificate(Base64.decode(keyInfo));
        if(!com.ceba.key.utils.KeyUtils.checkHasSpecialNode(certificate)){
            return false;
        }
        return true;
    }
}
