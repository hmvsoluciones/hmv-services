import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import EmpleadoService from './empleado.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Empleado, type IEmpleado } from '@/shared/model/empleado.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EmpleadoUpdate',
  setup() {
    const empleadoService = inject('empleadoService', () => new EmpleadoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const empleado: Ref<IEmpleado> = ref(new Empleado());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEmpleado = async empleadoId => {
      try {
        const res = await empleadoService().find(empleadoId);
        empleado.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.empleadoId) {
      retrieveEmpleado(route.params.empleadoId);
    }

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 150 caracteres.', 150),
      },
      celular: {
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      especialidad: {
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      activo: {},
    };
    const v$ = useVuelidate(validationRules, empleado as any);
    v$.value.$validate();

    return {
      empleadoService,
      alertService,
      empleado,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.empleado.id) {
        this.empleadoService()
          .update(this.empleado)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Empleado is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.empleadoService()
          .create(this.empleado)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Empleado is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
