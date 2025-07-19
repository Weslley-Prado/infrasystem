package br.com.dagostini.infrasystem.equipment.application.usecase;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;

import java.util.List;

public interface ListEquipmentsUseCase {
    List<Equipment> execute();
}