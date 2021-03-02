package com.aida.uvspartenariats.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.aida.uvspartenariats.domain.Country} entity.
 */
public class CountryDTO implements Serializable {
    private Long id;

    private String countryName;

    private Long regionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDTO)) {
            return false;
        }

        return id != null && id.equals(((CountryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDTO{" +
            "id=" + getId() +
            ", countryName='" + getCountryName() + "'" +
            ", regionId=" + getRegionId() +
            "}";
    }
}
