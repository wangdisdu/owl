(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-235dcd2a"],{4660:function(e,a,t){},"697f":function(e,a,t){"use strict";var r=function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("div",[e.isInput(e.parameter)?t("el-input",{attrs:{placeholder:e.parameter.placeholder,type:"text"},model:{value:e.value[e.parameter.name],callback:function(a){e.$set(e.value,e.parameter.name,a)},expression:"value[parameter.name]"}}):e._e(),e.isPassword(e.parameter)?t("el-input",{attrs:{placeholder:e.parameter.placeholder,type:"password"},model:{value:e.value[e.parameter.name],callback:function(a){e.$set(e.value,e.parameter.name,a)},expression:"value[parameter.name]"}}):e._e(),e.isSelection(e.parameter)?t("el-select",{attrs:{placeholder:"请选择"},model:{value:e.value[e.parameter.name],callback:function(a){e.$set(e.value,e.parameter.name,a)},expression:"value[parameter.name]"}},e._l(e.parameter.candidates,(function(e){return t("el-option",{key:e,attrs:{label:e,value:e}})})),1):e._e(),e.isInt(e.parameter)?t("el-input",{attrs:{placeholder:e.parameter.placeholder,type:"number"},model:{value:e.value[e.parameter.name],callback:function(a){e.$set(e.value,e.parameter.name,a)},expression:"value[parameter.name]"}}):e._e(),e.isFloat(e.parameter)?t("el-input",{attrs:{placeholder:e.parameter.placeholder,type:"number"},model:{value:e.value[e.parameter.name],callback:function(a){e.$set(e.value,e.parameter.name,a)},expression:"value[parameter.name]"}}):e._e(),e.isBool(e.parameter)?t("el-radio-group",{model:{value:e.value[e.parameter.name],callback:function(a){e.$set(e.value,e.parameter.name,a)},expression:"value[parameter.name]"}},[t("el-radio",{attrs:{label:"true"}},[e._v("是")]),t("el-radio",{attrs:{label:"false"}},[e._v("否")])],1):e._e(),e.isList(e.parameter)?t("div",{staticClass:"kv-group"},[e.value[e.parameter.name]?e._e():t("el-row",{staticClass:"kv-row"},[t("el-col",{attrs:{span:24}},[t("i",{staticClass:"el-icon-circle-plus kv-icon",on:{click:function(a){return e.putArray(e.parameter.name,0)}}})])],1),e._l(e.value[e.parameter.name],(function(a,r){return t("el-row",{key:r,staticClass:"kv-row"},[t("el-col",{attrs:{span:22}},[t("el-input",{staticStyle:{width:"100%"},model:{value:e.value[e.parameter.name][r],callback:function(a){e.$set(e.value[e.parameter.name],r,a)},expression:"value[parameter.name][index]"}})],1),t("el-col",{attrs:{span:2}},[t("i",{staticClass:"el-icon-remove kv-icon",on:{click:function(a){return e.removeArray(e.parameter.name,r)}}}),t("i",{staticClass:"el-icon-circle-plus kv-icon",on:{click:function(a){return e.putArray(e.parameter.name,r)}}})])],1)}))],2):e._e(),e.isKv(e.parameter)?t("div",{staticClass:"kv-group"},[e.value[e.parameter.name]?e._e():t("el-row",{staticClass:"kv-row"},[t("el-col",{attrs:{span:24}},[t("i",{staticClass:"el-icon-circle-plus kv-icon",on:{click:function(a){return e.putMap(e.parameter.name,0)}}})])],1),e._l(e.value[e.parameter.name],(function(a,r){return t("el-row",{key:r,staticClass:"kv-row"},[t("el-col",{attrs:{span:10}},[t("el-input",{staticStyle:{width:"100%"},model:{value:a.key,callback:function(t){e.$set(a,"key",t)},expression:"item.key"}})],1),t("el-col",{staticClass:"kv-middle",attrs:{span:2}},[e._v(":")]),t("el-col",{attrs:{span:10}},[t("el-input",{staticStyle:{width:"100%"},model:{value:a.value,callback:function(t){e.$set(a,"value",t)},expression:"item.value"}})],1),t("el-col",{attrs:{span:2}},[t("i",{staticClass:"el-icon-remove kv-icon",on:{click:function(a){return e.removeMap(e.parameter.name,r)}}}),t("i",{staticClass:"el-icon-circle-plus kv-icon",on:{click:function(a){return e.putMap(e.parameter.name,r)}}})])],1)}))],2):e._e()],1)},n=[],l=(t("a434"),{name:"DynamicInput",props:{value:{type:Object,required:!0},parameter:{type:Object,required:!0}},methods:{isInput:function(e){return"STRING"===e.type&&!this.isPassword(e)&&!this.isSelection(e)},isPassword:function(e){return"STRING"===e.type&&!0===e.password},isSelection:function(e){return"STRING"===e.type&&e.candidates&&e.candidates.length>0},isInt:function(e){return"INT"===e.type||"LONG"===e.type},isFloat:function(e){return"FLOAT"===e.type||"DOUBLE"===e.type},isBool:function(e){return"BOOL"===e.type},isList:function(e){return"LIST"===e.type},isKv:function(e){return"KV"===e.type},putArray:function(e,a){this.value[e]?this.value[e].splice(a+1,0,""):this.$set(this.value,e,[""])},removeArray:function(e,a){this.value[e].splice(a,1)},putMap:function(e,a){this.value[e]?this.value[e].splice(a+1,0,{key:"",value:""}):this.$set(this.value,e,[{key:"",value:""}])},removeMap:function(e,a){this.value[e].splice(a,1)}}}),i=l,o=(t("76a2"),t("2877")),s=Object(o["a"])(i,r,n,!1,null,"377dbc02",null);a["a"]=s.exports},"6a00":function(e,a,t){},"76a2":function(e,a,t){"use strict";t("4660")},a434:function(e,a,t){"use strict";var r=t("23e7"),n=t("23cb"),l=t("a691"),i=t("50c4"),o=t("7b0b"),s=t("65f0"),c=t("8418"),u=t("1dde"),p=t("ae40"),m=u("splice"),f=p("splice",{ACCESSORS:!0,0:0,1:2}),d=Math.max,v=Math.min,h=9007199254740991,b="Maximum allowed length exceeded";r({target:"Array",proto:!0,forced:!m||!f},{splice:function(e,a){var t,r,u,p,m,f,k=o(this),y=i(k.length),g=n(e,y),w=arguments.length;if(0===w?t=r=0:1===w?(t=0,r=y-g):(t=w-2,r=v(d(l(a),0),y-g)),y+t-r>h)throw TypeError(b);for(u=s(k,r),p=0;p<r;p++)m=g+p,m in k&&c(u,p,k[m]);if(u.length=r,t<r){for(p=g;p<y-r;p++)m=p+r,f=p+t,m in k?k[f]=k[m]:delete k[f];for(p=y;p>y-r+t;p--)delete k[p-1]}else if(t>r)for(p=y-r;p>g;p--)m=p+r-1,f=p+t-1,m in k?k[f]=k[m]:delete k[f];for(p=0;p<t;p++)k[p+g]=arguments[p+2];return k.length=y-r+t,u}})},ad56:function(e,a,t){"use strict";t("6a00")},eeb7:function(e,a,t){"use strict";t.r(a);var r=function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("div",{staticClass:"app-container"},[t("el-form",{ref:"form",attrs:{model:e.form,"label-width":"120px"}},[t("el-form-item",{attrs:{label:"名称",prop:"name",rules:e.requiredRule}},[t("el-input",{model:{value:e.form.name,callback:function(a){e.$set(e.form,"name",a)},expression:"form.name"}})],1),t("el-form-item",{attrs:{label:"类型",prop:"builder",rules:e.requiredRule}},[t("el-select",{attrs:{placeholder:"请选择"},on:{change:e.onBuilderSelect},model:{value:e.form.builder,callback:function(a){e.$set(e.form,"builder",a)},expression:"form.builder"}},e._l(e.builders,(function(e){return t("el-option",{key:e.builder,attrs:{label:e.display,value:e.builder}})})),1)],1),e._l(e.form.meta.parameters,(function(a){return t("el-form-item",{key:a.name,attrs:{prop:"config."+a.name,label:a.display,rules:a.required?e.requiredRule:e.noneRule}},[t("dynamic-input",{attrs:{parameter:a},model:{value:e.form.config,callback:function(a){e.$set(e.form,"config",a)},expression:"form.config"}})],1)})),t("el-form-item",{attrs:{label:"描述"}},[t("el-input",{attrs:{type:"textarea"},model:{value:e.form.description,callback:function(a){e.$set(e.form,"description",a)},expression:"form.description"}})],1),t("el-form-item",[t("el-button",{attrs:{type:"primary"},on:{click:e.onSubmit}},[e._v("保存")]),t("el-button",{on:{click:e.onCancel}},[e._v("取消")])],1)],2)],1)},n=[],l=(t("4160"),t("b0c0"),t("159b"),t("697f")),i=t("15db"),o={name:"IntegrationCreate",components:{DynamicInput:l["a"]},data:function(){return{builders:[],form:{name:"",builder:"",description:"",config:{},meta:{}},requiredRule:{required:!0,message:"必填",trigger:"blur"},noneRule:[]}},created:function(){this.loadData()},methods:{loadData:function(){var e=this;i["a"].builders().then((function(a){e.builders=a.result})).catch((function(){}))},onBuilderSelect:function(e){for(var a=0,t=this.builders.length;a<t;a++)this.builders[a].builder===e&&(this.form.meta=this.builders[a])},onSubmit:function(){var e=this;this.$refs["form"].validate((function(a){if(a){for(var t=e.form,r=0,n=t.meta.parameters.length;r<n;r++){var l=t.meta.parameters[r];"KV"===l.type&&t.config[l.name]&&function(){var e={};t.config[l.name].forEach((function(a){e[a.key]=a.value})),t.config[l.name]=e}()}i["a"].create(e.form).then((function(a){e.$router.push("/integration")})).catch((function(){}))}}))},onCancel:function(){this.$router.push("/integration")}}},s=o,c=(t("ad56"),t("2877")),u=Object(c["a"])(s,r,n,!1,null,"248ffc35",null);a["default"]=u.exports}}]);