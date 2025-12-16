<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="template">
        <h2 class="jh-entity-heading" data-cy="templateDetailsHeading"><span>Template</span> {{ template.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ template.nombre }}</span>
          </dd>
          <dt>
            <span>Contenido Markdown</span>
          </dt>
          <dd>
            <span>{{ template.contenidoMarkdown }}</span>
          </dd>
          <dt>
            <span>Variables</span>
          </dt>
          <dd>
            <span>{{ template.variables }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ template.activo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="template.id" :to="{ name: 'TemplateEdit', params: { templateId: template.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./template-details.component.ts"></script>
