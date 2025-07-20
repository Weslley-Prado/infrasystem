package br.com.dagostini.infrasystem.shared.domain.orchestrator;

import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.violation.application.service.ViolationServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DomainOrchestratorImpl implements DomainOrchestrator{
    private final ViolationServiceAdapter violationServiceAdapter;
    private final DomainOrchestratorMapper domainOrchestratorMapper;

    @Override
    public ResponseEntity<List<ViolationResponseRepresentation>> listViolationsByEquipment(String serial, Date from, Date to) {
        return ResponseEntity.ok(domainOrchestratorMapper.toResponseList(violationServiceAdapter.listViolationsByEquipment(serial, from, to)));

    }
}
