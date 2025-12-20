<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="servicio">
        <h2 class="jh-entity-heading" data-cy="servicioDetailsHeading"><span>Servicio</span> {{ servicio.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Fecha Atencion</span>
          </dt>
          <dd>
            <span v-if="servicio.fechaAtencion">{{ formatDateLong(servicio.fechaAtencion) }}</span>
          </dd>
          <dt>
            <span>Contenido</span>
          </dt>
          <dd>
            <span>{{ servicio.contenido }}</span>
          </dd>
          <dt>
            <span>Precio</span>
          </dt>
          <dd>
            <span>{{ servicio.precio }}</span>
          </dd>
          <dt>
            <span>Cita</span>
          </dt>
          <dd>
            <div v-if="servicio.cita">
              <router-link :to="{ name: 'CitaView', params: { citaId: servicio.cita.id } }">{{ servicio.cita.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="servicio.id" :to="{ name: 'ServicioEdit', params: { servicioId: servicio.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./servicio-details.component.ts"></script>
