package com.Agilysys.TipMock.Configuration;

import com.Agilysys.TipMock.Properties.ApplicationProperties;
import com.Agilysys.TipMock.Properties.KafkaProperties;
import com.Agilysys.TipMock.Util.MultiThreadConsumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConsumerThread {
    private String topics;

    public void startConsume() throws IOException {
        Properties appProp = ApplicationProperties.getProperties();
        Boolean avroSerialization = Boolean.valueOf(appProp.get("AvroSerialialzation").toString());
        topics=appProp.get("Topics").toString();
        List<String> topicsList = Arrays.stream(topics.split(",")).toList();

        ExecutorService executorService = Executors.newFixedThreadPool(topicsList.size());

        topicsList.stream().forEach((x) -> {
            KafkaConsumer<String, ?> consumer = new KafkaConsumer<>(Boolean.valueOf(appProp.get("AvroSerialialzation").toString()) ? KafkaProperties.kafkaConsumerWithAvro() : KafkaProperties.kafkaConsumerWithJSON());

            executorService.submit(new MultiThreadConsumer(x, consumer, avroSerialization));
        });
    }
}

