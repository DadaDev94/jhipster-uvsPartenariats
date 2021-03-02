package com.aida.uvspartenariats.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aida.uvspartenariats.UvsPartenariatsApp;
import com.aida.uvspartenariats.domain.Role;
import com.aida.uvspartenariats.repository.RoleRepository;
import com.aida.uvspartenariats.repository.search.RoleSearchRepository;
import com.aida.uvspartenariats.service.RoleService;
import com.aida.uvspartenariats.service.dto.RoleDTO;
import com.aida.uvspartenariats.service.mapper.RoleMapper;
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
 * Integration tests for the {@link RoleResource} REST controller.
 */
@SpringBootTest(classes = UvsPartenariatsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RoleResourceIT {
    private static final String DEFAULT_POSTE_OCCUP = "AAAAAAAAAA";
    private static final String UPDATED_POSTE_OCCUP = "BBBBBBBBBB";

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    /**
     * This repository is mocked in the com.aida.uvspartenariats.repository.search test package.
     *
     * @see com.aida.uvspartenariats.repository.search.RoleSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoleSearchRepository mockRoleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMockMvc;

    private Role role;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role().posteOccup(DEFAULT_POSTE_OCCUP).fonction(DEFAULT_FONCTION);
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = new Role().posteOccup(UPDATED_POSTE_OCCUP).fonction(UPDATED_FONCTION);
        return role;
    }

    @BeforeEach
    public void initTest() {
        role = createEntity(em);
    }

    @Test
    @Transactional
    public void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();
        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);
        restRoleMockMvc
            .perform(
                post("/api/roles").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getPosteOccup()).isEqualTo(DEFAULT_POSTE_OCCUP);
        assertThat(testRole.getFonction()).isEqualTo(DEFAULT_FONCTION);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(1)).save(testRole);
    }

    @Test
    @Transactional
    public void createRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role with an existing ID
        role.setId(1L);
        RoleDTO roleDTO = roleMapper.toDto(role);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc
            .perform(
                post("/api/roles").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(0)).save(role);
    }

    @Test
    @Transactional
    public void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc
            .perform(get("/api/roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].posteOccup").value(hasItem(DEFAULT_POSTE_OCCUP)))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)));
    }

    @Test
    @Transactional
    public void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc
            .perform(get("/api/roles/{id}", role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.posteOccup").value(DEFAULT_POSTE_OCCUP))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION));
    }

    @Test
    @Transactional
    public void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).get();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole.posteOccup(UPDATED_POSTE_OCCUP).fonction(UPDATED_FONCTION);
        RoleDTO roleDTO = roleMapper.toDto(updatedRole);

        restRoleMockMvc
            .perform(
                put("/api/roles").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getPosteOccup()).isEqualTo(UPDATED_POSTE_OCCUP);
        assertThat(testRole.getFonction()).isEqualTo(UPDATED_FONCTION);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(1)).save(testRole);
    }

    @Test
    @Transactional
    public void updateNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put("/api/roles").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(0)).save(role);
    }

    @Test
    @Transactional
    public void deleteRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Delete the role
        restRoleMockMvc
            .perform(delete("/api/roles/{id}", role.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(1)).deleteById(role.getId());
    }

    @Test
    @Transactional
    public void searchRole() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        roleRepository.saveAndFlush(role);
        when(mockRoleSearchRepository.search(queryStringQuery("id:" + role.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(role), PageRequest.of(0, 1), 1));

        // Search the role
        restRoleMockMvc
            .perform(get("/api/_search/roles?query=id:" + role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].posteOccup").value(hasItem(DEFAULT_POSTE_OCCUP)))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)));
    }
}
