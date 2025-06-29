package com.dbserver.votacao.service;

import com.dbserver.votacao.entity.Associate;
import com.dbserver.votacao.dto.v1.associate.AssociateCreateDTO;
import com.dbserver.votacao.dto.v1.associate.AssociateDTO;
import com.dbserver.votacao.repository.AssociateRepository;
import com.dbserver.votacao.util.DateConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociateService {

    private final AssociateRepository associateRepository;
    private final ModelMapper mapper;

    public AssociateDTO create(AssociateCreateDTO associateCreateDTO) {
        Associate entity = mapper.map(associateCreateDTO, Associate.class);

        Associate entitySaved = associateRepository.save(entity);

        AssociateDTO associateDTO = mapper.map(entitySaved, AssociateDTO.class);

        // Trata atributos que o ModelMapper não consegue converter.
        associateDTO.setCreatedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getCreatedAt()));
        associateDTO.setUpdatedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getUpdatedAt()));

        return associateDTO;
    }
}
