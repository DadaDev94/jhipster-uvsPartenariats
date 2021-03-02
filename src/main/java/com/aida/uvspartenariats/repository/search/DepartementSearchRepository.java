package com.aida.uvspartenariats.repository.search;

import com.aida.uvspartenariats.domain.Departement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Departement} entity.
 */
public interface DepartementSearchRepository extends ElasticsearchRepository<Departement, Long> {}
