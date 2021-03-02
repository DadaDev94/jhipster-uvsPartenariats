package com.aida.uvspartenariats.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aida.uvspartenariats.UvsPartenariatsApp;
import com.aida.uvspartenariats.domain.Etablissement;
import com.aida.uvspartenariats.repository.EtablissementRepository;
import com.aida.uvspartenariats.repository.search.EtablissementSearchRepository;
import com.aida.uvspartenariats.service.EtablissementService;
import com.aida.uvspartenariats.service.dto.EtablissementDTO;
import com.aida.uvspartenariats.service.mapper.EtablissementMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EtablissementResource} REST controller.
 */
@SpringBootTest(classes = UvsPartenariatsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EtablissementResourceIT {
    private static final String DEFAULT_NOM_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ETABLISSEMENT = "BBBBBBBBBB";

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private EtablissementMapper etablissementMapper;

    @Autowired
    private EtablissementService etablissementService;

    /**
     * This repository is mocked in the com.aida.uvspartenariats.repository.search test package.
     *
     * @see com.aida.uvspartenariats.repository.search.EtablissementSearchRepositoryMockConfiguration
     */
    @Autowired
    private EtablissementSearchRepository mockEtablissementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtablissementMockMvc;

    private Etablissement etablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement().nomEtablissement(DEFAULT_NOM_ETABLISSEMENT);
        return etablissement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createUpdatedEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement().nomEtablissement(UPDATED_NOM_ETABLISSEMENT);
        return etablissement;
    }

    @BeforeEach
    public void initTest() {
        etablissement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtablissement() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();
        // Create the Etablissement
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);
        restEtablissementMockMvc
            .perform(
                post("/api/etablissements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate + 1);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).save(testEtablissement);
    }

    @Test
    @Transactional
    public void createEtablissementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // Create the Etablissement with an existing ID
        etablissement.setId(1L);
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtablissementMockMvc
            .perform(
                post("/api/etablissements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(0)).save(etablissement);
    }

    @Test
    @Transactional
    public void checkNomEtablissementIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setNomEtablissement(null);

        // Create the Etablissement, which fails.
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);

        restEtablissementMockMvc
            .perform(
                post("/api/etablissements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEtablissements() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList
        restEtablissementMockMvc
            .perform(get("/api/etablissements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEtablissement").value(hasItem(DEFAULT_NOM_ETABLISSEMENT)));
    }

    @Test
    @Transactional
    public void getEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get the etablissement
        restEtablissementMockMvc
            .perform(get("/api/etablissements/{id}", etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etablissement.getId().intValue()))
            .andExpect(jsonPath("$.nomEtablissement").value(DEFAULT_NOM_ETABLISSEMENT));
    }

    @Test
    @Transactional
    public void getNonExistingEtablissement() throws Exception {
        // Get the etablissement
        restEtablissementMockMvc.perform(get("/api/etablissements/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement
        Etablissement updatedEtablissement = etablissementRepository.findById(etablissement.getId()).get();
        // Disconnect from session so that the updates on updatedEtablissement are not directly saved in db
        em.detach(updatedEtablissement);
        updatedEtablissement.nomEtablissement(UPDATED_NOM_ETABLISSEMENT);
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(updatedEtablissement);

        restEtablissementMockMvc
            .perform(
                put("/api/etablissements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).save(testEtablissement);
    }

    @Test
    @Transactional
    public void updateNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Create the Etablissement
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                put("/api/etablissements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(0)).save(etablissement);
    }

    @Test
    @Transactional
    public void deleteEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeDelete = etablissementRepository.findAll().size();

        // Delete the etablissement
        restEtablissementMockMvc
            .perform(delete("/api/etablissements/{id}", etablissement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).deleteById(etablissement.getId());
    }

    @Test
    @Transactional
    public void searchEtablissement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);
        when(mockEtablissementSearchRepository.search(queryStringQuery("id:" + etablissement.getId())))
            .thenReturn(Collections.singletonList(etablissement));

        // Search the etablissement
        restEtablissementMockMvc
            .perform(get("/api/_search/etablissements?query=id:" + etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEtablissement").value(hasItem(DEFAULT_NOM_ETABLISSEMENT)));
    }
}
