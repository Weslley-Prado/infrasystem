package br.com.dagostini.infrasystem.violation.interfaces.controller;

import br.com.agostini.openapi.provider.api.ViolationsApi;
import br.com.agostini.openapi.provider.representation.ViolationRequestRepresentation;
import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentInactiveException;
import br.com.dagostini.infrasystem.equipment.domain.exception.EquipmentNotFoundException;
import br.com.dagostini.infrasystem.shared.utils.ImageStorageUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.CreateViolationUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.FindViolationByIdUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.ListViolationsByEquipmentUseCase;
import br.com.dagostini.infrasystem.violation.domain.exception.ViolationValidationException;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.interfaces.mapper.ViolationDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ViolationController implements ViolationsApi {

    private final ImageStorageUseCase imageStorageUseCase;

    private final ViolationDtoMapper violationDtoMapper;

    private final CreateViolationUseCase createViolationUseCase;

    private final FindViolationByIdUseCase findViolationByIdUseCase;

    private final ListViolationsByEquipmentUseCase listViolationsByEquipment;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ViolationsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<ViolationResponseRepresentation> createViolation(@RequestPart("violation") ViolationRequestRepresentation violationRequestRepresentation, @RequestPart("picture") MultipartFile picture) {
        log.info("Received request to create violation of type: {}", violationRequestRepresentation.getType());
        try {
            if (violationRequestRepresentation.getType() == ViolationRequestRepresentation.TypeEnum.VELOCITY) {
                if (violationRequestRepresentation.getMeasuredSpeed() == null ||
                        violationRequestRepresentation.getConsideredSpeed() == null ||
                        violationRequestRepresentation.getRegulatedSpeed() == null) {
                    log.error("Validation failed: Missing required speed fields for VELOCITY violation");
                    throw new ViolationValidationException("For VELOCITY type, measuredSpeed, consideredSpeed, and regulatedSpeed are required.");
                }
            }
            log.debug("Storing image for violation");

            String pictureUrl;
            pictureUrl = imageStorageUseCase.storeImage(picture);

            violationRequestRepresentation.setPicture(pictureUrl);

            Violation violation = createViolationUseCase.execute(violationDtoMapper.toDomain(violationRequestRepresentation));
            URI location = URI.create("/violations/" + violation.id());
            log.info("Successfully created violation with ID: {}", maskViolationId(violation.id()));

            return ResponseEntity
                    .created(location)
                    .body(violationDtoMapper.toResponse(violation));
        } catch (ViolationValidationException ex) {
            log.error("Validation error during violation creation: {}", ex.getMessage());
            throw ex;
        } catch (EquipmentNotFoundException | EquipmentInactiveException ex) {
            log.warn("Equipment-related error during violation creation: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during violation creation: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ResponseEntity<ViolationResponseRepresentation> findViolationById(Long id) {
        log.info("Received request to find violation by ID: {}", maskViolationId(id));
        try {
            Violation violation = findViolationByIdUseCase.findViolationById(id);
            log.info("Successfully retrieved violation for ID: {}", maskViolationId(id));
            return ResponseEntity.status(HttpStatus.OK).body(violationDtoMapper.toResponse(violation));
        } catch (Exception ex) {
            log.error("Error retrieving violation for ID: {} - {}", maskViolationId(id), ex.getMessage());
            throw ex;
        }
    }

    private String maskViolationId(Long id) {
        if (id == null) {
            return "****";
        }
        String idStr = id.toString();
        if (idStr.length() < 4) {
            return "****";
        }
        return "****" + idStr.substring(idStr.length() - 4);
    }
}