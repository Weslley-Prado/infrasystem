package br.com.dagostini.infrasystem.violation.interfaces.mapper;

import br.com.agostini.openapi.provider.representation.ViolationRequestRepresentation;
import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface ViolationDtoMapper {


    Violation toDomain(ViolationRequestRepresentation violationRequestRepresentation);
    ViolationResponseRepresentation toResponse(Violation violation);

    default OffsetDateTime map(Date value) {
        return value == null ? null : value.toInstant().atOffset(ZoneOffset.UTC);
    }

    default Date map(OffsetDateTime value) {
        return value == null ? null : Date.from(value.toInstant());
    }
}
