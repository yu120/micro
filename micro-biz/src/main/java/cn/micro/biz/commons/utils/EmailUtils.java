package cn.micro.biz.commons.utils;

import java.util.regex.Pattern;

public class EmailUtils {

    private static final String PATTERN_FORMAT = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_FORMAT);

    public static boolean validate(String email) {
        return PATTERN.matcher(email).find();
    }

}
