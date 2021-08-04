<template>
  <div v-loading="connectLoading" element-loading-text="Connecting" class="app-container">
    <split-pane :min-percent="10" :default-percent="30" split="vertical">
      <template slot="paneL">
        <div class="left-container">
          <el-input
            v-model="filterSchema"
            placeholder="search"
            style="margin-bottom:10px;"
            @keyup.enter.native="searchTree()"
          />
          <div class="tree-container">
            <div v-for="(table, i) in ptables" :key="i" class="tree-node">
              <div class="tree-head" @click="showTable(table)" @dblclick="copyTableToSql(table.name)">
                <span class="table-icon"><svg-icon icon-class="table" /> </span>
                <span>{{ table.name }}</span>
              </div>
              <el-collapse-transition>
                <div v-show="table.show" class="tree-child">
                  <div v-for="(column, j) in table.columns" :key="j" class="tree-item" @dblclick="copyColumnToSql(column.name)">
                    <span class="column-icon"><svg-icon icon-class="column" /> </span>
                    <span>{{ column.name }}</span>
                  </div>
                </div>
              </el-collapse-transition>
            </div>
            <div class="tree-foot">
              <el-pagination
                align="right"
                layout="prev, next"
                prev-text="上一页"
                next-text="下一页"
                :hide-on-single-page="true"
                :current-page="vCurrentPage"
                :page-size="vPageSize"
                :total="vtables.length"
                @current-change="handleVCurrentChange"
              />
            </div>
          </div>
        </div>
      </template>
      <template slot="paneR">
        <div class="right-container">
          <div class="editor-container">
            <div class="button-container">
              <el-button-group style="float: right; margin-top: 4px;">
                <el-button icon="el-icon-video-play" size="small" @click="handleQueryData">Run</el-button>
              </el-button-group>
            </div>
            <div>
              <textarea ref="sqleditor" />
            </div>
          </div>
          <div class="tab-container">
            <el-tabs>
              <el-tab-pane label="Result">
                <div class="table-container">
                  <el-table
                    v-loading="tableLoading"
                    :data="rows.slice((currentPage-1)*pageSize,currentPage*pageSize)"
                    :header-cell-style="{background:'#f5f7fa'}"
                    element-loading-text="Loading"
                    border
                    fit
                    highlight-current-row
                  >
                    <el-table-column v-for="(column, k) in columns" :key="k" :label="column.name">
                      <template slot-scope="scope">
                        {{ scope.row[column.name] }}
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
                <div v-if="rows.length > 0" class="block" style="margin-top:10px;">
                  <el-pagination
                    align="right"
                    layout="total, prev, pager, next"
                    :current-page="currentPage"
                    :page-size="pageSize"
                    :total="rows.length"
                    @current-change="handleCurrentChange"
                  />
                </div>
              </el-tab-pane>
              <el-tab-pane label="History" />
            </el-tabs>
          </div>
        </div>
      </template>
    </split-pane>
  </div>
</template>

<script>

import splitPane from 'vue-splitpane'
import CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/addon/hint/sql-hint.js'
import 'codemirror/addon/edit/matchbrackets.js'
import integrationAPI from '@/api/integration.js'

export default {
  name: 'SqlEditor',
  components: { splitPane },
  data() {
    return {
      connectLoading: false,
      integration: null,
      tables: [],
      vtables: [],
      vPageSize: 20,
      vCurrentPage: 1,
      filterSchema: '',
      editor: null,
      tableLoading: false,
      columns: [],
      rows: [],
      pageSize: 10,
      currentPage: 1
    }
  },
  computed: {
    ptables() {
      return this.vtables.slice((this.vCurrentPage - 1) * this.vPageSize, this.vCurrentPage * this.vPageSize)
    }
  },
  created() {
    this.loadData()
  },
  mounted() {
    this.editor = CodeMirror.fromTextArea(this.$refs.sqleditor, {
      tabSize: 4,
      mode: 'text/x-mysql',
      theme: 'default',
      lineNumbers: true,
      line: true,
      lineWrapping: true,
      smartIndent: true,
      matchBrackets: true,
      hintOptions: {
        completeSingle: false,
        hint: this.hint
      }
    })
    this.editor.on('keypress', editor => {
      editor.showHint()
    })
    this.editor.setSize('auto', '200px')
  },
  methods: {
    loadData() {
      // 通过路由名删除前缀'query-'，得到id
      const id = this.$route.name.substring(6)

      this.connectLoading = true
      integrationAPI.schema(id).then(response => {
        const tables = response.result.tables
        for (let j = 0, len = tables.length; j < len; j++) {
          tables[j].show = false
        }
        this.tables = tables
        this.vtables = tables
      }).catch(() => {})
        .finally(() => {
          this.connectLoading = false
        })
        .then(() => {
          integrationAPI.get(id).then(response => {
            this.integration = response.result
            this.editor.setValue(this.integration.meta.sqlPlaceholder)
          }).catch(() => {})
        })
    },
    showTable(table) {
      table.show = !(table.show === true)
    },
    copyTableToSql(name) {
    },
    copyColumnToSql(name) {
    },
    searchTree() {
      this.vtables = []
      for (let j = 0, len = this.tables.length; j < len; j++) {
        const table = this.tables[j]
        if (table.name.includes(this.filterSchema)) {
          this.vtables.push(table)
        }
      }
      this.vCurrentPage = 1
    },
    handleVCurrentChange(val) {
      this.vCurrentPage = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    handleQueryData() {
      const data = {
        name: this.integration.name,
        sql: this.editor.getValue()
      }
      this.tableLoading = true
      integrationAPI.query(data).then(response => {
        this.columns = response.result.columns
        this.rows = response.result.rows
      }).catch(() => {
        this.columns = []
        this.rows = []
      }).finally(() => {
        this.tableLoading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>

.app-container {
  position: relative;
  height: calc(100vh - 50px);
}

.left-container {
  width: 100%;
  height: 100%;
  padding: 5px;
  background-color: #f5f7fa;
  border:1px solid rgba(0, 0, 0, .1);
  display: flex;
  flex-direction: column;
}

.tree-container {
  overflow: auto;
  flex: 1;
}

.tree-node {
  white-space: nowrap;
  background-color: #fff;
  cursor: pointer;
  border-bottom: 1px solid #ebeef5;
}

.tree-foot {
  height: 38px;
  line-height: 38px;
  background-color: #fff;
  padding-right:20px;
}

.tree-head {
  height: 38px;
  line-height: 38px;

  .table-icon {
    color: rgb(140, 197, 255);
    margin: 0 5px;
  }
}

.tree-child {

  .column-icon {
    color: rgb(140, 197, 255);
    margin-right: 5px;
  }
}

.tree-item {
  height: 30px;
  line-height: 30px;
  cursor: pointer;
  border-top: 1px solid #ebeef5;
  margin-left: 20px;
}

.right-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.button-container {
  height: 42px;
  padding: 0 14px;
  background: #f5f7fa;
  border-bottom: 1px solid #e1e3e6;
}

.editor-container {
  margin-bottom: 12px;
  border:1px solid #e1e3e6;
  background-color: #f5f7fa;
}

.tab-container {
  flex: 1;
  overflow: hidden;
  padding: 0 14px;
  border: 1px solid #e1e3e6;
}

.table-container {

}

::v-deep .el-tabs {
  display: flex;
  height: 100%;
  flex-direction: column;

  .el-tabs__content {
    flex: 1;
    overflow: auto;
  }
}
</style>
