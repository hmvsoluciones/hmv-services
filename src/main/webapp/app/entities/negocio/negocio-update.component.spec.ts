import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import NegocioUpdate from './negocio-update.vue';
import NegocioService from './negocio.service';
import AlertService from '@/shared/alert/alert.service';

type NegocioUpdateComponentType = InstanceType<typeof NegocioUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const negocioSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<NegocioUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Negocio Management Update Component', () => {
    let comp: NegocioUpdateComponentType;
    let negocioServiceStub: SinonStubbedInstance<NegocioService>;

    beforeEach(() => {
      route = {};
      negocioServiceStub = sinon.createStubInstance<NegocioService>(NegocioService);
      negocioServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          negocioService: () => negocioServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(NegocioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.negocio = negocioSample;
        negocioServiceStub.update.resolves(negocioSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(negocioServiceStub.update.calledWith(negocioSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        negocioServiceStub.create.resolves(entity);
        const wrapper = shallowMount(NegocioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.negocio = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(negocioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        negocioServiceStub.find.resolves(negocioSample);
        negocioServiceStub.retrieve.resolves([negocioSample]);

        // WHEN
        route = {
          params: {
            negocioId: `${negocioSample.id}`,
          },
        };
        const wrapper = shallowMount(NegocioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.negocio).toMatchObject(negocioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        negocioServiceStub.find.resolves(negocioSample);
        const wrapper = shallowMount(NegocioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
