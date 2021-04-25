package com.lishan.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class MyProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        //kafka集群 broker-list
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop01:9092");
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        //重试次数
        props.put("retries", 1);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间  每隔一毫秒发送一次数据
        props.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        //创建生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(props);
        for(int i=0;i<10;i++){
            //ProducerRecord:将数据封装成一个ProducerRecord对象
            producer.send(new ProducerRecord<String, String>("first","guigu--"+i));
        }

        //关闭环境  关闭操作会触发发送数据，如果不关闭环境  而发送的数据不到batch.size，且程序运行完毕不到一毫秒
        //那么消费者就收不到数据
        producer.close();



    }
}
