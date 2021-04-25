package com.lishan.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 本项目需求优化为：先按照点击数排名，靠前的就排名高；如果点击数相同，再比较下
 * 单数；下单数再相同，就比较支付数。
 * 2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_37_2019-07-17 00:00:02_手机_-1_-1_null_null_null_null_3
 * 2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_48_2019-07-17 00:00:10_null_16_98_null_null_null_null_19
 * 2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_6_2019-07-17 00:00:17_null_19_85_null_null_null_null_7
 * 2019-07-17_38_6502cdc9-cf95-4b08-8854-f03a25baa917_29_2019-07-17 00:00:19_null_12_36_null_null_null_null_5
 * 2019-07-17_38_6502cdc9-cf95-4b08-8854-f03a25baa917_22_2019-07-17 00:00:28_null_-1_-1_null_null_15,1,20,6,4_15,88,75_9
 */
object TypeTop10 {
  def main(args: Array[String]): Unit = {
    //创建环境
    val conf = new SparkConf().setMaster("local[*]").setAppName("reqOne")
    val sc = new SparkContext(conf)
    //TODO: TOP10热门品类
    //1.获取数据
    val actionRDD: RDD[String] = sc.textFile("spark-core/datas/user_visit_action.txt")
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
    //    ( 品类ID, ( 点击数量, 下单数量, 支付数量 ) ) 使用cogroup连接
    //cogroup:对两个RDD中的KV元素，每个RDD中相同key中的元素分别聚合成一个集合
    val coRDD: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] = clickWc.cogroup(orderWc, payWc)
    //由于上面的count是一个可迭代的集合，是不需要的所以需要取出集合里的值

    val analyRDD: RDD[(String, (Int, Int, Int))] = coRDD.mapValues {
      case (click, order, pay) =>
        //用于接收迭代器里面的值
        var clickcount = 0
        //获得迭代器
        val iter1 = click.iterator
        if (iter1.hasNext) {
          clickcount = iter1.next()
        }

        var ordercount = 0
        val iter2 = order.iterator
        if (iter2.hasNext) {
          ordercount = iter2.next()
        }

        var paycount = 0
        val iter3 = pay.iterator
        if (iter3.hasNext) {
          paycount = iter3.next()
        }

        (clickcount, ordercount, paycount)

    }


    //6.打印 输出
    //sortby 会自动将元组里的数据按照需求排序 flase参数：降序
    //采集数据 因为take(10)已经采集了 所以不需要collect
    val resultRDD: Array[(String, (Int, Int, Int))] = analyRDD.sortBy(_._2, false).take(10)
    resultRDD.foreach(println)



    //关闭环境
    sc.stop()
  }

}
