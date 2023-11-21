package com.Agilysys.TipMock.Util;

import com.Agilysys.TipMock.Modal.WiremockDAO;
import com.Agilysys.TipMock.Modal.WiremockDTO;
import com.Agilysys.TipMock.Properties.ApplicationProperties;
import com.Agilysys.TipMock.Properties.KafkaProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.avro.Schema;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MultiThreadConsumer implements Runnable {
    private String topicName;
    private KafkaConsumer<String, byte[]> consumer;
    private Boolean avroSerialization;

    public MultiThreadConsumer(String topic) {
        this.topicName = topic;
        Properties props = KafkaProperties.kafkaConsumerWithAvro();
        consumer = new KafkaConsumer<>(props);

    }

    public MultiThreadConsumer(String topic, KafkaConsumer consumer, boolean avroSerialization) {
        this.topicName = topic;
        this.consumer = consumer;
        this.avroSerialization = avroSerialization;

    }


    @Override
    public void run() {
        consumer.subscribe(Collections.singletonList(topicName));
        if (avroSerialization) {
            startAvro();
        } else {
            startJson();
        }
    }

    private void startAvro() {
        Properties appProp = ApplicationProperties.getProperties();
        Properties kafkaProp = KafkaProperties.kafkaProducerWithAvro();
        // Create a Kafka producer
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(kafkaProp);
        ProducerUtil producerUtil = new ProducerUtil();
        Properties props = KafkaProperties.kafkaConsumerWithAvro();
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));

        while (true) {
            try {
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, byte[]> record : records) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(record.value());
                    AvroHelper avroHelper = new AvroHelper();
                    Schema avroSchema = new SchemaHelper().getOutboundSchema(topicName);
                    avroHelper.convertAvroToJson(inputStream, outputStream, avroSchema);
                    System.out.printf("Offset = %d, Key = %s, Value = %s%n", record.offset(), record.key(), outputStream.toString());
                    HttpClient httpClient = HttpClients.createDefault();
                    HttpPost request = new HttpPost(appProp.getProperty("wiremockUrl") + "/" + topicName);

                    WiremockDTO wiremockOut = new WiremockDTO();
                    wiremockOut.setKafkaHeader(new JSONObject(KafkaHeader.tojson(record.headers())));
                    wiremockOut.setPayload(new JSONObject(outputStream.toString()));

                    System.out.println(wiremockOut.toJsonString());
                    StringEntity entity = new StringEntity(wiremockOut.toJsonString());

                    entity.setContentType("application/json");
                    request.setEntity(entity);
                    HttpResponse response = httpClient.execute(request);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Message from the wireMock" + responseBody);
                    WiremockDAO wiremockDAO = new ObjectMapper().readValue(responseBody, WiremockDAO.class);
                   if(Boolean.valueOf(appProp.get("AvroSerialialzation").toString())) {
                       if (wiremockDAO.getTopic() != null) producerUtil.produceAvro(wiremockDAO);
                    }
                   else {
                       if (wiremockDAO.getTopic() != null) producerUtil.produceJSON(responseBody);
                   }
                }
            } catch (Exception e) {
                System.out.println("Exception in the consumption of the message" + e.getMessage());

            }

        }
    }

    private void startJson() {
        Properties appProp = ApplicationProperties.getProperties();
        Properties kafkaProp = KafkaProperties.kafkaProducerWithJSON();
        // Create a Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProp);
        ProducerUtil producerUtil = new ProducerUtil();
        String topicName = "hotel-ops.property-financials.pms-agilysys.night-audit-events";
        Properties props = KafkaProperties.kafkaConsumerWithJSON();
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));

        while (true) {
            try {
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, byte[]> record : records) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(record.value());
                    AvroHelper avroHelper = new AvroHelper();
                    Schema avroSchema = new SchemaHelper().getInboundSchema(topicName);
                    avroHelper.convertAvroToJson(inputStream, outputStream, avroSchema);
                    System.out.printf("Offset = %d, Key = %s, Value = %s%n", record.offset(), record.key(), outputStream.toString());
                    HttpClient httpClient = HttpClients.createDefault();
                    HttpPost request = new HttpPost(appProp.getProperty("wiremockUrl") + "/" + topicName);

                    WiremockDTO wiremockOut = new WiremockDTO();
                    wiremockOut.setKafkaHeader(new JSONObject(record.headers().toString()));
                    wiremockOut.setPayload(new JSONObject(outputStream.toString()));

                    System.out.println(wiremockOut.toJsonString());
                    StringEntity entity = new StringEntity(wiremockOut.toJsonString());

                    entity.setContentType("application/json");
                    request.setEntity(entity);
                    HttpResponse response = httpClient.execute(request);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Message from the wireMock" + responseBody);
                    WiremockDTO wiremockDTO = new ObjectMapper().readValue(responseBody, WiremockDTO.class);
                    if (wiremockDTO.getTopic() != null) producerUtil.produceAvro(responseBody);
                }
            } catch (Exception e) {
                System.out.println("Exception in the consumption of the message" + e.getMessage());

            }

        }

    }
}

