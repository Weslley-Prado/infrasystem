package br.com.dagostini.infrasystem.equipment.application.usecase;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;

public interface GetEquipmentBySerialUseCase {
    Equipment execute(String serial);
}