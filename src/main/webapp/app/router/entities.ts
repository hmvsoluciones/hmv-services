import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Cliente = () => import('@/entities/cliente/cliente.vue');
const ClienteUpdate = () => import('@/entities/cliente/cliente-update.vue');
const ClienteDetails = () => import('@/entities/cliente/cliente-details.vue');

const Cita = () => import('@/entities/cita/cita.vue');
const CitaUpdate = () => import('@/entities/cita/cita-update.vue');
const CitaDetails = () => import('@/entities/cita/cita-details.vue');

const Servicio = () => import('@/entities/servicio/servicio.vue');
const ServicioUpdate = () => import('@/entities/servicio/servicio-update.vue');
const ServicioDetails = () => import('@/entities/servicio/servicio-details.vue');

const Template = () => import('@/entities/template/template.vue');
const TemplateUpdate = () => import('@/entities/template/template-update.vue');
const TemplateDetails = () => import('@/entities/template/template-details.vue');

const Negocio = () => import('@/entities/negocio/negocio.vue');
const NegocioUpdate = () => import('@/entities/negocio/negocio-update.vue');
const NegocioDetails = () => import('@/entities/negocio/negocio-details.vue');

const Empleado = () => import('@/entities/empleado/empleado.vue');
const EmpleadoUpdate = () => import('@/entities/empleado/empleado-update.vue');
const EmpleadoDetails = () => import('@/entities/empleado/empleado-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'cliente',
      name: 'Cliente',
      component: Cliente,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/new',
      name: 'ClienteCreate',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/edit',
      name: 'ClienteEdit',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/view',
      name: 'ClienteView',
      component: ClienteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cita',
      name: 'Cita',
      component: Cita,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cita/new',
      name: 'CitaCreate',
      component: CitaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cita/:citaId/edit',
      name: 'CitaEdit',
      component: CitaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cita/:citaId/view',
      name: 'CitaView',
      component: CitaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'servicio',
      name: 'Servicio',
      component: Servicio,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'servicio/new',
      name: 'ServicioCreate',
      component: ServicioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'servicio/:servicioId/edit',
      name: 'ServicioEdit',
      component: ServicioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'servicio/:servicioId/view',
      name: 'ServicioView',
      component: ServicioDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'template',
      name: 'Template',
      component: Template,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'template/new',
      name: 'TemplateCreate',
      component: TemplateUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'template/:templateId/edit',
      name: 'TemplateEdit',
      component: TemplateUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'template/:templateId/view',
      name: 'TemplateView',
      component: TemplateDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'negocio',
      name: 'Negocio',
      component: Negocio,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'negocio/new',
      name: 'NegocioCreate',
      component: NegocioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'negocio/:negocioId/edit',
      name: 'NegocioEdit',
      component: NegocioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'negocio/:negocioId/view',
      name: 'NegocioView',
      component: NegocioDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado',
      name: 'Empleado',
      component: Empleado,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado/new',
      name: 'EmpleadoCreate',
      component: EmpleadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado/:empleadoId/edit',
      name: 'EmpleadoEdit',
      component: EmpleadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado/:empleadoId/view',
      name: 'EmpleadoView',
      component: EmpleadoDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
