package com.aida.uvspartenariats.repository;

import com.aida.uvspartenariats.domain.Etablissement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Etablissement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Long> {}
