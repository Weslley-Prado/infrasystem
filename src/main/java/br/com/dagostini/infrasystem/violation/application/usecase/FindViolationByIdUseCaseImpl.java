package br.com.dagostini.infrasystem.violation.application.usecase;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.service.ViolationService;
import org.springframework.stereotype.Service;

@Service
public class FindViolationByIdUseCaseImpl implements FindViolationByIdUseCase{
    private final ViolationService violationService;
    public FindViolationByIdUseCaseImpl(ViolationService violationService) {
        this.violationService = violationService;
    }

    @Override
    public Violation findViolationById(Long id) {
        return violationService.findViolationById(id);
    }
}
