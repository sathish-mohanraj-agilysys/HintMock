package com.Agilysys.TipMock.KafKaProperties;

import java.util.Properties;

public final class KafkaProperties {
    public static Properties kafkaProducerWithAvro(){
        Properties properties=new Properties();
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("basic.auth.credentials.source","USER_INFO");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"TT6AVYAXJH7ZXTTD\" password=\"ehVQiRt54cY88NoZFllsr5G55nPBBbmwn7OOB4mqOcWWOVxiQJcYmNFhxlwR6il9\";");
        properties.setProperty("bootstrap.servers","pkc-ldvmy.centralus.azure.confluent.cloud:9092");
        properties.setProperty("ssl.endpoint.identification.algorithm","https");
        return properties;
    }
    public static Properties kafkaConsumerWithAvro(){
        Properties properties=new Properties();
        properties.setProperty("group.id","consumer1");
        properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.ByteArrayDeserializer");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("basic.auth.credentials.source","USER_INFO");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"TT6AVYAXJH7ZXTTD\" password=\"ehVQiRt54cY88NoZFllsr5G55nPBBbmwn7OOB4mqOcWWOVxiQJcYmNFhxlwR6il9\";");
        properties.setProperty("bootstrap.servers","pkc-ldvmy.centralus.azure.confluent.cloud:9092");
        properties.setProperty("ssl.endpoint.identification.algorithm","https");
        return properties;
    }
}
