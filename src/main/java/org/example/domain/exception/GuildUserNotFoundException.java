package org.example.domain.exception;

public class GuildUserNotFoundException extends RuntimeException {

    public GuildUserNotFoundException(String message) {
        super(message);
    }
}
