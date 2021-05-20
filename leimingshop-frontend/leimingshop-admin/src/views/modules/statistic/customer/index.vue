<template>
  <div>
    <Bread :breaddata="breaddata"></Bread>
     <!-- 会员等级 -->
      <membershipGrowth></membershipGrowth>
      <!-- 会员来源 -->
      <memberSource></memberSource>
      <!-- 会员等级 -->
      <!-- <memberGrade></memberGrade> -->

  </div>
</template>

<script>
import Bread from "@/components/bread";
import membershipGrowth from "./membershipGrowth";
import memberSource from "./memberSource";
import memberGrade from "./memberGrade";
export default {
 data(){
   this.chartSettings={
    dimension:['日期'],
    xAxisType:'time' 
   }
return{
    breaddata: ["统计", "会员分析", "会员统计"],
     value:[],
    pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
      }
    }
  }
 },
 components:{
   Bread,
    membershipGrowth,
    memberSource,
    memberGrade
 },
 mounted(){
      // this.changeTimeType();
      // this.getGrowStatis();

  },
   methods:{
    // 按时，按天，按月切换
      // this.dateObj.endTime=this.timeType[1]
    },
//  //会员增长统计展示
//     getGrowStatis(){
//       var obj={
//         params:{
//           timeType:this.timeType,//时间类型(1:时,2:日,3:月)
//           startDate:this.dateObj.startDate,
//           endDate:this.dateObj.endDate,
//         }
//       }
//       statis(obj).then(res=>{
//         if(res.code==200){
//           this.chartData.rows['人数']=res.data.memberIncreaseNumber;
//         }
//       })
//     },

}
</script>
