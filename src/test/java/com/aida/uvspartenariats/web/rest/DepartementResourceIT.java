package com.aida.uvspartenariats.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aida.uvspartenariats.UvsPartenariatsApp;
import com.aida.uvspartenariats.domain.Departement;
import com.aida.uvspartenariats.repository.DepartementRepository;
import com.aida.uvspartenariats.repository.search.DepartementSearchRepository;
import com.aida.uvspartenariats.service.DepartementService;
import com.aida.uvspartenariats.service.dto.DepartementDTO;
import com.aida.uvspartenariats.service.mapper.DepartementMapper;
import java.util.ArrayList;
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
 * Integration tests for the {@link DepartementResource} REST controller.
 */
@SpringBootTest(classes = UvsPartenariatsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DepartementResourceIT {
    private static final String DEFAULT_NOM_DEPARTEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DEPARTEMENT = "BBBBBBBBBB";

    @Autowired
    private DepartementRepository departementRepository;

    @Mock
    private DepartementRepository departementRepositoryMock;

    @Autowired
    private DepartementMapper departementMapper;

    @Mock
    private DepartementService departementServiceMock;

    @Autowired
    private DepartementService departementService;

    /**
     * This repository is mocked in the com.aida.uvspartenariats.repository.search test package.
     *
     * @see com.aida.uvspartenariats.repository.search.DepartementSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartementSearchRepository mockDepartementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartementMockMvc;

    private Departement departement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createEntity(EntityManager em) {
        Departement departement = new Departement().nomDepartement(DEFAULT_NOM_DEPARTEMENT);
        return departement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createUpdatedEntity(EntityManager em) {
        Departement departement = new Departement().nomDepartement(UPDATED_NOM_DEPARTEMENT);
        return departement;
    }

    @BeforeEach
    public void initTest() {
        departement = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartement() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().size();
        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);
        restDepartementMockMvc
            .perform(
                post("/api/departements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate + 1);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNomDepartement()).isEqualTo(DEFAULT_NOM_DEPARTEMENT);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(1)).save(testDepartement);
    }

    @Test
    @Transactional
    public void createDepartementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().size();

        // Create the Departement with an existing ID
        departement.setId(1L);
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartementMockMvc
            .perform(
                post("/api/departements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    public void checkNomDepartementIsRequired() throws Exception {
        int databaseSizeBeforeTest = departementRepository.findAll().size();
        // set the field null
        departement.setNomDepartement(null);

        // Create the Departement, which fails.
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        restDepartementMockMvc
            .perform(
                post("/api/departements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartements() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList
        restDepartementMockMvc
            .perform(get("/api/departements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomDepartement").value(hasItem(DEFAULT_NOM_DEPARTEMENT)));
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllDepartementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(departementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartementMockMvc.perform(get("/api/departements?eagerload=true")).andExpect(status().isOk());

        verify(departementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllDepartementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(departementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartementMockMvc.perform(get("/api/departements?eagerload=true")).andExpect(status().isOk());

        verify(departementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get the departement
        restDepartementMockMvc
            .perform(get("/api/departements/{id}", departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departement.getId().intValue()))
            .andExpect(jsonPath("$.nomDepartement").value(DEFAULT_NOM_DEPARTEMENT));
    }

    @Test
    @Transactional
    public void getNonExistingDepartement() throws Exception {
        // Get the departement
        restDepartementMockMvc.perform(get("/api/departements/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement
        Departement updatedDepartement = departementRepository.findById(departement.getId()).get();
        // Disconnect from session so that the updates on updatedDepartement are not directly saved in db
        em.detach(updatedDepartement);
        updatedDepartement.nomDepartement(UPDATED_NOM_DEPARTEMENT);
        DepartementDTO departementDTO = departementMapper.toDto(updatedDepartement);

        restDepartementMockMvc
            .perform(
                put("/api/departements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNomDepartement()).isEqualTo(UPDATED_NOM_DEPARTEMENT);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(1)).save(testDepartement);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put("/api/departements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    public void deleteDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeDelete = departementRepository.findAll().size();

        // Delete the departement
        restDepartementMockMvc
            .perform(delete("/api/departements/{id}", departement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(1)).deleteById(departement.getId());
    }

    @Test
    @Transactional
    public void searchDepartement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        departementRepository.saveAndFlush(departement);
        when(mockDepartementSearchRepository.search(queryStringQuery("id:" + departement.getId())))
            .thenReturn(Collections.singletonList(departement));

        // Search the departement
        restDepartementMockMvc
            .perform(get("/api/_search/departements?query=id:" + departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomDepartement").value(hasItem(DEFAULT_NOM_DEPARTEMENT)));
    }
}
