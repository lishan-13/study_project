package com.lishan.recursion;

/***
 * 迷宫回溯问题
 */
public class MiGong {
    public static void main(String[] args) {
        int[][] map = new int[8][7];
        //使用1表示墙
        //将上下都设置为1
        for(int i=0;i<7;i++){
            map[0][i] = 1;
            map[7][i] = 1;
        }
        //将左右设置为1
        for(int i=0;i<8;i++){
            map[i][0] = 1;
            map[i][6] = 1;
        }
        //再设置两个挡板
        map[3][1] = 1;
        map[3][2] = 1;
        //遍历原地图
        for (int i=0;i<8;i++){
            for (int j=0;j<7;j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        setWay(map, 1, 1);

        //输出新的地图, 小球走过，并标识过的递归
        System.out.println("小球走过，并标识过的 地图的情况");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    //使用递归回溯来给小球找路
    //说明
    //1. map 表示地图
    //2. i,j 表示从地图的哪个位置开始出发 (1,1)
    //3. 如果小球能到 map[6][5] 位置，则说明通路找到.
    //4. 约定： 当map[i][j] 为 0 表示该点没有走过 当为 1 表示墙  ； 2 表示通路可以走 ； 3 表示该点已经走过，但是走不通
    //5. 在走迷宫时，需要确定一个策略(方法) 下->右->上->左 , 如果该点走不通，再回溯
    /**
     *
     * @param map 表示地图
     * @param i 从哪个位置开始找
     * @param j
     * @return 如果找到通路，就返回true, 否则返回false
     */
    public static boolean setWay(int[][] map, int i, int j) {
        if(map[6][5] == 2){
            //就是找到了终点  直接返回
            return true;
        }else{
            if(map[i][j] == 0){//说明还没有走过  只要按照规则找到的点为零就一直往下找
                //先假设该点为通路  开始验证

                map[i][j] = 2;
                //判断下面的点是否为通路
                //说明：只有值为0的点才可能被设置为通路
                //一直调用 直到有返回值（false）为止
                if(setWay(map,i+1,j)){
                    return true;
                }else if (setWay(map,i,j+1)){
                    return true;
                }else if (setWay(map,i-1,j)){
                    return true;
                }else if (setWay(map,i,j-1)){
                    return true;
                }else {
                    //上下左右都走不通 就设置为3
                    //开始回溯上一个点的其他情况
                    map[i][j] = 3;
                    return false;
                }
            }else {
                //不为零 直接就不用走了，开始测试下一个点
                return false;
            }
        }
    }
}
