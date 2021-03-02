package com.aida.uvspartenariats.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aida.uvspartenariats.UvsPartenariatsApp;
import com.aida.uvspartenariats.domain.Accord;
import com.aida.uvspartenariats.domain.enumeration.StatutAccord;
import com.aida.uvspartenariats.domain.enumeration.TypeAccord;
import com.aida.uvspartenariats.repository.AccordRepository;
import com.aida.uvspartenariats.repository.search.AccordSearchRepository;
import com.aida.uvspartenariats.service.AccordService;
import com.aida.uvspartenariats.service.dto.AccordDTO;
import com.aida.uvspartenariats.service.mapper.AccordMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccordResource} REST controller.
 */
@SpringBootTest(classes = UvsPartenariatsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AccordResourceIT {
    private static final Integer DEFAULT_ID_ACCORD = 1;
    private static final Integer UPDATED_ID_ACCORD = 2;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TypeAccord DEFAULT_TYPE = TypeAccord.Signer;
    private static final TypeAccord UPDATED_TYPE = TypeAccord.EnCours;

    private static final StatutAccord DEFAULT_STATUT = StatutAccord.National;
    private static final StatutAccord UPDATED_STATUT = StatutAccord.International;

    private static final Instant DEFAULT_DATE_ACCORD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACCORD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AccordRepository accordRepository;

    @Autowired
    private AccordMapper accordMapper;

    @Autowired
    private AccordService accordService;

    /**
     * This repository is mocked in the com.aida.uvspartenariats.repository.search test package.
     *
     * @see com.aida.uvspartenariats.repository.search.AccordSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccordSearchRepository mockAccordSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccordMockMvc;

    private Accord accord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accord createEntity(EntityManager em) {
        Accord accord = new Accord()
            .idAccord(DEFAULT_ID_ACCORD)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .statut(DEFAULT_STATUT)
            .dateAccord(DEFAULT_DATE_ACCORD);
        return accord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accord createUpdatedEntity(EntityManager em) {
        Accord accord = new Accord()
            .idAccord(UPDATED_ID_ACCORD)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .statut(UPDATED_STATUT)
            .dateAccord(UPDATED_DATE_ACCORD);
        return accord;
    }

    @BeforeEach
    public void initTest() {
        accord = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccord() throws Exception {
        int databaseSizeBeforeCreate = accordRepository.findAll().size();
        // Create the Accord
        AccordDTO accordDTO = accordMapper.toDto(accord);
        restAccordMockMvc
            .perform(
                post("/api/accords")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accordDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeCreate + 1);
        Accord testAccord = accordList.get(accordList.size() - 1);
        assertThat(testAccord.getIdAccord()).isEqualTo(DEFAULT_ID_ACCORD);
        assertThat(testAccord.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAccord.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccord.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAccord.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testAccord.getDateAccord()).isEqualTo(DEFAULT_DATE_ACCORD);

        // Validate the Accord in Elasticsearch
        verify(mockAccordSearchRepository, times(1)).save(testAccord);
    }

    @Test
    @Transactional
    public void createAccordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accordRepository.findAll().size();

        // Create the Accord with an existing ID
        accord.setId(1L);
        AccordDTO accordDTO = accordMapper.toDto(accord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccordMockMvc
            .perform(
                post("/api/accords")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeCreate);

        // Validate the Accord in Elasticsearch
        verify(mockAccordSearchRepository, times(0)).save(accord);
    }

    @Test
    @Transactional
    public void getAllAccords() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        // Get all the accordList
        restAccordMockMvc
            .perform(get("/api/accords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accord.getId().intValue())))
            .andExpect(jsonPath("$.[*].idAccord").value(hasItem(DEFAULT_ID_ACCORD)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateAccord").value(hasItem(DEFAULT_DATE_ACCORD.toString())));
    }

    @Test
    @Transactional
    public void getAccord() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        // Get the accord
        restAccordMockMvc
            .perform(get("/api/accords/{id}", accord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accord.getId().intValue()))
            .andExpect(jsonPath("$.idAccord").value(DEFAULT_ID_ACCORD))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateAccord").value(DEFAULT_DATE_ACCORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccord() throws Exception {
        // Get the accord
        restAccordMockMvc.perform(get("/api/accords/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccord() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        int databaseSizeBeforeUpdate = accordRepository.findAll().size();

        // Update the accord
        Accord updatedAccord = accordRepository.findById(accord.getId()).get();
        // Disconnect from session so that the updates on updatedAccord are not directly saved in db
        em.detach(updatedAccord);
        updatedAccord
            .idAccord(UPDATED_ID_ACCORD)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .statut(UPDATED_STATUT)
            .dateAccord(UPDATED_DATE_ACCORD);
        AccordDTO accordDTO = accordMapper.toDto(updatedAccord);

        restAccordMockMvc
            .perform(
                put("/api/accords")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accordDTO))
            )
            .andExpect(status().isOk());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
        Accord testAccord = accordList.get(accordList.size() - 1);
        assertThat(testAccord.getIdAccord()).isEqualTo(UPDATED_ID_ACCORD);
        assertThat(testAccord.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAccord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccord.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccord.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAccord.getDateAccord()).isEqualTo(UPDATED_DATE_ACCORD);

        // Validate the Accord in Elasticsearch
        verify(mockAccordSearchRepository, times(1)).save(testAccord);
    }

    @Test
    @Transactional
    public void updateNonExistingAccord() throws Exception {
        int databaseSizeBeforeUpdate = accordRepository.findAll().size();

        // Create the Accord
        AccordDTO accordDTO = accordMapper.toDto(accord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccordMockMvc
            .perform(
                put("/api/accords")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Accord in Elasticsearch
        verify(mockAccordSearchRepository, times(0)).save(accord);
    }

    @Test
    @Transactional
    public void deleteAccord() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        int databaseSizeBeforeDelete = accordRepository.findAll().size();

        // Delete the accord
        restAccordMockMvc
            .perform(delete("/api/accords/{id}", accord.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Accord in Elasticsearch
        verify(mockAccordSearchRepository, times(1)).deleteById(accord.getId());
    }

    @Test
    @Transactional
    public void searchAccord() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accordRepository.saveAndFlush(accord);
        when(mockAccordSearchRepository.search(queryStringQuery("id:" + accord.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accord), PageRequest.of(0, 1), 1));

        // Search the accord
        restAccordMockMvc
            .perform(get("/api/_search/accords?query=id:" + accord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accord.getId().intValue())))
            .andExpect(jsonPath("$.[*].idAccord").value(hasItem(DEFAULT_ID_ACCORD)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateAccord").value(hasItem(DEFAULT_DATE_ACCORD.toString())));
    }
}
