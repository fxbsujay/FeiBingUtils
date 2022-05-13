package com.susu.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: Regex</p>
 * <p>正则表达式</p>
 * @author sujay
 * @version 18:36 2022/5/5
 * @since JDK1.8 <br/>
 */
public class RegexUtils {

    /**
     * <p>Description: integer</p>
     * <p>整数</p>
     */
    public static final String INTEGER = "-?[1-9]\\d*";

    /**
     * <p>Description: positive integer</p>
     * <p>正整数</p>
     */
    public static final String POSITIVE_INTEGER = "[1-9]\\d*";

    /**
     * <p>Description: negative  integer</p>
     * <p>负整数</p>
     */
    public static final String NEGATIVE_INTEGER = "-[1-9]\\d*";

    /**
     * <p>Description: Floating point number</p>
     * <p>浮点数</p>
     */
    public static final String FLOAT = "-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)";

    /**
     * <p>Description: positive Floating point number</p>
     * <p>正浮点数</p>
     */
    public static final String POSITIVE_FLOAT = "[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*";

    /**
     * <p>Description: negative Floating point number</p>
     * <p>负浮点数</p>
     */
    public static final String NEGATIVE_FLOAT = "[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*";

    /**
     * <p>Description: IP</p>
     * <p>ip地址</p>
     */
    public static final String IP = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * <p>Description: China ID Card</p>
     * <p>身份证号</p>
     */
    public static final String ID_NUMBER = "\\d{17}[\\d|x]|\\d{15}";

    /**
     * <p>Description: chinese</p>
     * <p>汉字</p>
     */
    public static final String CHINESE_CHARACTERS = "[\\u4e00-\\u9fa5]";

    /**
     * <p>Description: e-mail address</p>
     * <p>邮箱地址</p>
     */
    public static final String EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    /**
     * <p>Description: website</p>
     * <p>网址</p>
     */
    public static final String WEBSITE = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";

    /**
     * <p>Description: China mobile number</p>
     * <p>中国内地手机号</p>
     */
    public static final String PHONE_NUMBER = "0?(13|14|15|17|18)[0-9]{9}";

    /**
     * <p>Description: China telephone number</p>
     * <p>中国内地电话号</p>
     */
    public static final String TELEPHONE_NUMBER = "[0-9-()（）]{7,18}";

    /**
     * <p>Description: ${key}</p>
     * <p>能够匹配字符串中以${key}形式的文本(其中key为小写英文字母)</p>
     */
    public static final String KEY_$ = "\\$\\{([a-z]+)\\}";

    /**
     * <p>Description: ${key}</p>
     * <p>可以用来检测文本中是否有${key}形式的文本</p>
     */
    public static final String IS_KEY_$ = ".*\\$\\{([a-z]+)\\}.*";

    public static boolean check(String line,String pattern) {
       return Pattern.matches(pattern, line);
    }

    public static List<String> getValues(String line, String pattern) {
        Pattern compile = Pattern.compile(pattern);
        Set<String> set = new LinkedHashSet<>();
        Matcher matcher = compile.matcher(line);
        while (matcher.find()) {
            set.add(matcher.group(1));
        }
        return new ArrayList<>(set);
    }
}
