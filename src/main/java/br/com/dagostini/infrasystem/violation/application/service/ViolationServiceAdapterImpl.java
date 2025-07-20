package br.com.dagostini.infrasystem.violation.application.service;

import br.com.dagostini.infrasystem.violation.application.usecase.CreateViolationUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.ListViolationsByEquipmentUseCase;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolationServiceAdapterImpl implements ViolationServiceAdapter {
    private final CreateViolationUseCase createViolationUseCase;
    private final ListViolationsByEquipmentUseCase listViolationsByEquipmentUseCase;
    private final ViolationService violationService;

    @Override
    public Violation saveViolationRegistry(Violation violation) {
        return createViolationUseCase.execute(violation);
    }

    @Override
    public List<Violation> listViolationsByEquipment(String serial, Date from, Date to) {
        return violationService.listViolationsByEquipment(serial, from, to);
    }
}
