package com.Agilysys.TipMock.Configuration;

import com.Agilysys.TipMock.Properties.ApplicationProperties;
import com.Agilysys.TipMock.Properties.KafkaProperties;
import com.Agilysys.TipMock.Util.MultiThreadConsumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConsumerThread {

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

