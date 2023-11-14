package com.Agilysys.TipMock.Configuration;

import com.Agilysys.TipMock.Properties.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ProducerConfiguration {
    @Bean
    public KafkaProducer<String ,byte[]> kafkaProducer(){
        Properties props = KafkaProperties.kafkaProducerWithAvro();
        // Create a Kafka producer
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);
        return producer;
    }
}


