<template>
  <div class="explain">
    <div class="explain-top">优惠券使用说明</div>
    <div class="explain-content">
      <p class="explainSpan">1.优惠券的获取</p>
      <p class="explainWord">
        通过活动赠送、客服补偿等获得(系统自动添加, 无需兑换;
      </p>
      <p class="explainSpan">2.优惠券使用</p>
      <p class="explainWord">
        1）每张抵用券仅能使用一次，不找零，不退换，不兑换现金；
      </p>
      <p class="explainWord">
        2）单笔订单只能使用1张优惠券, 不支持同时使用多张, 用券后差额不找零,
        不退回；
      </p>
      <p class="explainWord">
        3）优惠券 (包括新用户券) 不能抵扣运费,
        只能抵扣商品金额。特价商品不可使用优惠券, 与其他优惠不同享；
      </p>
      <p class="explainWord">
        4）每张优惠券的使用条件请查看对应优惠券的使用说明；
      </p>
      <p class="explainWord">
        5）请在有效期内使用优惠券, 未使用的优惠券过期后, 将自动作废；
      </p>
      <p class="explainWord">
        6）每个用户仅限使用1张新用户专享优惠券, 同一帐号和手机号均视为同一用户；
      </p>
      <p class="explainWord">
        7）用券前订单满88元即可包邮（港澳台地区需满500元包邮）；
      </p>
      <p class="explainWord">8）使用抵用券抵扣部分的货款不开具发票</p>
      <p class="explainSpan">3.优惠券失效</p>
      <p class="explainWord">
        1）新用户券使用一张后其他新用户券失效, 取消订单后不返还；
      </p>
      <p class="explainWord">
        2）使用优惠券的订单, 若产生退货, 优惠券均不退回,
        退款金额按优惠后的小计金额退款；
      </p>
      <p class="explainWord">
        3）参加满赠券活动获得的赠券,发生退货时,若订单中剩余商品不满足赠券条件,实物赠品需勾选退货并寄回,否则将从退款中
        扣减对应金额,客服审核通过退货申请后,所获赠券将会自动失效；
      </p>
      <p class="explainWord">
        4）优惠券严禁出售或转让, 如经发现并证实的, 该券将予以失效处理；
      </p>
      <p class="explainWord">
        5）如需了解更多, 请联系在线客服或拨打客服电话400-0000-000。
      </p>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return;
  },
  head() {
    return {
      title: "优惠券说明",
      meta: [
        {
          hid: "description",
          name: "description",
          content: "My custom description",
        },
      ],
    };
  },
};
</script>
<style lang="scss" scoped>
.explain {
  width: 948px;
  height: 850px;
  background: #ffffff;
  padding: 0px 40px;
}
.explain-top {
  height: 54px;
  line-height: 54px;
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 600;
  color: #333333;
  border-bottom: 1px solid #f5f5f5ff;
  margin-bottom: 20px;
}
.explain-content {
  .explainSpan {
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 600;
    color: #222222;
    margin-top: 20px;
  }
  .explainWord {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    margin-top: 10px;
  }
}
</style>