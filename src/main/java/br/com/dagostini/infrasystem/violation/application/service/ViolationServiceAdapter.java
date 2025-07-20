package br.com.dagostini.infrasystem.violation.application.service;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

public interface ViolationServiceAdapter {
    Violation saveViolationRegistry(Violation violation);
}
