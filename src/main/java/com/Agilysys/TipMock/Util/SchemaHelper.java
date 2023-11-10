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
        String path;
        File file;
        path = System.getProperty("user.dir") + "\\" + SchemaPath + "\\" + name + ".avsc";
        file = new File(Paths.get(path).toUri());
        if (file.exists() && file.isFile()) return file;
        file = new File(Paths.get(System.getProperty("user.dir") + "\\TipMock\\src\\main\\resources\\" + SchemaPath + "\\" + name + ".avsc").toUri());
        return file;

    }
}
