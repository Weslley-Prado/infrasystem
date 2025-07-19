package br.com.dagostini.infrasystem.violation.domain.model;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record Violation(
        Long id,
        String equipmentSerial,
        OffsetDateTime occurrenceDateUtc,
        Double measuredSpeed,
        Double consideredSpeed,
        Double regulatedSpeed,
        String picture,
        String type
) {
}