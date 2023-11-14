package com.Agilysys.TipMock.Util;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import tech.allegro.schema.json2avro.converter.AvroConversionException;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;
import tech.allegro.schema.json2avro.converter.JsonGenericRecordReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class producer {
    public static void main(String[] args) throws IOException {
        // Configure Kafka producer properties
        System.out.println("hello world");
        Properties props = new Properties();
        props.load(new FileReader(new File("C:\\Users\\sathi\\Downloads\\KafkaTransformer\\src\\main\\java\\KAFKA.properties")));

        // Create a Kafka producer
        Producer<String, byte[]> producer = new KafkaProducer<>(props);

        // Define the Kafka topic
        String topic = "night_audit";
        JsonAvroConverter jsonAvroConverter=new JsonAvroConverter();
        Schema avroSchema = new Schema.Parser().parse(new File("C:\\kafkaCli\\avro-cli\\src\\main\\java\\io\\github\\rkluszczynski\\avro\\cli\\command\\conversion\\night_audit.avsc"));
        InputStream inputStream=new FileInputStream("C:\\kafkaCli\\avro-cli\\src\\main\\java\\io\\github\\rkluszczynski\\avro\\cli\\command\\conversion\\input.json");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] avro;
        try {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            GenericDatumWriter<Object> writer = new GenericDatumWriter<>(avroSchema);
            JsonGenericRecordReader jsonGenericRecordReader = new JsonGenericRecordReader();
            byte[] data = IOUtils.toByteArray(inputStream);
            writer.write(jsonGenericRecordReader.read(data,avroSchema), encoder);
            encoder.flush();
            avro= outputStream.toByteArray();
        } catch (IOException e) {
            throw new AvroConversionException("Failed to convert to AVRO.", e);
        }

        outputStream.flush();
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, avro);

        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to " + metadata.topic() + " at partition " + metadata.partition());

            } else {
                exception.printStackTrace();
            }
        });

        // Close the Kafka producer
        producer.close();
    }
}
