package br.com.dagostini.infrasystem.violation.application.usecase;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

public interface FindViolationByIdUseCase {
    Violation findViolationById(Long id);
}
