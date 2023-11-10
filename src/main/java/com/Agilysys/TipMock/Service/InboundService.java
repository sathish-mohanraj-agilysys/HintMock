package com.Agilysys.TipMock.Service;

import com.Agilysys.TipMock.Util.AvroHelper;
import com.Agilysys.TipMock.Util.SchemaHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class InboundService {
    @Autowired
    Producer<String, byte[]> producer;
    private String topicName;
    private String jsonBody;
    private AvroHelper avroHelper=new AvroHelper();
    public String produce(String payload) throws IOException {
        //parsing the input payload
        JsonParser jsonParser=new JsonParser();
        JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
        topicName=jsonObject.get("topicName").getAsString();
        jsonBody=jsonObject.get("payload").getAsJsonObject().toString();
        //generating the schema
       Schema avroSchema=new SchemaHelper().getInboundSchema(topicName);
       //conversion of the avro data
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
