package com.Agilysys.TipMock.ControllerAdvice;

import com.Agilysys.TipMock.Modal.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleException(Exception e) {
        ErrorObject errorObject=new ErrorObject();
        errorObject.setException(e.getCause().toString());
        errorObject.setMessage(e.getMessage());
        errorObject.setCause(Arrays.toString(e.getStackTrace()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorObject);
    }



}
