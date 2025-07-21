package br.com.dagostini.infrasystem.equipment.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.entity.EquipmentEntity;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.mapper.EquipmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentRepositoryImplTest {

    @Mock
    private EquipmentJpaRepository jpaRepository;

    @Mock
    private EquipmentMapper mapper;

    @Mock
    private Logger logger;

    @InjectMocks
    private EquipmentRepositoryImpl equipmentRepository;

    private Equipment testEquipment;
    private EquipmentEntity testEquipmentEntity;
    private static final String TEST_SERIAL = "ABC12345";
    private static final String SHORT_SERIAL = "ABC";
    private static final String MASKED_SERIAL = "****2345";

    @BeforeEach
    void setUp() {
        testEquipment = new Equipment();
        testEquipment.setSerial(TEST_SERIAL);
        testEquipment.setActive(true);

        testEquipmentEntity = new EquipmentEntity();
        testEquipmentEntity.setSerial(TEST_SERIAL);
        testEquipmentEntity.setActive(true);
    }

    @Test
    void save_shouldSaveEquipmentAndReturnMappedDomain() {
        when(mapper.toEntity(testEquipment)).thenReturn(testEquipmentEntity);
        when(jpaRepository.save(testEquipmentEntity)).thenReturn(testEquipmentEntity);
        when(mapper.toDomain(testEquipmentEntity)).thenReturn(testEquipment);

        Equipment result = equipmentRepository.save(testEquipment);

        assertNotNull(result);
        assertEquals(testEquipment, result);
        assertEquals(TEST_SERIAL, result.getSerial());
        verify(mapper, times(1)).toEntity(testEquipment);
        verify(jpaRepository, times(1)).save(testEquipmentEntity);
        verify(mapper, times(1)).toDomain(testEquipmentEntity);
    }

    @Test
    void save_shouldThrowExceptionWhenJpaRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(mapper.toEntity(testEquipment)).thenReturn(testEquipmentEntity);
        when(jpaRepository.save(testEquipmentEntity)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                equipmentRepository.save(testEquipment));
        assertEquals("Database error", thrown.getMessage());
        verify(mapper, times(1)).toEntity(testEquipment);
        verify(jpaRepository, times(1)).save(testEquipmentEntity);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findAll_shouldReturnListOfEquipments() {
        List<EquipmentEntity> entities = Collections.singletonList(testEquipmentEntity);
        List<Equipment> expectedEquipments = Collections.singletonList(testEquipment);
        when(jpaRepository.findAll()).thenReturn(entities);
        when(mapper.toDomain(testEquipmentEntity)).thenReturn(testEquipment);

        List<Equipment> result = equipmentRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEquipment, result.get(0));
        verify(jpaRepository, times(1)).findAll();
        verify(mapper, times(1)).toDomain(testEquipmentEntity);
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoEquipmentsFound() {
        when(jpaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Equipment> result = equipmentRepository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaRepository, times(1)).findAll();
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findAll_shouldThrowExceptionWhenJpaRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(jpaRepository.findAll()).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                equipmentRepository.findAll());
        assertEquals("Database error", thrown.getMessage());
        verify(jpaRepository, times(1)).findAll();
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findBySerial_shouldReturnEquipmentWhenFound() {
        when(jpaRepository.findBySerial(TEST_SERIAL)).thenReturn(Optional.of(testEquipmentEntity));
        when(mapper.toDomain(testEquipmentEntity)).thenReturn(testEquipment);

        Optional<Equipment> result = equipmentRepository.findBySerial(TEST_SERIAL);

        assertTrue(result.isPresent());
        assertEquals(testEquipment, result.get());
        assertEquals(TEST_SERIAL, result.get().getSerial());
        verify(jpaRepository, times(1)).findBySerial(TEST_SERIAL);
        verify(mapper, times(1)).toDomain(testEquipmentEntity);
    }

    @Test
    void findBySerial_shouldReturnEmptyWhenNotFound() {
        when(jpaRepository.findBySerial(TEST_SERIAL)).thenReturn(Optional.empty());

        Optional<Equipment> result = equipmentRepository.findBySerial(TEST_SERIAL);

        assertTrue(result.isEmpty());
        verify(jpaRepository, times(1)).findBySerial(TEST_SERIAL);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findBySerial_shouldThrowExceptionWhenJpaRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(jpaRepository.findBySerial(TEST_SERIAL)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                equipmentRepository.findBySerial(TEST_SERIAL));
        assertEquals("Database error", thrown.getMessage());
        verify(jpaRepository, times(1)).findBySerial(TEST_SERIAL);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void maskSerial_shouldMaskSerialCorrectly() throws Exception {
        Method maskSerialMethod = EquipmentRepositoryImpl.class.getDeclaredMethod("maskSerial", String.class);
        maskSerialMethod.setAccessible(true);

        String result = (String) maskSerialMethod.invoke(equipmentRepository, TEST_SERIAL);

        assertEquals(MASKED_SERIAL, result);
    }

    @Test
    void maskSerial_shouldReturnDefaultMaskForNullSerial() throws Exception {
        Method maskSerialMethod = EquipmentRepositoryImpl.class.getDeclaredMethod("maskSerial", String.class);
        maskSerialMethod.setAccessible(true);

        String result = (String) maskSerialMethod.invoke(equipmentRepository, (String) null);

        assertEquals("****", result);
    }

    @Test
    void maskSerial_shouldReturnDefaultMaskForShortSerial() throws Exception {
        Method maskSerialMethod = EquipmentRepositoryImpl.class.getDeclaredMethod("maskSerial", String.class);
        maskSerialMethod.setAccessible(true);

        String result = (String) maskSerialMethod.invoke(equipmentRepository, SHORT_SERIAL);

        assertEquals("****", result);
    }

    @Test
    void maskSerial_shouldReturnDefaultMaskForEmptySerial() throws Exception {
        Method maskSerialMethod = EquipmentRepositoryImpl.class.getDeclaredMethod("maskSerial", String.class);
        maskSerialMethod.setAccessible(true);

        String result = (String) maskSerialMethod.invoke(equipmentRepository, "");

        assertEquals("****", result);
    }
}