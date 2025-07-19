package br.com.dagostini.infrasystem.equipment.application.usecase;

import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.domain.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetEquipmentBySerialUseCaseImpl implements GetEquipmentBySerialUseCase{
    private final EquipmentRepository repository;

    @Override
    public Equipment execute(String serial) {
        return repository.findBySerial(serial)
                .orElseThrow(() -> new EquipmentNotFoundException("Equipment not found for serial: " + serial));
    }
}