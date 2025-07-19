package br.com.dagostini.infrasystem.equipment.application.usecase;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.domain.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateEquipmentUseCaseImpl implements  CreateEquipmentUseCase {
    private final EquipmentRepository repository;

    @Override
    public Equipment execute(Equipment equipment) {
        return repository.save(equipment);
    }
}
