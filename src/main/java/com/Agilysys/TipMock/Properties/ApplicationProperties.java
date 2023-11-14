package com.Agilysys.TipMock.Properties;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ApplicationProperties {
    public static Properties getProperties(){
        Properties prop=new Properties();
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "application.properties");
            prop.load(new FileReader(path.toFile()));

        } catch (IOException e) {
            try {
                prop.load(new FileReader(Paths.get(System.getProperty("user.dir"),"src","main","resources","application.properties").toFile()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return prop;
    }
}
