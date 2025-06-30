package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.v1.agenda.AgendaCreateDTO;
import com.dbserver.votacao.dto.v1.agenda.AgendaDTO;
import com.dbserver.votacao.entity.Agenda;
import com.dbserver.votacao.exception.BusinessRuleException;
import com.dbserver.votacao.repository.AgendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Agenda Service Tests")
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    @DisplayName("Deve criar uma pauta com sucesso")
    void shouldCreateAgendaSuccessfully() {
        // Arrange
        AgendaCreateDTO createDTO = new AgendaCreateDTO();
        createDTO.setTitle("Pauta Teste");
        createDTO.setDescription("Descrição da pauta de teste");

        // Simula o mapeamento do DTO para a entidade
        Agenda agendaToSave = new Agenda();
        agendaToSave.setTitle(createDTO.getTitle());
        agendaToSave.setDescription(createDTO.getDescription());
        // O slug seria gerado dentro do serviço, então não o setamos aqui

        // Simula a entidade salva com dados gerados (ID, slug, datas)
        Agenda savedAgenda = new Agenda();
        savedAgenda.setIdAgenda(1);
        savedAgenda.setTitle("Pauta Teste");
        savedAgenda.setSlug("pauta-teste");
        savedAgenda.setDescription("Descrição da pauta de teste");
        savedAgenda.setCreatedAt(Timestamp.from(Instant.now()));
        savedAgenda.setUpdatedAt(Timestamp.from(Instant.now()));

        // Simula o mapeamento da entidade salva para o DTO de resposta
        AgendaDTO expectedDTO = new AgendaDTO();
        expectedDTO.setIdAgenda(1);
        expectedDTO.setSlug("pauta-teste");

        when(mapper.map(createDTO, Agenda.class)).thenReturn(agendaToSave);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(savedAgenda);
        when(mapper.map(savedAgenda, AgendaDTO.class)).thenReturn(expectedDTO);

        // Act
        AgendaDTO resultDTO = agendaService.create(createDTO);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getIdAgenda(), resultDTO.getIdAgenda());
        assertEquals(expectedDTO.getSlug(), resultDTO.getSlug());
        assertNotNull(resultDTO.getCreatedAt());
        assertNotNull(resultDTO.getUpdatedAt());

        verify(mapper).map(createDTO, Agenda.class);
        verify(agendaRepository).save(any(Agenda.class));
        verify(mapper).map(savedAgenda, AgendaDTO.class);
    }

    @Test
    @DisplayName("Deve encontrar uma pauta pelo slug")
    void shouldFindBySlugSuccessfully() {
        // Arrange
        String slug = "pauta-existente";
        Agenda agenda = new Agenda();
        agenda.setSlug(slug);
        AgendaDTO expectedDTO = new AgendaDTO();
        expectedDTO.setSlug(slug);

        when(agendaRepository.findBySlug(slug)).thenReturn(Optional.of(agenda));
        when(mapper.map(agenda, AgendaDTO.class)).thenReturn(expectedDTO);

        // Act
        AgendaDTO resultDTO = agendaService.findBySlug(slug);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(slug, resultDTO.getSlug());
        verify(agendaRepository).findBySlug(slug);
        verify(mapper).map(agenda, AgendaDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por slug não existente")
    void shouldThrowExceptionWhenSlugNotFound() {
        // Arrange
        String slug = "slug-nao-existente";
        when(agendaRepository.findBySlug(slug)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            agendaService.findBySlug(slug);
        });

        assertEquals("Nenhuma agenda encontrada com este slug", exception.getMessage());
        verify(agendaRepository).findBySlug(slug);
        verify(mapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Deve retornar uma lista de todas as pautas")
    void shouldFindAllAgendas() {
        // Arrange
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        List<Agenda> agendaList = Collections.singletonList(agenda);

        AgendaDTO agendaDTO = new AgendaDTO();
        agendaDTO.setIdAgenda(1);

        when(agendaRepository.findAll()).thenReturn(agendaList);
        when(mapper.map(agenda, AgendaDTO.class)).thenReturn(agendaDTO);

        // Act
        List<AgendaDTO> resultList = agendaService.findAll();

        // Assert
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(agendaDTO.getIdAgenda(), resultList.get(0).getIdAgenda());

        verify(agendaRepository).findAll();
        verify(mapper, times(1)).map(any(Agenda.class), eq(AgendaDTO.class));
    }

    @Test
    @DisplayName("Deve retornar uma pauta pelo ID")
    void shouldGetAgendaByIdSuccessfully() {
        // Arrange
        Integer agendaId = 1;
        Agenda expectedAgenda = new Agenda();
        expectedAgenda.setIdAgenda(agendaId);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(expectedAgenda));

        // Act
        Agenda result = agendaService.getAgenda(agendaId);

        // Assert
        assertNotNull(result);
        assertEquals(agendaId, result.getIdAgenda());
        verify(agendaRepository).findById(agendaId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID de pauta não existente")
    void shouldThrowExceptionWhenAgendaIdNotFound() {
        // Arrange
        Integer agendaId = 999;
        when(agendaRepository.findById(agendaId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            agendaService.getAgenda(agendaId);
        });

        assertEquals("Pauta de id" + agendaId + " não encontrada", exception.getMessage());
        verify(agendaRepository).findById(agendaId);
    }
}