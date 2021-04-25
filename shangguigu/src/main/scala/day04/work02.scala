package day04

object work02 {
  def main(args: Array[String]): Unit = {
    val point = Point(3,4)
    point.printpoint()
  }
}
class Point(len:Int,weight:Int){
  var x = len
  var y = weight
  def printpoint(): Unit ={
    println("长："+x+" 宽："+y)
  }

}
object Point{
  //apply 方法的参数 要传给new的point伴生类，所以伴生类要有对应的构造器接收参数
  def apply(len:Int,weight:Int): Point = new Point(len,weight)
}

