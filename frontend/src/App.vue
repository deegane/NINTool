<template>
  <div id="app">
    <el-menu theme="dark" :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">Generator</el-menu-item>
      <el-menu-item index="2">Validator</el-menu-item>
    </el-menu>
    <img src="./assets/logo.png">
    <router-view></router-view>
  </div>
</template>

<script>
  import 'element-ui/lib/theme-default/index.css'
  import { Menu, MenuItem } from 'element-ui'
  import lang from 'element-ui/lib/locale/lang/en'
  import locale from 'element-ui/lib/locale'
  import router from './router'
  locale.use(lang)

export default {
  name: 'app',
  components: {
    'el-menu': Menu,
    'el-menu-item': MenuItem
  },
  data() {
    return {
      activeIndex: '1',
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      key == 1 ? router.push("/nin/generator") : router.push("/nin/validator")
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
</style>
