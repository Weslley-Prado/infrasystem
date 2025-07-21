package br.com.dagostini.infrasystem.equipment.infrastructure.persistence.mapper;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.entity.EquipmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(MockitoExtension.class)
class EquipmentMapperImplTest {

    private br.com.dagostini.infrasystem.equipment.infrastructure.persistence.mapper.EquipmentMapperImpl equipmentMapper;

    private Equipment testEquipment;
    private EquipmentEntity testEquipmentEntity;
    private static final Long TEST_ID = 1L;
    private static final String TEST_SERIAL = "ABC12345";
    private static final String TEST_MODEL = "TestModel";
    private static final String TEST_ADDRESS = "123 Test Street";
    private static final Double TEST_LATITUDE = 40.7128;
    private static final Double TEST_LONGITUDE = -74.0060;
    private static final Boolean TEST_ACTIVE = true;

    @BeforeEach
    void setUp() {
        equipmentMapper = new br.com.dagostini.infrasystem.equipment.infrastructure.persistence.mapper.EquipmentMapperImpl();

        testEquipment = new Equipment();
        testEquipment.setId(TEST_ID);
        testEquipment.setSerial(TEST_SERIAL);
        testEquipment.setModel(TEST_MODEL);
        testEquipment.setAddress(TEST_ADDRESS);
        testEquipment.setLatitude(TEST_LATITUDE);
        testEquipment.setLongitude(TEST_LONGITUDE);
        testEquipment.setActive(TEST_ACTIVE);

        testEquipmentEntity = new EquipmentEntity();
        testEquipmentEntity.setId(TEST_ID);
        testEquipmentEntity.setSerial(TEST_SERIAL);
        testEquipmentEntity.setModel(TEST_MODEL);
        testEquipmentEntity.setAddress(TEST_ADDRESS);
        testEquipmentEntity.setLatitude(TEST_LATITUDE);
        testEquipmentEntity.setLongitude(TEST_LONGITUDE);
        testEquipmentEntity.setActive(TEST_ACTIVE);
    }

    @Test
    void toEntity_shouldMapEquipmentToEntityCorrectly() {
        EquipmentEntity result = equipmentMapper.toEntity(testEquipment);

        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_SERIAL, result.getSerial());
        assertEquals(TEST_MODEL, result.getModel());
        assertEquals(TEST_ADDRESS, result.getAddress());
        assertEquals(TEST_LATITUDE, result.getLatitude());
        assertEquals(TEST_LONGITUDE, result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toEntity_shouldReturnNullWhenEquipmentIsNull() {
        EquipmentEntity result = equipmentMapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void toEntity_shouldMapPartialEquipmentCorrectly() {
        Equipment partialEquipment = new Equipment();
        partialEquipment.setSerial(TEST_SERIAL);
        partialEquipment.setActive(TEST_ACTIVE);

        EquipmentEntity result = equipmentMapper.toEntity(partialEquipment);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(TEST_SERIAL, result.getSerial());
        assertNull(result.getModel());
        assertNull(result.getAddress());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toDomain_shouldMapEntityToEquipmentCorrectly() {
        Equipment result = equipmentMapper.toDomain(testEquipmentEntity);
        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_SERIAL, result.getSerial());
        assertEquals(TEST_MODEL, result.getModel());
        assertEquals(TEST_ADDRESS, result.getAddress());
        assertEquals(TEST_LATITUDE, result.getLatitude());
        assertEquals(TEST_LONGITUDE, result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toDomain_shouldReturnNullWhenEntityIsNull() {
        Equipment result = equipmentMapper.toDomain(null);
        assertNull(result);
    }

    @Test
    void toDomain_shouldMapPartialEntityCorrectly() {
        EquipmentEntity partialEntity = new EquipmentEntity();
        partialEntity.setSerial(TEST_SERIAL);
        partialEntity.setActive(TEST_ACTIVE);
        Equipment result = equipmentMapper.toDomain(partialEntity);
        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(TEST_SERIAL, result.getSerial());
        assertNull(result.getModel());
        assertNull(result.getAddress());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toEntityAndToDomain_shouldBeSymmetric() {
        EquipmentEntity entity = equipmentMapper.toEntity(testEquipment);
        Equipment result = equipmentMapper.toDomain(entity);
        assertNotNull(result);
        assertEquals(testEquipment.getId(), result.getId());
        assertEquals(testEquipment.getSerial(), result.getSerial());
        assertEquals(testEquipment.getModel(), result.getModel());
        assertEquals(testEquipment.getAddress(), result.getAddress());
        assertEquals(testEquipment.getLatitude(), result.getLatitude());
        assertEquals(testEquipment.getLongitude(), result.getLongitude());
        assertEquals(testEquipment.getActive(), result.getActive());
    }
}