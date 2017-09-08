<template>
  <div class="NINGenerator">
    <h1>{{ msg }}</h1>
    <el-form :model="details" ref="details">
      <el-form-item prop="gender" :rules="[{ required: true, message: 'gender required' }]">
        <el-select v-model="details.gender" placeholder="Select Gender">
          <el-option
            v-for="item in genders"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item prop="dob" :rules="[{ required: true, message: 'dob required' }] " >
        <el-date-picker id="dob"
                        v-model="details.dob"
                        type="date"
                        placeholder="Select DOB"
                        format="dd-MM-yyyy">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">Generate NIN</el-button>
        <el-button @click="resetForm">Reset</el-button>
      </el-form-item>
    </el-form>

    <p v-if="NIN">NIN:<b>  {{ NIN }} </b></p>
    <p v-if="errorMsg"><b>Error:</b> {{ errorMsg }}</p>
  </div>
</template>

<script>
import axios from 'axios'
import 'element-ui/lib/theme-default/index.css'
import { Button, Select, Input, Option, DatePicker, Form, FormItem } from 'element-ui'
import lang from 'element-ui/lib/locale/lang/en'
import locale from 'element-ui/lib/locale'
locale.use(lang)

export default {
  name: 'NINGenerator',
  components: {
    'el-select': Select,
    'el-option': Option,
    'el-date-picker': DatePicker,
    'el-button': Button,
    'el-input': Input,
    'el-form': Form,
    'el-form-item': FormItem
  },
  data () {
    return {
      msg: 'Generator',
      errorMsg: '',
      details: {
        dob: '',
        gender: ''
      },
      NIN: '',
      genders: [{
        value: 'MALE',
        label: 'Male'
      }, {
        value: 'FEMALE',
        label: 'Female'
      }]
    }
  },
  mounted: function () {
    document.addEventListener('keypress', this.submitForm, false)
  },
  beforeDestroy: function () {
    document.removeEventListener('keypress', this.submitForm, false)
  },
  methods: {
    submitForm(e) {
      if(e.key=='Enter') {
        this.$refs['details'].validate((valid) => {
          if (valid) {
            this.post()
          }
        })
      }
    },
    resetForm() {
      this.NIN = ''
      this.errorMsg = ''
      this.$refs['nin'].resetFields()
    },
    post () {
      axios.post('/generate', {
        gender: this.details.gender,
        dob: this.details.dob
      }).then(response => {
        this.NIN = response.data
      }).catch(error => {
        this.NIN = ''
        if(error.response.status==404) {
          this.errorMsg = "cannot contact server"
        } else {
          this.errorMsg = error.response.data
        }
      })
    }
  }
}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

div {
  padding-bottom: 0px;
}

</style>

<style>
  div.el-form-item__error {
    position:static;
  }
</style>
