package com.aida.uvspartenariats.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Table de contacts
 */
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etablissement")
public class Etablissement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_etablissement", nullable = false)
    private String nomEtablissement;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Departement> nomEtablissements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEtablissement() {
        return nomEtablissement;
    }

    public Etablissement nomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
        return this;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public Location getLocation() {
        return location;
    }

    public Etablissement location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Departement> getNomEtablissements() {
        return nomEtablissements;
    }

    public Etablissement nomEtablissements(Set<Departement> departements) {
        this.nomEtablissements = departements;
        return this;
    }

    public Etablissement addNomEtablissement(Departement departement) {
        this.nomEtablissements.add(departement);
        departement.setEtablissement(this);
        return this;
    }

    public Etablissement removeNomEtablissement(Departement departement) {
        this.nomEtablissements.remove(departement);
        departement.setEtablissement(null);
        return this;
    }

    public void setNomEtablissements(Set<Departement> departements) {
        this.nomEtablissements = departements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nomEtablissement='" + getNomEtablissement() + "'" +
            "}";
    }
}
