<template>
  <div class="app-container" v-loading="connectLoading" element-loading-text="Connecting">
    <split-pane :min-percent='10' :default-percent='30' split="vertical">
      <template slot="paneL">
        <div class="schema-container">
          <el-input v-model="filterSchema" 
            placeholder="search" 
            style="margin-bottom:10px;"
            @keyup.enter.native="searchTree()" 
            />
          <div class="tree-container">
            <div v-for="(table, i) in vtables" v-if="i >= (vCurrentPage-1)*vPageSize && i < vCurrentPage*vPageSize" class="tree-node">
              <div class="tree-head" @click="showTable(table)" @dblclick="copyToSql(table.name)">
                <span class="table-icon"><svg-icon icon-class="table" /> </span>
                <span>{{table.name}}</span>
              </div>
              <el-collapse-transition>
                <div v-show="table.show" class="tree-child">
                  <div v-for="(column, j) in table.columns"  class="tree-item" @dblclick="copyToSql(column.name)">
                    <span class="column-icon"><svg-icon icon-class="column" /> </span>
                    <span>{{column.name}}</span>
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
                @current-change="handleVCurrentChange" 
                :current-page="vCurrentPage" 
                :page-size="vPageSize" 
                :hide-on-single-page="true"
                :total="vtables.length">
              </el-pagination>
            </div>
          </div>
        </div>
      </template>
      <template slot="paneR">
        <div style="height: 100%; display: flex; flex-direction: column;">
        <div class="editor-container">
          <div class="button-container">
            <el-button-group style="float: right; margin-top: 4px;">
              <el-button icon="el-icon-video-play" size="small" @click="handleQueryData">Run</el-button>
              <el-button icon="el-icon-video-pause" size="small">Stop</el-button>
            </el-button-group>
          </div>
          <div>
            <textarea ref="sqleditor"></textarea>
          </div>
        </div>
        <div class="table-container">
          <el-table
            v-loading="tableLoading"
            :data="rows.slice((currentPage-1)*pageSize,currentPage*pageSize)"
            element-loading-text="Loading"
            border
            fit
            highlight-current-row>

            <el-table-column v-for="column in columns" :label="column.name">
               <template slot-scope="scope">
                {{scope.row[column.name]}}
              </template>
            </el-table-column>

          </el-table>
          <div class="block" v-if="rows.length > 0" style="margin-top:10px;">
            <el-pagination align="right"
              @current-change="handleCurrentChange" 
              layout="total, prev, pager, next" 
              :current-page="currentPage" 
              :page-size="pageSize" 
              :total="rows.length">
            </el-pagination>
          </div>
        </div></div>
      </template>
    </split-pane>
  </div>
</template>

<script>

import splitPane from 'vue-splitpane'
import CodeMirror from "codemirror"
import "codemirror/lib/codemirror.css"
import "codemirror/mode/sql/sql.js"
import "codemirror/addon/hint/show-hint.js"
import "codemirror/addon/hint/show-hint.css"
import "codemirror/addon/hint/sql-hint.js"
import "codemirror/addon/edit/matchbrackets.js"

import integrationAPI from '@/api/integration.js'

export default {
  name: "SqlEditor",
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
  created() {
    this.loadData()
  },
  mounted() {
    this.editor = CodeMirror.fromTextArea(this.$refs.sqleditor, {
      tabSize: 4,
      mode: "text/x-mysql",
      theme: "default",
      lineNumbers: true,
      line: true,
      lineWrapping: true,
      smartIndent: true,
      matchBrackets:true,
      hintOptions: {
        completeSingle: false,
        hint: this.hint
      }
    });
    this.editor.on("keypress", editor => {
      editor.showHint();
    });
    this.editor.setSize('auto','200px');
  },
  methods: {
    resize() {
      
    },
    loadData() {
      //删除前缀'integration-'
      const id = this.$route.name.substring(12)
      integrationAPI.get(id).then(response => {
        this.integration = response.result
        this.editor.setValue(this.integration.meta.sqlPlaceholder)
      }).catch(() => {})

      this.connectLoading = true
      integrationAPI.schema(id).then(response => {
        let tables = response.result.tables
        for(let j = 0,len=tables.length; j < len; j++) {
          tables[j].show = false
        }
        this.tables = tables
        this.vtables = tables
      })
      .catch(() => {})
      .finally(() => {
        this.connectLoading = false;
      })
    },
    showTable(table) {
      table.show = !(true === table.show)
    },
    copyToSql(name) {
      
    },
    searchTree() {
      this.vtables = []
      for(let j = 0,len=this.tables.length; j < len; j++) {
        let table = this.tables[j]
        if(table.name.includes(this.filterSchema)) {
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
      })
      .catch(() => {})
      .finally(() => {
        this.tableLoading = false;
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

.schema-container {
  width: 100%;
  height: 100%;
  padding: 5px;
  background-color: #f0f0f0;
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

.editor-container {
  margin-bottom: 12px;
  border:1px solid rgba(0, 0, 0, .1);
}

.button-container {
  background: #f5f9fe;
  height: 42px;
  border-bottom: 1px solid #e1e3e6;
  padding: 0 14px;
}

.table-container {
  flex: 1;
  overflow: auto;
}

</style>