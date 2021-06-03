import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/integration',
    children: [{
      path: 'integration',
      name: 'Integration',
      component: () => import('@/views/integration/index'),
      meta: { title: 'Integration', icon: 'integration' }
    },{
      path: '/integration/create',
      name: 'IntegrationCreate',
      component: () => import('@/views/integration/create'),
      meta: { activeMenu: '/integration' },
      hidden: true
    },{
      path: '/integration/edit/:id',
      name: 'IntegrationEdit',
      component: () => import('@/views/integration/edit'),
      meta: { activeMenu: '/integration' },
      props: true,
      hidden: true
    }]
  },

  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

]

export const tailRoutes = [
  // redirect to home !!!
  { path: '*', redirect: '/', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
