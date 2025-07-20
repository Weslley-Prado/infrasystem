package br.com.dagostini.infrasystem.violation.domain.service;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

public interface ViolationService {
    Violation createViolation(Violation violation);
}
