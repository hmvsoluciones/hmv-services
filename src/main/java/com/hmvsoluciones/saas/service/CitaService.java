package com.hmvsoluciones.saas.service;

import com.hmvsoluciones.saas.domain.Cita;
import com.hmvsoluciones.saas.repository.CitaRepository;
import com.hmvsoluciones.saas.service.dto.CitaDTO;
import com.hmvsoluciones.saas.service.mapper.CitaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.saas.domain.Cita}.
 */
@Service
@Transactional
public class CitaService {

  private static final Logger LOG = LoggerFactory.getLogger(CitaService.class);

  private final CitaRepository citaRepository;

  private final CitaMapper citaMapper;

  public CitaService(CitaRepository citaRepository, CitaMapper citaMapper) {
    this.citaRepository = citaRepository;
    this.citaMapper = citaMapper;
  }

  /**
   * Save a cita.
   *
   * @param citaDTO the entity to save.
   * @return the persisted entity.
   */
  public CitaDTO save(CitaDTO citaDTO) {
    LOG.debug("Request to save Cita : {}", citaDTO);
    Cita cita = citaMapper.toEntity(citaDTO);
    cita = citaRepository.save(cita);
    return citaMapper.toDto(cita);
  }

  /**
   * Update a cita.
   *
   * @param citaDTO the entity to save.
   * @return the persisted entity.
   */
  public CitaDTO update(CitaDTO citaDTO) {
    LOG.debug("Request to update Cita : {}", citaDTO);
    Cita cita = citaMapper.toEntity(citaDTO);
    cita = citaRepository.save(cita);
    return citaMapper.toDto(cita);
  }

  /**
   * Partially update a cita.
   *
   * @param citaDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<CitaDTO> partialUpdate(CitaDTO citaDTO) {
    LOG.debug("Request to partially update Cita : {}", citaDTO);

    return citaRepository
      .findById(citaDTO.getId())
      .map(existingCita -> {
        citaMapper.partialUpdate(existingCita, citaDTO);

        return existingCita;
      })
      .map(citaRepository::save)
      .map(citaMapper::toDto);
  }

  /**
   *  Get all the citas where Servicio is {@code null}.
   *  @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<CitaDTO> findAllWhereServicioIsNull() {
    LOG.debug("Request to get all citas where Servicio is null");
    return StreamSupport.stream(citaRepository.findAll().spliterator(), false)
      .filter(cita -> cita.getServicio() == null)
      .map(citaMapper::toDto)
      .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one cita by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<CitaDTO> findOne(Long id) {
    LOG.debug("Request to get Cita : {}", id);
    return citaRepository.findById(id).map(citaMapper::toDto);
  }

  /**
   * Delete the cita by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete Cita : {}", id);
    citaRepository.deleteById(id);
  }
}
