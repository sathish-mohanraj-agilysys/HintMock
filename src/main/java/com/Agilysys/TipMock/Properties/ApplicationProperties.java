package com.Agilysys.TipMock.Properties;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class ApplicationProperties {
    public static Properties getProperties(){
        Properties prop=new Properties();
        try {
            String path=System.getProperty("user.dir")+"\\application.properties";
            prop.load(new FileReader(new File(Paths.get(path).toUri())));

        } catch (IOException e) {
            try {
                prop.load(new FileReader(new File(Paths.get(System.getProperty("user.dir")+"\\TipMock\\src\\main\\resources\\application.properties").toUri())));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return prop;
    }
}
