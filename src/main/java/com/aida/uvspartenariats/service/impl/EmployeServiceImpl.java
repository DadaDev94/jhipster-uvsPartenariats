package com.aida.uvspartenariats.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.aida.uvspartenariats.domain.Employe;
import com.aida.uvspartenariats.repository.EmployeRepository;
import com.aida.uvspartenariats.repository.search.EmployeSearchRepository;
import com.aida.uvspartenariats.service.EmployeService;
import com.aida.uvspartenariats.service.dto.EmployeDTO;
import com.aida.uvspartenariats.service.mapper.EmployeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employe}.
 */
@Service
@Transactional
public class EmployeServiceImpl implements EmployeService {
    private final Logger log = LoggerFactory.getLogger(EmployeServiceImpl.class);

    private final EmployeRepository employeRepository;

    private final EmployeMapper employeMapper;

    private final EmployeSearchRepository employeSearchRepository;

    public EmployeServiceImpl(
        EmployeRepository employeRepository,
        EmployeMapper employeMapper,
        EmployeSearchRepository employeSearchRepository
    ) {
        this.employeRepository = employeRepository;
        this.employeMapper = employeMapper;
        this.employeSearchRepository = employeSearchRepository;
    }

    @Override
    public EmployeDTO save(EmployeDTO employeDTO) {
        log.debug("Request to save Employe : {}", employeDTO);
        Employe employe = employeMapper.toEntity(employeDTO);
        employe = employeRepository.save(employe);
        EmployeDTO result = employeMapper.toDto(employe);
        employeSearchRepository.save(employe);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employes");
        return employeRepository.findAll(pageable).map(employeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeDTO> findOne(Long id) {
        log.debug("Request to get Employe : {}", id);
        return employeRepository.findById(id).map(employeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employe : {}", id);
        employeRepository.deleteById(id);
        employeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Employes for query {}", query);
        return employeSearchRepository.search(queryStringQuery(query), pageable).map(employeMapper::toDto);
    }
}
