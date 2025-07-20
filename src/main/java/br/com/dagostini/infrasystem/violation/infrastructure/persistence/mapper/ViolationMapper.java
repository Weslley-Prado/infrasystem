package br.com.dagostini.infrasystem.violation.infrastructure.persistence.mapper;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ViolationMapper {
    ViolationEntity toEntity(Violation violation);
    Violation toDomain(ViolationEntity entity);
}
