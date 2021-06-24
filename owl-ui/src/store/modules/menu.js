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
      
      let menu_routes = []
      for (var i = 0; i < integrations.length; i++) {
        let integration = integrations[i]
        let name = integration.name
        let id = 'integration-' + name
        let path = "/" + id
        menu_routes.push({
          path: path,
          component: Layout,
          children: [
            {
              path: 'index',
              name: id,
              component: () => import('@/views/query/index'),
              meta: { title: name, icon: 'integration', iconUrl: integration.meta.iconUrl }
            }
          ]
        })
      }

      menu_routes.push.apply(menu_routes, tailRoutes);

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