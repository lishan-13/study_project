package com.lishan.spark.core.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 1.获取数据分割
 * 2.数据转换 （（省份，广告），1）->（（省份，广告），sum）-> （省份，（广告，sum））
 * 3.根据省份分组
 * 4.组内排序
 */
object demo {
  def main(args: Array[String]): Unit = {
    //环境准备
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)
    
    //数据操作
    //1.获取数据
    val rdd:RDD[String] = sc.textFile("spark-core/datas/agent.log")
    //2.数据转换（（省份，广告），1）
    val rdd2 = rdd.map(line =>
    {
      val datas = line.split(" ")
      ((datas(1),datas(4)),1)
    })
    //3.聚合（（省份，广告），sum）
    val rdd3 = rdd2.reduceByKey(_ + _)
    //4.数据转换 （（省份，广告），sum）-> （省份，（广告，sum））
    val rdd4 = rdd3.map(res => {
      (res._1._1,(res._1._2,res._2))
    })

    //5.groupBykey：将数据源的数据根据 key 对 value 进行分组
    val rdd5 = rdd4.groupByKey()
    //6.组内排序 由于value是可迭代的集合需转换为list
    // sortBy(排序依据)(排序方法)是只对value操作不对key操作的算子
    //
    val rdd6 = rdd5.mapValues(item => {
      item.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
    })
    //7.数据采集
    val array = rdd6.collect()
    array.foreach(println)
    //关闭环境
    sc.stop()

  }
}
