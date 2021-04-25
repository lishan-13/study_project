package day04
/*
　我们可以在单例对象后边加上 extends App获取App特性。

　　使用App特性，我们可以不用编写main方法，把打算写在main方法中的代码直接写在单例对象的花括号中。

　　可以通过名为args的字符串数组来访问命令行参数。

　　然后我们就可以像其他应用程序一样来编译和运行它。
 */
object work03 extends App {
  val args2 =  args.reverse.mkString(" ")
  println(args2)
}
