<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.cliente.home.createOrEditLabel" data-cy="ClienteCreateUpdateHeading">Crear o editar Cliente</h2>
        <div>
          <div class="form-group" v-if="cliente.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="cliente.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="cliente-nombre"
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
            <label class="form-control-label" for="cliente-celular">Celular</label>
            <input
              type="text"
              class="form-control"
              name="celular"
              id="cliente-celular"
              data-cy="celular"
              :class="{ valid: !v$.celular.$invalid, invalid: v$.celular.$invalid }"
              v-model="v$.celular.$model"
              required
            />
            <div v-if="v$.celular.$anyDirty && v$.celular.$invalid">
              <small class="form-text text-danger" v-for="error of v$.celular.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-correo">Correo</label>
            <input
              type="text"
              class="form-control"
              name="correo"
              id="cliente-correo"
              data-cy="correo"
              :class="{ valid: !v$.correo.$invalid, invalid: v$.correo.$invalid }"
              v-model="v$.correo.$model"
            />
            <div v-if="v$.correo.$anyDirty && v$.correo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.correo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-identificacion">Identificacion</label>
            <input
              type="text"
              class="form-control"
              name="identificacion"
              id="cliente-identificacion"
              data-cy="identificacion"
              :class="{ valid: !v$.identificacion.$invalid, invalid: v$.identificacion.$invalid }"
              v-model="v$.identificacion.$model"
            />
            <div v-if="v$.identificacion.$anyDirty && v$.identificacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.identificacion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-direccion">Direccion</label>
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="cliente-direccion"
              data-cy="direccion"
              :class="{ valid: !v$.direccion.$invalid, invalid: v$.direccion.$invalid }"
              v-model="v$.direccion.$model"
            />
            <div v-if="v$.direccion.$anyDirty && v$.direccion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.direccion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="cliente-activo">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="cliente-activo"
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
<script lang="ts" src="./cliente-update.component.ts"></script>
