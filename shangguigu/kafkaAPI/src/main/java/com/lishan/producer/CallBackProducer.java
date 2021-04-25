package com.lishan.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class CallBackProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        //kafka集群 broker-list
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop01:9092");
        props.put(ProducerConfig.ACKS_CONFIG,"all");

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        //创建生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(props);
        for(int i=0;i<10;i++){
            //如果指定了分区 那么数据就只会生产到一个分区
            //如果只有key没有指定分区那么分区值取key的哈希值对3取模
            //如果分区和key都没有指定  那么就采用轮询的方式 轮询创建主题十的分区
            producer.send(new ProducerRecord<String, String>("first",0,"k","guigu--"+i), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if(e == null){
                        //返回数据的分区信息 以及每个分区消费数据的记录offset
                        System.out.println(metadata.partition()+"----"+metadata.offset());
                    }else {
                        e.printStackTrace();
                    }
                }
            });
        }

        //关闭环境  关闭操作会触发发送数据，如果不关闭环境  而发送的数据不到batch.size，且程序运行完毕不到一毫秒
        //那么消费者就收不到数据
        producer.close();
    }
}
