package com.Agilysys.TipMock.Util;

import com.Agilysys.TipMock.Properties.KafkaProperties;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProducerUtil {
    private AvroHelper avroHelper = new AvroHelper();
    private JsonParser jsonParser = new JsonParser();
    public boolean produceAvro(String payload) throws IOException {
        Properties props = KafkaProperties.kafkaProducerWithAvro();
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
        String topicName = jsonObject.get("topic").getAsString();
        String jsonBody = jsonObject.get("payload").getAsJsonObject().toString();
        String kafkaHeader = null;
        try {
            kafkaHeader = jsonObject.get("kafkaHeader").getAsJsonObject().toString();
        } catch (Exception e) {
        }

        Schema avroSchema;
        try {
            avroSchema = new SchemaHelper().getInboundSchema(topicName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //conversion of the avro data
        InputStream inputStream = new ByteArrayInputStream(jsonBody.toString().getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        avroHelper.convertJsonToAvro(inputStream, outputStream, avroSchema);
        byte[] avro = outputStream.toByteArray();
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topicName, avro);

        if (kafkaHeader != null) {
            JsonParser jsonParser1 = new JsonParser();
            JsonObject data = jsonParser1.parse(kafkaHeader).getAsJsonObject();
            for (String key : data.getAsJsonObject().keySet()) {
                record.headers().add(key, data.get(key).toString().getBytes());
            }
        }
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to " + metadata.topic() + " at partition " + metadata.partition() + " at Offset= " + metadata.offset());

            } else {
                exception.printStackTrace();
            }
        });

        return true;
    }
    public boolean produceJSON(String payload) throws IOException {
        Properties props = KafkaProperties.kafkaProducerWithJSON();
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
        String topicName = jsonObject.get("topic").getAsString();
        String jsonBody = jsonObject.get("payload").getAsJsonObject().toString();
        String kafkaHeader = null;
        try {
            kafkaHeader = jsonObject.get("kafkaHeader").getAsJsonObject().toString();
        } catch (Exception e) {
        }
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, jsonBody);

        if (kafkaHeader != null) {
            JsonParser jsonParser1 = new JsonParser();
            JsonObject data = jsonParser1.parse(kafkaHeader).getAsJsonObject();
            for (String key : data.getAsJsonObject().keySet()) {
                record.headers().add(key, data.get(key).toString().getBytes());
            }
        }
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to " + metadata.topic() + " at partition " + metadata.partition() + " at Offset= " + metadata.offset());

            } else {
                exception.printStackTrace();
            }
        });

        return true;
    }
}
