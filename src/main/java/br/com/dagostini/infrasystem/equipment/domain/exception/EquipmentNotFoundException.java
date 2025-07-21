package br.com.dagostini.infrasystem.equipment.domain.exception;

public class EquipmentNotFoundException extends RuntimeException{
    public EquipmentNotFoundException(String serial) {
        super("Equipment with serial " + serial + " not found.");
    }
}
