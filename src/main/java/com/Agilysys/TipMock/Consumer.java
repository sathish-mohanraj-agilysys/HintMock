package com.Agilysys.TipMock;

import com.Agilysys.TipMock.KafKaProperties.KafkaProperties;
import com.Agilysys.TipMock.Util.AvroHelper;
import com.Agilysys.TipMock.Util.FriendlyConverterUtil;
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
    public static void start() throws IOException {
        Properties props = KafkaProperties.kafkaConsumerWithAvro();
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("night_audit"));
        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, byte[]> record : records) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ByteArrayInputStream inputStream=new ByteArrayInputStream(record.value());
                AvroHelper avroHelper=new AvroHelper();
                Schema avroSchema = new Schema.Parser().parse(new File("C:\\kafkaCli\\avro-cli\\src\\main\\java\\io\\github\\rkluszczynski\\avro\\cli\\command\\conversion\\schema.avsc"));

                avroHelper.convertAvroToJson(inputStream,outputStream,avroSchema);
                System.out.printf("Offset = %d, Key = %s, Value = %s%n", record.offset(), record.key(),outputStream.toString());

            }

        }
    }
}
