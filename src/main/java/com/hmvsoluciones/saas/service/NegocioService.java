package com.hmvsoluciones.saas.service;

import com.hmvsoluciones.saas.domain.Negocio;
import com.hmvsoluciones.saas.repository.NegocioRepository;
import com.hmvsoluciones.saas.service.dto.NegocioDTO;
import com.hmvsoluciones.saas.service.mapper.NegocioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.saas.domain.Negocio}.
 */
@Service
@Transactional
public class NegocioService {

    private static final Logger LOG = LoggerFactory.getLogger(NegocioService.class);

    private final NegocioRepository negocioRepository;

    private final NegocioMapper negocioMapper;

    public NegocioService(NegocioRepository negocioRepository, NegocioMapper negocioMapper) {
        this.negocioRepository = negocioRepository;
        this.negocioMapper = negocioMapper;
    }

    /**
     * Save a negocio.
     *
     * @param negocioDTO the entity to save.
     * @return the persisted entity.
     */
    public NegocioDTO save(NegocioDTO negocioDTO) {
        LOG.debug("Request to save Negocio : {}", negocioDTO);
        Negocio negocio = negocioMapper.toEntity(negocioDTO);
        negocio = negocioRepository.save(negocio);
        return negocioMapper.toDto(negocio);
    }

    /**
     * Update a negocio.
     *
     * @param negocioDTO the entity to save.
     * @return the persisted entity.
     */
    public NegocioDTO update(NegocioDTO negocioDTO) {
        LOG.debug("Request to update Negocio : {}", negocioDTO);
        Negocio negocio = negocioMapper.toEntity(negocioDTO);
        negocio = negocioRepository.save(negocio);
        return negocioMapper.toDto(negocio);
    }

    /**
     * Partially update a negocio.
     *
     * @param negocioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NegocioDTO> partialUpdate(NegocioDTO negocioDTO) {
        LOG.debug("Request to partially update Negocio : {}", negocioDTO);

        return negocioRepository
            .findById(negocioDTO.getId())
            .map(existingNegocio -> {
                negocioMapper.partialUpdate(existingNegocio, negocioDTO);

                return existingNegocio;
            })
            .map(negocioRepository::save)
            .map(negocioMapper::toDto);
    }

    /**
     * Get one negocio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NegocioDTO> findOne(Long id) {
        LOG.debug("Request to get Negocio : {}", id);
        return negocioRepository.findById(id).map(negocioMapper::toDto);
    }

    /**
     * Delete the negocio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Negocio : {}", id);
        negocioRepository.deleteById(id);
    }
}
