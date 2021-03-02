package com.aida.uvspartenariats.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aida.uvspartenariats.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class EmployeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeDTO.class);
        EmployeDTO employeDTO1 = new EmployeDTO();
        employeDTO1.setId(1L);
        EmployeDTO employeDTO2 = new EmployeDTO();
        assertThat(employeDTO1).isNotEqualTo(employeDTO2);
        employeDTO2.setId(employeDTO1.getId());
        assertThat(employeDTO1).isEqualTo(employeDTO2);
        employeDTO2.setId(2L);
        assertThat(employeDTO1).isNotEqualTo(employeDTO2);
        employeDTO1.setId(null);
        assertThat(employeDTO1).isNotEqualTo(employeDTO2);
    }
}
