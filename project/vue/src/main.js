import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
import router from './router'
import '@/assets/global.css'
import '@/assets/manager.css'

Vue.directive('title', {
  inserted: function (el, binding) {
    document.title = el.dataset.title
  }
})
Vue.config.productionTip = false
Vue.use(ElementUI);
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')