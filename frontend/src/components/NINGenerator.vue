<template>
  <div class="NINGenerator">
    <h1>{{ msg }}</h1>
    <el-form :model="details" ref="detailsForm">
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
                        format="DD-MM-YYYY">
        </el-date-picker>
      </el-form-item>

      <el-form-item class="button-row">
        <el-button type="primary" @click="submitForm" :loading="isLoading">Generate NIN</el-button>
        <el-button @click="resetForm">Reset</el-button>
      </el-form-item>
    </el-form>

    <p v-if="NIN">NIN:<b>  {{ NIN }} </b></p>
    <p v-if="errorMsg"><b>Error:</b> {{ errorMsg }}</p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'NINGenerator',
  data () {
    return {
      msg: 'Generator',
      errorMsg: '',
      isLoading: false,
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
        this.$refs.detailsForm.validate((valid) => {
          if (valid) {
            this.post()
          }
        })
      }
    },
    resetForm() {
      this.NIN = ''
      this.errorMsg = ''
      this.$refs.detailsForm.resetFields()
    },
    post () {
      this.isLoading = true
      axios.post('/generate', {
        gender: this.details.gender,
        dob: this.details.dob
      }).then(response => {
        this.NIN = response.data.nationalIdentityNumber
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

.NINGenerator {
  max-width: 300px;
  margin: 0 auto;
  padding: 20px;
}


</style>
