package com.lishan.spark.core.acc

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * 自定义累加器 完成词频统计
 */
object AccWC {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("acc")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List("hello","scala","hello"))
    //创建累加器对象
    val acc = new MyAcc
    //想spark注册累加器
    sc.register(acc,"myacc")

    rdd.foreach{
      word =>
        //使用累加方法
        acc.add(word)
    }
    //获取结果
    println(acc.value)

    sc.stop()

  }

}

/**
 * 如果想定义累加器对象 必须继承AccumulatorV2类
 */
class MyAcc extends AccumulatorV2[String, mutable.Map[String, Long]]{
  //创建一个map 用于接收统计结果
  private var myMap = mutable.Map[String,Long]()

  //判断初始化状态
  override def isZero: Boolean = {
    myMap.isEmpty
  }

  //复制一个累加器
  override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
    new MyAcc
  }

  //初始化累加器
  override def reset(): Unit = {
    myMap.clear()
  }

  //具体累加方法 在每个executer执行
  override def add(word: String): Unit = {
    //判断单词是否存在 存在返回返回key+1 不存在就是第一次进入返回1
    //存不存在都加1 更新
    val newcount = myMap.getOrElse(word,0L) + 1
    //跟新map
    myMap.update(word,newcount)

  }

  //将每个分区的数据聚合
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
    val map1 = this.myMap
    val map2 = other.value

    map2.foreach{
      case (word,count) => {
        //如果不存在就在map1存入新的1键值对 存在就将两个value相加
        //存不存在都加count  map1.getOrElse(word,0l)只是为了获取map1的结果
        val newcount = map1.getOrElse(word,0l) + count
        //跟新map1
        map1.update(word,newcount)
      }
    }

  }

  //返回结果
  override def value: mutable.Map[String, Long] = {
    myMap
  }
}