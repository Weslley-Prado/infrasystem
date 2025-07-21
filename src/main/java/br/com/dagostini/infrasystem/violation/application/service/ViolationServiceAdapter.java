package br.com.dagostini.infrasystem.violation.application.service;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

import java.util.Date;
import java.util.List;

public interface ViolationServiceAdapter {
    Violation saveViolationRegistry(Violation violation);
    Violation findViolationById(Long id);
    List<Violation> listViolationsByEquipment(String serial, Date from, Date to);
}
