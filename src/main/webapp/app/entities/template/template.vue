<template>
  <div>
    <h2 id="page-heading" data-cy="TemplateHeading">
      <span id="template-heading">Templates</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'TemplateCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-template"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Template</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && templates && templates.length === 0">
      <span>Ningún Templates encontrado</span>
    </div>
    <div class="table-responsive" v-if="templates && templates.length > 0">
      <table class="table table-striped" aria-describedby="templates">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('contenido')">
              <span>Contenido</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contenido'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('activo')">
              <span>Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="template in templates" :key="template.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TemplateView', params: { templateId: template.id } }">{{ template.id }}</router-link>
            </td>
            <td>{{ template.nombre }}</td>
            <td>{{ template.contenido }}</td>
            <td>{{ template.activo }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TemplateView', params: { templateId: template.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TemplateEdit', params: { templateId: template.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(template)"
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
        <span id="hmvsolucionesSaasMvcApp.template.delete.question" data-cy="templateDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-template-heading">¿Seguro que quiere eliminar Template {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-template"
            data-cy="entityConfirmDeleteButton"
            @click="removeTemplate()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./template.component.ts"></script>
