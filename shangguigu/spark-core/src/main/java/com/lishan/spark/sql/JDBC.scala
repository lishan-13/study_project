package com.lishan.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object JDBC {
  def main(args: Array[String]): Unit = {

    // TODO 创建SparkSQL的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._

    // 读取MySQL数据
    val df = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/javaweb1")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", "student")
      .load()
    df.show()
    // 保存数据
    df.write
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/javaweb1")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", "student3")
      .mode(SaveMode.Append)
      .save()


    //TODO 关闭环境
    spark.close()

  }
}
