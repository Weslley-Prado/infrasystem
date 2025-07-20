package br.com.dagostini.infrasystem.violation.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViolationJpaRepository extends JpaRepository<ViolationEntity, Long> {
}