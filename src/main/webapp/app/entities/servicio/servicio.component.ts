import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';

import ServicioService from './servicio.service';
import { type IServicio } from '@/shared/model/servicio.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Servicio',
  setup() {
    const dateFormat = useDateFormat();
    const servicioService = inject('servicioService', () => new ServicioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const servicios: Ref<IServicio[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveServicios = async () => {
      isFetching.value = true;
      try {
        const res = await servicioService().retrieve();
        servicios.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveServicios();
    };

    onMounted(async () => {
      await retrieveServicios();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IServicio) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeServicio = async () => {
      try {
        await servicioService().delete(removeId.value);
        const message = `A Servicio is deleted with identifier ${removeId.value}`;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveServicios();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      servicios,
      handleSyncList,
      isFetching,
      retrieveServicios,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeServicio,
    };
  },
});
