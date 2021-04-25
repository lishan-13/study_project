package day15

/**
 * 商品打折案例：
 * 1.商品捆绑可以是单个商品 也可以是多个商品
 * 2.打折时按照x原进行设计
 * 3.能够统计出所有商品打折后的价格
 */
object Salesdemo {
  def main(args: Array[String]): Unit = {
    //三个参数获取的测试
    //1.先定一个嵌套对象
    val sale = Bundle("书籍",10,Book("漫画",40),Bundle("文学作品",20,Book("西游记",80),Book("水浒传",30)))
    val res = sale match{
      //得到 漫画 这个description，绑定的是单个属性
      case Bundle(_,_,Book(desc,_),_*) => desc
      case _ => println("没有匹配到")
    }
    val res2 = sale match{
      // 通过@ 表示法将嵌套的值绑定到变量 绑定的是一个变量，这个变量返回的是一个集合
      case Bundle(_,_,manhua @ Book(_,_),rest @ _*) =>(manhua,rest)
    }
    //println(res2)

    def price(item: Item):Double ={
      item match {
        case Book(_,price) => price
          //相当于是一个递归 反复调用price 最终通过book得到价格 再减去当前对象的折扣
          //由于its是一个集合所以可以map操作和sum操作
        case Bundle(_,dis,its @ _*) => its.map(price).sum-dis

      }
    }
    println("price = "+price(sale))
  }
}
//1.样例类的设计

//用来传入多个对象 可以是book 也可以是bundle
abstract class Item
//商品类 price价格
case class Book(descriptinon:String,price:Double) extends Item
//捆绑类 discount折扣 item 捆绑对象 一个bundle对象对应一个折扣
case class Bundle(description:String,discount:Double,item: Item*) extends Item
