<template>
  <div>
    <el-input v-if="isInput(parameter)" v-model="value[parameter.name]" :placeholder="parameter.placeholder" type="text" />

    <el-input v-if="isPassword(parameter)" v-model="value[parameter.name]" :placeholder="parameter.placeholder" type="password" />

    <el-select v-if="isSelection(parameter)" v-model="value[parameter.name]" placeholder="请选择">
      <el-option
        v-for="item in parameter.candidates"
        :key="item"
        :label="item"
        :value="item"
      />
    </el-select>

    <el-input v-if="isInt(parameter)" v-model="value[parameter.name]" :placeholder="parameter.placeholder" type="number" />

    <el-input v-if="isFloat(parameter)" v-model="value[parameter.name]" :placeholder="parameter.placeholder" type="number" />

    <el-radio-group v-if="isBool(parameter)" v-model="value[parameter.name]">
      <el-radio label="true">是</el-radio>
      <el-radio label="false">否</el-radio>
    </el-radio-group>

    <div v-if="isList(parameter)" class="kv-group">
      <el-row v-if="!(value[parameter.name])" class="kv-row">
        <el-col :span="24">
          <i class="el-icon-circle-plus kv-icon" @click="putArray(parameter.name, 0)" />
        </el-col>
      </el-row>
      <el-row v-for="(item, index) in value[parameter.name]" :key="index" class="kv-row">
        <el-col :span="22">
          <el-input v-model="value[parameter.name][index]" style="width: 100%;" />
        </el-col>
        <el-col :span="2">
          <i class="el-icon-remove kv-icon" @click="removeArray(parameter.name, index)" />
          <i class="el-icon-circle-plus kv-icon" @click="putArray(parameter.name, index)" />
        </el-col>
      </el-row>
    </div>

    <div v-if="isKv(parameter)" class="kv-group">
      <el-row v-if="!(value[parameter.name])" class="kv-row">
        <el-col :span="24">
          <i class="el-icon-circle-plus kv-icon" @click="putMap(parameter.name, 0)" />
        </el-col>
      </el-row>
      <el-row v-for="(item, index) in value[parameter.name]" :key="index" class="kv-row">
        <el-col :span="10">
          <el-input v-model="item.key" style="width: 100%;" />
        </el-col>
        <el-col class="kv-middle" :span="2">:</el-col>
        <el-col :span="10">
          <el-input v-model="item.value" style="width: 100%;" />
        </el-col>
        <el-col :span="2">
          <i class="el-icon-remove kv-icon" @click="removeMap(parameter.name, index)" />
          <i class="el-icon-circle-plus kv-icon" @click="putMap(parameter.name, index)" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DynamicInput',
  props: {
    value: {
      type: Object,
      required: true
    },
    parameter: {
      type: Object,
      required: true
    }
  },
  methods: {
    isInput(parameter) {
      return parameter.type === 'STRING' && !this.isPassword(parameter) && !this.isSelection(parameter)
    },
    isPassword(parameter) {
      return parameter.type === 'STRING' && parameter.password === true
    },
    isSelection(parameter) {
      return parameter.type === 'STRING' && parameter.candidates && parameter.candidates.length > 0
    },
    isInt(parameter) {
      return parameter.type === 'INT' || parameter.type === 'LONG'
    },
    isFloat(parameter) {
      return parameter.type === 'FLOAT' || parameter.type === 'DOUBLE'
    },
    isBool(parameter) {
      return parameter.type === 'BOOL'
    },
    isList(parameter) {
      return parameter.type === 'LIST'
    },
    isKv(parameter) {
      return parameter.type === 'KV'
    },
    putArray(name, index) {
      if (this.value[name]) {
        this.value[name].splice(index + 1, 0, '')
      } else {
        this.$set(this.value, name, [''])
      }
    },
    removeArray(name, index) {
      this.value[name].splice(index, 1)
    },
    putMap(name, index) {
      if (this.value[name]) {
        this.value[name].splice(index + 1, 0, { key: '', value: '' })
      } else {
        this.$set(this.value, name, [{ key: '', value: '' }])
      }
    },
    removeMap(name, index) {
      this.value[name].splice(index, 1)
    }
  }
}
</script>

<style scoped>
.kv-middle {
  text-align: center;
}

.kv-group {
  margin-bottom: -10px;
}

.kv-row {
  margin-bottom: 10px;
}

.kv-icon {
  margin: 5px 0 5px 5px;
  height: 28px;
  line-height: 28px;
  font-size: 20px;
  border-color: #e9e9eb;
  color: #909399;
  cursor: pointer;
}
</style>
