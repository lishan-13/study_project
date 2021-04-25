package com.lishan.tree;

public class BinaryTreeDemo {
    public static void main(String[] args) {
        //先需要创建一颗二叉树
        BinaryTree binaryTree = new BinaryTree();
        //创建需要的结点
        HeroNode root = new HeroNode(1, "宋江");
        HeroNode node2 = new HeroNode(2, "吴用");
        HeroNode node3 = new HeroNode(3, "卢俊义");
        HeroNode node4 = new HeroNode(4, "林冲");
        HeroNode node5 = new HeroNode(5, "关胜");
        //说明，我们先手动创建该二叉树，后面我们学习递归的方式创建二叉树
        root.setLeft(node2);
        root.setRight(node3);
        node3.setRight(node4);
        node3.setLeft(node5);
        //设置根节点
        binaryTree.setRoot(root);
        //测试
		System.out.println("前序遍历"); // 1,2,3,5,4
		binaryTree.preOrder();

        //前序查找的次数 ：4
		System.out.println("前序遍历方式~~~");
		HeroNode resNode = binaryTree.preOrderSearch(5);
		if (resNode != null) {
			System.out.printf("找到了，信息为 no=%d name=%s", resNode.getNo(), resNode.getName());
		} else {
			System.out.printf("没有找到 no = %d 的英雄", 5);
		}

        binaryTree.delNode(5);
        //binaryTree.delNode(3);
        System.out.println("删除后，前序遍历");
        binaryTree.preOrder(); // 1,2,3,4
    }
}

//定义BinaryTree 二叉树
class BinaryTree {
    //创建根节点
    private HeroNode root;

    public void setRoot(HeroNode root) {
        this.root = root;
    }
    //前序遍历
    public void preOrder() {
        if(this.root != null) {
            this.root.preOrder();
        }else {
            System.out.println("二叉树为空，无法遍历");
        }
    }
    //中序遍历
    public void infixOrder() {
        if(this.root != null) {
            this.root.infixOrder();
        }else {
            System.out.println("二叉树为空，无法遍历");
        }
    }
    //后序遍历
    public void postOrder() {
        if(this.root != null) {
            this.root.postOrder();
        }else {
            System.out.println("二叉树为空，无法遍历");
        }
    }

    //前序查找
    public HeroNode preOrderSearch(int no) {
        if(root != null) {
            return root.preOrderSearch(no);
        } else {
            return null;
        }
    }
    //中序遍历
    public HeroNode infixOrderSearch(int no) {
        if(root != null) {
            return root.infixOrderSearch(no);
        }else {
            return null;
        }
    }
    //后序遍历
    public HeroNode postOrderSearch(int no) {
        if (root != null) {
            return this.root.postOrderSearch(no);
        } else {
            return null;
        }
    }

    //删除节点
    public void delNode(int no){
        //先判断根节点是否为空
        if(root != null){
            if(root.getNo() == no){
                root =null;
            }else{
                root.delNode(no);
            }
        }else{
            System.out.println("空数，不能删除");
        }
    }

}
//先创建HeroNode 结点
class HeroNode {
    private int no;
    private String name;
    private HeroNode left; //默认null
    private HeroNode right; //默认null

    public HeroNode(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeroNode getLeft() {
        return left;
    }

    public void setLeft(HeroNode left) {
        this.left = left;
    }

    public HeroNode getRight() {
        return right;
    }

    public void setRight(HeroNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "HeroNode [no=" + no + ", name=" + name + "]";
    }
    //前序遍历
    public void preOrder(){
        //每一个节点都可以 看成一个根节点
        //递归是栈形式  TODO:从后往前回溯
        System.out.println(this);
        if(this.left != null){
            this.left.preOrder();
        }
        if(this.right != null){
            this.right.preOrder();
        }
    }
    //中序遍历
    public void infixOrder() {

        //递归向左子树中序遍历
        if(this.left != null) {
            this.left.infixOrder();
        }
        //输出父结点
        System.out.println(this);
        //递归向右子树中序遍历
        if(this.right != null) {
            this.right.infixOrder();
        }
    }
    //后序遍历
    public void postOrder() {
        if(this.left != null) {
            this.left.postOrder();
        }
        if(this.right != null) {
            this.right.postOrder();
        }
        System.out.println(this);
    }


    //前序遍历查找
    /**
     *
     * @param no 查找no
     * @return 如果找到就返回该Node ,如果没有找到返回 null
     */
    public HeroNode preOrderSearch(int no) {
        //比较当前结点是不是
        if (this.no == no) {
            //TODO:这里返回的是给本次方法的  本次方法右将结果返回给下面的栈  最终得到的resNode就是结果

            return this;
        }
        //往左边递归
        //TODO：定义一个变量接收上面栈 回溯的结果
        HeroNode resNode = null;
        if (this.left != null) {
            resNode = this.left.preOrderSearch(no);
        }
        //
        if (resNode != null) {//说明我们左子树找到
            return resNode;
        }
        if (this.right != null) {
            resNode = this.right.preOrderSearch(no);
        }
        return resNode;
    }
        //中序遍历查找
        public HeroNode infixOrderSearch(int no) {
            //判断当前结点的左子节点是否为空，如果不为空，则递归中序查找
            HeroNode resNode = null;
            if(this.left != null) {
                resNode = this.left.infixOrderSearch(no);
            }
            if(resNode != null) {
                return resNode;
            }
            System.out.println("进入中序查找");
            //如果找到，则返回，如果没有找到，就和当前结点比较，如果是则返回当前结点
            if(this.no == no) {
                return this;
            }
            //否则继续进行右递归的中序查找
            if(this.right != null) {
                resNode = this.right.infixOrderSearch(no);
            }
            return resNode;

        }

        //后序遍历查找
        public HeroNode postOrderSearch(int no) {

            //判断当前结点的左子节点是否为空，如果不为空，则递归后序查找
            HeroNode resNode = null;
            if(this.left != null) {
                resNode = this.left.postOrderSearch(no);
            }
            if(resNode != null) {//说明在左子树找到
                return resNode;
            }

            //如果左子树没有找到，则向右子树递归进行后序遍历查找
            if(this.right != null) {
                resNode = this.right.postOrderSearch(no);
            }
            if(resNode != null) {
                return resNode;
            }
            System.out.println("进入后序查找");
            //如果左右子树都没有找到，就比较当前结点是不是
            if(this.no == no) {
                return this;
            }
            return resNode;
        }
        //删除节点
        public void delNode(int no) {

        //思路
		/*
		 * 	1. 因为我们的二叉树是单向的，所以我们是判断当前结点的子结点是否需要删除结点，而不能去判断当前这个结点是不是需要删除结点.
			2. 如果当前结点的左子结点不为空，并且左子结点 就是要删除结点，就将this.left = null; 并且就返回(结束递归删除)
			3. 如果当前结点的右子结点不为空，并且右子结点 就是要删除结点，就将this.right= null ;并且就返回(结束递归删除)
			4. 如果第2和第3步没有删除结点，那么我们就需要向左子树进行递归删除
			5.  如果第4步也没有删除结点，则应当向右子树进行递归删除.

		 */
		    //判断左子节点
            if(this.left !=null && this.left.no == no){
                this.left = null;
                return;
            }
            //再判断当前节点的有子节点
            if(this.right != null && this.right.no == no) {
                this.right = null;
                return;
            }
            //开始左递归
            if(this.left != null){
                this.left.delNode(no);
            }
            //开始右递归
            if(this.right != null) {
                this.right.delNode(no);
            }
        }

    }



