<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="160px">
      <el-form-item label="名称" prop="name" :rules="requiredRule">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="类型" prop="builder" :rules="requiredRule">
        <el-select v-model="form.builder" placeholder="请选择" @change="onBuilderSelect">
          <el-option 
            v-for="builder in builders" 
            :key="builder.builder"
            :label="builder.display" 
            :value="builder.builder" />
        </el-select>
      </el-form-item>
      <el-form-item 
        v-for="parameter in form.meta.parameters" 
        :prop="'config.' + parameter.name"
        :label="parameter.display" 
        :rules="parameter.required ? requiredRule : noneRule">
        <el-input v-model="form.config[parameter.name]" :placeholder="parameter.placeholder" />
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
      builders: [],
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
      integrationAPI.builders().then(response => {
        this.builders = response.result
      }).catch(() => {})
    },
    onBuilderSelect(item) {
      for(let j = 0,len=this.builders.length; j < len; j++) {
        if(this.builders[j].builder == item) {
          this.form.meta = this.builders[j]
        }
      }
    },
    onSubmit() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          integrationAPI.create(this.form).then(response => {
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

