import { defineComponent, provide } from 'vue';

import ClienteService from './cliente/cliente.service';
import CitaService from './cita/cita.service';
import ServicioService from './servicio/servicio.service';
import TemplateService from './template/template.service';
import NegocioService from './negocio/negocio.service';
import EmpleadoService from './empleado/empleado.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('clienteService', () => new ClienteService());
    provide('citaService', () => new CitaService());
    provide('servicioService', () => new ServicioService());
    provide('templateService', () => new TemplateService());
    provide('negocioService', () => new NegocioService());
    provide('empleadoService', () => new EmpleadoService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
