package com.aida.uvspartenariats.service.mapper;

import com.aida.uvspartenariats.domain.*;
import com.aida.uvspartenariats.service.dto.RoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeMapper.class })
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Mapping(source = "employe.id", target = "employeId")
    RoleDTO toDto(Role role);

    @Mapping(source = "employeId", target = "employe")
    Role toEntity(RoleDTO roleDTO);

    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
