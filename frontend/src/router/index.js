import Vue from 'vue'
import Router from 'vue-router'
import NINGenerator from '../components/NINGenerator'
import NINValidator from '../components/NINValidator'
import NINBatch from '../components/NINBatch'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/nin/generator'
    },
    {
      path: '/nin/generator',
      name: 'NINGenerator',
      component: NINGenerator
    },
    {
      path: '/nin/validator',
      name: 'NINValidator',
      component: NINValidator
    },
    {
      path: '/nin/batch',
      name: 'NINBatch',
      component: NINBatch
    }
  ]
})
