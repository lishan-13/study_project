package com.lishan.queue;

import java.util.Scanner;

public class ArrayQueueDemo {
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue(5);
        char key = ' '; //接收用户输入
        Scanner sc = new Scanner(System.in);
        Boolean loop = true;
        while(loop){
            System.out.println("s(show): 显示队列");
            System.out.println("e(exit): 退出程序");
            System.out.println("a(add): 添加数据到队列");
            System.out.println("g(get): 从队列取出数据");
            System.out.println("h(head): 查看队列头的数据");
            key = sc.next().charAt(0);//接收一个字符
            switch (key){
                case 's':
                    queue.showQueue();
                    break;
                case 'a':
                    System.out.println("输出一个数");
                    int value = sc.nextInt();
                    queue.addQueue(value);
                    break;
                case 'g': //取出数据
                    try {
                        int res = queue.getQueue();
                        System.out.printf("取出的数据是%d\n", res);
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'h': //查看队列头的数据
                    try {
                        int res = queue.headQueue();
                        System.out.printf("队列头的数据是%d\n", res);
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'e': //退出
                    sc.close();
                    loop = false;
                    break;
                default:
                    break;
            }
        }
        System.out.println("程序退出~~");
    }
}
class ArrayQueue{
    private int maxSize;//最大容量
    private int front;//指向对头的前一个位置
    private int rear; //指向对尾数据即是最后一个数据
    private int[] arr;//创建数组模拟队列

    //构造方法
    public ArrayQueue(int maxsize){
        maxSize = maxsize;
        front = -1;
        front = -1;
        arr = new int[maxsize];
    }
    //判断对满
    public Boolean isFull(){
        return front == maxSize-1;
    }
    //判断对空
    public Boolean isEmpty(){
        return front == rear;
    }

    //入队
    public void addQueue(int n){
        if(isFull()){
            System.out.println("对满，不能入队！");
            return;
        }
        else{
            rear++;
            arr[rear] = n;
        }
    }

    //出队，获取数据不是取出数据
    public int getQueue(){
        if (isEmpty()){
            throw new RuntimeException("队列空，不能出队");
        }else{
            front++;
            return arr[front];
        }
    }

    //查询队列所有数据
    public void showQueue(){
        if (isEmpty()){
            throw new RuntimeException("对空，不能查询！");
        }else{
            for(int data:arr){
                System.out.println(data);
            }
        }
    }

    //获取队头数据
    public int headQueue(){
        if (isEmpty()){
            throw new RuntimeException("对空，获取队头！");
        }else{
            return arr[front+1];
        }
    }

}
