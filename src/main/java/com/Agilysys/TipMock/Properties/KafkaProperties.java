package com.Agilysys.TipMock.Properties;

import java.util.Properties;

public final class KafkaProperties {
    public static Properties kafkaProducerWithAvro(){
        Properties prop=ApplicationProperties.getProperties();
        Properties properties=new Properties();
        properties.setProperty("sasl.jaas.config",prop.getProperty("sasl.jaas.config"));
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("basic.auth.credentials.source","USER_INFO");
        properties.setProperty("bootstrap.servers",prop.getProperty("bootstrapServer"));
        properties.setProperty("ssl.endpoint.identification.algorithm","https");
        return properties;
    }
    public static Properties kafkaConsumerWithAvro(){
        Properties prop=ApplicationProperties.getProperties();
        Properties properties=new Properties();
        properties.setProperty("group.id","consumer11");
        properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.ByteArrayDeserializer");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("basic.auth.credentials.source","USER_INFO");
        properties.setProperty("sasl.jaas.config",prop.getProperty("sasl.jaas.config"));
        properties.setProperty("bootstrap.servers",prop.getProperty("bootstrapServer"));
        properties.setProperty("ssl.endpoint.identification.algorithm","https");
        return properties;
    }
    public static Properties kafkaProducerWithJSON(){
        Properties prop=ApplicationProperties.getProperties();
        Properties properties=new Properties();
        properties.setProperty("sasl.jaas.config",prop.getProperty("sasl.jaas.config"));
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("basic.auth.credentials.source","USER_INFO");
        properties.setProperty("bootstrap.servers",prop.getProperty("bootstrapServer"));
        properties.setProperty("ssl.endpoint.identification.algorithm","https");
        return properties;
    }
    public static Properties kafkaConsumerWithJSON(){
        Properties prop=ApplicationProperties.getProperties();
        Properties properties=new Properties();
        properties.setProperty("group.id","consumer1");
        properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("basic.auth.credentials.source","USER_INFO");
        properties.setProperty("sasl.jaas.config",prop.getProperty("sasl.jaas.config"));
        properties.setProperty("bootstrap.servers",prop.getProperty("bootstrapServer"));
        properties.setProperty("ssl.endpoint.identification.algorithm","https");
        return properties;
    }
}
