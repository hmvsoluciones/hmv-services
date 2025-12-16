import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import ServicioUpdate from './servicio-update.vue';
import ServicioService from './servicio.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import CitaService from '@/entities/cita/cita.service';

type ServicioUpdateComponentType = InstanceType<typeof ServicioUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const servicioSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ServicioUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Servicio Management Update Component', () => {
    let comp: ServicioUpdateComponentType;
    let servicioServiceStub: SinonStubbedInstance<ServicioService>;

    beforeEach(() => {
      route = {};
      servicioServiceStub = sinon.createStubInstance<ServicioService>(ServicioService);
      servicioServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          servicioService: () => servicioServiceStub,
          citaService: () =>
            sinon.createStubInstance<CitaService>(CitaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(ServicioUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ServicioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.servicio = servicioSample;
        servicioServiceStub.update.resolves(servicioSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(servicioServiceStub.update.calledWith(servicioSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        servicioServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ServicioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.servicio = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(servicioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        servicioServiceStub.find.resolves(servicioSample);
        servicioServiceStub.retrieve.resolves([servicioSample]);

        // WHEN
        route = {
          params: {
            servicioId: `${servicioSample.id}`,
          },
        };
        const wrapper = shallowMount(ServicioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.servicio).toMatchObject(servicioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        servicioServiceStub.find.resolves(servicioSample);
        const wrapper = shallowMount(ServicioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
