package com.hmvsoluciones.saas.web.rest;

import static com.hmvsoluciones.saas.domain.ServicioAsserts.*;
import static com.hmvsoluciones.saas.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hmvsoluciones.saas.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.saas.IntegrationTest;
import com.hmvsoluciones.saas.domain.Cita;
import com.hmvsoluciones.saas.domain.Servicio;
import com.hmvsoluciones.saas.repository.ServicioRepository;
import com.hmvsoluciones.saas.service.dto.ServicioDTO;
import com.hmvsoluciones.saas.service.mapper.ServicioMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ServicioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServicioResourceIT {

    private static final Instant DEFAULT_FECHA_ATENCION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ATENCION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CONTENIDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTENIDO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/servicios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ServicioMapper servicioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioMockMvc;

    private Servicio servicio;

    private Servicio insertedServicio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createEntity(EntityManager em) {
        Servicio servicio = new Servicio().fechaAtencion(DEFAULT_FECHA_ATENCION).contenido(DEFAULT_CONTENIDO).precio(DEFAULT_PRECIO);
        // Add required entity
        Cita cita;
        if (TestUtil.findAll(em, Cita.class).isEmpty()) {
            cita = CitaResourceIT.createEntity(em);
            em.persist(cita);
            em.flush();
        } else {
            cita = TestUtil.findAll(em, Cita.class).get(0);
        }
        servicio.setCita(cita);
        return servicio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createUpdatedEntity(EntityManager em) {
        Servicio updatedServicio = new Servicio().fechaAtencion(UPDATED_FECHA_ATENCION).contenido(UPDATED_CONTENIDO).precio(UPDATED_PRECIO);
        // Add required entity
        Cita cita;
        if (TestUtil.findAll(em, Cita.class).isEmpty()) {
            cita = CitaResourceIT.createUpdatedEntity(em);
            em.persist(cita);
            em.flush();
        } else {
            cita = TestUtil.findAll(em, Cita.class).get(0);
        }
        updatedServicio.setCita(cita);
        return updatedServicio;
    }

    @BeforeEach
    void initTest() {
        servicio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServicio != null) {
            servicioRepository.delete(insertedServicio);
            insertedServicio = null;
        }
    }

    @Test
    @Transactional
    void createServicio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);
        var returnedServicioDTO = om.readValue(
            restServicioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServicioDTO.class
        );

        // Validate the Servicio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServicio = servicioMapper.toEntity(returnedServicioDTO);
        assertServicioUpdatableFieldsEquals(returnedServicio, getPersistedServicio(returnedServicio));

        insertedServicio = returnedServicio;
    }

    @Test
    @Transactional
    void createServicioWithExistingId() throws Exception {
        // Create the Servicio with an existing ID
        servicio.setId(1L);
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaAtencionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setFechaAtencion(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicios() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaAtencion").value(hasItem(DEFAULT_FECHA_ATENCION.toString())))
            .andExpect(jsonPath("$.[*].contenido").value(hasItem(DEFAULT_CONTENIDO)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))));
    }

    @Test
    @Transactional
    void getServicio() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get the servicio
        restServicioMockMvc
            .perform(get(ENTITY_API_URL_ID, servicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicio.getId().intValue()))
            .andExpect(jsonPath("$.fechaAtencion").value(DEFAULT_FECHA_ATENCION.toString()))
            .andExpect(jsonPath("$.contenido").value(DEFAULT_CONTENIDO))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)));
    }

    @Test
    @Transactional
    void getNonExistingServicio() throws Exception {
        // Get the servicio
        restServicioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicio() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicio
        Servicio updatedServicio = servicioRepository.findById(servicio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServicio are not directly saved in db
        em.detach(updatedServicio);
        updatedServicio.fechaAtencion(UPDATED_FECHA_ATENCION).contenido(UPDATED_CONTENIDO).precio(UPDATED_PRECIO);
        ServicioDTO servicioDTO = servicioMapper.toDto(updatedServicio);

        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicioToMatchAllProperties(updatedServicio);
    }

    @Test
    @Transactional
    void putNonExistingServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio.precio(UPDATED_PRECIO);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedServicio, servicio), getPersistedServicio(servicio));
    }

    @Test
    @Transactional
    void fullUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio.fechaAtencion(UPDATED_FECHA_ATENCION).contenido(UPDATED_CONTENIDO).precio(UPDATED_PRECIO);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioUpdatableFieldsEquals(partialUpdatedServicio, getPersistedServicio(partialUpdatedServicio));
    }

    @Test
    @Transactional
    void patchNonExistingServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicio() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servicio
        restServicioMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicioRepository.count();
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

    protected Servicio getPersistedServicio(Servicio servicio) {
        return servicioRepository.findById(servicio.getId()).orElseThrow();
    }

    protected void assertPersistedServicioToMatchAllProperties(Servicio expectedServicio) {
        assertServicioAllPropertiesEquals(expectedServicio, getPersistedServicio(expectedServicio));
    }

    protected void assertPersistedServicioToMatchUpdatableProperties(Servicio expectedServicio) {
        assertServicioAllUpdatablePropertiesEquals(expectedServicio, getPersistedServicio(expectedServicio));
    }
}
