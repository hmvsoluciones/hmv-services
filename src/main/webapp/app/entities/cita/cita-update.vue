<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.cita.home.createOrEditLabel" data-cy="CitaCreateUpdateHeading">Crear o editar Cita</h2>
        <div>
          <div class="form-group" v-if="cita.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="cita.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-fecha">Fecha</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="cita-fecha"
                  v-model="v$.fecha.$model"
                  name="fecha"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="cita-fecha"
                data-cy="fecha"
                type="text"
                class="form-control"
                name="fecha"
                :class="{ valid: !v$.fecha.$invalid, invalid: v$.fecha.$invalid }"
                v-model="v$.fecha.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fecha.$anyDirty && v$.fecha.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fecha.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-horaInicio">Hora Inicio</label>
            <input
              type="time"
              class="form-control"
              name="horaInicio"
              id="cita-horaInicio"
              data-cy="horaInicio"
              :class="{ valid: !v$.horaInicio.$invalid, invalid: v$.horaInicio.$invalid }"
              v-model="v$.horaInicio.$model"
              required
            />
            <div v-if="v$.horaInicio.$anyDirty && v$.horaInicio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.horaInicio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-duracionMinutos">Duracion Minutos</label>
            <input
              type="number"
              class="form-control"
              name="duracionMinutos"
              id="cita-duracionMinutos"
              data-cy="duracionMinutos"
              :class="{ valid: !v$.duracionMinutos.$invalid, invalid: v$.duracionMinutos.$invalid }"
              v-model.number="v$.duracionMinutos.$model"
            />
            <div v-if="v$.duracionMinutos.$anyDirty && v$.duracionMinutos.$invalid">
              <small class="form-text text-danger" v-for="error of v$.duracionMinutos.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-descripcion">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="cita-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
              required
            />
            <div v-if="v$.descripcion.$anyDirty && v$.descripcion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.descripcion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-esUrgente">Es Urgente</label>
            <input
              type="checkbox"
              class="form-check"
              name="esUrgente"
              id="cita-esUrgente"
              data-cy="esUrgente"
              :class="{ valid: !v$.esUrgente.$invalid, invalid: v$.esUrgente.$invalid }"
              v-model="v$.esUrgente.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-estadoCita">Estado Cita</label>
            <select
              class="form-control"
              name="estadoCita"
              :class="{ valid: !v$.estadoCita.$invalid, invalid: v$.estadoCita.$invalid }"
              v-model="v$.estadoCita.$model"
              id="cita-estadoCita"
              data-cy="estadoCita"
            >
              <option v-for="estadoCita in estadoCitaValues" :key="estadoCita" :value="estadoCita">{{ estadoCita }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-cliente">Cliente</label>
            <select class="form-control" id="cita-cliente" data-cy="cliente" name="cliente" v-model="cita.cliente" required>
              <option v-if="!cita.cliente" :value="null" selected></option>
              <option
                :value="cita.cliente && clienteOption.id === cita.cliente.id ? cita.cliente : clienteOption"
                v-for="clienteOption in clientes"
                :key="clienteOption.id"
              >
                {{ clienteOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.cliente.$anyDirty && v$.cliente.$invalid">
            <small class="form-text text-danger" v-for="error of v$.cliente.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cita-empleado">Empleado</label>
            <select class="form-control" id="cita-empleado" data-cy="empleado" name="empleado" v-model="cita.empleado">
              <option :value="null"></option>
              <option
                :value="cita.empleado && empleadoOption.id === cita.empleado.id ? cita.empleado : empleadoOption"
                v-for="empleadoOption in empleados"
                :key="empleadoOption.id"
              >
                {{ empleadoOption.id }}
              </option>
            </select>
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
<script lang="ts" src="./cita-update.component.ts"></script>
