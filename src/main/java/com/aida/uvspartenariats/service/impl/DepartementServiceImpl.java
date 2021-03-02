package com.aida.uvspartenariats.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.aida.uvspartenariats.domain.Departement;
import com.aida.uvspartenariats.repository.DepartementRepository;
import com.aida.uvspartenariats.repository.search.DepartementSearchRepository;
import com.aida.uvspartenariats.service.DepartementService;
import com.aida.uvspartenariats.service.dto.DepartementDTO;
import com.aida.uvspartenariats.service.mapper.DepartementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departement}.
 */
@Service
@Transactional
public class DepartementServiceImpl implements DepartementService {
    private final Logger log = LoggerFactory.getLogger(DepartementServiceImpl.class);

    private final DepartementRepository departementRepository;

    private final DepartementMapper departementMapper;

    private final DepartementSearchRepository departementSearchRepository;

    public DepartementServiceImpl(
        DepartementRepository departementRepository,
        DepartementMapper departementMapper,
        DepartementSearchRepository departementSearchRepository
    ) {
        this.departementRepository = departementRepository;
        this.departementMapper = departementMapper;
        this.departementSearchRepository = departementSearchRepository;
    }

    @Override
    public DepartementDTO save(DepartementDTO departementDTO) {
        log.debug("Request to save Departement : {}", departementDTO);
        Departement departement = departementMapper.toEntity(departementDTO);
        departement = departementRepository.save(departement);
        DepartementDTO result = departementMapper.toDto(departement);
        departementSearchRepository.save(departement);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartementDTO> findAll() {
        log.debug("Request to get all Departements");
        return departementRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(departementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<DepartementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return departementRepository.findAllWithEagerRelationships(pageable).map(departementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartementDTO> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findOneWithEagerRelationships(id).map(departementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
        departementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartementDTO> search(String query) {
        log.debug("Request to search Departements for query {}", query);
        return StreamSupport
            .stream(departementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(departementMapper::toDto)
            .collect(Collectors.toList());
    }
}
