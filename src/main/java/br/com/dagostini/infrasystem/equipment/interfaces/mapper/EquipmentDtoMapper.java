package br.com.dagostini.infrasystem.equipment.interfaces.mapper;

import br.com.agostini.openapi.provider.representation.EquipmentRequestRepresentation;
import br.com.agostini.openapi.provider.representation.EquipmentResponseRepresentation;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentDtoMapper {
    Equipment toDomain(EquipmentRequestRepresentation equipmentRequestRepresentation);
    EquipmentResponseRepresentation toResponse(Equipment equipment);
}