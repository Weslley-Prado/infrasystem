package br.com.dagostini.infrasystem.violation.infrastructure.persistence.mapper;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViolationMapper {
    ViolationEntity toEntity(Violation violation);
    Violation toDomain(ViolationEntity entity);
    List<Violation> toDomainList(List<ViolationEntity> violationEntityList);
}
