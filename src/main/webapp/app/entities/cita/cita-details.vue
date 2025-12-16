<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cita">
        <h2 class="jh-entity-heading" data-cy="citaDetailsHeading"><span>Cita</span> {{ cita.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Fecha</span>
          </dt>
          <dd>
            <span>{{ cita.fecha }}</span>
          </dd>
          <dt>
            <span>Hora Inicio</span>
          </dt>
          <dd>
            <span>{{ cita.horaInicio }}</span>
          </dd>
          <dt>
            <span>Duracion Minutos</span>
          </dt>
          <dd>
            <span>{{ cita.duracionMinutos }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ cita.descripcion }}</span>
          </dd>
          <dt>
            <span>Es Urgente</span>
          </dt>
          <dd>
            <span>{{ cita.esUrgente }}</span>
          </dd>
          <dt>
            <span>Estado Cita</span>
          </dt>
          <dd>
            <span>{{ cita.estadoCita }}</span>
          </dd>
          <dt>
            <span>Cliente</span>
          </dt>
          <dd>
            <div v-if="cita.cliente">
              <router-link :to="{ name: 'ClienteView', params: { clienteId: cita.cliente.id } }">{{ cita.cliente.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Empleado</span>
          </dt>
          <dd>
            <div v-if="cita.empleado">
              <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: cita.empleado.id } }">{{ cita.empleado.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="cita.id" :to="{ name: 'CitaEdit', params: { citaId: cita.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cita-details.component.ts"></script>
