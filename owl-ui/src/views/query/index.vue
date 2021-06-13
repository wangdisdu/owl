<template>
  <div class="app-container">
    <div class="content-container">
      <div class="editor-container">
        <textarea ref="sqleditor"></textarea>
      </div>
    </div>
    <div class="content-container" style="float: right;">
      <el-button type="primary" icon="el-icon-search" @click="handleFilter">
        Search
      </el-button>
    </div>
    <div class="content-container">
        <el-table
          v-loading="listLoading"
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
        <div class="block" style="margin-top:15px;">
          <el-pagination align='right' 
            @current-change="handleCurrentChange" 
            layout="total, prev, pager, next" 
            :current-page="currentPage" 
            :page-size="pageSize" 
            :total="rows.length">
          </el-pagination>
        </div>
    </div>
  </div>
</template>

<script>

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
  data() {
    return {
      integration: null,
      editor: null,
      listLoading:false,
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
      },
      extraKeys: {
        "Ctrl-Space": editor => {
          editor.showHint();
        }
      }
    });
    this.editor.on("keypress", editor => {
      editor.showHint();
    });
  },
  methods: {
    loadData() {
      //删除前缀'integration-'
      const id = this.$route.name.substring(12)
      integrationAPI.get(id).then(response => {
        this.integration = response.result
        this.editor.setValue(this.integration.meta.sqlPlaceholder)
      }).catch(() => {})
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    handleFilter() {
      const data = {
        name: this.integration.name, 
        sql: this.editor.getValue()
      }
      this.listLoading = true
      integrationAPI.query(data).then(response => {
        this.columns = response.result.columns
        this.rows = response.result.rows
      })
      .catch(() => {})
      .finally(() => {
        this.listLoading = false;
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.content-container{
  margin-bottom: 12px;
}

.editor-container{
  border:1px solid rgba(0, 0, 0, .1);
}
</style>