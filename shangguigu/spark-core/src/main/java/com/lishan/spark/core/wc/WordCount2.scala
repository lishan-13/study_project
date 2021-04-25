package com.lishan.spark.core.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 利用reducubykey 将分组和聚合阶段一起执行
 */
object WordCount2 {
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

    //3.4 分组聚合 将key相同的value聚合操作
    val wordToCount = wordOneRDD.reduceByKey(_+_)

    //5.输出
    val array:Array[(String, Int)] = wordToCount.collect()
    array.foreach(println)

    //关闭连接
    sc.stop()
  }
}
