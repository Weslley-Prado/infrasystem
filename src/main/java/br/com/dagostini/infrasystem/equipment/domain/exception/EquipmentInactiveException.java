package br.com.dagostini.infrasystem.equipment.domain.exception;

public class EquipmentInactiveException extends RuntimeException {
  public EquipmentInactiveException(String serial) {
    super("Equipment with serial '" + serial + "' is inactive.");
  }
}
