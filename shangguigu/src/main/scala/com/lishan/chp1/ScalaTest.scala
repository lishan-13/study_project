package com.lishan.chp1

import scala.io.StdIn
import util.control.Breaks._

object ScalaTest {
  /**
   * @deprecated 测试
   * @param args
   */
  def main(args: Array[String]): Unit = {

    println("hello scala")
    //字符串输出方式的测试 3种
    var str1:String = "hello"
    var str2:String = " world"
    println(str1+str2)
    //格式化输出
    var name:String = "tom"
    var age:Int = 10
    var height:Double = 180.3
    printf("姓名是：%s 年龄是:%d 身高是:%.2f",name,age,height)
    //变量输出
    var name1:String = "jarry"
    var age1:Int = 12
    println(s"\n姓名：$name1 年龄:${age1+3}")

    //键盘输入测试
    println("请输入：")
    //var input = StdIn.readLine()

    //for循环测试
    var start = 1
    var end = 3
//    for(i <- start to end){ //前闭后闭
    for(i <- start until  end){ //前闭后开
      println(s"$i hello word!")
    }

    //循环守卫测试
    for(i <- start to end if i != 2){ //true 执行 false 跳过
      println(s"$i hello word!")
    }

    //引入变量测试
    for(i <- start to end;j = 4-i){ //true 执行 false 跳过
      println(s"$j")
    }

    //嵌套循环测试
    for(i <- start to end;j <- start to end){ //输出9次
      println(s"$i $j")
    }

    //循环返回值测试
    var res = for(i <- 1 to  3) yield i *2 //将结果返回到一个vector集合 yield 后面也可以返回逻辑表达式
    println(res)


    //for循环的步长控制
    for(i <-Range(1,5,2)) println(i) //步长为2 也可通过循环守卫控制步长


    //while 循环 不同与if语句  没有返回值Unit
    var x =0
    while(x < 10){
      println(x)
      x+=1
    }


    //九九乘法表
    for(i <- 1 to 9){
      for(j <- 1 to i){
        printf("%dx%d=%d\t",j,i,i * j)
      }
      println()
    }



    //break 和 continue的实现
    var y = 0
    breakable { //高阶函数：以函数为参数的函数 该函数会解决break()抛出的异常
      while (y < 20) {
        println("y = " + y)
        y += 1
        if (y == 18) {
          break() //这里的break为一个函数 会抛出异常结束代码块,如果不处理 后面的代码不会执行
        }
      }
    }
    println("ok~~")
    //continue 可以通过循环守卫或者if else 实现
  }

}
