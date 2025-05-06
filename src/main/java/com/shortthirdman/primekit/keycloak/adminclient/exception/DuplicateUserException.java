package com.shortthirdman.primekit.keycloak.adminclient.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
