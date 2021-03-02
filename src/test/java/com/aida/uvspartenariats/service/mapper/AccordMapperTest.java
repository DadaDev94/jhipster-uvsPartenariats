package com.aida.uvspartenariats.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccordMapperTest {
    private AccordMapper accordMapper;

    @BeforeEach
    public void setUp() {
        accordMapper = new AccordMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(accordMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(accordMapper.fromId(null)).isNull();
    }
}
