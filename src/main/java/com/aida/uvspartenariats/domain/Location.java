package com.aida.uvspartenariats.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "location")
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "site_internet")
    private String siteInternet;

    @Column(name = "site_local")
    private String siteLocal;

    @Column(name = "telephone")
    private Integer telephone;

    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public Location address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiteInternet() {
        return siteInternet;
    }

    public Location siteInternet(String siteInternet) {
        this.siteInternet = siteInternet;
        return this;
    }

    public void setSiteInternet(String siteInternet) {
        this.siteInternet = siteInternet;
    }

    public String getSiteLocal() {
        return siteLocal;
    }

    public Location siteLocal(String siteLocal) {
        this.siteLocal = siteLocal;
        return this;
    }

    public void setSiteLocal(String siteLocal) {
        this.siteLocal = siteLocal;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public Location telephone(Integer telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public Country getCountry() {
        return country;
    }

    public Location country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", siteInternet='" + getSiteInternet() + "'" +
            ", siteLocal='" + getSiteLocal() + "'" +
            ", telephone=" + getTelephone() +
            "}";
    }
}
