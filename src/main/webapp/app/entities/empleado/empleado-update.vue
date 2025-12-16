<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.empleado.home.createOrEditLabel" data-cy="EmpleadoCreateUpdateHeading">Crear o editar Empleado</h2>
        <div>
          <div class="form-group" v-if="empleado.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="empleado.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empleado-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="empleado-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empleado-celular">Celular</label>
            <input
              type="text"
              class="form-control"
              name="celular"
              id="empleado-celular"
              data-cy="celular"
              :class="{ valid: !v$.celular.$invalid, invalid: v$.celular.$invalid }"
              v-model="v$.celular.$model"
            />
            <div v-if="v$.celular.$anyDirty && v$.celular.$invalid">
              <small class="form-text text-danger" v-for="error of v$.celular.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empleado-especialidad">Especialidad</label>
            <input
              type="text"
              class="form-control"
              name="especialidad"
              id="empleado-especialidad"
              data-cy="especialidad"
              :class="{ valid: !v$.especialidad.$invalid, invalid: v$.especialidad.$invalid }"
              v-model="v$.especialidad.$model"
            />
            <div v-if="v$.especialidad.$anyDirty && v$.especialidad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.especialidad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empleado-activo">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="empleado-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
            />
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
<script lang="ts" src="./empleado-update.component.ts"></script>
