package com.lishan.hashtab;

import java.util.Scanner;

public class HashTabDemo {
    public static void main(String[] args) {
        //创建哈希表
        HashTab hashTab = new HashTab(7);

        //写一个简单的菜单
        String key = "";
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("add:  添加雇员");
            System.out.println("list: 显示雇员");
            System.out.println("find: 查找雇员");
            System.out.println("exit: 退出系统");

            key = scanner.next();
            switch (key) {
                case "add":
                    System.out.println("输入id");
                    int id = scanner.nextInt();
                    System.out.println("输入名字");
                    String name = scanner.next();
                    //创建 雇员
                    Emp emp = new Emp(id, name);
                    hashTab.add(emp);
                    break;
                case "list":
                    hashTab.list();
                    break;
                case "find":
                    System.out.println("请输入要查找的id");
                    id = scanner.nextInt();
                    hashTab.findEmpById(id);
                    break;
                case "exit":
                    scanner.close();
                    System.exit(0);
                default:
                    break;
            }
        }
    }
}

//创建HashTab 管理多条链表
class HashTab {
    private EmpLinkedList[] empLinkedListArray;
    private int size; //表示有多少条链表
    public HashTab(int size){
        this.size = size;
        empLinkedListArray = new EmpLinkedList[size];
        for(int i=0;i<size;i++){ //创建数组
            empLinkedListArray[i] = new EmpLinkedList();
        }
    }
    //1.添加
    public void add(Emp emp){
        int id = hashFun(emp.id);
        empLinkedListArray[id].add(emp);

    }
    //编写散列函数, 使用一个简单取模法  确定是那一条链表
    public int hashFun(int id) {
        return id % size;
    }
    //2.遍历哈希表
    public void list(){
        for (int i=0;i<size;i++){
            //遍历每一条链表
            empLinkedListArray[i].list(i);
        }
    }
    //3.查找雇员
    public void findEmpById(int id) {
        //先找到在哪一条链表
        int empLinkedListNO = hashFun(id);
        //开始查找
        Emp emp = empLinkedListArray[empLinkedListNO].findEmpById(id);
        if(emp != null){  //找到
            System.out.printf("在第%d条链表中找到 雇员 id = %d\n", (empLinkedListNO + 1), id);
        }else {
            System.out.println("在哈希表中，没有找到该雇员~");
        }

    }

}

//创建一个雇员类 表示一个雇员
class Emp {
    public int id;
    public String name;
    public Emp next; //next 默认为 null
    public Emp(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
}

//创建EmpLinkedList ,表示链表
class EmpLinkedList {
    //头指针，执行第一个Emp,因此我们这个链表的head 是直接指向第一个Emp
    private Emp head; //默认null

    //1.添加方法
    public void add(Emp emp){
        if(head == null){ //没有数据 直接加入
            head = emp;
            return;
        }
        Emp curEmp = head; //辅助指针用于找到最后一个元素
        while (true){ //直到找到最后一个元素
            if(curEmp.next == null){
                break;
            }
            curEmp = curEmp.next;
        }
        //退出时说明找到了添加节点
        curEmp.next = emp;
    }
    //2.遍历方法  这里遍历的是每一条链表
    public void list(int no){
        //每条链表都有一个head
        if(head == null){
            System.out.println("第 "+(no+1)+" 链表为空");
            return;
        }
        System.out.print("第 "+(no+1)+" 链表的信息为");
        Emp curEmp = head; //辅助指针
        //循环打印每个节点的信息
        while(true){
            System.out.printf(" => id=%d name=%s\t", curEmp.id, curEmp.name);
            if (curEmp.next == null){
                break;
            }
            curEmp = curEmp.next;
        }
        System.out.println();
    }
    //3.查找方法 如果查找到，就返回Emp, 如果没有找到，就返回null
    public Emp findEmpById(int id) {
        //只需要写关于链表的方法 具体是那一条链表 由数组哈希表类决定
        if (head == null){
            System.out.println("链表为空");
            return null;
        }
        Emp curEmp = head;
        while(true) {  //找到查找的对象
            if (curEmp.id == id) {
                break; //不需要再这里返回对象  只需要找到即可
            }
            if (curEmp.next == null){
                curEmp = null;
                break;
            }
            curEmp = curEmp.next;
        }
        return curEmp;
    }
}