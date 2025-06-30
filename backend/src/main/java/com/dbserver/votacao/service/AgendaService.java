package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.v1.agenda.AgendaCreateDTO;
import com.dbserver.votacao.dto.v1.agenda.AgendaDTO;
import com.dbserver.votacao.entity.Agenda;
import com.dbserver.votacao.entity.enumeration.SessionStatus;
import com.dbserver.votacao.exception.BusinessRuleException;
import com.dbserver.votacao.repository.AgendaRepository;
import com.dbserver.votacao.util.DateConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final SessionService sessionService;
    private final ModelMapper mapper;

    public AgendaDTO create(AgendaCreateDTO agendaCreateDTO) {
        Agenda entity = mapper.map(agendaCreateDTO, Agenda.class);

        entity.setSlug(generateSlug(entity.getTitle()));
        entity.setSession(sessionService.createSessionIntoAgenda(entity, agendaCreateDTO.getDurationInSeconds()));

        Agenda entitySaved = agendaRepository.save(entity);

        AgendaDTO agendaDTO = mapper.map(entitySaved, AgendaDTO.class);

        // Trata atributos que o ModelMapper não consegue converter.
        agendaDTO.setCreatedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getCreatedAt()));
        agendaDTO.setUpdatedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getUpdatedAt()));

        return agendaDTO;
    }

    public List<AgendaDTO> findAll() {
        return agendaRepository.findAll()
                .stream()
                .map(agenda ->  {
                   AgendaDTO agendaDTO = mapper.map(agenda, AgendaDTO.class);
                   agendaDTO.setCreatedAt(DateConverter.convertTimestampToLocalDateTime(agenda.getCreatedAt()));
                   agendaDTO.setUpdatedAt(DateConverter.convertTimestampToLocalDateTime(agenda.getUpdatedAt()));
                   return agendaDTO;
                })
                .toList();
    }

    public List<AgendaDTO> findAllBySessionStatus(SessionStatus sessionStatus) {
        List<Agenda> agendas = agendaRepository.findAllBySessionStatus(sessionStatus);

        return agendas.stream()
                .map(agenda -> {
                    AgendaDTO agendaDTO = mapper.map(agenda, AgendaDTO.class);
                    agendaDTO.setCreatedAt(DateConverter.convertTimestampToLocalDateTime(agenda.getCreatedAt()));
                    agendaDTO.setUpdatedAt(DateConverter.convertTimestampToLocalDateTime(agenda.getUpdatedAt()));
                    return agendaDTO;
                })
                .toList();
    }

    public AgendaDTO findBySlug(String slug) {
        Agenda agenda = agendaRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessRuleException("Nenhuma agenda encontrada com este slug"));

        AgendaDTO agendaDTO = mapper.map(agenda, AgendaDTO.class);
        agendaDTO.setCreatedAt(DateConverter.convertTimestampToLocalDateTime(agenda.getCreatedAt()));
        agendaDTO.setUpdatedAt(DateConverter.convertTimestampToLocalDateTime(agenda.getUpdatedAt()));

        return agendaDTO;
    }


    public long countTotalAgendas() {
        return agendaRepository.count();
    }

    public Agenda getAgenda(Integer idAgenda) {
        return agendaRepository.findById(idAgenda)
                .orElseThrow(() -> new BusinessRuleException("Pauta de id" + idAgenda + " não encontrada"));
    }

    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
    }
}
