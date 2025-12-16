import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import EmpleadoService from './empleado.service';
import { type IEmpleado } from '@/shared/model/empleado.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EmpleadoDetails',
  setup() {
    const empleadoService = inject('empleadoService', () => new EmpleadoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const empleado: Ref<IEmpleado> = ref({});

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

    return {
      alertService,
      empleado,

      previousState,
    };
  },
});
