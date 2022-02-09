package com.susu.utils;

import java.util.UUID;

/**
 * <p>Description: String processing class</p>
 * <p>字符串处理工具类</p>
 * @author sujay
 * @version 21:19 2022/1/20
 * @see String
 * @since JDK1.8
 */
public class StringUtils {


    /**
     * <p>Description: Are they all numbers</p>
     * <p>是否全是数字</p>
     * <pre>
     * StringUtils.isAllDigital("123.45")    = false
     * StringUtils.isAllDigital("12345ABC")  = false
     * StringUtils.isAllDigital(" 12345")    = false
     * StringUtils.isAllDigital("00000")     = true
     * StringUtils.isAllDigital("123456")    = true
     * StringUtils.isAllDigital("")          = true
     * </pre>
     */
    public static boolean isAllDigital(String str) {
        char[] cs = str.toCharArray();
        boolean result = true;
        for (char c : cs) {
            if (!Character.isDigit(c)) {
                result = false;
                break;
            }
        }
        return result;
    }


    /**
     * <p>Description: First capital letter</p>
     * <p>首字母大写,会去掉字符串前后空格</p>
     */
    public static String firstLetterBig(String str) {
        if (isBlank(str)){
            return str;
        }
        char[] cs = dispelBlank(str).toCharArray();
        if (cs[0] >= 'A' && cs[0] <= 'Z'){
            return str;
        }
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * <p>Description: First letter lowercase</p>
     * <p>首字母小写</p>
     */
    public static String firstLetterSmall(String str) {
        if (isBlank(str)){
            return str;
        }
        char[] cs = dispelBlank(str).toCharArray();
        if (cs[0] >= 'a' && cs[0] <= 'z'){
            return str;
        }
        cs[0] += 32;
        return String.valueOf(cs);
    }

    /**
     * <p>Description: get uuid</p>
     * <p>获取UUID</p>
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
    }

    /**
     * <p>Description: Clear the space around the string</p>
     * <p>清除字符串两边空白</p>
     */
    public static String dispelBlank(String str) {
        return isNotBlank(str) ? str.trim() : "";
    }


    /**
     * <p>Description: Clear all blanks in string</p>
     * <p>清除字符串中全部空白</p>
     */
    public static String dispelBlankAll(String str) {
        if (isEmpty(str)){
            return "";
        }
        StringBuilder sb = new StringBuilder(str);
        int index = 0;
        while (sb.length() > index) {
            if (Character.isWhitespace(sb.charAt(index))) {
                sb.deleteCharAt(index);
            } else {
                index++;
            }
        }
        return sb.toString();
    }

    /**
     *  <p>Description: Convert object to string</p>
     *  <p>转为String</p>
     */
    public static String toString(Object object) {
        if (object != null && object.toString().length() > 0) {
            return object.toString();
        }
        return "";
    }



    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     * <p>not empty and not null and not whitespace only</p>
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     *
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * <p>Checks if a CharSequence is not empty ("") and not null.</p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static void main(String[] args) {
        String str = "  Hello Word Java ";
        Object obj = "  Hello Word Java ";
        System.out.println("删除前后空白" + dispelBlank(str));
        System.out.println("删除字符串中所有空白" + dispelBlankAll(str));
        System.out.println("字符串转换" + toString(obj));
        System.out.println("首字母大写" + firstLetterBig(" hello"));
        System.out.println("首字母小写" + firstLetterSmall(" Hello Word"));
        System.out.println("是否全是数字" + isAllDigital(" 0000"));
    }

}
