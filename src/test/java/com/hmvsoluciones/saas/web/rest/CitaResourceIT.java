package com.hmvsoluciones.saas.web.rest;

import static com.hmvsoluciones.saas.domain.CitaAsserts.*;
import static com.hmvsoluciones.saas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.saas.IntegrationTest;
import com.hmvsoluciones.saas.domain.Cita;
import com.hmvsoluciones.saas.domain.Cliente;
import com.hmvsoluciones.saas.domain.Empleado;
import com.hmvsoluciones.saas.domain.enumeration.EstadoCita;
import com.hmvsoluciones.saas.repository.CitaRepository;
import com.hmvsoluciones.saas.service.dto.CitaDTO;
import com.hmvsoluciones.saas.service.mapper.CitaMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
 * Integration tests for the {@link CitaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CitaResourceIT {

  private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

  private static final LocalTime DEFAULT_HORA_INICIO = LocalTime.NOON;
  private static final LocalTime UPDATED_HORA_INICIO = LocalTime.MAX.withNano(0);

  private static final Integer DEFAULT_DURACION_MINUTOS = 0;
  private static final Integer UPDATED_DURACION_MINUTOS = 1;
  private static final Integer SMALLER_DURACION_MINUTOS = 0 - 1;

  private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

  private static final Boolean DEFAULT_ES_URGENTE = false;
  private static final Boolean UPDATED_ES_URGENTE = true;

  private static final EstadoCita DEFAULT_ESTADO_CITA = EstadoCita.PROGRAMADA;
  private static final EstadoCita UPDATED_ESTADO_CITA = EstadoCita.CONFIRMADA;

  private static final String ENTITY_API_URL = "/api/citas";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private ObjectMapper om;

  @Autowired
  private CitaRepository citaRepository;

  @Autowired
  private CitaMapper citaMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restCitaMockMvc;

  private Cita cita;

  private Cita insertedCita;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Cita createEntity(EntityManager em) {
    Cita cita = new Cita()
      .fecha(DEFAULT_FECHA)
      .horaInicio(DEFAULT_HORA_INICIO)
      .duracionMinutos(DEFAULT_DURACION_MINUTOS)
      .descripcion(DEFAULT_DESCRIPCION)
      .esUrgente(DEFAULT_ES_URGENTE)
      .estadoCita(DEFAULT_ESTADO_CITA);
    // Add required entity
    Cliente cliente;
    if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
      cliente = ClienteResourceIT.createEntity();
      em.persist(cliente);
      em.flush();
    } else {
      cliente = TestUtil.findAll(em, Cliente.class).get(0);
    }
    cita.setCliente(cliente);
    return cita;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Cita createUpdatedEntity(EntityManager em) {
    Cita updatedCita = new Cita()
      .fecha(UPDATED_FECHA)
      .horaInicio(UPDATED_HORA_INICIO)
      .duracionMinutos(UPDATED_DURACION_MINUTOS)
      .descripcion(UPDATED_DESCRIPCION)
      .esUrgente(UPDATED_ES_URGENTE)
      .estadoCita(UPDATED_ESTADO_CITA);
    // Add required entity
    Cliente cliente;
    if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
      cliente = ClienteResourceIT.createUpdatedEntity();
      em.persist(cliente);
      em.flush();
    } else {
      cliente = TestUtil.findAll(em, Cliente.class).get(0);
    }
    updatedCita.setCliente(cliente);
    return updatedCita;
  }

  @BeforeEach
  void initTest() {
    cita = createEntity(em);
  }

  @AfterEach
  void cleanup() {
    if (insertedCita != null) {
      citaRepository.delete(insertedCita);
      insertedCita = null;
    }
  }

  @Test
  @Transactional
  void createCita() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the Cita
    CitaDTO citaDTO = citaMapper.toDto(cita);
    var returnedCitaDTO = om.readValue(
      restCitaMockMvc
        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      CitaDTO.class
    );

    // Validate the Cita in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    var returnedCita = citaMapper.toEntity(returnedCitaDTO);
    assertCitaUpdatableFieldsEquals(returnedCita, getPersistedCita(returnedCita));

    insertedCita = returnedCita;
  }

  @Test
  @Transactional
  void createCitaWithExistingId() throws Exception {
    // Create the Cita with an existing ID
    cita.setId(1L);
    CitaDTO citaDTO = citaMapper.toDto(cita);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restCitaMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkFechaIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    cita.setFecha(null);

    // Create the Cita, which fails.
    CitaDTO citaDTO = citaMapper.toDto(cita);

    restCitaMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkHoraInicioIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    cita.setHoraInicio(null);

    // Create the Cita, which fails.
    CitaDTO citaDTO = citaMapper.toDto(cita);

    restCitaMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkDescripcionIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    cita.setDescripcion(null);

    // Create the Cita, which fails.
    CitaDTO citaDTO = citaMapper.toDto(cita);

    restCitaMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllCitas() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList
    restCitaMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
      .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
      .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
      .andExpect(jsonPath("$.[*].duracionMinutos").value(hasItem(DEFAULT_DURACION_MINUTOS)))
      .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
      .andExpect(jsonPath("$.[*].esUrgente").value(hasItem(DEFAULT_ES_URGENTE)))
      .andExpect(jsonPath("$.[*].estadoCita").value(hasItem(DEFAULT_ESTADO_CITA.toString())));
  }

  @Test
  @Transactional
  void getCita() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get the cita
    restCitaMockMvc
      .perform(get(ENTITY_API_URL_ID, cita.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(cita.getId().intValue()))
      .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
      .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO.toString()))
      .andExpect(jsonPath("$.duracionMinutos").value(DEFAULT_DURACION_MINUTOS))
      .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
      .andExpect(jsonPath("$.esUrgente").value(DEFAULT_ES_URGENTE))
      .andExpect(jsonPath("$.estadoCita").value(DEFAULT_ESTADO_CITA.toString()));
  }

  @Test
  @Transactional
  void getCitasByIdFiltering() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    Long id = cita.getId();

    defaultCitaFiltering("id.equals=" + id, "id.notEquals=" + id);

    defaultCitaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

    defaultCitaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
  }

  @Test
  @Transactional
  void getAllCitasByFechaIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where fecha equals to
    defaultCitaFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
  }

  @Test
  @Transactional
  void getAllCitasByFechaIsInShouldWork() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where fecha in
    defaultCitaFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
  }

  @Test
  @Transactional
  void getAllCitasByFechaIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where fecha is not null
    defaultCitaFiltering("fecha.specified=true", "fecha.specified=false");
  }

  @Test
  @Transactional
  void getAllCitasByFechaIsGreaterThanOrEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where fecha is greater than or equal to
    defaultCitaFiltering("fecha.greaterThanOrEqual=" + DEFAULT_FECHA, "fecha.greaterThanOrEqual=" + UPDATED_FECHA);
  }

  @Test
  @Transactional
  void getAllCitasByFechaIsLessThanOrEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where fecha is less than or equal to
    defaultCitaFiltering("fecha.lessThanOrEqual=" + DEFAULT_FECHA, "fecha.lessThanOrEqual=" + SMALLER_FECHA);
  }

  @Test
  @Transactional
  void getAllCitasByFechaIsLessThanSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where fecha is less than
    defaultCitaFiltering("fecha.lessThan=" + UPDATED_FECHA, "fecha.lessThan=" + DEFAULT_FECHA);
  }

  @Test
  @Transactional
  void getAllCitasByFechaIsGreaterThanSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where fecha is greater than
    defaultCitaFiltering("fecha.greaterThan=" + SMALLER_FECHA, "fecha.greaterThan=" + DEFAULT_FECHA);
  }

  @Test
  @Transactional
  void getAllCitasByHoraInicioIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where horaInicio equals to
    defaultCitaFiltering("horaInicio.equals=" + DEFAULT_HORA_INICIO, "horaInicio.equals=" + UPDATED_HORA_INICIO);
  }

  @Test
  @Transactional
  void getAllCitasByHoraInicioIsInShouldWork() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where horaInicio in
    defaultCitaFiltering("horaInicio.in=" + DEFAULT_HORA_INICIO + "," + UPDATED_HORA_INICIO, "horaInicio.in=" + UPDATED_HORA_INICIO);
  }

  @Test
  @Transactional
  void getAllCitasByHoraInicioIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where horaInicio is not null
    defaultCitaFiltering("horaInicio.specified=true", "horaInicio.specified=false");
  }

  @Test
  @Transactional
  void getAllCitasByDuracionMinutosIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where duracionMinutos equals to
    defaultCitaFiltering("duracionMinutos.equals=" + DEFAULT_DURACION_MINUTOS, "duracionMinutos.equals=" + UPDATED_DURACION_MINUTOS);
  }

  @Test
  @Transactional
  void getAllCitasByDuracionMinutosIsInShouldWork() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where duracionMinutos in
    defaultCitaFiltering(
      "duracionMinutos.in=" + DEFAULT_DURACION_MINUTOS + "," + UPDATED_DURACION_MINUTOS,
      "duracionMinutos.in=" + UPDATED_DURACION_MINUTOS
    );
  }

  @Test
  @Transactional
  void getAllCitasByDuracionMinutosIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where duracionMinutos is not null
    defaultCitaFiltering("duracionMinutos.specified=true", "duracionMinutos.specified=false");
  }

  @Test
  @Transactional
  void getAllCitasByDuracionMinutosIsGreaterThanOrEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where duracionMinutos is greater than or equal to
    defaultCitaFiltering(
      "duracionMinutos.greaterThanOrEqual=" + DEFAULT_DURACION_MINUTOS,
      "duracionMinutos.greaterThanOrEqual=" + (DEFAULT_DURACION_MINUTOS + 1)
    );
  }

  @Test
  @Transactional
  void getAllCitasByDuracionMinutosIsLessThanOrEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where duracionMinutos is less than or equal to
    defaultCitaFiltering(
      "duracionMinutos.lessThanOrEqual=" + DEFAULT_DURACION_MINUTOS,
      "duracionMinutos.lessThanOrEqual=" + SMALLER_DURACION_MINUTOS
    );
  }

  @Test
  @Transactional
  void getAllCitasByDuracionMinutosIsLessThanSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where duracionMinutos is less than
    defaultCitaFiltering(
      "duracionMinutos.lessThan=" + (DEFAULT_DURACION_MINUTOS + 1),
      "duracionMinutos.lessThan=" + DEFAULT_DURACION_MINUTOS
    );
  }

  @Test
  @Transactional
  void getAllCitasByDuracionMinutosIsGreaterThanSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where duracionMinutos is greater than
    defaultCitaFiltering(
      "duracionMinutos.greaterThan=" + SMALLER_DURACION_MINUTOS,
      "duracionMinutos.greaterThan=" + DEFAULT_DURACION_MINUTOS
    );
  }

  @Test
  @Transactional
  void getAllCitasByDescripcionIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where descripcion equals to
    defaultCitaFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
  }

  @Test
  @Transactional
  void getAllCitasByDescripcionIsInShouldWork() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where descripcion in
    defaultCitaFiltering("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION, "descripcion.in=" + UPDATED_DESCRIPCION);
  }

  @Test
  @Transactional
  void getAllCitasByDescripcionIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where descripcion is not null
    defaultCitaFiltering("descripcion.specified=true", "descripcion.specified=false");
  }

  @Test
  @Transactional
  void getAllCitasByDescripcionContainsSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where descripcion contains
    defaultCitaFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
  }

  @Test
  @Transactional
  void getAllCitasByDescripcionNotContainsSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where descripcion does not contain
    defaultCitaFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
  }

  @Test
  @Transactional
  void getAllCitasByEsUrgenteIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where esUrgente equals to
    defaultCitaFiltering("esUrgente.equals=" + DEFAULT_ES_URGENTE, "esUrgente.equals=" + UPDATED_ES_URGENTE);
  }

  @Test
  @Transactional
  void getAllCitasByEsUrgenteIsInShouldWork() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where esUrgente in
    defaultCitaFiltering("esUrgente.in=" + DEFAULT_ES_URGENTE + "," + UPDATED_ES_URGENTE, "esUrgente.in=" + UPDATED_ES_URGENTE);
  }

  @Test
  @Transactional
  void getAllCitasByEsUrgenteIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where esUrgente is not null
    defaultCitaFiltering("esUrgente.specified=true", "esUrgente.specified=false");
  }

  @Test
  @Transactional
  void getAllCitasByEstadoCitaIsEqualToSomething() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where estadoCita equals to
    defaultCitaFiltering("estadoCita.equals=" + DEFAULT_ESTADO_CITA, "estadoCita.equals=" + UPDATED_ESTADO_CITA);
  }

  @Test
  @Transactional
  void getAllCitasByEstadoCitaIsInShouldWork() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where estadoCita in
    defaultCitaFiltering("estadoCita.in=" + DEFAULT_ESTADO_CITA + "," + UPDATED_ESTADO_CITA, "estadoCita.in=" + UPDATED_ESTADO_CITA);
  }

  @Test
  @Transactional
  void getAllCitasByEstadoCitaIsNullOrNotNull() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    // Get all the citaList where estadoCita is not null
    defaultCitaFiltering("estadoCita.specified=true", "estadoCita.specified=false");
  }

  @Test
  @Transactional
  void getAllCitasByClienteIsEqualToSomething() throws Exception {
    Cliente cliente;
    if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
      citaRepository.saveAndFlush(cita);
      cliente = ClienteResourceIT.createEntity();
    } else {
      cliente = TestUtil.findAll(em, Cliente.class).get(0);
    }
    em.persist(cliente);
    em.flush();
    cita.setCliente(cliente);
    citaRepository.saveAndFlush(cita);
    Long clienteId = cliente.getId();
    // Get all the citaList where cliente equals to clienteId
    defaultCitaShouldBeFound("clienteId.equals=" + clienteId);

    // Get all the citaList where cliente equals to (clienteId + 1)
    defaultCitaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
  }

  @Test
  @Transactional
  void getAllCitasByEmpleadoIsEqualToSomething() throws Exception {
    Empleado empleado;
    if (TestUtil.findAll(em, Empleado.class).isEmpty()) {
      citaRepository.saveAndFlush(cita);
      empleado = EmpleadoResourceIT.createEntity();
    } else {
      empleado = TestUtil.findAll(em, Empleado.class).get(0);
    }
    em.persist(empleado);
    em.flush();
    cita.setEmpleado(empleado);
    citaRepository.saveAndFlush(cita);
    Long empleadoId = empleado.getId();
    // Get all the citaList where empleado equals to empleadoId
    defaultCitaShouldBeFound("empleadoId.equals=" + empleadoId);

    // Get all the citaList where empleado equals to (empleadoId + 1)
    defaultCitaShouldNotBeFound("empleadoId.equals=" + (empleadoId + 1));
  }

  private void defaultCitaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
    defaultCitaShouldBeFound(shouldBeFound);
    defaultCitaShouldNotBeFound(shouldNotBeFound);
  }

  /**
   * Executes the search, and checks that the default entity is returned.
   */
  private void defaultCitaShouldBeFound(String filter) throws Exception {
    restCitaMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
      .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
      .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
      .andExpect(jsonPath("$.[*].duracionMinutos").value(hasItem(DEFAULT_DURACION_MINUTOS)))
      .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
      .andExpect(jsonPath("$.[*].esUrgente").value(hasItem(DEFAULT_ES_URGENTE)))
      .andExpect(jsonPath("$.[*].estadoCita").value(hasItem(DEFAULT_ESTADO_CITA.toString())));

    // Check, that the count call also returns 1
    restCitaMockMvc
      .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(content().string("1"));
  }

  /**
   * Executes the search, and checks that the default entity is not returned.
   */
  private void defaultCitaShouldNotBeFound(String filter) throws Exception {
    restCitaMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isEmpty());

    // Check, that the count call also returns 0
    restCitaMockMvc
      .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(content().string("0"));
  }

  @Test
  @Transactional
  void getNonExistingCita() throws Exception {
    // Get the cita
    restCitaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingCita() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the cita
    Cita updatedCita = citaRepository.findById(cita.getId()).orElseThrow();
    // Disconnect from session so that the updates on updatedCita are not directly saved in db
    em.detach(updatedCita);
    updatedCita
      .fecha(UPDATED_FECHA)
      .horaInicio(UPDATED_HORA_INICIO)
      .duracionMinutos(UPDATED_DURACION_MINUTOS)
      .descripcion(UPDATED_DESCRIPCION)
      .esUrgente(UPDATED_ES_URGENTE)
      .estadoCita(UPDATED_ESTADO_CITA);
    CitaDTO citaDTO = citaMapper.toDto(updatedCita);

    restCitaMockMvc
      .perform(put(ENTITY_API_URL_ID, citaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isOk());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedCitaToMatchAllProperties(updatedCita);
  }

  @Test
  @Transactional
  void putNonExistingCita() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    cita.setId(longCount.incrementAndGet());

    // Create the Cita
    CitaDTO citaDTO = citaMapper.toDto(cita);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCitaMockMvc
      .perform(put(ENTITY_API_URL_ID, citaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchCita() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    cita.setId(longCount.incrementAndGet());

    // Create the Cita
    CitaDTO citaDTO = citaMapper.toDto(cita);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCitaMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamCita() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    cita.setId(longCount.incrementAndGet());

    // Create the Cita
    CitaDTO citaDTO = citaMapper.toDto(cita);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCitaMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateCitaWithPatch() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the cita using partial update
    Cita partialUpdatedCita = new Cita();
    partialUpdatedCita.setId(cita.getId());

    partialUpdatedCita.horaInicio(UPDATED_HORA_INICIO).descripcion(UPDATED_DESCRIPCION);

    restCitaMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedCita))
      )
      .andExpect(status().isOk());

    // Validate the Cita in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertCitaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCita, cita), getPersistedCita(cita));
  }

  @Test
  @Transactional
  void fullUpdateCitaWithPatch() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the cita using partial update
    Cita partialUpdatedCita = new Cita();
    partialUpdatedCita.setId(cita.getId());

    partialUpdatedCita
      .fecha(UPDATED_FECHA)
      .horaInicio(UPDATED_HORA_INICIO)
      .duracionMinutos(UPDATED_DURACION_MINUTOS)
      .descripcion(UPDATED_DESCRIPCION)
      .esUrgente(UPDATED_ES_URGENTE)
      .estadoCita(UPDATED_ESTADO_CITA);

    restCitaMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedCita))
      )
      .andExpect(status().isOk());

    // Validate the Cita in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertCitaUpdatableFieldsEquals(partialUpdatedCita, getPersistedCita(partialUpdatedCita));
  }

  @Test
  @Transactional
  void patchNonExistingCita() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    cita.setId(longCount.incrementAndGet());

    // Create the Cita
    CitaDTO citaDTO = citaMapper.toDto(cita);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCitaMockMvc
      .perform(patch(ENTITY_API_URL_ID, citaDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchCita() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    cita.setId(longCount.incrementAndGet());

    // Create the Cita
    CitaDTO citaDTO = citaMapper.toDto(cita);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCitaMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(citaDTO))
      )
      .andExpect(status().isBadRequest());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamCita() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    cita.setId(longCount.incrementAndGet());

    // Create the Cita
    CitaDTO citaDTO = citaMapper.toDto(cita);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCitaMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citaDTO)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Cita in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteCita() throws Exception {
    // Initialize the database
    insertedCita = citaRepository.saveAndFlush(cita);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the cita
    restCitaMockMvc.perform(delete(ENTITY_API_URL_ID, cita.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return citaRepository.count();
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

  protected Cita getPersistedCita(Cita cita) {
    return citaRepository.findById(cita.getId()).orElseThrow();
  }

  protected void assertPersistedCitaToMatchAllProperties(Cita expectedCita) {
    assertCitaAllPropertiesEquals(expectedCita, getPersistedCita(expectedCita));
  }

  protected void assertPersistedCitaToMatchUpdatableProperties(Cita expectedCita) {
    assertCitaAllUpdatablePropertiesEquals(expectedCita, getPersistedCita(expectedCita));
  }
}
