import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TemplateDetails from './template-details.vue';
import TemplateService from './template.service';
import AlertService from '@/shared/alert/alert.service';

type TemplateDetailsComponentType = InstanceType<typeof TemplateDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const templateSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Template Management Detail Component', () => {
    let templateServiceStub: SinonStubbedInstance<TemplateService>;
    let mountOptions: MountingOptions<TemplateDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      templateServiceStub = sinon.createStubInstance<TemplateService>(TemplateService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          templateService: () => templateServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        templateServiceStub.find.resolves(templateSample);
        route = {
          params: {
            templateId: `${123}`,
          },
        };
        const wrapper = shallowMount(TemplateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.template).toMatchObject(templateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        templateServiceStub.find.resolves(templateSample);
        const wrapper = shallowMount(TemplateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
