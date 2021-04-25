package com.lishan.sort;

import java.util.Arrays;

public class SelectSort {
    public static void main(String[] args) {
        int [] arr = {101, 34, 119, 1, -1, 90, 123};
        selectSort(arr);
        System.out.println(Arrays.toString(arr));

    }

    public static void selectSort(int[] arr) {
        for (int i=0;i<arr.length-1;i++){
            //存储两个变量用于标记最小值
            int minindex = i;
            int min = arr[i];
            for (int j=i+1;j<arr.length;j++){
                if (arr[j]<min){
                    //标记最小值
                    minindex = j;
                    min = arr[j];
                }
            }
            if (minindex != i){
                //如果找到最小值 交换
                arr[minindex] = arr[i];
                arr[i] = min;
            }
        }
    }

}
