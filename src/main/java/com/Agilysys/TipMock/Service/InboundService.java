package com.Agilysys.TipMock.Service;

import com.Agilysys.TipMock.KafKaProperties.KafkaProperties;
import com.Agilysys.TipMock.Util.AvroHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.allegro.schema.json2avro.converter.AvroConversionException;
import tech.allegro.schema.json2avro.converter.JsonGenericRecordReader;

import java.io.*;
import java.util.Properties;

@Service
public class InboundService {
    @Autowired
    Producer<String, byte[]> producer;
    private String topicName;
    private String jsonBody;
    private AvroHelper avroHelper=new AvroHelper();
    public String produce(String payload) throws IOException {
        JsonParser jsonParser=new JsonParser();
        JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
        topicName=jsonObject.get("topicName").getAsString();
        jsonBody=jsonObject.get("payload").getAsJsonObject().toString();
//        Properties props = KafkaProperties.kafkaProducerWithAvro();
//        // Create a Kafka producer
//        Producer<String, byte[]> producer = new KafkaProducer<>(props);

        // Define the Kafka topic
        Schema avroSchema = new Schema.Parser().parse(new File("C:\\kafkaCli\\avro-cli\\src\\main\\java\\io\\github\\rkluszczynski\\avro\\cli\\command\\conversion\\schema.avsc"));
        InputStream inputStream=new ByteArrayInputStream(jsonBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        avroHelper.convertJsonToAvro(inputStream,outputStream,avroSchema);
        byte[] avro=outputStream.toByteArray();


        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topicName, avro);

        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to " + metadata.topic() + " at partition " + metadata.partition()+" at Offset= " +metadata.offset());

            } else {
                exception.printStackTrace();
            }
        });

        return "Message sent to kafka topic";
    }

}
