package com.lishan.linkedlist;

public class DoubleLinkedListDemo {
    public static void main(String[] args) {
        // 测试
        System.out.println("双向链表的测试");
        // 先创建节点
        HeroNode2 hero1 = new HeroNode2(1, "宋江", "及时雨");
        HeroNode2 hero2 = new HeroNode2(2, "卢俊义", "玉麒麟");
        HeroNode2 hero3 = new HeroNode2(3, "吴用", "智多星");
        HeroNode2 hero4 = new HeroNode2(4, "林冲", "豹子头");

        HeroNode2 hero5 = new HeroNode2(4, "林冲", "豹子头");
        // 创建一个双向链表
        DoubleLinkedList doubleLinkedList = new DoubleLinkedList();
        doubleLinkedList.add(hero1);
        doubleLinkedList.add(hero2);
        doubleLinkedList.add(hero3);
        doubleLinkedList.add(hero4);

        doubleLinkedList.list();

        // 修改
        HeroNode2 newHeroNode = new HeroNode2(4, "公孙胜", "入云龙");
        doubleLinkedList.update(newHeroNode);
        System.out.println("修改后的链表情况");
        doubleLinkedList.list();

        // 删除
        doubleLinkedList.del(hero5);
        System.out.println("删除后的链表情况~~");
        doubleLinkedList.list();
    }
}

//定义链表类
class DoubleLinkedList{
    //初始化一个头结点
    HeroNode2 head = new HeroNode2(0,"","");
    public HeroNode2 getHead(){
        return head;
    }

    //添加节点
    public void add(HeroNode2 heronode2){
        HeroNode2 temp = head;
        //直接找到链表的最后 将节点加入
        while(true){
            if (temp.next == null){
                break;
            }
            temp = temp.next;
        }
        temp.next = heronode2;
        heronode2.pre = temp;
    }
    //删除节点
    public void del(HeroNode2 heronode2){
        //双向链表可以实现自删除 所以直接找到需要删除的节点
        HeroNode2 temp = head.next;
        //定义一个变量是否找到需要删除的节点
        boolean flag = false;
        if (head.next == null){
            System.out.println("节点为空，不能删除");
            return;
        }
        while(true){
            if(temp == null){
                break;
            }else if(temp.no == heronode2.no){
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag){
            temp.pre.next = temp.next;
            if (temp.next != null){
                //为了防止删除的节点 是最后一个节点，所以需要加一个判断
                temp.next.pre = temp.pre;
            }
        }else {
            System.out.println("没有找到需要删除的节点！");
        }

    }

    //修改节点
    //传入需要修改的节点
    public void update(HeroNode2 heronode2){
        HeroNode2 temp = head.next;
        //用来判断 遍历完节点后是否找到需要修改的节点
        boolean flag = false;
        while (true){
            if (head.next == null){
                System.out.println("节点为空，不能修改！");
                break;
            }else if(temp == null){
                //没有找到节点
                break;
            }else if (temp.no == heronode2.no){
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag){
            temp.name = heronode2.name;
            temp.nickname = heronode2.nickname;
        }else {
            System.out.println("没有找到需要修改的节点！");
        }
    }

    //查询链表
    public void list(){
        //同样需要辅助节点
        HeroNode2 temp = head;
        while (true){
            if(temp.next == null){
                break;
            }
            //输出节点信息
            System.out.println(temp.next);
            temp = temp.next;
        }
    }
}

// 定义节点类
class HeroNode2 {
    public int no;
    public String name;
    public String nickname;
    public HeroNode2 next; // 指向下一个节点, 默认为null
    public HeroNode2 pre; // 指向前一个节点, 默认为null
    // 构造器

    public HeroNode2(int no, String name, String nickname) {
        this.no = no;
        this.name = name;
        this.nickname = nickname;
    }

    // 为了显示方法，我们重新toString
    @Override
    public String toString() {
        return "HeroNode [no=" + no + ", name=" + name + ", nickname=" + nickname + "]";
    }

}
