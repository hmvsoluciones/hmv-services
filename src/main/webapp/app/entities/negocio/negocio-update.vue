<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.negocio.home.createOrEditLabel" data-cy="NegocioCreateUpdateHeading">Crear o editar Negocio</h2>
        <div>
          <div class="form-group" v-if="negocio.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="negocio.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="negocio-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="negocio-nombre"
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
            <label class="form-control-label" for="negocio-responsable">Responsable</label>
            <input
              type="text"
              class="form-control"
              name="responsable"
              id="negocio-responsable"
              data-cy="responsable"
              :class="{ valid: !v$.responsable.$invalid, invalid: v$.responsable.$invalid }"
              v-model="v$.responsable.$model"
              required
            />
            <div v-if="v$.responsable.$anyDirty && v$.responsable.$invalid">
              <small class="form-text text-danger" v-for="error of v$.responsable.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="negocio-celular">Celular</label>
            <input
              type="text"
              class="form-control"
              name="celular"
              id="negocio-celular"
              data-cy="celular"
              :class="{ valid: !v$.celular.$invalid, invalid: v$.celular.$invalid }"
              v-model="v$.celular.$model"
            />
            <div v-if="v$.celular.$anyDirty && v$.celular.$invalid">
              <small class="form-text text-danger" v-for="error of v$.celular.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="negocio-correo">Correo</label>
            <input
              type="text"
              class="form-control"
              name="correo"
              id="negocio-correo"
              data-cy="correo"
              :class="{ valid: !v$.correo.$invalid, invalid: v$.correo.$invalid }"
              v-model="v$.correo.$model"
            />
            <div v-if="v$.correo.$anyDirty && v$.correo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.correo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="negocio-subscriptionKey">Subscription Key</label>
            <input
              type="text"
              class="form-control"
              name="subscriptionKey"
              id="negocio-subscriptionKey"
              data-cy="subscriptionKey"
              :class="{ valid: !v$.subscriptionKey.$invalid, invalid: v$.subscriptionKey.$invalid }"
              v-model="v$.subscriptionKey.$model"
              required
            />
            <div v-if="v$.subscriptionKey.$anyDirty && v$.subscriptionKey.$invalid">
              <small class="form-text text-danger" v-for="error of v$.subscriptionKey.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="negocio-esActivo">Es Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="esActivo"
              id="negocio-esActivo"
              data-cy="esActivo"
              :class="{ valid: !v$.esActivo.$invalid, invalid: v$.esActivo.$invalid }"
              v-model="v$.esActivo.$model"
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
<script lang="ts" src="./negocio-update.component.ts"></script>
