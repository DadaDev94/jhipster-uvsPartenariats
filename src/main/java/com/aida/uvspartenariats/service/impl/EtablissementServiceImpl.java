package com.aida.uvspartenariats.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.aida.uvspartenariats.domain.Etablissement;
import com.aida.uvspartenariats.repository.EtablissementRepository;
import com.aida.uvspartenariats.repository.search.EtablissementSearchRepository;
import com.aida.uvspartenariats.service.EtablissementService;
import com.aida.uvspartenariats.service.dto.EtablissementDTO;
import com.aida.uvspartenariats.service.mapper.EtablissementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etablissement}.
 */
@Service
@Transactional
public class EtablissementServiceImpl implements EtablissementService {
    private final Logger log = LoggerFactory.getLogger(EtablissementServiceImpl.class);

    private final EtablissementRepository etablissementRepository;

    private final EtablissementMapper etablissementMapper;

    private final EtablissementSearchRepository etablissementSearchRepository;

    public EtablissementServiceImpl(
        EtablissementRepository etablissementRepository,
        EtablissementMapper etablissementMapper,
        EtablissementSearchRepository etablissementSearchRepository
    ) {
        this.etablissementRepository = etablissementRepository;
        this.etablissementMapper = etablissementMapper;
        this.etablissementSearchRepository = etablissementSearchRepository;
    }

    @Override
    public EtablissementDTO save(EtablissementDTO etablissementDTO) {
        log.debug("Request to save Etablissement : {}", etablissementDTO);
        Etablissement etablissement = etablissementMapper.toEntity(etablissementDTO);
        etablissement = etablissementRepository.save(etablissement);
        EtablissementDTO result = etablissementMapper.toDto(etablissement);
        etablissementSearchRepository.save(etablissement);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtablissementDTO> findAll() {
        log.debug("Request to get all Etablissements");
        return etablissementRepository.findAll().stream().map(etablissementMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EtablissementDTO> findOne(Long id) {
        log.debug("Request to get Etablissement : {}", id);
        return etablissementRepository.findById(id).map(etablissementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etablissement : {}", id);
        etablissementRepository.deleteById(id);
        etablissementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtablissementDTO> search(String query) {
        log.debug("Request to search Etablissements for query {}", query);
        return StreamSupport
            .stream(etablissementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(etablissementMapper::toDto)
            .collect(Collectors.toList());
    }
}
