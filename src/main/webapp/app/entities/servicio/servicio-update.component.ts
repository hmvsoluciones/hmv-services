import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ServicioService from './servicio.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CitaService from '@/entities/cita/cita.service';
import { type ICita } from '@/shared/model/cita.model';
import { type IServicio, Servicio } from '@/shared/model/servicio.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ServicioUpdate',
  setup() {
    const servicioService = inject('servicioService', () => new ServicioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const servicio: Ref<IServicio> = ref(new Servicio());

    const citaService = inject('citaService', () => new CitaService());

    const citas: Ref<ICita[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveServicio = async servicioId => {
      try {
        const res = await servicioService().find(servicioId);
        res.fechaAtencion = new Date(res.fechaAtencion);
        servicio.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.servicioId) {
      retrieveServicio(route.params.servicioId);
    }

    const initRelationships = () => {
      citaService()
        .retrieve()
        .then(res => {
          citas.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      fechaAtencion: {
        required: validations.required('Este campo es obligatorio.'),
      },
      informe: {
        required: validations.required('Este campo es obligatorio.'),
      },
      precio: {},
      cita: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, servicio as any);
    v$.value.$validate();

    return {
      servicioService,
      alertService,
      servicio,
      previousState,
      isSaving,
      currentLanguage,
      citas,
      v$,
      ...useDateFormat({ entityRef: servicio }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.servicio.id) {
        this.servicioService()
          .update(this.servicio)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Servicio is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.servicioService()
          .create(this.servicio)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Servicio is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
