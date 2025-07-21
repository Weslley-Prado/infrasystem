package br.com.dagostini.infrasystem.violation.infrastructure.persistence.repository;

import br.com.dagostini.infrasystem.violation.domain.model.Violation;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.entity.ViolationEntity;
import br.com.dagostini.infrasystem.violation.infrastructure.persistence.mapper.ViolationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViolationRepositoryImplTest {

    @Mock
    private ViolationJpaRepository jpaRepository;

    @Mock
    private ViolationMapper mapper;

    @InjectMocks
    private ViolationRepositoryImpl violationRepository;

    private Violation violationDomain;
    private ViolationEntity violationEntity;

    @BeforeEach
    void setup() {
        violationDomain = mock(Violation.class);
        violationEntity = mock(ViolationEntity.class);
    }

    @Test
    void save_ShouldSaveAndReturnDomain() {
        when(mapper.toEntity(violationDomain)).thenReturn(violationEntity);
        when(jpaRepository.save(violationEntity)).thenReturn(violationEntity);
        when(mapper.toDomain(violationEntity)).thenReturn(violationDomain);

        Violation saved = violationRepository.save(violationDomain);

        assertNotNull(saved);
        verify(jpaRepository).save(violationEntity);
        verify(mapper).toEntity(violationDomain);
        verify(mapper).toDomain(violationEntity);
    }

    @Test
    void findById_ShouldReturnDomain_WhenEntityExists() {
        when(jpaRepository.getReferenceById(12345L)).thenReturn(violationEntity);
        when(mapper.toDomain(violationEntity)).thenReturn(violationDomain);

        Violation found = violationRepository.findById(12345L);

        assertNotNull(found);
        assertEquals(violationDomain, found);
        verify(jpaRepository).getReferenceById(12345L);
        verify(mapper).toDomain(violationEntity);
    }

    @Test
    void findById_ShouldThrowException_WhenJpaThrows() {
        when(jpaRepository.getReferenceById(12345L)).thenThrow(new RuntimeException("DB error"));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> violationRepository.findById(12345L));

        assertEquals("DB error", thrown.getMessage());
        verify(jpaRepository).getReferenceById(12345L);
    }

    @Test
    void findBySerialAndOptionalDateRange_ShouldCallFindBySerial_WhenDatesNull() {
        List<ViolationEntity> entities = List.of(violationEntity);
        List<Violation> domainList = List.of(violationDomain);

        when(jpaRepository.findBySerialAndOptional("serial123")).thenReturn(entities);
        when(mapper.toDomainList(entities)).thenReturn(domainList);

        List<Violation> result = violationRepository.findBySerialAndOptionalDateRange("serial123", null, null);

        assertEquals(1, result.size());
        verify(jpaRepository).findBySerialAndOptional("serial123");
        verify(mapper).toDomainList(entities);
    }

    @Test
    void findBySerialAndOptionalDateRange_ShouldCallFindBySerialAndDateRange_WhenDatesPresent() {
        List<ViolationEntity> entities = List.of(violationEntity);
        List<Violation> domainList = List.of(violationDomain);
        Date from = new Date();
        Date to = new Date();

        when(jpaRepository.findBySerialAndOptionalDateRange("serial123", from, to)).thenReturn(entities);
        when(mapper.toDomainList(entities)).thenReturn(domainList);

        List<Violation> result = violationRepository.findBySerialAndOptionalDateRange("serial123", from, to);

        assertEquals(1, result.size());
        verify(jpaRepository).findBySerialAndOptionalDateRange("serial123", from, to);
        verify(mapper).toDomainList(entities);
    }

    @Test
    void save_ShouldThrowException_WhenJpaSaveFails() {
        when(mapper.toEntity(violationDomain)).thenReturn(violationEntity);
        when(jpaRepository.save(violationEntity)).thenThrow(new RuntimeException("DB save error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> violationRepository.save(violationDomain));

        assertEquals("DB save error", ex.getMessage());
        verify(jpaRepository).save(violationEntity);
        verify(mapper).toEntity(violationDomain);
    }

    @Test
    void findById_ShouldThrowException_WhenJpaGetReferenceFails() {
        when(jpaRepository.getReferenceById(12345L)).thenThrow(new RuntimeException("DB find error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> violationRepository.findById(12345L));

        assertEquals("DB find error", ex.getMessage());
        verify(jpaRepository).getReferenceById(12345L);
    }

    @Test
    void findBySerialAndOptionalDateRange_ShouldThrowException_WhenJpaFindBySerialFails() {
        when(jpaRepository.findBySerialAndOptional("serial123")).thenThrow(new RuntimeException("DB find error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> violationRepository.findBySerialAndOptionalDateRange("serial123", null, null));

        assertEquals("DB find error", ex.getMessage());
        verify(jpaRepository).findBySerialAndOptional("serial123");
    }

    @Test
    void findBySerialAndOptionalDateRange_ShouldThrowException_WhenJpaFindBySerialAndDateRangeFails() {
        Date from = new Date();
        Date to = new Date();

        when(jpaRepository.findBySerialAndOptionalDateRange("serial123", from, to)).thenThrow(new RuntimeException("DB find error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> violationRepository.findBySerialAndOptionalDateRange("serial123", from, to));

        assertEquals("DB find error", ex.getMessage());
        verify(jpaRepository).findBySerialAndOptionalDateRange("serial123", from, to);
    }
}
