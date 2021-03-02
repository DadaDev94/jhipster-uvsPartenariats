package com.aida.uvspartenariats.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "departement")
public class Departement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_departement", nullable = false)
    private String nomDepartement;

    @OneToOne
    @JoinColumn(unique = true)
    private Etablissement nomDepartment;

    /**
     * un utilisateur peut occuper plusieurs postes
     */
    @OneToMany(mappedBy = "departement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Employe> employes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "departement_accord",
        joinColumns = @JoinColumn(name = "departement_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "accord_id", referencedColumnName = "id")
    )
    private Set<Accord> accords = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "nomEtablissements", allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public Departement nomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
        return this;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public Etablissement getNomDepartment() {
        return nomDepartment;
    }

    public Departement nomDepartment(Etablissement etablissement) {
        this.nomDepartment = etablissement;
        return this;
    }

    public void setNomDepartment(Etablissement etablissement) {
        this.nomDepartment = etablissement;
    }

    public Set<Employe> getEmployes() {
        return employes;
    }

    public Departement employes(Set<Employe> employes) {
        this.employes = employes;
        return this;
    }

    public Departement addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.setDepartement(this);
        return this;
    }

    public Departement removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.setDepartement(null);
        return this;
    }

    public void setEmployes(Set<Employe> employes) {
        this.employes = employes;
    }

    public Set<Accord> getAccords() {
        return accords;
    }

    public Departement accords(Set<Accord> accords) {
        this.accords = accords;
        return this;
    }

    public Departement addAccord(Accord accord) {
        this.accords.add(accord);
        accord.getDepartements().add(this);
        return this;
    }

    public Departement removeAccord(Accord accord) {
        this.accords.remove(accord);
        accord.getDepartements().remove(this);
        return this;
    }

    public void setAccords(Set<Accord> accords) {
        this.accords = accords;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public Departement etablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
        return this;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return id != null && id.equals(((Departement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", nomDepartement='" + getNomDepartement() + "'" +
            "}";
    }
}
