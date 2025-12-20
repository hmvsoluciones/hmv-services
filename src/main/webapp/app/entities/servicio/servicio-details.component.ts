import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ServicioService from './servicio.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat } from '@/shared/composables';
import { type IServicio } from '@/shared/model/servicio.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ServicioDetails',
  setup() {
    const dateFormat = useDateFormat();
    const servicioService = inject('servicioService', () => new ServicioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const servicio: Ref<IServicio> = ref({});

    const retrieveServicio = async servicioId => {
      try {
        const res = await servicioService().find(servicioId);
        servicio.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.servicioId) {
      retrieveServicio(route.params.servicioId);
    }

    return {
      ...dateFormat,
      alertService,
      servicio,

      ...dataUtils,

      previousState,
    };
  },
});
