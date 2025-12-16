import { defineComponent, computed, watch } from 'vue';
import { useLoginModal } from '@/account/login-modal';
import { useStore } from '@/store';
import { onBeforeRouteLeave, useRoute, useRouter } from 'vue-router';
import { PRODUCT_NAME } from '@/shared/config/constants/constants';

export default defineComponent({
  name: 'SidebarComponent',
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['update:visible'],
  setup(props, { emit }) {
    const { showLogin } = useLoginModal();
    const router = useRouter();
    const store = useStore();
    const route = useRoute();

    watch(route, (to, from) => {
      console.log('Ruta cambiada:', to.path);
      closeSidebar();
    });

    const closeSidebar = (): void => {
      emit('update:visible', false);
    };

    const handleLoginClick = (): void => {
      closeSidebar();
      setTimeout(() => {
        showLogin();
      }, 100);
    };

    const handleNavigation = (event: Event, path: string): void => {
      closeSidebar();
      // La navegación se manejará automáticamente por router-link
      // Solo necesitamos cerrar el sidebar
    };

    const handleLogout = (): void => {
      closeSidebar();
      localStorage.removeItem('jhi-authenticationToken');
      sessionStorage.removeItem('jhi-authenticationToken');
      store.logout();
      if (router.currentRoute.value.path !== '/') {
        router.push('/');
      }
      console.log('Logout clicked');
    };

    // Ejemplo de propiedades computadas para mostrar/ocultar grupos
    const isAuthenticated = computed(() => {
      // Esto es un ejemplo - ajusta según tu implementación de autenticación
      return store.authenticated || false;
    });

    const showAdminSection = computed(() => {
      // Ejemplo - mostrar sección admin solo a usuarios autenticados
      return isAuthenticated.value;
    });

    return {
      closeSidebar,
      handleLoginClick,
      handleLogout,
      isAuthenticated,
      showAdminSection,
      PRODUCT_NAME,
      showLogin,
      handleNavigation,
    };
  },
});
