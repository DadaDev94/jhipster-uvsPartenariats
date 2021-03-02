package com.aida.uvspartenariats.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aida.uvspartenariats.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class AccordDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccordDTO.class);
        AccordDTO accordDTO1 = new AccordDTO();
        accordDTO1.setId(1L);
        AccordDTO accordDTO2 = new AccordDTO();
        assertThat(accordDTO1).isNotEqualTo(accordDTO2);
        accordDTO2.setId(accordDTO1.getId());
        assertThat(accordDTO1).isEqualTo(accordDTO2);
        accordDTO2.setId(2L);
        assertThat(accordDTO1).isNotEqualTo(accordDTO2);
        accordDTO1.setId(null);
        assertThat(accordDTO1).isNotEqualTo(accordDTO2);
    }
}
