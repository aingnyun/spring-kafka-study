import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class RebalanceConsumer {
    private final static Logger logger = LoggerFactory.getLogger(RebalanceConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static int PARTITION_NUMBER = 0;
    private final static String BOOTSTRAP_SERVER = "my-kafka:9092";
    private final static String GROUP_ID = "test-group";
    public static void main(String[] args) {
        Properties configs = new Properties();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
        consumer.assign(Collections.singleton(new TopicPartition(TOPIC_NAME, 0)));
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

        consumer.subscribe(Arrays.asList(TOPIC_NAME), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                logger.warn("Partitions are assigned");
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                logger.warn("Partitions are revoked");
                consumer.commitSync(currentOffsets);
            }
        });

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}", record);
                currentOffsets.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1, null));
                consumer.commitSync(currentOffsets);
            }
        }
    }
}
