package br.com.dagostini.infrasystem.violation.application.usecase;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListViolationsByEquipmentUseCaseImpl implements ListViolationsByEquipmentUseCase{
    private final ViolationService violationService;
    @Override
    public List<Violation> listViolationsByEquipment(String serial, Date from, Date to) {
        return violationService.listViolationsByEquipment(serial, from, to);
    }
}
