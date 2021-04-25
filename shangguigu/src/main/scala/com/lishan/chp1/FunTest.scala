package com.lishan.chp1

object FunTest {
  def main(args: Array[String]): Unit = {
    println(fbn(7))
    println(monkey(10))
  }
  //斐波拉契数列  1 1 2 3 5 8 13
  def fbn(n:Int): Int ={
    if(n == 1 || n == 2){
      1
    }else{
      fbn(n-1) +fbn(n-2)
    }
  }
  //猴子吃桃 1 4 10
  def monkey(n:Int): Int ={
    if(n == 1){
      1
    }else{
      2 * (monkey(n-1) + 1)
    }
  }
}
