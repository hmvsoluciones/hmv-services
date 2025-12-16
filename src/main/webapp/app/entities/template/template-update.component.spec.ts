import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TemplateUpdate from './template-update.vue';
import TemplateService from './template.service';
import AlertService from '@/shared/alert/alert.service';

type TemplateUpdateComponentType = InstanceType<typeof TemplateUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const templateSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TemplateUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Template Management Update Component', () => {
    let comp: TemplateUpdateComponentType;
    let templateServiceStub: SinonStubbedInstance<TemplateService>;

    beforeEach(() => {
      route = {};
      templateServiceStub = sinon.createStubInstance<TemplateService>(TemplateService);
      templateServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          templateService: () => templateServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.template = templateSample;
        templateServiceStub.update.resolves(templateSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(templateServiceStub.update.calledWith(templateSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        templateServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.template = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(templateServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        templateServiceStub.find.resolves(templateSample);
        templateServiceStub.retrieve.resolves([templateSample]);

        // WHEN
        route = {
          params: {
            templateId: `${templateSample.id}`,
          },
        };
        const wrapper = shallowMount(TemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.template).toMatchObject(templateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        templateServiceStub.find.resolves(templateSample);
        const wrapper = shallowMount(TemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
