<template>
    <div>
      <el-radio-group v-model="queryType" @change="changeQueryType(queryType)" style="padding-right:20px;margin:18px">
          <el-radio-button label="1">本月</el-radio-button>
          <el-radio-button label="2">上月</el-radio-button>
      </el-radio-group>
      <!-- <el-date-picker
        v-model="value"
        type="daterange"
        placeholder="选择日期时间"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :value-format="'yyyy-MM'"
        :format="'yyyy-MM'"
        :align="align"
        @change="getDataType()"
        :picker-options="pickerOptions"
        > -->
        <!-- :default-time="['','']" -->
        <el-date-picker
          v-model="value"
          type="month"
          placeholder="选择月"
          :value-format="'yyyy-MM'"
          :format="'yyyy-MM'"
         :picker-options="pickerOptions"
          @change="getDataType()">
        </el-date-picker>

      </el-date-picker>
    </div>
</template>
<script>
export default {
    name:'bread',
     data(){
       return{
         queryType:'1',
         value:[],
         pickerOptions: {
              disabledDate(time) {
                return time.getTime() > Date.now();
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
      // 给默认值
       this.getTime();
      //  调用父级别方法请求接口
        this.getData()
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
      // 给默认值
      getTime(){
         var time = new Date(); 
         var y = time.getFullYear();
         var m = time.getMonth()+1;
         this.nowMonth =  y+'-'+m //  本月

         var time2 = new Date(new Date().getTime()-86400000*31); 
         var y2 = time2.getFullYear();
         var m2 = time2.getMonth()+1;
         this.preMonth = y2+'-'+m2 // 上月
         if( this.queryType ==1){
            this.value = this.nowMonth
         }else if(this.queryType ==2){
            this.value = this.preMonth
         }
        // var currentDateStamp = new Date().getTime();
        // if(this.queryType==1){
        //   this.value = [this.formatDate(currentDateStamp-86400000,31),this.formatDate(currentDateStamp-86400000,32)]
        // }else if(this.queryType==2){ 
        //   this.value = [this.formatDate(currentDateStamp-86400000*7,31),this.formatDate(currentDateStamp-86400000,32)]
        // }else if(this.queryType==3){
        //   this.value = [this.formatDate(currentDateStamp-86400000*31,31),this.formatDate(currentDateStamp,32)]
        // }
     },
      getDataType(){
        console.log(this.value);
        if(this.value==this.nowMonth){
            this.queryType = 1  //1:本月,
        }else if(this.value== this.preMonth){
           this.queryType = 2  //2:上月
        }else{
         this.queryType = 3  //3:自定义月份【yyyy-MM-dd】
        }
        this.getData()
      },
      changeQueryType(){
        this.getTime()
        this.getData()
      },
      // 只用来触发父级方法
      getData(){
        var obj = {
            queryType:this.queryType,
            date:this.value,
        }
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
// .el-date-editor.el-input, .el-date-editor.el-input__inner {
//   width: 380px !important;
// }
/deep/ .el-popper{
  left: 1700px !important;

}
</style>

