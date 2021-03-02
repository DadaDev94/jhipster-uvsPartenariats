package com.aida.uvspartenariats.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.aida.uvspartenariats.domain.Accord;
import com.aida.uvspartenariats.repository.AccordRepository;
import com.aida.uvspartenariats.repository.search.AccordSearchRepository;
import com.aida.uvspartenariats.service.AccordService;
import com.aida.uvspartenariats.service.dto.AccordDTO;
import com.aida.uvspartenariats.service.mapper.AccordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Accord}.
 */
@Service
@Transactional
public class AccordServiceImpl implements AccordService {
    private final Logger log = LoggerFactory.getLogger(AccordServiceImpl.class);

    private final AccordRepository accordRepository;

    private final AccordMapper accordMapper;

    private final AccordSearchRepository accordSearchRepository;

    public AccordServiceImpl(AccordRepository accordRepository, AccordMapper accordMapper, AccordSearchRepository accordSearchRepository) {
        this.accordRepository = accordRepository;
        this.accordMapper = accordMapper;
        this.accordSearchRepository = accordSearchRepository;
    }

    @Override
    public AccordDTO save(AccordDTO accordDTO) {
        log.debug("Request to save Accord : {}", accordDTO);
        Accord accord = accordMapper.toEntity(accordDTO);
        accord = accordRepository.save(accord);
        AccordDTO result = accordMapper.toDto(accord);
        accordSearchRepository.save(accord);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Accords");
        return accordRepository.findAll(pageable).map(accordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccordDTO> findOne(Long id) {
        log.debug("Request to get Accord : {}", id);
        return accordRepository.findById(id).map(accordMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Accord : {}", id);
        accordRepository.deleteById(id);
        accordSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccordDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Accords for query {}", query);
        return accordSearchRepository.search(queryStringQuery(query), pageable).map(accordMapper::toDto);
    }
}
