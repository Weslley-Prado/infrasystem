package br.com.dagostini.infrasystem.violation.domain.service;

import br.com.dagostini.infrasystem.equipment.application.service.EquipmentServiceAdapter;
import br.com.dagostini.infrasystem.violation.domain.exception.ViolationException;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.repository.ViolationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViolationServiceImpl implements ViolationService {
    private final ViolationRepository violationRepository;
    private final EquipmentServiceAdapter conectionEquipmentService;

    public ViolationServiceImpl(ViolationRepository violationRepository, EquipmentServiceAdapter conectionEquipmentService) {
        this.violationRepository = violationRepository;
        this.conectionEquipmentService = conectionEquipmentService;
    }

    @Override
    public Violation createViolation(Violation violation) {
        log.info("Creating violation for equipment serial: {}", violation.equipmentSerial());

        Boolean isActive = conectionEquipmentService.isEquipmentActive(violation.equipmentSerial());
        if (!isActive) {
            log.error("Cannot create violation: Equipment {} is inactive", violation.equipmentSerial());
            throw new ViolationException("Cannot create violation for inactive equipment: " + violation.equipmentSerial());
        }

        return violationRepository.save(violation);
    }
}
