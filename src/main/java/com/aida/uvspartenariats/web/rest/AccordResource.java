package com.aida.uvspartenariats.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.aida.uvspartenariats.service.AccordService;
import com.aida.uvspartenariats.service.dto.AccordDTO;
import com.aida.uvspartenariats.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.aida.uvspartenariats.domain.Accord}.
 */
@RestController
@RequestMapping("/api")
public class AccordResource {
    private final Logger log = LoggerFactory.getLogger(AccordResource.class);

    private static final String ENTITY_NAME = "accord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccordService accordService;

    public AccordResource(AccordService accordService) {
        this.accordService = accordService;
    }

    /**
     * {@code POST  /accords} : Create a new accord.
     *
     * @param accordDTO the accordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accordDTO, or with status {@code 400 (Bad Request)} if the accord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accords")
    public ResponseEntity<AccordDTO> createAccord(@RequestBody AccordDTO accordDTO) throws URISyntaxException {
        log.debug("REST request to save Accord : {}", accordDTO);
        if (accordDTO.getId() != null) {
            throw new BadRequestAlertException("A new accord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccordDTO result = accordService.save(accordDTO);
        return ResponseEntity
            .created(new URI("/api/accords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /accords} : Updates an existing accord.
     *
     * @param accordDTO the accordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accordDTO,
     * or with status {@code 400 (Bad Request)} if the accordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accords")
    public ResponseEntity<AccordDTO> updateAccord(@RequestBody AccordDTO accordDTO) throws URISyntaxException {
        log.debug("REST request to update Accord : {}", accordDTO);
        if (accordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccordDTO result = accordService.save(accordDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /accords} : get all the accords.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accords in body.
     */
    @GetMapping("/accords")
    public ResponseEntity<List<AccordDTO>> getAllAccords(Pageable pageable) {
        log.debug("REST request to get a page of Accords");
        Page<AccordDTO> page = accordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accords/:id} : get the "id" accord.
     *
     * @param id the id of the accordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accords/{id}")
    public ResponseEntity<AccordDTO> getAccord(@PathVariable Long id) {
        log.debug("REST request to get Accord : {}", id);
        Optional<AccordDTO> accordDTO = accordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accordDTO);
    }

    /**
     * {@code DELETE  /accords/:id} : delete the "id" accord.
     *
     * @param id the id of the accordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/accords/{id}")
    public ResponseEntity<Void> deleteAccord(@PathVariable Long id) {
        log.debug("REST request to delete Accord : {}", id);
        accordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/accords?query=:query} : search for the accord corresponding
     * to the query.
     *
     * @param query the query of the accord search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/accords")
    public ResponseEntity<List<AccordDTO>> searchAccords(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Accords for query {}", query);
        Page<AccordDTO> page = accordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
