package br.com.dagostini.infrasystem.equipment.application.service;

import br.com.dagostini.infrasystem.equipment.application.usecase.CreateEquipmentUseCase;
import br.com.dagostini.infrasystem.equipment.application.usecase.GetEquipmentBySerialUseCase;
import br.com.dagostini.infrasystem.equipment.application.usecase.ListEquipmentsUseCase;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceAdapterImpl implements EquipmentServiceAdapter {
    private final CreateEquipmentUseCase createEquipmentUseCase;
    private final ListEquipmentsUseCase listEquipmentsUseCase;
    private final GetEquipmentBySerialUseCase getEquipmentBySerialUseCase;

    @Override
    public Equipment createEquipment(Equipment equipment) {
        return createEquipmentUseCase.execute(equipment);
    }
    @Override
    public List<Equipment> listEquipments() {
        return listEquipmentsUseCase.execute();
    }
    @Override
    public Equipment getEquipmentBySerial(String serial) {
        return getEquipmentBySerialUseCase.execute(serial);
    }

    @Override
    public Boolean isEquipmentActive(String serial) {
        var equipment = getEquipmentBySerialUseCase.execute(serial);
        return equipment.getActive();
    }
}