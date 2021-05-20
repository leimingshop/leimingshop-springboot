<template>
  <div>
   <div style="width:100%; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
      <span style="margin:27px">会员来源</span>
      <span style="margin-right:30px;">
         <statisticTimePick  @getData='getData'></statisticTimePick>
      </span>
    </div>
     <div style="display:flex;justify-content: center;">
        <ve-pie 
            width="500px" 
            height="400px"
            :title="title"
            :tooltip="tooltip"
            :legend="legend"
            :series="series"
            >
        </ve-pie>
    </div>
    <div v-loading="loading" style="margin-top:30px;">
        <div style="display:flex;justify-content: center;">
            <ve-line 
                width="100%" 
                height="450px"
                :data="chartData"
                :extend="extend"
                :events="chartEvents"
                >
            </ve-line>
        </div>

    </div>

  </div>
</template>

<script>
import {statisMemberSource} from "@/api/api";
import statisticTimePick from "@/components/statisticTimePick"

export default {
 data(){
     this.extend={
      series:{
        smooth:false
      }
    }
    var self = this
    this.chartEvents = {
        click: function (e) {
            self.handleCakeShapeData(e);
        }
    }
   return{
        chartData:{
            columns:['时间','PC','H5',"Android","IOS",'微信','小程序'],
            rows:[
                 { '时间': '1/1', 'PC': 1,'H5': 4,'Android': 1,'IOS': 1,'微信': 1,'小程序': 1},
                //  { '时间': '1/2', 'PC': 1,'H5': 2,'Android': 1,'IOS': 1,'微信': 1,'小程序': 1},
                //  { '时间': '1/3', 'PC': 1,'H5': 4,'Android': 1,'IOS': 1,'微信': 1,'小程序': 1},

            ]
        },

        title: {
            text: '会员来源各端口占比',
            // subtext: '纯属虚构',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            type: 'scroll',
            orient: 'vertical',
            right: 10,
            top: "center",
        },
        series: [
            {
                name: '会员来源占比',
                type: 'pie',
                radius: '55%',
                center: ['40%', '50%'],
                data: [
                    {name:'PC',value:0},
                    {name:'H5',value:0},
                    {name:'Android',value:0},
                    {name:'IOS',value:0},
                    {name:'微信',value:0},
                    {name:'小程序',value:0},
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ],
        timeType:1, //时间类型(1:时,2:日,3:月)


        loading:false,
    }
 },
 components:{
    statisticTimePick
 },
  mounted(){
  },
  methods:{
    getData(objArgu){
        this.timeType = objArgu.timeType
        var obj = {
            params:{
                ...objArgu
            }
        }
        this.loading = true;
        statisMemberSource(obj).then((res)=>{
            this.loading = false
            if(res.code==200){
                this.handleFoldLineData(res.data);
            }else{
                // this.$message.error(res.msg);
            }
        })
    },
    // 处理折线数据
    handleFoldLineData(data){
            this.chartData.rows = [];
            data.forEach((item,index)=>{
                let time =""
                if(this.timeType==1){
                    time = item.createHourTime.substring(11,13)+":00"
                }else if(this.timeType==2){
                    time = item.createDayTime.substring(5,10);
                }else{
                    time = item.createMonthTime.substring(0,7)
                }
                this.chartData.rows.push(
                    { 
                        '时间':time,
                        'PC': item.pcIncreaseNumber,
                        'H5': item.otherIncreaseNumber,
                        'Android': item.androidIncreaseNumber,
                        'IOS': item.iosIncreaseNumber,
                        '微信': item.wechatIncreaseNumber,
                        '小程序': item.appletsIncreaseNumber,
                    },
                )
                // 默认饼图图展示第第一列数据
                if(index==0){
                    this.handleCakeShapeData({name:time});
                }

            })
    },
    // 处理饼形图数据
    handleCakeShapeData(e){
        // console.log("处理饼形图数据")
        let obj =  this.chartData.rows.find((item)=>{
            return e.name==item['时间']
        })
        this.series[0].data = [
             {name:'PC',value:0},
             {name:'H5',value:0},
             {name:'Android',value:0},
             {name:'IOS',value:0},
             {name:'微信',value:0},
             {name:'小程序',value:0},
        ]
        console.log(obj);
        this.series[0].data=[
            {name:'PC',value:obj['PC']},
            {name:'H5',value:obj['H5']},
            {name:'Android',value:obj['Android']},
            {name:'IOS',value:obj['IOS']},
            {name:'微信',value:obj['微信']},
            {name:'小程序',value:obj['小程序']},
       ]
    }

  },   
    
    
  }

</script>
