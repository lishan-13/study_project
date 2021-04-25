package com.lishan.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分查找法
 */
public class BinarySearch {
    public static void main(String[] args) {
        int arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 , 11, 12, 13,14,15,16,17,18,19,20 };
        int[] arr2 = {1,8, 10, 89, 1000, 1000,1234};
        List<Integer> resIndex = binarySearch2(arr2, 0, arr2.length - 1, 1000);
		System.out.println("resIndex=" + resIndex);
    }

    public static int binarySearch(int[] arr, int left, int right, int findVal) {
        if (left > right){
            return -1;
        }
        int mid = (left+right)/2;
        if (findVal < arr[mid]){
            return binarySearch(arr,left,mid-1,findVal);
        }else if (findVal > arr[mid]){
            return binarySearch(arr,mid+1,right,findVal);
        }else {
            return mid;
        }
    }
    //完成一个课后思考题:
    /*
     * 课后思考题： {1,8, 10, 89, 1000, 1000，1234} 当一个有序数组中，
     * 有多个相同的数值时，如何将所有的数值都查找到，比如这里的 1000
     * 思路分析
     * 1. 在找到mid 索引值，不要马上返回
     * 2. 向mid 索引值的左边扫描，将所有满足 1000， 的元素的下标，加入到集合ArrayList
     * 3. 向mid 索引值的右边扫描，将所有满足 1000， 的元素的下标，加入到集合ArrayList
     * 4. 将Arraylist返回
     */
    public static List<Integer> binarySearch2(int[] arr, int left, int right, int findVal) {
        if (left > right){
            return new ArrayList<Integer>();
        }
        int mid = (left+right)/2;
        if (findVal < arr[mid]){
            return binarySearch2(arr,left,mid-1,findVal);
        }else if (findVal > arr[mid]){
            return binarySearch2(arr,mid+1,right,findVal);
        }else {

            //return mid;
            List<Integer> resIndexlist = new ArrayList<Integer>();
            int temp = mid-1;
            //向左继续扫描  直到扫描完毕或者值不相等
            while(true){
                if (left<0 || arr[temp] != arr[mid]){
                    break;
                }
                resIndexlist.add(temp);
                //标记位置继续移动
                temp -= 1;
            }
            //把第一个找到的值也加入进去
            resIndexlist.add(mid);

            //向右继续扫描  直到扫描完毕或者值不相等
            temp = mid+1;
            while(true){
                if (right>arr.length-1 || arr[temp] != arr[mid]){
                    break;
                }
                resIndexlist.add(temp);
                temp += 1;
            }
            return resIndexlist;
        }
    }


}
