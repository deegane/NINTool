<template>
  <div class="NINValidator">
      <h1>{{ msg }}</h1>

      <el-form :model="nin" ref="nin">
        <el-form-item prop="input"
                      :rules="[
                        { required: true, message: 'NIN required'},
                        { pattern: /^[0-9]+$/, message: 'NIN should only contain numeric digits'},
                        { min: 11, max: 11, message: 'NIN should be 11 digits'}
                        ]">

          <el-input placeholder="Enter NIN" v-model="nin.input" width="20px"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @key.enter="submitForm" @click="submitForm">Validate</el-button>
          <el-button @click="resetForm">Reset</el-button>
        </el-form-item>
      </el-form>

      <p v-if="dob">DOB:<b>  {{ dob }} </b></p>
      <p v-if="gender">Gender:<b>  {{ gender }} </b></p>
      <p v-if="errorMsg"><b>Error:</b> {{ errorMsg }}</p>

    </div>
</template>

    <script>
    import axios from 'axios'
    import 'element-ui/lib/theme-chalk/index.css'
    import { Button, Input, InputNumber, Form, FormItem } from 'element-ui'
    import lang from 'element-ui/lib/locale/lang/en'
    import locale from 'element-ui/lib/locale'
    locale.use(lang)

    export default {
      name: 'Validator',
      components: {
        'el-button': Button,
        'el-input': Input,
        'el-form': Form,
        'el-form-item': FormItem,
        'el-input-number': InputNumber
      },
      data () {
        return {
          msg: 'Validator',
          errorMsg: '',
          nin: {
            input: ''
          },
          dob: '',
          gender: ''
    }
  },
  mounted: function () {
    document.addEventListener('keypress', this.submitForm, false)
  },
  beforeDestroy: function () {
    document.removeEventListener('keypress', this.submitForm, false)
  },
  methods: {
    submitForm (e) {
      this.errorMsg = ''
      if(e.type==='click' || (e.type==='keypress' && e.key==='Enter')) {
        this.$refs['nin'].validate((valid) => {
          if (valid) {
            this.post()
          }
        })
      }
    },
    resetForm () {
      this.dob = ''
      this.gender = ''
      this.errorMsg = ''
      this.$refs['nin'].resetFields()
    },
    post () {
      axios.post('/validate', {
        nationalIdentityNumber: this.nin.input
      })
        .then(response => {
        this.dob = response.data.dob
        this.gender = response.data.gender
      }).catch(error => {
        this.dob = ''
        this.gender = ''
        if(!error.response) {
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

</style>

<style>
  div.el-form-item__error {
    position:static;
  }
</style>
