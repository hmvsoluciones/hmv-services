import { type ComputedRef, defineComponent, inject } from 'vue';

import { useLoginModal } from '@/account/login-modal';

import { PRODUCT_NAME, PRODUCT_DESCRIPTION } from '@/shared/config/constants/constants';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const { showLogin } = useLoginModal();
    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    return {
      authenticated,
      username,
      showLogin,
      productName: PRODUCT_NAME,
      productDescription: PRODUCT_DESCRIPTION,
    };
  },
});
