<template>
  <div>
      <el-steps :active="showPageNumber" align-center  finish-status="success">
        <el-step title="选择商品类目"></el-step>
        <el-step title="编辑商品信息"></el-step>
        <el-step title="编辑完成"></el-step>
      </el-steps>
  </div>
</template>

<script>
export default {
   props: {
    breaddata: Array,
    showPageNumber: {
      type: Number,
      default:"1"
    },
  },
  created () {
  },
  methods: {
  }
}
</script>
