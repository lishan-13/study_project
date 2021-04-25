package com.lishan.linkedlist;

/**
 * 单项环形链表
 */
public class Josepfu {
    public static void main(String[] args) {
        // 测试一把看看构建环形链表，和遍历是否ok
        CircleSingleLinkedList circleSingleLinkedList = new CircleSingleLinkedList();
        circleSingleLinkedList.addBoy(5);// 加入5个小孩节点
        circleSingleLinkedList.showBoy();
        circleSingleLinkedList.countBoy(1,2,5);
    }
}

// 创建一个环形的单向链表
class CircleSingleLinkedList {
    // 创建一个first节点,当前没有编号  相当于头结点
    private Boy first = null;
    // 添加小孩节点，构建成一个环形的链表
    public void addBoy(int nums) {
        if(nums < 1){
            System.out.println("num的值不正确");
            return;
        }
        Boy curBoy = null; //辅助指针用于创建环形链表
        for(int i=1;i<=nums;i++){
            //创建节点
            Boy boy = new Boy(i);
            //如果是第一个节点 需要单独处理
            if(i==1){
                first = boy;
                first.setNext(first); //构成环
                curBoy = first;
            }else{
                curBoy.setNext(boy);
                boy.setNext(first);
                curBoy = boy;
            }
        }
    }

    //遍历当前环形链表
    public void showBoy(){
        if (first == null){
            System.out.println("当前链表为空无法遍历！");
            return;
        }
        //同样需要一个指针来帮助遍历
        Boy curBoy = first;

        while(true){
            System.out.println("小孩的编号为："+curBoy.getNo());
            if (curBoy.getNext() == first){
                break;
            }
            curBoy = curBoy.getNext();
        }
    }

    //约瑟夫问题具体实现
    // 根据用户的输入，计算出小孩出圈的顺序
    /**
     *
     * @param startNo
     *            表示从第几个小孩开始数数
     * @param countNum
     *            表示数几下
     * @param nums
     *            表示最初有多少小孩在圈中
     */
    public void countBoy(int startNo, int countNum, int nums) {
        //首先进行数据校验
        if(first ==null || startNo < 1 || startNo>nums){
            return;
        }
        // 创建要给辅助指针,帮助完成小孩出圈(要出圈节点的前一个节点
        Boy helper = first;
        while(true){
            if (helper.getNext() == first){
                break;
            }
            helper = helper.getNext(); //定位到first节点的后一个节点
        }

        //定位到开始数的小孩 只需要移动k-1次
        for(int i=0;i<startNo-1;i++){
            first = first.getNext();
            helper = helper.getNext();
        }
        //开始数 只需要移动countNum - 1次
        while (true){
            if (helper == first){
                //说明循环链表中只剩一个节点了
                break;
            }
            for(int i=0;i<countNum-1;i++){
                //现在first节点就是要出圈的节点
                first = first.getNext();
                helper = helper.getNext();
            }
            System.out.println("正在出圈的是"+first.getNo());
            //正真的出圈操作
            first = first.getNext();
            helper.setNext(first);
        }
        System.out.println("最后一个出圈的是"+first.getNo());
    }
}

// 创建一个Boy类，表示一个节点
class Boy {
    private int no;// 编号
    private Boy next; // 指向下一个节点,默认null

    public Boy(int no) {
        this.no = no;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Boy getNext() {
        return next;
    }

    public void setNext(Boy next) {
        this.next = next;
    }

}