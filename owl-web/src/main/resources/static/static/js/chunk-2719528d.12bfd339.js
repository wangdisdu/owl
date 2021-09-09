(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2719528d"],{"3c3a":function(e,t,r){},"7cf8":function(e,t,r){"use strict";var n=r("b775");function a(e){var t="/api/v1/monitor/"+e+"/metric";return Object(n["a"])({url:t,method:"get"})}function o(e,t){var r="/api/v1/monitor/"+e+"/history";return Object(n["a"])({url:r,method:"post",data:t})}t["a"]={metric:a,history:o}},ddff:function(e,t,r){"use strict";r.r(t);var n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticClass:"app-container"},[r("div",{staticClass:"top-container"},[r("el-tabs",{attrs:{type:"border-card"}},[r("el-tab-pane",{attrs:{label:"Consumer"}},[r("div",{staticClass:"search-container"},[r("el-input",{staticStyle:{width:"200px","margin-right":"20px"},attrs:{size:"small",placeholder:"GROUP",clearable:""},model:{value:e.consumerGroup,callback:function(t){e.consumerGroup=t},expression:"consumerGroup"}}),r("el-input",{staticStyle:{width:"200px","margin-right":"20px"},attrs:{size:"small",placeholder:"TOPIC",clearable:""},model:{value:e.consumerTopic,callback:function(t){e.consumerTopic=t},expression:"consumerTopic"}}),r("el-button",{attrs:{size:"small",type:"primary",icon:"el-icon-search"},on:{click:function(t){return e.consumerRender()}}},[e._v("Search")])],1),r("div",{staticClass:"table-container"},[r("el-table",{attrs:{border:"",size:"small",data:e.consumerMetrics.slice((e.consumerCurrentPage-1)*e.consumerPageSize,e.consumerCurrentPage*e.consumerPageSize)},on:{"sort-change":e.consumerSortChange}},[r("el-table-column",{attrs:{prop:"offset.tag1",label:"GROUP",sortable:""}}),r("el-table-column",{attrs:{prop:"offset.tag2",label:"TOPIC",sortable:""}}),r("el-table-column",{attrs:{prop:"offset.value",label:"OFFSET",sortable:""}}),r("el-table-column",{attrs:{prop:"lag.value",label:"LAGS",sortable:""}})],1)],1),r("div",{attrs:{lass:"page-container"}},[r("el-pagination",{attrs:{align:"right",layout:"total, prev, pager, next, sizes","current-page":e.consumerCurrentPage,"page-size":e.consumerPageSize,total:e.consumerMetrics.length},on:{"current-change":e.consumerCurrentChange,"size-change":e.consumerSizeChange}})],1)]),r("el-tab-pane",{attrs:{label:"Producer"}},[r("div",{staticClass:"search-container"},[r("el-input",{staticStyle:{width:"200px","margin-right":"20px"},attrs:{size:"small",placeholder:"TOPIC",clearable:""},model:{value:e.producerTopic,callback:function(t){e.producerTopic=t},expression:"producerTopic"}}),r("el-select",{staticStyle:{width:"200px","margin-right":"20px"},attrs:{size:"small",placeholder:"STATE",clearable:""},model:{value:e.producerState,callback:function(t){e.producerState=t},expression:"producerState"}},e._l(["normal","error"],(function(e){return r("el-option",{key:e,attrs:{label:e,value:e}})})),1),r("el-button",{attrs:{size:"small",type:"primary",icon:"el-icon-search"},on:{click:function(t){return e.producerRender()}}},[e._v("Search")])],1),r("div",{staticClass:"table-container"},[r("el-table",{attrs:{border:"",size:"small",data:e.producerMetrics.slice((e.producerCurrentPage-1)*e.producerPageSize,e.producerCurrentPage*e.producerPageSize)},on:{"sort-change":e.producerSortChange}},[r("el-table-column",{attrs:{prop:"records_count.tag1",label:"TOPIC",sortable:""}}),r("el-table-column",{attrs:{label:"STATE",prop:"partitions_miss.value",sortable:""},scopedSlots:e._u([{key:"default",fn:function(t){return[0==t.row.partitions_miss.value?r("el-tag",{attrs:{type:"success"}},[e._v("normal")]):e._e(),t.row.partitions_miss.value>0?r("el-tag",{attrs:{type:"danger"}},[e._v("error")]):e._e()]}}])}),r("el-table-column",{attrs:{prop:"partitions_count.value",label:"PARTITIONS",sortable:""},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.partitions_count.value)+" "),t.row.partitions_miss.value>0?r("span",[e._v("(miss "+e._s(t.row.partitions_miss.value)+")")]):e._e()]}}])}),r("el-table-column",{attrs:{prop:"records_count.value",label:"RECORDS",sortable:""}}),r("el-table-column",{attrs:{prop:"begin_offset.value",label:"OFFSETS",sortable:""},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.begin_offset.value)+" - "+e._s(t.row.end_offset.value)+" ")]}}])})],1)],1),r("div",{attrs:{lass:"page-container"}},[r("el-pagination",{attrs:{align:"right",layout:"total, prev, pager, next, sizes","current-page":e.producerCurrentPage,"page-size":e.producerPageSize,total:e.producerMetrics.length},on:{"current-change":e.producerCurrentChange,"size-change":e.producerSizeChange}})],1)])],1)],1)])},a=[],o=(r("caad"),r("b0c0"),r("d3b7"),r("2532"),r("2ef0")),s=r("7cf8"),i=r("ed08"),c=r("eff9"),u={name:"Monitor",components:{linechart:c["a"]},data:function(){return{id:"",loading:!1,consumerCurrentPage:1,consumerPageSize:10,consumerGroup:"",consumerTopic:"",consumerSortkey:"",consumerSortOrder:"",consumerMetrics:[],consumerMetricsAll:[],producerCurrentPage:1,producerPageSize:10,producerTopic:"",producerState:"",producerSortkey:"",producerSortOrder:"",producerMetrics:[],producerMetricsAll:[]}},created:function(){this.fetchData()},methods:{humanReadable:i["a"],fetchData:function(){var e=this,t=this.$route.name.substring(8);this.id=t,this.loading=!0,s["a"].metric(this.id).then((function(t){var r=Object(o["filter"])(t.result,["category","consumer"]),n=Object(o["groupBy"])(r,"instance"),a=[];Object(o["forIn"])(n,(function(e,t){var r={};Object(o["forEach"])(e,(function(e){r[e.metric]=e})),a.push(r)})),e.consumerMetrics=a,e.consumerMetricsAll=a;var s=Object(o["filter"])(t.result,["category","producer"]),i=Object(o["groupBy"])(s,"instance"),c=[];Object(o["forIn"])(i,(function(e,t){var r={};Object(o["forEach"])(e,(function(e){r[e.metric]=e})),c.push(r)})),e.producerMetrics=c,e.producerMetricsAll=c})).catch((function(){})).finally((function(){e.loading=!1}))},consumerCurrentChange:function(e){this.consumerCurrentPage=e},consumerSizeChange:function(e){this.consumerPageSize=e},consumerSortChange:function(e){this.consumerSortkey=e.prop,this.consumerSortOrder=e.order,this.consumerRender()},consumerRender:function(){var e=this.consumerSortkey,t=this.consumerSortOrder,r=this.consumerGroup,n=this.consumerTopic;this.consumerMetrics=r||n?Object(o["filter"])(this.consumerMetricsAll,(function(e){return(!r||e.offset.tag1.includes(r))&&(!n||e.offset.tag2.includes(n))})):this.consumerMetricsAll,e&&"ascending"==t?this.consumerMetrics=Object(o["orderBy"])(this.consumerMetrics,[e],["asc"]):e&&"descending"==t&&(this.consumerMetrics=Object(o["orderBy"])(this.consumerMetrics,[e],["desc"]))},producerCurrentChange:function(e){this.producerCurrentPage=e},producerSizeChange:function(e){this.producerPageSize=e},producerSortChange:function(e){this.producerSortkey=e.prop,this.producerSortOrder=e.order,this.producerRender()},producerRender:function(){var e=this.producerSortkey,t=this.producerSortOrder,r=this.producerTopic,n=this.producerState;this.producerMetrics=r||n?Object(o["filter"])(this.producerMetricsAll,(function(e){return(!r||e.records_count.tag1.includes(r))&&(!n||"normal"==n&&0==e.partitions_miss.value||"error"==n&&e.partitions_miss.value>0)})):this.producerMetricsAll,e&&"ascending"==t?this.producerMetrics=Object(o["orderBy"])(this.producerMetrics,[e],["asc"]):e&&"descending"==t&&(this.producerMetrics=Object(o["orderBy"])(this.producerMetrics,[e],["desc"]))}}},l=u,d=(r("fa7f"),r("2877")),p=Object(d["a"])(l,n,a,!1,null,"0ceae7e4",null);t["default"]=p.exports},ed08:function(e,t,r){"use strict";r.d(t,"a",(function(){return n}));r("4160"),r("c975"),r("b680"),r("d3b7"),r("4d63"),r("ac1f"),r("25f0"),r("4d90"),r("5319"),r("1276"),r("159b"),r("53ca");function n(e,t){return"percent"===t?e+"%":"count"===t?a(e):"ms"===t?o(e):"bytes"===t?s(e):"kilobytes"===t?i(e):e}function a(e){if(Math.abs(e)<1e4)return e+"";var t=1,r=["万","亿","万亿"],n=-1,a=Math.pow(10,t);do{e/=1e4,++n}while(Math.round(Math.abs(e)*a)/a>=1e4&&n<r.length-1);return e.toFixed(t)+" "+r[n]}function o(e){return Math.abs(e)<1e3?e+"毫秒":(e/=1e3,e<60?e.toFixed(1)+"秒":(e/=60,e<60?e.toFixed(1)+"分钟":(e/=60,e<60?e.toFixed(1)+"小时":(e/=24,e.toFixed(1)+"天"))))}function s(e){if(Math.abs(e)<1024)return e+" B";var t=1,r=["kB","MB","GB","TB","PB","EB","ZB","YB"],n=-1,a=Math.pow(10,t);do{e/=1024,++n}while(Math.round(Math.abs(e)*a)/a>=1024&&n<r.length-1);return e.toFixed(t)+" "+r[n]}function i(e){if(Math.abs(e)<1024)return e+" kB";var t=1,r=["MB","GB","TB","PB","EB","ZB","YB"],n=-1,a=Math.pow(10,t);do{e/=1024,++n}while(Math.round(Math.abs(e)*a)/a>=1024&&n<r.length-1);return e.toFixed(t)+" "+r[n]}},eff9:function(e,t,r){"use strict";var n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{style:{height:e.height,width:e.width}})},a=[],o=r("313e"),s=r("7cf8"),i=r("ed08"),c={props:{width:{type:String,default:"100%"},height:{type:String,default:"250px"},settings:{type:Object,required:!0}},data:function(){return{data:[],chart:null}},watch:{settings:{deep:!0,handler:function(){this.refreshChart()}}},mounted:function(){var e=this;this.$nextTick((function(){e.initChart()}))},beforeDestroy:function(){this.chart&&(this.chart.dispose(),this.chart=null)},methods:{initChart:function(){this.chart=o["a"](this.$el),this.refreshChart()},refreshChart:function(){var e=this;this.data=[],s["a"].history(this.settings.reqId,this.settings.reqBody).then((function(t){for(var r=t.result,n=0,a=r.length;n<a;n++)e.data.push([r[n].time,r[n].value]);e.setOptions()})).catch((function(){}))},setOptions:function(){var e=this,t={grid:{top:"10%",bottom:"20%",left:"10%",rigth:"10%"},tooltip:{trigger:"axis",formatter:function(t){var r=t[0].axisValueLabel,n=t[0].data[1],a=t[0].marker,o=Object(i["a"])(n,e.settings.unit);return o===n?r+"<br />"+a+n:"percent"===e.settings.unit?r+"<br />"+a+o:r+"<br />"+a+o+" ("+n+")"}},xAxis:{type:"time"},yAxis:{type:"value",scale:!0,axisLabel:{formatter:function(t,r){return Object(i["a"])(t,e.settings.unit)}}},series:{name:"value",type:"line",symbol:"circle",data:e.data,lineStyle:{width:1,color:e.settings.color},itemStyle:{color:e.settings.color}}};this.chart.setOption(t)}}},u=c,l=r("2877"),d=Object(l["a"])(u,n,a,!1,null,null,null);t["a"]=d.exports},fa7f:function(e,t,r){"use strict";r("3c3a")}}]);