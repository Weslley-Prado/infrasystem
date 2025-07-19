package br.com.dagostini.infrasystem.equipment.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentJpaRepository extends JpaRepository<EquipmentEntity, Long> {
    Optional<EquipmentEntity> findBySerial(String serial);
}