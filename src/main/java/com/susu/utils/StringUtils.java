package com.susu.utils;

/**
 * <p>Description: String processing class</p>
 * @author sujay
 * @version 21:19 2022/1/20
 * @see java.lang.String
 * @since JDK1.8
 */
public class StringUtils {

    /**
     * <p>Description: title case</p>
     * <p>首字母大写</p>
     */
    public static String initialBig(String str) {
        char[] cs = str.toCharArray();
        System.out.println(cs[0]);
        if (cs[0] >= 'A' && cs[0] <= 'Z'){
            return str;
        }
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * <p>Description: initial lowercase</p>
     * <p>首字母小写</p>
     */
    public static String initialSmall(String str) {
        char[] cs=str.toCharArray();
        if (cs[0] >= 'a' && cs[0] <= 'z'){
            return str;
        }
        cs[0] += 32;
        return String.valueOf(cs);
    }

    /**
     * <p>Description: Clear the space around the string</p>
     */
    public static String dispelBlank(String str){
        return isNotBlank(str) ? str.trim() : "";
    }


    /**
     * <p>Description: Clear all blanks in string</p>
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
        System.out.println(dispelBlank(str));
        System.out.println(dispelBlankAll(str));
        System.out.println(toString(obj));
    }

}
