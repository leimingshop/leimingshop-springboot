<template>
  <div>
   <div style="width:100%; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
      <span style="margin:27px">流量来源</span>
      <span style="margin-right:30px;">
         <statisticDatePick  @getData='getData'></statisticDatePick>
      </span>
    </div>
    <div v-loading="loading" style="margin-top:30px;">
        <div style="display:flex;justify-content: center;">
            <ve-pie 
                width="500px" 
                height="400px"
                :title="title"
                :tooltip="tooltip"
                :legend="legend"
                :series="series">
            </ve-pie>
            
            <div style="margin-left:10px;border-left: 8px solid whitesmoke;">
                <ve-pie 
                    width="500px" 
                    height="400px"
                    :title="title2"
                    :tooltip="tooltip"
                    :legend="legend"
                    :series="series2">
                </ve-pie>
            </div> 
        </div>

    </div>

  </div>
</template>

<script>
import {pageSource} from "@/api/api";
import statisticDatePick from "@/components/statisticDatePick"

export default {
 data(){
   return{
        title: {
            text: '浏览次数(pv)',
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
                name: '浏览次数(pv)',
                type: 'pie',
                radius: '55%',
                center: ['40%', '50%'],
                data: [
                    // {name:'pc',value:1},
                    // {name:'h5',value:2},
                    // {name:'android',value:3},
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
        title2: {
            text: '独立访客(uv)',
            // subtext: '纯属虚构',
            left: 'center'
        },
        series2: [
            {
                name: '独立访客(uv)',
                type: 'pie',
                radius: '55%',
                center: ['40%', '50%'],
                data: [
                    // {name:'pc',value:1},
                    // {name:'h5',value:2},
                    // {name:'android',value:3},
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


        loading:false,
    }
 },
 components:{
    statisticDatePick
 },
  mounted(){
  },
  methods:{
    getData(objArgu){
        var obj = {
            params:{
                ...objArgu
            }
        }
        this.series[0].data = []
        this.series2[0].data = []
        this.loading = true;
        pageSource(obj).then((res)=>{
            this.loading = false
            if(res.code==200){
                // pc pvPcCount uvWeChatCount
                this.series[0].data.push({name:"PC",value:res.data.pvPcCount})
                this.series2[0].data.push({name:"PC",value:res.data.uvWeChatCount})
                //   h5 pvOtherCount  uvOtherCount
                 this.series[0].data.push({name:"H5",value:res.data.pvOtherCount})
                this.series2[0].data.push({name:"H5",value:res.data.uvOtherCount})
                // android pvAndroidCount uvAndroidCount
                this.series[0].data.push({name:"Android",value:res.data.pvAndroidCount})
                this.series2[0].data.push({name:"Android",value:res.data.uvAndroidCount})
                // ios pvIosCount  uvIosCount
                this.series[0].data.push({name:"IOS",value:res.data.pvIosCount})
                this.series2[0].data.push({name:"IOS",value:res.data.uvIosCount})
                // 微信 pvWeChatCount uvWeChatCount
                this.series[0].data.push({name:"微信",value:res.data.pvWeChatCount})
                this.series2[0].data.push({name:"微信",value:res.data.uvWeChatCount})
                // 小程序  pvAppletsCount uvAppletsCount
                this.series[0].data.push({name:"小程序",value:res.data.pvAppletsCount})
                this.series2[0].data.push({name:"小程序",value:res.data.uvAppletsCount})
            }else{
                this.$message({
                    message:res.msg,
                    type: 'error',
                    duration: 1500,
                })
            }
            console.log(this.series);
            console.log(this.series2);


        })
    },
  },   
    
    
  }

</script>
