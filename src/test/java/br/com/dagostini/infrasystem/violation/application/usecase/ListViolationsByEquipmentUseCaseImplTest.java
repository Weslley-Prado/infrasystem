package br.com.dagostini.infrasystem.violation.application.usecase;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.service.ViolationService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListViolationsByEquipmentUseCaseImplTest {

    @Mock
    private ViolationService violationService;

    @InjectMocks
    private ListViolationsByEquipmentUseCaseImpl listViolationsByEquipmentUseCase;

    private Violation testViolation;
    private static final Long TEST_ID = 1L;
    private static final String TEST_SERIAL = "ABC12345";
    private static final OffsetDateTime TEST_VIOLATION_DATE = OffsetDateTime.of(2021, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final Date TEST_DATE_FROM = new Date(1626748800000L); // 2021-07-20
    private static final Date TEST_DATE_TO = new Date(1626835200000L);   // 2021-07-21

    @BeforeEach
    void setUp() {
        testViolation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_SERIAL)
                .occurrenceDateUtc(TEST_VIOLATION_DATE)
                .build();
    }

    @Test
    void listViolationsByEquipment_shouldReturnViolationsList() {
        List<Violation> expectedViolations = Collections.singletonList(testViolation);
        when(violationService.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO))
                .thenReturn(expectedViolations);

        List<Violation> result = listViolationsByEquipmentUseCase.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testViolation, result.get(0));
        assertEquals(TEST_ID, result.get(0).id());
        assertEquals(TEST_SERIAL, result.get(0).equipmentSerial());
        assertEquals(TEST_VIOLATION_DATE, result.get(0).occurrenceDateUtc());
        verify(violationService, times(1)).listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);
    }

    @Test
    void listViolationsByEquipment_shouldReturnEmptyListWhenNoViolationsFound() {
        when(violationService.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO))
                .thenReturn(Collections.emptyList());

        List<Violation> result = listViolationsByEquipmentUseCase.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(violationService, times(1)).listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);
    }

    @Test
    void listViolationsByEquipment_shouldHandleNullDates() {
        List<Violation> expectedViolations = Collections.singletonList(testViolation);
        when(violationService.listViolationsByEquipment(TEST_SERIAL, null, null))
                .thenReturn(expectedViolations);

        List<Violation> result = listViolationsByEquipmentUseCase.listViolationsByEquipment(TEST_SERIAL, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testViolation, result.get(0));
        assertEquals(TEST_ID, result.get(0).id());
        assertEquals(TEST_SERIAL, result.get(0).equipmentSerial());
        assertEquals(TEST_VIOLATION_DATE, result.get(0).occurrenceDateUtc());
        verify(violationService, times(1)).listViolationsByEquipment(TEST_SERIAL, null, null);
    }

    @Test
    void listViolationsByEquipment_shouldHandleNullSerial() {
        List<Violation> expectedViolations = Collections.emptyList();
        when(violationService.listViolationsByEquipment(null, TEST_DATE_FROM, TEST_DATE_TO))
                .thenReturn(expectedViolations);

        List<Violation> result = listViolationsByEquipmentUseCase.listViolationsByEquipment(null, TEST_DATE_FROM, TEST_DATE_TO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(violationService, times(1)).listViolationsByEquipment(null, TEST_DATE_FROM, TEST_DATE_TO);
    }

    @Test
    void listViolationsByEquipment_shouldThrowExceptionWhenServiceFails() {
        RuntimeException exception = new RuntimeException("Service error");
        when(violationService.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                listViolationsByEquipmentUseCase.listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO));
        assertEquals("Service error", thrown.getMessage());
        verify(violationService, times(1)).listViolationsByEquipment(TEST_SERIAL, TEST_DATE_FROM, TEST_DATE_TO);
    }
}