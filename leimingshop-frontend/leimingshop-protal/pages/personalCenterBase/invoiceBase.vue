<template>
  <div class="invoiceBase">
    <div class="invoiceTop">
      <div class="invoice-top-link">
        <!-- <p class="invoiceTop-on">我的发票</p>
                <p class="invoiceTop-li" @click="$router.push('/invoiceTitle')">发票抬头</p>
                <p class="invoiceTop-li" @click="$router.push('/invoiceText')">发票须知</p> -->
        <router-link
          to="/personalCenterBase/invoiceBase/useInvoice"
          :class="{ 'router-link-exact-active': exactFlag1 }"
          >我的发票</router-link
        >
        <router-link
          to="/personalCenterBase/invoiceBase/invoiceTitle"
          :class="{ 'router-link-exact-active': exactFlag2 }"
          >发票抬头</router-link
        >
        <router-link
          to="/personalCenterBase/invoiceBase/invoiceText"
          :class="{ 'router-link-exact-active': exactFlag3 }"
          >发票须知</router-link
        >
      </div>
      <p class="invoiceTop-left">
        <img
          :src="'/img/06.personalCenter/wenhao.png'"
          style="width：13px;height:13px;"
        />
        发票使用常见问题
      </p>
    </div>
    <router-view class="router-view"></router-view>
  </div>
</template>

<script>
export default {
  data() {
    return {
      exactFlag1: false,
      exactFlag2: false,
      exactFlag3: false,
    };
  },
  mounted() {
    this.judgeRouter(this.$route.name);
  },
  methods: {
    judgeRouter(routerName) {
      if (routerName == "useInvoice") {
        this.exactFlag1 = true;
        this.exactFlag2 = false;
        this.exactFlag3 = false;
      } else if (
        routerName == "applyInvoice" ||
        routerName == "verificationInvoice"
      ) {
        this.exactFlag1 = true;
        this.exactFlag2 = false;
        this.exactFlag3 = false;
      } else if (routerName == "invoiceTitle") {
        this.exactFlag1 = false;
        this.exactFlag2 = true;
        this.exactFlag3 = false;
      } else if (routerName == "invoiceText") {
        this.exactFlag1 = false;
        this.exactFlag2 = false;
        this.exactFlag3 = true;
      }
    },
  },
  watch: {
    $route: {
      immediate: true,
      handler(newVal, oldVal) {
        this.judgeRouter(newVal.name);
      },
    },
  },
};
</script>

<style lang="scss" scoped>
.invoiceTop {
  width: 948px;
  height: 55px;
  background-color: #ffffff;
  padding: 0 40px 0 0;
  border-bottom: 1px solid #dd2619;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .invoice-top-link {
    display: flex;

    a {
      width: 120px;
      height: 55px;
      line-height: 55px;
      font-size: 15px;
      font-weight: 500;
      color: #666666;
      text-align: center;
      cursor: pointer;
    }

    .nuxt-link-exact-active {
      width: 120px;
      height: 55px;
      background: #dd2619;
      line-height: 55px;
      font-size: 15px;
      font-weight: 500;
      color: #ffffff;
      text-align: center;
    }
  }
}
.invoiceTop-left {
  height: 14px;
  line-height: 14px;
  font-size: 13px;
  margin: 30px 0 10px;
}
.router-view {
}
</style>
