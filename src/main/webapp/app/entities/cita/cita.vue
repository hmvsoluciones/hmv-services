<template>
  <div>
    <h2 id="page-heading" data-cy="CitaHeading">
      <span id="cita-heading">Citas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'CitaCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-cita">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Cita</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && citas && citas.length === 0">
      <span>Ningún Citas encontrado</span>
    </div>
    <div class="table-responsive" v-if="citas && citas.length > 0">
      <table class="table table-striped" aria-describedby="citas">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fecha')">
              <span>Fecha</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('horaInicio')">
              <span>Hora Inicio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'horaInicio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('duracionMinutos')">
              <span>Duracion Minutos</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'duracionMinutos'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('descripcion')">
              <span>Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('esUrgente')">
              <span>Es Urgente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'esUrgente'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('estadoCita')">
              <span>Estado Cita</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estadoCita'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cliente.id')">
              <span>Cliente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cliente.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('empleado.id')">
              <span>Empleado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'empleado.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CitaView', params: { citaId: cita.id } }">{{ cita.id }}</router-link>
            </td>
            <td>{{ cita.fecha }}</td>
            <td>{{ cita.horaInicio }}</td>
            <td>{{ cita.duracionMinutos }}</td>
            <td>{{ cita.descripcion }}</td>
            <td>{{ cita.esUrgente }}</td>
            <td>{{ cita.estadoCita }}</td>
            <td>
              <div v-if="cita.cliente">
                <router-link :to="{ name: 'ClienteView', params: { clienteId: cita.cliente.id } }">{{ cita.cliente.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cita.empleado">
                <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: cita.empleado.id } }">{{ cita.empleado.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CitaView', params: { citaId: cita.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CitaEdit', params: { citaId: cita.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(cita)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Eliminar</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="hmvsolucionesSaasMvcApp.cita.delete.question" data-cy="citaDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-cita-heading">¿Seguro que quiere eliminar Cita {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-cita"
            data-cy="entityConfirmDeleteButton"
            @click="removeCita()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="citas && citas.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cita.component.ts"></script>
