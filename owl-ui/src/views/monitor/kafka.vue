<template>
  <div v-loading="loading" class="app-container">
    <div class="top-container">
      <el-tabs type="border-card">
        <el-tab-pane label="Consumer">
          <div class="search-container">
            <el-input v-model="consumerGroup" size="small" placeholder="GROUP" clearable style="width: 200px; margin-right: 20px;" />
            <el-input v-model="consumerTopic" size="small" placeholder="TOPIC" clearable style="width: 200px; margin-right: 20px;" />
            <el-button size="small" type="primary" icon="el-icon-search" @click="consumerRender()">Search</el-button>
          </div>
          <div class="table-container">
            <el-table
              border
              size="small"
              :data="consumerMetrics.slice((consumerCurrentPage-1)*consumerPageSize,consumerCurrentPage*consumerPageSize)"
              @sort-change="consumerSortChange"
            >
              <el-table-column prop="offset.tag1" label="GROUP" sortable />
              <el-table-column prop="offset.tag2" label="TOPIC" sortable />
              <el-table-column prop="offset.value" label="OFFSET" sortable />
              <el-table-column prop="lag.value" label="LAGS" sortable />
            </el-table>
          </div>
          <div lass="page-container">
            <el-pagination
              align="right"
              layout="total, prev, pager, next, sizes"
              :current-page="consumerCurrentPage"
              :page-size="consumerPageSize"
              :total="consumerMetrics.length"
              @current-change="consumerCurrentChange"
              @size-change="consumerSizeChange"
            />
          </div>
        </el-tab-pane>
        <el-tab-pane label="Producer">
          <div class="search-container">
            <el-input v-model="producerTopic" size="small" placeholder="TOPIC" clearable style="width: 200px; margin-right: 20px;" />
            <el-select v-model="producerState" size="small" placeholder="STATE" clearable style="width: 200px; margin-right: 20px;">
              <el-option
                v-for="item in ['normal', 'error']"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
            <el-button size="small" type="primary" icon="el-icon-search" @click="producerRender()">Search</el-button>
          </div>
          <div class="table-container">
            <el-table
              border
              size="small"
              :data="producerMetrics.slice((producerCurrentPage-1)*producerPageSize,producerCurrentPage*producerPageSize)"
              @sort-change="producerSortChange"
            >
              <el-table-column prop="records_count.tag1" label="TOPIC" sortable />
              <el-table-column label="STATE" prop="partitions_miss.value" sortable>
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.partitions_miss.value == 0" type="success">normal</el-tag>
                  <el-tag v-if="scope.row.partitions_miss.value > 0" type="danger">error</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="partitions_count.value" label="PARTITIONS" sortable>
                <template slot-scope="scope">
                  {{ scope.row.partitions_count.value }}
                  <span v-if="scope.row.partitions_miss.value > 0">(miss {{ scope.row.partitions_miss.value }})</span>
                </template>
              </el-table-column>
              <el-table-column prop="records_count.value" label="RECORDS" sortable />
              <el-table-column prop="begin_offset.value" label="OFFSETS" sortable>
                <template slot-scope="scope">
                  {{ scope.row.begin_offset.value }} - {{ scope.row.end_offset.value }}
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div lass="page-container">
            <el-pagination
              align="right"
              layout="total, prev, pager, next, sizes"
              :current-page="producerCurrentPage"
              :page-size="producerPageSize"
              :total="producerMetrics.length"
              @current-change="producerCurrentChange"
              @size-change="producerSizeChange"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { filter, groupBy, orderBy, forEach, forIn } from 'lodash'
import monitorAPI from '@/api/monitor.js'
import { humanReadable } from '@/utils'
import linechart from './linechart'

export default {
  name: 'Monitor',
  components: {
    linechart
  },
  data() {
    return {
      id: '',
      loading: false,
      consumerCurrentPage: 1,
      consumerPageSize: 10,
      consumerGroup: '',
      consumerTopic: '',
      consumerSortkey: '',
      consumerSortOrder: '',
      consumerMetrics: [],
      consumerMetricsAll: [],
      producerCurrentPage: 1,
      producerPageSize: 10,
      producerTopic: '',
      producerState: '',
      producerSortkey: '',
      producerSortOrder: '',
      producerMetrics: [],
      producerMetricsAll: []
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
      this.loading = true
      monitorAPI.metric(this.id).then(response => {
        const consumer = filter(response.result, ['category', 'consumer'])
        const consumerGroup = groupBy(consumer, 'instance')
        const consumerMetrics = []
        forIn(consumerGroup, function(value, key) {
          const consumerMetric = {}
          forEach(value, function(metric) {
            consumerMetric[metric.metric] = metric
          })
          consumerMetrics.push(consumerMetric)
        })
        this.consumerMetrics = consumerMetrics
        this.consumerMetricsAll = consumerMetrics
        const producer = filter(response.result, ['category', 'producer'])
        const producerGroup = groupBy(producer, 'instance')
        const producerMetrics = []
        forIn(producerGroup, function(value, key) {
          const producerMetric = {}
          forEach(value, function(metric) {
            producerMetric[metric.metric] = metric
          })
          producerMetrics.push(producerMetric)
        })
        this.producerMetrics = producerMetrics
        this.producerMetricsAll = producerMetrics
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    },
    consumerCurrentChange(val) {
      this.consumerCurrentPage = val
    },
    consumerSizeChange(val) {
      this.consumerPageSize = val
    },
    consumerSortChange(sorter) {
      this.consumerSortkey = sorter.prop
      this.consumerSortOrder = sorter.order
      this.consumerRender()
    },
    consumerRender() {
      const consumerSortkey = this.consumerSortkey
      const consumerSortOrder = this.consumerSortOrder
      const consumerGroup = this.consumerGroup
      const consumerTopic = this.consumerTopic
      if (consumerGroup || consumerTopic) {
        this.consumerMetrics = filter(this.consumerMetricsAll, function(o) {
          return (!consumerGroup || o.offset.tag1.includes(consumerGroup))
            && (!consumerTopic || o.offset.tag2.includes(consumerTopic))
        })
      } else {
        this.consumerMetrics = this.consumerMetricsAll
      }
      if (consumerSortkey && consumerSortOrder == 'ascending') {
        this.consumerMetrics = orderBy(this.consumerMetrics, [consumerSortkey], ['asc'])
      } else if (consumerSortkey && consumerSortOrder == 'descending') {
        this.consumerMetrics = orderBy(this.consumerMetrics, [consumerSortkey], ['desc'])
      }
    },
    producerCurrentChange(val) {
      this.producerCurrentPage = val
    },
    producerSizeChange(val) {
      this.producerPageSize = val
    },
    producerSortChange(sorter) {
      this.producerSortkey = sorter.prop
      this.producerSortOrder = sorter.order
      this.producerRender()
    },
    producerRender() {
      const producerSortkey = this.producerSortkey
      const producerSortOrder = this.producerSortOrder
      const producerTopic = this.producerTopic
      const producerState = this.producerState
      if (producerTopic || producerState) {
        this.producerMetrics = filter(this.producerMetricsAll, function(o) {
          return (!producerTopic || o.records_count.tag1.includes(producerTopic))
            && (!producerState
              || (producerState == 'normal' && o.partitions_miss.value == 0)
              || (producerState == 'error' && o.partitions_miss.value > 0))
        })
      } else {
        this.producerMetrics = this.producerMetricsAll
      }
      if (producerSortkey && producerSortOrder == 'ascending') {
        this.producerMetrics = orderBy(this.producerMetrics, [producerSortkey], ['asc'])
      } else if (producerSortkey && producerSortOrder == 'descending') {
        this.producerMetrics = orderBy(this.producerMetrics, [producerSortkey], ['desc'])
      }
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

.top-container {
}

.search-container {
  padding: 5px 20px;
}

.table-container {
  padding: 10px 20px;
}

.page-container {
}

</style>
