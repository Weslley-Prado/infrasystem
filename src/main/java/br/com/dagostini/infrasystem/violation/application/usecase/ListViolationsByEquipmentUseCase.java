package br.com.dagostini.infrasystem.violation.application.usecase;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;

import java.util.Date;
import java.util.List;

public interface ListViolationsByEquipmentUseCase {
    List<Violation> listViolationsByEquipment(String serial, Date from, Date to);
}
