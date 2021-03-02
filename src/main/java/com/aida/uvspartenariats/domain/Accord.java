package com.aida.uvspartenariats.domain;

import com.aida.uvspartenariats.domain.enumeration.StatutAccord;
import com.aida.uvspartenariats.domain.enumeration.TypeAccord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Accord.
 */
@Entity
@Table(name = "accord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accord")
public class Accord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_accord")
    private Integer idAccord;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeAccord type;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutAccord statut;

    @Column(name = "date_accord")
    private Instant dateAccord;

    @OneToMany(mappedBy = "accord")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Employe> bies = new HashSet<>();

    @ManyToMany(mappedBy = "accords")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Departement> departements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdAccord() {
        return idAccord;
    }

    public Accord idAccord(Integer idAccord) {
        this.idAccord = idAccord;
        return this;
    }

    public void setIdAccord(Integer idAccord) {
        this.idAccord = idAccord;
    }

    public String getTitle() {
        return title;
    }

    public Accord title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Accord description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeAccord getType() {
        return type;
    }

    public Accord type(TypeAccord type) {
        this.type = type;
        return this;
    }

    public void setType(TypeAccord type) {
        this.type = type;
    }

    public StatutAccord getStatut() {
        return statut;
    }

    public Accord statut(StatutAccord statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(StatutAccord statut) {
        this.statut = statut;
    }

    public Instant getDateAccord() {
        return dateAccord;
    }

    public Accord dateAccord(Instant dateAccord) {
        this.dateAccord = dateAccord;
        return this;
    }

    public void setDateAccord(Instant dateAccord) {
        this.dateAccord = dateAccord;
    }

    public Set<Employe> getBies() {
        return bies;
    }

    public Accord bies(Set<Employe> employes) {
        this.bies = employes;
        return this;
    }

    public Accord addBy(Employe employe) {
        this.bies.add(employe);
        employe.setAccord(this);
        return this;
    }

    public Accord removeBy(Employe employe) {
        this.bies.remove(employe);
        employe.setAccord(null);
        return this;
    }

    public void setBies(Set<Employe> employes) {
        this.bies = employes;
    }

    public Set<Departement> getDepartements() {
        return departements;
    }

    public Accord departements(Set<Departement> departements) {
        this.departements = departements;
        return this;
    }

    public Accord addDepartement(Departement departement) {
        this.departements.add(departement);
        departement.getAccords().add(this);
        return this;
    }

    public Accord removeDepartement(Departement departement) {
        this.departements.remove(departement);
        departement.getAccords().remove(this);
        return this;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accord)) {
            return false;
        }
        return id != null && id.equals(((Accord) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Accord{" +
            "id=" + getId() +
            ", idAccord=" + getIdAccord() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateAccord='" + getDateAccord() + "'" +
            "}";
    }
}
