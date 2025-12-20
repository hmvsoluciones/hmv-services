<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.servicio.home.createOrEditLabel" data-cy="ServicioCreateUpdateHeading">Crear o editar Servicio</h2>
        <div>
          <div class="form-group" v-if="servicio.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="servicio.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="servicio-fechaAtencion">Fecha Atencion</label>
            <div class="d-flex">
              <input
                id="servicio-fechaAtencion"
                data-cy="fechaAtencion"
                type="datetime-local"
                class="form-control"
                name="fechaAtencion"
                :class="{ valid: !v$.fechaAtencion.$invalid, invalid: v$.fechaAtencion.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaAtencion.$model)"
                @change="updateInstantField('fechaAtencion', $event)"
              />
            </div>
            <div v-if="v$.fechaAtencion.$anyDirty && v$.fechaAtencion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaAtencion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="servicio-contenido">Contenido</label>
            <textarea
              class="form-control"
              name="contenido"
              id="servicio-contenido"
              data-cy="contenido"
              :class="{ valid: !v$.contenido.$invalid, invalid: v$.contenido.$invalid }"
              v-model="v$.contenido.$model"
              required
            ></textarea>
            <div v-if="v$.contenido.$anyDirty && v$.contenido.$invalid">
              <small class="form-text text-danger" v-for="error of v$.contenido.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="servicio-precio">Precio</label>
            <input
              type="number"
              class="form-control"
              name="precio"
              id="servicio-precio"
              data-cy="precio"
              :class="{ valid: !v$.precio.$invalid, invalid: v$.precio.$invalid }"
              v-model.number="v$.precio.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="servicio-cita">Cita</label>
            <select class="form-control" id="servicio-cita" data-cy="cita" name="cita" v-model="servicio.cita" required>
              <option v-if="!servicio.cita" :value="null" selected></option>
              <option
                :value="servicio.cita && citaOption.id === servicio.cita.id ? servicio.cita : citaOption"
                v-for="citaOption in citas"
                :key="citaOption.id"
              >
                {{ citaOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.cita.$anyDirty && v$.cita.$invalid">
            <small class="form-text text-danger" v-for="error of v$.cita.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./servicio-update.component.ts"></script>
