import { type Ref, computed, defineComponent, inject, ref } from 'vue';

import { useRouter } from 'vue-router';
import { useLoginModal } from '@/account/login-modal';
import type AccountService from '@/account/account.service';
import EntitiesMenu from '@/entities/entities-menu.vue';

import { useStore } from '@/store';
import { PRODUCT_NAME } from '@/shared/config/constants/constants';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'JhiNavbar',
  components: {
    'entities-menu': EntitiesMenu,
  },
  emits: ['toggle-sidebar'], // Solo agregar esto
  setup(props, { emit }) {
    const { showLogin } = useLoginModal();
    const accountService = inject<AccountService>('accountService');
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const router = useRouter();
    const store = useStore();

    const version = `v${APP_VERSION}`;
    const hasAnyAuthorityValues: Ref<any> = ref({});

    const openAPIEnabled = computed(() => store.activeProfiles.indexOf('api-docs') > -1);
    const inProduction = computed(() => store.activeProfiles.indexOf('prod') > -1);
    const authenticated = computed(() => store.authenticated);

    const subIsActive = (input: string | string[]) => {
      const paths = Array.isArray(input) ? input : [input];
      return paths.some(path => {
        return router.currentRoute.value.path.indexOf(path) === 0; // current path starts with this path string
      });
    };

    const logout = async () => {
      localStorage.removeItem('jhi-authenticationToken');
      sessionStorage.removeItem('jhi-authenticationToken');
      store.logout();
      if (router.currentRoute.value.path !== '/') {
        router.push('/');
      }
    };

    // SOLO AGREGAR ESTA FUNCIÓN
    const toggleSidebar = () => {
      emit('toggle-sidebar');
    };

    return {
      logout,
      subIsActive,
      accountService,
      showLogin,
      version,
      currentLanguage,
      hasAnyAuthorityValues,
      openAPIEnabled,
      inProduction,
      authenticated,
      productName: PRODUCT_NAME,
      toggleSidebar,
    };
  },
  methods: {
    hasAnyAuthority(authorities: any): boolean {
      if (this.accountService) {
        this.accountService.hasAnyAuthorityAndCheckAuth(authorities).then(value => {
          if (this.hasAnyAuthorityValues[authorities] !== value) {
            this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
          }
        });
      }
      return this.hasAnyAuthorityValues[authorities] ?? false;
    },
  },
});
