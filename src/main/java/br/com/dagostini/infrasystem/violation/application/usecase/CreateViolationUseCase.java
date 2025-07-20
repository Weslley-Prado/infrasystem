package br.com.dagostini.infrasystem.violation.application.usecase;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

public interface CreateViolationUseCase {
    Violation execute(Violation violation);
}
