package com.excelsecu.iot.crypto;

import com.excelsecu.iot.EsIoTApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author sekfung
 * @date 2022/1/4
 */
public class Signature {

    private static final Logger log = LoggerFactory.getLogger(Signature.class);

    public static String sign(String content, String key, SignType signType) throws EsIoTApiException {
        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            if (SignType.RSA2.equals(signType)) {
                return RSA.sign(contentBytes, Base64.getDecoder().decode(keyBytes), "SHA256WithRSA");
            } else if (SignType.HMAC_SHA256.equals(signType)) {
                return HMAC.sign(contentBytes, keyBytes, "HmacSHA256");
            } else {
                log.error("not support sign type.");
                throw new EsIoTApiException("not support sign type");
            }
        } catch (Exception e) {
            log.error("sign exception.", e);
            throw new EsIoTApiException("sign exception.", e);
        }
    }

}
