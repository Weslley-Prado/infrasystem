package br.com.dagostini.infrasystem.violation.domain.service;

import br.com.dagostini.infrasystem.equipment.application.service.EquipmentServiceAdapter;
import br.com.dagostini.infrasystem.violation.application.service.ViolationServiceAdapter;
import br.com.dagostini.infrasystem.violation.domain.exception.ViolationValidationException;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViolationServiceImpl implements ViolationService {
    private final ViolationServiceAdapter violationServiceAdapter;
    private final EquipmentServiceAdapter conectionEquipmentService;

    @Override
    public Violation createViolation(Violation violation) {
        log.info("Creating violation for equipment serial: {}", violation.equipmentSerial());

        Boolean isActive = conectionEquipmentService.isEquipmentActive(violation.equipmentSerial());
        if (!isActive) {
            log.error("Cannot create violation: Equipment {} is inactive", violation.equipmentSerial());
            throw new ViolationValidationException("Cannot create violation for inactive equipment: " + violation.equipmentSerial());
        }
        return violationServiceAdapter.saveViolationRegistry(violation);
    }

    @Override
    public Violation findViolationById(Long id) {
        return violationServiceAdapter.findViolationById(id);
    }

    @Override
    public List<Violation> listViolationsByEquipment(String serial, Date from, Date to) {
        return violationServiceAdapter.listViolationsByEquipment(serial,from,to);
    }
}