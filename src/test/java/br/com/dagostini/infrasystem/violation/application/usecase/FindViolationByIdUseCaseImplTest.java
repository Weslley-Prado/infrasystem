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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindViolationByIdUseCaseImplTest {

    @Mock
    private ViolationService violationService;

    @InjectMocks
    private FindViolationByIdUseCaseImpl findViolationByIdUseCase;

    private Violation testViolation;
    private static final Long TEST_ID = 1L;
    private static final String TEST_SERIAL = "ABC12345";
    private static final OffsetDateTime TEST_DATE = OffsetDateTime.of(2021, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        testViolation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_SERIAL)
                .occurrenceDateUtc(TEST_DATE)
                .build();
    }

    @Test
    void findViolationById_shouldReturnViolationWhenFound() {
        when(violationService.findViolationById(TEST_ID)).thenReturn(testViolation);

        Violation result = findViolationByIdUseCase.findViolationById(TEST_ID);

        assertNotNull(result);
        assertEquals(testViolation, result);
        assertEquals(TEST_ID, result.id());
        assertEquals(TEST_SERIAL, result.equipmentSerial());
        assertEquals(TEST_DATE, result.occurrenceDateUtc());
        verify(violationService, times(1)).findViolationById(TEST_ID);
    }

    @Test
    void findViolationById_shouldReturnNullWhenNotFound() {
        when(violationService.findViolationById(TEST_ID)).thenReturn(null);

        Violation result = findViolationByIdUseCase.findViolationById(TEST_ID);

        assertNull(result);
        verify(violationService, times(1)).findViolationById(TEST_ID);
    }

    @Test
    void findViolationById_shouldThrowExceptionWhenServiceFails() {
        RuntimeException exception = new RuntimeException("Service error");
        when(violationService.findViolationById(TEST_ID)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                findViolationByIdUseCase.findViolationById(TEST_ID));
        assertEquals("Service error", thrown.getMessage());
        verify(violationService, times(1)).findViolationById(TEST_ID);
    }

    @Test
    void findViolationById_shouldHandleNullId() {
        when(violationService.findViolationById(null)).thenReturn(null);

        Violation result = findViolationByIdUseCase.findViolationById(null);
        assertNull(result);
        verify(violationService, times(1)).findViolationById(null);
    }
}