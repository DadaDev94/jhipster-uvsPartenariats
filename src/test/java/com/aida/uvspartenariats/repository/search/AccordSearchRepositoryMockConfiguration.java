package com.aida.uvspartenariats.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AccordSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AccordSearchRepositoryMockConfiguration {
    @MockBean
    private AccordSearchRepository mockAccordSearchRepository;
}
