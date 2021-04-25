package com.lishan.spark.core.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MemoryRDD{
  def main(args: Array[String]): Unit = {
    //环境准备
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)

    //创建rdd  将内存中的集合作为数据源
    val seq = Seq[Int](1, 2, 3, 4)
    //并行流创建rdd 名字不好记通常不用这个方法创建rdd
    //val rdd:RDD[Int] = sc.parallelize(seq)

    //makeRDD创建
    val rdd: RDD[Int] = sc.makeRDD(seq)


    rdd.collect().foreach(println)
    //关闭环境
    sc.stop()
  }
}
