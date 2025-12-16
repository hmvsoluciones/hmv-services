import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import NegocioDetails from './negocio-details.vue';
import NegocioService from './negocio.service';
import AlertService from '@/shared/alert/alert.service';

type NegocioDetailsComponentType = InstanceType<typeof NegocioDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const negocioSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Negocio Management Detail Component', () => {
    let negocioServiceStub: SinonStubbedInstance<NegocioService>;
    let mountOptions: MountingOptions<NegocioDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      negocioServiceStub = sinon.createStubInstance<NegocioService>(NegocioService);

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
          negocioService: () => negocioServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        negocioServiceStub.find.resolves(negocioSample);
        route = {
          params: {
            negocioId: `${123}`,
          },
        };
        const wrapper = shallowMount(NegocioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.negocio).toMatchObject(negocioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        negocioServiceStub.find.resolves(negocioSample);
        const wrapper = shallowMount(NegocioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
