package com.hmvsoluciones.saas.repository;

import com.hmvsoluciones.saas.domain.Negocio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Negocio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Long>, JpaSpecificationExecutor<Negocio> {}
