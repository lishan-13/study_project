package com.lishan.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object HiveDemo1 {
  def main(args: Array[String]): Unit = {
    // TODO 创建SparkSQL的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()
    import spark.implicits._

    System.setProperty("HADOOP_USER_NAME", "root")

    //选择数据库
    spark.sql("use SparkSQLdb")

    //操作



    //TODO 关闭环境
    spark.close()
  }
}
