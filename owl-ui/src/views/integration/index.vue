<template>
  <div class="app-container">
    <el-row v-loading="loading" :gutter="40">
      <el-col v-for="integration in integrations" :key="integration.name" :xs="24" :sm="24" :lg="12" class="card-col">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-name">
              {{ integration.name }}
            </span>
            <span class="card-tag">
              <svg-icon icon-class="integration" :icon-url="integration.meta.iconUrl" />
              {{ integration.meta.display }}
            </span>
          </div>
          <div class="card-body">
            <div v-for="parameter in integration.meta.parameters" :key="parameter.name">
              <span class="card-item-name">
                {{ parameter.display }}:
              </span>
              <span class="card-item-value">
                {{ integration.config[parameter.name] }}
              </span>
            </div>
          </div>
          <div class="card-bottom">
            <el-button-group>
              <el-button icon="el-icon-search" size="mini" @click="toQuery(integration.name)" />
              <el-button icon="el-icon-edit" size="mini" @click="toEdit(integration.name)" />
              <el-popconfirm
                icon-color="red"
                title="确定删除吗？"
                @onConfirm="toDelete(integration.name)"
              >
                <el-button slot="reference" icon="el-icon-delete" size="mini" />
              </el-popconfirm>
            </el-button-group>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12" class="card-panel-col">
        <div class="card-panel">
          <div class="card-new" @click="toCreate">
            <div class="card-create">
              <svg-icon icon-class="add" />
            </div>
            <div>
              <el-button style="" type="text">Create New</el-button>
            </div>
          </div>
        </div>
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
      this.$router.push('/integration-' + name + '/index')
    },
    toEdit(name) {
      this.$router.push('/integration/edit/' + name)
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

.app-container {
  padding: 20px 40px;
}

.card-col {
  margin-bottom: 32px;
}

.card-panel {
  height: 190px;
  position: relative;
  overflow: hidden;
  color: #666;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  white-space: nowrap;
  transition: .2s;

  &:hover {
    box-shadow: 0px 2px 18px rgba(0, 0, 0, .1);
  }
}

.card-header {
  padding: 10px 18px;
  border-bottom: 1px solid #ebeef5;

  .card-name {
    height: 32px;
    line-height: 30px;
    font-size: 16px;
  }

  .card-tag {
    float: right;
    height: 28px;
    padding: 0 10px;
    line-height: 28px;
    font-size: 12px;
    background-color: #f4f4f5;
    border-color: #e9e9eb;
    color: #909399;
    border-width: 1px;
    border-style: solid;
    border-radius: 4px;
  }
}

.card-body {
  margin: 10px 18px;

  .card-item-name {
    margin-right: 10px;
    height: 32px;
    line-height: 30px;
    font-size: 14px;
  }

  .card-item-value {
    height: 32px;
    line-height: 30px;
    font-size: 14px;
    border-bottom: 1px solid #ebeef5;
  }
}

.card-bottom {
  padding: 5px 18px;
  position:absolute;
  right: 0px;
  bottom: 0px;
}

.card-new {
  text-align: center;
  padding-top: 40px;
  cursor: pointer;
  height: 100%;

  .card-create {
    font-size: 46px;
  }
}
</style>
