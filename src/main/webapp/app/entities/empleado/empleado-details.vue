<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="empleado">
        <h2 class="jh-entity-heading" data-cy="empleadoDetailsHeading"><span>Empleado</span> {{ empleado.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ empleado.nombre }}</span>
          </dd>
          <dt>
            <span>Celular</span>
          </dt>
          <dd>
            <span>{{ empleado.celular }}</span>
          </dd>
          <dt>
            <span>Especialidad</span>
          </dt>
          <dd>
            <span>{{ empleado.especialidad }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ empleado.activo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="empleado.id" :to="{ name: 'EmpleadoEdit', params: { empleadoId: empleado.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./empleado-details.component.ts"></script>
