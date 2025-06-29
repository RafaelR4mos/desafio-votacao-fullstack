package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.v1.agenda.AgendaCreateDTO;
import com.dbserver.votacao.dto.v1.agenda.AgendaDTO;
import com.dbserver.votacao.entity.Agenda;
import com.dbserver.votacao.repository.AgendaRepository;
import com.dbserver.votacao.util.DateConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final SessionService sessionService;
    private final ModelMapper mapper;

    public AgendaDTO create(AgendaCreateDTO agendaCreateDTO) {
        Agenda entity = mapper.map(agendaCreateDTO, Agenda.class);

        entity.setSlug(generateSlug(entity.getTitle()));
        entity.setSession(sessionService.createSessionIntoAgenda(entity));

        Agenda entitySaved = agendaRepository.save(entity);

        AgendaDTO agendaDTO = mapper.map(entitySaved, AgendaDTO.class);

        // Trata atributos que o ModelMapper não consegue converter.
        agendaDTO.setCreatedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getCreatedAt()));
        agendaDTO.setUpdatedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getUpdatedAt()));

        return agendaDTO;
    }


    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
    }
}
