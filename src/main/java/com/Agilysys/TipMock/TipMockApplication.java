package com.Agilysys.TipMock;

import com.Agilysys.TipMock.Configuration.ConsumerThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
public class TipMockApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TipMockApplication.class, args);
        ConsumerThread thread=new ConsumerThread();
        thread.startConsume();
    }
}
