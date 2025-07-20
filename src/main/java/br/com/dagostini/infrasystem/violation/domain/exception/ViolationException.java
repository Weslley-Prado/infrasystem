package br.com.dagostini.infrasystem.violation.domain.exception;

public class ViolationException extends RuntimeException {
    public ViolationException(String message) {
        super(message);
    }
}