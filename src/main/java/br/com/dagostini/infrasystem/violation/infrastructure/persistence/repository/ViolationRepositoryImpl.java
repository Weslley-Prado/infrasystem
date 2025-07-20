package br.com.dagostini.infrasystem.violation.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.domain.repository.ViolationRepository;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.mapper.ViolationMapper;
import org.springframework.stereotype.Repository;

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
        ViolationEntity entity = mapper.toEntity(violation);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }
}