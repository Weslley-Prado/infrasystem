package br.com.dagostini.infrasystem.equipment.application.usecase;

import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.domain.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEquipmentBySerialUseCaseImplTest {

    @Mock
    private EquipmentRepository repository;

    @InjectMocks
    private GetEquipmentBySerialUseCaseImpl getEquipmentBySerialUseCase;

    private Equipment testEquipment;
    private static final Long TEST_ID = 1L;
    private static final String TEST_SERIAL = "ABC12345";
    private static final String TEST_MODEL = "TestModel";
    private static final String TEST_ADDRESS = "123 Test Street";
    private static final Double TEST_LATITUDE = 40.7128;
    private static final Double TEST_LONGITUDE = -74.0060;
    private static final Boolean TEST_ACTIVE = true;

    @BeforeEach
    void setUp() {
        testEquipment = new Equipment();
        testEquipment.setId(TEST_ID);
        testEquipment.setSerial(TEST_SERIAL);
        testEquipment.setModel(TEST_MODEL);
        testEquipment.setAddress(TEST_ADDRESS);
        testEquipment.setLatitude(TEST_LATITUDE);
        testEquipment.setLongitude(TEST_LONGITUDE);
        testEquipment.setActive(TEST_ACTIVE);
    }

    @Test
    void execute_shouldReturnEquipmentWhenFound() {
        when(repository.findBySerial(TEST_SERIAL)).thenReturn(Optional.of(testEquipment));

        Equipment result = getEquipmentBySerialUseCase.execute(TEST_SERIAL);

        assertNotNull(result);
        assertEquals(testEquipment, result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_SERIAL, result.getSerial());
        assertEquals(TEST_MODEL, result.getModel());
        assertEquals(TEST_ADDRESS, result.getAddress());
        assertEquals(TEST_LATITUDE, result.getLatitude());
        assertEquals(TEST_LONGITUDE, result.getLongitude());
        assertEquals(TEST_ACTIVE, result.getActive());
        verify(repository, times(1)).findBySerial(TEST_SERIAL);
    }

    @Test
    void execute_shouldThrowEquipmentNotFoundExceptionWhenNotFound() {
        when(repository.findBySerial(TEST_SERIAL)).thenReturn(Optional.empty());

        EquipmentNotFoundException thrown = assertThrows(EquipmentNotFoundException.class, () ->
                getEquipmentBySerialUseCase.execute(TEST_SERIAL));
        assertEquals("Equipment with serial Equipment not found for serial: " + TEST_SERIAL + " not found.", thrown.getMessage());
        verify(repository, times(1)).findBySerial(TEST_SERIAL);
    }

    @Test
    void execute_shouldThrowExceptionWhenRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(repository.findBySerial(TEST_SERIAL)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                getEquipmentBySerialUseCase.execute(TEST_SERIAL));
        assertEquals("Database error", thrown.getMessage());
        verify(repository, times(1)).findBySerial(TEST_SERIAL);
    }

    @Test
    void execute_shouldHandleNullSerial() {
        when(repository.findBySerial(null)).thenReturn(Optional.empty());

        EquipmentNotFoundException thrown = assertThrows(EquipmentNotFoundException.class, () ->
                getEquipmentBySerialUseCase.execute(null));
        assertEquals("Equipment with serial Equipment not found for serial: null not found.", thrown.getMessage());
        verify(repository, times(1)).findBySerial(null);
    }
}