package com.lishan.search;
/**
 * 插值查找 在二分查找的基础上改进了mid的求去方法
 */
public class InsertValueSearch {
    public static void main(String[] args) {
        int [] arr = new int[100];
		for(int i = 0; i < 100; i++) {
			arr[i] = i + 1;
		}
        int index = insertValueSearch(arr, 0, arr.length - 1, 100);
        System.out.println("index = " + index);
    }

    public static int insertValueSearch(int[] arr, int left, int right, int findVal) {
        if (left > right || findVal < arr[0] || findVal > arr[arr.length-1]){
            return -1;
        }
        // 求出mid, 自适应
        int mid = left + (right - left) * (findVal - arr[left]) / (arr[right] - arr[left]);

        if (findVal < arr[mid]){
            return insertValueSearch(arr,left,mid-1,findVal);
        }else if (findVal > arr[mid]){
            return insertValueSearch(arr,mid+1,right,findVal);
        }else {
            return mid;
        }
    }
}
