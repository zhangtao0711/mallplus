import Vue from 'vue'

import 'normalize.css/normalize.css'// A modern alternative to CSS resets

import { Cell, CellGroup, Button, Card, Icon, SwipeCell } from 'vant';
Vue.use(Card);
Vue.use(Button);
Vue.use(Cell);
Vue.use(CellGroup); 
Vue.use(Icon);
Vue.use(SwipeCell);


import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n
import VCharts from 'v-charts'

import '@/styles/index.scss' // global css

import App from './App'
import router from './router'
import store from './store'

import '@/icons' // icon
import '@/permission' // permission control
import '@/components/templatePage/static/common/common.less'

import CryptoJS from 'crypto-js'//加密

/**
 * 加密（需要先加载lib/aes/aes.min.js文件）
 */
export const encrypt = (word) => {
  var key = CryptoJS.enc.Utf8.parse("46cc793c53dc451b");
  var srcs = CryptoJS.enc.Utf8.parse(word);
  var encrypted = CryptoJS.AES.encrypt(srcs, key, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7
  });
  return encrypted.toString();
}


Vue.use(ElementUI, { locale })
import('element-ui/lib/theme-chalk/index.css')
Vue.use(VCharts)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
