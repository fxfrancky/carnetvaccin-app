package com.carnetvaccin.app.backend.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class CarnetException extends Exception{

    public CarnetException(String message, Throwable cause) {
        super(message, cause);
    }
}
