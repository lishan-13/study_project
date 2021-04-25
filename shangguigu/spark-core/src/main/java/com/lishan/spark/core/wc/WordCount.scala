package com.lishan.spark.core.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    //创建spark 运行配置对象
    val sparkConf = new SparkConf().setMaster("local").setAppName("wordcount")
    //创建spark上下文对象
    val sc = new SparkContext(sparkConf)

    //1.读取数据 一行一行的
    val fillRDD:RDD[String] = sc.textFile("spark-core/datas")

    //2.切分数据
    val wordRDD:RDD[String] = fillRDD.flatMap(_.split(" "))
    //对数据进行转换
    val wordOneRDD:RDD[(String, Int)] = wordRDD.map{
      word => (word,1)
    }

    //3.根据单词进行分组，便于统计
    val wordGroup:RDD[(String, Iterable[(String, Int)])]  = wordOneRDD.groupBy(word => word._1)

    //4.对分组的数据进行聚合
    val wordToCount = wordGroup.map{
      case(word,list) =>
        list.reduce((t1,t2) => (t1._1,t1._2+t2._2))
    }

    //5.输出
    val array:Array[(String, Int)] = wordToCount.collect()
    array.foreach(println)

    //关闭连接
    sc.stop()
  }
}
