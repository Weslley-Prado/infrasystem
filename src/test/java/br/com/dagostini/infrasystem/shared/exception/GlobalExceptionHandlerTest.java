package br.com.dagostini.infrasystem.shared.exception;


import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentInactiveException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentValidationException;
import br.com.dagostini.infrasystem.violation.domain.exception.ViolationValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    private static final String NOT_FOUND_MESSAGE = "Equipment with serial Equipment not found not found.";
    private static final String VALIDATION_MESSAGE = "Invalid equipment data";
    private static final String INACTIVE_MESSAGE = "Equipment with serial 'Equipment is inactive' is inactive.";
    private static final String GENERIC_MESSAGE = "Internal server error";
    private static final String VIOLATION_MESSAGE = "Violation validation failed";

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    void handleNotFound_shouldReturn404WithMessage() {
        EquipmentNotFoundException ex = new EquipmentNotFoundException(NOT_FOUND_MESSAGE);

        ResponseEntity<String> response = exceptionHandler.handleNotFound(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleValidation_shouldReturn422WithMessage() {
        EquipmentValidationException ex = new EquipmentValidationException(VALIDATION_MESSAGE);

        ResponseEntity<String> response = exceptionHandler.handleValidation(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(VALIDATION_MESSAGE, response.getBody());
    }

    @Test
    void handleEquipmentInactive_shouldReturn422WithMessage() {
        EquipmentInactiveException ex = new EquipmentInactiveException(INACTIVE_MESSAGE);

        ResponseEntity<String> response = exceptionHandler.handleEquipmentInactive(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void handleGeneric_shouldReturn500WithGenericMessage() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<String> response = exceptionHandler.handleGeneric(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(GENERIC_MESSAGE, response.getBody());
    }

    @Test
    void handleViolationValidation_shouldReturn400WithMessage() {
        ViolationValidationException ex = new ViolationValidationException(VIOLATION_MESSAGE);

        ResponseEntity<String> response = exceptionHandler.handleViolationValidation(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VIOLATION_MESSAGE, response.getBody());
    }

    @Test
    void handleNotFound_shouldHandleNullMessage() {
        EquipmentNotFoundException ex = new EquipmentNotFoundException(null);

        ResponseEntity<String> response = exceptionHandler.handleNotFound(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleValidation_shouldHandleNullMessage() {
        EquipmentValidationException ex = new EquipmentValidationException(null);

        ResponseEntity<String> response = exceptionHandler.handleValidation(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void handleEquipmentInactive_shouldHandleNullMessage() {
        EquipmentInactiveException ex = new EquipmentInactiveException(null);

        ResponseEntity<String> response = exceptionHandler.handleEquipmentInactive(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void handleViolationValidation_shouldHandleNullMessage() {
        ViolationValidationException ex = new ViolationValidationException(null);

        ResponseEntity<String> response = exceptionHandler.handleViolationValidation(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}