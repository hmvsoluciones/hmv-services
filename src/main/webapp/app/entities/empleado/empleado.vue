<template>
  <div>
    <h2 id="page-heading" data-cy="EmpleadoHeading">
      <span id="empleado-heading">Empleados</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'EmpleadoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-empleado"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Empleado</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && empleados && empleados.length === 0">
      <span>Ningún Empleados encontrado</span>
    </div>
    <div class="table-responsive" v-if="empleados && empleados.length > 0">
      <table class="table table-striped" aria-describedby="empleados">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('celular')">
              <span>Celular</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'celular'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('especialidad')">
              <span>Especialidad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'especialidad'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('activo')">
              <span>Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="empleado in empleados" :key="empleado.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: empleado.id } }">{{ empleado.id }}</router-link>
            </td>
            <td>{{ empleado.nombre }}</td>
            <td>{{ empleado.celular }}</td>
            <td>{{ empleado.especialidad }}</td>
            <td>{{ empleado.activo }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: empleado.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EmpleadoEdit', params: { empleadoId: empleado.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(empleado)"
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
        <span id="hmvsolucionesSaasMvcApp.empleado.delete.question" data-cy="empleadoDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-empleado-heading">¿Seguro que quiere eliminar Empleado {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-empleado"
            data-cy="entityConfirmDeleteButton"
            @click="removeEmpleado()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="empleados && empleados.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./empleado.component.ts"></script>
