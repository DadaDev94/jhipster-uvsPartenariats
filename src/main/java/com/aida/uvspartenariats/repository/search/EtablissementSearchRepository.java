package com.aida.uvspartenariats.repository.search;

import com.aida.uvspartenariats.domain.Etablissement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Etablissement} entity.
 */
public interface EtablissementSearchRepository extends ElasticsearchRepository<Etablissement, Long> {}
