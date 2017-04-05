package com.safecode.andcloud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sumy on 2017/4/5.
 */
public class StringUtil {

    private StringUtil() {
    }

    public static boolean isBase64(String str) {
        String regEx = "^[A-Za-z0-9+/]+[=]{0,2}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
