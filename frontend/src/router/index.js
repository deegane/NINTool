import { createRouter, createWebHistory } from 'vue-router'
import NINGenerator from '../components/NINGenerator.vue'
import NINValidator from '../components/NINValidator.vue'
import NINBatch from '../components/NINBatch.vue'

const routes = [
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
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/nin/generator'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
