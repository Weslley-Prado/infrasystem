package br.com.dagostini.infrasystem.equipment.application.service;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;

import java.util.List;

public interface EquipmentServiceAdapter {
    Equipment createEquipment(Equipment equipment);
    List<Equipment> listEquipments();
    Equipment getEquipmentBySerial(String serial);
    Boolean isEquipmentActive(String serial);
}