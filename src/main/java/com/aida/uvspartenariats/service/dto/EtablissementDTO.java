package com.aida.uvspartenariats.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aida.uvspartenariats.domain.Etablissement} entity.
 */
@ApiModel(description = "Table de contacts")
public class EtablissementDTO implements Serializable {
    private Long id;

    @NotNull
    private String nomEtablissement;

    private Long locationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEtablissement() {
        return nomEtablissement;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtablissementDTO)) {
            return false;
        }

        return id != null && id.equals(((EtablissementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtablissementDTO{" +
            "id=" + getId() +
            ", nomEtablissement='" + getNomEtablissement() + "'" +
            ", locationId=" + getLocationId() +
            "}";
    }
}
