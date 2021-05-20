<template>
    <div>
        <index :status="radio1" :breaddata="breaddata" :orderDetBreaddata="orderDetBreaddata"></index>
    </div>
</template>
<script>
import index from "../list/index";

export default {
  data() {
      return{
          "radio1": 20,
       breaddata: ["订单系统", "订单管理", "待发货订单"],
       orderDetBreaddata: ["订单系统", "订单管理", "待发货订单", "订单详情"],
      }

  },
  components: { index }
};
</script>
<style lang="scss" scoped>
</style>
