<template>
  <div :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
import monitorAPI from '@/api/monitor.js'
import { humanReadable } from '@/utils'

export default {
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '250px'
    },
    settings: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      data: [],
      chart: null
    }
  },
  watch: {
    settings: {
      deep: true,
      handler() {
        this.refreshChart()
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el)
      this.refreshChart()
    },
    refreshChart() {
      this.data = []
      monitorAPI.history(this.settings.reqId, this.settings.reqBody).then(response => {
        const records = response.result
        for (var i = 0, len = records.length; i < len; i++) {
          this.data.push([records[i].time, records[i].value])
        }
        this.setOptions()
      }).catch(() => {
      })
    },
    setOptions() {
      const _this = this
      const option = {
        grid: {
          top: '10%',
          bottom: '20%',
          left: '10%',
          rigth: '10%'
        },
        tooltip: {
          trigger: 'axis',
          formatter: function(param) {
            const x = param[0].axisValueLabel
            const y = param[0].data[1]
            const m = param[0].marker
            const hy = humanReadable(y, _this.settings.unit)
            if (hy === y) {
              return x + '<br />' + m + y
            } else if (_this.settings.unit === 'percent') {
              return x + '<br />' + m + hy
            } else {
              return x + '<br />' + m + hy + ' (' + y + ')'
            }
          }
        },
        xAxis: {
          type: 'time'
        },
        yAxis: {
          type: 'value',
          scale: true,
          axisLabel: {
            formatter: function(value, index) {
              return humanReadable(value, _this.settings.unit)
            }
          }
        },
        series: {
          name: 'value',
          type: 'line',
          symbol: 'circle',
          data: _this.data,
          lineStyle: {
            width: 1,
            color: _this.settings.color
          },
          itemStyle: {
            color: _this.settings.color
          }
        }
      }
      this.chart.setOption(option)
    }
  }
}
</script>
