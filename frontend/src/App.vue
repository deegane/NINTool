<template>
  <div id="app">
    <el-menu background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
             :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">

      <el-menu-item index="1">Generator</el-menu-item>
      <el-menu-item index="2">Batch</el-menu-item>
      <el-menu-item index="3">Validator</el-menu-item>
    </el-menu>
    <img src="./assets/logo.png">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      activeIndex: '1',
    }
  },
  methods: {
    handleSelect(key) {
      if (key === '1') {
        this.$router.push('/nin/generator')
      } else if (key === '2') {
        this.$router.push('/nin/batch')
      } else {
        this.$router.push('/nin/validator')
      }
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0px;
}

/* Uniform width for all form controls */
.el-form .el-select,
.el-form .el-date-editor,
.el-form .el-input {
  width: 100%;
}

/* Buttons stretch to match input width */
.el-form .button-row .el-form-item__content {
  display: flex;
  justify-content: space-between;
}

.el-form .button-row .el-button {
  flex: 1;
}

.el-form .button-row .el-button + .el-button {
  margin-left: 12px;
}

.el-form-item__error {
  position: static;
}
</style>
