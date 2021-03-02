package com.aida.uvspartenariats.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CountryMapperTest {
    private CountryMapper countryMapper;

    @BeforeEach
    public void setUp() {
        countryMapper = new CountryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(countryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(countryMapper.fromId(null)).isNull();
    }
}
