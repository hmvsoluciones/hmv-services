import { defineComponent, provide, ref } from 'vue';
import { storeToRefs } from 'pinia';

import { useLoginModal } from '@/account/login-modal';
import LoginForm from '@/account/login-form/login-form.vue';
import Ribbon from '@/core/ribbon/ribbon.vue';
import JhiFooter from '@/core/jhi-footer/jhi-footer.vue';
import JhiNavbar from '@/core/jhi-navbar/jhi-navbar.vue';
import SidebarComponent from '@/core/sidebar/sidebar.vue';
import { useAlertService } from '@/shared/alert/alert.service';
import '@/shared/config/dayjs';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'App',
  components: {
    ribbon: Ribbon,
    'jhi-navbar': JhiNavbar,
    'login-form': LoginForm,
    'jhi-footer': JhiFooter,
    'sidebar-component': SidebarComponent,
  },
  setup() {
    provide('alertService', useAlertService());

    const { loginModalOpen } = storeToRefs(useLoginModal());

    const sidebarVisible = ref(false);

    const toggleSidebar = () => {
      sidebarVisible.value = !sidebarVisible.value;
    };

    return {
      loginModalOpen,
      sidebarVisible,
      toggleSidebar,
    };
  },
});
