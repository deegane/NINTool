<template>
  <div class="NINBatch">
    <h1>{{ msg }}</h1>
    <el-form :model="batch" ref="batchForm">


      <el-form-item prop="from" :rules="[{ required: true, message: 'from date required' }] " >
        <el-date-picker id="from"
                        v-model="batch.from"
                        type="date"
                        placeholder="Select From Date"
                        format="DD-MM-YYYY">
        </el-date-picker>
      </el-form-item>


      <el-form-item prop="to" :rules="[{ required: true, message: 'to date required' }] " >
        <el-date-picker id="to"
                        v-model="batch.to"
                        type="date"
                        placeholder="Select To Date"
                        format="DD-MM-YYYY">
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
                        { pattern: /^[1-5]$/, message: 'Enter a number between 1 and 5'}
                        ]">
          <el-input id="numberToGenerate" placeholder="Number to generate" v-model="batch.numberToGenerate"/>
      </el-form-item>


      <el-form-item class="button-row">
        <el-button type="primary" @click="submitForm" :loading="isLoading">Generate File</el-button>
        <el-button @click="resetForm">Reset</el-button>
      </el-form-item>
    </el-form>

    <p v-if="NIN">NIN:<b>  {{ NIN }} </b></p>
    <p v-if="errorMsg"><b>Error:</b> {{ errorMsg }}</p>
  </div>
</template>

<script>
import axios from 'axios'
import dayjs from 'dayjs'

export default {
  name: 'NINBatch',
  data () {
    return {
      msg: 'Batch',
      errorMsg: '',
      isLoading: false,
      batch: {
        from: '',
        to: '',
        gender: '',
        numberToGenerate: ''
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
  mounted() {
    document.addEventListener('keypress', this.submitForm, false)
  },
  beforeUnmount() {
    document.removeEventListener('keypress', this.submitForm, false)
  },
  methods: {
    submitForm(e) {
      this.errorMsg = ''
      if(e.type==='click' || (e.type==='keypress' && e.key==='Enter')) {
        this.$refs.batchForm.validate((valid) => {
          if (valid) {
            this.post()
          }
        })
      }
    },
    resetForm() {
      this.NIN = ''
      this.errorMsg = ''
      this.$refs.batchForm.resetFields()
    },
    post () {
      this.isLoading = true
      this.errorMsg = ''
      axios.post('/batch', {
        gender: this.batch.gender,
        from: this.batch.from,
        to: this.batch.to,
        numberToGenerate: this.batch.numberToGenerate,
      }).then(response => {
        let blob = new Blob([response.data], { type:  'application/octet-stream' } )
        let link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = this.batch.numberToGenerate + "_" + this.batch.gender + "_NINs_from_" + dayjs(this.batch.from).format('DD-MM-YYYY')
          + "_to_" + dayjs(this.batch.to).format('DD-MM-YYYY') +"_"+ ".txt"
        link.click()
      }).catch(error => {
        this.NIN = ''
        if(!error.response) {
          this.errorMsg = "cannot contact server"
        } else {
          this.errorMsg = error.response.data
        }
      }).finally(() => {
        this.isLoading = false
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

.NINBatch {
  max-width: 300px;
  margin: 0 auto;
  padding: 20px;
}


</style>
