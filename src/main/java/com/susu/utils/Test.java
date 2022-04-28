package com.susu.utils;

/**
 * <p>Description:测试类</p>
 * @author sujay
 * @version  0:00 2022/1/24
 */
public class Test {


    public static void main(String[] args) {
        String a = "3";
        byte[] bytes = a.getBytes();
        System.out.println(bytes[0]);
        System.out.println(0xFF);
        System.out.println(0xFF & bytes[0]);
        System.out.println(Integer.toHexString(0xFF & bytes[0]));
    }

}
