<template>
  <div class="app-container">
    <el-row :gutter="40" class="panel-group" v-loading="loading">
      <el-col :xs="24" :sm="24" :lg="12" class="card-panel-col" v-for="(integration, i1) in integrations">
        <el-card class="box-card card-panel" shadow="hover">
          <div slot="header" class="clearfix" >
            <span>{{integration.name}}</span>
            <el-tag type="info" style="float: right; margin: 0">
              <svg-icon icon-class="integration" />
              {{integration.meta.display}}
            </el-tag>
          </div>
          <div v-for="(parameter, i2) in integration.meta.parameters" 
            :key="i2" v-if="i2 < 3" 
            class="text item">
            {{parameter.display}}
            {{integration.config[parameter.name]}}
          </div>
          <div bottom clearfix>
            <el-button-group style="float: right;">
              <el-button icon="el-icon-search" size="mini" @click="toQuery(integration.name)">
              </el-button>
              <el-button icon="el-icon-edit" size="mini" @click="toEdit(integration.name)">
              </el-button>
              <el-popconfirm
                icon-color="red"
                title="确定删除吗？"
                @onConfirm="toDelete(integration.name)">
                  <el-button slot="reference" 
                  icon="el-icon-delete" 
                  size="mini">
                  </el-button>
              </el-popconfirm>
            </el-button-group>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12" class="card-panel-col">
        <el-card class="box-card card-panel" shadow="hover">
          <div class="add-btn" @click="toCreate">
            <div class="add-btn-icon">
              <svg-icon icon-class="add" />
            </div>
            <div>
              <el-button style="" type="text">Create New</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import integrationAPI from '@/api/integration.js'

export default {
  name: 'Integration',
  data() {
    return {
      loading: false,
      integrations: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    toCreate() {
      this.$router.push('/integration/create')
    },
    toQuery(name) {
      this.$router.push('/integration-'+name+'/index')
    },
    toEdit(name) {
      this.$router.push('/integration/edit/'+name)
    },
    toDelete(name) {
      this.loading = true
      integrationAPI.remove(name).then(response => {
        this.fetchData()
      }).catch(() => {
      })
    },
    fetchData() {
      this.loading = true
      integrationAPI.list().then(response => {
        this.integrations = response.result
        this.loading = false
        this.$store.dispatch('menu/generateRoutes')
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
     height: 180px;
  }
}

.add-btn {
  text-align: center;
  margin-top: 20px;

  .add-btn-icon {
    font-size: 46px;
  }
}
</style>