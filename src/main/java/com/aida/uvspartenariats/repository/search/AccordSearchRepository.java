package com.aida.uvspartenariats.repository.search;

import com.aida.uvspartenariats.domain.Accord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Accord} entity.
 */
public interface AccordSearchRepository extends ElasticsearchRepository<Accord, Long> {}
