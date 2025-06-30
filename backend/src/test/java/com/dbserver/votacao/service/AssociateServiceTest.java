package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.v1.associate.AssociateCreateDTO;
import com.dbserver.votacao.dto.v1.associate.AssociateDTO;
import com.dbserver.votacao.dto.v1.associate.AssociateStatsDTO;
import com.dbserver.votacao.entity.Associate;
import com.dbserver.votacao.exception.BusinessRuleException;
import com.dbserver.votacao.repository.AssociateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Associate Service Tests")
class AssociateServiceTest {

    @Mock
    private AssociateRepository associateRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AssociateService associateService;

    @Test
    @DisplayName("Deve criar um associado com sucesso")
    void shouldCreateAssociateSuccessfully() {
        // Arrange
        AssociateCreateDTO createDTO = new AssociateCreateDTO("Rafael", "Ramos", "123.456.789-00", "rafael@email.com");
        Associate associateEntity = new Associate();
        associateEntity.setCpf("12345678900"); // CPF já sanitizado

        Associate savedAssociate = new Associate();
        savedAssociate.setIdAssociate(UUID.randomUUID().toString());
        savedAssociate.setFirstName("Rafael");
        savedAssociate.setLastName("Ramos");
        savedAssociate.setCpf("12345678900");
        savedAssociate.setEmail("rafael@email.com");
        savedAssociate.setCreatedAt(Timestamp.from(Instant.now()));
        savedAssociate.setUpdatedAt(Timestamp.from(Instant.now()));

        AssociateDTO expectedDTO = new AssociateDTO();
        expectedDTO.setIdAssociate(savedAssociate.getIdAssociate());

        when(mapper.map(createDTO, Associate.class)).thenReturn(associateEntity);
        when(associateRepository.save(any(Associate.class))).thenReturn(savedAssociate);
        when(mapper.map(savedAssociate, AssociateDTO.class)).thenReturn(expectedDTO);

        // Act
        AssociateDTO resultDTO = associateService.create(createDTO);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getIdAssociate(), resultDTO.getIdAssociate());
        assertNotNull(resultDTO.getCreatedAt()); // Verifica se a data foi convertida e setada
        assertNotNull(resultDTO.getUpdatedAt());

        verify(mapper).map(createDTO, Associate.class);
        verify(associateRepository).save(any(Associate.class));
        verify(mapper).map(savedAssociate, AssociateDTO.class);
    }

    @Test
    @DisplayName("Deve encontrar um associado pelo CPF")
    void shouldFindByCpfSuccessfully() {
        // Arrange
        String cpf = "12345678900";
        Associate associate = new Associate();
        associate.setCpf(cpf);
        AssociateDTO expectedDTO = new AssociateDTO();
        expectedDTO.setCpf(cpf);

        when(associateRepository.findByCpf(cpf)).thenReturn(Optional.of(associate));
        when(mapper.map(associate, AssociateDTO.class)).thenReturn(expectedDTO);

        // Act
        AssociateDTO resultDTO = associateService.findByCpf(cpf);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(cpf, resultDTO.getCpf());
        verify(associateRepository).findByCpf(cpf);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por CPF não existente")
    void shouldThrowExceptionWhenCpfNotFound() {
        // Arrange
        String cpf = "00000000000";
        when(associateRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            associateService.findByCpf(cpf);
        });

        assertEquals("Nenhum associado encontrado com este cpf", exception.getMessage());
        verify(associateRepository).findByCpf(cpf);
        verify(mapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Deve contar todos os associados ativos")
    void shouldCountAllActiveAssociates() {
        // Arrange
        long expectedCount = 15L;
        when(associateRepository.countAllByActive(true)).thenReturn(expectedCount);

        // Act
        AssociateStatsDTO result = associateService.countAllActiveAssociates();

        // Assert
        assertNotNull(result);
        assertEquals(expectedCount, result.getTotalAssociates());
        verify(associateRepository).countAllByActive(true);
    }

    @Test
    @DisplayName("Deve retornar um associado pelo ID")
    void shouldGetAssociateByIdSuccessfully() {
        // Arrange
        String associateId = UUID.randomUUID().toString();
        Associate expectedAssociate = new Associate();
        expectedAssociate.setIdAssociate(associateId);

        when(associateRepository.findById(associateId)).thenReturn(Optional.of(expectedAssociate));

        // Act
        Associate result = associateService.getAssociate(associateId);

        // Assert
        assertNotNull(result);
        assertEquals(associateId, result.getIdAssociate());
        verify(associateRepository).findById(associateId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID de associado não existente")
    void shouldThrowExceptionWhenAssociateIdNotFound() {
        // Arrange
        String associateId = "id-nao-existente";
        when(associateRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            associateService.getAssociate(associateId);
        });

        assertTrue(exception.getMessage().contains("Não foi encontrado nenhum associado com este id " + associateId));
        verify(associateRepository).findById(associateId);
    }
}