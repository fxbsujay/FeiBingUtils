package com.susu.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * <p>Description: Array sorting</p>
 * <p>排序算法</p>
 * @author sujay
 * @version 10:11 2023/11/3
 *
 * @since JDK1.8
 */
public class ArraySortUtils {

    /**
     * 冒泡排序
     * 依次交换位置
     */
    public static int[] bubble(int[] source) {
        int[] arr = copy(source);
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if(arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    /**
     * 快速排序
     *
     */
    public static int[] quick(int[] source) {
        return quick(copy(source), 0, source.length - 1);
    }

    public static int[] quick(int[] source, int left, int right) {
        if (left < right) {

            int index = left + 1;

            for (int i = index; i <= right; i++) {
                if (source[i] < source[left]) {
                    int temp = source[i];
                    source[i] = source[index];
                    source[index] = temp;
                    index++;
                }
            }

            int temp = source[left];
            source[left] = source[index - 1];
            source[index - 1] = temp;

            quick(source, left, index - 2);
            quick(source, index, right);
        }

        return source;
    }


    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    private static int[] copy(int[] source) {
        return Arrays.copyOf(source, source.length);
    }


    public static void main(String[] args) {

        int length = 10000;
        int[] arr = new int[length];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(100);
        }

        System.out.println(Arrays.toString(arr));

        long startTime = System.currentTimeMillis();
        int[] result = quick(arr);
        long milliseconds = System.currentTimeMillis() - startTime;

        System.out.println(Arrays.toString(result));

        System.out.println("耗时：" + milliseconds + "毫秒");
    }

}
