package com.aida.uvspartenariats.service;

import com.aida.uvspartenariats.service.dto.RoleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.aida.uvspartenariats.domain.Role}.
 */
public interface RoleService {
    /**
     * Save a role.
     *
     * @param roleDTO the entity to save.
     * @return the persisted entity.
     */
    RoleDTO save(RoleDTO roleDTO);

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" role.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoleDTO> findOne(Long id);

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the role corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoleDTO> search(String query, Pageable pageable);
}
