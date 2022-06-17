package com.ecust.sort;

import java.util.Arrays;

public class RadixSort {
    public static void main(String[] args) {
        int a[] = {53, 3, 542, 748, 14, 214, 154, 63, 616};
        radixSort(a);
        System.out.println(Arrays.toString(a));
    }

    private static void radixSort(int[] a) {
        int max=Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            max=Math.max(max,a[i]);
        }
        for (int i = 1; max/i >0 ; i*=10) {
            bucketSort(a,i);
        }
    }
    public static void bucketSort(int[] a,int exp){
        int[] output = new int[a.length];    // 存储"被排序数据"的临时数组
        int[] buckets = new int[10];

        // 将数据出现的次数存储在buckets[]中
        for (int i = 0; i < a.length; i++)
            buckets[ (a[i]/exp)%10 ]++;

        // 更改buckets[i]。目的是让更改后的buckets[i]的值，是该数据在output[]中的位置。
        for (int i = 1; i < 10; i++)
            buckets[i] += buckets[i - 1];

        // 将数据存储到临时数组output[]中
        for (int i = a.length - 1; i >= 0; i--) {
            output[buckets[ (a[i]/exp)%10 ] - 1] = a[i];
            buckets[ (a[i]/exp)%10 ]--;
        }

        // 将排序好的数据赋值给a[]
        for (int i = 0; i < a.length; i++)
            a[i] = output[i];

        output = null;
        buckets = null;
    }
}
