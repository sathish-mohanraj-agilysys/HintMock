package com.Agilysys.TipMock.Modal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

@Data
@Getter
@Setter
@ToString
public class WiremockDTO {
    JSONObject payload;
    JSONObject kafkaHeader;
    String schema;
    String topic;
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("payload", payload);
        json.put("kafkaHeader", kafkaHeader);
        json.put("schema", schema);
        json.put("topic", topic);
        return json.toString();
    }
}
