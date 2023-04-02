package com.fujitsu.deliveryfee.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongVehicleTypeException extends RuntimeException {

    public WrongVehicleTypeException(String message) {
        super(message);
    }
}
