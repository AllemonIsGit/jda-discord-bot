package org.example.domain.exception;

public class InsultNotFoundException extends RuntimeException {

    public InsultNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
