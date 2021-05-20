<template>
  <div>
    <div style="width:100%; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
      <span style="margin:27px">人均消费区间对比</span>
      <span style="margin-right:30px;">
        <statisticDatePick  @getData='getData'></statisticDatePick>
      </span>
    </div>
    <ve-histogram :data="chartData3" :extend="chartExtend"></ve-histogram>  
  </div>
</template>
<script>
import { single } from "@/api/api";
import statisticDatePick from "@/components/statisticDatePick"
export default {
    data(){
      this.chartExtend={
        barWidth:40
      }
      return{
        queryType:'1',
        valueTime:'',
        range:[],
        dateObj:{
          startDate:'',
          endDate:'',
        },
        pickerOptions:{
          disabledDate(time){
            return time.getTime()>Date.now();
          }
        },
        chartData3:{
          columns:['消费区间','人数'],
          rows:[],
        },
      }
    },
    components:{
      statisticDatePick
    },
    mounted(){
      this.getData();
    },
    methods:{
      getData(objArgu){
        this.objArgu = objArgu
        this.getSingleConsumer();
      },
      getSingleConsumer(){
        let obj={
          params:{
            queryType:this.objArgu.queryType,
            startDate:this.objArgu.startDate,
            endDate:this.objArgu.endDate,
          }
        }
        single(obj).then(res=>{
          if(res.code==200){
            this.chartData3.rows=[];
            var keys=Object.keys(res.data)
            var values=Object.values(res.data)
            var rows =[];
            values.forEach((item,index) => {
              var pillar = {};
              pillar["消费区间"]=keys[index]
              pillar["人数"]=values[index]
              rows.push(pillar);
              
            })
            // //柱形图的数据
            this.chartData3.rows=rows
            console.log(this.chartData3);
        }
        })
      },
    }
}
</script>
