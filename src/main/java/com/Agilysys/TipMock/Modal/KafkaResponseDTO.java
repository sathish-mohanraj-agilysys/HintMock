package com.Agilysys.TipMock.Modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaResponseDTO{
    private String message;
    private String topicName;
    private String partion;
    private String kafkaServer;
    private String offset;
}
