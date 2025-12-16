<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cliente">
        <h2 class="jh-entity-heading" data-cy="clienteDetailsHeading"><span>Cliente</span> {{ cliente.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ cliente.nombre }}</span>
          </dd>
          <dt>
            <span>Celular</span>
          </dt>
          <dd>
            <span>{{ cliente.celular }}</span>
          </dd>
          <dt>
            <span>Correo</span>
          </dt>
          <dd>
            <span>{{ cliente.correo }}</span>
          </dd>
          <dt>
            <span>Identificacion</span>
          </dt>
          <dd>
            <span>{{ cliente.identificacion }}</span>
          </dd>
          <dt>
            <span>Direccion</span>
          </dt>
          <dd>
            <span>{{ cliente.direccion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ cliente.activo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="cliente.id" :to="{ name: 'ClienteEdit', params: { clienteId: cliente.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cliente-details.component.ts"></script>
