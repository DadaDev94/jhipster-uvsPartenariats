package com.aida.uvspartenariats.service.dto;

import com.aida.uvspartenariats.domain.enumeration.StatutAccord;
import com.aida.uvspartenariats.domain.enumeration.TypeAccord;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.aida.uvspartenariats.domain.Accord} entity.
 */
public class AccordDTO implements Serializable {
    private Long id;

    private Integer idAccord;

    private String title;

    private String description;

    private TypeAccord type;

    private StatutAccord statut;

    private Instant dateAccord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdAccord() {
        return idAccord;
    }

    public void setIdAccord(Integer idAccord) {
        this.idAccord = idAccord;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeAccord getType() {
        return type;
    }

    public void setType(TypeAccord type) {
        this.type = type;
    }

    public StatutAccord getStatut() {
        return statut;
    }

    public void setStatut(StatutAccord statut) {
        this.statut = statut;
    }

    public Instant getDateAccord() {
        return dateAccord;
    }

    public void setDateAccord(Instant dateAccord) {
        this.dateAccord = dateAccord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccordDTO)) {
            return false;
        }

        return id != null && id.equals(((AccordDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccordDTO{" +
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
