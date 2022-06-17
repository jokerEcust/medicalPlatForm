package com.ecust.sort;

import java.util.Arrays;

public class BucketSort {
    public static void main(String[] args) {
        int[] arr=new int[]{8,2,3,4,3,6,3,2,9};
        int max=Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max=Math.max(max,arr[i]);
        }
        bucketSort(arr,max+1);
        System.out.println(Arrays.toString(arr));
    }

    public static void bucketSort(int[] arr,int max){
        if(arr==null||max<1){
            return;
        }
        int[] buckets=new int[max];
        for (int i = 0; i < arr.length; i++) {
            buckets[arr[i]]++;
        }
        for (int i = 0,j=0; i < max; i++) {
            while (buckets[i]>0){
                buckets[i]--;
                arr[j++]=i;
            }
        }
        buckets=null;
    }
}
