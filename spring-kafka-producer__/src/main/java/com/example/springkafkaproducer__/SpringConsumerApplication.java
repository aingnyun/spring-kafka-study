package com.example.springkafkaproducer__;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

@SpringBootApplication
public class SpringConsumerApplication {
    public static Logger logger = LoggerFactory.getLogger(SpringConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringConsumerApplication.class);
        application.run(args);
    }

    /**
     * 레코드 리스너
     */

    @KafkaListener(topics = "test", groupId = "test-group-00")
    public void recordListener(ConsumerRecord<String, String> record) {
        logger.info(record.toString());
    }

    @KafkaListener(topics = "test", groupId = "test-group-01")
    public void singleTopicListener(String messageValue) {
        logger.info(messageValue);
    }

    @KafkaListener(topics = "test", groupId = "test-group-02", properties = {
            "max.poll.interval.me:60000",
            "auto.offset.reset:earliest"
    })
    public void singleTopicWithPropertiesListener(String messageValue) {
        logger.info(messageValue);
    }

    @KafkaListener(topics = "test", groupId = "test-group-03", concurrency = "3")
    public void concurrentTopicListener(String messageValue) {
        logger.info(messageValue);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "test01", partitions = {"0", "1"}),
            @TopicPartition(topic = "test02", partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "3"))
    }, groupId = "test-group-04")
    public void listenSpecificPartition(ConsumerRecord<String, String> record) {
        logger.info(record.toString());
    }
    /**
     * 배치 리스너
     */

    @KafkaListener(topics = "test", groupId = "test-group-01")
    public void batchListener(ConsumerRecords<String, String> records) {
        records.forEach(record -> logger.info(record.toString()));
    }

    @KafkaListener(topics = "test", groupId = "test-group-02")
    public void batchListener(List<String> list) {
        list.forEach(recordValue ->logger.info(recordValue));
    }

    @KafkaListener(topics= "test", groupId = "test-group-03", concurrency = "3")
    public void concurrentBatchListener(ConsumerRecords<String, String> records) {
        records.forEach(record -> logger.info(record.toString()));
    }

    /**
     * 커밋 리스너
     */
    @KafkaListener(topics = "test", groupId = "test-group-02")
    public void commitListener(ConsumerRecords<String, String> records, Acknowledgment ack) {
        records.forEach(record ->logger.info(record.toString()));
        ack.acknowledge();
    }

    @KafkaListener(topics= "test", groupId = "test-group-03", concurrency = "3")
    public void consumerCommitListener(ConsumerRecords<String, String> records, Consumer<String, String> consumer) {
        records.forEach(record -> logger.info(record.toString()));
        consumer.commitAsync();
    }

    /**
     * 커스텀 리스너
     */
    @KafkaListener(topics = "test", groupId = "test-group-02", containerFactory = "customContainerFactory")
    public void customListener(String data) {
        logger.info(data);
    }






}
