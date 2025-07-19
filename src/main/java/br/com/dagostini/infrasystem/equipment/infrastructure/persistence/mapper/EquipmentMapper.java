package br.com.dagostini.infrasystem.equipment.infrastructure.persistence.mapper;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.entity.EquipmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
    EquipmentEntity toEntity(Equipment equipment);
    Equipment toDomain(EquipmentEntity entity);
}