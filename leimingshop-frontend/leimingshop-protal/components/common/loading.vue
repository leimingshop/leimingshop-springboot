<template>
  <Spin class="loading" fix v-if="visible"></Spin>
</template>

<script>
// import $ from 'jquery' // ssr注释

export default {
  name: "Loading",
  data() {
    return {
      visible: false,
    };
  },
};
</script>

<style lang="scss" scoped>
.ivu-spin {
  z-index: 88;
  min-height: 40px;
  /deep/ .ivu-spin-dot {
    display: inline-block;
    width: 40px;
    height: 40px;
    animation-direction: alternate-reverse;
    animation-timing-function: ease-in;
  }
  /deep/ .ivu-spin-text {
    font-size: 26px;
    letter-spacing: 3px;
  }
}
</style>