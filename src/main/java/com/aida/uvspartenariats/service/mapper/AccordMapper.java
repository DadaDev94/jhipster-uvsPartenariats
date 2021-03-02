package com.aida.uvspartenariats.service.mapper;

import com.aida.uvspartenariats.domain.*;
import com.aida.uvspartenariats.service.dto.AccordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Accord} and its DTO {@link AccordDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccordMapper extends EntityMapper<AccordDTO, Accord> {
    @Mapping(target = "bies", ignore = true)
    @Mapping(target = "removeBy", ignore = true)
    @Mapping(target = "departements", ignore = true)
    @Mapping(target = "removeDepartement", ignore = true)
    Accord toEntity(AccordDTO accordDTO);

    default Accord fromId(Long id) {
        if (id == null) {
            return null;
        }
        Accord accord = new Accord();
        accord.setId(id);
        return accord;
    }
}
