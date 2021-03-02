package com.aida.uvspartenariats.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.aida.uvspartenariats.domain.Role} entity.
 */
public class RoleDTO implements Serializable {
    private Long id;

    private String posteOccup;

    private String fonction;

    private Long employeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosteOccup() {
        return posteOccup;
    }

    public void setPosteOccup(String posteOccup) {
        this.posteOccup = posteOccup;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Long getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Long employeId) {
        this.employeId = employeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDTO)) {
            return false;
        }

        return id != null && id.equals(((RoleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleDTO{" +
            "id=" + getId() +
            ", posteOccup='" + getPosteOccup() + "'" +
            ", fonction='" + getFonction() + "'" +
            ", employeId=" + getEmployeId() +
            "}";
    }
}
