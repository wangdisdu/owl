<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="名称">
        <el-input v-model="form.name" disabled/>
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="form.meta.display" disabled>
        </el-select>
      </el-form-item>
      <el-form-item 
        v-for="parameter in form.meta.parameters" 
        :prop="'config.' + parameter.name"
        :label="parameter.display" 
        :rules="parameter.required ? requiredRule : noneRule">
        <el-input v-model="form.config[parameter.name]" />
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
import integrationAPI from '@/api/integration.js'

export default {
  props: ['id'],
  data() {
    return {
      form: {
        name: '',
        builder: '',
        description: '',
        config: {},
        meta: {}
      },
      requiredRule: [
        { required: true, message: '必填', trigger: 'blur' }
      ],
      noneRule: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      integrationAPI.get(this.id).then(response => {
        this.form = response.result
      }).catch(() => {})
    },
    onSubmit() {
      integrationAPI.update(this.form).then(response => {
        this.$router.push('/integration')
      }).catch(() => {})
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

