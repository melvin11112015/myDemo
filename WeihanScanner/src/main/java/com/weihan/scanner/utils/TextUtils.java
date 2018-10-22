package com.weihan.scanner.utils;

import java.util.regex.Pattern;

public class TextUtils {
    public static boolean isIntString(String s) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(s).matches();
    }
}
