package com.lishan.spark.core.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * 计算页面的单跳转换率
 * 就是打开了一个页面跳转到下一个页面的比率是多少
 * 1.将同一个用户额会话按照时间顺序汇总到一起
 * 2.
 */
object PageflowAnalysis {
  def main(args: Array[String]): Unit = {
    //创建环境
    val conf = new SparkConf().setMaster("local[*]").setAppName("reqOne")
    val sc = new SparkContext(conf)
    //TODO: TOP10热门品类
    //1.获取数据
    val actionRDD: RDD[String] = sc.textFile("spark-core/datas/user_visit_action_test.txt")
    //2.将数据映射到样例类
    val actionDataRDD = actionRDD.map(
      action => {
        val datas = action.split("_")
        UserVisitAction(
          datas(0),
          datas(1).toLong,
          datas(2),
          datas(3).toLong,
          datas(4),
          datas(5),
          datas(6).toLong,
          datas(7).toLong,
          datas(8),
          datas(9),
          datas(10),
          datas(11),
          datas(12).toLong
        )
      }
    )
    //由于计算分子和分母都会用到这个 rdd所以需要做一个缓存
    actionDataRDD.cache()

    //6. TODO 对指定的页面连续跳转进行统计
    // 1-2,2-3,3-4,4-5,5-6,6-7
    val ids = List[Long](1,2,3,4,5,6,7)
    //用于对分子进行过滤
    val okflowIds: List[(Long, Long)] = ids.zip(ids.tail)

    //3.  TODO:计算分母
    //只需要对所有页面的点击次数做一个统计即可
    val pageidToCountMap: Map[Long, Long] = actionDataRDD.filter(
      //对分母过滤
      action => {
        //init 去掉最后一个元素，因为最后一个元素永远不可能做为分母
        ids.init.contains(action.page_id)
      }
    ).map(
      action => {
        (action.page_id, 1L)
      }
    ).reduceByKey(_ + _).collect().toMap //由于分子分母是单独计算的所以需要对分母做一个采集；转换成map是因为方便后面计算寻找pageid

    //4.  TODO:计算分子
    //根据session分组将同一个人的会话分到一组
    val sessionRDD: RDD[(String, Iterable[UserVisitAction])] = actionDataRDD.groupBy(
      action => {
        action.session_id
      }
    )
    //根据访问时间进行一个排序
    val mvRDD: RDD[(String, List[((Long, Long), Int)])] = sessionRDD.mapValues(
      item => {
        //由于只需要对值处理所以用mapvalues 迭代器无法排序先转换为lis，t，根据每个对象的时间属性排序
        val sortList: List[UserVisitAction] = item.toList.sortBy(_.action_time)
        //不需要其他信息只需要 页面id
        val flowids: List[Long] = sortList.map(_.page_id)

        /**
         * 想要将页面转换成下面的效果 连续页面的跳转
         * 【1，2，3，4】
         * 【1，2】，【2，3】，【3，4】
         * 【1-2，2-3，3-4】
         * 通过zip : 拉链可以实现只需要复制一份将第一个元素(tail)去掉再与原数据进行拉链操作       （Sliding : 滑窗）
         * 【1，2，3，4】
         * 【2，3，4】
         */

        val pageflowids: List[(Long, Long)] = flowids.zip(flowids.tail)
        //对分子进行过滤
        pageflowids.filter(t => okflowIds.contains(t)).map(t => (t, 1)) //结构转换方便聚合

      }
    )

    //只需要 页面跳转信息  由于第二个元素是list所以使用flatmap算子
    val flatRDD: RDD[((Long, Long), Int)] = mvRDD.map(_._2).flatMap(list => list)
    
    //做聚合 // ((1,2),1) => ((1,2),sum)
    val dataRDD: RDD[((Long, Long), Int)] = flatRDD.reduceByKey(_ + _)

    //5.  TODO:计算单跳转换率
    dataRDD.foreach{
      case ((page1, page2), sum) =>
        val lon: Long = pageidToCountMap.getOrElse(page1, 0L)
        println(s"页面${page1}跳转到页面${page2}单跳转换率为:" + ( sum.toDouble/lon ))
    }



    //关闭环境
    sc.stop()
  }
}

//用户访问动作表 不用每次都分割数据
case class UserVisitAction(
                            date: String,//用户点击行为的日期
                            user_id: Long,//用户的 ID
                            session_id: String,//Session 的 ID
                            page_id: Long,//某个页面的 ID
                            action_time: String,//动作的时间点
                            search_keyword: String,//用户搜索的关键词
                            click_category_id: Long,//某一个商品品类的 ID
                            click_product_id: Long,//某一个商品的 ID
                            order_category_ids: String,//一次订单中所有品类的 ID 集合
                            order_product_ids: String,//一次订单中所有商品的 ID 集合
                            pay_category_ids: String,//一次支付中所有品类的 ID 集合
                            pay_product_ids: String,//一次支付中所有商品的 ID 集合
                            city_id: Long
                          )//城市 id
