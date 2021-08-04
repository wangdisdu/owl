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
              {{ cluster_metrics.cpu_percent.value }} %
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              内存使用
            </span>
            <span v-if="cluster_metrics.mem_used_bytes" class="overview-text">
              {{ humanBytes(cluster_metrics.mem_used_bytes.value) }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              存储使用
            </span>
            <span v-if="cluster_metrics.store_bytes" class="overview-text">
              {{ humanBytes(cluster_metrics.store_bytes.value) }}
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
              {{ cluster_metrics.indices_count.value }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              数据总量
            </span>
            <span v-if="cluster_metrics.docs_count" class="overview-text">
              {{ humanNumber(cluster_metrics.docs_count.value) }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              磁盘剩余
            </span>
            <span v-if="cluster_metrics.fs_free_bytes" class="overview-text">
              {{ humanBytes(cluster_metrics.fs_free_bytes.value) }}
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
              {{ node_metrics.cpu_percent.value }} %
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              内存使用
            </span>
            <span v-if="node_metrics.mem_used_bytes" class="overview-text">
              {{ humanBytes(node_metrics.mem_used_bytes.value) }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              存储使用
            </span>
            <span v-if="node_metrics.store_bytes" class="overview-text">
              {{ humanBytes(node_metrics.store_bytes.value) }}
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
              数据总量
            </span>
            <span v-if="node_metrics.docs_count" class="overview-text">
              {{ humanNumber(node_metrics.docs_count.value) }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              内存占比
            </span>
            <span v-if="node_metrics.mem_percent" class="overview-text">
              {{ node_metrics.mem_percent.value }} %
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              存储剩余
            </span>
            <span v-if="node_metrics.fs_free_bytes" class="overview-text">
              {{ humanBytes(node_metrics.fs_free_bytes.value) }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" class="overview-row">
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              写入总量
            </span>
            <span v-if="node_metrics.index_total" class="overview-text">
              {{ humanNumber(node_metrics.index_total.value) }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              查询总量
            </span>
            <span v-if="node_metrics.query_total" class="overview-text">
              {{ humanNumber(node_metrics.query_total.value) }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              查询阻塞
            </span>
            <span v-if="node_metrics.search_queue" class="overview-text">
              {{ node_metrics.search_queue.value }}
            </span>
          </div>
        </el-col>
        <el-col :span="6" class="overview-col">
          <div class="overview-item">
            <span class="overview-name">
              写入阻塞
            </span>
            <span v-if="node_metrics.write_queue" class="overview-text">
              {{ node_metrics.write_queue.value }}
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
import { humanBytes, humanNumber } from '@/utils'
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
      timeRange: [new Date().getTime() - 3600 * 1000, new Date().getTime()],
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
    humanBytes: humanBytes,
    humanNumber: humanNumber,
    fetchData() {
      // 通过路由名删除前缀'monitor-'，得到id
      const id = this.$route.name.substring(8)
      this.id = id
      this.fetchMetric()
      this.renderClusterChart()
      this.renderNodesChart()
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
      const id = this.id
      this.cluster_charts.push({
        visible: true,
        title: 'CPU使用',
        type: 'percent',
        color: '#409eff',
        reqId: id,
        reqBody: { filter: { metric: 'cpu_percent', category: 'cluster', timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      })
      this.cluster_charts.push({
        visible: true,
        title: '内存使用',
        type: 'bytes',
        color: '#409eff',
        reqId: id,
        reqBody: { filter: { metric: 'mem_used_bytes', category: 'cluster', timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      })
      this.cluster_charts.push({
        visible: false,
        title: '存储使用',
        type: 'bytes',
        color: '#409eff',
        reqId: id,
        reqBody: { filter: { metric: 'store_bytes', category: 'cluster', timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      })
      this.cluster_charts.push({
        visible: false,
        title: '磁盘剩余',
        type: 'bytes',
        color: '#409eff',
        reqId: id,
        reqBody: { filter: { metric: 'fs_free_bytes', category: 'cluster', timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      })
      this.cluster_charts.push({
        visible: false,
        title: '数据总量',
        type: 'count',
        color: '#409eff',
        reqId: id,
        reqBody: { filter: { metric: 'docs_count', category: 'cluster', timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      })
    },
    renderNodesChart() {
      const id = this.id
      const nodes_metrics = this.nodes_metrics
      for (var i = 0, len = nodes_metrics.length; i < len; i++) {
        const node_metrics = nodes_metrics[i]
        const instance = node_metrics.cpu_percent.instance
        this.renderNodeChart(id, instance)
      }
    },
    renderNodeChart(id, instance) {
      const node_charts = [{
        visible: true,
        title: instance + ' / CPU使用',
        type: 'percent',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'cpu_percent', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: true,
        title: instance + ' / 内存占比',
        type: 'percent',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'mem_percent', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 内存使用',
        type: 'bytes',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'mem_used_bytes', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / fielddata缓存',
        type: 'bytes',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'fielddata_bytes', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 查询缓存',
        type: 'bytes',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'query_cache_bytes', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 请求缓存',
        type: 'bytes',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'request_cache_bytes', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 存储使用',
        type: 'bytes',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'store_bytes', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 磁盘剩余',
        type: 'bytes',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'fs_free_bytes', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 数据总量',
        type: 'count',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'docs_count', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / YoungGC次数',
        type: 'count',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'young_gc_count', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / YoungGC耗时',
        type: 'ms',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'young_gc_ms', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / OldGC次数',
        type: 'count',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'old_gc_count', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / OldGC耗时',
        type: 'ms',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'old_gc_ms', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 写入总量',
        type: 'count',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'index_total', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 查询总量',
        type: 'count',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'query_total', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 查询阻塞',
        type: 'count',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'search_queue', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }, {
        visible: false,
        title: instance + ' / 写入阻塞',
        type: 'count',
        color: '#67c23a',
        reqId: id,
        reqBody: { filter: { metric: 'write_queue', category: 'node', instance: instance, timeFrom: this.timeRange[0], timeTo: this.timeRange[1] }}
      }]
      this.nodes_charts.push(node_charts)
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
