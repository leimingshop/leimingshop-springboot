<template>
  <!-- 面包屑组件 -->
  <div class="breadcrumb1">
    <Breadcrumb :separator="separator">
      <BreadcrumbItem v-for="(item, index) in list" :key="index" :to="item.toPath">{{ item.title }}</BreadcrumbItem>
      <slot></slot>
    </Breadcrumb>
  </div>
</template>
<script>
export default {
  props: {
    list: {
      type: Array,
      default: () => {
        return [
          {
            title: "首页",
            toPath: "/",
          },
        ];
      },
    },
  },
  data() {
    return {
      separator: ">",
    };
  },
};
</script>
<style lang="scss" scoped>
.breadcrumb1 {
  height: 40px;
}
/deep/ .ivu-breadcrumb {
  width: 1200px;
  margin: 0 auto;
  line-height: 40px;
  height: 40px;
}
/deep/ .ivu-breadcrumb > span {
  color: #666;
  font-size: 13px;
  font-weight: 400;
  &:hover {
    cursor: text;
  }
  a {
    color: #666;
    font-weight: 400;
    &:hover {
      color: #dd2619;
    }
  }
  .ivu-breadcrumb-item-separator {
    margin: 0 4px;
    color: #666;
    /*font-family: simsun;*/
  }
}
</style>