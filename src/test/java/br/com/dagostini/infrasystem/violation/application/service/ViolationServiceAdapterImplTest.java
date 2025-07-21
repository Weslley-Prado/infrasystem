package br.com.dagostini.infrasystem.violation.application.service;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.repository.ViolationRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViolationServiceAdapterImplTest {

    @Mock
    private ViolationRepository violationRepository;

    @InjectMocks
    private ViolationServiceAdapterImpl violationServiceAdapter;

    private Violation testViolation;
    private static final Long TEST_ID = 1L;
    private static final String TEST_SERIAL = "ABC12345";
    private static final OffsetDateTime TEST_DATE = OffsetDateTime.of(2021, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime TEST_DATE_FROM = OffsetDateTime.of(2021, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime TEST_DATE_TO = OffsetDateTime.of(2021, 7, 21, 0, 0, 0, 0, ZoneOffset.UTC);

    private static final Date TEST_DATE_FROM_TYPE_DATE = Date.from(OffsetDateTime.of(2021, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
    private static final Date TEST_DATE_TO_TYPE_DATE = Date.from(OffsetDateTime.of(2021, 7, 21, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());

    @BeforeEach
    void setUp() {
        testViolation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_SERIAL)
                .occurrenceDateUtc(TEST_DATE)
                .build();
    }

    @Test
    void saveViolationRegistry_shouldSaveAndReturnViolation() {
        when(violationRepository.save(testViolation)).thenReturn(testViolation);

        Violation result = violationServiceAdapter.saveViolationRegistry(testViolation);

        assertNotNull(result);
        assertEquals(testViolation, result);
        assertEquals(TEST_ID, result.id());
        assertEquals(TEST_SERIAL, result.equipmentSerial());
        assertEquals(TEST_DATE, result.occurrenceDateUtc());
        verify(violationRepository, times(1)).save(testViolation);
    }

    @Test
    void saveViolationRegistry_shouldThrowExceptionWhenRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(violationRepository.save(testViolation)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationServiceAdapter.saveViolationRegistry(testViolation));
        assertEquals("Database error", thrown.getMessage());
        verify(violationRepository, times(1)).save(testViolation);
    }

    @Test
    void findViolationById_shouldReturnViolationWhenFound() {
        when(violationRepository.findById(TEST_ID)).thenReturn(testViolation);

        Violation result = violationServiceAdapter.findViolationById(TEST_ID);

        assertNotNull(result);
        assertEquals(testViolation, result);
        assertEquals(TEST_ID, result.id());
        assertEquals(TEST_SERIAL, result.equipmentSerial());
        assertEquals(TEST_DATE, result.occurrenceDateUtc());
        verify(violationRepository, times(1)).findById(TEST_ID);
    }

    @Test
    void findViolationById_shouldReturnNullWhenNotFound() {
        when(violationRepository.findById(TEST_ID)).thenReturn(null);

        Violation result = violationServiceAdapter.findViolationById(TEST_ID);

        assertNull(result);
        verify(violationRepository, times(1)).findById(TEST_ID);
    }

    @Test
    void findViolationById_shouldThrowExceptionWhenRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(violationRepository.findById(TEST_ID)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationServiceAdapter.findViolationById(TEST_ID));
        assertEquals("Database error", thrown.getMessage());
        verify(violationRepository, times(1)).findById(TEST_ID);
    }

    @Test
    void listViolationsByEquipment_shouldReturnViolationsList() {
        List<Violation> expectedViolations = Collections.singletonList(testViolation);
        when(violationRepository.findBySerialAndOptionalDateRange(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE))
                .thenReturn(expectedViolations);

        List<Violation> result = violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testViolation, result.get(0));
        assertEquals(TEST_ID, result.get(0).id());
        assertEquals(TEST_SERIAL, result.get(0).equipmentSerial());
        assertEquals(TEST_DATE, result.get(0).occurrenceDateUtc());
        verify(violationRepository, times(1)).findBySerialAndOptionalDateRange(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE);
    }

    @Test
    void listViolationsByEquipment_shouldReturnEmptyListWhenNoViolationsFound() {
        when(violationRepository.findBySerialAndOptionalDateRange(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE))
                .thenReturn(Collections.emptyList());

        List<Violation> result = violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(violationRepository, times(1)).findBySerialAndOptionalDateRange(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE);
    }

    @Test
    void listViolationsByEquipment_shouldHandleNullDates() {
        List<Violation> expectedViolations = Collections.singletonList(testViolation);
        when(violationRepository.findBySerialAndOptionalDateRange(TEST_SERIAL, null, null))
                .thenReturn(expectedViolations);

        List<Violation> result = violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testViolation, result.get(0));
        assertEquals(TEST_ID, result.get(0).id());
        assertEquals(TEST_SERIAL, result.get(0).equipmentSerial());
        assertEquals(TEST_DATE, result.get(0).occurrenceDateUtc());
        verify(violationRepository, times(1)).findBySerialAndOptionalDateRange(TEST_SERIAL, null, null);
    }

    @Test
    void listViolationsByEquipment_shouldThrowExceptionWhenRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(violationRepository.findBySerialAndOptionalDateRange(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                violationServiceAdapter.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE));
        assertEquals("Database error", thrown.getMessage());
        verify(violationRepository, times(1)).findBySerialAndOptionalDateRange(TEST_SERIAL, TEST_DATE_FROM_TYPE_DATE, TEST_DATE_TO_TYPE_DATE);
    }
}