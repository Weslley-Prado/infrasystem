package br.com.dagostini.infrasystem.violation.interfaces.controller;

import br.com.agostini.openapi.provider.representation.ViolationRequestRepresentation;
import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentInactiveException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.shared.utils.ImageStorageUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.CreateViolationUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.FindViolationByIdUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.ListViolationsByEquipmentUseCase;
import br.com.dagostini.infrasystem.violation.domain.exception.ViolationValidationException;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.interfaces.mapper.ViolationDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViolationControllerTest {

    @Mock
    private ImageStorageUseCase imageStorageUseCase;

    @Mock
    private ViolationDtoMapper violationDtoMapper;

    @Mock
    private CreateViolationUseCase createViolationUseCase;

    @Mock
    private FindViolationByIdUseCase findViolationByIdUseCase;

    @Mock
    private ListViolationsByEquipmentUseCase listViolationsByEquipmentUseCase;

    @InjectMocks
    private ViolationController violationController;

    private Violation testViolation;
    private ViolationRequestRepresentation testRequestRepresentation;
    private ViolationResponseRepresentation testResponseRepresentation;
    private MultipartFile testPicture;
    private static final Long TEST_ID = 1L;
    private static final String TEST_SERIAL = "ABC12345";
    private static final OffsetDateTime TEST_DATE = OffsetDateTime.of(2021, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final String TEST_PICTURE_URL = "s3://bucket/violation.jpg";
    private static final BigDecimal TEST_SPEED = new BigDecimal("80.0");

    @BeforeEach
    void setUp() {
        testViolation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_SERIAL)
                .occurrenceDateUtc(TEST_DATE)
                .build();

        testRequestRepresentation = new ViolationRequestRepresentation();
        testRequestRepresentation.setType(ViolationRequestRepresentation.TypeEnum.VELOCITY);
        testRequestRepresentation.setEquipmentSerial(TEST_SERIAL);
        testRequestRepresentation.setMeasuredSpeed(TEST_SPEED);
        testRequestRepresentation.setConsideredSpeed(TEST_SPEED);
        testRequestRepresentation.setRegulatedSpeed(TEST_SPEED);
        testRequestRepresentation.setPicture(TEST_PICTURE_URL);

        testResponseRepresentation = new ViolationResponseRepresentation();
        testResponseRepresentation.setEquipmentSerial(TEST_SERIAL);

        testPicture = new MockMultipartFile("picture", "violation.jpg", "image/jpeg", "test image".getBytes());
    }

    @Test
    void createViolation_shouldCreateViolationAndReturn201() throws Exception {
        // Arrange
        when(imageStorageUseCase.storeImage(testPicture)).thenReturn(TEST_PICTURE_URL);
        when(violationDtoMapper.toDomain(testRequestRepresentation)).thenReturn(testViolation);
        when(createViolationUseCase.execute(testViolation)).thenReturn(testViolation);
        when(violationDtoMapper.toResponse(testViolation)).thenReturn(testResponseRepresentation);

        // Act
        ResponseEntity<ViolationResponseRepresentation> response = violationController.createViolation(testRequestRepresentation, testPicture);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testResponseRepresentation, response.getBody());
        assertEquals(URI.create("/violations/" + TEST_ID), response.getHeaders().getLocation());
        verify(imageStorageUseCase, times(1)).storeImage(testPicture);
        verify(violationDtoMapper, times(1)).toDomain(testRequestRepresentation);
        verify(createViolationUseCase, times(1)).execute(testViolation);
        verify(violationDtoMapper, times(1)).toResponse(testViolation);
    }

    @Test
    void createViolation_shouldThrowValidationExceptionForMissingSpeedFields() {
        // Arrange
        testRequestRepresentation.setMeasuredSpeed(null);
        testRequestRepresentation.setConsideredSpeed(null);
        testRequestRepresentation.setRegulatedSpeed(null);

        // Act & Assert
        ViolationValidationException thrown = assertThrows(ViolationValidationException.class, () ->
                violationController.createViolation(testRequestRepresentation, testPicture));
        assertEquals("For VELOCITY type, measuredSpeed, consideredSpeed, and regulatedSpeed are required.", thrown.getMessage());
        verify(imageStorageUseCase, never()).storeImage(any());
        verify(violationDtoMapper, never()).toDomain(any());
        verify(createViolationUseCase, never()).execute(any());
        verify(violationDtoMapper, never()).toResponse(any());
    }

    @Test
    void createViolation_shouldThrowEquipmentNotFoundException() {
        // Arrange
        EquipmentNotFoundException exception = new EquipmentNotFoundException("Equipment not found");
        when(imageStorageUseCase.storeImage(testPicture)).thenReturn(TEST_PICTURE_URL);
        when(violationDtoMapper.toDomain(testRequestRepresentation)).thenReturn(testViolation);
        when(createViolationUseCase.execute(testViolation)).thenThrow(exception);

        // Act & Assert
        EquipmentNotFoundException thrown = assertThrows(EquipmentNotFoundException.class, () ->
                violationController.createViolation(testRequestRepresentation, testPicture));
        assertEquals("Equipment with serial Equipment not found not found.", thrown.getMessage());
        verify(imageStorageUseCase, times(1)).storeImage(testPicture);
        verify(violationDtoMapper, times(1)).toDomain(testRequestRepresentation);
        verify(createViolationUseCase, times(1)).execute(testViolation);
        verify(violationDtoMapper, never()).toResponse(any());
    }

    @Test
    void createViolation_shouldThrowEquipmentInactiveException() {
        // Arrange
        EquipmentInactiveException exception = new EquipmentInactiveException("Equipment is inactive");
        when(imageStorageUseCase.storeImage(testPicture)).thenReturn(TEST_PICTURE_URL);
        when(violationDtoMapper.toDomain(testRequestRepresentation)).thenReturn(testViolation);
        when(createViolationUseCase.execute(testViolation)).thenThrow(exception);

        // Act & Assert
        EquipmentInactiveException thrown = assertThrows(EquipmentInactiveException.class, () ->
                violationController.createViolation(testRequestRepresentation, testPicture));
        assertEquals("Equipment with serial 'Equipment is inactive' is inactive.", thrown.getMessage());
        verify(imageStorageUseCase, times(1)).storeImage(testPicture);
        verify(violationDtoMapper, times(1)).toDomain(testRequestRepresentation);
        verify(createViolationUseCase, times(1)).execute(testViolation);
        verify(violationDtoMapper, never()).toResponse(any());
    }

    @Test
    void createViolation_shouldThrowUnexpectedException() {
        // Arrange
        RuntimeException exception = new RuntimeException("Unexpected error");
        when(imageStorageUseCase.storeImage(testPicture)).thenReturn(TEST_PICTURE_URL);
        when(violationDtoMapper.toDomain(testRequestRepresentation)).thenReturn(testViolation);
        when(createViolationUseCase.execute(testViolation)).thenThrow(exception);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationController.createViolation(testRequestRepresentation, testPicture));
        assertEquals("Unexpected error", thrown.getMessage());
        verify(imageStorageUseCase, times(1)).storeImage(testPicture);
        verify(violationDtoMapper, times(1)).toDomain(testRequestRepresentation);
        verify(createViolationUseCase, times(1)).execute(testViolation);
        verify(violationDtoMapper, never()).toResponse(any());
    }

    @Test
    void findViolationById_shouldReturnViolationWhenFound() {
        // Arrange
        when(findViolationByIdUseCase.findViolationById(TEST_ID)).thenReturn(testViolation);
        when(violationDtoMapper.toResponse(testViolation)).thenReturn(testResponseRepresentation);

        // Act
        ResponseEntity<ViolationResponseRepresentation> response = violationController.findViolationById(TEST_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testResponseRepresentation, response.getBody());
        verify(findViolationByIdUseCase, times(1)).findViolationById(TEST_ID);
        verify(violationDtoMapper, times(1)).toResponse(testViolation);
    }

    @Test
    void findViolationById_shouldThrowExceptionWhenServiceFails() {
        // Arrange
        RuntimeException exception = new RuntimeException("Service error");
        when(findViolationByIdUseCase.findViolationById(TEST_ID)).thenThrow(exception);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationController.findViolationById(TEST_ID));
        assertEquals("Service error", thrown.getMessage());
        verify(findViolationByIdUseCase, times(1)).findViolationById(TEST_ID);
        verify(violationDtoMapper, never()).toResponse(any());
    }

    @Test
    void maskViolationId_shouldMaskCorrectly() throws Exception {
        // Arrange
        Method maskViolationIdMethod = ViolationController.class.getDeclaredMethod("maskViolationId", Long.class);
        maskViolationIdMethod.setAccessible(true);

        // Act
        String result = (String) maskViolationIdMethod.invoke(violationController, 12345678L);

        // Assert
        assertEquals("****5678", result);
    }

    @Test
    void maskViolationId_shouldReturnDefaultMaskForNullId() throws Exception {
        // Arrange
        Method maskViolationIdMethod = ViolationController.class.getDeclaredMethod("maskViolationId", Long.class);
        maskViolationIdMethod.setAccessible(true);

        // Act
        String result = (String) maskViolationIdMethod.invoke(violationController, (Long) null);

        // Assert
        assertEquals("****", result);
    }

    @Test
    void maskViolationId_shouldReturnDefaultMaskForShortId() throws Exception {
        // Arrange
        Method maskViolationIdMethod = ViolationController.class.getDeclaredMethod("maskViolationId", Long.class);
        maskViolationIdMethod.setAccessible(true);

        // Act
        String result = (String) maskViolationIdMethod.invoke(violationController, 123L);

        // Assert
        assertEquals("****", result);
    }
}