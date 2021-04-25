package com.lishan.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object connHive {
  def main(args: Array[String]): Unit = {
    // TODO 创建SparkSQL的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()
    import spark.implicits._

    //使用sparksql 连接外置hive
    //1.拷贝hive-site.xml
    //2.启用对hive的支持 enableHiveSupport()
    //3.导入相关依赖

    spark.sql("show tables").show()

    //TODO 关闭环境
    spark.close()
  }
}
