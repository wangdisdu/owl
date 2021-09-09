import Layout from '@/layout'
import { constantRoutes, tailRoutes, resetRouter } from '@/router'
import router from '@/router'
import integrationAPI from '@/api/integration'

const state = {
  menu_routes: []
}

const mutations = {
  SET_ROUTES: (state, menu_routes) => {
    state.menu_routes = constantRoutes.concat(menu_routes)
  }
}

const actions = {
  generateRoutes({ commit }) {
    integrationAPI.list().then(response => {
      const integrations = response.result

      const query_route = {
        path: '/query',
        name: 'Query',
        component: Layout,
        meta: { title: 'Query', icon: 'query' },
        children: []
      }
      const monitor_route = {
        path: '/monitor',
        name: 'Monitor',
        component: Layout,
        meta: { title: 'Monitor', icon: 'monitor' },
        children: []
      }

      for (var i = 0; i < integrations.length; i++) {
        const integration = integrations[i]
        const name = integration.name
        query_route.children.push({
          path: '/query/' + name,
          name: 'query-' + name,
          component: () => import('@/views/query/index'),
          meta: { title: name, icon: 'query', iconUrl: integration.meta.iconUrl }
        })
        if (integration.meta.monitorEnable) {
          if (integration.meta.display === 'Elasticsearch') {
            monitor_route.children.push({
              path: '/monitor/' + name,
              name: 'monitor-' + name,
              component: () => import('@/views/monitor/elasticsearch'),
              meta: { title: name, icon: 'monitor', iconUrl: integration.meta.iconUrl }
            })
          } else if (integration.meta.display === 'Kafka') {
            monitor_route.children.push({
              path: '/monitor/' + name,
              name: 'monitor-' + name,
              component: () => import('@/views/monitor/kafka'),
              meta: { title: name, icon: 'monitor', iconUrl: integration.meta.iconUrl }
            })
          }
        }
      }
      if (query_route.children.length === 0) {
        query_route.children.push({
          path: '/query/empty',
          name: 'QueryEmpty',
          component: () => import('@/views/integration/empty'),
          meta: { title: 'Query', icon: 'query' }
        })
      }
      if (monitor_route.children.length === 0) {
        monitor_route.children.push({
          path: '/monitor/empty',
          name: 'MonitorEmpty',
          component: () => import('@/views/integration/empty'),
          meta: { title: 'Monitor', icon: 'monitor' }
        })
      }

      const menu_routes = [query_route, monitor_route].concat(tailRoutes)

      resetRouter()

      router.addRoutes(menu_routes)

      commit('SET_ROUTES', menu_routes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
