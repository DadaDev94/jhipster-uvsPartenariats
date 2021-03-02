package com.aida.uvspartenariats.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Entité employé.
 */
@Entity
@Table(name = "employe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "employe")
public class Employe implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "hire_date")
    private Instant hireDate;

    @OneToMany(mappedBy = "employe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "employes", allowSetters = true)
    private Accord manager;

    /**
     * un departement peut avoir plusieur utilisateurs
     */
    @ManyToOne
    @JsonIgnoreProperties(value = "employes", allowSetters = true)
    private Departement departement;

    @ManyToOne
    @JsonIgnoreProperties(value = "bies", allowSetters = true)
    private Accord accord;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Employe nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Employe prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public Employe email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Employe telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Instant getHireDate() {
        return hireDate;
    }

    public Employe hireDate(Instant hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Employe roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Employe addRole(Role role) {
        this.roles.add(role);
        role.setEmploye(this);
        return this;
    }

    public Employe removeRole(Role role) {
        this.roles.remove(role);
        role.setEmploye(null);
        return this;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Accord getManager() {
        return manager;
    }

    public Employe manager(Accord accord) {
        this.manager = accord;
        return this;
    }

    public void setManager(Accord accord) {
        this.manager = accord;
    }

    public Departement getDepartement() {
        return departement;
    }

    public Employe departement(Departement departement) {
        this.departement = departement;
        return this;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Accord getAccord() {
        return accord;
    }

    public Employe accord(Accord accord) {
        this.accord = accord;
        return this;
    }

    public void setAccord(Accord accord) {
        this.accord = accord;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employe)) {
            return false;
        }
        return id != null && id.equals(((Employe) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employe{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            "}";
    }
}
