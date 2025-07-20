package br.com.dagostini.infrasystem.violation.domain.service;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

import java.util.Date;
import java.util.List;

public interface ViolationService {
    Violation createViolation(Violation violation);
    Violation findViolationById(Long id);
    List<Violation> listViolationsByEquipment(String serial, Date from, Date to);
}
