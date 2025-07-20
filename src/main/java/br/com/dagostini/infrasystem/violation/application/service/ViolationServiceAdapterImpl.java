package br.com.dagostini.infrasystem.violation.application.service;

import br.com.dagostini.infrasystem.violation.application.usecase.CreateViolationUseCase;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViolationServiceAdapterImpl implements ViolationServiceAdapter {
    private final CreateViolationUseCase createViolationUseCase;

    @Override
    public Violation saveViolationRegistry(Violation violation) {
        return createViolationUseCase.execute(violation);
    }
}
