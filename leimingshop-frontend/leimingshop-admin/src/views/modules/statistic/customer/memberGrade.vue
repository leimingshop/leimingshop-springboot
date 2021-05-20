<template>
    <div>
        <div  style="width:100%; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
            <span style="margin:27px">会员等级占比</span>
            <span style="margin-right:30px;">
                <statisticTimePick  @getData='getData'></statisticTimePick>
            </span>
        </div>    
        <div v-loading="loading" style="margin-top:40px;">       
        <div v-if="this.chartData4.rows && this.chartData4.rows.length!=0" >
                <ve-pie :data="chartData3" :settings="chartSettings"></ve-pie>

                <ve-line :data="chartData4"
                :events="chartEvents"
                ></ve-line> 
                <!-- :settings="chartSettings1"
                -->
        </div>
        <div  v-else style="font-size: 14px;text-align: center;color: #909399;margin-top:20px">
            暂无数据
        </div>
        </div>
    </div>
</template>
<script>
import { proportion } from "@/api/api";
import statisticTimePick from "@/components/statisticTimePick"
import {type} from 'os';
export default {
    data(){
        this.chartSettings={
            radius:160,
            selectedMode:'single',
            hoverAnimation:false
        }

        //  每次点击折线图，饼形图会变动
        var self=this
        this.chartEvents={
            click:function(e){
                 console.log('-------');
                console.log(e.name);``
                var obj =   self.chartData4.rows.find((item)=>{
                    return e.name==item['日期']
                })
                self.chartData3.rows = []
                var  keys=  Object.keys(obj) 
                var  values=  Object.values(obj) 
                keys.forEach((item,index)=>{
                    if(item!="日期"){
                        self.chartData3.rows.push({
                        "会员等级":item,
                        "会员数量":values[index]
                        })
                    }
                })
                console.log(self.chartData3);
            }
        }
        return{
            loading:false,
            timeType:'1',
            valueTime:[],
            dateObj:{
                startDate:'',
                endDate:'',
            },
            chartData3:{
                columns:['会员等级',"会员数量"],
                rows:[
                    { "会员等级": "钻石会员", "会员数量": 0 }, 
                    { "会员等级": "金牌会员", "会员数量": 0 }, 
                    { "会员等级": "银牌会员", "会员数量": 0 }, 
                    { "会员等级": "中级小白", "会员数量": 0 },
                     { "会员等级": "铜牌会员", "会员数量": 0 }, 
                     { "会员等级": "初级小白", "会员数量": 0 } 
                ]
            },
            chartData4:{
                columns:[],
                rows:[]
            },
            pickerOptions:{
                disabledDate(time){
                    return time.getTime()>Date.now();
                }
            },
            dateObj:{
                startTime:'',
                endTime:'',
            },
            objArgu:"",
        }
    },
    components:{
        statisticTimePick
    },
    methods:{
        //会员等级占比
        // getGradeProportion(){this.getGradeProportionDely()},
    //    getGradeProportion(timeType){
    //           var currentDateStamp = new Date().getTime();
    //             if(timeType==1){
    //                 this.valueTime = [this.formatDate(currentDateStamp-86400000,31),this.formatDate(currentDateStamp,31)]
    //             }else if(timeType==2){
    //                  this.valueTime = [this.formatDate(currentDateStamp-2678400000,31),this.formatDate(currentDateStamp,31)]
                
    //             }else if(timeType==3){
    //                  this.valueTime = [this.formatDate(currentDateStamp-31536000000,1),this.formatDate(currentDateStamp,1)]
    //             }
    //         // setTimeout(()=>{
    //             this.getGradeProportionDely(timeType)
    //         // },0)
    //     },
        getData(objArgu){
            this.objArgu = objArgu
            this.getGradeProportionDely();
        },
        getGradeProportionDely(){
            if(this.dateObj.startDate=='' || this.dateObj.endDate==''){
                this.$message.error("请选择时间");
                return
            }
            var obj={
                params:{
                    timeType:this.objArgu.timeType,
                    // startDate:this.dateObj.startDate,
                    // endDate:this.dateObj.endDate,
                    startDate:this.objArgu.startDate,
                    endDate:this.objArgu.endDate,
                }
            }
            this.loading=true;
            proportion(obj).then(res=>{
                this.loading=false;
                if(res.code==200){
                    var keys  = Object.keys(res.data) //取出所有key
                    var values  = Object.values(res.data)  //取出所有value
                    console.log(keys);
                    console.log(values);
                    //处理折线图数据
                    this.chartData4.columns = [];
                    this.chartData4.rows = [] ; 
                    // this.chartData3.rows=[];
                   this.chartData3.rows= [];
                    keys.forEach((key,index)=>{
                            if(this.objArgu.timeType==1){
                                key=keys[index].substring(11,13)
                                key = key+":00"
                            }else if(this.objArgu.timeType==2){
                                key=keys[index].substring(5,10)
                            }else if(this.objArgu.timeType==3){
                                key=keys[index]
                            }
                        var rowObj = {}
                        values[index].forEach((item2,index2)=>{

                            console.log(item2)

                            this.chartData4.columns.push(item2.memberGraderName);
                            // this.chartData3.columns.push(item2.memberGraderName);
                            rowObj[item2.memberGraderName] = item2.memberNumber
                            // obj2[item2.memberGraderName] = item2.memberNumber
                            // 默认饼形图拿第一列
                            if(index==0){
                                var obj2={}
                                obj2["会员等级"] =item2.memberGraderName
                                obj2["会员数量"] = item2.memberNumber
                                this.chartData3.rows.push({
                                    ...obj2
                                });
                            }
                        })
                        this.chartData4.rows.push({
                            '日期':key,
                            ...rowObj
                        });
                    })
                    this.chartData4.columns = ['日期',...new Set(this.chartData4.columns)]
                    
                }else{
                    this.$message.error(res.msg);
                    this.valueTime = "";
                }
            })
        },

    }
}
</script>
<style lang="scss" scoped>

</style>
