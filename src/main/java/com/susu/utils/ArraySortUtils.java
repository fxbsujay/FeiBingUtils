package com.susu.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * <p>Description: Array sorting</p>
 * <p>排序算法：冒泡排序 快速排序 选择排序 希尔排序 插入排序 归并排序 计数排序 堆排序 基数排序 桶排序</p>
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
     */
    public static int[] quick(int[] source) {
        return quick(copy(source), 0, source.length - 1);
    }

    private static int[] quick(int[] source, int left, int right) {
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

    /**
     * 选择排序
     */
    public static int[] selection(int[] source) {
        int[] arr = copy(source);

        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }

            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
        return arr;
    }

    /**
     * 希尔排序
     */
    public static int[] shell(int[] source) {
        int[] arr = copy(source);

        int gap = 1;
        while (gap < arr.length) {
            gap = gap * 3 + 1;
        }

        while (gap > 0) {
            for (int i = gap; i < arr.length; i++) {
                int tmp = arr[i];
                int j = i - gap;
                while (j >= 0 && arr[j] > tmp) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }
                arr[j + gap] = tmp;
            }
            gap = (int) Math.floor((double) gap / 3);
        }
        return arr;
    }

    /**
     * 插入排序
     */
    public static int[] insert(int[] source) {
        int[] arr = copy(source);

        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i;
            while (j > 0 && tmp < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }
            if (j != i) {
                arr[j] = tmp;
            }
        }
        return arr;
    }

    /**
     * 归并排序
     */
    public static int[] merge(int[] source) {
        int[] arr = copy(source);
        if (arr.length < 2) {
            return arr;
        }

        int middle = (int) Math.floor((double) arr.length / 2);

        int[] left = Arrays.copyOfRange(arr, 0, middle);
        int[] right = Arrays.copyOfRange(arr, middle, arr.length);

        return merge(merge(left), merge(right));
    }

    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int l = 0, r = 0, len = 0;

        while (len < left.length + right.length) {
            if (left[l] <= right[r]) {
                result[len++] = left[l++];
                if (l == left.length) {
                    for (int i = r; i < right.length; i++) {
                        result[len++] = right[r++];
                    }
                }
            } else {
                result[len++] = right[r++];
                if (r == right.length) {
                    for (int i = l; i < left.length; i++) {
                        result[len++] = left[l++];
                    }
                }
            }
        }
        return result;
    }

    /**
     * 计数排序
     */
    public static int[] counting(int[] source) {
        int[] arr = copy(source);

        int maxValue = arr[0];
        for (int value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }

        int bucketLen = maxValue + 1;
        int[] bucket = new int[bucketLen];
        for (int value : arr) {
            bucket[value]++;
        }

        int sortedIndex = 0;
        for (int j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                arr[sortedIndex++] = j;
                bucket[j]--;
            }
        }
        return arr;
    }

    /**
     *  堆排序
     */
    public static int[] heap(int[] source) {
        int[] arr = copy(source);
        int len = arr.length;

        for (int i = (int) Math.floor((double) len / 2); i >= 0; i--) {
            heapify(arr, i, len);
        }

        for (int i = len - 1; i > 0; i--) {
            int temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            len--;
            heapify(arr, 0, len);
        }
        return arr;
    }


    private static void heapify(int[] arr, int i, int len) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;

        if (left < len && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < len && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(arr, largest, len);
        }
    }

    /**
     * 基数排序
     */
    public static int[] radix(int[] source) {
        int[] arr = copy(source);

        int maxValue = arr[0];
        for (int value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }

        int lenght = 0;
        if (maxValue == 0) {
            lenght = 1;
        } else {
            for (long temp = maxValue; temp != 0; temp /= 10) {
                lenght++;
            }
        }

        int mod = 10;
        int dev = 1;

        for (int i = 0; i < lenght; i++, dev *= 10, mod *= 10) {
            int[][] counter = new int[mod * 2][0];

            for (int k : arr) {
                int bucket = ((k % mod) / dev) + mod;
                counter[bucket] = Arrays.copyOf(counter[bucket], counter[bucket].length + 1);
                counter[bucket][counter[bucket].length - 1] = k;
            }

            int pos = 0;
            for (int[] bucket : counter) {
                for (int value : bucket) {
                    arr[pos++] = value;
                }
            }
        }

        return arr;
    }

    /**
     * 桶排序
     * @param bucketSize 桶的数量
     */
    public static int[] bucket(int[] source, int bucketSize) {
        int[] arr = copy(source);

        int minValue = arr[0];
        int maxValue = arr[0];
        for (int value : arr) {
            if (value < minValue) {
                minValue = value;
            } else if (value > maxValue) {
                maxValue = value;
            }
        }

        int bucketCount = (int) Math.floor((double) (maxValue - minValue) / bucketSize) + 1;
        int[][] buckets = new int[bucketCount][0];
        for (int j : arr) {
            int index = (int) Math.floor((double) (j - minValue) / bucketSize);
            buckets[index] = Arrays.copyOf(buckets[index], buckets[index].length + 1);
            buckets[index][buckets[index].length - 1] = j;
        }

        int arrIndex = 0;
        for (int[] bucket : buckets) {
            if (bucket.length == 0) {
                continue;
            }
            bucket = insert(bucket);
            for (int value : bucket) {
                arr[arrIndex++] = value;
            }
        }
        return arr;
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
