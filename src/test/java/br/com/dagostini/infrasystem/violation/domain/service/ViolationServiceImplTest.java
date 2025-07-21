package br.com.dagostini.infrasystem.violation.domain.service;

import br.com.dagostini.infrasystem.equipment.application.service.EquipmentServiceAdapter;
import br.com.dagostini.infrasystem.violation.application.service.ViolationServiceAdapter;
import br.com.dagostini.infrasystem.violation.domain.exception.ViolationValidationException;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViolationServiceImplTest {

    @Mock
    private ViolationServiceAdapter violationServiceAdapter;

    @Mock
    private EquipmentServiceAdapter equipmentServiceAdapter;

    @InjectMocks
    private ViolationServiceImpl violationService;

    private Violation testViolation;
    private static final Long TEST_ID = 1L;
    private static final String TEST_SERIAL = "ABC12345";
    private static final OffsetDateTime TEST_DATE = OffsetDateTime.of(2021, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final Date TEST_DATE_FROM = new Date(1626748800000L); // 2021-07-20
    private static final Date TEST_DATE_TO = new Date(1626835200000L);   // 2021-07-21

    @BeforeEach
    void setUp() {
        testViolation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_SERIAL)
                .occurrenceDateUtc(TEST_DATE)
                .build();
    }

    @Test
    void createViolation_shouldCreateViolationWhenEquipmentIsActive() {
        when(equipmentServiceAdapter.isEquipmentActive(TEST_SERIAL)).thenReturn(true);
        when(violationServiceAdapter.saveViolationRegistry(testViolation)).thenReturn(testViolation);
        Violation result = violationService.createViolation(testViolation);

        assertNotNull(result);
        assertEquals(testViolation, result);
        assertEquals(TEST_ID, result.id());
        assertEquals(TEST_SERIAL, result.equipmentSerial());
        assertEquals(TEST_DATE, result.occurrenceDateUtc());
        verify(equipmentServiceAdapter, times(1)).isEquipmentActive(TEST_SERIAL);
        verify(violationServiceAdapter, times(1)).saveViolationRegistry(testViolation);
    }

    @Test
    void createViolation_shouldThrowValidationExceptionWhenEquipmentIsInactive() {
        when(equipmentServiceAdapter.isEquipmentActive(TEST_SERIAL)).thenReturn(false);
        ViolationValidationException thrown = assertThrows(ViolationValidationException.class, () ->
                violationService.createViolation(testViolation));
        assertEquals("Cannot create violation for inactive equipment: " + TEST_SERIAL, thrown.getMessage());
        verify(equipmentServiceAdapter, times(1)).isEquipmentActive(TEST_SERIAL);
        verify(violationServiceAdapter, never()).saveViolationRegistry(any());
    }

    @Test
    void createViolation_shouldThrowExceptionWhenServiceAdapterFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(equipmentServiceAdapter.isEquipmentActive(TEST_SERIAL)).thenReturn(true);
        when(violationServiceAdapter.saveViolationRegistry(testViolation)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationService.createViolation(testViolation));
        assertEquals("Database error", thrown.getMessage());
        verify(equipmentServiceAdapter, times(1)).isEquipmentActive(TEST_SERIAL);
        verify(violationServiceAdapter, times(1)).saveViolationRegistry(testViolation);
    }

    @Test
    void findViolationById_shouldReturnViolationWhenFound() {
        when(violationServiceAdapter.findViolationById(TEST_ID)).thenReturn(testViolation);

        Violation result = violationService.findViolationById(TEST_ID);

        assertNotNull(result);
        assertEquals(testViolation, result);
        assertEquals(TEST_ID, result.id());
        assertEquals(TEST_SERIAL, result.equipmentSerial());
        assertEquals(TEST_DATE, result.occurrenceDateUtc());
        verify(violationServiceAdapter, times(1)).findViolationById(TEST_ID);
    }

    @Test
    void findViolationById_shouldReturnNullWhenNotFound() {
        // Arrange
        when(violationServiceAdapter.findViolationById(TEST_ID)).thenReturn(null);

        Violation result = violationService.findViolationById(TEST_ID);

        assertNull(result);
        verify(violationServiceAdapter, times(1)).findViolationById(TEST_ID);
    }

    @Test
    void findViolationById_shouldThrowExceptionWhenServiceAdapterFails() {
        RuntimeException exception = new RuntimeException("Service error");
        when(violationServiceAdapter.findViolationById(TEST_ID)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationService.findViolationById(TEST_ID));
        assertEquals("Service error", thrown.getMessage());
        verify(violationServiceAdapter, times(1)).findViolationById(TEST_ID);
    }

    @Test
    void listViolationsByEquipment_shouldReturnViolationsList() {
        List<Violation> expectedViolations = Collections.singletonList(testViolation);
        when(violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO))
                .thenReturn(expectedViolations);

        List<Violation> result = violationService.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testViolation, result.get(0));
        assertEquals(TEST_ID, result.get(0).id());
        assertEquals(TEST_SERIAL, result.get(0).equipmentSerial());
        assertEquals(TEST_DATE, result.get(0).occurrenceDateUtc());
        verify(violationServiceAdapter, times(1)).listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);
    }

    @Test
    void listViolationsByEquipment_shouldReturnEmptyListWhenNoViolationsFound() {
        when(violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO))
                .thenReturn(Collections.emptyList());

        List<Violation> result = violationService.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(violationServiceAdapter, times(1)).listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);
    }

    @Test
    void listViolationsByEquipment_shouldHandleNullDates() {
        List<Violation> expectedViolations = Collections.singletonList(testViolation);
        when(violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, null, null))
                .thenReturn(expectedViolations);

        List<Violation> result = violationService.listViolationsByEquipment(TEST_SERIAL, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testViolation, result.get(0));
        verify(violationServiceAdapter, times(1)).listViolationsByEquipment(TEST_SERIAL, null, null);
    }

    @Test
    void listViolationsByEquipment_shouldThrowExceptionWhenServiceAdapterFails() {
        RuntimeException exception = new RuntimeException("Service error");
        when(violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationService.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO));
        assertEquals("Service error", thrown.getMessage());
        verify(violationServiceAdapter, times(1)).listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);
    }
}