<template>
  <div class="invoice">
    <div class="invoiceMain">
      <span>一、发票介绍</span>
      <p>
        雷铭商城一共可以提供三种发票可供用户选择：电子普通发票、纸质普通发票、增值税专项发票具体商品可开具发票已商家可提供发票为准。
      </p>
      <span>二、开具时间范围</span>
      <p>
        自订单完成之日起，会员可在一个月内进行发票申请，已申请的发票，在订单完成之日一年，可以进行换开，已过期的无法申请。
      </p>
      <span>三、作废方式</span>
      <p>
        对于已开具发票的订单，会员予以退货处理的，纸质发票应与货物一同邮寄到商家。电子发票无需处理，自动作废。如发票缺失，则按照税点扣除会申请退款的金额。
      </p>
      <span>四、发票相关</span>
      <p>
        雷铭商城一切发票均遵守国家相关税法规定，具体规定是啥我也不知道，为了显着整齐，再打几个字。反正就是不能违法就对了。。
      </p>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  head() {
    return {
      title: "发票须知",
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
.invoice {
  width: 948px;
  height: 1247px;
  background: #ffffff;
  padding: 45px 0 0 0;
  .invoiceMain {
    width: 773px;
    height: 418px;
    margin: 0 auto;
    padding: 40px 38px 24px 38px;
    background: #feeded;
    span {
      font-size: 16px;
      font-weight: 400;
      color: #333333;
    }
    p {
      font-size: 14px;
      font-weight: 400;
      margin: 7px 0 25px 0;
      color: #666666;
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}
</style>