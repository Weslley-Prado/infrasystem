package br.com.dagostini.infrasystem.equipment.interfaces.mapper;


import br.com.agostini.openapi.provider.representation.EquipmentRequestRepresentation;
import br.com.agostini.openapi.provider.representation.EquipmentResponseRepresentation;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class EquipmentDtoMapperImplTest {
    private br.com.dagostini.infrasystem.equipment.interfaces.mapper.EquipmentDtoMapperImpl equipmentDtoMapper;

    private Equipment testEquipment;
    private EquipmentRequestRepresentation testRequestRepresentation;
    private EquipmentResponseRepresentation testResponseRepresentation;
    private static final String TEST_SERIAL = "ABC12345";
    private static final String TEST_MODEL = "TestModel";
    private static final String TEST_ADDRESS = "123 Test Street";
    private static final Double TEST_LATITUDE = 40.7128;
    private static final Double TEST_LONGITUDE = -74.0060;
    private static final BigDecimal TEST_LATITUDE_BD = BigDecimal.valueOf(TEST_LATITUDE);
    private static final BigDecimal TEST_LONGITUDE_BD = BigDecimal.valueOf(TEST_LONGITUDE);
    private static final Boolean TEST_ACTIVE = true;

    @BeforeEach
    void setUp() {
        equipmentDtoMapper = new br.com.dagostini.infrasystem.equipment.interfaces.mapper.EquipmentDtoMapperImpl();

        testEquipment = new Equipment();
        testEquipment.setSerial(TEST_SERIAL);
        testEquipment.setModel(TEST_MODEL);
        testEquipment.setAddress(TEST_ADDRESS);
        testEquipment.setLatitude(TEST_LATITUDE);
        testEquipment.setLongitude(TEST_LONGITUDE);
        testEquipment.setActive(TEST_ACTIVE);

        testRequestRepresentation = new EquipmentRequestRepresentation();
        testRequestRepresentation.setSerial(TEST_SERIAL);
        testRequestRepresentation.setModel(TEST_MODEL);
        testRequestRepresentation.setAddress(TEST_ADDRESS);
        testRequestRepresentation.setLatitude(TEST_LATITUDE_BD);
        testRequestRepresentation.setLongitude(TEST_LONGITUDE_BD);
        testRequestRepresentation.setActive(TEST_ACTIVE);

        testResponseRepresentation = new EquipmentResponseRepresentation();
        testResponseRepresentation.setSerial(TEST_SERIAL);
        testResponseRepresentation.setModel(TEST_MODEL);
        testResponseRepresentation.setAddress(TEST_ADDRESS);
        testResponseRepresentation.setLatitude(TEST_LATITUDE_BD);
        testResponseRepresentation.setLongitude(TEST_LONGITUDE_BD);
        testResponseRepresentation.setActive(TEST_ACTIVE);
    }

    @Test
    void toDomain_shouldMapRequestRepresentationToEquipmentCorrectly() {
        // Act
        Equipment result = equipmentDtoMapper.toDomain(testRequestRepresentation);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_SERIAL, result.getSerial());
        assertEquals(TEST_MODEL, result.getModel());
        assertEquals(TEST_ADDRESS, result.getAddress());
        assertEquals(TEST_LATITUDE, result.getLatitude());
        assertEquals(TEST_LONGITUDE, result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toDomain_shouldReturnNullWhenRequestRepresentationIsNull() {
        // Act
        Equipment result = equipmentDtoMapper.toDomain(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDomain_shouldMapPartialRequestRepresentationCorrectly() {
        EquipmentRequestRepresentation partialRequest = new EquipmentRequestRepresentation();
        partialRequest.setSerial(TEST_SERIAL);
        partialRequest.setActive(TEST_ACTIVE);

        Equipment result = equipmentDtoMapper.toDomain(partialRequest);

        assertNotNull(result);
        assertEquals(TEST_SERIAL, result.getSerial());
        assertNull(result.getModel());
        assertNull(result.getAddress());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toDomain_shouldHandleNullLatitudeAndLongitude() {
        testRequestRepresentation.setLatitude(null);
        testRequestRepresentation.setLongitude(null);

        Equipment result = equipmentDtoMapper.toDomain(testRequestRepresentation);

        assertNotNull(result);
        assertEquals(TEST_SERIAL, result.getSerial());
        assertEquals(TEST_MODEL, result.getModel());
        assertEquals(TEST_ADDRESS, result.getAddress());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toResponse_shouldMapEquipmentToResponseRepresentationCorrectly() {
        EquipmentResponseRepresentation result = equipmentDtoMapper.toResponse(testEquipment);

        assertNotNull(result);
        assertEquals(TEST_SERIAL, result.getSerial());
        assertEquals(TEST_MODEL, result.getModel());
        assertEquals(TEST_ADDRESS, result.getAddress());
        assertEquals(TEST_LATITUDE_BD, result.getLatitude());
        assertEquals(TEST_LONGITUDE_BD, result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toResponse_shouldReturnNullWhenEquipmentIsNull() {
        EquipmentResponseRepresentation result = equipmentDtoMapper.toResponse(null);
        assertNull(result);
    }

    @Test
    void toResponse_shouldMapPartialEquipmentCorrectly() {
        Equipment partialEquipment = new Equipment();
        partialEquipment.setSerial(TEST_SERIAL);
        partialEquipment.setActive(TEST_ACTIVE);
        EquipmentResponseRepresentation result = equipmentDtoMapper.toResponse(partialEquipment);
        assertNotNull(result);
        assertEquals(TEST_SERIAL, result.getSerial());
        assertNull(result.getModel());
        assertNull(result.getAddress());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toResponse_shouldHandleNullLatitudeAndLongitude() {
        testEquipment.setLatitude(null);
        testEquipment.setLongitude(null);

        EquipmentResponseRepresentation result = equipmentDtoMapper.toResponse(testEquipment);

        assertNotNull(result);
        assertEquals(TEST_SERIAL, result.getSerial());
        assertEquals(TEST_MODEL, result.getModel());
        assertEquals(TEST_ADDRESS, result.getAddress());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
    }

    @Test
    void toDomainAndToResponse_shouldBeSymmetric() {
        Equipment equipment = equipmentDtoMapper.toDomain(testRequestRepresentation);
        EquipmentResponseRepresentation result = equipmentDtoMapper.toResponse(equipment);

        assertNotNull(result);
        assertEquals(testRequestRepresentation.getSerial(), result.getSerial());
        assertEquals(testRequestRepresentation.getModel(), result.getModel());
        assertEquals(testRequestRepresentation.getAddress(), result.getAddress());
        assertEquals(testRequestRepresentation.getLatitude(), result.getLatitude());
        assertEquals(testRequestRepresentation.getLongitude(), result.getLongitude());
        assertEquals(testRequestRepresentation.getActive(), result.getActive());
    }
}
