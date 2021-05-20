<template>
    <div>
      <el-radio-group v-model="timeType" @change="changeTimeType(timeType)" style="padding-right:20px">
          <el-radio-button label="1" ref="pageDefault">按时</el-radio-button>
          <el-radio-button label="2">按天</el-radio-button>
          <el-radio-button label="3">按月</el-radio-button>
      </el-radio-group>
      <el-date-picker v-if="timeType != 3"
          style="width: 350px;"
          v-model="value"
          type="datetimerange"
          :default-time="['00:00:00','23:59:59']"
          placeholder="选择日期时间"
          range-separator="-"
          :value-format="'yyyy-MM-dd HH:mm:ss'"
          :format="'yyyy-MM-dd HH:mm:ss'"
          start-placeholde="开始日期"
          end-placeholde="结束日期"
          :align="align"
          :picker-options="pickerOptions"
          @change="getData(timeType)"
        >
      </el-date-picker>
          <!-- :value-format="timeType==1 ? 'yyyy-MM-dd HH' : timeType==2 ? 'yyyy-MM-dd' : 'yyyy-MM'" -->
          <!-- :format="timeType==1 ? 'yyyy-MM-dd HH' : timeType==2 ? 'yyyy-MM-dd' : 'yyyy-MM'" -->
      <el-date-picker v-else
        v-model="value"
        type="monthrange"
        placeholder="选择日期时间"
        range-separator="-"
        :value-format="'yyyy-MM'"
        :format="'yyyy-MM'"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :align="align"
        :picker-options="pickerOptions"
        @change="getData(timeType)"
          >
    </el-date-picker>
    </div>
</template>
<script>
export default {
    name:'bread',
     data(){
       return{
         timeType:1,
         value:[],
         pickerOptions: {
              disabledDate(time) {
                return (time.getTime() > Date.now()) || (time.getTime()<(Date.now()-365*2*24*60*60*1000));
                 // return time.getTime() > Date.now() - 8.64e7;
              }
          },

       }
     },
    props: {
      align: {
        type: String,
        default:"right"
      },
    },
    created () {

        this.changeTimeType()
    },
    methods: {
      formatDate(shijianchuo,type=1) {

          //时间戳是整数，否则要parseInt转换
          var time = new Date(shijianchuo); // 需要使用Date格式进行日期转化，若是时间戳、字符串时间，需要通过new Date(..)转化

          var y = time.getFullYear();

          var m = time.getMonth()+1;

          var d = time.getDate();

          var h = time.getHours();

          var mm = time.getMinutes();

          var s = time.getSeconds();

          if(type==1){
            return y+'-'+'01'
          }else if(type==21){
            return y+'-'+m+'-'+'01'
          }else if(type==31){
            return y+'-'+m+'-'+d+' '+'00:'+'00:'+'00'
          }else if(type==32){
            return y+'-'+m+'-'+d+' '+'23:'+'59:'+'59'
          }else{
            return y+'-'+m+'-'+d+' '+h+':'+m+':'+s;
          }
      },
      changeTimeType(){
        this.getTime()
        this.getData()
      },
      getTime(){
        var currentDateStamp = new Date().getTime();
        if(this.timeType==1){
          this.value = [this.formatDate(currentDateStamp-86400000,31),this.formatDate(currentDateStamp-86400000,32)]
        }else if(this.timeType==2){ 
          this.value = [this.formatDate(currentDateStamp-2678400000,31),this.formatDate(currentDateStamp-86400000,32)]
        }else if(this.timeType==3){
          this.value = [this.formatDate(currentDateStamp-31536000000+2505600000,31),this.formatDate(currentDateStamp,32)]
        }
     },
     getData(){
       var obj = {
          timeType:this.timeType,
          startDate:this.value[0],
          endDate:this.value[1],
       }
        console.log("getDatagetDatagetDatagetData");
       console.log(obj);
      this.$emit("getData",obj)
     }
    }
}
</script>
<style lang="scss" scoped>
.canClick{
  cursor: pointer;
  color: #2260D2;
}
.el-date-editor.el-input, .el-date-editor.el-input__inner {
  width: 380px !important;
}
/deep/ .el-popper{
  left: 1700px !important;

}
</style>

