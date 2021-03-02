package com.aida.uvspartenariats.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aida.uvspartenariats.UvsPartenariatsApp;
import com.aida.uvspartenariats.domain.Location;
import com.aida.uvspartenariats.repository.LocationRepository;
import com.aida.uvspartenariats.repository.search.LocationSearchRepository;
import com.aida.uvspartenariats.service.LocationService;
import com.aida.uvspartenariats.service.dto.LocationDTO;
import com.aida.uvspartenariats.service.mapper.LocationMapper;
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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@SpringBootTest(classes = UvsPartenariatsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LocationResourceIT {
    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_INTERNET = "AAAAAAAAAA";
    private static final String UPDATED_SITE_INTERNET = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_SITE_LOCAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_TELEPHONE = 1;
    private static final Integer UPDATED_TELEPHONE = 2;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private LocationService locationService;

    /**
     * This repository is mocked in the com.aida.uvspartenariats.repository.search test package.
     *
     * @see com.aida.uvspartenariats.repository.search.LocationSearchRepositoryMockConfiguration
     */
    @Autowired
    private LocationSearchRepository mockLocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .address(DEFAULT_ADDRESS)
            .siteInternet(DEFAULT_SITE_INTERNET)
            .siteLocal(DEFAULT_SITE_LOCAL)
            .telephone(DEFAULT_TELEPHONE);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .address(UPDATED_ADDRESS)
            .siteInternet(UPDATED_SITE_INTERNET)
            .siteLocal(UPDATED_SITE_LOCAL)
            .telephone(UPDATED_TELEPHONE);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();
        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);
        restLocationMockMvc
            .perform(
                post("/api/locations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLocation.getSiteInternet()).isEqualTo(DEFAULT_SITE_INTERNET);
        assertThat(testLocation.getSiteLocal()).isEqualTo(DEFAULT_SITE_LOCAL);
        assertThat(testLocation.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(1)).save(testLocation);
    }

    @Test
    @Transactional
    public void createLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location with an existing ID
        location.setId(1L);
        LocationDTO locationDTO = locationMapper.toDto(location);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(
                post("/api/locations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(0)).save(location);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get("/api/locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].siteInternet").value(hasItem(DEFAULT_SITE_INTERNET)))
            .andExpect(jsonPath("$.[*].siteLocal").value(hasItem(DEFAULT_SITE_LOCAL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get("/api/locations/{id}", location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.siteInternet").value(DEFAULT_SITE_INTERNET))
            .andExpect(jsonPath("$.siteLocal").value(DEFAULT_SITE_LOCAL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .address(UPDATED_ADDRESS)
            .siteInternet(UPDATED_SITE_INTERNET)
            .siteLocal(UPDATED_SITE_LOCAL)
            .telephone(UPDATED_TELEPHONE);
        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);

        restLocationMockMvc
            .perform(
                put("/api/locations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLocation.getSiteInternet()).isEqualTo(UPDATED_SITE_INTERNET);
        assertThat(testLocation.getSiteLocal()).isEqualTo(UPDATED_SITE_LOCAL);
        assertThat(testLocation.getTelephone()).isEqualTo(UPDATED_TELEPHONE);

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(1)).save(testLocation);
    }

    @Test
    @Transactional
    public void updateNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put("/api/locations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(0)).save(location);
    }

    @Test
    @Transactional
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc
            .perform(delete("/api/locations/{id}", location.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(1)).deleteById(location.getId());
    }

    @Test
    @Transactional
    public void searchLocation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        locationRepository.saveAndFlush(location);
        when(mockLocationSearchRepository.search(queryStringQuery("id:" + location.getId())))
            .thenReturn(Collections.singletonList(location));

        // Search the location
        restLocationMockMvc
            .perform(get("/api/_search/locations?query=id:" + location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].siteInternet").value(hasItem(DEFAULT_SITE_INTERNET)))
            .andExpect(jsonPath("$.[*].siteLocal").value(hasItem(DEFAULT_SITE_LOCAL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }
}
