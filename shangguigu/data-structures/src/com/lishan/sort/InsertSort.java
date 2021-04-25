package com.lishan.sort;

import java.util.Arrays;

public class InsertSort {
    public static void main(String[] args) {
        int[] arr = {101, 34, 119, 1, -1, 89};
        insertSort(arr); //调用插入排序算法
        System.out.println(Arrays.toString(arr));
    }

    public static void insertSort(int[] arr) {
        //定义两个变量 一个存储需要插入的值，一个储存待插入数的前一个位置索引 用于判断是否插入
        int insertVal = 0;
        int insertIndex = 0;
        //从i=1开始  因为第一个默认有序
        for (int i=1;i<arr.length;i++){
            insertVal = arr[i];
            insertIndex = i-1;
            //开始从后往前查找直到找到需要插入的位置 即是arr[insertIndex] < insertVal
            while(insertIndex >=0 && arr[insertIndex] > insertVal){
                //将所有值往前移一位
                arr[insertIndex+1] = arr[insertIndex];
                insertIndex--;
            }
            if (insertIndex + 1 != i){ //如果位置没发生改变
                //将待插入的值 插入到该插入的位置
                arr[insertIndex+1]  =insertVal;
            }
        }
    }
}
