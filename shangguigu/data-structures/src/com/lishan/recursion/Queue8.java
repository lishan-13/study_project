package com.lishan.recursion;

public class Queue8 {
    static int max = 8;
    //定义一个一维数组 用于保存位置结果
    //索引用来表示行数 值用来保存每一行列的位置
    static int[] array = new int[max];

    static int count = 0;
    static int judgeCount = 0;
    public static void main(String[] args) {
        Queue8 queue8 = new Queue8();
        queue8.check(0);
        System.out.printf("一共有%d解法", count);
        System.out.printf("一共判断冲突的次数%d次", judgeCount); // 1.5w
    }

    private void check(int n) {
        if(n == 8){ //就是说已经开始摆第九列了，所以结束
            print();
            return;
        }
        for (int i=0;i<max;i++){ //就是说每一行的每一列都要测试 i表示列数 //TODO:回溯
            //这里的回溯是从最后一行开始往回回溯，就是将n=7的所有列试一遍符合的输出，将所有n=6时的所有合法列试一遍。。。
            array[n] = i;
            judgeCount++;
            if (judge(n)){ //如果这个列不冲突就直接测试下一行 //TODO:开始递归
                check(n+1);
            }
        }
    }

    //查看当我们放置第n个皇后, 就去检测该皇后是否和前面已经摆放的皇后冲突
    /**
     *
     * @param n 表示第n个皇后
     * @return
     */
    private boolean judge(int n) {
        //judgeCount++;
        //用来判断当前行与前面所有行是否冲突
        for(int i=0;i<n;i++){
            //array[i] == array[n]  用来判断两行是否处于同一列
            // Math.abs(n-i) == Math.abs(array[n] -array[i]) 用来判断两行是否处于同一斜线，对角线为对称关系
            //所以行数相减=列数相减
            if (array[i] == array[n] || Math.abs(n-i) == Math.abs(array[n] -array[i])){
                return false;
            }
        }
        return true;
    }
    //写一个方法，可以将皇后摆放的位置输出
    private void print() {
        count++;
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
}
