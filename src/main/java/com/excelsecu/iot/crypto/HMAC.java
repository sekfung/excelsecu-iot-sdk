package com.excelsecu.iot.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author sekfung
 * @date 2022/1/4
 */
public class HMAC {
    public static String sign(byte[] content, byte[] secret, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac macer = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(secret, algorithm);
        macer.init(secretKey);
        byte[] array = macer.doFinal(content);
        return Base64.getEncoder().encodeToString(array);
    }
}
