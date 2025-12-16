import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import { library } from '@fortawesome/fontawesome-svg-core';
import * as solidIcons from '@fortawesome/free-solid-svg-icons';

// Agregar todos los íconos sólidos

export function initFortAwesome(vue: any) {
  vue.component('font-awesome-icon', FontAwesomeIcon);

  Object.keys(solidIcons)
    .filter(key => key !== 'fas' && key !== 'prefix') // excluir internos
    .forEach(key => library.add((solidIcons as any)[key]));
}
