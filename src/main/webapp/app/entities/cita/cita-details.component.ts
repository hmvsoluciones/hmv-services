import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import CitaService from './cita.service';
import { type ICita } from '@/shared/model/cita.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CitaDetails',
  setup() {
    const citaService = inject('citaService', () => new CitaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cita: Ref<ICita> = ref({});

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

    return {
      alertService,
      cita,

      previousState,
    };
  },
});
