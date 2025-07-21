package br.com.dagostini.infrasystem.equipment.interfaces.controller;

import br.com.agostini.openapi.provider.api.EquipmentsApi;
import br.com.agostini.openapi.provider.representation.EquipmentRequestRepresentation;
import br.com.agostini.openapi.provider.representation.EquipmentResponseRepresentation;
import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.equipment.application.service.EquipmentServiceAdapterImpl;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentValidationException;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.interfaces.mapper.EquipmentDtoMapper;
import br.com.dagostini.infrasystem.shared.domain.orchestrator.DomainOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EquipmentController implements EquipmentsApi {
    private final EquipmentServiceAdapterImpl equipmentService;
    private final EquipmentDtoMapper mapper;
    private final DomainOrchestrator domainOrchestrator;

    @Override
    public ResponseEntity<EquipmentResponseRepresentation> createEquipment(EquipmentRequestRepresentation equipmentRequestRepresentation) {
        log.info("Received request to create equipment");
        try {
            Equipment equipment = equipmentService.createEquipment(mapper.toDomain(equipmentRequestRepresentation));
            log.info("Successfully created equipment with serial: {}", maskSerial(equipment.getSerial()));
            URI location = URI.create("/equipments/" + equipment.getSerial());
            return ResponseEntity.status(HttpStatus.CREATED).location(location).body(mapper.toResponse(equipment));
        }catch (IllegalArgumentException ex) {
            log.error("Validation failed during equipment creation: {}", ex.getMessage());
            throw new EquipmentValidationException(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<EquipmentResponseRepresentation>> listEquipments() {
        log.info("Received request to list all equipments");
        List<EquipmentResponseRepresentation> responses = equipmentService.listEquipments().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        log.info("Returning {} equipment records", responses.size());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<EquipmentResponseRepresentation> findEquipmentBySerial(String serial) {
        log.info("Received request to find equipment by serial");
        Equipment equipment = equipmentService.getEquipmentBySerial(serial);
        if (equipment == null) {
            log.warn("Equipment not found for serial: {}", maskSerial(serial));
            throw new EquipmentNotFoundException(serial);
        }
        log.info("Successfully retrieved equipment for serial: {}", maskSerial(serial));
        return ResponseEntity.ok(mapper.toResponse(equipment));
    }

    @Override
    public ResponseEntity<List<ViolationResponseRepresentation>> listViolationsByEquipment(String serial, Date from, Date to) {
        log.info("Received request to list violations for equipment from {} to {}", from, to);
        ResponseEntity<List<ViolationResponseRepresentation>> response = domainOrchestrator.listViolationsByEquipment(serial, from, to);
        log.info("Returning {} violation records for equipment", response.getBody() != null ? response.getBody().size() : 0);
        return response;
    }

    private String maskSerial(String serial) {
        if (serial == null || serial.length() < 4) {
            return "****";
        }
        return "****" + serial.substring(serial.length() - 4);
    }
}