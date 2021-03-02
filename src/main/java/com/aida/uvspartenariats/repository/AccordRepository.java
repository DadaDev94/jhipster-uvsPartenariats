package com.aida.uvspartenariats.repository;

import com.aida.uvspartenariats.domain.Accord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Accord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccordRepository extends JpaRepository<Accord, Long> {}
