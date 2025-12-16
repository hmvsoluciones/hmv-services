<template>
  <div>
    <h2 id="page-heading" data-cy="NegocioHeading">
      <span id="negocio-heading">Negocios</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'NegocioCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-negocio"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Negocio</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && negocios && negocios.length === 0">
      <span>Ningún Negocios encontrado</span>
    </div>
    <div class="table-responsive" v-if="negocios && negocios.length > 0">
      <table class="table table-striped" aria-describedby="negocios">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('responsable')">
              <span>Responsable</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'responsable'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('celular')">
              <span>Celular</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'celular'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('correo')">
              <span>Correo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'correo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('subscriptionKey')">
              <span>Subscription Key</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subscriptionKey'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('esActivo')">
              <span>Es Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'esActivo'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="negocio in negocios" :key="negocio.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'NegocioView', params: { negocioId: negocio.id } }">{{ negocio.id }}</router-link>
            </td>
            <td>{{ negocio.nombre }}</td>
            <td>{{ negocio.responsable }}</td>
            <td>{{ negocio.celular }}</td>
            <td>{{ negocio.correo }}</td>
            <td>{{ negocio.subscriptionKey }}</td>
            <td>{{ negocio.esActivo }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'NegocioView', params: { negocioId: negocio.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'NegocioEdit', params: { negocioId: negocio.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(negocio)"
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
        <span ref="infiniteScrollEl"></span>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="hmvsolucionesSaasMvcApp.negocio.delete.question" data-cy="negocioDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-negocio-heading">¿Seguro que quiere eliminar Negocio {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-negocio"
            data-cy="entityConfirmDeleteButton"
            @click="removeNegocio()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./negocio.component.ts"></script>
