<template>
  <div class="aui-theme-tools" :class="{ 'aui-theme-tools--open': isOpen }">
    <div class="aui-theme-tools__toggle" @click="isOpen = !isOpen">
      <svg class="icon-svg" aria-hidden="true"><use xlink:href="#icon-setting"></use></svg>
    </div>
    <div class="aui-theme-tools__content">
      <!-- <div class="aui-theme-tools__item">
        <h3>Navbar</h3>
        <el-checkbox v-model="$store.state.navbarLayoutType" true-label="colorful">colorful 鲜艳</el-checkbox>
      </div> -->
      <div class="aui-theme-tools__item">
        <h3>Sidebar</h3>
        <el-checkbox v-model="$store.state.sidebarLayoutSkin" true-label="dark">dark 黑色</el-checkbox>
      </div>
      <div class="aui-theme-tools__item">
        <h3>Theme</h3>
        <el-radio-group v-model="themeColor" @change="themeColorChangeHandle">
          <el-radio v-for="item in themeList" :key="item.name" :label="item.name">{{ `${item.name} ${item.desc}` }}</el-radio>
        </el-radio-group>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      isOpen: false,
      themeList: require('@/element-ui/config.js'),
      themeColor: 'turquoise'
    }
  },
  methods: {
    themeColorChangeHandle (val) {
      var styleList = [
        {
          id: 'J_elementTheme',
          url: `${process.env.BASE_URL}element-theme/${val}/index.css?t=${new Date().getTime()}`
        },
        {
          id: 'J_auiTheme',
          url: `${process.env.BASE_URL}element-theme/${val}/aui.css?t=${new Date().getTime()}`
        }
      ]
      for (var i = 0; i < styleList.length; i++) {
        var el = document.querySelector(`#${styleList[i].id}`)
        if (el) {
          el.href = styleList[i].url
          continue
        }
        el = document.createElement('link')
        el.id = styleList[i].id
        el.href = styleList[i].url
        el.rel = 'stylesheet'
        document.querySelector('head').appendChild(el)
      }
    }
  }
}
</script>
