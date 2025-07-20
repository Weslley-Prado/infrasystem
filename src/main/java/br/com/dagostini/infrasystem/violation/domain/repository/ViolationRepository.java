package br.com.dagostini.infrasystem.violation.domain.repository;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

import java.util.Date;
import java.util.List;

public interface ViolationRepository {
    Violation save(Violation violation);
    Violation findById(Long id);
    List<Violation> findBySerialAndOptionalDateRange(String serial, Date from, Date to);
}
