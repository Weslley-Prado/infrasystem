package br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "violation")
@Data
public class ViolationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_serial", nullable = false)
    private String equipmentSerial;

    @Column(name = "occurrence_date_utc", nullable = false)
    private OffsetDateTime occurrenceDateUtc;

    @Column(name = "measured_speed")
    private Double measuredSpeed;

    @Column(name = "considered_speed")
    private Double consideredSpeed;

    @Column(name = "regulated_speed")
    private Double regulatedSpeed;

    @Column(name = "picture", nullable = false)
    private String picture;

    @Column(name = "type", nullable = false)
    private String type;
}