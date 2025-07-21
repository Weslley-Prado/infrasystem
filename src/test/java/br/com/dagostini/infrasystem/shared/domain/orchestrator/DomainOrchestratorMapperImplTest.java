package br.com.dagostini.infrasystem.shared.domain.orchestrator;

import br.com.agostini.openapi.provider.representation.ViolationRequestRepresentation;
import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DomainOrchestratorMapperImplTest {

    @InjectMocks
    private br.com.dagostini.infrasystem.shared.domain.orchestrator.DomainOrchestratorMapperImpl mapper;

    private ViolationRequestRepresentation violationRequest;
    private Violation violation;
    private OffsetDateTime occurrenceDate;

    @BeforeEach
    void setUp() {
        occurrenceDate = OffsetDateTime.of(2025, 7, 20, 22, 0, 0, 0, ZoneOffset.UTC);
        violationRequest = new ViolationRequestRepresentation();
        violationRequest.setEquipmentSerial("EQUIP123");
        violationRequest.setOccurrenceDateUtc(new Date());
        violationRequest.setMeasuredSpeed(BigDecimal.valueOf(80.5));
        violationRequest.setConsideredSpeed(BigDecimal.valueOf(78.0));
        violationRequest.setRegulatedSpeed(BigDecimal.valueOf(60.0));
        violationRequest.setPicture("image.jpg");
        violationRequest.setType(ViolationRequestRepresentation.TypeEnum.VELOCITY);

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
    }

    // Tests for toDomain
    @Test
    void testToDomainWithAllFields() {
        Violation result = mapper.toDomain(violationRequest);

        assertNotNull(result, "Mapped Violation should not be null");
        assertEquals("EQUIP123", result.equipmentSerial(), "Equipment serial should match");
        assertEquals(80.5, result.measuredSpeed(), "Measured speed should match");
        assertEquals(78.0, result.consideredSpeed(), "Considered speed should match");
        assertEquals(60.0, result.regulatedSpeed(), "Regulated speed should match");
        assertEquals("image.jpg", result.picture(), "Picture should match");
        assertEquals("VELOCITY", result.type(), "Type should match");
    }

    @Test
    void testToDomainWithNullFields() {
        violationRequest.setMeasuredSpeed(null);
        violationRequest.setConsideredSpeed(null);
        violationRequest.setRegulatedSpeed(null);
        violationRequest.setType(null);

        Violation result = mapper.toDomain(violationRequest);

        assertNotNull(result, "Mapped Violation should not be null");
        assertEquals("EQUIP123", result.equipmentSerial(), "Equipment serial should match");
        assertNull(result.measuredSpeed(), "Measured speed should be null");
        assertNull(result.consideredSpeed(), "Considered speed should be null");
        assertNull(result.regulatedSpeed(), "Regulated speed should be null");
        assertEquals("image.jpg", result.picture(), "Picture should match");
        assertNull(result.type(), "Type should be null");
    }

    @Test
    void testToDomainWithNullInput() {
        Violation result = mapper.toDomain(null);
        assertNull(result, "Result should be null for null input");
    }

    // Tests for toResponseList
    @Test
    void testToResponseListWithMultipleViolations() {
        List<Violation> violations = Arrays.asList(violation, Violation.builder()
                .id(2L)
                .equipmentSerial("EQUIP456")
                .occurrenceDateUtc(occurrenceDate)
                .measuredSpeed(90.0)
                .consideredSpeed(88.0)
                .regulatedSpeed(70.0)
                .picture("image2.jpg")
                .type("VELOCITY")
                .build());

        List<ViolationResponseRepresentation> result = mapper.toResponseList(violations);

        assertNotNull(result, "Result list should not be null");
        assertEquals(2, result.size(), "Result list should contain two items");

        ViolationResponseRepresentation first = result.get(0);
        assertEquals(1L, first.getId(), "First ID should match");
        assertEquals("EQUIP123", first.getEquipmentSerial(), "First equipment serial should match");
        assertEquals(BigDecimal.valueOf(80.5), first.getMeasuredSpeed(), "First measured speed should match");
        assertEquals(BigDecimal.valueOf(78.0), first.getConsideredSpeed(), "First considered speed should match");
        assertEquals(BigDecimal.valueOf(60.0), first.getRegulatedSpeed(), "First regulated speed should match");
        assertEquals("image.jpg", first.getPicture(), "First picture should match");
        assertEquals(ViolationResponseRepresentation.TypeEnum.VELOCITY, first.getType(), "First type should match");

        ViolationResponseRepresentation second = result.get(1);
        assertEquals(2L, second.getId(), "Second ID should match");
        assertEquals("EQUIP456", second.getEquipmentSerial(), "Second equipment serial should match");
    }

    @Test
    void testToResponseListWithEmptyList() {
        List<ViolationResponseRepresentation> result = mapper.toResponseList(Collections.emptyList());
        assertNotNull(result, "Result list should not be null");
        assertTrue(result.isEmpty(), "Result list should be empty");
    }

    @Test
    void testToResponseListWithNullInput() {
        List<ViolationResponseRepresentation> result = mapper.toResponseList(null);
        assertNull(result, "Result should be null for null input");
    }

    // Tests for toViolationResponseRepresentation
    @Test
    void testToViolationResponseRepresentationWithAllFields() {
        ViolationResponseRepresentation result = mapper.toViolationResponseRepresentation(violation);

        assertNotNull(result, "Mapped ViolationResponseRepresentation should not be null");
        assertEquals(1L, result.getId(), "ID should match");
        assertEquals("EQUIP123", result.getEquipmentSerial(), "Equipment serial should match");
        assertEquals(BigDecimal.valueOf(80.5), result.getMeasuredSpeed(), "Measured speed should match");
        assertEquals(BigDecimal.valueOf(78.0), result.getConsideredSpeed(), "Considered speed should match");
        assertEquals(BigDecimal.valueOf(60.0), result.getRegulatedSpeed(), "Regulated speed should match");
        assertEquals(ViolationResponseRepresentation.TypeEnum.VELOCITY, result.getType(), "Type should match");
    }

    @Test
    void testToViolationResponseRepresentationWithNullFields() {
        Violation nullFieldsViolation = Violation.builder()
                .id(1L)
                .equipmentSerial("EQUIP123")
                .occurrenceDateUtc(occurrenceDate)
                .picture("image.jpg")
                .build();

        ViolationResponseRepresentation result = mapper.toViolationResponseRepresentation(nullFieldsViolation);

        assertNotNull(result, "Mapped ViolationResponseRepresentation should not be null");
        assertEquals(1L, result.getId(), "ID should match");
        assertEquals("EQUIP123", result.getEquipmentSerial(), "Equipment serial should match");
        assertNull(result.getMeasuredSpeed(), "Measured speed should be null");
        assertNull(result.getConsideredSpeed(), "Considered speed should be null");
        assertNull(result.getRegulatedSpeed(), "Regulated speed should be null");
        assertNull(result.getType(), "Type should be null");
    }

    @Test
    void testToViolationResponseRepresentationWithNullInput() {
        ViolationResponseRepresentation result = mapper.toViolationResponseRepresentation(null);
        assertNull(result, "Result should be null for null input");
    }

    @Test
    void testToViolationResponseRepresentationWithInvalidType() {
        Violation invalidTypeViolation = Violation.builder()
                .id(1L)
                .equipmentSerial("EQUIP123")
                .occurrenceDateUtc(occurrenceDate)
                .measuredSpeed(80.5)
                .consideredSpeed(78.0)
                .regulatedSpeed(60.0)
                .picture("image.jpg")
                .type("INVALID_TYPE")
                .build();

        assertThrows(IllegalArgumentException.class, () -> mapper.toViolationResponseRepresentation(invalidTypeViolation),
                "Should throw IllegalArgumentException for invalid type");
    }
}