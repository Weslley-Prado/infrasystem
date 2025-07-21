package br.com.dagostini.infrasystem.equipment.application.usecase;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.domain.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListEquipmentsUseCaseImplTest {

    @Mock
    private EquipmentRepository repository;

    @InjectMocks
    private ListEquipmentsUseCaseImpl listEquipmentsUseCase;

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
    void execute_shouldReturnEquipmentList() {
        List<Equipment> expectedEquipments = Collections.singletonList(testEquipment);
        when(repository.findAll()).thenReturn(expectedEquipments);

        List<Equipment> result = listEquipmentsUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEquipment, result.get(0));
        assertEquals(TEST_ID, result.get(0).getId());
        assertEquals(TEST_SERIAL, result.get(0).getSerial());
        assertEquals(TEST_MODEL, result.get(0).getModel());
        assertEquals(TEST_ADDRESS, result.get(0).getAddress());
        assertEquals(TEST_LATITUDE, result.get(0).getLatitude());
        assertEquals(TEST_LONGITUDE, result.get(0).getLongitude());
        assertEquals(TEST_ACTIVE, result.get(0).getActive());
        verify(repository, times(1)).findAll();
    }

    @Test
    void execute_shouldReturnEmptyListWhenNoEquipmentsFound() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Equipment> result = listEquipmentsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void execute_shouldThrowExceptionWhenRepositoryFails() {
        RuntimeException exception = new RuntimeException("Database error");
        when(repository.findAll()).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                listEquipmentsUseCase.execute());
        assertEquals("Database error", thrown.getMessage());
        verify(repository, times(1)).findAll();
    }
}