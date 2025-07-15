package com.wildan.e_commerce.exceptions;

public class APIException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public APIException(){

    }

    public APIException(String message) {
        super(message);
    }
}
