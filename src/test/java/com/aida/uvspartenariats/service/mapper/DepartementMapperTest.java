package com.aida.uvspartenariats.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepartementMapperTest {
    private DepartementMapper departementMapper;

    @BeforeEach
    public void setUp() {
        departementMapper = new DepartementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(departementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(departementMapper.fromId(null)).isNull();
    }
}
