package com.Agilysys.TipMock.Modal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class WiremockDTOInbound {
    Object payload;
    String kafkaHeader;
    String schema;
    String topic;
}
