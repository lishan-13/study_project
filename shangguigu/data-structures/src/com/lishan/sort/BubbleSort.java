package com.lishan.sort;

import java.util.Arrays;

public class BubbleSort {
    public static void main(String[] args) {
        int arr[] = {3, 9, -1, 10, 20};
        bubbleSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void bubbleSort(int[] arr) {
        //只需要排 len-1次即可，因为每次都会将最大的数排到后面，后面的排完了最后一个就不用排了就是最小的
        int temp = 0;
        //定义一个变量优化 如果某次排序顺序都没有变 则说明排序完毕
        boolean flag = false; //是否改变
        for (int i=0;i<arr.length-1;i++){
            //每次 都会确定一个数，所以每次都少一个数
            for (int j=0;j<arr.length-1-i;j++){
                if (arr[j]>arr[j+1]){
                    flag = true;
                    temp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                }
            }
            if (!flag){ //如果没变
                break;
            }else {
                flag = false; // // 重置flag!!!, 进行下次判断
            }
        }
    }
}
