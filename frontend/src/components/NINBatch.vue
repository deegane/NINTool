<template>
  <div class="NINBatch">
    <h1>{{ msg }}</h1>
    <el-form :model="batch" ref="batch">


      <el-form-item prop="from" :rules="[{ required: true, message: 'from date required' }] " >
        <el-date-picker id="from"
                        v-model="batch.from"
                        type="date"
                        placeholder="Select From Date"
                        format="dd-MM-yyyy">
        </el-date-picker>
      </el-form-item>


      <el-form-item prop="to" :rules="[{ required: true, message: 'to date required' }] " >
        <el-date-picker id="to"
                        v-model="batch.to"
                        type="date"
                        placeholder="Select To Date"
                        format="dd-MM-yyyy">
        </el-date-picker>
      </el-form-item>


      <el-form-item prop="gender" :rules="[{ required: true, message: 'gender required' }]">
        <el-select v-model="batch.gender" placeholder="Select Gender">
          <el-option
            v-for="item in genders"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item prop="numberToGenerate" :rules="[
                        { required: true, message: 'Number required'},
                        { pattern: /^[0-9]+$/, message: 'Number required'},
                        { min: 1, max: 5, message: 'Number outside allowable range'}
                        ]">
          <el-input id="numberToGenerate" placeholder="Number to generate" v-model="batch.numberToGenerate" width="10px"/>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" @click="submitForm">Generate File</el-button>
        <el-button @click="resetForm">Reset</el-button>
      </el-form-item>
    </el-form>

    <p v-if="NIN">NIN:<b>  {{ NIN }} </b></p>
    <p v-if="errorMsg"><b>Error:</b> {{ errorMsg }}</p>
  </div>
</template>

<script>
import axios from 'axios'
import moment from 'moment'
import 'element-ui/lib/theme-chalk/index.css'
import { Button, Select, Input, Option, DatePicker, Form, FormItem } from 'element-ui'
import lang from 'element-ui/lib/locale/lang/en'
import locale from 'element-ui/lib/locale'
locale.use(lang)

export default {
  name: 'NINBatch',
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
      msg: 'Batch',
      errorMsg: '',
      batch: {
        from: '',
        to: '',
        gender: '',
        numberToGenerate: ''
      },
      NIN: '',
      number:'',
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
      this.errorMsg = ''
      if(e.type==='click' || (e.type==='keypress' && e.key==='Enter')) {
        this.$refs['batch'].validate((valid) => {
          if (valid) {
            this.post()
          }
        })
      }
    },
    resetForm() {
      this.NIN = ''
      this.errorMsg = ''
      this.$refs['batch'].resetFields()
    },
    post () {
      axios.post('/batch', {
        gender: this.batch.gender,
        from: this.batch.from,
        to: this.batch.to,
        numberToGenerate: this.batch.numberToGenerate,
      }).then(response => {
        let blob = new Blob([response.data], { type:  'application/octet-stream' } )
        let link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = this.batch.numberToGenerate + "_" + this.batch.gender + "_NINs_from_" + moment(this.batch.from).format('DD-MM-YYYY')
          + "_to_" + moment(this.batch.to).format('DD-MM-YYYY') +"_"+ ".txt"
        link.click()
      }).catch(error => {
        this.NIN = ''
        if(error.response.status===404) {
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

div.el-input {
  width:20%;
}


div.el-select {
  width:20%;
}

</style>

<style>
  div.el-form-item__error {
    position:static;
  }
</style>
