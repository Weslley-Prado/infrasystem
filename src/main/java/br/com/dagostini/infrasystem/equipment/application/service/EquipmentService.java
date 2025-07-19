package br.com.dagostini.infrasystem.equipment.application.service;

import br.com.dagostini.infrasystem.equipment.application.usecase.CreateEquipmentUseCase;
import br.com.dagostini.infrasystem.equipment.application.usecase.GetEquipmentBySerialUseCase;
import br.com.dagostini.infrasystem.equipment.application.usecase.ListEquipmentsUseCase;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    private final CreateEquipmentUseCase createEquipmentUseCase;
    private final ListEquipmentsUseCase listEquipmentsUseCase;
    private final GetEquipmentBySerialUseCase getEquipmentBySerialUseCase;

    public EquipmentService(CreateEquipmentUseCase createEquipmentUseCase,
                            ListEquipmentsUseCase listEquipmentsUseCase,
                            GetEquipmentBySerialUseCase getEquipmentBySerialUseCase) {
        this.createEquipmentUseCase = createEquipmentUseCase;
        this.listEquipmentsUseCase = listEquipmentsUseCase;
        this.getEquipmentBySerialUseCase = getEquipmentBySerialUseCase;
    }

    public Equipment createEquipment(Equipment equipment) {
        return createEquipmentUseCase.execute(equipment);
    }

    public List<Equipment> listEquipments() {
        return listEquipmentsUseCase.execute();
    }

    public Equipment getEquipmentBySerial(String serial) {
        return getEquipmentBySerialUseCase.execute(serial);
    }
}