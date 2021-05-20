<template>
    <div>
      <list v-show="showListVisible" :status="status"  :breaddata="breaddata" @hiddenList="hiddenList"></list>
      <orderDet
          ref="orderDetailCompon"
          v-if="!showListVisible"
          :index="'1'"
          @changePage="showList"
          :orderDetBreaddata="orderDetBreaddata"
      ></orderDet>
    </div>
</template>

<script>
import list from "./list";
import orderDet from "./orderDet";
export default {
  data() {
    return {
        showListVisible:true,
        // orderDetBreaddata:["订单管理", "订单管理", "订单列表", "订单详情"],
    };
  },
  // props:["status","breaddata"],
  props:{
     status: {
      type: Number,
    },
    // 列表面包屑
    breaddata: {
      type: Array,
      default() {
        return ["订单管理", "订单列表"];
      }
    },
    // 订单详情面包屑
    orderDetBreaddata: {
      type: Array,
      default() {
        return ["订单管理", "订单列表", "订单详情"];
      }
    },
  },
  created() {

  },
  methods: {
      showList(){
        this.showListVisible = true;
      },
      hiddenList(row){
        this.showListVisible = false;
        this.$nextTick(()=>{
            this.$refs.orderDetailCompon.init(row);
        })
      },

  },
  components: {
    list,
    orderDet
  }
};
</script>
<style scoped>

</style>
