package com.lishan.spark.core.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * Top10 热门品类中每个品类的 Top10 活跃 Session 统计
 * 在需求一的基础上，增加每个品类用户 session 的点击统计
 * 就是在top10的每个类别里面找到点击次数最多的十个用户 排序
 */
object TypeTop10Session {
  def main(args: Array[String]): Unit = {
    //创建环境
    val conf = new SparkConf().setMaster("local[*]").setAppName("reqOne")
    val sc = new SparkContext(conf)
    //TODO: TOP10热门品类
    //1.获取数据
    val actionRDD: RDD[String] = sc.textFile("spark-core/datas/user_visit_action.txt")
    actionRDD.cache()

    //2.获取top10的热门品类
    val top10ids: Array[String] = top10Category(actionRDD)

    //3.过滤掉非top10的品类
    val filterRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas: Array[String] = action.split("_")
        if (datas(6) != "-1") {
          //只需要 在top10里面的点击行为
          top10ids.contains(datas(6))
        } else {
          false
        }
      }
    )

    //4.分组((id,session),1)
    //因为要统计每个品类的session top10 所以需要将id和session作为联合key
    val reduceRDD: RDD[((String, String), Int)] = filterRDD.map(
      action => {
        val datas: Array[String] = action.split("_")
        val ssession = datas(2)
        val id = datas(6)
        ((id, ssession), 1)
      }
    ).reduceByKey(_+_)

    //5.结构转换 (id,(session,count))
    val mapRDD: RDD[(String, (String, Int))] = reduceRDD.map {
      case ((cid, sid), count) => {
        (cid, (sid, count))
      }
    }

    //6.因为换了key需要重新聚合
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = mapRDD.groupByKey()

    //7.排序 只针对rdd里面的value排序 是一个可迭代的集合
    val resultRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
      item => {
        //转换为list排序  降序
        item.toList.sortBy(_._2)(Ordering.Int.reverse).take(10)
      }
    )

    //8.打印
    resultRDD.collect().foreach(println)




    //关闭环境
    sc.stop()
  }
  def top10Category(actionRDD: RDD[String]) ={
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

    analyRDD.sortBy(_._2, false).take(10).map(_._1)

  }

}
