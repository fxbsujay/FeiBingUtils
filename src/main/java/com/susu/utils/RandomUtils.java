package com.susu.utils;

import java.util.Random;

/**
 * <p>Description: Generate random number</p>
 * <p>生成随机数</p>
 * @author sujay
 * @version 20:59 2022/1/25
 * @since JDK1.8
 */
public class RandomUtils {

    public static final Random RANDOM = new Random();

    /**
     * 纯数字
     */
    public static final String NUMBER = "0123456789";

    /**
     * 小写英文字母
     */
    public static final String SMALL_LETTERS = "abcdefghrjklmnopqistuvwxyz";

    /**
     * 大写英文字母
     */
    public static final String BIG_LETTERS = "ABCDEFGHRJKLMNOPQISTUVWXYZ";

    /**
     * 符号
     */
    public static final String SYMBOLS = "#@!%_&./^?";

    /**
     * 数字 + 小写英文字母
     */
    public static final String DEFAULT = NUMBER + SMALL_LETTERS;

    /**
     * 大小写英文字母
     */
    public static final String LETTERS = SMALL_LETTERS + BIG_LETTERS;

    /**
     * 数字 + 大小写英文字母
     */
    public static final String LETTERS_NUMBER = NUMBER + LETTERS;

    /**
     *
     * <p>Description: Generate random number</p>
     * <p>获取随机字符串</p>
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandom(int length) {
        return getRandom(length, false);
    }

    /**
     * <p>Description: Generate random number</p>
     * <p>获取随机字符串</p>
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandom(int length, boolean upperCase) {
        return getRandom(length, DEFAULT, upperCase);
    }

    /**
     * <p>Description: Generate random number</p>
     * <p>获取随机字符串</p>
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandom(int length, String key) {
        return getRandom(length, key, false);
    }

    /**
     * <p>Description: Generate random number</p>
     * <p>获取随机字符串</p>
     * @param length 字符串长度
     * @param upperCase 是否小写字母转大写字母
     * @return 随机字符串
     */
    public static String getRandom(int length, String key, boolean upperCase) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = RANDOM.nextInt(key.length());
            sb.append(key.charAt(number));
        }
        String s = sb.toString();
        return upperCase ? s.toUpperCase() : s;
    }

    public static void main(String[] args) {
        System.out.println(getRandom(6));
        System.out.println(getRandom(6, true));
        System.out.println(getRandom(6, NUMBER));
        System.out.println(getRandom(6, LETTERS));
        System.out.println(getRandom(6, LETTERS_NUMBER));
        System.out.println(getRandom(6, SYMBOLS));
    }
}
