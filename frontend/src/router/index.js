import Vue from 'vue'
import Router from 'vue-router'
import NINGenerator from '@/components/NINGenerator'
import NINValidator from '@/components/NINValidator'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/nin/generator',
      name: 'NINGenerator',
      component: NINGenerator
    },
    {
      path: '/',
      redirect: '/nin/generator'
    },
    {
      path: '/nin/validator',
      name: 'NINValidator',
      component: NINValidator
    }
  ]
})
