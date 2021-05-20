<template>
  <div class="changePhone">
    <div class="changePhoneTop">
      <span>更换邮箱</span>
    </div>
    <div class="changeContent">
      <div style="height: 700px">
        <component :is="tab" @childFn1="parentFn1">下一步</component>
      </div>
      <div class="changeContent-footer">
        <ul>
          <li class="change-p">温馨提示：</li>
          <li class="change-text">
            • 为保障您的帐号安全，变更重要信息需要身份验证
          </li>
          <li class="change-text">
            • 若有疑问请联系在线客服或拨打400-0000-000（周一至周日 8:00-18:00）
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
import Cookies from "js-cookie";
import changeEmail01 from "@/components/06.personalCenter/06-10.2.changeEmail/changeEmail01.vue";
import changeEmail02 from "@/components/06.personalCenter/06-10.2.changeEmail/changeEmail02.vue";
import changeEmail03 from "@/components/06.personalCenter/06-10.2.changeEmail/changeEmail03.vue";
export default {
  head() {
    return {
      title: "更换邮箱",
      meta: [
        {
          hid: "description",
          name: "description",
          content: "My custom description",
        },
      ],
    };
  },
  components: {
    changeEmail01,
    changeEmail02,
    changeEmail03,
  },
  data() {
    return {
      current: 0,
      tab: "changeEmail01",
    };
  },
  methods: {
    parentFn1(data) {
      this.tab = data;
    },
  },
};
</script>
<style lang="scss" scoped>
.changePhone {
  background: #eeeeee;
}

.changePhoneTop {
  width: 948px;
  height: 54px;
  line-height: 54px;
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #373737;
  background: #ffffff;
  padding: 0 40px;
  border-bottom: 1px solid #ebebeb;
}
.changePhoneContent {
  width: 411px;
  margin: 0 auto;
  .changePhone-text {
    font-size: 18px;
    font-weight: 500;
    margin: 40px 0 10px;
    text-align: center;
  }
  .changePhone-small {
    font-size: 16px;
    color: #999999;
    margin: 0 0 40px;
    text-align: center;
  }
}
.changeContent {
  width: 948px;
  height: 990px;
  background: #ffffff;
  // margin: 10px 0 0 0 ;
  padding: 100px 40px 0 40px;
}
.changeContent-footer {
  height: 168px;
  background-color: #ffffff;
  padding: 40px 0;
  border-top: 1px solid #ebebeb;
  .change-p {
    font-size: 14px;
    font-weight: 500;
    margin: 0 0 15px;
    color: #333333;
  }
  .change-text {
    font-size: 13px;
    font-weight: 400;
    margin: 0 0 10px;
    color: #717171;
  }
}
/deep/ .ivu-steps-horizontal .ivu-steps-content {
  padding: 0;
  font-size: 16px;
  font-weight: 400;
}
/deep/ .ivu-steps-item.ivu-steps-status-process .ivu-steps-head-inner {
  border-color: #2ac68a;
  background-color: #2ac688;
}
/deep/ .ivu-steps .ivu-steps-head-inner {
  width: 24px;
  height: 24px;
  line-height: 22px;
  background: #dddddd;
}

/deep/.ivu-steps-item.ivu-steps-status-process .ivu-steps-tail > i {
  background: #82d38d;
}
/deep/ .person-center-right[data-v-4fc7d81c] {
  background: rgb(238, 238, 238);
}
/deep/ .ivu-input-wrapper {
  width: 213px;
}
</style>