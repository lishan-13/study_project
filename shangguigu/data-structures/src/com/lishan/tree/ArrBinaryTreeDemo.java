package com.lishan.tree;

/**
 * 顺序存储二叉树  就是将二叉树的节点按照顺序存入到数组中，
 * 再按照一定规格对数组完成类似二叉树遍历的操作
 */
public class ArrBinaryTreeDemo {
    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
        ArrBinaryTree arrBinaryTree = new ArrBinaryTree(arr);
        arrBinaryTree.preOrder(); // 1,2,4,5,3,6,7
    }
}

//编写一个ArrayBinaryTree, 实现顺序存储二叉树遍历

class ArrBinaryTree {
    private int[] arr;
    //接收一个数组
    public ArrBinaryTree(int[] arr){
        this.arr = arr;
    }
    //重载遍历方法 调用下面的方法
    public void preOrder(){
        this.preOrder(0);
    }

    public void preOrder(int index){
        //如果数组为空，或者 arr.length = 0
        if(arr == null || arr.length == 0) {
            System.out.println("数组为空，不能按照二叉树的前序遍历");
        }
        //输出当前元素
        System.out.println(arr[index]);
        //向左递归遍历
        if((index*2+1) < arr.length){
            preOrder(index*2+1);
        }
        //向右递归遍历
        if((index*2+2) < arr.length){
            preOrder(index*2+2);
        }
    }

}