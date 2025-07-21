package br.com.dagostini.infrasystem.violation.interfaces.mapper;

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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ViolationDtoMapperTest {

    @InjectMocks
    private br.com.dagostini.infrasystem.violation.interfaces.mapper.ViolationDtoMapperImpl violationDtoMapper;

    private ViolationRequestRepresentation requestRepresentation;
    private Violation violation;
    private final Long TEST_ID = 1L;
    private final String TEST_EQUIPMENT_SERIAL = "EQ123456";
    private final OffsetDateTime TEST_DATE = OffsetDateTime.of(2025, 7, 20, 22, 30, 0, 0, ZoneOffset.UTC);
    private final Date TEST_DATE_STRING = new Date();
    private final Double TEST_MEASURED_SPEED = 80.5;
    private final Double TEST_CONSIDERED_SPEED = 78.0;
    private final Double TEST_REGULATED_SPEED = 60.0;
    private final String TEST_PICTURE = "image.jpg";
    private final String TEST_TYPE = "VELOCITY";

    @BeforeEach
    void setUp() {
        // Setup test data
        requestRepresentation = new ViolationRequestRepresentation();
        requestRepresentation.setEquipmentSerial(TEST_EQUIPMENT_SERIAL);
        requestRepresentation.setOccurrenceDateUtc(TEST_DATE_STRING);
        requestRepresentation.setMeasuredSpeed(BigDecimal.valueOf(TEST_MEASURED_SPEED));
        requestRepresentation.setConsideredSpeed(BigDecimal.valueOf(TEST_CONSIDERED_SPEED));
        requestRepresentation.setRegulatedSpeed(BigDecimal.valueOf(TEST_REGULATED_SPEED));
        requestRepresentation.setPicture(TEST_PICTURE);
        requestRepresentation.setType(ViolationRequestRepresentation.TypeEnum.VELOCITY);

        violation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_EQUIPMENT_SERIAL)
                .occurrenceDateUtc(TEST_DATE)
                .measuredSpeed(TEST_MEASURED_SPEED)
                .consideredSpeed(TEST_CONSIDERED_SPEED)
                .regulatedSpeed(TEST_REGULATED_SPEED)
                .picture(TEST_PICTURE)
                .type(TEST_TYPE)
                .build();
    }

    @Test
    void testToDomain_NullInput() {
        assertNull(violationDtoMapper.toDomain(null));
    }

    @Test
    void testToDomain_ValidInput() {
        Violation result = violationDtoMapper.toDomain(requestRepresentation);

        assertNotNull(result);
        assertEquals(TEST_EQUIPMENT_SERIAL, result.equipmentSerial());
        assertEquals(TEST_MEASURED_SPEED, result.measuredSpeed());
        assertEquals(TEST_CONSIDERED_SPEED, result.consideredSpeed());
        assertEquals(TEST_REGULATED_SPEED, result.regulatedSpeed());
        assertEquals(TEST_PICTURE, result.picture());
        assertEquals(TEST_TYPE, result.type());
    }

    @Test
    void testToDomain_NullOptionalFields() {
        requestRepresentation.setMeasuredSpeed(null);
        requestRepresentation.setConsideredSpeed(null);
        requestRepresentation.setRegulatedSpeed(null);
        requestRepresentation.setType(null);

        Violation result = violationDtoMapper.toDomain(requestRepresentation);

        assertNotNull(result);
        assertEquals(TEST_EQUIPMENT_SERIAL, result.equipmentSerial());
        assertNull(result.measuredSpeed());
        assertNull(result.consideredSpeed());
        assertNull(result.regulatedSpeed());
        assertEquals(TEST_PICTURE, result.picture());
        assertNull(result.type());
    }

    @Test
    void testToResponse_NullInput() {
        assertNull(violationDtoMapper.toResponse(null));
    }

    @Test
    void testToResponse_ValidInput() {
        ViolationResponseRepresentation result = violationDtoMapper.toResponse(violation);

        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_EQUIPMENT_SERIAL, result.getEquipmentSerial());
        assertEquals(BigDecimal.valueOf(TEST_MEASURED_SPEED), result.getMeasuredSpeed());
        assertEquals(BigDecimal.valueOf(TEST_CONSIDERED_SPEED), result.getConsideredSpeed());
        assertEquals(BigDecimal.valueOf(TEST_REGULATED_SPEED), result.getRegulatedSpeed());
        assertEquals(TEST_PICTURE, result.getPicture());
        assertEquals(ViolationResponseRepresentation.TypeEnum.VELOCITY, result.getType());
    }

    @Test
    void testToResponse_NullOptionalFields() {
        Violation nullFieldsViolation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_EQUIPMENT_SERIAL)
                .occurrenceDateUtc(TEST_DATE)
                .picture(TEST_PICTURE)
                .build();

        ViolationResponseRepresentation result = violationDtoMapper.toResponse(nullFieldsViolation);

        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_EQUIPMENT_SERIAL, result.getEquipmentSerial());
        assertNull(result.getMeasuredSpeed());
        assertNull(result.getConsideredSpeed());
        assertNull(result.getRegulatedSpeed());
        assertNull(result.getType());
    }

    @Test
    void testToResponse_InvalidEnumType() {
        Violation invalidTypeViolation = Violation.builder()
                .id(TEST_ID)
                .equipmentSerial(TEST_EQUIPMENT_SERIAL)
                .occurrenceDateUtc(TEST_DATE)
                .picture(TEST_PICTURE)
                .type("INVALID_TYPE")
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            violationDtoMapper.toResponse(invalidTypeViolation);
        });
    }
}