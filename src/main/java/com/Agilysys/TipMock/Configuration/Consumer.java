package com.Agilysys.TipMock.Configuration;

import com.Agilysys.TipMock.Properties.KafkaProperties;
import com.Agilysys.TipMock.Util.AvroHelper;
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
public class Consumer {

    public void consumer() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        Properties props = KafkaProperties.kafkaConsumerWithAvro();
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("night_audit"));
        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, byte[]> record : records) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(record.value());
                AvroHelper avroHelper = new AvroHelper();
                Schema avroSchema = new Schema.Parser().parse(new File("C:\\kafkaCli\\avro-cli\\src\\main\\java\\io\\github\\rkluszczynski\\avro\\cli\\command\\conversion\\night_audit.avsc"));

                avroHelper.convertAvroToJson(inputStream, outputStream, avroSchema);
                System.out.printf("Offset = %d, Key = %s, Value = %s%n", record.offset(), record.key(), outputStream.toString());

            }

        }
    }

}

