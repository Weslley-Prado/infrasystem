package br.com.dagostini.infrasystem.violation.interfaces.controller;

import br.com.agostini.openapi.provider.api.ViolationsApi;
import br.com.agostini.openapi.provider.representation.ViolationRequestRepresentation;
import br.com.agostini.openapi.provider.representation.ViolationResponseRepresentation;
import br.com.dagostini.infrasystem.shared.utils.ImageStorageUseCase;
import br.com.dagostini.infrasystem.violation.application.usecase.CreateViolationUseCase;
import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.interfaces.mapper.ViolationDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ViolationController implements ViolationsApi {

    private final ImageStorageUseCase imageStorageUseCase;

    private final ViolationDtoMapper violationDtoMapper;

    private final CreateViolationUseCase createViolationUseCase;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ViolationsApi.super.getRequest();
    }

    @Override
//    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ViolationResponseRepresentation> createViolation(@RequestPart("violation") ViolationRequestRepresentation violationRequestRepresentation, @RequestPart("picture") MultipartFile picture) {
        if (violationRequestRepresentation.getType() == ViolationRequestRepresentation.TypeEnum.VELOCITY) {
            if (violationRequestRepresentation.getMeasuredSpeed() == null ||
                    violationRequestRepresentation.getConsideredSpeed() == null ||
                    violationRequestRepresentation.getRegulatedSpeed() == null) {
                throw new IllegalArgumentException("measuredSpeed, consideredSpeed, and regulatedSpeed are required for VELOCITY type");
            }
        }

        String pictureUrl;
        pictureUrl = imageStorageUseCase.storeImage(picture);
        violationRequestRepresentation.setPicture(pictureUrl);

        Violation violation = createViolationUseCase.execute(violationDtoMapper.toDomain(violationRequestRepresentation));
        return ResponseEntity.status(HttpStatus.CREATED).body(violationDtoMapper.toResponse(violation));
    }

    @Override
    public ResponseEntity<ViolationResponseRepresentation> findViolationById(Long id) {
        return ViolationsApi.super.findViolationById(id);
    }
}
