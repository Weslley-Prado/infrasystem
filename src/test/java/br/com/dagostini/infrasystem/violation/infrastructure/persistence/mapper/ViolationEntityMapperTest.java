package br.com.dagostini.infrasystem.violation.infrastructure.persistence.mapper;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ViolationEntityMapperTest {

    private ViolationMapper violationMapper;
    private ViolationEntity violationEntity;
    private Violation violation;

    private final Long TEST_ID = 1L;
    private final String TEST_EQUIPMENT_SERIAL = "EQ123456";
    private final OffsetDateTime TEST_DATE = OffsetDateTime.of(2025, 7, 20, 22, 30, 0, 0, ZoneOffset.UTC);
    private final Double TEST_MEASURED_SPEED = 80.5;
    private final Double TEST_CONSIDERED_SPEED = 78.0;
    private final Double TEST_REGULATED_SPEED = 60.0;
    private final String TEST_PICTURE = "image.jpg";
    private final String TEST_TYPE = "SPEEDING";

    @BeforeEach
    void setUp() {
        violationMapper = new br.com.dagostini.infrasystem.violation.infrastructure.persistence.mapper.ViolationMapperImpl();

        violationEntity = new ViolationEntity();
        violationEntity.setId(TEST_ID);
        violationEntity.setEquipmentSerial(TEST_EQUIPMENT_SERIAL);
        violationEntity.setOccurrenceDateUtc(TEST_DATE);
        violationEntity.setMeasuredSpeed(TEST_MEASURED_SPEED);
        violationEntity.setConsideredSpeed(TEST_CONSIDERED_SPEED);
        violationEntity.setRegulatedSpeed(TEST_REGULATED_SPEED);
        violationEntity.setPicture(TEST_PICTURE);
        violationEntity.setType(TEST_TYPE);

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
    void testViolationEntityGettersAndSetters() {
        ViolationEntity entity = new ViolationEntity();

        entity.setId(TEST_ID);
        entity.setEquipmentSerial(TEST_EQUIPMENT_SERIAL);
        entity.setOccurrenceDateUtc(TEST_DATE);
        entity.setMeasuredSpeed(TEST_MEASURED_SPEED);
        entity.setConsideredSpeed(TEST_CONSIDERED_SPEED);
        entity.setRegulatedSpeed(TEST_REGULATED_SPEED);
        entity.setPicture(TEST_PICTURE);
        entity.setType(TEST_TYPE);

        assertEquals(TEST_ID, entity.getId());
        assertEquals(TEST_EQUIPMENT_SERIAL, entity.getEquipmentSerial());
        assertEquals(TEST_DATE, entity.getOccurrenceDateUtc());
        assertEquals(TEST_MEASURED_SPEED, entity.getMeasuredSpeed());
        assertEquals(TEST_CONSIDERED_SPEED, entity.getConsideredSpeed());
        assertEquals(TEST_REGULATED_SPEED, entity.getRegulatedSpeed());
        assertEquals(TEST_PICTURE, entity.getPicture());
        assertEquals(TEST_TYPE, entity.getType());
    }

    @Test
    void testToEntity_NullInput() {
        assertNull(violationMapper.toEntity(null));
    }

    @Test
    void testToEntity_ValidInput() {
        ViolationEntity result = violationMapper.toEntity(violation);

        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_EQUIPMENT_SERIAL, result.getEquipmentSerial());
        assertEquals(TEST_DATE, result.getOccurrenceDateUtc());
        assertEquals(TEST_MEASURED_SPEED, result.getMeasuredSpeed());
        assertEquals(TEST_CONSIDERED_SPEED, result.getConsideredSpeed());
        assertEquals(TEST_REGULATED_SPEED, result.getRegulatedSpeed());
        assertEquals(TEST_PICTURE, result.getPicture());
        assertEquals(TEST_TYPE, result.getType());
    }

    @Test
    void testToDomain_NullInput() {
        assertNull(violationMapper.toDomain(null));
    }

    @Test
    void testToDomain_ValidInput() {
        Violation result = violationMapper.toDomain(violationEntity);

        assertNotNull(result);
        assertEquals(TEST_ID, result.id());
        assertEquals(TEST_EQUIPMENT_SERIAL, result.equipmentSerial());
        assertEquals(TEST_DATE, result.occurrenceDateUtc());
        assertEquals(TEST_MEASURED_SPEED, result.measuredSpeed());
        assertEquals(TEST_CONSIDERED_SPEED, result.consideredSpeed());
        assertEquals(TEST_REGULATED_SPEED, result.regulatedSpeed());
        assertEquals(TEST_PICTURE, result.picture());
        assertEquals(TEST_TYPE, result.type());
    }

    @Test
    void testToDomainList_NullInput() {
        assertNull(violationMapper.toDomainList(null));
    }

    @Test
    void testToDomainList_EmptyList() {
        List<Violation> result = violationMapper.toDomainList(new ArrayList<>());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToDomainList_ValidInput() {
        ViolationEntity entity2 = new ViolationEntity();
        entity2.setId(2L);
        entity2.setEquipmentSerial("EQ789012");
        entity2.setOccurrenceDateUtc(TEST_DATE.plusDays(1));
        entity2.setMeasuredSpeed(90.0);
        entity2.setConsideredSpeed(88.0);
        entity2.setRegulatedSpeed(70.0);
        entity2.setPicture("image2.jpg");
        entity2.setType("SPEEDING");

        List<ViolationEntity> entityList = Arrays.asList(violationEntity, entity2);
        List<Violation> result = violationMapper.toDomainList(entityList);

        assertNotNull(result);
        assertEquals(2, result.size());

        Violation firstViolation = result.get(0);
        assertEquals(TEST_ID, firstViolation.id());
        assertEquals(TEST_EQUIPMENT_SERIAL, firstViolation.equipmentSerial());

        Violation secondViolation = result.get(1);
        assertEquals(2L, secondViolation.id());
        assertEquals("EQ789012", secondViolation.equipmentSerial());
    }

    @Test
    void testViolationEntityEqualsAndHashCode() {
        ViolationEntity entity1 = new ViolationEntity();
        entity1.setId(TEST_ID);
        entity1.setEquipmentSerial(TEST_EQUIPMENT_SERIAL);

        ViolationEntity entity2 = new ViolationEntity();
        entity2.setId(TEST_ID);
        entity2.setEquipmentSerial(TEST_EQUIPMENT_SERIAL);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());

        entity2.setEquipmentSerial("DIFFERENT");
        assertNotEquals(entity1, entity2);
    }

    @Test
    void testViolationEntityToString() {
        String result = violationEntity.toString();
        assertTrue(result.contains("id=" + TEST_ID));
        assertTrue(result.contains("equipmentSerial=" + TEST_EQUIPMENT_SERIAL));
        assertTrue(result.contains("picture=" + TEST_PICTURE));
    }
}