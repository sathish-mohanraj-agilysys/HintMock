package com.Agilysys.TipMock.Util;

import com.Agilysys.TipMock.KafKaProperties.KafkaProperties;
import org.apache.avro.Schema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MultiThreadConsumer implements Runnable {
    private String topic;
    private KafkaConsumer<String, byte[]> consumer;

    public MultiThreadConsumer(String topic) {
        this.topic = topic;
        Properties props = KafkaProperties.kafkaConsumerWithAvro();
        consumer = new KafkaConsumer<>(props);

    }

    public MultiThreadConsumer(String topic, KafkaConsumer consumer) {
        this.topic = topic;
        this.consumer = consumer;

    }


    @Override
    public void run() {
        consumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, byte[]> record : records) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(record.value());
                AvroHelper avroHelper = new AvroHelper();
                Schema avroSchema = null;
                try {
                    avroSchema = new Schema.Parser().parse(new File("C:\\kafkaCli\\avro-cli\\src\\main\\java\\io\\github\\rkluszczynski\\avro\\cli\\command\\conversion\\schema.avsc"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    avroHelper.convertAvroToJson(inputStream, outputStream, avroSchema);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("Offset = %d, Key = %s, Value = %s%n", record.offset(), record.key(), outputStream.toString());

            }

        }
    }

}

