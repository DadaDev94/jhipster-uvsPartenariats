package com.aida.uvspartenariats.service;

import com.aida.uvspartenariats.service.dto.EtablissementDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.aida.uvspartenariats.domain.Etablissement}.
 */
public interface EtablissementService {
    /**
     * Save a etablissement.
     *
     * @param etablissementDTO the entity to save.
     * @return the persisted entity.
     */
    EtablissementDTO save(EtablissementDTO etablissementDTO);

    /**
     * Get all the etablissements.
     *
     * @return the list of entities.
     */
    List<EtablissementDTO> findAll();

    /**
     * Get the "id" etablissement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EtablissementDTO> findOne(Long id);

    /**
     * Delete the "id" etablissement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the etablissement corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<EtablissementDTO> search(String query);
}
