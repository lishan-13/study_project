package com.lishan.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object Sparksql_Basic {
  def main(args: Array[String]): Unit = {
    //TODO 创建环境
    val conf = new SparkConf().setMaster("local[*]").setAppName("sql")
    //创建上下文对象
    val spark = SparkSession.builder().config(conf).getOrCreate()
    //RDD=>DataFrame=>DataSet 转换需要引入隐式转换规则，否则无法转换
    import spark.implicits._

    //TODO 逻辑
    //读取json创建df
    val df: DataFrame = spark.read.json("spark-core/datas/test.json")
    //df.show()

    //SQL 语法风格
    //1.创建一个临时表 方便查询
    df.createOrReplaceTempView("user")
    //2.利用上下文对象查询
    spark.sql("select * from user").show()

    //DSL语法风格
    df.select("age").show()

    //RDD <=> DataFrame
    //创建rdd
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(List((1, "lisi", 30), (2, "zhangsan", 20)))
    //1.rdd转df需要加入结构
    val df1: DataFrame = rdd.toDF("id", "name", "age")
    //2.df转rdd
    val rdd1: RDD[Row] = df1.rdd

    //DataFrame <=> DataSet
    //1.df转ds需要加入类型 case class
    val ds1: Dataset[User] = df1.as[User]
    //2.ds 转df
    val df2: DataFrame = ds1.toDF()

    //rdd <=> ds
    //1.rdd转ds
    val ds2: DataFrame = rdd.map {
      case (id, name, age) => {
        User(id, name, age)
      }
    }.toDF()
    //2.ds 转rdd
    val rdd3: RDD[Row] = ds2.rdd


    //TODO 关闭环境
    spark.close()

  }
}

case class User(id:Int,name:String,age:Int)
