package br.com.dagostini.infrasystem.shared.domain.orchestrator;

import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.violation.application.service.ViolationServiceAdapter;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainOrchestratorImplTest {

    @InjectMocks
    private DomainOrchestratorImpl domainOrchestrator;

    @Mock
    private ViolationServiceAdapter violationServiceAdapter;

    @Mock
    private DomainOrchestratorMapper domainOrchestratorMapper;

    private Violation violation;
    private ViolationResponseRepresentation responseRepresentation;
    private Date fromDate;
    private Date toDate;

    @BeforeEach
    void setUp() {
        OffsetDateTime occurrenceDate = OffsetDateTime.of(2025, 7, 20, 22, 0, 0, 0, ZoneOffset.UTC);
        fromDate = new Date(1745174400000L); // Approx 2025-07-20
        toDate = new Date(1745260800000L);   // Approx 2025-07-21

        violation = Violation.builder()
                .id(1L)
                .equipmentSerial("EQUIP123")
                .occurrenceDateUtc(occurrenceDate)
                .measuredSpeed(80.5)
                .consideredSpeed(78.0)
                .regulatedSpeed(60.0)
                .picture("image.jpg")
                .type("VELOCITY")
                .build();

        responseRepresentation = new ViolationResponseRepresentation();
        responseRepresentation.setId(1L);
        responseRepresentation.setEquipmentSerial("EQUIP123");
        responseRepresentation.setOccurrenceDateUtc(new Date());
        responseRepresentation.setMeasuredSpeed(BigDecimal.valueOf(80.5));
        responseRepresentation.setConsideredSpeed(BigDecimal.valueOf(78.0));
        responseRepresentation.setRegulatedSpeed(BigDecimal.valueOf(60.0));
        responseRepresentation.setPicture("image.jpg");
        responseRepresentation.setType(ViolationResponseRepresentation.TypeEnum.VELOCITY);
    }

    @Test
    void testListViolationsByEquipmentWithValidInput() {
        // Arrange
        String serial = "EQUIP123";
        List<Violation> violations = Arrays.asList(violation);
        List<ViolationResponseRepresentation> responseList = Arrays.asList(responseRepresentation);

        when(violationServiceAdapter.listViolationsByEquipment(serial, fromDate, toDate))
                .thenReturn(violations);
        when(domainOrchestratorMapper.toResponseList(violations))
                .thenReturn(responseList);

        // Act
        ResponseEntity<List<ViolationResponseRepresentation>> response = domainOrchestrator.listViolationsByEquipment(serial, fromDate, toDate);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertEquals(responseList, response.getBody(), "Response body should match mapped list");
        assertEquals(1, response.getBody().size(), "Response list should contain one item");
        assertEquals("EQUIP123", response.getBody().get(0).getEquipmentSerial(), "Equipment serial should match");

        verify(violationServiceAdapter).listViolationsByEquipment(serial, fromDate, toDate);
        verify(domainOrchestratorMapper).toResponseList(violations);
    }

    @Test
    void testListViolationsByEquipmentWithEmptyList() {
        // Arrange
        String serial = "EQUIP123";
        List<Violation> violations = Collections.emptyList();
        List<ViolationResponseRepresentation> responseList = Collections.emptyList();

        when(violationServiceAdapter.listViolationsByEquipment(serial, fromDate, toDate))
                .thenReturn(violations);
        when(domainOrchestratorMapper.toResponseList(violations))
                .thenReturn(responseList);

        // Act
        ResponseEntity<List<ViolationResponseRepresentation>> response = domainOrchestrator.listViolationsByEquipment(serial, fromDate, toDate);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty");

        verify(violationServiceAdapter).listViolationsByEquipment(serial, fromDate, toDate);
        verify(domainOrchestratorMapper).toResponseList(violations);
    }

    @Test
    void testListViolationsByEquipmentWithNullSerial() {
        // Arrange
        List<Violation> violations = Arrays.asList(violation);
        List<ViolationResponseRepresentation> responseList = Arrays.asList(responseRepresentation);

        when(violationServiceAdapter.listViolationsByEquipment(null, fromDate, toDate))
                .thenReturn(violations);
        when(domainOrchestratorMapper.toResponseList(violations))
                .thenReturn(responseList);

        // Act
        ResponseEntity<List<ViolationResponseRepresentation>> response = domainOrchestrator.listViolationsByEquipment(null, fromDate, toDate);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertEquals(responseList, response.getBody(), "Response body should match mapped list");

        verify(violationServiceAdapter).listViolationsByEquipment(null, fromDate, toDate);
        verify(domainOrchestratorMapper).toResponseList(violations);
    }

    @Test
    void testListViolationsByEquipmentWithNullDates() {
        // Arrange
        String serial = "EQUIP123";
        List<Violation> violations = Arrays.asList(violation);
        List<ViolationResponseRepresentation> responseList = Arrays.asList(responseRepresentation);

        when(violationServiceAdapter.listViolationsByEquipment(serial, null, null))
                .thenReturn(violations);
        when(domainOrchestratorMapper.toResponseList(violations))
                .thenReturn(responseList);

        // Act
        ResponseEntity<List<ViolationResponseRepresentation>> response = domainOrchestrator.listViolationsByEquipment(serial, null, null);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertEquals(responseList, response.getBody(), "Response body should match mapped list");

        verify(violationServiceAdapter).listViolationsByEquipment(serial, null, null);
        verify(domainOrchestratorMapper).toResponseList(violations);
    }

    @Test
    void testListViolationsByEquipmentWithServiceException() {
        // Arrange
        String serial = "EQUIP123";
        when(violationServiceAdapter.listViolationsByEquipment(serial, fromDate, toDate))
                .thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> domainOrchestrator.listViolationsByEquipment(serial, fromDate, toDate),
                "Should propagate service exception");

        verify(violationServiceAdapter).listViolationsByEquipment(serial, fromDate, toDate);
        verify(domainOrchestratorMapper, never()).toResponseList(anyList());
    }

    @Test
    void testListViolationsByEquipmentWithSameFromAndToDate() {
        // Arrange
        String serial = "EQUIP123";
        Date sameDate = new Date(1745174400000L); // 2025-07-20
        List<Violation> violations = Arrays.asList(violation);
        List<ViolationResponseRepresentation> responseList = Arrays.asList(responseRepresentation);

        when(violationServiceAdapter.listViolationsByEquipment(serial, sameDate, sameDate))
                .thenReturn(violations);
        when(domainOrchestratorMapper.toResponseList(violations))
                .thenReturn(responseList);

        // Act
        ResponseEntity<List<ViolationResponseRepresentation>> response = domainOrchestrator.listViolationsByEquipment(serial, sameDate, sameDate);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertEquals(responseList, response.getBody(), "Response body should match mapped list");

        verify(violationServiceAdapter).listViolationsByEquipment(serial, sameDate, sameDate);
        verify(domainOrchestratorMapper).toResponseList(violations);
    }
}