package com.lishan.spark.core.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * wc的不同实现
 */
object wc {
  def main(args: Array[String]): Unit = {
    //环境准备
    val conf = new SparkConf().setMaster("local[*]").setAppName("rdd")
    val sc = new SparkContext(conf)

    //调用方法
    val res = wc06(sc)
    println("----------")
    wc07(sc)
    println("----------")
    wc08(sc)
    println("----------")
    wc91011(sc)
    println("----------")

    //输出
    res.collect().foreach(println)

    //关闭环境
    sc.stop()
  }

  //1.使用groupby算子
  def wc01(sc:SparkContext) ={
    //数据准备
    val rdd = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    //数据分割
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    val rdd3: RDD[(String, Iterable[String])] = rdd2.groupBy(word => word)
    /*
    (Hello,CompactBuffer(Hello, Hello))
    (Spark,CompactBuffer(Spark))
    (Scala,CompactBuffer(Scala))
     */
//    val rdd4: RDD[(String, Int)] = rdd3.map(res => {
//      (res._1, res._2.size)
//    })
    //因为key不变 所以可用mapvalues()操作值就行
    val rdd4: RDD[(String, Int)] = rdd3.mapValues(item => item.size)
    rdd4
  }

  //2.使用groupByKey算子
  def wc02(sc: SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    //因为groupbykey操作的是tuple类型 所以要先进行数据的转换
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))
    //
    val rdd4: RDD[(String, Iterable[Int])] = rdd3.groupByKey()
    //聚合 item => item.size 迭代结合的长度item.size 或整数集合的和item.sum 都可
    val rdd5: RDD[(String, Int)] = rdd4.mapValues(item => item.sum)
    rdd5
  }

  //3.reducebykey
  def wc03(sc:SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    //数据转换
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))
    //分组聚合 对相同key的 value进行聚合
    val rdd4: RDD[(String, Int)] = rdd3.reduceByKey(_ + _)
    rdd4

  }

  //4.aggregateByKey 将数据根据不同的规则进行分区内计算和分区间计算
  def wc04(sc:SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    //数据转换
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))
    val rdd4: RDD[(String, Int)] = rdd3.aggregateByKey(0)(_ + _, _ + _)
    rdd4
  }

  //5.foldByKey 分区内分区间计算规则相同
  def wc05(sc:SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    //数据转换
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))
    val rdd4: RDD[(String, Int)] = rdd3.foldByKey(0)(_ + _)
    rdd4
  }

  //6.combineByKey
  def wc06(sc:SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    //数据转换
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))

    val rdd4: RDD[(String, Int)] = rdd3.combineByKey(
      (v => v),
      //分区内类型不能改变都是v的类型int
      ((x: Int, v) => x + v),
      //分区间运算 将每个分区的结果 聚合
      ((x: Int, y: Int) => x + y)
    )

    rdd4
  }

  //7.countByKey
  def wc07(sc:SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    //数据转换
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))
    //返回一个map
    val map: collection.Map[String, Long] = rdd3.countByKey()
    for((k,v)<-map){
      println((k,v))
    }
  }

  //8.countByValue
  def wc08(sc:SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))

    //返回一个map
    val map: collection.Map[String, Long] = rdd2.countByValue()
    for((k,v)<-map){
      println((k,v))
    }
  }

  //9 10 11 reduce, aggregate, fold
  def wc91011(sc:SparkContext) ={
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    //数据转换
    // 【（word, count）,(word, count)】
    // word => Map[(word,1)]
    val rdd3 = rdd2.map(
      word => {
        mutable.Map[String, Long]((word,1))
      }
    )
    //ruduce 聚集 RDD 中的所有元素，先聚合分区内数据，再聚合分区间数据  分区内分区间聚合逻辑相同
    val rdd4: mutable.Map[String, Long] = rdd3.reduce(
      //map1 map2是每个元素
      (map1, map2) => {
        map2.foreach {
              //模式匹配
          case (word, count) => {
            val newCount = map1.getOrElse(word, 0L) + count
            //跟新map1 将后续的所有结果都加入到这个集合
            map1.update(word, newCount)
          }
        }
        map1
      }
    )
    //因为rudecu是行动算子 所以不需要采集，有返回结果
    println(rdd4)
  }

}
