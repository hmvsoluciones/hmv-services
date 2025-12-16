import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CitaDetails from './cita-details.vue';
import CitaService from './cita.service';
import AlertService from '@/shared/alert/alert.service';

type CitaDetailsComponentType = InstanceType<typeof CitaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const citaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Cita Management Detail Component', () => {
    let citaServiceStub: SinonStubbedInstance<CitaService>;
    let mountOptions: MountingOptions<CitaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      citaServiceStub = sinon.createStubInstance<CitaService>(CitaService);

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
          citaService: () => citaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        citaServiceStub.find.resolves(citaSample);
        route = {
          params: {
            citaId: `${123}`,
          },
        };
        const wrapper = shallowMount(CitaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cita).toMatchObject(citaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        citaServiceStub.find.resolves(citaSample);
        const wrapper = shallowMount(CitaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
