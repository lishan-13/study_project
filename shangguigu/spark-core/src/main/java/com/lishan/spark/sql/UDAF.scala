package com.lishan.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, functions}

/**
 * UDAF 聚合函数
 */
object UDAF {
  def main(args: Array[String]): Unit = {

    // TODO 创建SparkSQL的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    //读取json创建 df
    val df: DataFrame = spark.read.json("spark-core/datas/test.json")
    //创建临时表  方便使用sql语句查询
    df.createOrReplaceTempView("user")
    //注册自定义函数 functions.udaf将强类型转换为低类型
    spark.udf.register("ageAvg", functions.udaf(new MyAvgUDAF()))

    spark.sql("select ageAvg(age) from user").show()
    //TODO 关闭环境
    spark.close()
  }
  /*
 自定义聚合函数类：计算年龄的平均值
 1. 继承org.apache.spark.sql.expressions.Aggregator, 定义泛型
     IN : 输入的数据类型 Long
     BUF : 缓冲区的数据类型 Buff
     OUT : 输出的数据类型 Long
 2. 重写方法(6)
 */
  //创建样例类 用于存放缓冲区的数据
  case class Buff(var total:Long,var count:Long)

  class MyAvgUDAF extends Aggregator[Long,Buff,Long]{
    // 缓冲区的初始化
    override def zero: Buff = {
      Buff(0L,0L)
    }
    // 聚合操作 根据输入数据跟新缓冲区
    override def reduce(buff: Buff, in: Long): Buff = {
      buff.total += in
      buff.count += 1
      buff
    }

    // 合并缓冲区
    override def merge(b1: Buff, b2: Buff): Buff = {
      b1.count += b2.count
      b1.total += b2.total
      b1
    }

    //计算结果
    override def finish(reducebuff: Buff): Long = {
      reducebuff.total / reducebuff.count
    }

    //在网络中传输涉及到序列化问题，所以需要编码
    //缓冲区的编码操作 自定义的buff就使用 product
    override def bufferEncoder: Encoder[Buff] = Encoders.product

    // 输出的编码操作
    override def outputEncoder: Encoder[Long] = Encoders.scalaLong
  }
}
