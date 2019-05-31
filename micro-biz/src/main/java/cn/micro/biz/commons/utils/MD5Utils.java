package cn.micro.biz.commons.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class MD5Utils {

    private static final String PREFIX = "micro";

    /**
     * The build random salt str
     *
     * @return salt str
     */
    public static String randomSalt() {
        return UUID.randomUUID().toString().replace("-", "").substring(8, 24);
    }

    /**
     * The encode password
     *
     * @param password
     * @return
     */
    public static String encode(String password) {
        return DigestUtils.sha1Hex(password).toLowerCase();
    }

    /**
     * The encode password
     *
     * @param password md5 password
     * @param salt     salt
     * @return encode password result
     */
    public static String encode(String password, String salt) {
        return encode(PREFIX + password + salt);
    }

    /**
     * The check password
     *
     * @param checkPassword need check md5 password
     * @param password      password
     * @param salt          salt
     * @return check password result
     */
    public static boolean checkNotEquals(String checkPassword, String password, String salt) {
        return !password.equals(encode(checkPassword, salt));
    }

}
