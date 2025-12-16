import { PRODUCT_NAME, WEB_SITE_HMV, PHONE_HMV, EMAIL_HMV } from '@/shared/config/constants/constants';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'JhiFooter',
  setup() {
    const currentYear = ref<number>(new Date().getFullYear());
    return {
      currentYear,
      productName: PRODUCT_NAME,
      jhiFooter: {
        website: WEB_SITE_HMV,
        phone: PHONE_HMV,
        email: EMAIL_HMV,
      },
    };
  },
});
