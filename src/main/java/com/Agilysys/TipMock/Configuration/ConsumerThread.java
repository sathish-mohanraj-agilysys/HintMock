package com.Agilysys.TipMock.Configuration;

import com.Agilysys.TipMock.KafKaProperties.KafkaProperties;
import com.Agilysys.TipMock.Util.AvroHelper;
import com.Agilysys.TipMock.Util.MultiThreadConsumer;
import org.apache.avro.Schema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ConsumerThread {
    @Scheduled(fixedDelay = 1000)
    public void consumer() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("night_audit");
        arrayList.add("day_audit");
        Properties props = KafkaProperties.kafkaConsumerWithAvro();
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);

        arrayList.stream().forEach(x -> executorService.submit(new MultiThreadConsumer(x,consumer)));
    }
}

