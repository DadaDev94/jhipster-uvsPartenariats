package com.aida.uvspartenariats.service;

import com.aida.uvspartenariats.service.dto.AccordDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.aida.uvspartenariats.domain.Accord}.
 */
public interface AccordService {
    /**
     * Save a accord.
     *
     * @param accordDTO the entity to save.
     * @return the persisted entity.
     */
    AccordDTO save(AccordDTO accordDTO);

    /**
     * Get all the accords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccordDTO> findOne(Long id);

    /**
     * Delete the "id" accord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accord corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccordDTO> search(String query, Pageable pageable);
}
