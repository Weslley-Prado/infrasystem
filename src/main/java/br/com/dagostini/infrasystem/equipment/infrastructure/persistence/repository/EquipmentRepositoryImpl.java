package br.com.dagostini.infrasystem.equipment.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.equipment.domain.model.Equipment;
import br.com.dagostini.infrasystem.equipment.domain.repository.EquipmentRepository;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.entity.EquipmentEntity;
import br.com.dagostini.infrasystem.equipment.infrastructure.persistence.mapper.EquipmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EquipmentRepositoryImpl implements EquipmentRepository {
    private final EquipmentJpaRepository jpaRepository;
    private final EquipmentMapper mapper;

    @Override
    public Equipment save(Equipment equipment) {
        log.info("Saving equipment with serial: {}", maskSerial(equipment.getSerial()));
        try {
            EquipmentEntity entity = mapper.toEntity(equipment);
            Equipment savedEquipment = mapper.toDomain(jpaRepository.save(entity));
            log.info("Successfully saved equipment with serial: {}", maskSerial(savedEquipment.getSerial()));
            return savedEquipment;
        } catch (Exception ex) {
            log.error("Error saving equipment with serial: {} - {}", maskSerial(equipment.getSerial()), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Equipment> findAll() {
        log.info("Retrieving all equipment records");
        try {
            List<Equipment> equipments = jpaRepository.findAll().stream()
                    .map(mapper::toDomain)
                    .collect(Collectors.toList());
            log.info("Retrieved {} equipment records", equipments.size());
            return equipments;
        } catch (Exception ex) {
            log.error("Error retrieving all equipment records: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Optional<Equipment> findBySerial(String serial) {
        log.info("Finding equipment by serial: {}", maskSerial(serial));
        try {
            Optional<Equipment> equipment = jpaRepository.findBySerial(serial)
                    .map(mapper::toDomain);
            if (equipment.isEmpty()) {
                log.warn("Equipment not found for serial: {}", maskSerial(serial));
            } else {
                log.info("Successfully retrieved equipment for serial: {}", maskSerial(serial));
            }
            return equipment;
        } catch (Exception ex) {
            log.error("Error retrieving equipment for serial: {} - {}", maskSerial(serial), ex.getMessage());
            throw ex;
        }
    }

    private String maskSerial(String serial) {
        if (serial == null || serial.length() < 4) {
            return "****";
        }
        return "****" + serial.substring(serial.length() - 4);
    }
}