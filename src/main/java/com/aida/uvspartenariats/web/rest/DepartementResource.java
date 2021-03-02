package com.aida.uvspartenariats.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.aida.uvspartenariats.service.DepartementService;
import com.aida.uvspartenariats.service.dto.DepartementDTO;
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
 * REST controller for managing {@link com.aida.uvspartenariats.domain.Departement}.
 */
@RestController
@RequestMapping("/api")
public class DepartementResource {
    private final Logger log = LoggerFactory.getLogger(DepartementResource.class);

    private static final String ENTITY_NAME = "departement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartementService departementService;

    public DepartementResource(DepartementService departementService) {
        this.departementService = departementService;
    }

    /**
     * {@code POST  /departements} : Create a new departement.
     *
     * @param departementDTO the departementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departementDTO, or with status {@code 400 (Bad Request)} if the departement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departements")
    public ResponseEntity<DepartementDTO> createDepartement(@Valid @RequestBody DepartementDTO departementDTO) throws URISyntaxException {
        log.debug("REST request to save Departement : {}", departementDTO);
        if (departementDTO.getId() != null) {
            throw new BadRequestAlertException("A new departement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartementDTO result = departementService.save(departementDTO);
        return ResponseEntity
            .created(new URI("/api/departements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departements} : Updates an existing departement.
     *
     * @param departementDTO the departementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departementDTO,
     * or with status {@code 400 (Bad Request)} if the departementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departements")
    public ResponseEntity<DepartementDTO> updateDepartement(@Valid @RequestBody DepartementDTO departementDTO) throws URISyntaxException {
        log.debug("REST request to update Departement : {}", departementDTO);
        if (departementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartementDTO result = departementService.save(departementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /departements} : get all the departements.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departements in body.
     */
    @GetMapping("/departements")
    public List<DepartementDTO> getAllDepartements(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Departements");
        return departementService.findAll();
    }

    /**
     * {@code GET  /departements/:id} : get the "id" departement.
     *
     * @param id the id of the departementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departements/{id}")
    public ResponseEntity<DepartementDTO> getDepartement(@PathVariable Long id) {
        log.debug("REST request to get Departement : {}", id);
        Optional<DepartementDTO> departementDTO = departementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departementDTO);
    }

    /**
     * {@code DELETE  /departements/:id} : delete the "id" departement.
     *
     * @param id the id of the departementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departements/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        log.debug("REST request to delete Departement : {}", id);
        departementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/departements?query=:query} : search for the departement corresponding
     * to the query.
     *
     * @param query the query of the departement search.
     * @return the result of the search.
     */
    @GetMapping("/_search/departements")
    public List<DepartementDTO> searchDepartements(@RequestParam String query) {
        log.debug("REST request to search Departements for query {}", query);
        return departementService.search(query);
    }
}
