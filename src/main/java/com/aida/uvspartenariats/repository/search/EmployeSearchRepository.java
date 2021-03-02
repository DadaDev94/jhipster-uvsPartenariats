package com.aida.uvspartenariats.repository.search;

import com.aida.uvspartenariats.domain.Employe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Employe} entity.
 */
public interface EmployeSearchRepository extends ElasticsearchRepository<Employe, Long> {}
