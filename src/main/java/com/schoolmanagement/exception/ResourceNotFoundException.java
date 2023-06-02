package com.schoolmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //status kodunu görmek istiyorsak
public class ResourceNotFoundException extends  RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
