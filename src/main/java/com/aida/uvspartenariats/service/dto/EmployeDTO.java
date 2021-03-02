package com.aida.uvspartenariats.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.aida.uvspartenariats.domain.Employe} entity.
 */
@ApiModel(description = "Entité employé.")
public class EmployeDTO implements Serializable {
    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private Instant hireDate;

    private Long managerId;

    /**
     * un departement peut avoir plusieur utilisateurs
     */
    @ApiModelProperty(value = "un departement peut avoir plusieur utilisateurs")
    private Long departementId;

    private Long accordId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Instant getHireDate() {
        return hireDate;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long accordId) {
        this.managerId = accordId;
    }

    public Long getDepartementId() {
        return departementId;
    }

    public void setDepartementId(Long departementId) {
        this.departementId = departementId;
    }

    public Long getAccordId() {
        return accordId;
    }

    public void setAccordId(Long accordId) {
        this.accordId = accordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", managerId=" + getManagerId() +
            ", departementId=" + getDepartementId() +
            ", accordId=" + getAccordId() +
            "}";
    }
}
