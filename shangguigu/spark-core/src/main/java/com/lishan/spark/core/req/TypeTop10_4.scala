package com.lishan.spark.core.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

/**
 * 在3的基础上改进
 * 此次一个reducebykey都不要 使用累加器，直接遍历每个分区将结果累加
 */
object TypeTop10_4 {
  def main(args: Array[String]): Unit = {
    //创建环境
    val conf = new SparkConf().setMaster("local[*]").setAppName("reqOne")
    val sc = new SparkContext(conf)
    //TODO: TOP10热门品类
    //1.获取数据
    val actionRDD: RDD[String] = sc.textFile("spark-core/datas/user_visit_action.txt")
    //3.创建累计器对象 注册累加器
    val acc = new HotCategoryAccumulator
    sc.register(acc, "hotCategory")

    //4.数据结构转换 使用累加器
    actionRDD.foreach{
      action =>
        val datas = action.split("_")
        //点击行为
        if(datas(6) != "-1"){
          acc.add((datas(6),"click"))
        }else if(datas(8) != "null"){
          val ids: Array[String] = datas(8).split(",")
          //下订单行为
          ids.foreach(id => acc.add((id,"order")))
        }else if(datas(10) != "null"){
          val ids: Array[String] = datas(10).split(",")
          //支付行为
          ids.foreach(id => acc.add((id,"pay")))
        }
    }
    //5.排序
    val accVal: mutable.Map[String, HotCategory] = acc.value
    //由于对象中有cid 所以不需要第一个元素
    val categories: mutable.Iterable[HotCategory] = accVal.map(_._2)
    //将categories可迭代集合转换为list
    //sortWith可以通过制定规则进行升降序排序
    val sort = categories.toList.sortWith(
      //嵌套判断 排序
      (left,right) => {
        if(left.clickCnt > right.clickCnt){
          true
        }else if(left.clickCnt == right.clickCnt){
          if(left.orderCnt > right.orderCnt){
            true
          }else if(left.orderCnt == right.orderCnt){
            left.payCnt > right.payCnt
          }else{
            false
          }
        }else{
          false
        }
      }
    )
    //6.打印
    sort.take(10).foreach(println)


    //关闭环境
    sc.stop()
  }
}

//2.定义累加器
/**
 * 自定义累加器
 * 1. 继承AccumulatorV2，定义泛型
 *    IN : ( 品类ID, 行为类型 )
 *    OUT : mutable.Map[String, HotCategory]
 * 2. 重写方法（6）
 */
//创建一个样例类用来保存数据 由于count属性是可变的所以定义为 var类型
//用id来创建对象；用行为类型来判断 累加
case class HotCategory( cid:String, var clickCnt : Int, var orderCnt : Int, var payCnt : Int )
class HotCategoryAccumulator extends AccumulatorV2[(String, String), mutable.Map[String, HotCategory]]{
  //创建返回的累加map
  private val hcMap = mutable.Map[String,HotCategory]()
  override def isZero: Boolean = {
    hcMap.isEmpty
  }

  override def copy(): AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] = {
    new HotCategoryAccumulator
  }

  override def reset(): Unit = {
    hcMap.clear()
  }

  override def add(v: (String, String)): Unit = {
    val cid = v._1
    val actionType = v._2
    //用id来创建对象；如多有就返回那个id的对象如果没有就创建一个
    // 用行为类型来判断 累加
    val category: HotCategory = hcMap.getOrElse(cid, HotCategory(cid, 0, 0, 0))
    if(actionType == "click"){
      category.clickCnt += 1
    }else if(actionType == "order"){
      category.orderCnt += 1
    }else if(actionType == "pay"){
      category.payCnt += 1
    }
    //更新map
    hcMap.update(cid,category)
  }

  override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, HotCategory]]): Unit = {
    val map1 = this.hcMap
    val map2 = other.value
    //遍历另一个map 看当前map是否包含
    //不存在创建一个新的 用于存在map1
    map2.foreach {
      case (cid,hc ) =>
        val category: HotCategory = map1.getOrElse(cid, HotCategory(cid, 0, 0, 0))
        category.payCnt += hc.payCnt
        category.orderCnt += hc.orderCnt
        category.clickCnt += hc.clickCnt
        map1.update(cid,category)
    }

  }

  override def value: mutable.Map[String, HotCategory] = {
    hcMap
  }
}
