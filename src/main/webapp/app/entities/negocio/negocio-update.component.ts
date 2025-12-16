import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import NegocioService from './negocio.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type INegocio, Negocio } from '@/shared/model/negocio.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'NegocioUpdate',
  setup() {
    const negocioService = inject('negocioService', () => new NegocioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const negocio: Ref<INegocio> = ref(new Negocio());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveNegocio = async negocioId => {
      try {
        const res = await negocioService().find(negocioId);
        negocio.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.negocioId) {
      retrieveNegocio(route.params.negocioId);
    }

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 150 caracteres.', 150),
      },
      responsable: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 150 caracteres.', 150),
      },
      celular: {
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      correo: {
        maxLength: validations.maxLength('Este campo no puede superar más de 256 caracteres.', 256),
      },
      subscriptionKey: {
        required: validations.required('Este campo es obligatorio.'),
      },
      esActivo: {},
    };
    const v$ = useVuelidate(validationRules, negocio as any);
    v$.value.$validate();

    return {
      negocioService,
      alertService,
      negocio,
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
      if (this.negocio.id) {
        this.negocioService()
          .update(this.negocio)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Negocio is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.negocioService()
          .create(this.negocio)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Negocio is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
