package cn.micro.biz.commons.utils;

import cn.micro.biz.commons.exception.MicroErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 非对称加密算法RSA算法组件
 * <p>
 * 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 * <p>
 * 流程：
 * 1.前端：使用公钥加密传输给后端
 * 2.后端：使用私钥解密前端传输过来的内容
 * 3.后端：使用私钥+随机盐加密后入库
 * 4.后端使用公钥解密后对比密码
 *
 * @author lry
 */
@Slf4j
public class RSAUtils {

    private static final String SALT_SEPARATOR = ".";

    /**
     * 非对称密钥算法
     */
    private static final String KEY_ALGORITHM = "RSA";
    /**
     * 密钥长度，DH算法的默认密钥长度是1024。密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 512;
    /**
     * 公钥 KEY
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * 私钥 KEY
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    /**
     * 公钥
     */
    private static final byte[] PUBLIC_CODE = Base64.getDecoder().decode(
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJJUL6PraepgV77xARtObesZl7W+9o6nNb3" +
                    "byiFpQeFITL+JYgcG36r3wB2gUyD8RlksvFVhFPZBREUeAXtTMzUCAwEAAQ==");
    /**
     * 私钥
     */
    private static final byte[] PRIVATE_CODE = Base64.getDecoder().decode(
            "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAklQvo+tp6mBXvvEBG05" +
                    "t6xmXtb72jqc1vdvKIWlB4UhMv4liBwbfqvfAHaBTIPxGWSy8VWEU9kFERR4Be1M" +
                    "zNQIDAQABAkBAJk4YY600RfZRzCA7E2AW0Ep1L/mxQlbKHB/6E8YA996PR/7kFy5" +
                    "PjvwR2wugF4R8jpMmMQoRBRbZ8yGRmVGlAiEA8HKdEbmu8eetkfVI6ctc8s065Hi" +
                    "fsERATPthS+vQwA8CIQCbyyGzyL2eImdmeMFb2iAOaVY4OoZsUCQpnNobvt5UewI" +
                    "gS2cl6pEYU7QbslIBc4/arim99jf9nAFLSNjsQirJY/ECIQCJ3MVkHL1/FB7AFRe" +
                    "2Ol2noxtDArc1Xe3CTSTM7Kg9xQIhAIpmV4s0hSAd6+M65ZdtxZE3o5m09t/6j3I" +
                    "0x7O8gX4J");

    public static void main(String[] args) throws Exception {
        //初始化密钥
        //生成密钥对
        Map<String, Object> keyMap = initKey();
        //公钥
        byte[] publicKey = getPublicKey(keyMap);
        //私钥
        byte[] privateKey = getPrivateKey(keyMap);
        System.out.println("公钥：\n" + Base64.getEncoder().encodeToString(PUBLIC_CODE));
        System.out.println("私钥：\n" + Base64.getEncoder().encodeToString(PRIVATE_CODE));

        String str = "RSA密码交换算法";
        System.out.println("原文:" + str);
        String code1 = encryptByPrivateKeyHex(str);
        System.out.println("私钥加密后的数据：" + code1);
        String decode1 = decryptByPublicKeyHex(code1);
        System.out.println("公钥解密后的数据：" + decode1);

        str = "乙方向甲方发送数据RSA算法";
        System.out.println("原文:" + str);
        String code2 = encryptByPublicKeyHex(str);
        System.out.println("公钥加密后的数据：" + code2);
        String decode3 = decryptByPrivateKeyHex(code2);
        System.out.println("私钥解密后的数据：" + decode3);
    }

    /**
     * The build random salt str
     *
     * @return salt str
     */
    public static String randomSalt() {
        return UUID.randomUUID().toString().replace("-", "").substring(8, 24);
    }

    /**
     * pwd加密密码
     * <p>
     * 私钥解密后使用私钥加盐加密
     *
     * @param data public encrypt string
     * @param salt salt
     * @return private encrypt string
     */
    public static String encryptPwd(String data, String salt) {
        return encryptByPrivateKeyHex(decryptByPrivateKeyHex(data), salt);
    }

    /**
     * password加密
     *
     * @param pwd pwd string
     * @return password string
     */
    public static String encryptPassword(String pwd) {
        return DigestUtils.sha1Hex(pwd).toLowerCase();
    }

    /**
     * The check password
     *
     * @param checkPassword need check md5 password
     * @param dbPassword    password
     * @param salt          salt
     * @return check password result
     */
    public static boolean checkNotEquals(String checkPassword, String dbPassword, String salt) {
        String pwd = encryptPwd(checkPassword, salt);
        return !encryptPassword(pwd).equals(dbPassword);
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param salt 盐值
     * @return byte 加密数据
     */
    public static String encryptByPrivateKeyHex(String data, String salt) {
        return encryptByPrivateKeyHex(data + SALT_SEPARATOR + salt);
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @return byte 加密数据
     */
    public static String encryptByPrivateKeyHex(String data) {
        try {
            byte[] dataByte = encryptByPrivateKey(data.getBytes(StandardCharsets.UTF_8), PRIVATE_CODE);
            return Base64.getEncoder().encodeToString(dataByte);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MicroErrorException("加密失败");
        }
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @return byte 解密数据
     */
    public static String decryptByPublicKeyHex(String data) {
        try {
            byte[] dataByte = Base64.getDecoder().decode(data);
            byte[] decryptByte = decryptByPublicKey(dataByte, PUBLIC_CODE);
            return new String(decryptByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MicroErrorException("解密失败");
        }
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param salt 盐值
     * @return byte 加密数据
     */
    public static String encryptByPublicKeyHex(String data, String salt) {
        return encryptByPublicKeyHex(data + SALT_SEPARATOR + salt);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @return byte 加密数据
     */
    public static String encryptByPublicKeyHex(String data) {
        try {
            byte[] dataByte = encryptByPublicKey(data.getBytes(StandardCharsets.UTF_8), PUBLIC_CODE);
            return Base64.getEncoder().encodeToString(dataByte);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MicroErrorException("加密失败");
        }
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @return byte 解密数据
     */
    public static String decryptByPrivateKeyHex(String data) {
        try {
            byte[] dataByte = Base64.getDecoder().decode(data);
            byte[] decryptByte = decryptByPrivateKey(dataByte, PRIVATE_CODE);
            return new String(decryptByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MicroErrorException("解密失败");
        }
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 初始化公钥
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    private static Map<String, Object> initKey() throws Exception {
        // 实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    private static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    private static byte[] getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

}
