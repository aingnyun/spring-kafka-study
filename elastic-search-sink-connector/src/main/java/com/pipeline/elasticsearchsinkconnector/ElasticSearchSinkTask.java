package com.pipeline.elasticsearchsinkconnector;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class ElasticSearchSinkTask extends SinkTask {
    private final Logger logger = LoggerFactory.getLogger(ElasticSearchSinkTask.class);
    private ElasticSearchSinkConnectorConfig config;
    private RestHighLevelClient esClient;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        try {
            config = new ElasticSearchSinkConnectorConfig(props);
        } catch (ConfigException e) {
            throw new ConnectException(e.getMessage(), e);
        }

        esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(config.getString(config.ES_CLUSTER_HOST), config.getInt(config.ES_CLUSTER_PORT))));
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        if (records.size() > 0) {
            final var bulkRequest = new BulkRequest();
            final var objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

            for (var record : records) {
                logger.info("record: {}", record.value());
                logger.info("record toString: {}", record.value().toString());

                try {
                    Map<String, String> map = objectMapper.readValue(record.value().toString(), Map.class);
                    bulkRequest.add(new IndexRequest(config.getString(config.ES_INDEX))
                            .source(map, XContentType.JSON));
                } catch (Exception e) {
                    logger.error("record: {}", record.value());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void stop() {
        try {
            esClient.close();
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        logger.info("flush");
    }

}
