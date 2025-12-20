<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.template.home.createOrEditLabel" data-cy="TemplateCreateUpdateHeading">Crear o editar Template</h2>
        <div>
          <div class="form-group" v-if="template.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="template.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="template-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="template-nombre"
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
            <label class="form-control-label" for="template-contenido">Contenido</label>
            <QuillEditor
              v-model:content="v$.contenido.$model"
              content-type="html"
              :options="quillOptions"
              theme="snow"
              style="height: 200px"
            />
            <div v-if="v$.contenido.$anyDirty && v$.contenido.$invalid">
              <small class="form-text text-danger" v-for="error of v$.contenido.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="template-activo">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="template-activo"
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
<script lang="ts" src="./template-update.component.ts"></script>
