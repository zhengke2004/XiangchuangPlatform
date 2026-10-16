import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './style.css'
import App from './App.vue'

import { library } from '@fortawesome/fontawesome-svg-core'
import { faEnvelope, faPhone } from '@fortawesome/free-solid-svg-icons'
import { faWeixin, faWeibo, faTiktok } from '@fortawesome/free-brands-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(faEnvelope, faPhone, faWeixin, faWeibo, faTiktok)

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.component('FontAwesomeIcon', FontAwesomeIcon)

app.mount('#app')