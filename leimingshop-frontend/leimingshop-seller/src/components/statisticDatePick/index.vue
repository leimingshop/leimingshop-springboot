<template>
    <div>
      <el-radio-group v-model="queryType" @change="changeQueryType(queryType)" style="padding-right:20px;margin:18px">
          <el-radio-button label="1">昨天</el-radio-button>
          <el-radio-button label="2">最近7天</el-radio-button>
          <el-radio-button label="3">最近30天</el-radio-button>
      </el-radio-group>
      <el-date-picker
        v-model="value"
        type="daterange"
        placeholder="选择日期时间"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :value-format="'yyyy-MM-dd'"
        :format="'yyyy-MM-dd'"
        :align="align"
        @change="getDataType()"
        :picker-options="pickerOptions"
        >
        <!-- :default-time="['','']" -->

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
                return (time.getTime() > Date.now()) || (time.getTime()<(Date.now()-365*2*24*60*60*1000));
                // return true;
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
    activated () {
      // 给默认值
      this.queryType='1'
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
        var currentDateStamp = new Date().getTime();
        if(this.queryType==1){
          this.value = [this.formatDate(currentDateStamp-86400000,31),this.formatDate(currentDateStamp-86400000,32)]
        }else if(this.queryType==2){ 
          this.value = [this.formatDate(currentDateStamp-86400000*7,31),this.formatDate(currentDateStamp-86400000,32)]
        }else if(this.queryType==3){
          this.value = [this.formatDate(currentDateStamp-86400000*31,31),this.formatDate(currentDateStamp,32)]
        }
     },
      getDataType(){
        this.queryType = 4
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
            startDate:this.value[0],
            endDate:this.value[1],
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
.el-date-editor.el-input, .el-date-editor.el-input__inner {
  width: 380px !important;
}
/deep/ .el-popper{
  left: 1700px !important;

}
</style>

