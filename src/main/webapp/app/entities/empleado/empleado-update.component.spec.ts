import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EmpleadoUpdate from './empleado-update.vue';
import EmpleadoService from './empleado.service';
import AlertService from '@/shared/alert/alert.service';

type EmpleadoUpdateComponentType = InstanceType<typeof EmpleadoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const empleadoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EmpleadoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Empleado Management Update Component', () => {
    let comp: EmpleadoUpdateComponentType;
    let empleadoServiceStub: SinonStubbedInstance<EmpleadoService>;

    beforeEach(() => {
      route = {};
      empleadoServiceStub = sinon.createStubInstance<EmpleadoService>(EmpleadoService);
      empleadoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          empleadoService: () => empleadoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.empleado = empleadoSample;
        empleadoServiceStub.update.resolves(empleadoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empleadoServiceStub.update.calledWith(empleadoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        empleadoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.empleado = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empleadoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        empleadoServiceStub.find.resolves(empleadoSample);
        empleadoServiceStub.retrieve.resolves([empleadoSample]);

        // WHEN
        route = {
          params: {
            empleadoId: `${empleadoSample.id}`,
          },
        };
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.empleado).toMatchObject(empleadoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        empleadoServiceStub.find.resolves(empleadoSample);
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
