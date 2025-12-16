<template>
  <div>
    <b-dropdown-item to="/cliente">
      <font-awesome-icon icon="asterisk" />
      <span>Cliente</span>
    </b-dropdown-item>
    <b-dropdown-item to="/cita">
      <font-awesome-icon icon="asterisk" />
      <span>Cita</span>
    </b-dropdown-item>
    <b-dropdown-item to="/servicio">
      <font-awesome-icon icon="asterisk" />
      <span>Servicio</span>
    </b-dropdown-item>
    <b-dropdown-item to="/template">
      <font-awesome-icon icon="asterisk" />
      <span>Template</span>
    </b-dropdown-item>
    <b-dropdown-item to="/negocio">
      <font-awesome-icon icon="asterisk" />
      <span>Negocio</span>
    </b-dropdown-item>
    <b-dropdown-item to="/empleado">
      <font-awesome-icon icon="asterisk" />
      <span>Empleado</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
