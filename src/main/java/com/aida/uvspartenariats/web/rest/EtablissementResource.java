package com.aida.uvspartenariats.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.aida.uvspartenariats.service.EtablissementService;
import com.aida.uvspartenariats.service.dto.EtablissementDTO;
import com.aida.uvspartenariats.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.aida.uvspartenariats.domain.Etablissement}.
 */
@RestController
@RequestMapping("/api")
public class EtablissementResource {
    private final Logger log = LoggerFactory.getLogger(EtablissementResource.class);

    private static final String ENTITY_NAME = "etablissement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtablissementService etablissementService;

    public EtablissementResource(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * {@code POST  /etablissements} : Create a new etablissement.
     *
     * @param etablissementDTO the etablissementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etablissementDTO, or with status {@code 400 (Bad Request)} if the etablissement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etablissements")
    public ResponseEntity<EtablissementDTO> createEtablissement(@Valid @RequestBody EtablissementDTO etablissementDTO)
        throws URISyntaxException {
        log.debug("REST request to save Etablissement : {}", etablissementDTO);
        if (etablissementDTO.getId() != null) {
            throw new BadRequestAlertException("A new etablissement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EtablissementDTO result = etablissementService.save(etablissementDTO);
        return ResponseEntity
            .created(new URI("/api/etablissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etablissements} : Updates an existing etablissement.
     *
     * @param etablissementDTO the etablissementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etablissementDTO,
     * or with status {@code 400 (Bad Request)} if the etablissementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etablissementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etablissements")
    public ResponseEntity<EtablissementDTO> updateEtablissement(@Valid @RequestBody EtablissementDTO etablissementDTO)
        throws URISyntaxException {
        log.debug("REST request to update Etablissement : {}", etablissementDTO);
        if (etablissementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EtablissementDTO result = etablissementService.save(etablissementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etablissementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /etablissements} : get all the etablissements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etablissements in body.
     */
    @GetMapping("/etablissements")
    public List<EtablissementDTO> getAllEtablissements() {
        log.debug("REST request to get all Etablissements");
        return etablissementService.findAll();
    }

    /**
     * {@code GET  /etablissements/:id} : get the "id" etablissement.
     *
     * @param id the id of the etablissementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etablissementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etablissements/{id}")
    public ResponseEntity<EtablissementDTO> getEtablissement(@PathVariable Long id) {
        log.debug("REST request to get Etablissement : {}", id);
        Optional<EtablissementDTO> etablissementDTO = etablissementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etablissementDTO);
    }

    /**
     * {@code DELETE  /etablissements/:id} : delete the "id" etablissement.
     *
     * @param id the id of the etablissementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etablissements/{id}")
    public ResponseEntity<Void> deleteEtablissement(@PathVariable Long id) {
        log.debug("REST request to delete Etablissement : {}", id);
        etablissementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/etablissements?query=:query} : search for the etablissement corresponding
     * to the query.
     *
     * @param query the query of the etablissement search.
     * @return the result of the search.
     */
    @GetMapping("/_search/etablissements")
    public List<EtablissementDTO> searchEtablissements(@RequestParam String query) {
        log.debug("REST request to search Etablissements for query {}", query);
        return etablissementService.search(query);
    }
}
