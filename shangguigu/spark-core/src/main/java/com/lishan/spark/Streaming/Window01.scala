package com.lishan.spark.Streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Window01 {
  def main(args: Array[String]): Unit = {
    //TODO 创建环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    //ssc配置需要两个参数 一是spark配置对象 二是采集周期
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //TODO 逻辑处理
    //通过端口监控 获取数据
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
    val wordToOne = lines.map((_,1))
    // 窗口的范围应该是采集周期的整数倍
    // 窗口可以滑动的，但是默认情况下，一个采集周期进行滑动
    // 这样的话，可能会出现重复数据的计算，为了避免这种情况，可以改变滑动的滑动（步长）
    val windowDS: DStream[(String, Int)] = wordToOne.window(Seconds(6), Seconds(6))  //窗口大小为6 滑动步长为6

    //采集到了一个窗口的数据 做处理
    val wc: DStream[(String, Int)] = windowDS.reduceByKey(_ + _)

    wc.print()

    //TODO 开启采集器
    //由于SparkStreaming采集器是长期执行的任务，所以不能直接关闭
    ssc.start()
    //等待采集器关闭
    ssc.awaitTermination()

  }
}
