import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CitaUpdate from './cita-update.vue';
import CitaService from './cita.service';
import AlertService from '@/shared/alert/alert.service';

import ClienteService from '@/entities/cliente/cliente.service';
import EmpleadoService from '@/entities/empleado/empleado.service';

type CitaUpdateComponentType = InstanceType<typeof CitaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const citaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CitaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Cita Management Update Component', () => {
    let comp: CitaUpdateComponentType;
    let citaServiceStub: SinonStubbedInstance<CitaService>;

    beforeEach(() => {
      route = {};
      citaServiceStub = sinon.createStubInstance<CitaService>(CitaService);
      citaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          citaService: () => citaServiceStub,
          clienteService: () =>
            sinon.createStubInstance<ClienteService>(ClienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          empleadoService: () =>
            sinon.createStubInstance<EmpleadoService>(EmpleadoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cita = citaSample;
        citaServiceStub.update.resolves(citaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(citaServiceStub.update.calledWith(citaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        citaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cita = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(citaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        citaServiceStub.find.resolves(citaSample);
        citaServiceStub.retrieve.resolves([citaSample]);

        // WHEN
        route = {
          params: {
            citaId: `${citaSample.id}`,
          },
        };
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cita).toMatchObject(citaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        citaServiceStub.find.resolves(citaSample);
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
