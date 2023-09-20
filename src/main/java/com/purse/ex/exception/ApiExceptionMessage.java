package com.purse.ex.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Setter
@Getter
public class ApiExceptionMessage {
    private String cause;
    private HttpStatus httpStatus;
    private Date timeStamp;
    public ApiExceptionMessage(String cause, HttpStatus httpStatus, Date timeStamp){
        this.cause = cause != null ? cause :"";
        this.httpStatus = httpStatus;
        this.timeStamp = timeStamp;
    }
}
