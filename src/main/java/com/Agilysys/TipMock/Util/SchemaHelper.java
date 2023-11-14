package com.Agilysys.TipMock.Util;

import org.apache.avro.Schema;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class SchemaHelper {
    public Schema getInboundSchema(String name) throws IOException {

        return new Schema.Parser().parse(fileGetter("InboundSchema", name));

    }

    public Schema getOutboundSchema(String name) throws IOException {
        return new Schema.Parser().parse(fileGetter("OutboundSchema", name));
    }

    private File fileGetter(String SchemaPath, String name) {
            File file;
            file =Paths.get(System.getProperty("user.dir"),"resources",SchemaPath,name+".avsc").toFile();
            if(!file.exists())  file =Paths.get(System.getProperty("user.dir"),"src","main","resources",SchemaPath,name+".avsc").toFile();

        return file;

    }
}
