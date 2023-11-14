package com.Agilysys.TipMock;


import com.Agilysys.TipMock.Modal.WiremockDTO;
import com.Agilysys.TipMock.Properties.ApplicationProperties;
import com.Agilysys.TipMock.Properties.KafkaProperties;
import com.Agilysys.TipMock.Util.AvroHelper;
import com.Agilysys.TipMock.Util.ProducerUtil;
import com.Agilysys.TipMock.Util.SchemaHelper;
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
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {
    public static void startAvro() throws IOException {
        Properties appProp = ApplicationProperties.getProperties();
        Properties kafkaProp = KafkaProperties.kafkaProducerWithAvro();
        // Create a Kafka producer
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(kafkaProp);
        ProducerUtil producerUtil = new ProducerUtil();
        String topicName = "hotel-ops.property-financials.pms-agilysys.night-audit-events";
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
                    Schema avroSchema = new SchemaHelper().getInboundSchema(topicName);
                    avroHelper.convertAvroToJson(inputStream, outputStream, avroSchema);
                    System.out.printf("Offset = %d, Key = %s, Value = %s%n", record.offset(), record.key(), outputStream.toString());
                    HttpClient httpClient = HttpClients.createDefault();
                    HttpPost request = new HttpPost(appProp.getProperty("wiremockUrl") + "/" + topicName);

                    WiremockDTO wiremockOut = new WiremockDTO();
                    wiremockOut.setKafkaHeader(new JSONObject(record.headers().toString()));
                    wiremockOut.setPayload(new JSONObject(outputStream.toString()));

                    System.out.println(new Gson().toJson(wiremockOut));
                    StringEntity entity = new StringEntity(new Gson().toJson(wiremockOut));

                    entity.setContentType("application/json");
                    request.setEntity(entity);
                    HttpResponse response = httpClient.execute(request);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Message from the wireMock" + responseBody);
                    WiremockDTO wiremockDTO = new ObjectMapper().readValue(responseBody, WiremockDTO.class);
                    if (wiremockDTO.getTopic() != null) producerUtil.produceAvro(responseBody);
                }
            } catch (Exception e) {
                System.out.println("Exception in the consumption of the message"+e.getMessage());

            }

        }
    }
    public static void startJson() throws IOException {
        Properties appProp = ApplicationProperties.getProperties();
        Properties kafkaProp = KafkaProperties.kafkaProducerWithJSON();
        // Create a Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProp);
        ProducerUtil producerUtil = new ProducerUtil();
        String topicName = "hotel-ops.property-financials.pms-agilysys.night-audit-events";
        Properties props = KafkaProperties.kafkaConsumerWithJSON();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));

        while (true) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    HttpClient httpClient = HttpClients.createDefault();
                    HttpPost request = new HttpPost(appProp.getProperty("wiremockUrl") + "/" + topicName);
                    WiremockDTO wiremockOut = new WiremockDTO();
                    wiremockOut.setKafkaHeader(new JSONObject(record.headers().toString()));
                    wiremockOut.setPayload(new JSONObject(record.value()));
                    System.out.println(new Gson().toJson(wiremockOut));
                    StringEntity entity = new StringEntity(new Gson().toJson(wiremockOut));

                    entity.setContentType("application/json");
                    request.setEntity(entity);
                    HttpResponse response = httpClient.execute(request);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Message from the wireMock" + responseBody);
                    WiremockDTO wiremockDTO = new ObjectMapper().readValue(responseBody, WiremockDTO.class);
                    if (wiremockDTO.getTopic() != null) producerUtil.produceAvro(responseBody);
                }
            } catch (Exception e) {
                System.out.println("Exception in the consumption of the message"+e.getMessage());

            }

        }
    }


}

