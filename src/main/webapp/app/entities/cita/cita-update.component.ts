import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CitaService from './cita.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ClienteService from '@/entities/cliente/cliente.service';
import { type ICliente } from '@/shared/model/cliente.model';
import EmpleadoService from '@/entities/empleado/empleado.service';
import { type IEmpleado } from '@/shared/model/empleado.model';
import { Cita, type ICita } from '@/shared/model/cita.model';
import { EstadoCita } from '@/shared/model/enumerations/estado-cita.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CitaUpdate',
  setup() {
    const citaService = inject('citaService', () => new CitaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cita: Ref<ICita> = ref(new Cita());

    const clienteService = inject('clienteService', () => new ClienteService());

    const clientes: Ref<ICliente[]> = ref([]);

    const empleadoService = inject('empleadoService', () => new EmpleadoService());

    const empleados: Ref<IEmpleado[]> = ref([]);
    const estadoCitaValues: Ref<string[]> = ref(Object.keys(EstadoCita));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCita = async citaId => {
      try {
        const res = await citaService().find(citaId);
        cita.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.citaId) {
      retrieveCita(route.params.citaId);
    }

    const initRelationships = () => {
      clienteService()
        .retrieve()
        .then(res => {
          clientes.value = res.data;
        });
      empleadoService()
        .retrieve()
        .then(res => {
          empleados.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      fecha: {
        required: validations.required('Este campo es obligatorio.'),
      },
      horaInicio: {
        required: validations.required('Este campo es obligatorio.'),
      },
      duracionMinutos: {
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
        max: validations.maxValue('Este campo no puede ser mayor que 1000.', 1000),
      },
      descripcion: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 200 caracteres.', 200),
      },
      esUrgente: {},
      estadoCita: {},
      cliente: {
        required: validations.required('Este campo es obligatorio.'),
      },
      empleado: {},
      servicio: {},
    };
    const v$ = useVuelidate(validationRules, cita as any);
    v$.value.$validate();

    return {
      citaService,
      alertService,
      cita,
      previousState,
      estadoCitaValues,
      isSaving,
      currentLanguage,
      clientes,
      empleados,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.cita.id) {
        this.citaService()
          .update(this.cita)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Cita is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.citaService()
          .create(this.cita)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Cita is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
