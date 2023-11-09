package com.Agilysys.TipMock;

import com.Agilysys.TipMock.KafKaProperties.KafkaProperties;
import com.Agilysys.TipMock.Util.AvroHelper;
import com.Agilysys.TipMock.Util.FriendlyConverterUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.*;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class TipMockApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(TipMockApplication.class, args);


	}
}
