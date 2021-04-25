package com.lishan.sparsearray;

/**
 * @author lishan
 * 二维数组压缩 转为稀疏数组
 * 可通过稀疏数组得到原二维数组
 */
public class SparseArray {
    public static void main(String[] args) {
        //1.创建原始二维数组
        int chessArr[][] = new int[11][11];
        chessArr[1][2] = 1;
        chessArr[2][3] = 1;
        //输出原始数组 读取数组时下标从0开始
        System.out.println("原始二维数组：");
        for (int[] row:chessArr){
            for(int data:row){
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }
        //2.普通数组转稀疏数组
        //先遍历原始数组的数字值个数 从而确定稀疏数组的行数
        int sum = 0;
        for (int[] row:chessArr){
            for(int data:row){
                if(data != 0 ){
                    sum++;
                }
            }
        }
        int[][] parseArr = new int[sum+1][3];
        parseArr[0][0] = 11;
        parseArr[0][1] = 11;
        parseArr[0][2] = sum;
        //遍历原数组将 合法数据放入稀疏数组
        int count = 0; //用于记录行数
        for (int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                if(chessArr[i][j] != 0 ){
                    count++;
                    parseArr[count][0] = i;
                    parseArr[count][1] = j;
                    parseArr[count][2] = chessArr[i][j];
                }
            }
        }
        //打印稀疏数组
        System.out.println("稀疏数组为：");
        for (int[] row:parseArr){
            for(int data:row){
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }
        //3.稀疏数组转普通数组 不赋值默认为0
        int[][] chessArr2 = new int[parseArr[0][0]][parseArr[0][1]];
        //从稀疏数组的第二行开始为普通数组赋值
        for(int i=1;i<parseArr.length;i++){
            chessArr2[parseArr[i][0]][parseArr[i][1]] = parseArr[i][2];
        }

        System.out.println("稀疏数组转普通二维数组：");
        for (int[] row:chessArr2){
            for(int data:row){
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }



    }
}
