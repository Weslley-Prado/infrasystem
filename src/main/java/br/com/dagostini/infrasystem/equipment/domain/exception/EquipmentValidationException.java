package br.com.dagostini.infrasystem.equipment.domain.exception;

public class EquipmentValidationException extends RuntimeException {
    public EquipmentValidationException(String message) {
        super(message);
    }
}
