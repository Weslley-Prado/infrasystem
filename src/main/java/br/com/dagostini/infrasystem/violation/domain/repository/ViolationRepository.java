package br.com.dagostini.infrasystem.violation.domain.repository;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

public interface ViolationRepository {
    Violation save(Violation violation);
    Violation findById(Long id);

}
