package com.hmvsoluciones.saas.web.rest;

import com.hmvsoluciones.saas.repository.NegocioRepository;
import com.hmvsoluciones.saas.service.NegocioQueryService;
import com.hmvsoluciones.saas.service.NegocioService;
import com.hmvsoluciones.saas.service.criteria.NegocioCriteria;
import com.hmvsoluciones.saas.service.dto.NegocioDTO;
import com.hmvsoluciones.saas.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hmvsoluciones.saas.domain.Negocio}.
 */
@RestController
@RequestMapping("/api/negocios")
public class NegocioResource {

  private static final Logger LOG = LoggerFactory.getLogger(NegocioResource.class);

  private static final String ENTITY_NAME = "negocio";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final NegocioService negocioService;

  private final NegocioRepository negocioRepository;

  private final NegocioQueryService negocioQueryService;

  public NegocioResource(NegocioService negocioService, NegocioRepository negocioRepository, NegocioQueryService negocioQueryService) {
    this.negocioService = negocioService;
    this.negocioRepository = negocioRepository;
    this.negocioQueryService = negocioQueryService;
  }

  /**
   * {@code POST  /negocios} : Create a new negocio.
   *
   * @param negocioDTO the negocioDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new negocioDTO, or with status {@code 400 (Bad Request)} if the negocio has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<NegocioDTO> createNegocio(@Valid @RequestBody NegocioDTO negocioDTO) throws URISyntaxException {
    LOG.debug("REST request to save Negocio : {}", negocioDTO);
    if (negocioDTO.getId() != null) {
      throw new BadRequestAlertException("A new negocio cannot already have an ID", ENTITY_NAME, "idexists");
    }
    negocioDTO = negocioService.save(negocioDTO);
    return ResponseEntity.created(new URI("/api/negocios/" + negocioDTO.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, negocioDTO.getId().toString()))
      .body(negocioDTO);
  }

  /**
   * {@code PUT  /negocios/:id} : Updates an existing negocio.
   *
   * @param id the id of the negocioDTO to save.
   * @param negocioDTO the negocioDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated negocioDTO,
   * or with status {@code 400 (Bad Request)} if the negocioDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the negocioDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<NegocioDTO> updateNegocio(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody NegocioDTO negocioDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update Negocio : {}, {}", id, negocioDTO);
    if (negocioDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, negocioDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!negocioRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    negocioDTO = negocioService.update(negocioDTO);
    return ResponseEntity.ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, negocioDTO.getId().toString()))
      .body(negocioDTO);
  }

  /**
   * {@code PATCH  /negocios/:id} : Partial updates given fields of an existing negocio, field will ignore if it is null
   *
   * @param id the id of the negocioDTO to save.
   * @param negocioDTO the negocioDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated negocioDTO,
   * or with status {@code 400 (Bad Request)} if the negocioDTO is not valid,
   * or with status {@code 404 (Not Found)} if the negocioDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the negocioDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
  public ResponseEntity<NegocioDTO> partialUpdateNegocio(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody NegocioDTO negocioDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update Negocio partially : {}, {}", id, negocioDTO);
    if (negocioDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, negocioDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!negocioRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<NegocioDTO> result = negocioService.partialUpdate(negocioDTO);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, negocioDTO.getId().toString())
    );
  }

  /**
   * {@code GET  /negocios} : get all the negocios.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of negocios in body.
   */
  @GetMapping("")
  public ResponseEntity<List<NegocioDTO>> getAllNegocios(
    NegocioCriteria criteria,
    @org.springdoc.core.annotations.ParameterObject Pageable pageable
  ) {
    LOG.debug("REST request to get Negocios by criteria: {}", criteria);

    Page<NegocioDTO> page = negocioQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /negocios/count} : count all the negocios.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
   */
  @GetMapping("/count")
  public ResponseEntity<Long> countNegocios(NegocioCriteria criteria) {
    LOG.debug("REST request to count Negocios by criteria: {}", criteria);
    return ResponseEntity.ok().body(negocioQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /negocios/:id} : get the "id" negocio.
   *
   * @param id the id of the negocioDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the negocioDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<NegocioDTO> getNegocio(@PathVariable("id") Long id) {
    LOG.debug("REST request to get Negocio : {}", id);
    Optional<NegocioDTO> negocioDTO = negocioService.findOne(id);
    return ResponseUtil.wrapOrNotFound(negocioDTO);
  }

  /**
   * {@code DELETE  /negocios/:id} : delete the "id" negocio.
   *
   * @param id the id of the negocioDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteNegocio(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete Negocio : {}", id);
    negocioService.delete(id);
    return ResponseEntity.noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
      .build();
  }
}
