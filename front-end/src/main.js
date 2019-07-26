import Vue from 'vue'
import App from './App.vue'
import router from './router'

Vue.config.productionTip = false;

import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue);

import 'sweetalert2/dist/sweetalert2.min.css';
import VueSweetalert2 from "vue-sweetalert2";

const options = {
  confirmButtonColor: '#04AAFB',
  cancelButtonColor: '#ff7674'
};

Vue.use(VueSweetalert2, options);

import { store } from './store/store'

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

const DEFAULT_TITLE = 'Obss Jobs';
router.afterEach((to, from) => {
  document.title = to.meta.title || DEFAULT_TITLE;
});

