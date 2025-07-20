package br.com.dagostini.infrasystem.violation.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ViolationJpaRepository extends JpaRepository<ViolationEntity, Long> {
    @Query(value = """
    SELECT * FROM violation v
    WHERE v.equipment_serial = :serial
      AND v.occurrence_date_utc >= :from
      AND v.occurrence_date_utc <= :to
""", nativeQuery = true)
    List<ViolationEntity> findBySerialAndOptionalDateRange(
            @Param("serial") String serial,
            @Param("from") Date from,
            @Param("to") Date to
    );

    @Query(value = """
    SELECT * FROM violation v
    WHERE v.equipment_serial = :serial
""", nativeQuery = true)
    List<ViolationEntity> findBySerialAndOptional(
            @Param("serial") String serial
    );
}