package br.com.dagostini.infrasystem.violation.application.service;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.repository.ViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolationServiceAdapterImpl implements ViolationServiceAdapter {
    private final ViolationRepository violationRepository;


    @Override
    public Violation saveViolationRegistry(Violation violation) {
        return violationRepository.save(violation);
    }

    @Override
    public Violation findViolationById(Long id) {
        return violationRepository.findById(id);
    }

    @Override
    public List<Violation> listViolationsByEquipment(String serial, Date from, Date to) {
        return violationRepository.findBySerialAndOptionalDateRange(serial, from, to);
    }
}