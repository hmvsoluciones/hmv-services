import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ServicioDetails from './servicio-details.vue';
import ServicioService from './servicio.service';
import AlertService from '@/shared/alert/alert.service';

type ServicioDetailsComponentType = InstanceType<typeof ServicioDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const servicioSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Servicio Management Detail Component', () => {
    let servicioServiceStub: SinonStubbedInstance<ServicioService>;
    let mountOptions: MountingOptions<ServicioDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      servicioServiceStub = sinon.createStubInstance<ServicioService>(ServicioService);

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
          servicioService: () => servicioServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        servicioServiceStub.find.resolves(servicioSample);
        route = {
          params: {
            servicioId: `${123}`,
          },
        };
        const wrapper = shallowMount(ServicioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.servicio).toMatchObject(servicioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        servicioServiceStub.find.resolves(servicioSample);
        const wrapper = shallowMount(ServicioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
