package com.lishan.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object connHiveTest {
  def main(args: Array[String]): Unit = {
    // TODO 创建SparkSQL的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()
    import spark.implicits._

    System.setProperty("HADOOP_USER_NAME", "root")

    //选择数据库
    spark.sql("use SparkSQLdb")

    //准备数据
    spark.sql(
      """
        |CREATE TABLE `user_visit_action`(
        | `date` string,
        | `user_id` bigint,
        | `session_id` string,
        | `page_id` bigint,
        | `action_time` string,
        | `search_keyword` string,
        | `click_category_id` bigint,
        | `click_product_id` bigint,
        | `order_category_ids` string,
        | `order_product_ids` string,
        | `pay_category_ids` string,
        | `pay_product_ids` string,
        | `city_id` bigint)
        |row format delimited fields terminated by '\t'
        |""".stripMargin
    )

    spark.sql(
      """
        |load data local inpath 'spark-core/datas/user_visit_action.txt' into table
        |SparkSQLdb.user_visit_action
        |""".stripMargin)

    spark.sql(
      """
        |CREATE TABLE `product_info`(
        | `product_id` bigint,
        | `product_name` string,
        | `extend_info` string)
        |row format delimited fields terminated by '\t'
        |""".stripMargin)

    spark.sql(
      """
        |load data local inpath 'spark-core/datas/product_info.txt' into table  SparkSQLdb.product_info
        |""".stripMargin)

    spark.sql(
      """
        |CREATE TABLE `city_info`(
        | `city_id` bigint,
        | `city_name` string,
        | `area` string)
        |row format delimited fields terminated by '\t'
        |""".stripMargin)

    spark.sql(
      """
        |load data local inpath 'spark-core/datas/city_info.txt' into table SparkSQLdb.city_info
        |""".stripMargin)

    //三张表创建完成 验证
    spark.sql("select * from city_info").show()


    //TODO 关闭环境
    spark.close()
  }
}
