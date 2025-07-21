package br.com.dagostini.infrasystem.shared.exception;

import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentInactiveException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentValidationException;
import br.com.dagostini.infrasystem.violation.domain.exception.ViolationValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(EquipmentNotFoundException.class)
  public ResponseEntity<String> handleNotFound(EquipmentNotFoundException ex) {
    return ResponseEntity.status(404).body(ex.getMessage());
  }

  @ExceptionHandler(EquipmentValidationException.class)
  public ResponseEntity<String> handleValidation(EquipmentValidationException ex) {
    return ResponseEntity.status(422).body(ex.getMessage());
  }

  @ExceptionHandler(EquipmentInactiveException.class)
  public ResponseEntity<String> handleEquipmentInactive(EquipmentInactiveException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneric(Exception ex, WebRequest request) {
    return ResponseEntity.status(500).body("Internal server error");
  }

  @ExceptionHandler(ViolationValidationException.class)
  public ResponseEntity<String> handleViolationValidation(ViolationValidationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
