<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="negocio">
        <h2 class="jh-entity-heading" data-cy="negocioDetailsHeading"><span>Negocio</span> {{ negocio.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ negocio.nombre }}</span>
          </dd>
          <dt>
            <span>Responsable</span>
          </dt>
          <dd>
            <span>{{ negocio.responsable }}</span>
          </dd>
          <dt>
            <span>Celular</span>
          </dt>
          <dd>
            <span>{{ negocio.celular }}</span>
          </dd>
          <dt>
            <span>Correo</span>
          </dt>
          <dd>
            <span>{{ negocio.correo }}</span>
          </dd>
          <dt>
            <span>Subscription Key</span>
          </dt>
          <dd>
            <span>{{ negocio.subscriptionKey }}</span>
          </dd>
          <dt>
            <span>Es Activo</span>
          </dt>
          <dd>
            <span>{{ negocio.esActivo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="negocio.id" :to="{ name: 'NegocioEdit', params: { negocioId: negocio.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./negocio-details.component.ts"></script>
