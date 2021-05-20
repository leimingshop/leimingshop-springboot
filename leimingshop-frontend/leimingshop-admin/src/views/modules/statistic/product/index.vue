<template>
  <div>
      <Bread :breaddata="breaddata"></Bread>
      <!-- 分类商品统计 -->
      <goodsClassStatic></goodsClassStatic>
      <!-- 单个商品销售情况 -->
      <singGoodsSale></singGoodsSale>
  </div>
</template>

<script>

import Bread from "@/components/bread"
import goodsClassStatic from "./goodsClassStatic"
import singGoodsSale from "./singGoodsSale";
export default {
  data(){
    return{
      breaddata: ["统计", "商品分析", "商品统计"],
     
      chartData:{
        columns:['分类名称','销售数量'],
        rows:[
          {'分类名称':'彩妆','销售数量':'100'},
          {'分类名称':'食品','销售数量':'100'},
          {'分类名称':'家电','销售数量':'100'},
          {'分类名称':'厨具','销售数量':'100'},
          {'分类名称':'男装','销售数量':'100'}
        ]
      },
     
    //   starttime:{
    //     disabledDate:time=>{
    //       if (this.temp.endTime){
    //         return (
    //           time.getTime()>new Date(this.temp.endTime).getTime()
    //         )
    //       }else{
    //         return time.getTime()>Date.now()
    //       }
    //     }
    //   },
    //   endTime:{
    //     disabledDate:time=>{
    //       if (this.temp.starttime){
    //       return(
    //         time.getTime()>Date.now()||
    //         time.getTime()<new Date(this.temp.starttime).getTime()-8.64e7
    //       )
    //     }else{
    //       return time.getTime()<Date.now()
    //     }
    //   }
    // },
  }
  },
  components:{
   Bread,
   goodsClassStatic,
   singGoodsSale
 },
  mounted(){

  },
  methods:{
  
  },

}
</script>
<style lang="scss" scoped>
/deep/ canvas{
// width: 1700px !important;
}
</style>