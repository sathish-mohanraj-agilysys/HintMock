package com.Agilysys.TipMock.Util;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.compress.utils.IOUtils;
import tech.allegro.schema.json2avro.converter.AvroConversionException;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;
import tech.allegro.schema.json2avro.converter.JsonGenericRecordReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AvroHelper {
     JsonAvroConverter converter = new JsonAvroConverter();

     public void convertAvroToJson(InputStream inputStream, OutputStream outputStream, Schema schema)
            throws IOException {
        outputStream.write(
                converter.convertToJson(
                        IOUtils.toByteArray(inputStream),
                        schema
                )
        );
        outputStream.flush();
    }

    public OutputStream convertJsonToAvro(InputStream inputStream, OutputStream outputStream, Schema avroSchema)
            throws IOException {
        try {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            GenericDatumWriter<Object> writer = new GenericDatumWriter<>(avroSchema);
            JsonGenericRecordReader jsonGenericRecordReader = new JsonGenericRecordReader();
            byte[] data = IOUtils.toByteArray(inputStream);
            writer.write(jsonGenericRecordReader.read(data,avroSchema), encoder);
            encoder.flush();

        } catch (IOException e) {
            throw new AvroConversionException("Failed to convert to AVRO.", e);
        }

        outputStream.flush();
        return outputStream;
     }

   public AvroHelper() {
    }
}

