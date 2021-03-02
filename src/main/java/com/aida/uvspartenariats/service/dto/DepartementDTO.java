package com.aida.uvspartenariats.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aida.uvspartenariats.domain.Departement} entity.
 */
public class DepartementDTO implements Serializable {
    private Long id;

    @NotNull
    private String nomDepartement;

    private Long nomDepartmentId;

    /**
     * un utilisateur peut occuper plusieurs postes
     */
    @ApiModelProperty(value = "un utilisateur peut occuper plusieurs postes")
    private Set<AccordDTO> accords = new HashSet<>();

    private Long etablissementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public Long getNomDepartmentId() {
        return nomDepartmentId;
    }

    public void setNomDepartmentId(Long etablissementId) {
        this.nomDepartmentId = etablissementId;
    }

    public Set<AccordDTO> getAccords() {
        return accords;
    }

    public void setAccords(Set<AccordDTO> accords) {
        this.accords = accords;
    }

    public Long getEtablissementId() {
        return etablissementId;
    }

    public void setEtablissementId(Long etablissementId) {
        this.etablissementId = etablissementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartementDTO)) {
            return false;
        }

        return id != null && id.equals(((DepartementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementDTO{" +
            "id=" + getId() +
            ", nomDepartement='" + getNomDepartement() + "'" +
            ", nomDepartmentId=" + getNomDepartmentId() +
            ", accords='" + getAccords() + "'" +
            ", etablissementId=" + getEtablissementId() +
            "}";
    }
}
