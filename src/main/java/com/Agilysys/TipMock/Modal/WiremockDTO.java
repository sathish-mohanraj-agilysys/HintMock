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
}
