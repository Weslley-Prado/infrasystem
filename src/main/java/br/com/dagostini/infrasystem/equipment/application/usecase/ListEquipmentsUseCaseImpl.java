package br.com.dagostini.infrasystem.equipment.application.usecase;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.domain.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class ListEquipmentsUseCaseImpl implements ListEquipmentsUseCase {
    private final EquipmentRepository repository;

    public List<Equipment> execute() {
        return repository.findAll();
    }
}
