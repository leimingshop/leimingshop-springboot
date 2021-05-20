<template>
  <div>
    <div style="width:100%; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
      <span style="margin:27px">订单来源</span>
      <span style="margin-right:30px;">
        <statisticMonthPick  @getData='getData'></statisticMonthPick>
      </span>
    </div>
    <div v-loading="loading">
        <div style="display:flex;justify-content: center;">
            <ve-pie 
                width="900px" 
                height="400px"
                :tooltip="tooltip"
                :legend="legend"
                :toolbox="toolbox"
                :series="series">
            </ve-pie>
        </div>
        <div style="margin-bottom:80px; margin-top:30px;">
                <el-table
                    :data="dataList"
                    border
                    style="width: 100%">
                    <el-table-column
                        prop="className"
                        label="平台"
                        align="center">
                        <template slot-scope="scope">
                            <div>
                            <span v-if="scope.row.orderSource==0">PC</span>
                            <span v-if="scope.row.orderSource==1">h5</span>
                            <span v-if="scope.row.orderSource==2">android</span>
                            <span v-if="scope.row.orderSource==3">ios</span>
                            <span v-if="scope.row.orderSource==4">微信</span>
                            <span v-if="scope.row.orderSource==5">小程序</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="currentBuyerCount"
                        label="付款人数"
                        align="center">
                    </el-table-column>

                    <el-table-column
                        prop="lastMonthBuyerCount"
                        label="较前一月"
                        align="center">
                    </el-table-column>

                    <el-table-column
                        prop="buyerUptrend"
                        label="增幅/涨幅"
                        align="center">
                        <template  slot-scope="scope">
                            <div v-if="scope.row.lastMonthBuyerCount>0">
                                {{scope.row.buyerUptrend*100}}%
                            </div>
                            <div v-if="scope.row.lastMonthBuyerCount==0">
                                 {{"-"}}
                            </div>
                        </template>
                    </el-table-column>

                    <el-table-column
                        prop="currentOrderAmount"
                        label="付款金额"
                        align="center">
                    </el-table-column>

                    <el-table-column
                        prop="lastMonthOrderAmount"
                        label="较前一月"
                        align="center">
                    </el-table-column>

                    <el-table-column
                        prop="orderAmountUptrend"
                        label="增幅/降幅"
                        align="center">
                        <template slot-scope="scope">
                            <div v-if="scope.row.lastMonthOrderAmount>0">
                                {{scope.row.orderAmountUptrend*100}}%
                            </div>
                            <div v-if="scope.row.lastMonthOrderAmount==0">
                                 {{"-"}}
                            </div>
                        </template>
                    </el-table-column>

                </el-table>
            </div>
    </div>
  </div>
</template>
<script>
import { staticOrderSource } from "@/api/api";
import statisticMonthPick from "@/components/statisticMonthPick"
export default {
    data(){
        this.chartExtend={
            barWidth:40
        }
        return{
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                left: 'center',
                top: 'bottom',
                data: ['APP','微信端','PC端','小程序','其他']
            },
            toolbox: {
                show: true,
                top: 'top',
                feature: {
                    mark: {show: true},
                    // dataView: {show: true, readOnly: false},
                    magicType: {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            series: [
                {
                    name: '付款人数',
                    type: 'pie',
                    radius: [20, 110],
                    center: ['25%', '50%'],
                    roseType:false,
                    label: {
                        show: true
                    },
                    emphasis: {
                        label: {
                            show: true
                        }
                    },
                    data: [
                        {value: 0 ,name: 'APP'},
                        {value: 0, name: '微信端'},
                        {value: 0, name: 'PC端'},
                        {value: 0, name: '小程序'},
                        {value: 0, name: '其他'},

                    ]
                },
                {
                    name: '付款金额',
                    type: 'pie',
                    radius: [20, 110],
                    center: ['75%', '50%'],
                    roseType: false,
                    data: [
                    // {value: '' ,name: ''},
                    {value: 0 ,name: 'APP'},
                    {value: 0, name: '微信端'},
                    {value: 0, name: 'PC端'},
                    {value: 0, name: '小程序'},
                    {value: 0, name: '其他'},
                    
                    ]
                }
            ],
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
            loading:false,
            dataList:[],
      }
    },
    components:{
      statisticMonthPick
    },
    mounted(){
    },
    methods:{
      getData(obj){
        this.getStaticOrderSource(obj);
      },
      //  获取订单来源数据
      getStaticOrderSource(objArgu){
          let that = this;
        let obj={
          params:objArgu
        }
        that.loading= true;
        staticOrderSource(obj).then((res)=>{
          that.loading= false;
            console.log(res);
            var data = res.data
            that.series[0].data = [];
            that.series[1].data = [];
            that.legend.data = []
            that.dataList= [];
            Object.keys(data).forEach(function(key,index){
                console.log(key,data[key]);
                // 表格数据
                that.dataList.push(data[key]);
                if(data[key].orderSource==0){   //  0:pc,
                    that.legend.data.push("PC");
                    that.series[0].data.push({"name":"PC","value":data[key].currentBuyerCount})
                    that.series[1].data.push({"name":"PC","value":data[key].currentOrderAmount})
                }else if(data[key].orderSource==1){  // ,1:h5
                    that.legend.data.push("H5");
                    that.series[0].data.push({"name":"H5","value":data[key].currentBuyerCount})
                    that.series[1].data.push({"name":"H5","value":data[key].currentOrderAmount})
                }else if(data[key].orderSource==2){  // 2:android
                    that.legend.data.push("android");
                     that.series[0].data.push({"name":"android","value":data[key].currentBuyerCount})
                    that.series[1].data.push({"name":"android","value":data[key].currentOrderAmount})
                }else if(data[key].orderSource==3){ //3:ios,
                     that.legend.data.push("ios");
                    that.series[0].data.push({"name":"ios","value":data[key].currentBuyerCount})
                    that.series[1].data.push({"name":"ios","value":data[key].currentOrderAmount})
                }else if(data[key].orderSource==4){ //4:微信
                     that.legend.data.push("微信");
                     that.series[0].data.push({"name":"微信","value":data[key].currentBuyerCount})
                    that.series[1].data.push({"name":"微信","value":data[key].currentOrderAmount})
                }else if(data[key].orderSource==5){  // 5:小程序
                     that.legend.data.push("小程序");
                    that.series[0].data.push({"name":"小程序","value":data[key].currentBuyerCount})
                    that.series[1].data.push({"name":"小程序","value":data[key].currentOrderAmount})
                }

            });
        })
      },
    }
}
</script>
