<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <div class="formControlArea">
            <div></div>
            <div style="display: flex">
                <mainTipsMessage></mainTipsMessage>
            </div>
        </div>
        <el-alert
            title="操作提示"
            type="warning"
            @close="$store.commit('showListMessage')"
            v-show="$store.state.listMessageFlag"
        >
            <template slot="title">
                <div class="iconSize">操作提示：</div>
                <div class="iconSize">
                    1、交易数据统计图展示了指定时间段内交易维度的数据统计
                </div>
                <div class="iconSize">
                    2、近七天交易数据为昨天之前7日内的付款金额、退款金额、下单转化率等数据的趋势变化
                </div>
                <div class="iconSize">
                    3、人均消费区间对比数据统计图展示了指定时间段内各交易区间的人数占比
                </div>
                <div class="iconSize">
                    4、订单来源数据统计图展示了指定时间段内下单的来源
                </div>
            </template>
        </el-alert>

        <transactionOrder></transactionOrder>
        <sevenDaySatis></sevenDaySatis>
        <consumerOrder></consumerOrder>
        <orderSouce></orderSouce>

        <!-- <div style="width:98%; float:flex; height:50px;justify-content: space-between;display: flex;">
           <span >交易数据</span>
           <span>
              <el-radio-group v-model="queryType" @change="queryData(queryType)" style="padding-right:20px">
                <el-radio-button label="1">昨天</el-radio-button>
                <el-radio-button label="2">最近7天</el-radio-button>
                <el-radio-button label="3">最近30天</el-radio-button>
            </el-radio-group>
        
            <el-date-picker
              v-model="valueTime"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @blur="getTime()"
              >
            </el-date-picker>
           </span>
        </div> -->
        <!-- <div class="tradeDatawarp">
          <div class="tradeDataLeft" style=" ">
            <div class="tradeDataTableItem" v-for="(item,index) in  leftTableData" :key="index" style="    ">
              <div  class="tradeDataTableItemTop" style="">{{item.title}}</div>
              <div class="tradeDataTableItemBottom" style="    ">{{item.value}}</div>
            </div>
          </div>
         <div  class="tradeDataRight" >
             <ve-funnel :data="chartData1" ></ve-funnel>
         </div>
         <div style="clear:both"></div>
       </div>-->
        <!-- <span class="">近七日交易数据</span>
        <div style="width:100%; height:400px; display:flex;">
          <div style="width:80%">
            <ve-line :data="chartData2" :settings="chartSettings2"></ve-line> 
          </div>

          <div style="width:20%">
            <ul class="sevenDayDataRight" style="margin:0 auto;">
              <li  class="sevenDayDataRightItem"  v-for="(item,index) in chartData2.controlColumns" v-if="index!=0">
                <span class="sevenDayDataItemName">{{item.name}}</span>
                <span class="sevenDayDataItemBt"  @click="hiddenOneAttr(index)" style="">{{item.show?"点击隐藏":"点击展示"}}</span>
              </li>
            </ul> 
          </div>
          
        </div> -->

        <!-- <div style="width:98%; float:flex; height:50px;justify-content: space-between;display: flex;">
           <span >人均消费区间对比</span>
           <span>
            <el-radio-group v-model="queryType" @change="getSingleConsumer(queryType)" style="padding-right:20px">
              <el-radio-button label="1" >昨天</el-radio-button>
              <el-radio-button label="2" >最近7天</el-radio-button>
              <el-radio-button label="3">最近30天</el-radio-button>
            </el-radio-group>
            
            <el-date-picker
              v-model="valueTime"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @blur="getTime()">
            </el-date-picker>
           </span>
        </div>
        <ve-histogram :data="chartData3"></ve-histogram> -->
    </div>
</template>

<script>
    //import { transaction,seven,single } from "@/api/api";
    import Bread from "@/components/bread";
    import transactionOrder from "./transactionOrder";
    import sevenDaySatis from "./sevenDaySatis";
    import consumerOrder from "./consumerOrder";
    import orderSouce from "./orderSouce";
    export default {
        data() {
            // this.chartSettings = {
            //     dataType: 'percent'
            //   }
            // this.chartSettings2={
            //   axisSite:{right:['下单率']},
            //   yAxisType:['KMB','percent'],
            //   yAxisName:['数值','比率']
            // }
            return {
                // leftTableData:[
                //   {title:"浏览量",value:""},
                //   {title:"访客数",value:""},
                //   {title:"下单人数",value:""},
                //   {title:"订单数",value:""},
                //   {title:"下单件数",value:""},
                //   {title:"下单金额",value:""},

                //   {title:"退款金额",value:""},
                //   {title:"付款人数",value:""},
                //   {title:"付款订单数",value:""},
                //   {title:"付款件数",value:""},
                //   {title:"付款金额",value:""},
                //   {title:"客单价",value:""},
                // ],
                // chartData1: {
                //   columns: ['状态', '数值'],
                //     rows: [
                //       { '状态': '展示', '数值': "" },
                //       { '状态': '访问', '数值': "" },
                //       { '状态': '点击', '数值': "" }
                //     ]
                // },

                breaddata: ["统计", "交易分析", "交易统计"],
                queryType: "1",
                valueTime: "",
                range: [],
                dateObj: {
                    startTime: "",
                    endTime: "",
                    getTableData: "transaction",
                },

                // chartData2:{
                //   columns:['日期','付款金额','退款金额','下单转化率','付款转化率','成交转化率'],
                //   rows:[
                //     { '日期': '', '付款金额': "" , '退款金额': "", '下单转化率':"", '付款转化率':"", '成交转化率':""},
                //     { '日期': '', '付款金额': "", '退款金额': "", '下单转化率':"", '付款转化率':"", '成交转化率':""},
                //     { '日期': '', '付款金额': "", '退款金额': "", '下单转化率':"", '付款转化率':"", '成交转化率':""},
                //     { '日期': '', '付款金额': "", '退款金额': "", '下单转化率':"", '付款转化率':"", '成交转化率':""},
                //     { '日期': '', '付款金额': "", '退款金额': "", '下单转化率':"", '付款转化率':"", '成交转化率':""},
                //     { '日期': '', '付款金额': "", '退款金额': "", '下单转化率':"", '付款转化率':"", '成交转化率':""},
                //     { '日期': '', '付款金额': "", '退款金额': "", '下单转化率':"", '付款转化率':"", '成交转化率':""},
                //   ],
                //   controlColumns:[
                //     {name:"日期",show:true},
                //     {name:"付款金额",show:true},
                //     {name:"退款金额",show:true},
                //     {name:"下单转化率",show:true},
                //     {name:"付款转化率",show:true},
                //     {name:"成交转化率",show:true}
                //   ]
                // },
                // chartData3:{
                //   columns:['消费区间','人数'],
                //   rows:[
                //     {'消费区间':'0-50','人数':"0"},
                //     {'消费区间':'50-100','人数':"0"},
                //     {'消费区间':'100-200','人数':"0"},
                //     {'消费区间':'200-500','人数':"0"},
                //     {'消费区间':'500-1000','人数':"0"},
                //     {'消费区间':'1000-2000','人数':"0"},
                //     {'消费区间':'2000-5000','人数':"0"},
                //     {'消费区间':'5000-10000','人数':"0"},
                //     {'消费区间':'10000以上','人数':"0"},

                //   ],
                // },
            };
        },
        mounted() {
            // this.queryData(1);
            // // 获取数据
            // this.getSevenDayStatis();
            // this.getSingleConsumer(1);
        },
        components: {
            Bread,
            consumerOrder,
            orderSouce,
            sevenDaySatis,
            transactionOrder,
        },
        methods: {
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
            getTime() {
                console.log(this.valueTime);
                this.dateObj.startTime = this.valueTime[0];
                this.dateObj.endTime = this.valueTime[1];
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
            getSingleConsumer(query) {
                var obj = {
                    params: {
                        queryType: query,
                        startDate: this.dateObj.startDate,
                        endDate: this.dateObj.endDate,
                    },
                };
                single(obj).then((res) => {
                    if (res.code == 200) {
                        //柱形图的数据
                    }
                });
            },
        },
    };
</script>
<style lang="scss"  scoped>
    // 交易数据
    .tradeDatawarp {
        width: 100%;
        height: 450px;
        // display:flex;
        .tradeDataLeft {
            float: left;
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            width: 982px;
            height: 198px;
            border-bottom: 1px solid #bebcbc;
            border-right: 1px solid #bebcbc;
            .tradeDataTableItem {
                display: flex;
                justify-content: space-between;
                flex-direction: column;
                .tradeDataTableItemTop {
                    width: 163px;
                    height: 50px;
                    line-height: 50px;
                    text-align: center;
                    border-top: 1px solid #bebcbc;
                    border-left: 1px solid #bebcbc;
                    background: #f3f3f3;
                }
                .tradeDataTableItemBottom {
                    width: 163px;
                    height: 50px;
                    line-height: 50px;
                    text-align: center;
                    border-top: 1px solid #bebcbc;
                    border-left: 1px solid #bebcbc;
                }
            }
        }
        .tradeDataRight {
            width: 350px;
            float: left;
        }
    }
    .sevenDayDataRight {
        .sevenDayDataRightItem {
            .sevenDayDataItemName {
                display: inline-block;
                width: 80px;
                height: 30px;
            }
            .sevenDayDataItemBt {
                margin-left: 20px;
                cursor: pointer;
            }
        }
    }
</style>