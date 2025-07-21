package br.com.dagostini.infrasystem.violation.domain.exception;

public class ViolationValidationException extends RuntimeException {
    public ViolationValidationException(String message) {
        super(message);
    }
}