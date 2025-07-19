package br.com.dagostini.infrasystem.equipment.domain.repository;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository {
    Equipment save(Equipment equipment);
    List<Equipment> findAll();
    Optional<Equipment> findBySerial(String serial);
}
