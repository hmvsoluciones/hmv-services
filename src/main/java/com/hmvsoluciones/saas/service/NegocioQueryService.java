package com.hmvsoluciones.saas.service;

import com.hmvsoluciones.saas.domain.*; // for static metamodels
import com.hmvsoluciones.saas.domain.Negocio;
import com.hmvsoluciones.saas.repository.NegocioRepository;
import com.hmvsoluciones.saas.service.criteria.NegocioCriteria;
import com.hmvsoluciones.saas.service.dto.NegocioDTO;
import com.hmvsoluciones.saas.service.mapper.NegocioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Negocio} entities in the database.
 * The main input is a {@link NegocioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NegocioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NegocioQueryService extends QueryService<Negocio> {

    private static final Logger LOG = LoggerFactory.getLogger(NegocioQueryService.class);

    private final NegocioRepository negocioRepository;

    private final NegocioMapper negocioMapper;

    public NegocioQueryService(NegocioRepository negocioRepository, NegocioMapper negocioMapper) {
        this.negocioRepository = negocioRepository;
        this.negocioMapper = negocioMapper;
    }

    /**
     * Return a {@link Page} of {@link NegocioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NegocioDTO> findByCriteria(NegocioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Negocio> specification = createSpecification(criteria);
        return negocioRepository.findAll(specification, page).map(negocioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NegocioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Negocio> specification = createSpecification(criteria);
        return negocioRepository.count(specification);
    }

    /**
     * Function to convert {@link NegocioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Negocio> createSpecification(NegocioCriteria criteria) {
        Specification<Negocio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Negocio_.id),
                buildStringSpecification(criteria.getNombre(), Negocio_.nombre),
                buildStringSpecification(criteria.getResponsable(), Negocio_.responsable),
                buildStringSpecification(criteria.getCelular(), Negocio_.celular),
                buildStringSpecification(criteria.getCorreo(), Negocio_.correo),
                buildStringSpecification(criteria.getSubscriptionKey(), Negocio_.subscriptionKey),
                buildSpecification(criteria.getEsActivo(), Negocio_.esActivo)
            );
        }
        return specification;
    }
}
