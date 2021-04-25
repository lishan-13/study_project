package com.lishan.sort;

import java.util.Arrays;

/**
 * 基数排序
 */
public class RadixSort {
    public static void main(String[] args) {
        int[] arr = { 53, 3, 542, 748, 14, 214};
        radixSort(arr);
        System.out.println("基数排序后 " + Arrays.toString(arr));
    }

    public static void radixSort(int[] arr) {
        //首先要知道排序几轮  就是最大数的位数
        int max = arr[0];
        for(int i:arr){
            if (i>max){
                max = i;
            }
        }
        int maxLength = (max+"").length();
        //定义十个桶  每个桶最多放arr.length个元素，空间换时间
        int[][] bucket = new int[10][arr.length];
        //用于统计每个桶有多少个元素
        int[] bucketElementCounts = new int[10];
        //n用于获取每位元素
        for (int i=0,n=1;i<maxLength;i++,n*=10){
            for (int j = 0;j<arr.length;j++){
                int digitOfElement = arr[j] / n % 10;
                //bucketElementCounts[digitOfElement] 表示第几个桶的第几个元素
                bucket[digitOfElement][bucketElementCounts[digitOfElement]] = arr[j];
                //将桶中的元素个数++
                bucketElementCounts[digitOfElement]++;
            }
            //将桶中的数据取出放入原数组
            int index = 0;
            //遍历每一个桶  只需要遍历bucketElementCounts数组即可  原因见下面
            for (int k = 0;k<bucketElementCounts.length;k++){
                // 先看桶中有没有数据  只有当有数据时才放入原数组
                if (bucketElementCounts[k] != 0){
                    //遍历 每个桶中元素个数那么多次；bucketElementCounts[k]表示每个桶的元素个数
                    for (int l=0;l<bucketElementCounts[k];l++){
                        arr[index] = bucket[k][l];
                        index++;
                    }
                }
                //将桶中的元素取出 所以需要将标记个数清0
                bucketElementCounts[k] = 0;

                //为什么不把桶bucket清0
                //因为：下次往桶中放数据时就会将原数据覆盖掉，而且只会取到标记个数bucketElementCounts
                //那么多个原始就算有没有覆盖的元素也不会取到
            }
        }
    }
}
