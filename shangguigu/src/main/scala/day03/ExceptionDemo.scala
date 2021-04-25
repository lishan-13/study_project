package day03

object ExceptionDemo {
  def main(args: Array[String]): Unit = {
    //lazy 懒汉模式 初始化语句不会被执行，只有被调用时才会执行，只能是val
    lazy val res = sum(3,5)
    exception1()
    println("---------------")
    try{
      val th:String = exception02()
    }catch {
      case ex:Exception=>println("利用string接收了异常，并通过try catch处理")
    }



  }

  def sum(n1:Int,n2:Int): Int ={
    println("sum 函数被初始化了")
    n1+n2
  }

  //try catch 来处理异常
  def exception1() ={
    try{
      val res = 10/0
    }catch {
        //scala中只有一个catch
        //通过模式匹配case 来捕获不同的异常
        case ex:ArithmeticException=>println("捕获了除零异常")
        case ex:Exception=>println("捕获了异常")
    }finally{
      println("最终要执行的代码")
    }
    println("处理了异常继续执行")
  }

  //throw 抛出异常也是有类型的 nothing,所以任何类型都可以接受并处理异常
  def exception02(): Nothing ={
    throw new ArithmeticException("这是抛出的异常")
  }

}
