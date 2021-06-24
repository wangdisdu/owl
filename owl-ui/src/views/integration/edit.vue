<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="名称">
        <el-input v-model="form.name" disabled />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="form.meta.display" disabled />
      </el-form-item>
      <el-form-item
        v-for="parameter in form.meta.parameters"
        :key="parameter.name"
        :prop="'config.'+parameter.name"
        :label="parameter.display"
        :rules="parameter.required?requiredRule:noneRule"
      >
        <dynamic-input
          v-model="form.config"
          :parameter="parameter"
        />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import DynamicInput from '@/components/DynamicInput'
import integrationAPI from '@/api/integration.js'

export default {
  name: 'IntegrationEdit',
  components: {
    DynamicInput
  },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      form: {
        name: '',
        builder: '',
        description: '',
        config: {},
        meta: {}
      },
      requiredRule: { required: true, message: '必填', trigger: 'blur' },
      noneRule: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      integrationAPI.get(this.id).then(response => {
        const result = response.result
        for (let j = 0, len = result.meta.parameters.length; j < len; j++) {
          const parameter = result.meta.parameters[j]
          if (parameter.type === 'KV' && result.config[parameter.name]) {
            const arr = Object.keys(result.config[parameter.name]).map(k => {
              return {
                key: k,
                value: result.config[parameter.name][k]
              }
            })
            result.config[parameter.name] = arr
          }
        }
        this.form = result
      }).catch(() => {})
    },
    onSubmit() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const request = this.form
          for (let j = 0, len = request.meta.parameters.length; j < len; j++) {
            const parameter = request.meta.parameters[j]
            if (parameter.type === 'KV' && request.config[parameter.name]) {
              const obj = {}
              request.config[parameter.name].forEach(o => { obj[o.key] = o.value })
              request.config[parameter.name] = obj
            }
          }
          integrationAPI.update(request).then(response => {
            this.$router.push('/integration')
          }).catch(() => {})
        }
      })
    },
    onCancel() {
      this.$router.push('/integration')
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

