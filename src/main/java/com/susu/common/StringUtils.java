package com.susu.common;

/**
 * <p> Description： 字符串处理类 </p> <br/>
 * @author sujay
 * @version  21:19 2022/1/20
 * @see com.susu.common.StringUtils
 * @since JDK1.8
 */
public class StringUtils {


    /**
     * <p> Description： 清空字符串两边空格 </p>
     */
    public static String clearBlank(String str){
        return isNotBlank(str) ? str.trim() : "";
    }

    /**
     *  <p> Description： 将对象转化为字符串 </p>
     */
    public static String toString(Object object) {
        if (object != null && object.toString().length() > 0) {
            return object.toString();
        }
        return "";
    }


    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

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

        System.out.println(isNotBlank(" "));
    }

}
