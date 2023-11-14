package com.Agilysys.TipMock.Modal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class WiremockDAO {
    private Object payload;
    private String kafkaHeader;
    private String schema;
    private String topic;
}

