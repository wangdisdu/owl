<template>
  <div v-loading="loading" class="app-container">
    <div class="setting-container">
      <i class="el-icon-setting" @click="drawer = true" />
    </div>
    <div>
      <el-drawer :visible.sync="drawer" :with-header="false" size="460px">
        <a href="/" style="height:0;" />
        <div class="setting-timer">
          <el-date-picker
            v-model="timeRange"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator="到"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="timestamp"
            align="left"
            size="small"
            @change="refreshChart"
          />
        </div>
        <div class="setting-cluster">
          <template v-for="(chart, i) in cluster_charts">
            <span :key="i">
              <el-checkbox v-model="chart.visible" :label="chart.title" border size="small" class="setting-item" />
            </span>
          </template>
        </div>
        <template v-for="(node_metrics, i) in nodes_metrics">
          <div :key="'i'+i" class="setting-node">
            <template v-for="(chart, j) in nodes_charts[i]">
              <span :key="'j'+j">
                <el-checkbox v-model="chart.visible" :label="chart.title" border size="small" class="setting-item" />
              </span>
            </template>
          </div>
        </template>
      </el-drawer>
    </div>
    <div class="cluster-container">
      <el-row :gutter="0" class="overview-row">
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              集群状态
            </span>
            <span v-if="cluster_metrics.cluster_status && cluster_metrics.cluster_status.value === 0" class="overview-text overview-green">
              green
            </span>
            <span v-if="cluster_metrics.cluster_status && cluster_metrics.cluster_status.value === 1" class="overview-text overview-yellow">
              yellow
            </span>
            <span v-if="cluster_metrics.cluster_status && cluster_metrics.cluster_status.value === 2" class="overview-text overview-red">
              red
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              CPU使用
            </span>
            <span v-if="cluster_metrics.cpu_percent" class="overview-text">
              {{ humanReadable(cluster_metrics.cpu_percent.value, 'percent') }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              内存使用
            </span>
            <span v-if="cluster_metrics.heap_used_percent" class="overview-text">
              {{ humanReadable(cluster_metrics.heap_used_percent.value, 'percent') }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              存储使用
            </span>
            <span v-if="cluster_metrics.store_bytes" class="overview-text">
              {{ humanReadable(cluster_metrics.store_bytes.value, 'bytes') }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" class="overview-row">
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              节点数量
            </span>
            <span v-if="cluster_metrics.nodes_count" class="overview-text">
              {{ cluster_metrics.nodes_count.value }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              索引数量
            </span>
            <span v-if="cluster_metrics.indices_count" class="overview-text">
              {{ humanReadable(cluster_metrics.indices_count.value, 'count') }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              数据总量
            </span>
            <span v-if="cluster_metrics.docs_count" class="overview-text">
              {{ humanReadable(cluster_metrics.docs_count.value, 'count') }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              磁盘剩余
            </span>
            <span v-if="cluster_metrics.fs_free_bytes" class="overview-text">
              {{ humanReadable(cluster_metrics.fs_free_bytes.value, 'bytes') }}
            </span>
          </div>
        </el-col>
      </el-row>
      <template v-for="(chart, i) in cluster_charts">
        <el-collapse-transition :key="i">
          <el-row v-if="chart.visible" :gutter="0" class="chart-row">
            <span class="chart-title chart-title-cluster">
              {{ chart.title }}
            </span>
            <span class="chart-close">
              <i class="el-icon el-icon-close" @click="chart.visible = false" />
            </span>
            <linechart :settings="chart" />
          </el-row>
        </el-collapse-transition>
      </template>
    </div>
    <div v-for="(node_metrics, i) in nodes_metrics" :key="'i'+i" class="node-container">
      <el-row :gutter="0" class="overview-row">
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              节点名称
            </span>
            <span v-if="node_metrics.cpu_percent.instance" class="overview-text">
              {{ node_metrics.cpu_percent.instance }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              CPU使用
            </span>
            <span v-if="node_metrics.cpu_percent" class="overview-text">
              {{ humanReadable(node_metrics.cpu_percent.value, 'percent') }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              内存使用
            </span>
            <span v-if="node_metrics.heap_used_percent" class="overview-text">
              {{ humanReadable(node_metrics.heap_used_percent.value, 'percent') }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              存储使用
            </span>
            <span v-if="node_metrics.store_bytes" class="overview-text">
              {{ humanReadable(node_metrics.store_bytes.value, 'bytes') }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" class="overview-row">
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              节点地址
            </span>
            <span v-if="node_metrics.cpu_percent" class="overview-text">
              {{ node_metrics.cpu_percent.host }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              cpu负载
            </span>
            <span v-if="node_metrics.index_total" class="overview-text">
              {{ node_metrics.cpu_load.value }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              数据总量
            </span>
            <span v-if="node_metrics.docs_count" class="overview-text">
              {{ humanReadable(node_metrics.docs_count.value, 'count') }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              存储剩余
            </span>
            <span v-if="node_metrics.fs_free_bytes" class="overview-text">
              {{ humanReadable(node_metrics.fs_free_bytes.value, 'bytes') }}
            </span>
          </div>
        </el-col>
      </el-row>
      <template v-for="(chart, j) in nodes_charts[i]">
        <el-collapse-transition :key="'j'+j">
          <el-row v-if="chart.visible" :gutter="0" class="chart-row">
            <span class="chart-title chart-title-node">
              {{ chart.title }}
            </span>
            <span class="chart-close">
              <i class="el-icon el-icon-close" @click="chart.visible = false" />
            </span>
            <linechart :settings="chart" />
          </el-row>
        </el-collapse-transition>
      </template>
    </div>
  </div>
</template>

<script>
import monitorAPI from '@/api/monitor.js'
import { humanReadable } from '@/utils'
import { groupBy, forEach, forIn } from 'lodash'
import linechart from './linechart'

export default {
  name: 'Monitor',
  components: {
    linechart
  },
  data() {
    return {
      id: '',
      drawer: false,
      loading: false,
      timeRange: [new Date().getTime() - 24 * 3600 * 1000, new Date().getTime()],
      pickerOptions: {
        shortcuts: [{
          text: '最近1小时',
          onClick(picker) {
            const end = new Date().getTime()
            const start = end - 3600 * 1000
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近6小时',
          onClick(picker) {
            const end = new Date().getTime()
            const start = end - 3600 * 1000 * 6
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近1天',
          onClick(picker) {
            const end = new Date().getTime()
            const start = end - 3600 * 1000 * 24
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近2天',
          onClick(picker) {
            const end = new Date().getTime()
            const start = end - 3600 * 1000 * 24 * 2
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近1周',
          onClick(picker) {
            const end = new Date().getTime()
            const start = end - 3600 * 1000 * 24 * 7
            picker.$emit('pick', [start, end])
          }
        }]
      },
      cluster_metrics: {},
      cluster_charts: [],
      nodes_metrics: [],
      nodes_charts: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    humanReadable: humanReadable,
    fetchData() {
      // 通过路由名删除前缀'monitor-'，得到id
      const id = this.$route.name.substring(8)
      this.id = id
      this.fetchMetric()
    },
    fetchMetric() {
      this.loading = true
      monitorAPI.metric(this.id).then(response => {
        const group = groupBy(response.result, 'category')
        const cluster_metrics = {}
        forEach(group['cluster'], function(metric) {
          cluster_metrics[metric.metric] = metric
        })
        const nodes = groupBy(group['node'], 'instance')
        const nodes_metrics = []
        forIn(nodes, function(value, key) {
          const node_metrics = {}
          forEach(value, function(metric) {
            node_metrics[metric.metric] = metric
          })
          nodes_metrics.push(node_metrics)
        })
        this.cluster_metrics = cluster_metrics
        this.nodes_metrics = nodes_metrics
        this.renderClusterChart()
        this.renderNodesChart()
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    },
    refreshChart() {
      if (!this.timeRange) {
        return
      }
      for (var i = 0, len = this.cluster_charts.length; i < len; i++) {
        const chart = this.cluster_charts[i]
        chart.reqBody.filter.timeFrom = this.timeRange[0]
        chart.reqBody.filter.timeTo = this.timeRange[1]
      }
      for (var m = 0, mlen = this.nodes_charts.length; m < mlen; m++) {
        const node_charts = this.nodes_charts[m]
        for (var n = 0, nlen = node_charts.length; n < nlen; n++) {
          const chart = node_charts[n]
          chart.reqBody.filter.timeFrom = this.timeRange[0]
          chart.reqBody.filter.timeTo = this.timeRange[1]
        }
      }
    },
    renderClusterChart() {
      const _this = this
      const id = this.id
      const timeRange = this.timeRange
      const cluster_metrics = this.cluster_metrics
      forIn(cluster_metrics, function(metric, key) {
        const visible = metric.metric === 'cpu_percent' || metric.metric === 'heap_used_percent'
        _this.cluster_charts.push({
          visible: visible,
          title: metric.instance + "-" + metric.alias,
          unit: metric.unit,
          color: '#409eff',
          reqId: id,
          reqBody: { 
            filter: {
              metric: metric.metric,
              category: metric.category,
              timeFrom: timeRange[0],
              timeTo: timeRange[1]
            }
          }
        })
      })
    },
    renderNodesChart() {
      const _this = this
      const id = this.id
      const timeRange = this.timeRange
      const nodes_metrics = this.nodes_metrics
      forEach(nodes_metrics, function(node_metrics) {
        const node_charts = []
        forIn(node_metrics, function(metric, key) {
          const visible = metric.metric === 'cpu_percent' || metric.metric === 'heap_used_percent'
          const title = metric.instance + "-" + metric.alias
          node_charts.push({
            visible: visible,
            title: metric.instance + "-" + metric.alias,
            unit: metric.unit,
            color: '#67c23a',
            reqId: id,
            reqBody: { 
              filter: {
                metric: metric.metric,
                category: metric.category,
                instance: metric.instance,
                timeFrom: timeRange[0],
                timeTo: timeRange[1]
              }
            }
          })
        })
        _this.nodes_charts.push(node_charts)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 32px;
  height: 100%;
  background-color: #f5f7fa;
  min-height: calc(100vh - 50px);
}

.setting-container {
  height: 48px;
  width: 48px;
  position: fixed;
  top: 280px;
  right: 0;
  cursor: pointer;
  font-size: 28px;
  text-align: center;
  line-height: 48px;
  color: #fff;
  background-color: rgb(26, 148, 255);
  border-top-left-radius: 8px;
  border-bottom-left-radius: 8px;
  z-index: 2
}

.cluster-container {
  height: 100%;
  min-width: 400px;
  white-space: nowrap;
  color: #666;
  background: #fff;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  border-top: 5px solid #409eff;
  border-left: 1px solid #ebeef5;
  border-right: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}

.node-container {
  margin-top: 20px;
  height: 100%;
  min-width: 400px;
  color: #666;
  background: #fff;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  border-top: 5px solid #67c23a;
  border-left: 1px solid #ebeef5;
  border-right: 1px solid #ebeef5;
  white-space: nowrap;
}

.setting-timer {
  padding: 14px 28px;
  height: 60px;
  background-color: #f5f7fa;
}

.setting-cluster {
  padding: 14px;
  border-top-left-radius: 6px;
  border-top-right-radius: 6px;
  border-top: 5px solid #409eff;
}

.setting-node {
  padding: 14px;
  border-top-left-radius: 6px;
  border-top-right-radius: 6px;
  border-top: 5px solid #67c23a;
}

.setting-item {
  margin: 5px;
}

.chart-row {
  border-bottom: 1px solid #ebeef5;

  &:last-child {
    border: none;
  }

  .chart-close {
    float: right;
    display:inline-block;
    padding: 8px;
    font-size: 18px;
    cursor: pointer;
  }

  .chart-title {
    display:inline-block;
    padding: 8px;
    font-size: 14px;
  }

  .chart-title-cluster {
    background: rgb(236, 245, 255);
    border-left: 5px solid #409eff;
  }

  .chart-title-node {
    background: rgb(240, 249, 235);
    border-left: 5px solid #67c23a;
  }
}

.overview-row {
  border-bottom: 1px solid #ebeef5;

  .overview-col {
    border-right: 1px solid #ebeef5;

    &:last-child {
      border: none;
    }

    .overview-item {
      padding: 14px;
      height: 50px;

      .overview-name {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.5);
        border-right: 1px solid #ebeef5;
        padding-right:14px;
      }

      .overview-text {
        float: right;
        font-weight: bold;
        font-size: 16px;
        color: rgba(0, 0, 0, 0.6);
      }

      .overview-green {
        color: #67C23A;
      }

      .overview-yellow {
        color: #E6A23C;
      }

      .overview-red {
        color: #F56C6C;
      }
    }
  }
}

::v-deep .el-drawer.rtl{
  overflow: scroll;
}
</style>
