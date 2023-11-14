package com.Agilysys.TipMock.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.util.HashMap;
import java.util.Map;

public class KafkaHeader {
    public static String tojson(Headers headers) {

        Map<String, String> headersMap = new HashMap<>();

        // Iterate over headers
        for (Header header : headers) {
            headersMap.put(header.key(), new String(header.value()));
        }

        // Convert headers map to JSON string
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(headersMap);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Return an empty JSON object in case of an error

        }
    }
}
