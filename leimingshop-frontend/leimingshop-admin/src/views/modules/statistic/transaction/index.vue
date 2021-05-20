<template>
  <div>   
    <Bread :breaddata="breaddata"></Bread>
    <transactionOrder></transactionOrder>
    <sevenDaySatis></sevenDaySatis>
    <consumerOrder></consumerOrder>
    <orderSouce></orderSouce>
  </div>
</template>

<script>
//import { transaction,seven,single } from "@/api/api";
import Bread from "@/components/bread";
import transactionOrder from "./transactionOrder";
import sevenDaySatis from "./sevenDaySatis";
import consumerOrder  from "./consumerOrder";
import orderSouce from "./orderSouce"
export default {
  data(){
    return{
      breaddata: ["统计", "交易分析", "交易统计"],
      queryType:'1',
      valueTime:'',
       range:[],
      dateObj:{
          startTime:'',
          endTime:'',
          getTableData:'transaction',
        
       },

    }
  },
    mounted(){
        
    },
      components:{
          Bread,
          consumerOrder,
          orderSouce,
          sevenDaySatis,
          transactionOrder,  
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
      getTime(){
        console.log(this.valueTime)
        this.dateObj.startTime=this.valueTime[0]
        this.dateObj.endTime=this.valueTime[1]
      },

      // queryData(query){
      
      //   this.getDatalist(query)
      // },
      // getDatalist(query){
      //   var obj = {
      //     params:{
      //       queryType:query,
      //       startTime:this.dateObj.startTime,
      //       endTime:this.dateObj.endTime,
      //     }
      //   }
      //    transaction(obj).then(res => {
      //     if (res.code == 200) {

      //       // 表格数据
      //       this.leftTableData[0].value = res.data.pageView ?  res.data.pageView : 0;//浏览量
      //       this.leftTableData[1].value = res.data.visitorsNumber ;//访客数
      //       this.leftTableData[2].value = res.data.submitOrderNumber ;//下单人数
      //       this.leftTableData[3].value = res.data.orderNumber ;//订单数
      //       this.leftTableData[4].value = res.data.orderGoodsNumber ;//下单件数
      //       this.leftTableData[5].value = res.data.submitOrderAmount ;//下单金额
      //       this.leftTableData[6].value = res.data.refundAmount ;//退款金额
      //       this.leftTableData[7].value = res.data.paymentNumber ;//付款人数
      //       this.leftTableData[8].value = res.data.paymentOrderNumber ;//付款订单数
      //       this.leftTableData[9].value = res.data.paymentGoodsNumber ;//付款件数
      //       this.leftTableData[10].value = res.data.paymentAmount ;//付款金额
      //       this.leftTableData[11].value = res.data.perTicketSales ?  res.data.perTicketSales : 0;//客单价

      //       // 右边锥形图表数据
      //       //  this.tableData   = res.data;
      //        this.chartData.rows[0]["数值"] = res.data.paymentConversion ;//;///"123.1"//
      //        this.chartData.rows[1]["数值"] = res.data.submitConversion ;// res.data.submitConversion ;// "123.1" //
      //        this.chartData.rows[2]["数值"] =  res.data.closingConversion ;// res.data.closingConversion ;// "123.1"//
              
      //       //
      //     }
      //   });
      // },
      //定义参数
      // getSevenDayStatis(){
      //   seven().then(res=>{
      //     if(res.code==200){
      //       this.chartData2.rows["日期"]=res.data.createDayTime;
         
      //     }
      //   })
      // },
      getSingleConsumer(query){
        var obj={
          params:{
            queryType:query,
            startDate:this.dateObj.startDate,
            endDate:this.dateObj.endDate,
          }
        }
        single(obj).then(res=>{
          if(res.code==200){

            //柱形图的数据
            }
        })
      },
    }
};

</script>
<style lang="scss"  scoped>
// 交易数据
  .tradeDatawarp{
      width:100%; 
      height:450px; 
      // display:flex;
    .tradeDataLeft{
        float: left;
        display:flex; 
        justify-content:space-between;
        flex-wrap: wrap;
        width: 982px;
        height: 198px;
        border-bottom: 1px solid #bebcbc;
        border-right: 1px solid #bebcbc;
      .tradeDataTableItem{
        display: flex;
        justify-content: space-between;
        flex-direction: column;
        .tradeDataTableItemTop{
          width: 163px;
         height: 50px;
         line-height:50px; 
         text-align: center;
         border-top: 1px solid #bebcbc;
         border-left: 1px solid #bebcbc;
         background: #f3f3f3;
        }
        .tradeDataTableItemBottom{
          width: 163px;
          height: 50px;
          line-height:50px;
          text-align: center;
          border-top: 1px solid #bebcbc;
          border-left: 1px solid #bebcbc;
        }
      }
    }
    .tradeDataRight{
      width:350px;
      float:left;
    }
  }
  .sevenDayDataRight{
      .sevenDayDataRightItem{
        .sevenDayDataItemName{
          display: inline-block;
          width:80px;
          height: 30px;
        }
        .sevenDayDataItemBt{
          margin-left:20px; 
          cursor: pointer;
        }
        
        
      }
  }
</style>