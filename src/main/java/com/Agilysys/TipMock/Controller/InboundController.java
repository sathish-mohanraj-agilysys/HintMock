package com.Agilysys.TipMock.Controller;

import com.Agilysys.TipMock.Service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class InboundController {
    @Autowired
    InboundService inboundService;

    @PostMapping("/producer")
    public String inboundProducer(@RequestBody String payload) throws IOException {
        return inboundService.produce(payload);

    }
}
