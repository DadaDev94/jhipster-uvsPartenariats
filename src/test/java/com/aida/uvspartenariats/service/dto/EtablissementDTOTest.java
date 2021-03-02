package com.aida.uvspartenariats.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aida.uvspartenariats.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class EtablissementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtablissementDTO.class);
        EtablissementDTO etablissementDTO1 = new EtablissementDTO();
        etablissementDTO1.setId(1L);
        EtablissementDTO etablissementDTO2 = new EtablissementDTO();
        assertThat(etablissementDTO1).isNotEqualTo(etablissementDTO2);
        etablissementDTO2.setId(etablissementDTO1.getId());
        assertThat(etablissementDTO1).isEqualTo(etablissementDTO2);
        etablissementDTO2.setId(2L);
        assertThat(etablissementDTO1).isNotEqualTo(etablissementDTO2);
        etablissementDTO1.setId(null);
        assertThat(etablissementDTO1).isNotEqualTo(etablissementDTO2);
    }
}
