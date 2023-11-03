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

    public static final String RANDOM_KEY = "a1b2c3d4e5f6g7h8r9g0k1l2m3n4o5p6q7i8s9t0uvwxyz";


    /**
     *
     * <p>Description: Generate random number</p>
     * <p>获取随机字符串</p>
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandom(int length) {
        return getRandom(length, RANDOM_KEY);
    }

    /**
     * <p>Description: Generate random number</p>
     * <p>获取随机字符串</p>
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandom(int length, boolean upperCase) {
        return getRandom(length, RANDOM_KEY, upperCase);
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
}
