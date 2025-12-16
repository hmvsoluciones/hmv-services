import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import NegocioService from './negocio.service';
import { type INegocio } from '@/shared/model/negocio.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'NegocioDetails',
  setup() {
    const negocioService = inject('negocioService', () => new NegocioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const negocio: Ref<INegocio> = ref({});

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

    return {
      alertService,
      negocio,

      previousState,
    };
  },
});
