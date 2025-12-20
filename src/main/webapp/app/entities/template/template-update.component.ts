import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TemplateService from './template.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ITemplate, Template } from '@/shared/model/template.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TemplateUpdate',
  setup() {
    const templateService = inject('templateService', () => new TemplateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const template: Ref<ITemplate> = ref(new Template());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      contenido: {
        required: validations.required('Este campo es obligatorio.'),
      },
      activo: {},
    };
    const v$ = useVuelidate(validationRules, template as any);

    const quillOptions = {
      modules: {
        toolbar: [
          [{ font: [] }],
          [{ size: ['small', false, 'large', 'huge'] }],

          ['bold', 'italic', 'underline', 'strike'],

          [{ color: [] }, { background: [] }],

          [{ script: 'sub' }, { script: 'super' }],

          [{ header: 1 }, { header: 2 }, { header: 3 }, { header: 4 }, { header: 5 }, { header: 6 }],

          [{ list: 'ordered' }, { list: 'bullet' }, { list: 'check' }],

          [{ indent: '-1' }, { indent: '+1' }],

          [{ align: [] }],

          ['blockquote', 'code-block'],

          ['link', 'image', 'video'],

          ['clean'],

          ['table'], // custom (HTML)
        ],
      },
    };
    v$.value.$validate();

    return {
      templateService,
      alertService,
      template,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      quillOptions,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.template.id) {
        this.templateService()
          .update(this.template)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Template is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.templateService()
          .create(this.template)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Template is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
