package day03


object work {
  def main(args: Array[String]): Unit = {
    //3
    for(i <- 0 to 10 reverse){ //反转 大到小
      println(i)
    }
    println("---------")
    //4
    def countdown(n:Int){
      for(i <- 0 to n reverse){
        println(i)
      }
    }
    countdown(5)
    println("---------")

    def countdown2(n:Int): Unit ={
      (0 to n).reverse.foreach(println) //foreach 里面的参数为函数，相当于将集合中的每个元素都传递给参数里的函数处理

    }
    countdown2(5)
    println("---------")
    //5
    var sum:Long = 1L
    for(i <- "Hello"){
//      val num:Int = i
//      sum *= num
      sum *= i.toLong
    }
    println(sum)
    println("---------")
    6
    var sum2:Long = 1L
    "Hello".foreach(sum2 *= _.toLong)
    println(sum2)
    println("---------")
    def digui(s:String): Long ={
      if(s.length ==1) s.charAt(0).toLong
        //take(1) 取出第一个字符 drop(1)删除第一个字符返回后面的所有字符
      else s.take(1).charAt(0).toLong * digui(s.drop(1))
    }
    println(digui("Hello"))
    println("---------")

    //9 x^n
    def digui2(x:Int,n:Int): Double ={
      if(n ==0 ) 1
      else{
        if(n > 0){
          x * digui2(x,n-1)
        }else{
          //-n 就为正数  等于就还是调用递归函数进行一个转换后的幂运算 最后分之一就可以了
          1/digui2(x,-n)

        }
      }
    }

    println(digui2(2,-2))
  }
}
