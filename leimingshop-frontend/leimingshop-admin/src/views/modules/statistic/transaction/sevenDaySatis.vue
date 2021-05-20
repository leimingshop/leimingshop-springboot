<template>
    <div>
     <span class="">近七日交易数据</span>
        <div style="width:100%; height:400px; display:flex; justify-content:space-between;">
          <div style="width:100%">
                <ve-line :data="chartData2" :extend="extend2" :settings="chartSettings"></ve-line>
          </div>

          <!-- <div style="width:18%">
            <ul class="sevenDayDataRight" style="margin:60px auto; color:green;">
              <li  class="sevenDayDataRightItem"  v-for="(item,index) in chartData2.controlColumns" v-if="index!=0" style="margin:30px;">
                <span class="sevenDayDataItemName" style="color:black; margin-right:6px">{{item.name}}</span>
                <span class="sevenDayDataItemBt"  @click="hiddenOneAttr(index)" style="">{{item.show?"点击隐藏":"点击展示"}}</span>
              </li>
            </ul> 
          </div> -->
          
        </div>   
    </div>
</template>
<script>
import { seven } from "@/api/api";
export default {
    data(){
    this.extend2={
      series:{
        smooth:false
      }
    }
     this.chartSettings={
      'axisSite':{right:['下单转化率','付款转化率','成交转化率']},
      'yAxisType':['KMB','percent'],
      'yAxisName':['数值','比率']
    };
     return{
        chartData2:{
            columns:['日期','付款金额','退款金额','下单转化率','付款转化率','成交转化率'],
            rows:[
                 
            ],
            controlColumns:[
              {name:"日期",show:true},
              {name:"付款金额",show:true},
              {name:"退款金额",show:true},
              {name:"下单转化率",show:true},
              {name:"付款转化率",show:true},
              {name:"成交转化率",show:true}
            ]
          },
        }
    },
     mounted(){
        this.getSevenDayStatis();
    },
    methods:{
      // 隐藏或展示属性
      // hiddenOneAttr(num){
      //   this.chartData2.controlColumns[num].show = !this.chartData2.controlColumns[num].show;
      //   this.chartData2.columns=[]
      //   this.chartData2.controlColumns.forEach((item,index)=>{
      //         if(item.show){
      //              this.chartData2.columns.push(item.name)
      //         }
      //   })
      //   console.log(this.chartData2);
      // },
       getSevenDayStatis(){

        //
        seven().then(res=>{
          if(res.code==200){
            this.chartData2.rows=[];
            var keys=Object.keys(res.data)
            var values=Object.values(res.data)
            values.forEach((item,index)=>{
            // this.chartData2.rows["日期"]=res.data.createDayTime;
              item['日期']=keys[index]  
              item["付款金额"]=item.paymentAmount;//paymentAmount (number, optional): 付款金额 ,
              item["退款金额"]=item.refundAmount;//refundAmount (number, optional): 退款金额 ,
              item["下单转化率"]=item.submitConversion;//submitConversion (number, optional): 下单转换率 ,
              item["付款转化率"]=item.paymentConversion;//paymentConversion (number, optional): 付款转化率 ,
              item["成交转化率"]=item.closingConversion;//closingConversion (number, optional): 成交转化率 ,
           })
            this.chartData2.rows = values
          }else{
            this.$message({
                message:res.msg,
                type: 'error',
                duration: 1500,
            })
         }
        })
      },
    }
}
</script>
<style lang="scss" scoped>

/deep/.sevenDayDataRightItemName{
  color: black !important;
  margin-left:10px;
}
</style>