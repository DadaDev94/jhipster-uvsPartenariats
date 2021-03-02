package com.aida.uvspartenariats.service;

import com.aida.uvspartenariats.service.dto.DepartementDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.aida.uvspartenariats.domain.Departement}.
 */
public interface DepartementService {
    /**
     * Save a departement.
     *
     * @param departementDTO the entity to save.
     * @return the persisted entity.
     */
    DepartementDTO save(DepartementDTO departementDTO);

    /**
     * Get all the departements.
     *
     * @return the list of entities.
     */
    List<DepartementDTO> findAll();

    /**
     * Get all the departements with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<DepartementDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" departement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepartementDTO> findOne(Long id);

    /**
     * Delete the "id" departement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the departement corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<DepartementDTO> search(String query);
}
