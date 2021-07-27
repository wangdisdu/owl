(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-77c3c136"],{5856:function(t,e,n){},"937a":function(t,e,n){"use strict";n.r(e);var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{directives:[{name:"loading",rawName:"v-loading",value:t.connectLoading,expression:"connectLoading"}],staticClass:"app-container",attrs:{"element-loading-text":"Connecting"}},[n("split-pane",{attrs:{"min-percent":10,"default-percent":30,split:"vertical"}},[n("template",{slot:"paneL"},[n("div",{staticClass:"left-container"},[n("el-input",{staticStyle:{"margin-bottom":"10px"},attrs:{placeholder:"search"},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.searchTree()}},model:{value:t.filterSchema,callback:function(e){t.filterSchema=e},expression:"filterSchema"}}),n("div",{staticClass:"tree-container"},[t._l(t.ptables,(function(e,a){return n("div",{key:a,staticClass:"tree-node"},[n("div",{staticClass:"tree-head",on:{click:function(n){return t.showTable(e)},dblclick:function(n){return t.copyTableToSql(e.name)}}},[n("span",{staticClass:"table-icon"},[n("svg-icon",{attrs:{"icon-class":"table"}})],1),n("span",[t._v(t._s(e.name))])]),n("el-collapse-transition",[n("div",{directives:[{name:"show",rawName:"v-show",value:e.show,expression:"table.show"}],staticClass:"tree-child"},t._l(e.columns,(function(e,a){return n("div",{key:a,staticClass:"tree-item",on:{dblclick:function(n){return t.copyColumnToSql(e.name)}}},[n("span",{staticClass:"column-icon"},[n("svg-icon",{attrs:{"icon-class":"column"}})],1),n("span",[t._v(t._s(e.name))])])})),0)])],1)})),n("div",{staticClass:"tree-foot"},[n("el-pagination",{attrs:{align:"right",layout:"prev, next","prev-text":"上一页","next-text":"下一页","hide-on-single-page":!0,"current-page":t.vCurrentPage,"page-size":t.vPageSize,total:t.vtables.length},on:{"current-change":t.handleVCurrentChange}})],1)],2)],1)]),n("template",{slot:"paneR"},[n("div",{staticClass:"right-container"},[n("div",{staticClass:"editor-container"},[n("div",{staticClass:"button-container"},[n("el-button-group",{staticStyle:{float:"right","margin-top":"4px"}},[n("el-button",{attrs:{icon:"el-icon-video-play",size:"small"},on:{click:t.handleQueryData}},[t._v("Run")])],1)],1),n("div",[n("textarea",{ref:"sqleditor"})])]),n("div",{staticClass:"tab-container"},[n("el-tabs",[n("el-tab-pane",{attrs:{label:"Result"}},[n("div",{staticClass:"table-container"},[n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.tableLoading,expression:"tableLoading"}],attrs:{data:t.rows.slice((t.currentPage-1)*t.pageSize,t.currentPage*t.pageSize),"header-cell-style":{background:"#f5f7fa"},"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[t._v(" > "),t._l(t.columns,(function(e,a){return n("el-table-column",{key:a,attrs:{label:e.name},scopedSlots:t._u([{key:"default",fn:function(n){return[t._v(" "+t._s(n.row[e.name])+" ")]}}],null,!0)})}))],2)],1),t.rows.length>0?n("div",{staticClass:"block",staticStyle:{"margin-top":"10px"}},[n("el-pagination",{attrs:{align:"right",layout:"total, prev, pager, next","current-page":t.currentPage,"page-size":t.pageSize,total:t.rows.length},on:{"current-change":t.handleCurrentChange}})],1):t._e()]),n("el-tab-pane",{attrs:{label:"Log"}}),n("el-tab-pane",{attrs:{label:"History"}})],1)],1)])])],2)],1)},i=[],s=(n("caad"),n("fb6a"),n("b0c0"),n("d3b7"),n("2532"),n("19ab")),l=n.n(s),o=n("56b3"),r=n.n(o),c=(n("a7be"),n("ffda"),n("9b74"),n("f6b6"),n("991c"),n("8c33"),n("15db")),u={name:"SqlEditor",components:{splitPane:l.a},data:function(){return{connectLoading:!1,integration:null,tables:[],vtables:[],vPageSize:20,vCurrentPage:1,filterSchema:"",editor:null,tableLoading:!1,columns:[],rows:[],pageSize:10,currentPage:1}},computed:{ptables:function(){return this.vtables.slice((this.vCurrentPage-1)*this.vPageSize,this.vCurrentPage*this.vPageSize)}},created:function(){this.loadData()},mounted:function(){this.editor=r.a.fromTextArea(this.$refs.sqleditor,{tabSize:4,mode:"text/x-mysql",theme:"default",lineNumbers:!0,line:!0,lineWrapping:!0,smartIndent:!0,matchBrackets:!0,hintOptions:{completeSingle:!1,hint:this.hint}}),this.editor.on("keypress",(function(t){t.showHint()})),this.editor.setSize("auto","200px")},methods:{loadData:function(){var t=this,e=this.$route.name.substring(12);this.connectLoading=!0,c["a"].schema(e).then((function(e){for(var n=e.result.tables,a=0,i=n.length;a<i;a++)n[a].show=!1;t.tables=n,t.vtables=n})).catch((function(){})).finally((function(){t.connectLoading=!1})).then((function(){c["a"].get(e).then((function(e){t.integration=e.result,t.editor.setValue(t.integration.meta.sqlPlaceholder)})).catch((function(){}))}))},showTable:function(t){t.show=!(!0===t.show)},copyTableToSql:function(t){},copyColumnToSql:function(t){},searchTree:function(){this.vtables=[];for(var t=0,e=this.tables.length;t<e;t++){var n=this.tables[t];n.name.includes(this.filterSchema)&&this.vtables.push(n)}this.vCurrentPage=1},handleVCurrentChange:function(t){this.vCurrentPage=t},handleCurrentChange:function(t){this.currentPage=t},handleQueryData:function(){var t=this,e={name:this.integration.name,sql:this.editor.getValue()};this.tableLoading=!0,c["a"].query(e).then((function(e){t.columns=e.result.columns,t.rows=e.result.rows})).catch((function(){t.columns=[],t.rows=[]})).finally((function(){t.tableLoading=!1}))}}},h=u,d=(n("df5f"),n("2877")),g=Object(d["a"])(h,a,i,!1,null,"6dfae38e",null);e["default"]=g.exports},df5f:function(t,e,n){"use strict";n("5856")}}]);