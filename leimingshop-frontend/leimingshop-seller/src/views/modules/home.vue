<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-home">
      <h3>{{ $t('home.desc.title') }}</h3>
      <ul>
        <li v-for="item in $t('home.desc.list')" :key="item" v-html="item"></li>
      </ul>
    </div>
  </el-card>
</template>

<script>
export default {
}
</script>
