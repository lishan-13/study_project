package day04
object work01 {
  def main(args: Array[String]): Unit = {
    val mna = new manager
    mna.bonus = 10
    mna.name = "张三"
    mna.gz = 100

    val test = new Test
    println(test.showA(mna))
    println(test.testWork(mna))
  }
}

abstract class Employee{
  var name:String = _
  var gz:Int = _
  def getAnnual():Int
}

class empl extends Employee {
  def work() = {
    "搬砖"
  }

  override def getAnnual(): Int = {
    gz * 12
  }
}

  class manager extends Employee {
  var bonus:Int = _
  def manage() ={
    "管理"
  }
    override def getAnnual(): Int = {
      gz * 12 + bonus
    }
  }
class Test{
  def showA(employee: Employee): Int ={
    employee.getAnnual()
  }
  def testWork(employee: Employee): String ={
    if(employee.isInstanceOf[empl]){
      employee.asInstanceOf[empl].work()
    }else{
      employee.asInstanceOf[manager].manage()
    }
  }
}

