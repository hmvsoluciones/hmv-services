package com.hmvsoluciones.saas.web.rest;

import static com.hmvsoluciones.saas.domain.ClienteAsserts.*;
import static com.hmvsoluciones.saas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.saas.IntegrationTest;
import com.hmvsoluciones.saas.domain.Cliente;
import com.hmvsoluciones.saas.repository.ClienteRepository;
import com.hmvsoluciones.saas.service.dto.ClienteDTO;
import com.hmvsoluciones.saas.service.mapper.ClienteMapper;
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
 * Integration tests for the {@link ClienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClienteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICACION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    private Cliente insertedCliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity() {
        return new Cliente()
            .nombre(DEFAULT_NOMBRE)
            .celular(DEFAULT_CELULAR)
            .correo(DEFAULT_CORREO)
            .identificacion(DEFAULT_IDENTIFICACION)
            .direccion(DEFAULT_DIRECCION)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createUpdatedEntity() {
        return new Cliente()
            .nombre(UPDATED_NOMBRE)
            .celular(UPDATED_CELULAR)
            .correo(UPDATED_CORREO)
            .identificacion(UPDATED_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        cliente = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCliente != null) {
            clienteRepository.delete(insertedCliente);
            insertedCliente = null;
        }
    }

    @Test
    @Transactional
    void createCliente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);
        var returnedClienteDTO = om.readValue(
            restClienteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClienteDTO.class
        );

        // Validate the Cliente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCliente = clienteMapper.toEntity(returnedClienteDTO);
        assertClienteUpdatableFieldsEquals(returnedCliente, getPersistedCliente(returnedCliente));

        insertedCliente = returnedCliente;
    }

    @Test
    @Transactional
    void createClienteWithExistingId() throws Exception {
        // Create the Cliente with an existing ID
        cliente.setId(1L);
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setNombre(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCelularIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setCelular(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientes() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getCliente() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
            .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO))
            .andExpect(jsonPath("$.identificacion").value(DEFAULT_IDENTIFICACION))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getClientesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        Long id = cliente.getId();

        defaultClienteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClienteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClienteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre equals to
        defaultClienteFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre in
        defaultClienteFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre is not null
        defaultClienteFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre contains
        defaultClienteFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre does not contain
        defaultClienteFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByCelularIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where celular equals to
        defaultClienteFiltering("celular.equals=" + DEFAULT_CELULAR, "celular.equals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllClientesByCelularIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where celular in
        defaultClienteFiltering("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR, "celular.in=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllClientesByCelularIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where celular is not null
        defaultClienteFiltering("celular.specified=true", "celular.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByCelularContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where celular contains
        defaultClienteFiltering("celular.contains=" + DEFAULT_CELULAR, "celular.contains=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllClientesByCelularNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where celular does not contain
        defaultClienteFiltering("celular.doesNotContain=" + UPDATED_CELULAR, "celular.doesNotContain=" + DEFAULT_CELULAR);
    }

    @Test
    @Transactional
    void getAllClientesByCorreoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo equals to
        defaultClienteFiltering("correo.equals=" + DEFAULT_CORREO, "correo.equals=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllClientesByCorreoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo in
        defaultClienteFiltering("correo.in=" + DEFAULT_CORREO + "," + UPDATED_CORREO, "correo.in=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllClientesByCorreoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo is not null
        defaultClienteFiltering("correo.specified=true", "correo.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByCorreoContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo contains
        defaultClienteFiltering("correo.contains=" + DEFAULT_CORREO, "correo.contains=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllClientesByCorreoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo does not contain
        defaultClienteFiltering("correo.doesNotContain=" + UPDATED_CORREO, "correo.doesNotContain=" + DEFAULT_CORREO);
    }

    @Test
    @Transactional
    void getAllClientesByIdentificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where identificacion equals to
        defaultClienteFiltering("identificacion.equals=" + DEFAULT_IDENTIFICACION, "identificacion.equals=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllClientesByIdentificacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where identificacion in
        defaultClienteFiltering(
            "identificacion.in=" + DEFAULT_IDENTIFICACION + "," + UPDATED_IDENTIFICACION,
            "identificacion.in=" + UPDATED_IDENTIFICACION
        );
    }

    @Test
    @Transactional
    void getAllClientesByIdentificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where identificacion is not null
        defaultClienteFiltering("identificacion.specified=true", "identificacion.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByIdentificacionContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where identificacion contains
        defaultClienteFiltering("identificacion.contains=" + DEFAULT_IDENTIFICACION, "identificacion.contains=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllClientesByIdentificacionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where identificacion does not contain
        defaultClienteFiltering(
            "identificacion.doesNotContain=" + UPDATED_IDENTIFICACION,
            "identificacion.doesNotContain=" + DEFAULT_IDENTIFICACION
        );
    }

    @Test
    @Transactional
    void getAllClientesByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where direccion equals to
        defaultClienteFiltering("direccion.equals=" + DEFAULT_DIRECCION, "direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllClientesByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where direccion in
        defaultClienteFiltering("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION, "direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllClientesByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where direccion is not null
        defaultClienteFiltering("direccion.specified=true", "direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByDireccionContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where direccion contains
        defaultClienteFiltering("direccion.contains=" + DEFAULT_DIRECCION, "direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllClientesByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where direccion does not contain
        defaultClienteFiltering("direccion.doesNotContain=" + UPDATED_DIRECCION, "direccion.doesNotContain=" + DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void getAllClientesByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where activo equals to
        defaultClienteFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllClientesByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where activo in
        defaultClienteFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllClientesByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where activo is not null
        defaultClienteFiltering("activo.specified=true", "activo.specified=false");
    }

    private void defaultClienteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClienteShouldBeFound(shouldBeFound);
        defaultClienteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClienteShouldBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClienteShouldNotBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCliente() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente
            .nombre(UPDATED_NOMBRE)
            .celular(UPDATED_CELULAR)
            .correo(UPDATED_CORREO)
            .identificacion(UPDATED_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO);
        ClienteDTO clienteDTO = clienteMapper.toDto(updatedCliente);

        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClienteToMatchAllProperties(updatedCliente);
    }

    @Test
    @Transactional
    void putNonExistingCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente.nombre(UPDATED_NOMBRE).celular(UPDATED_CELULAR).direccion(UPDATED_DIRECCION).activo(UPDATED_ACTIVO);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCliente, cliente), getPersistedCliente(cliente));
    }

    @Test
    @Transactional
    void fullUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente
            .nombre(UPDATED_NOMBRE)
            .celular(UPDATED_CELULAR)
            .correo(UPDATED_CORREO)
            .identificacion(UPDATED_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteUpdatableFieldsEquals(partialUpdatedCliente, getPersistedCliente(partialUpdatedCliente));
    }

    @Test
    @Transactional
    void patchNonExistingCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCliente() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cliente
        restClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cliente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clienteRepository.count();
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

    protected Cliente getPersistedCliente(Cliente cliente) {
        return clienteRepository.findById(cliente.getId()).orElseThrow();
    }

    protected void assertPersistedClienteToMatchAllProperties(Cliente expectedCliente) {
        assertClienteAllPropertiesEquals(expectedCliente, getPersistedCliente(expectedCliente));
    }

    protected void assertPersistedClienteToMatchUpdatableProperties(Cliente expectedCliente) {
        assertClienteAllUpdatablePropertiesEquals(expectedCliente, getPersistedCliente(expectedCliente));
    }
}
