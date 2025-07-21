package br.com.dagostini.infrasystem.equipment.interfaces.controller;

import br.com.agostini.openapi.provider.representation.EquipmentRequestRepresentation;
import br.com.agostini.openapi.provider.representation.EquipmentResponseRepresentation;
import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.equipment.application.service.EquipmentServiceAdapterImpl;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentValidationException;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.interfaces.mapper.EquipmentDtoMapper;
import br.com.dagostini.infrasystem.shared.domain.orchestrator.DomainOrchestrator;
import br.com.dagostini.infrasystem.violation.application.service.ViolationServiceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentControllerTest {
    @InjectMocks
    private EquipmentController controller;

    @Mock
    private EquipmentServiceAdapterImpl equipmentService;

    @Mock
    private EquipmentDtoMapper mapper;

    @Mock
    private DomainOrchestrator domainOrchestrator;

    @Test
    void shouldCreateEquipmentSuccessfully() {
        EquipmentRequestRepresentation request = new EquipmentRequestRepresentation();
        request.setSerial("ABC123f4d56");
        request.setModel("Modelo-X");
        request.setAddress("Rua das Flores, 1028");
        request.setLatitude(BigDecimal.valueOf(-23.55052));
        request.setLongitude(BigDecimal.valueOf(-46.63331));
        request.setActive(true);

        Equipment equipment = new Equipment();
        equipment.setSerial("ABC123f4d56");
        equipment.setModel("Modelo-X");
        equipment.setAddress("Rua das Flores, 1028");
        equipment.setLatitude(-23.55052);
        equipment.setLongitude(-46.63331);
        equipment.setActive(true);

        EquipmentResponseRepresentation response = new EquipmentResponseRepresentation();
        response.setSerial("ABC123f4d56");
        response.setModel("Modelo-X");
        response.setAddress("Rua das Flores, 1028");
        response.setLatitude(BigDecimal.valueOf(-23.55052));
        response.setLongitude(BigDecimal.valueOf(-46.63331));
        response.setActive(true);

        when(mapper.toDomain(any())).thenReturn(equipment);
        when(equipmentService.createEquipment(any())).thenReturn(equipment);
        when(mapper.toResponse(any())).thenReturn(response);
        ResponseEntity<EquipmentResponseRepresentation> result = controller.createEquipment(request);
        assertEquals(201, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
        verify(equipmentService).createEquipment(equipment);
    }

    @Test
    void shouldFindEquipmentBySerial() {
        String serial = "ABC123f4d56";

        Equipment equipment = new Equipment();
        equipment.setSerial(serial);

        EquipmentResponseRepresentation response = new EquipmentResponseRepresentation();
        response.setSerial(serial);
        response.setModel("Modelo-X");
        response.setAddress("Rua das Flores, 1028");
        response.setLatitude(BigDecimal.valueOf(-23.55052));
        response.setLongitude(BigDecimal.valueOf(-46.63331));
        response.setActive(true);

        when(equipmentService.getEquipmentBySerial(serial)).thenReturn(equipment);
        when(mapper.toResponse(equipment)).thenReturn(response);

        ResponseEntity<EquipmentResponseRepresentation> result = controller.findEquipmentBySerial(serial);

        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals(serial, result.getBody().getSerial());
        verify(equipmentService).getEquipmentBySerial(serial);
        verify(mapper).toResponse(equipment);
    }

    @Test
    void shouldThrowWhenEquipmentNotFound() {
        String serial = "ABC123";
        assertThrows(EquipmentNotFoundException.class, () -> controller.findEquipmentBySerial(serial));
    }

    @Test
    void shouldListViolationsByEquipment() {
        String serial = "ABC123f4d56";
        Date from = new Date(System.currentTimeMillis() - 1000000);
        Date to = new Date();

        ViolationResponseRepresentation v1 = new ViolationResponseRepresentation();
        v1.setId(1L);

        ViolationResponseRepresentation v2 = new ViolationResponseRepresentation();
        v2.setId(2L);

        List<ViolationResponseRepresentation> violations = Arrays.asList(v1, v2);
        ResponseEntity<List<ViolationResponseRepresentation>> response = ResponseEntity.ok(violations);

        when(domainOrchestrator.listViolationsByEquipment(serial, from, to)).thenReturn(response);

        ResponseEntity<List<ViolationResponseRepresentation>> result = controller.listViolationsByEquipment(serial, from, to);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());
        assertEquals(1, result.getBody().get(0).getId());
        assertEquals(2, result.getBody().get(1).getId());

        verify(domainOrchestrator).listViolationsByEquipment(serial, from, to);
    }

    @Test
    void shouldThrowEquipmentValidationExceptionOnIllegalArgument() {
        EquipmentRequestRepresentation request = new EquipmentRequestRepresentation();
        request.setSerial("INVALID");

        when(mapper.toDomain(request)).thenThrow(new IllegalArgumentException("Invalid serial format"));
        EquipmentValidationException exception = assertThrows(
                EquipmentValidationException.class,
                () -> controller.createEquipment(request)
        );
        assertEquals("Invalid serial format", exception.getMessage());
        verify(mapper).toDomain(request);
        verifyNoInteractions(equipmentService);
    }

    @Test
    void shouldListAllEquipmentsSuccessfully() {
        Equipment e1 = new Equipment();
        e1.setSerial("A123");
        Equipment e2 = new Equipment();
        e2.setSerial("B456");
        List<Equipment> equipmentList = Arrays.asList(e1, e2);
        EquipmentResponseRepresentation r1 = new EquipmentResponseRepresentation();
        r1.setSerial("A123");
        EquipmentResponseRepresentation r2 = new EquipmentResponseRepresentation();
        r2.setSerial("B456");

        when(equipmentService.listEquipments()).thenReturn(equipmentList);
        when(mapper.toResponse(e1)).thenReturn(r1);
        when(mapper.toResponse(e2)).thenReturn(r2);

        ResponseEntity<List<EquipmentResponseRepresentation>> result = controller.listEquipments();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());
        assertEquals("A123", result.getBody().get(0).getSerial());
        assertEquals("B456", result.getBody().get(1).getSerial());

        verify(equipmentService).listEquipments();
        verify(mapper).toResponse(e1);
        verify(mapper).toResponse(e2);
    }
}