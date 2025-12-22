package com.hmvsoluciones.saas.web.rest;

import static com.hmvsoluciones.saas.domain.NegocioAsserts.*;
import static com.hmvsoluciones.saas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.saas.IntegrationTest;
import com.hmvsoluciones.saas.domain.Negocio;
import com.hmvsoluciones.saas.repository.NegocioRepository;
import com.hmvsoluciones.saas.service.dto.NegocioDTO;
import com.hmvsoluciones.saas.service.mapper.NegocioMapper;
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
 * Integration tests for the {@link NegocioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NegocioResourceIT {

  private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
  private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

  private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
  private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

  private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
  private static final String UPDATED_CELULAR = "BBBBBBBBBB";

  private static final String DEFAULT_CORREO = "AAAAAAAAAA";
  private static final String UPDATED_CORREO = "BBBBBBBBBB";

  private static final String DEFAULT_SUBSCRIPTION_KEY = "AAAAAAAAAA";
  private static final String UPDATED_SUBSCRIPTION_KEY = "BBBBBBBBBB";

  private static final Boolean DEFAULT_ES_ACTIVO = false;
  private static final Boolean UPDATED_ES_ACTIVO = true;

  private static final String ENTITY_API_URL = "/api/negocios";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private ObjectMapper om;

  @Autowired
  private NegocioRepository negocioRepository;

  @Autowired
  private NegocioMapper negocioMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restNegocioMockMvc;

  private Negocio negocio;

  private Negocio insertedNegocio;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Negocio createEntity() {
    return new Negocio()
      .nombre(DEFAULT_NOMBRE)
      .responsable(DEFAULT_RESPONSABLE)
      .celular(DEFAULT_CELULAR)
      .correo(DEFAULT_CORREO)
      .subscriptionKey(DEFAULT_SUBSCRIPTION_KEY)
      .esActivo(DEFAULT_ES_ACTIVO);
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Negocio createUpdatedEntity() {
    return new Negocio()
      .nombre(UPDATED_NOMBRE)
      .responsable(UPDATED_RESPONSABLE)
      .celular(UPDATED_CELULAR)
      .correo(UPDATED_CORREO)
      .subscriptionKey(UPDATED_SUBSCRIPTION_KEY)
      .esActivo(UPDATED_ES_ACTIVO);
  }

  @BeforeEach
  void initTest() {
    negocio = createEntity();
  }

  @AfterEach
  void cleanup() {
    if (insertedNegocio != null) {
      negocioRepository.delete(insertedNegocio);
      insertedNegocio = null;
    }
  }

  @Test
  @Transactional
  void createNegocio() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the Negocio
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);
    var returnedNegocioDTO = om.readValue(
      restNegocioMockMvc
        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      NegocioDTO.class
    );

    // Validate the Negocio in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    var returnedNegocio = negocioMapper.toEntity(returnedNegocioDTO);
    assertNegocioUpdatableFieldsEquals(returnedNegocio, getPersistedNegocio(returnedNegocio));

    insertedNegocio = returnedNegocio;
  }

  @Test
  @Transactional
  void createNegocioWithExistingId() throws Exception {
    // Create the Negocio with an existing ID
    negocio.setId(1L);
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restNegocioMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNombreIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    negocio.setNombre(null);

    // Create the Negocio, which fails.
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    restNegocioMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkResponsableIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    negocio.setResponsable(null);

    // Create the Negocio, which fails.
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    restNegocioMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkSubscriptionKeyIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    negocio.setSubscriptionKey(null);

    // Create the Negocio, which fails.
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    restNegocioMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllNegocios() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList
    restNegocioMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(negocio.getId().intValue())))
      .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
      .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
      .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
      .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
      .andExpect(jsonPath("$.[*].subscriptionKey").value(hasItem(DEFAULT_SUBSCRIPTION_KEY)))
      .andExpect(jsonPath("$.[*].esActivo").value(hasItem(DEFAULT_ES_ACTIVO)));
  }

  @Test
  @Transactional
  void getNegocio() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get the negocio
    restNegocioMockMvc
      .perform(get(ENTITY_API_URL_ID, negocio.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(negocio.getId().intValue()))
      .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
      .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
      .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
      .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO))
      .andExpect(jsonPath("$.subscriptionKey").value(DEFAULT_SUBSCRIPTION_KEY))
      .andExpect(jsonPath("$.esActivo").value(DEFAULT_ES_ACTIVO));
  }

  @Test
  @Transactional
  void getNegociosByIdFiltering() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    Long id = negocio.getId();

    defaultNegocioFiltering("id.equals=" + id, "id.notEquals=" + id);

    defaultNegocioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

    defaultNegocioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
  }

  @Test
  @Transactional
  void getAllNegociosByNombreIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where nombre equals to
    defaultNegocioFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
  }

  @Test
  @Transactional
  void getAllNegociosByNombreIsInShouldWork() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where nombre in
    defaultNegocioFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
  }

  @Test
  @Transactional
  void getAllNegociosByNombreIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where nombre is not null
    defaultNegocioFiltering("nombre.specified=true", "nombre.specified=false");
  }

  @Test
  @Transactional
  void getAllNegociosByNombreContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where nombre contains
    defaultNegocioFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
  }

  @Test
  @Transactional
  void getAllNegociosByNombreNotContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where nombre does not contain
    defaultNegocioFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
  }

  @Test
  @Transactional
  void getAllNegociosByResponsableIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where responsable equals to
    defaultNegocioFiltering("responsable.equals=" + DEFAULT_RESPONSABLE, "responsable.equals=" + UPDATED_RESPONSABLE);
  }

  @Test
  @Transactional
  void getAllNegociosByResponsableIsInShouldWork() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where responsable in
    defaultNegocioFiltering("responsable.in=" + DEFAULT_RESPONSABLE + "," + UPDATED_RESPONSABLE, "responsable.in=" + UPDATED_RESPONSABLE);
  }

  @Test
  @Transactional
  void getAllNegociosByResponsableIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where responsable is not null
    defaultNegocioFiltering("responsable.specified=true", "responsable.specified=false");
  }

  @Test
  @Transactional
  void getAllNegociosByResponsableContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where responsable contains
    defaultNegocioFiltering("responsable.contains=" + DEFAULT_RESPONSABLE, "responsable.contains=" + UPDATED_RESPONSABLE);
  }

  @Test
  @Transactional
  void getAllNegociosByResponsableNotContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where responsable does not contain
    defaultNegocioFiltering("responsable.doesNotContain=" + UPDATED_RESPONSABLE, "responsable.doesNotContain=" + DEFAULT_RESPONSABLE);
  }

  @Test
  @Transactional
  void getAllNegociosByCelularIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where celular equals to
    defaultNegocioFiltering("celular.equals=" + DEFAULT_CELULAR, "celular.equals=" + UPDATED_CELULAR);
  }

  @Test
  @Transactional
  void getAllNegociosByCelularIsInShouldWork() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where celular in
    defaultNegocioFiltering("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR, "celular.in=" + UPDATED_CELULAR);
  }

  @Test
  @Transactional
  void getAllNegociosByCelularIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where celular is not null
    defaultNegocioFiltering("celular.specified=true", "celular.specified=false");
  }

  @Test
  @Transactional
  void getAllNegociosByCelularContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where celular contains
    defaultNegocioFiltering("celular.contains=" + DEFAULT_CELULAR, "celular.contains=" + UPDATED_CELULAR);
  }

  @Test
  @Transactional
  void getAllNegociosByCelularNotContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where celular does not contain
    defaultNegocioFiltering("celular.doesNotContain=" + UPDATED_CELULAR, "celular.doesNotContain=" + DEFAULT_CELULAR);
  }

  @Test
  @Transactional
  void getAllNegociosByCorreoIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where correo equals to
    defaultNegocioFiltering("correo.equals=" + DEFAULT_CORREO, "correo.equals=" + UPDATED_CORREO);
  }

  @Test
  @Transactional
  void getAllNegociosByCorreoIsInShouldWork() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where correo in
    defaultNegocioFiltering("correo.in=" + DEFAULT_CORREO + "," + UPDATED_CORREO, "correo.in=" + UPDATED_CORREO);
  }

  @Test
  @Transactional
  void getAllNegociosByCorreoIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where correo is not null
    defaultNegocioFiltering("correo.specified=true", "correo.specified=false");
  }

  @Test
  @Transactional
  void getAllNegociosByCorreoContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where correo contains
    defaultNegocioFiltering("correo.contains=" + DEFAULT_CORREO, "correo.contains=" + UPDATED_CORREO);
  }

  @Test
  @Transactional
  void getAllNegociosByCorreoNotContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where correo does not contain
    defaultNegocioFiltering("correo.doesNotContain=" + UPDATED_CORREO, "correo.doesNotContain=" + DEFAULT_CORREO);
  }

  @Test
  @Transactional
  void getAllNegociosBySubscriptionKeyIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where subscriptionKey equals to
    defaultNegocioFiltering("subscriptionKey.equals=" + DEFAULT_SUBSCRIPTION_KEY, "subscriptionKey.equals=" + UPDATED_SUBSCRIPTION_KEY);
  }

  @Test
  @Transactional
  void getAllNegociosBySubscriptionKeyIsInShouldWork() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where subscriptionKey in
    defaultNegocioFiltering(
      "subscriptionKey.in=" + DEFAULT_SUBSCRIPTION_KEY + "," + UPDATED_SUBSCRIPTION_KEY,
      "subscriptionKey.in=" + UPDATED_SUBSCRIPTION_KEY
    );
  }

  @Test
  @Transactional
  void getAllNegociosBySubscriptionKeyIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where subscriptionKey is not null
    defaultNegocioFiltering("subscriptionKey.specified=true", "subscriptionKey.specified=false");
  }

  @Test
  @Transactional
  void getAllNegociosBySubscriptionKeyContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where subscriptionKey contains
    defaultNegocioFiltering("subscriptionKey.contains=" + DEFAULT_SUBSCRIPTION_KEY, "subscriptionKey.contains=" + UPDATED_SUBSCRIPTION_KEY);
  }

  @Test
  @Transactional
  void getAllNegociosBySubscriptionKeyNotContainsSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where subscriptionKey does not contain
    defaultNegocioFiltering(
      "subscriptionKey.doesNotContain=" + UPDATED_SUBSCRIPTION_KEY,
      "subscriptionKey.doesNotContain=" + DEFAULT_SUBSCRIPTION_KEY
    );
  }

  @Test
  @Transactional
  void getAllNegociosByEsActivoIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where esActivo equals to
    defaultNegocioFiltering("esActivo.equals=" + DEFAULT_ES_ACTIVO, "esActivo.equals=" + UPDATED_ES_ACTIVO);
  }

  @Test
  @Transactional
  void getAllNegociosByEsActivoIsInShouldWork() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where esActivo in
    defaultNegocioFiltering("esActivo.in=" + DEFAULT_ES_ACTIVO + "," + UPDATED_ES_ACTIVO, "esActivo.in=" + UPDATED_ES_ACTIVO);
  }

  @Test
  @Transactional
  void getAllNegociosByEsActivoIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    // Get all the negocioList where esActivo is not null
    defaultNegocioFiltering("esActivo.specified=true", "esActivo.specified=false");
  }

  private void defaultNegocioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
    defaultNegocioShouldBeFound(shouldBeFound);
    defaultNegocioShouldNotBeFound(shouldNotBeFound);
  }

  /**
   * Executes the search, and checks that the default entity is returned.
   */
  private void defaultNegocioShouldBeFound(String filter) throws Exception {
    restNegocioMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(negocio.getId().intValue())))
      .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
      .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
      .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
      .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
      .andExpect(jsonPath("$.[*].subscriptionKey").value(hasItem(DEFAULT_SUBSCRIPTION_KEY)))
      .andExpect(jsonPath("$.[*].esActivo").value(hasItem(DEFAULT_ES_ACTIVO)));

    // Check, that the count call also returns 1
    restNegocioMockMvc
      .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(content().string("1"));
  }

  /**
   * Executes the search, and checks that the default entity is not returned.
   */
  private void defaultNegocioShouldNotBeFound(String filter) throws Exception {
    restNegocioMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isEmpty());

    // Check, that the count call also returns 0
    restNegocioMockMvc
      .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(content().string("0"));
  }

  @Test
  @Transactional
  void getNonExistingNegocio() throws Exception {
    // Get the negocio
    restNegocioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingNegocio() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the negocio
    Negocio updatedNegocio = negocioRepository.findById(negocio.getId()).orElseThrow();
    // Disconnect from session so that the updates on updatedNegocio are not directly saved in db
    em.detach(updatedNegocio);
    updatedNegocio
      .nombre(UPDATED_NOMBRE)
      .responsable(UPDATED_RESPONSABLE)
      .celular(UPDATED_CELULAR)
      .correo(UPDATED_CORREO)
      .subscriptionKey(UPDATED_SUBSCRIPTION_KEY)
      .esActivo(UPDATED_ES_ACTIVO);
    NegocioDTO negocioDTO = negocioMapper.toDto(updatedNegocio);

    restNegocioMockMvc
      .perform(put(ENTITY_API_URL_ID, negocioDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isOk());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedNegocioToMatchAllProperties(updatedNegocio);
  }

  @Test
  @Transactional
  void putNonExistingNegocio() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    negocio.setId(longCount.incrementAndGet());

    // Create the Negocio
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restNegocioMockMvc
      .perform(put(ENTITY_API_URL_ID, negocioDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchNegocio() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    negocio.setId(longCount.incrementAndGet());

    // Create the Negocio
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNegocioMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(negocioDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamNegocio() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    negocio.setId(longCount.incrementAndGet());

    // Create the Negocio
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNegocioMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateNegocioWithPatch() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the negocio using partial update
    Negocio partialUpdatedNegocio = new Negocio();
    partialUpdatedNegocio.setId(negocio.getId());

    partialUpdatedNegocio.responsable(UPDATED_RESPONSABLE).celular(UPDATED_CELULAR);

    restNegocioMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedNegocio.getId())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedNegocio))
      )
      .andExpect(status().isOk());

    // Validate the Negocio in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertNegocioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNegocio, negocio), getPersistedNegocio(negocio));
  }

  @Test
  @Transactional
  void fullUpdateNegocioWithPatch() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the negocio using partial update
    Negocio partialUpdatedNegocio = new Negocio();
    partialUpdatedNegocio.setId(negocio.getId());

    partialUpdatedNegocio
      .nombre(UPDATED_NOMBRE)
      .responsable(UPDATED_RESPONSABLE)
      .celular(UPDATED_CELULAR)
      .correo(UPDATED_CORREO)
      .subscriptionKey(UPDATED_SUBSCRIPTION_KEY)
      .esActivo(UPDATED_ES_ACTIVO);

    restNegocioMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedNegocio.getId())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedNegocio))
      )
      .andExpect(status().isOk());

    // Validate the Negocio in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertNegocioUpdatableFieldsEquals(partialUpdatedNegocio, getPersistedNegocio(partialUpdatedNegocio));
  }

  @Test
  @Transactional
  void patchNonExistingNegocio() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    negocio.setId(longCount.incrementAndGet());

    // Create the Negocio
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restNegocioMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, negocioDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(negocioDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchNegocio() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    negocio.setId(longCount.incrementAndGet());

    // Create the Negocio
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNegocioMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(negocioDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamNegocio() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    negocio.setId(longCount.incrementAndGet());

    // Create the Negocio
    NegocioDTO negocioDTO = negocioMapper.toDto(negocio);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNegocioMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(negocioDTO)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Negocio in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteNegocio() throws Exception {
    // Initialize the database
    insertedNegocio = negocioRepository.saveAndFlush(negocio);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the negocio
    restNegocioMockMvc
      .perform(delete(ENTITY_API_URL_ID, negocio.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return negocioRepository.count();
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

  protected Negocio getPersistedNegocio(Negocio negocio) {
    return negocioRepository.findById(negocio.getId()).orElseThrow();
  }

  protected void assertPersistedNegocioToMatchAllProperties(Negocio expectedNegocio) {
    assertNegocioAllPropertiesEquals(expectedNegocio, getPersistedNegocio(expectedNegocio));
  }

  protected void assertPersistedNegocioToMatchUpdatableProperties(Negocio expectedNegocio) {
    assertNegocioAllUpdatablePropertiesEquals(expectedNegocio, getPersistedNegocio(expectedNegocio));
  }
}
