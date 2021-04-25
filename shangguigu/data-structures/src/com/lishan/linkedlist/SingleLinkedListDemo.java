package com.lishan.linkedlist;

import java.util.Stack;

public class SingleLinkedListDemo {
    public static void main(String[] args) {
        //进行测试
        //先创建节点
        HeroNode hero1 = new HeroNode(1, "宋江", "及时雨");
        HeroNode hero2 = new HeroNode(2, "卢俊义", "玉麒麟");
        HeroNode hero3 = new HeroNode(3, "吴用", "智多星");
        HeroNode hero4 = new HeroNode(4, "林冲", "豹子头");

        HeroNode hero5 = new HeroNode(4, "小林", "豹子头");

        SingleLinkedList singlelinkedlist = new SingleLinkedList();

//        singlelinkedlist.add(hero1);
//        singlelinkedlist.add(hero2);
//        singlelinkedlist.add(hero3);
//        singlelinkedlist.add(hero4);
        singlelinkedlist.addByOrder(hero1);
        singlelinkedlist.addByOrder(hero2);
        singlelinkedlist.addByOrder(hero3);
        singlelinkedlist.addByOrder(hero4);


        //修改节点测试
        singlelinkedlist.update(hero5);
        //删除节点测试
        singlelinkedlist.delete(hero5);
        //有效节点方法测试
        int num = getLength(singlelinkedlist.getHead());
        System.out.println("有效节点个数："+num);
        //测试倒数第k个节点
        System.out.println("这是倒数第1个节点："+findLastIndexNode(singlelinkedlist.getHead(),1));

        //单链表反转测试
        reverseList(singlelinkedlist.getHead());

        singlelinkedlist.list();

        //逆序打印测试
        System.out.println("下面为逆序打印结果：");
        reversePrint(singlelinkedlist.getHead());
    }
    //查找倒数第k个节点  新浪面试题
    public static HeroNode findLastIndexNode(HeroNode head,int index){
        if (head.next == null){
            System.out.println("链表为空，不能查找倒数第k个节点");
            return null;
        }
        int length = getLength(head);
        //先做一个index的校验
        if(index <=0 || index > length) {
            return null;
        }
        HeroNode temp = head.next;
        //球倒数第k个  只需要遍历到 长度减去倒数个数index次，就可以找到需要的节点
        for (int i=0;i<length-index;i++){
            temp = temp.next;
        }
        return temp;

    }

    //单链表反转 腾讯面试题

    /**
     * 1.创建一个新的节点reverseHead
     * 2.遍历原链表 将新遍历的值放在新节点的前面
     * 3.将原链表的head.next指向 新节点的next
     */
    public static void reverseList(HeroNode head){
        HeroNode reverseHead = new HeroNode(0,"","");

        HeroNode temp = head.next;
        HeroNode next = null;
        //如果当前链表为空，或者只有一个节点，无需反转，直接返回
        if(head.next == null || head.next.next == null) {
            return ;
        }
        while (temp != null){
            //先将temp的下一节点保存起来 因为后面会将temp与其下一节点分割开，链表就断了
            next = temp.next;
            //将temp的下一节点指向 新建节点的下一节点，为一个空对象
            temp.next = reverseHead.next;
            //头结点值想temp节点这样就将 temp节点与原节点断开了
            reverseHead.next = temp;
            //将temp 转换为有继承关系的先前保存的下一节点
            temp = next;
        }
        //改变原链表
        head.next = reverseHead.next;
    }

    //反转打印链表 不改变原有结构 百度面试题
    public static void reversePrint(HeroNode head){
        if (head.next == null){
            System.out.println("链表为空 不能打印");
            return;
        }
        //创建栈 将节点一次压如栈中，利用栈先进后出的特点 实现逆序打印链表
        Stack<HeroNode> stack = new Stack();
        HeroNode temp = head.next;
        while (temp != null){
            stack.push(temp);
            temp = temp.next;
        }
        while (stack.size() > 0){
            System.out.println(stack.pop());
        }
    }

    //查询有效节点个数
    public static int getLength(HeroNode heronode){
        if (heronode.next == null){
            return 0;
        }
        HeroNode temp = heronode.next;
        int sum = 0;
        while(temp != null){
            sum++;
            temp = temp.next;
        }
        return sum;
    }
}
class SingleLinkedList{
    //初始化一个头结点 不能变
    private HeroNode head = new HeroNode(0,"","");

    public HeroNode getHead(){
        return head;
    }

    //添加节点
    public void add(HeroNode hearnode){
        //需要找到最后一个节点 由于head不变，所以给一个中间变量
        HeroNode temp = head;
        while (true){
            if (temp.next == null){
                break;
            }
            temp = temp.next;
        }
        temp.next = hearnode;
    }

    //按顺序添加节点
    public void addByOrder(HeroNode heronode){
        //需要找到最后一个节点 由于head不变，所以给一个中间变量
        HeroNode temp = head;
        boolean flag = false;
        //遍历找到需要插入的节点
        while (true){
            //已经是最后一个节点 直接插入
            if (temp.next == null){
                break;
            }else if(temp.next.no == heronode.no){
                //节点已经存在
                flag = true;
                break;
            }else if(temp.next.no > heronode.no){
                //找到需要插入的节点
                break;
            }
            temp = temp.next;
        }
        //插入
        if (flag){
            System.out.println("插入的编号"+heronode.no+"已经存在！");
        }else {
            heronode.next = temp.next;
            temp.next = heronode;
        }

    }

    //修改节点
    public void update(HeroNode heronode){
        HeroNode temp = head.next;
        //用来判断 遍历完节点后是否找到需要修改的节点
        boolean flag = false;
        while (true){
            if (head.next == null){
                System.out.println("节点为空，不能修改！");
                break;
            }else if(temp == null){
                //没有找到节点
                break;
            }else if (temp.no == heronode.no){
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag){
            temp.name = heronode.name;
            temp.nickname = heronode.nickname;
        }else {
            System.out.println("没有找到需要修改的节点！");
        }
    }

    //删除节点  需要找到待删除节点的前一个节点
    public void delete(HeroNode heronode){
        HeroNode temp = head;
        boolean flag = false;
        while(true){
            if (head.next == null){
                System.out.println("节点为空，不能删除！");
                break;
            }else if (temp.next == null){
                //遍历到最后都没有找到 需要删除的节点
                break;
            }else if (temp.next.no == heronode.no){
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag){
            temp.next = temp.next.next;
        }else {
            System.out.println("没有找到需要删除的节点！");
        }
    }

    //查询链表
    public void list(){
        //同样需要辅助节点
        HeroNode temp = head;
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
//定义HeroNode ， 每个HeroNode 对象就是一个节点
class HeroNode {
    public int no;
    public String name;
    public String nickname;
    public HeroNode next; //指向下一个节点 默认值为null
    //构造器
    public HeroNode(int no, String name, String nickname) {
        this.no = no;
        this.name = name;
        this.nickname = nickname;
    }
    //为了显示方法，我们重新toString
    @Override
    public String toString() {
        return "HeroNode [no=" + no + ", name=" + name + ", nickname=" + nickname + "]";
    }

}
