package com.lishan.spark.core.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * 改进：
 * 1.actionRDD存在多次重用
 * 2.cogroup 可能会降低性能
 */
object TypeTop10_2 {
  def main(args: Array[String]): Unit = {
    //创建环境
    val conf = new SparkConf().setMaster("local[*]").setAppName("reqOne")
    val sc = new SparkContext(conf)
    //TODO: TOP10热门品类
    //1.获取数据
    val actionRDD: RDD[String] = sc.textFile("spark-core/datas/user_visit_action.txt")
    /** 做缓存解决重用问题*/
    actionRDD.cache()

    //2.点击数的wc
    //先过滤无用数据
    val clickRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas = action.split("_")
        datas(6) != "-1"
      }
    )
    //做统计
    val clickWc: RDD[(String, Int)] = clickRDD.map(
      actino => {
        val datas = actino.split("_")
        //返回一个tuple 这返回的是单个元素
        (datas(6), 1)
      }
    ).reduceByKey(_+_)

    //3.下单数的wc
    //过滤数据
    val orderRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas = action.split("_")
        datas(8) != "null"
      }
    )
    //做统计 flatmap 返回的是一个可迭代的对象
    val orderWc: RDD[(String, Int)] = orderRDD.flatMap(
      action => {
        val datas = action.split("_")
        val flatdata = datas(8).split(",")
        //scala 的map操作 下面是一个Array[str,int]
        flatdata.map(id => (id, 1))

      }
    ).reduceByKey(_+_)

    //4.支付数的wc
    val payRDD = actionRDD.filter(
      action => {
        val datas = action.split("_")
        datas(10) != "null"
      }
    )

    // orderid => 1,2,3
    // 【(1,1)，(2,1)，(3,1)】
    val payWc = payRDD.flatMap(
      action => {
        val datas = action.split("_")
        val cid = datas(10)
        val cids = cid.split(",")
        cids.map(id=>(id, 1))
      }
    ).reduceByKey(_+_)
    //5.排序取top10

    // (品类ID, 点击数量) => (品类ID, (点击数量, 0, 0))
    // (品类ID, 下单数量) => (品类ID, (0, 下单数量, 0))
    //                    => (品类ID, (点击数量, 下单数量, 0))
    // (品类ID, 支付数量) => (品类ID, (0, 0, 支付数量))
    //                    => (品类ID, (点击数量, 下单数量, 支付数量))
    // ( 品类ID, ( 点击数量, 下单数量, 支付数量 ) )
    /**解决cogroup可能降低性能的问题*/

    val rdd1: RDD[(String, (Int, Int, Int))] = clickWc.map {
      case (id, count) =>
        (id, (count, 0, 0))
    }

    val rdd2: RDD[(String, (Int, Int, Int))] = clickWc.map {
      case (id, count) =>
        (id, (0, count, 0))
    }

    val rdd3: RDD[(String, (Int, Int, Int))] = clickWc.map {
      case (id, count) =>
        (id, (0, 0, count))
    }

    //Union：将两个RDD进行合并，不去重
    val rdd4: RDD[(String, (Int, Int, Int))] = rdd1.union(rdd2).union(rdd3)
    //聚合
    val analyRDD: RDD[(String, (Int, Int, Int))] = rdd4.reduceByKey(
      (t1, t2) =>
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
    )


    //6.打印 输出
    //sortby 会自动将元组里的数据按照需求排序 flase参数：降序
    //采集数据 因为take(10)已经采集了 所以不需要collect
    val resultRDD: Array[(String, (Int, Int, Int))] = analyRDD.sortBy(_._2, false).take(10)
    resultRDD.foreach(println)



    //关闭环境
    sc.stop()
  }


}
