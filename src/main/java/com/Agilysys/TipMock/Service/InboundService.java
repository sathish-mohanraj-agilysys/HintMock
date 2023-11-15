package com.Agilysys.TipMock.Service;

import com.Agilysys.TipMock.Modal.KafkaResponseDTO;
import com.Agilysys.TipMock.Properties.ApplicationProperties;
import com.Agilysys.TipMock.Util.AvroHelper;
import com.Agilysys.TipMock.Util.SchemaHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class InboundService {
    @Autowired
    Producer<String, byte[]> producer;
    private String topicName;
    private String jsonBody;
    private String kafkaHeader;
    private AvroHelper avroHelper = new AvroHelper();

    private  Future<RecordMetadata> recordMetaData;
    private Exception serializationException = null;

    public ResponseEntity<Object> produce(String payload) throws IOException {
        //parsing the input payload
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
        topicName = jsonObject.get("topicName").getAsString();
        jsonBody = jsonObject.get("jsonBody").getAsJsonObject().toString();
        kafkaHeader = jsonObject.get("kafkaHeader").getAsJsonObject().toString();
        //generating the schema
        Schema avroSchema = new SchemaHelper().getInboundSchema(topicName);
        //conversion of the avro data
        InputStream inputStream = new ByteArrayInputStream(jsonBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        avroHelper.convertJsonToAvro(inputStream, outputStream, avroSchema);
        byte[] avro = outputStream.toByteArray();
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topicName, avro);
        JSONObject jsonObject1 = new JSONObject(kafkaHeader);
        for (String key : jsonObject1.keySet()) {
            Object value = jsonObject1.get(key);

            if (value instanceof String) {
                record.headers().add(key, ((String) value).getBytes());
            } else if (value instanceof Integer) {
                record.headers().add(key, Integer.toString((int) value).getBytes());
            } else if (value instanceof Float) {
                record.headers().add(key, Float.toString((float) value).getBytes());
            }
        }
        Future<RecordMetadata> recordMetaData = producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to " + metadata.topic() + " at partition " + metadata.partition() + " at Offset= " + metadata.offset());


            } else {
                serializationException = exception;
                exception.printStackTrace();
            }
        });
        if (serializationException == null) {
            KafkaResponseDTO kafkaResponseDTO=new KafkaResponseDTO();
            try {
                kafkaResponseDTO.setOffset(String.valueOf(recordMetaData.get().offset()));
                kafkaResponseDTO.setPartion(String.valueOf(recordMetaData.get().partition()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

            kafkaResponseDTO.setTopicName(topicName);
            kafkaResponseDTO.setMessage("Json successfully converted into Avro and posted in the Topic");
            kafkaResponseDTO.setKafkaServer(ApplicationProperties.getProperties().getProperty("bootstrapServer").toString());

            ResponseEntity<Object>kafkaResponseDTOResponseEntity=new ResponseEntity<>(kafkaResponseDTO, HttpStatus.OK);
            return kafkaResponseDTOResponseEntity;
        } else {
            return new ResponseEntity<Object>("Excepiton caused ---->"+serializationException.getMessage(),HttpStatus.CONFLICT);
        }
    }

}

