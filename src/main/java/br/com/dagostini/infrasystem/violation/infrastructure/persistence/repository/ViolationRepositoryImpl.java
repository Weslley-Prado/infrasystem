package br.com.dagostini.infrasystem.violation.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.repository.ViolationRepository;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.mapper.ViolationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class ViolationRepositoryImpl implements ViolationRepository {

    private final ViolationJpaRepository jpaRepository;
    private final ViolationMapper mapper;

    public ViolationRepositoryImpl(ViolationJpaRepository jpaRepository, ViolationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Violation save(Violation violation) {
        log.info("Saving violation with ID: {}", maskViolationId(violation.id()));
        try {
            ViolationEntity entity = mapper.toEntity(violation);
            entity = jpaRepository.save(entity);
            log.info("Successfully saved violation with ID: {}", maskViolationId(entity.getId()));
            return mapper.toDomain(entity);
        } catch (Exception ex) {
            log.error("Error saving violation with ID: {} - {}", maskViolationId(violation.id()), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Violation findById(Long id) {
        log.info("Finding violation by ID: {}", maskViolationId(id));
        try {
            Violation violation = mapper.toDomain(jpaRepository.getReferenceById(id));
            if (violation == null) {
                log.warn("Violation not found for ID: {}", maskViolationId(id));
            } else {
                log.info("Successfully retrieved violation for ID: {}", maskViolationId(id));
            }
            return violation;
        } catch (Exception ex) {
            log.error("Error retrieving violation for ID: {} - {}", maskViolationId(id), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Violation> findBySerialAndOptionalDateRange(String serial, Date from, Date to) {
        log.info("Finding violations for serial: {} with date range from {} to {}", maskSerial(serial), from, to);
        try {
            List<Violation> violations;
            if (Objects.isNull(from) && Objects.isNull(to)) {
                violations = mapper.toDomainList(jpaRepository.findBySerialAndOptional(serial));
            } else {
                violations = mapper.toDomainList(jpaRepository.findBySerialAndOptionalDateRange(serial, from, to));
            }
            log.info("Retrieved {} violations for serial: {}", violations.size(), maskSerial(serial));
            return violations;
        } catch (Exception ex) {
            log.error("Error retrieving violations for serial: {} - {}", maskSerial(serial), ex.getMessage());
            throw ex;
        }
    }

    private String maskSerial(String serial) {
        if (serial == null || serial.length() < 4) {
            return "****";
        }
        return "****" + serial.substring(serial.length() - 4);
    }

    private String maskViolationId(Long id) {
        if (id == null) {
            return "****";
        }
        String idStr = id.toString();
        if (idStr.length() < 4) {
            return "****";
        }
        return "****" + idStr.substring(idStr.length() - 4);
    }
}