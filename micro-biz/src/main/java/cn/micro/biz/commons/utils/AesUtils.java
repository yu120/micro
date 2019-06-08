package cn.micro.biz.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES Utils
 *
 * @author lry
 */
public class AesUtils {

    private static final String ENCODE_RULES = "abc123def";

    /**
     * encode
     *
     * @param content content
     * @return encoded content
     */
    public static String encode(String content) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(ENCODE_RULES.getBytes());
        keyGen.init(128, random);
        SecretKey originalKey = keyGen.generateKey();
        byte[] raw = originalKey.getEncoded();
        SecretKey key = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);
        byte[] byteAES = cipher.doFinal(byteEncode);

        return Base64.getEncoder().encodeToString(byteAES);
    }

    /**
     * decode
     *
     * @param content content
     * @return decoded content
     */
    public static String decode(String content) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(ENCODE_RULES.getBytes());
        keyGen.init(128, random);
        SecretKey originalKey = keyGen.generateKey();
        byte[] raw = originalKey.getEncoded();
        SecretKey key = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] byteContent = Base64.getDecoder().decode(content);
        byte[] byteDecode = cipher.doFinal(byteContent);

        return new String(byteDecode, StandardCharsets.UTF_8);
    }

}
