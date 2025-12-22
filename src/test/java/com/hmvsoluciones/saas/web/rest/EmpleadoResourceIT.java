package com.hmvsoluciones.saas.web.rest;

import static com.hmvsoluciones.saas.domain.EmpleadoAsserts.*;
import static com.hmvsoluciones.saas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.saas.IntegrationTest;
import com.hmvsoluciones.saas.domain.Empleado;
import com.hmvsoluciones.saas.repository.EmpleadoRepository;
import com.hmvsoluciones.saas.service.dto.EmpleadoDTO;
import com.hmvsoluciones.saas.service.mapper.EmpleadoMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmpleadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoResourceIT {

  private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
  private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

  private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
  private static final String UPDATED_CELULAR = "BBBBBBBBBB";

  private static final String DEFAULT_ESPECIALIDAD = "AAAAAAAAAA";
  private static final String UPDATED_ESPECIALIDAD = "BBBBBBBBBB";

  private static final Boolean DEFAULT_ACTIVO = false;
  private static final Boolean UPDATED_ACTIVO = true;

  private static final String ENTITY_API_URL = "/api/empleados";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private ObjectMapper om;

  @Autowired
  private EmpleadoRepository empleadoRepository;

  @Autowired
  private EmpleadoMapper empleadoMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restEmpleadoMockMvc;

  private Empleado empleado;

  private Empleado insertedEmpleado;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Empleado createEntity() {
    return new Empleado().nombre(DEFAULT_NOMBRE).celular(DEFAULT_CELULAR).especialidad(DEFAULT_ESPECIALIDAD).activo(DEFAULT_ACTIVO);
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Empleado createUpdatedEntity() {
    return new Empleado().nombre(UPDATED_NOMBRE).celular(UPDATED_CELULAR).especialidad(UPDATED_ESPECIALIDAD).activo(UPDATED_ACTIVO);
  }

  @BeforeEach
  void initTest() {
    empleado = createEntity();
  }

  @AfterEach
  void cleanup() {
    if (insertedEmpleado != null) {
      empleadoRepository.delete(insertedEmpleado);
      insertedEmpleado = null;
    }
  }

  @Test
  @Transactional
  void createEmpleado() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the Empleado
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);
    var returnedEmpleadoDTO = om.readValue(
      restEmpleadoMockMvc
        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      EmpleadoDTO.class
    );

    // Validate the Empleado in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    var returnedEmpleado = empleadoMapper.toEntity(returnedEmpleadoDTO);
    assertEmpleadoUpdatableFieldsEquals(returnedEmpleado, getPersistedEmpleado(returnedEmpleado));

    insertedEmpleado = returnedEmpleado;
  }

  @Test
  @Transactional
  void createEmpleadoWithExistingId() throws Exception {
    // Create the Empleado with an existing ID
    empleado.setId(1L);
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restEmpleadoMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNombreIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    empleado.setNombre(null);

    // Create the Empleado, which fails.
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    restEmpleadoMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllEmpleados() throws Exception {
    // Initialize the database
    insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

    // Get all the empleadoList
    restEmpleadoMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
      .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
      .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
      .andExpect(jsonPath("$.[*].especialidad").value(hasItem(DEFAULT_ESPECIALIDAD)))
      .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
  }

  @Test
  @Transactional
  void getEmpleado() throws Exception {
    // Initialize the database
    insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

    // Get the empleado
    restEmpleadoMockMvc
      .perform(get(ENTITY_API_URL_ID, empleado.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
      .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
      .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
      .andExpect(jsonPath("$.especialidad").value(DEFAULT_ESPECIALIDAD))
      .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
  }

  @Test
  @Transactional
  void getNonExistingEmpleado() throws Exception {
    // Get the empleado
    restEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingEmpleado() throws Exception {
    // Initialize the database
    insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the empleado
    Empleado updatedEmpleado = empleadoRepository.findById(empleado.getId()).orElseThrow();
    // Disconnect from session so that the updates on updatedEmpleado are not directly saved in db
    em.detach(updatedEmpleado);
    updatedEmpleado.nombre(UPDATED_NOMBRE).celular(UPDATED_CELULAR).especialidad(UPDATED_ESPECIALIDAD).activo(UPDATED_ACTIVO);
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(updatedEmpleado);

    restEmpleadoMockMvc
      .perform(
        put(ENTITY_API_URL_ID, empleadoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO))
      )
      .andExpect(status().isOk());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedEmpleadoToMatchAllProperties(updatedEmpleado);
  }

  @Test
  @Transactional
  void putNonExistingEmpleado() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    empleado.setId(longCount.incrementAndGet());

    // Create the Empleado
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restEmpleadoMockMvc
      .perform(
        put(ENTITY_API_URL_ID, empleadoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchEmpleado() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    empleado.setId(longCount.incrementAndGet());

    // Create the Empleado
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restEmpleadoMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(empleadoDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamEmpleado() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    empleado.setId(longCount.incrementAndGet());

    // Create the Empleado
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restEmpleadoMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateEmpleadoWithPatch() throws Exception {
    // Initialize the database
    insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the empleado using partial update
    Empleado partialUpdatedEmpleado = new Empleado();
    partialUpdatedEmpleado.setId(empleado.getId());

    partialUpdatedEmpleado.nombre(UPDATED_NOMBRE);

    restEmpleadoMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedEmpleado))
      )
      .andExpect(status().isOk());

    // Validate the Empleado in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertEmpleadoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmpleado, empleado), getPersistedEmpleado(empleado));
  }

  @Test
  @Transactional
  void fullUpdateEmpleadoWithPatch() throws Exception {
    // Initialize the database
    insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the empleado using partial update
    Empleado partialUpdatedEmpleado = new Empleado();
    partialUpdatedEmpleado.setId(empleado.getId());

    partialUpdatedEmpleado.nombre(UPDATED_NOMBRE).celular(UPDATED_CELULAR).especialidad(UPDATED_ESPECIALIDAD).activo(UPDATED_ACTIVO);

    restEmpleadoMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedEmpleado))
      )
      .andExpect(status().isOk());

    // Validate the Empleado in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertEmpleadoUpdatableFieldsEquals(partialUpdatedEmpleado, getPersistedEmpleado(partialUpdatedEmpleado));
  }

  @Test
  @Transactional
  void patchNonExistingEmpleado() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    empleado.setId(longCount.incrementAndGet());

    // Create the Empleado
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restEmpleadoMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, empleadoDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empleadoDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchEmpleado() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    empleado.setId(longCount.incrementAndGet());

    // Create the Empleado
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restEmpleadoMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(empleadoDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamEmpleado() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    empleado.setId(longCount.incrementAndGet());

    // Create the Empleado
    EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restEmpleadoMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empleadoDTO)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Empleado in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteEmpleado() throws Exception {
    // Initialize the database
    insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the empleado
    restEmpleadoMockMvc
      .perform(delete(ENTITY_API_URL_ID, empleado.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return empleadoRepository.count();
  }

  protected void assertIncrementedRepositoryCount(long countBefore) {
    assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
  }

  protected void assertDecrementedRepositoryCount(long countBefore) {
    assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
  }

  protected void assertSameRepositoryCount(long countBefore) {
    assertThat(countBefore).isEqualTo(getRepositoryCount());
  }

  protected Empleado getPersistedEmpleado(Empleado empleado) {
    return empleadoRepository.findById(empleado.getId()).orElseThrow();
  }

  protected void assertPersistedEmpleadoToMatchAllProperties(Empleado expectedEmpleado) {
    assertEmpleadoAllPropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
  }

  protected void assertPersistedEmpleadoToMatchUpdatableProperties(Empleado expectedEmpleado) {
    assertEmpleadoAllUpdatablePropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
  }
}
