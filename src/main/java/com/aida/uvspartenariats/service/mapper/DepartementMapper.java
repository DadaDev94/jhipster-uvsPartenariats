package com.aida.uvspartenariats.service.mapper;

import com.aida.uvspartenariats.domain.*;
import com.aida.uvspartenariats.service.dto.DepartementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring", uses = { EtablissementMapper.class, AccordMapper.class })
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {
    @Mapping(source = "nomDepartment.id", target = "nomDepartmentId")
    @Mapping(source = "etablissement.id", target = "etablissementId")
    DepartementDTO toDto(Departement departement);

    @Mapping(source = "nomDepartmentId", target = "nomDepartment")
    @Mapping(target = "employes", ignore = true)
    @Mapping(target = "removeEmploye", ignore = true)
    @Mapping(target = "removeAccord", ignore = true)
    @Mapping(source = "etablissementId", target = "etablissement")
    Departement toEntity(DepartementDTO departementDTO);

    default Departement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Departement departement = new Departement();
        departement.setId(id);
        return departement;
    }
}
