package com.aida.uvspartenariats.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.aida.uvspartenariats.domain.Location} entity.
 */
public class LocationDTO implements Serializable {
    private Long id;

    private String address;

    private String siteInternet;

    private String siteLocal;

    private Integer telephone;

    private Long countryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiteInternet() {
        return siteInternet;
    }

    public void setSiteInternet(String siteInternet) {
        this.siteInternet = siteInternet;
    }

    public String getSiteLocal() {
        return siteLocal;
    }

    public void setSiteLocal(String siteLocal) {
        this.siteLocal = siteLocal;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        return id != null && id.equals(((LocationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", siteInternet='" + getSiteInternet() + "'" +
            ", siteLocal='" + getSiteLocal() + "'" +
            ", telephone=" + getTelephone() +
            ", countryId=" + getCountryId() +
            "}";
    }
}
