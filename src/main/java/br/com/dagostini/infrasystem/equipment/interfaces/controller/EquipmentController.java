package br.com.dagostini.infrasystem.equipment.interfaces.controller;

import br.com.agostini.openapi.provider.api.EquipmentsApi;
import br.com.agostini.openapi.provider.representation.EquipmentRequestRepresentation;
import br.com.agostini.openapi.provider.representation.EquipmentResponseRepresentation;
import br.com.dagostini.infrasystem.equipment.application.service.EquipmentServiceAdapterImpl;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.interfaces.mapper.EquipmentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EquipmentController implements EquipmentsApi {
    private final EquipmentServiceAdapterImpl equipmentService;
    private final EquipmentDtoMapper mapper;

    @Override
    public ResponseEntity<EquipmentResponseRepresentation> createEquipment(EquipmentRequestRepresentation equipmentRequestRepresentation) {
        Equipment equipment = equipmentService.createEquipment(mapper.toDomain(equipmentRequestRepresentation));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(equipment));
    }

    @Override
    public ResponseEntity<List<EquipmentResponseRepresentation>> listEquipments() {
        List<EquipmentResponseRepresentation> responses = equipmentService.listEquipments().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<EquipmentResponseRepresentation> findEquipmentBySerial(String serial) {
        Equipment equipment = equipmentService.getEquipmentBySerial(serial);
        return ResponseEntity.ok(mapper.toResponse(equipment));
    }
}