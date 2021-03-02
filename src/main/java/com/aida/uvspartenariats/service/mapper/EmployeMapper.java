package com.aida.uvspartenariats.service.mapper;

import com.aida.uvspartenariats.domain.*;
import com.aida.uvspartenariats.service.dto.EmployeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employe} and its DTO {@link EmployeDTO}.
 */
@Mapper(componentModel = "spring", uses = { AccordMapper.class, DepartementMapper.class })
public interface EmployeMapper extends EntityMapper<EmployeDTO, Employe> {
    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(source = "departement.id", target = "departementId")
    @Mapping(source = "accord.id", target = "accordId")
    EmployeDTO toDto(Employe employe);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "removeRole", ignore = true)
    @Mapping(source = "managerId", target = "manager")
    @Mapping(source = "departementId", target = "departement")
    @Mapping(source = "accordId", target = "accord")
    Employe toEntity(EmployeDTO employeDTO);

    default Employe fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employe employe = new Employe();
        employe.setId(id);
        return employe;
    }
}
