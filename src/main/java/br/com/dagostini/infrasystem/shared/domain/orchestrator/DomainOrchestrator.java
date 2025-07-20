package br.com.dagostini.infrasystem.shared.domain.orchestrator;

import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface DomainOrchestrator {
    ResponseEntity<List<ViolationResponseRepresentation>> listViolationsByEquipment(String serial, Date from, Date to);
}
