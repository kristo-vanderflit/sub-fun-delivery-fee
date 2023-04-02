package com.fujitsu.deliveryfee.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class VehicleTypeForbiddenException extends RuntimeException {

    public VehicleTypeForbiddenException(String message) {
        super(message);
    }
}
