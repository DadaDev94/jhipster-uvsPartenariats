package com.aida.uvspartenariats.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeMapperTest {
    private EmployeMapper employeMapper;

    @BeforeEach
    public void setUp() {
        employeMapper = new EmployeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(employeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(employeMapper.fromId(null)).isNull();
    }
}
