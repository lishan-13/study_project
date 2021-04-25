package com.lishan.sort;

import java.util.Arrays;

public class ShellSort {
    public static void main(String[] args) {
        int[] arr = { 8, 9, 1, 7, 2, 3, 5, 4, 6, 0 };
        //int[] arr = { 8, 9, 1, 7, 5};
        shellSort2(arr); //希尔排序交换式
        System.out.println(Arrays.toString(arr));
    }
    //交换式
    public static void shellSort(int[] arr) {
        //中间变量用于交换两个值
        int temp = 0;
        //求共需要分几次组 也就是几次排序
        for (int gap = arr.length/2;gap>0;gap/=2){ //5 2 1
            //划分元素用于分组配对  一个步长配一对
            for (int i = gap; i < arr.length; i++) {
                // 遍历各组中所有的元素(共gap组，每组有个元素), 步长gap
                for (int j = i - gap; j >= 0; j -= gap) {
                    // 如果当前元素大于加上步长后的那个元素，说明交换
                    if (arr[j] > arr[j + gap]) {
                        temp = arr[j];
                        arr[j] = arr[j + gap];
                        arr[j + gap] = temp;
                    }
                }
            }
        }
    }
    //移位式
    public static void shellSort2(int[] arr) {
        // 增量gap, 并逐步的缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            // 从第gap个元素，逐个对其所在的组进行直接插入排序
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                int temp = arr[j];
                while (j - gap >=0 && temp<arr[j-gap]){
                    arr[j] = arr[j-gap];
                    j-=gap;
                }
                //当退出while后，就给temp找到插入的位置
                arr[j] = temp;
            }
        }
    }
}
