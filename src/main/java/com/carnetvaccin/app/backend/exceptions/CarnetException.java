package com.carnetvaccin.app.backend.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class CarnetException extends RuntimeException{

    public CarnetException(String message) {
        super(message);
    }
}
