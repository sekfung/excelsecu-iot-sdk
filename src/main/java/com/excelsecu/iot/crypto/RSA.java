package com.excelsecu.iot.crypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author sekfung
 * @date 2021/12/31
 */
public class RSA {
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";

    public static String encryptOAEP(String message, X509Certificate certificate) throws IllegalBlockSizeException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(ciphertext);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("not supportRSA v1.5/OAEP.", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("invalid certificate.", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("invalid block size.");
        }
    }

    public static String decryptOAEP(String ciphertext, PrivateKey privateKey) throws BadPaddingException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] data = Base64.getDecoder().decode(ciphertext);
            return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("not support RSA v1.5/OAEP.", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("invalid key.", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new BadPaddingException("decrypt failed.");
        }
    }

    public static String sign(byte[] data, byte[] priKey, String algorithm) throws Exception {

        PKCS8EncodedKeySpec pksc = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyfactory = KeyFactory.getInstance("RSA");
        PrivateKey pk = keyfactory.generatePrivate(pksc);
        java.security.Signature signature = Signature.getInstance(algorithm);
        signature.initSign(pk);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }
}
