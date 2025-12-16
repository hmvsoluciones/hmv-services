import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Servicio from './servicio.vue';
import ServicioService from './servicio.service';
import AlertService from '@/shared/alert/alert.service';

type ServicioComponentType = InstanceType<typeof Servicio>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Servicio Management Component', () => {
    let servicioServiceStub: SinonStubbedInstance<ServicioService>;
    let mountOptions: MountingOptions<ServicioComponentType>['global'];

    beforeEach(() => {
      servicioServiceStub = sinon.createStubInstance<ServicioService>(ServicioService);
      servicioServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          servicioService: () => servicioServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        servicioServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Servicio, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(servicioServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.servicios[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ServicioComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Servicio, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        servicioServiceStub.retrieve.reset();
        servicioServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        servicioServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeServicio();
        await comp.$nextTick(); // clear components

        // THEN
        expect(servicioServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(servicioServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
