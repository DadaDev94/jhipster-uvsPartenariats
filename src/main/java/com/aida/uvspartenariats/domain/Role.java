package com.aida.uvspartenariats.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "poste_occup")
    private String posteOccup;

    @Column(name = "fonction")
    private String fonction;

    @ManyToOne
    @JsonIgnoreProperties(value = "roles", allowSetters = true)
    private Employe employe;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosteOccup() {
        return posteOccup;
    }

    public Role posteOccup(String posteOccup) {
        this.posteOccup = posteOccup;
        return this;
    }

    public void setPosteOccup(String posteOccup) {
        this.posteOccup = posteOccup;
    }

    public String getFonction() {
        return fonction;
    }

    public Role fonction(String fonction) {
        this.fonction = fonction;
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Role employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", posteOccup='" + getPosteOccup() + "'" +
            ", fonction='" + getFonction() + "'" +
            "}";
    }
}
