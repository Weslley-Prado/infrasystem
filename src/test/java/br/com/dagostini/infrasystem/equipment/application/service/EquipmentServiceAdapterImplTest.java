package br.com.dagostini.infrasystem.equipment.application.service;

import br.com.dagostini.infrasystem.equipment.application.usecase.CreateEquipmentUseCase;
import br.com.dagostini.infrasystem.equipment.application.usecase.GetEquipmentBySerialUseCase;
import br.com.dagostini.infrasystem.equipment.application.usecase.ListEquipmentsUseCase;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
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
class EquipmentServiceAdapterImplTest {

    @Mock
    private CreateEquipmentUseCase createEquipmentUseCase;

    @Mock
    private ListEquipmentsUseCase listEquipmentsUseCase;

    @Mock
    private GetEquipmentBySerialUseCase getEquipmentBySerialUseCase;

    @InjectMocks
    private EquipmentServiceAdapterImpl equipmentServiceAdapter;

    private Equipment testEquipment;
    private static final String TEST_SERIAL = "12345";

    @BeforeEach
    void setUp() {
        testEquipment = new Equipment();
        testEquipment.setSerial(TEST_SERIAL);
        testEquipment.setActive(true);
    }

    @Test
    void createEquipment_shouldReturnCreatedEquipment() {

        when(createEquipmentUseCase.execute(testEquipment)).thenReturn(testEquipment);
        Equipment result = equipmentServiceAdapter.createEquipment(testEquipment);
        assertNotNull(result);
        assertEquals(testEquipment, result);
        verify(createEquipmentUseCase, times(1)).execute(testEquipment);
    }

    @Test
    void listEquipments_shouldReturnEquipmentList() {
        List<Equipment> expectedList = Collections.singletonList(testEquipment);
        when(listEquipmentsUseCase.execute()).thenReturn(expectedList);
        List<Equipment> result = equipmentServiceAdapter.listEquipments();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEquipment, result.get(0));
        verify(listEquipmentsUseCase, times(1)).execute();
    }

    @Test
    void getEquipmentBySerial_shouldReturnEquipment() {
        when(getEquipmentBySerialUseCase.execute(TEST_SERIAL)).thenReturn(testEquipment);
        Equipment result = equipmentServiceAdapter.getEquipmentBySerial(TEST_SERIAL);
        assertNotNull(result);
        assertEquals(testEquipment, result);
        assertEquals(TEST_SERIAL, result.getSerial());
        verify(getEquipmentBySerialUseCase, times(1)).execute(TEST_SERIAL);
    }

    @Test
    void isEquipmentActive_shouldReturnTrueWhenEquipmentIsActive() {
        when(getEquipmentBySerialUseCase.execute(TEST_SERIAL)).thenReturn(testEquipment);
        Boolean result = equipmentServiceAdapter.isEquipmentActive(TEST_SERIAL);
        assertTrue(result);
        verify(getEquipmentBySerialUseCase, times(1)).execute(TEST_SERIAL);
    }

    @Test
    void isEquipmentActive_shouldReturnFalseWhenEquipmentIsInactive() {
        testEquipment.setActive(false);
        when(getEquipmentBySerialUseCase.execute(TEST_SERIAL)).thenReturn(testEquipment);
        Boolean result = equipmentServiceAdapter.isEquipmentActive(TEST_SERIAL);
        assertFalse(result);
        verify(getEquipmentBySerialUseCase, times(1)).execute(TEST_SERIAL);
    }

    @Test
    void getEquipmentBySerial_shouldThrowExceptionWhenEquipmentNotFound() {
        when(getEquipmentBySerialUseCase.execute(TEST_SERIAL))
                .thenThrow(new RuntimeException("Equipment not found"));
        assertThrows(RuntimeException.class, () ->
                equipmentServiceAdapter.getEquipmentBySerial(TEST_SERIAL));
        verify(getEquipmentBySerialUseCase, times(1)).execute(TEST_SERIAL);
    }
}