package com.Agilysys.TipMock.Modal;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ErrorObject {
    private String message;
    private String cause;
    private String exception;
}
