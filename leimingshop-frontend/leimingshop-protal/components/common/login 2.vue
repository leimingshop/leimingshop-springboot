<template>
  <div class="container">
    <!--  <i-button class="test-btn" type="primary" @click="modal1 = true">登录弹窗,后期会删除的</i-button> -->
    <Modal
      v-model="modal1"
      title="您尚未登录，请登录后再执行操作"
      width="406px"
      :footer-hide="true"
      class="login-modal"
    >
      <loginCom class="content-list"></loginCom>
      <!-- <div class="artInput">
                <span><img src="/img/01.index/icon-password.png" alt=""></span>
                <i-input :value.sync="userName" placeholder="用户名称/手机号码"></i-input>
            </div>
            <div class="artInput">
                <span><img src="/img/01.index/yonghu.png" alt=""></span>
                <i-input :value.sync="passWord" placeholder="请输入登录密码"></i-input>
            </div>
            <div class="artLogin">登录</div>
            <div class="artRegister"><span>忘记密码？</span><span>新用户注册</span></div>
            <p class="artOtherlogin">其它方式登录</p>
            <div class="artOtherlogins">
                <p><img src="/img/01.index/QQ1.png" alt="">QQ</p>
                <p><img src="/img/01.index/weixin.png" alt="">微信</p>
                <p><img src="/img/01.index/weibo1.png" alt="">微博</p>
            </div> -->
    </Modal>
  </div>
</template>
<script>
import { Modal } from "iview";
import loginCom from "@/components/common/login.vue";
export default {
  name: "loginPopup",
  props: {
    topHeight: {
      //顶部显示高度
      type: Number,
      default: 100,
      required: false,
    },
  },
  components: {
    loginCom,
  },
  data() {
    return {
      modal1: false,
      userName: "",
      passWord: "",
    };
  },
  mounted() {},
  destroyed() {},
  methods: {
    ok() {
      this.$Message.info("点击了确定");
    },
    cancel() {
      this.$Message.info("点击了取消");
    },
  },
};
</script>
<style lang="scss" scoped>
.login-modal {
  /deep/ .ivu-modal {
    top: 25%;
  }
  /deep/ .ivu-modal-mask {
    background-color: rgba(0, 0, 0, 0.14);
  }
  /deep/ .ivu-modal-content {
    padding-bottom: 10px;
    border-radius: 10px;
    position: relative;
    /deep/ .ivu-modal-close {
      position: absolute;
      top: 0;
    }
    /deep/ .ivu-modal-header {
      height: 34px;
      background: #f3f3f3;
      padding: 0 20px;
      border-top-left-radius: 10px;
      border-top-right-radius: 10px;
      .ivu-modal-header-inner {
        height: 34px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        line-height: 34px;
      }
    }
    .ivu-modal-body {
      padding: 0;
      /deep/ .content-box {
        padding-top: 20px;
      }
    }
  }
}
</style>