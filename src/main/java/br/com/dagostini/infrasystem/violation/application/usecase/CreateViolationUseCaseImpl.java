package br.com.dagostini.infrasystem.violation.application.usecase;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.service.ViolationService;
import org.springframework.stereotype.Service;

@Service
public class CreateViolationUseCaseImpl implements CreateViolationUseCase {

    private final ViolationService violationService;
    public CreateViolationUseCaseImpl(ViolationService violationService) {
        this.violationService = violationService;
    }

    @Override
    public Violation execute(Violation violation) {
        return violationService.createViolation(violation);
    }
}