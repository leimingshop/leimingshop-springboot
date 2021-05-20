<template>
  <!--确认收货二次确认框-->
  <div class="mask-wrapper">
    <div class="mask-content">
      <P class="p1" @click="handleConfirmEnd">x</P>
      <p class="p2">{{promptTitle}}</p>
      <div class="button-group">
        <div class="button-left" @click="handleConfirmEnd">取消</div>
        <div class="button-right" @click="handleConfirm">确定</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    promptTitle: {
      type: String,
      default: ""
    }
  },
  data() {
    return {};
  },
  watch: {},
  computed: {},
  methods: {
    handleConfirmEnd() {
      this.$emit("handleConfirmEnd");
    },
    handleConfirm() {
      this.$emit("handleConfirm");
    }
  },
  created() {},
  mounted() {}
};
</script>
<style scoped lang="scss">
    @import '@/assets/scss/modules/modal.scss';

    /deep/ .ivu-modal{
        border: 8px solid rgba(0,0,0,0.15);
        border-radius: 10px;
    }
    .delete{
        /deep/ .ivu-modal-content{
            border-radius: 0;
        }
        /deep/ .ivu-modal-body{
            padding: 40px 16px 30px 16px;
        }
        /deep/ .ivu-modal-footer{
            border: none;
            height: 90px;
            .footerBtn{
                display: flex;
                justify-content: center;
                padding: 0;

            }
        }
        /deep/ .ivu-modal-close{
            right: 10px;
            top: 10px;
        }
    }
    .newBtn{
        width: 69px;
        height: 34px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 2px;
        &:hover{
            color: #333333;
        }
    }
    .newBtn1:hover{
        border-color: #dcdee2;
    }
//删除订单二次确认弹窗
.mask-wrapper {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  .mask-content {
    background: #ffffff;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 378px;
    height: 200px;
    border-radius: 5px;
    .p1 {
      text-align: right;
      padding: 5px 15px 0 0;
      font-size: 18px;
      cursor: pointer;
    }
    .p2 {
      text-align: center;
      margin-top: 15px;
      font-size: 24px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #333;
      line-height: 33px;
      margin-left: 20px;
    }
    .button-group {
      display: flex;
      justify-content: center;
      margin-top: 50px;
      margin-bottom: 20px;
      font-size: 15px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      .button-left {
        text-align: center;
        padding: 5px 0;
        cursor: pointer;
        width: 68px;
        height: 32px;
        background: #f5f5f5;
        border-radius: 1px;
        border: 1px solid #ddd;
        color: #333;
        line-height: 21px;
        letter-spacing: 1px;
        margin-right: 10px;
      }
      .button-right {
        text-align: center;
        padding: 5px 0;
        margin-left: 40px;
        cursor: pointer;
        width: 68px;
        height: 32px;
        background: #dd2619;
        border-radius: 1px;
        border: 1px solid #dd2619;
        color: #fff;
        line-height: 21px;
        letter-spacing: 1px;
      }
    }
  }
}
</style>