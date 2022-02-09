package com.susu.utils;

/**
 * <p>Description:测试类</p>
 * @author sujay
 * @version  0:00 2022/1/24
 */
public class Test {


    public static void main(String[] args) {

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 99999; i++) {

        }
        long end1 = System.currentTimeMillis();
        System.out.println(end1 - start1);
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 99999; i++) {

        }
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - start2);
    }

}
