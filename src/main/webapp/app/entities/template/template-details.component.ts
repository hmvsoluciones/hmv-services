import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import TemplateService from './template.service';
import { type ITemplate } from '@/shared/model/template.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TemplateDetails',
  setup() {
    const templateService = inject('templateService', () => new TemplateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const template: Ref<ITemplate> = ref({});

    const retrieveTemplate = async templateId => {
      try {
        const res = await templateService().find(templateId);
        template.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.templateId) {
      retrieveTemplate(route.params.templateId);
    }

    return {
      alertService,
      template,

      previousState,
    };
  },
});
