<template>
  <div >
        <div class="success">
            操作成功
        </div>
        <div class="nextStepWrap">
          <el-button class="nextStep1" @click="preStep()" >返回</el-button>

      </div>
  </div>
</template>

<script>
export default {
    data() {
    return {
    }
    },
    created () {
    },
    methods: {
      preStep(){
        //this.$emit("showPageNumberFn",1);
        this.$router.push({ 
          path: 'mggoods-list',
        })
      },

    }
}
</script>
<style lang="scss" scoped>
@import "@/element-ui/theme-variables.scss";
// .el-image{
//    text-align: center;
//     margin-top: 30px;
//    img{
//         width: 500px;
//         height: 375px;
//    }
// }
.nextStepWrap{
    margin-top:0px;
   text-align: center;
   .nextStep2{
     margin-left: 50px;
   }
}

.success{
    height: 300px;
    line-height: 300px;
    text-align: center;
    font-size: 28px;
     color: $--color-primary;
}

</style>
