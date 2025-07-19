package br.com.dagostini.infrasystem.equipment.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.domain.repository.EquipmentRepository;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.entity.EquipmentEntity;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.mapper.EquipmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EquipmentRepositoryImpl implements EquipmentRepository {
    private final EquipmentJpaRepository jpaRepository;
    private final EquipmentMapper mapper;

    @Override
    public Equipment save(Equipment equipment) {
        EquipmentEntity entity = mapper.toEntity(equipment);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<Equipment> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Equipment> findBySerial(String serial) {
        return jpaRepository.findBySerial(serial)
                .map(mapper::toDomain);
    }
}