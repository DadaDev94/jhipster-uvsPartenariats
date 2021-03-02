package com.aida.uvspartenariats.service.mapper;

import com.aida.uvspartenariats.domain.*;
import com.aida.uvspartenariats.service.dto.EtablissementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Etablissement} and its DTO {@link EtablissementDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocationMapper.class })
public interface EtablissementMapper extends EntityMapper<EtablissementDTO, Etablissement> {
    @Mapping(source = "location.id", target = "locationId")
    EtablissementDTO toDto(Etablissement etablissement);

    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "nomEtablissements", ignore = true)
    @Mapping(target = "removeNomEtablissement", ignore = true)
    Etablissement toEntity(EtablissementDTO etablissementDTO);

    default Etablissement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Etablissement etablissement = new Etablissement();
        etablissement.setId(id);
        return etablissement;
    }
}
