package com.aida.uvspartenariats.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegionMapperTest {
    private RegionMapper regionMapper;

    @BeforeEach
    public void setUp() {
        regionMapper = new RegionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(regionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(regionMapper.fromId(null)).isNull();
    }
}
