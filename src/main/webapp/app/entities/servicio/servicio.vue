<template>
  <div>
    <h2 id="page-heading" data-cy="ServicioHeading">
      <span id="servicio-heading">Servicios</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'ServicioCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-servicio"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Servicio</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && servicios && servicios.length === 0">
      <span>Ningún Servicios encontrado</span>
    </div>
    <div class="table-responsive" v-if="servicios && servicios.length > 0">
      <table class="table table-striped" aria-describedby="servicios">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Fecha Atencion</span></th>
            <th scope="row"><span>Informe</span></th>
            <th scope="row"><span>Precio</span></th>
            <th scope="row"><span>Cita</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="servicio in servicios" :key="servicio.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ServicioView', params: { servicioId: servicio.id } }">{{ servicio.id }}</router-link>
            </td>
            <td>{{ formatDateShort(servicio.fechaAtencion) || '' }}</td>
            <td>{{ servicio.informe }}</td>
            <td>{{ servicio.precio }}</td>
            <td>
              <div v-if="servicio.cita">
                <router-link :to="{ name: 'CitaView', params: { citaId: servicio.cita.id } }">{{ servicio.cita.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ServicioView', params: { servicioId: servicio.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ServicioEdit', params: { servicioId: servicio.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(servicio)"
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
        <span id="hmvsolucionesSaasMvcApp.servicio.delete.question" data-cy="servicioDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-servicio-heading">¿Seguro que quiere eliminar Servicio {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-servicio"
            data-cy="entityConfirmDeleteButton"
            @click="removeServicio()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./servicio.component.ts"></script>
