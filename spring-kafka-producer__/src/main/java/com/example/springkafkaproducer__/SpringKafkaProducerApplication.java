package com.example.springkafkaproducer__;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

//@SpringBootApplication
//public class SpringKafkaProducerApplication implements CommandLineRunner {
//
//    private static String TOPIC_NAME = "test";
//
//    @Autowired
//    private KafkaTemplate<Integer, String> customKafkaTemplate;
//
//
//    public static void main(String[] args) {
//        SpringApplication.run(SpringKafkaProducerApplication.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        ListenableFuture<SendResult<Integer, String>> future = customKafkaTemplate.send(TOPIC_NAME, "test");
//
//        future.addCallback(new KafkaSendCallback<String, String>() {
//            @Override
//            public void onSuccess(SendResult<String, String> result){}
//        });
//        System.exit(0);
//    }
//}
