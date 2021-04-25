package com.lishan.spark.core.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * 在2.0的基础上改进
 * 1.有许多reducebykey操作 存在大量shuffle操作，降低性能
 */
object TypeTop10_3 {
  def main(args: Array[String]): Unit = {
    //创建环境
    val conf = new SparkConf().setMaster("local[*]").setAppName("reqOne")
    val sc = new SparkContext(conf)
    //TODO: TOP10热门品类
    //1.获取数据
    val actionRDD: RDD[String] = sc.textFile("spark-core/datas/user_visit_action.txt")

    //2.改变数据结构
    //    点击的场合 : ( 品类ID，( 1, 0, 0 ) )
    //    下单的场合 : ( 品类ID，( 0, 1, 0 ) )
    //    支付的场合 : ( 品类ID，( 0, 0, 1 ) )
    val flatRDD: RDD[(String, (Int, Int, Int))] = actionRDD.flatMap(
      action => {
        val datas = action.split("_")
        if (datas(6) != "-1") {
          //flatmap 需要返回一个迭代器集合
          List((datas(6), (1, 0, 0)))
        } else if (datas(8) != "null") {
          val idList = datas(8).split(",")
          idList.map(id => (id, (0, 1, 0)))
        } else if (datas(10) != "null") {
          val idList = datas(10).split(",")
          idList.map(id => (id, (0, 0, 1)))
        } else {
          Nil
        }
      }
    )

    //聚合
    val analyRDD: RDD[(String, (Int, Int, Int))] = flatRDD.reduceByKey(
      (t1, t2) =>
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
    )

    //3.打印 输出
    //sortby 会自动将元组里的数据按照需求排序 flase参数：降序
    //采集数据 因为take(10)已经采集了 所以不需要collect
    val resultRDD: Array[(String, (Int, Int, Int))] = analyRDD.sortBy(_._2, false).take(10)
    resultRDD.foreach(println)



    //关闭环境
    sc.stop()
  }
}
