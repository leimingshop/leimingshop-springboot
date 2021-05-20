<template>
  <div class="to-pay">
    <!-- 顶部搜索部分 -->
    <search-first-level class="pay_top"></search-first-level>
    <!-- 面包屑 -->
    <breadcrumb1 :list="breadcrumbList" />
    <div class="to-pay-body">
      <div class="to-pay-head">
        <div class="head-left">
          <img
            src="/img/04.shoppingCar/pay_icon.png"
            class="head-left-icon"
          />
          <div class="head-left-word">
            <div>{{ payMsg }}</div>
            <div
              v-show="timeClock && timeClock !== '0时0分0秒'"
              style="font-size: 0px"
            >
              <span style="font-size: 14px">请在</span>
              <span class="timeClock">{{ timeClock }}</span>
              <span style="font-size: 14px">内完成支付, 超时后将取消订单</span>
            </div>
            <!-- <div v-show="!timeClock || timeClock =='0时0分0秒'">订单已过期或已支付完成</div> -->
          </div>
        </div>
        <div class="head-right">
          应付总额
          <span
            style="
              color: rgba(34, 34, 34, 1);
              font-weight: 600;
              font-family: PingFangSC-Regular, PingFang SC;
            "
            >¥{{ payInfo.orderAmount }}</span
          >
        </div>
      </div>
      <div class="choose-pay">
        <img
          src="/img/04.shoppingCar/pay_style.png"
          class="choose-style-img"
        />
        <div
          class="pay-style-box"
          v-for="(item, index) of payStyle"
          :key="index"
          :class="{ 'pay-style-box-checked': current == index }"
          @click="choosePayStyle(index)"
        >
          <div class="style-box-left">
            <!-- <div class="style-chekbox" :class="{'style-chekbox-checked':current==index}">
                            <img src="@static/img/04.shoppingCar/checked.png" v-show="current==index">
            </div>-->
            <Checkbox v-model="item.currentIndex"></Checkbox>
            <img
              class="style-img"
              :src="item.paymentLogo | filterImgUrl"
              style="margin: 0 10px 0 42px"
            />
            <div class="pay-style-name">{{ item.paymentName }}支付</div>
          </div>
          <div class="style-box-right" v-show="current == index">
            支付
            <span
              style="
                font-size: 18px;
                font-weight: 600;
                color: #dd2619;
                line-height: 22px;
              "
              >{{ payInfo.orderAmount }}</span
            >元
          </div>
        </div>
      </div>
      <a
        class="pay-now"
        @click="pay()"
        v-show="timeClock && timeClock !== '0时0分0秒'"
        >立即支付</a
      >
    </div>
    <Modal v-model="modal">
      <p>正在支付中...</p>
      <div class="message-first">即将跳转到支付页面</div>
      <div class="message-second">如果您已经支付完成请关闭该窗口并刷新页面</div>
    </Modal>
  </div>
</template>

<script>
// 搜索头部
import searchPart from "@/components/common/searchPartWhite.vue";
import breadcrumb1 from "@/components/common/breadcrumb1.vue";
import {
  checkPaySign,
  toPay,
  payType,
  aliPay,
} from "@/api/api_04.shoppingCar.js";
export default {
  data() {
    return {
      modal: false,
      paySn: "",
      payInfo: {
        cancelDate: "",
        createDate: "",
        currentTime: "",
        orderAmount: 0,
        orderId: null,
        paySn: "",
      },
      payStyle: [
        {
          id: "",
          paymentCode: "",
          paymentConfig: null,
          paymentLogo: "",
          paymentName: "",
          paymentState: 1,
          remark: "",
          currentIndex: false,
        },
      ],
      timeClock: "",
      current: 0,
      payMsg: "订单提交中",
      breadcrumbList: Object.freeze([
        {
          title: "首页",
          toPath: "/",
        },
        {
          title: "支付订单",
          toPath: "",
        },
      ]),
    };
  },
  components: {
    searchPart,
    breadcrumb1,
  },
  mounted() {
    this.gePayInfo();
  },
  methods: {
    gePayInfo() {
      payType()
        .then((res) => {
          this.payStyle = res.data;
          this.payStyle.forEach((item) => {
            item.currentIndex = false;
          });
          this.payStyle[0].currentIndex = true;
        })
        .catch((error) => {
          console.log(error);
        });
      checkPaySign(this.$route.query.orderId)
        .then((res) => {
          if (res.code == 500) {
            return;
          } else if (res.code !== 200) {
            setTimeout(() => {
              this.gePayInfo();
            }, 1000);
            console.log(res.code);
          } else {
            this.paySn = res.data.paySn;
            toPay(this.paySn)
              .then((res) => {
                this.payMsg = res.msg;
                if (res.code !== 200) {
                } else {
                  this.payInfo = res.data;
                  this.payInfo.orderId = this.$route.query.orderId;
                  this.countTime(res.data.cancelDate, res.data.currentTime);
                }
              })
              .catch((error) => {
                console.log(error);
              });
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    countTime(cancelDate, currentTime) {
      var dateBegin = currentTime; //将-转化为/，使用new Date
      var dateEnd = new Date(cancelDate.replace(/-/g, "/")); //获取当前时间
      var dateDiff = dateEnd.getTime() - dateBegin; //时间差的毫秒数
      var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000)); //计算出相差天数
      var leave1 = dateDiff % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
      var hours = Math.floor(leave1 / (3600 * 1000)); //计算出小时数
      //计算相差分钟数
      var leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
      var minutes = Math.floor(leave2 / (60 * 1000)); //计算相差分钟数
      //计算相差秒数
      var leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
      var seconds = Math.round(leave3 / 1000);
      // console.log(" 相差 " + dayDiff + "天 " + hours + "小时 " + minutes + " 分钟" + seconds + " 秒")
      // console.log(dateDiff + "时间差的毫秒数", dayDiff + "计算出相差天数", leave1 + "计算天数后剩余的毫秒数", hours + "计算出小时数",
      //     minutes + "计算相差分钟数", seconds + "计算相差秒数");
      var maxtime = leave1 / 1000; //一个小时，按秒计算，自己调整!
      var timer = setInterval(() => {
        if (maxtime >= 0) {
          hours = Math.floor(maxtime / 60 / 60);
          minutes = Math.floor(maxtime / 60);
          minutes = minutes % 60;
          seconds = Math.floor(maxtime % 60);
          this.timeClock = hours + "时" + minutes + "分" + seconds + "秒";
          --maxtime;
        } else {
          this.payMsg = "订单已过期或已支付完成";
          clearInterval(timer);
        }
      }, 1000);
    },
    choosePayStyle(index) {
      this.payStyle.forEach((item) => {
        item.currentIndex = false;
      });
      // this.payStyle[index].currentIndex = true
      this.$set(this.payStyle[index], "currentIndex", true);
      this.current = index;
    },
    pay() {
      this.modal = true;
      if (this.payStyle[this.current].paymentCode == "WX") {
        window.open(
          window.location.protocol +
            "//" +
            window.location.host +
            "/wechatPay?paySn=" +
            this.paySn +
            "&orderId=" +
            this.$route.query.orderId
        );
      } else if (this.payStyle[this.current].paymentCode == "ZFB") {
        aliPay({
          orderId: this.$route.query.orderId,
          paySn: this.paySn,
        })
          .then((res) => {
            if (res.code == 200) {
              var alipay = res.data;
              const div = document.createElement("div"); // 创建div
              div.innerHTML = alipay; // 将返回的form 放入div
              document.body.appendChild(div);
              document.forms[0].submit();
            }
            console.log(res);
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    cancel() {
      this.$Message.info("Clicked cancel");
    },
  },
};
</script>

<style lang="scss" scoped>
// 更改顶部搜索样式
/deep/ .search-wrap {
  justify-content: space-between;

  .search-con {
    width: 560px;
    flex-grow: inherit !important;

    .search-con-search-bottom {
      display: none;
    }
  }

  .artCart {
    display: none;
  }
}

/deep/ .pay_top > div:first-child {
  background: #fff;
}

.to-pay {
  width: 100%;
  background-color: #f6f6f6;
  padding: 0 0 60px 0;
  margin: 0 auto;

  .ivu-breadcrumb {
    width: 1200px;
    margin: 0 auto;
    line-height: 40px;
    span {
      font-size: 13px;
      color: #666666;
      a {
        font-size: 13px;
        color: #666666;
      }
    }
  }

  .to-pay-body {
    width: 1200px;
    background-color: #fff;
    margin: 0 auto;
    padding: 0 0 50px 0;

    .to-pay-head {
      width: 1200px;
      height: 104px;
      background: rgba(255, 255, 255, 1);
      padding: 0 88px 0 52px;
      box-shadow: 0px 2px 6px 0px rgba(221, 217, 217, 0.31);
      display: flex;
      justify-content: space-between;
      align-items: center;

      .head-left {
        display: flex;
        align-items: center;

        img {
          width: 36px;
          height: 36px;
          margin: 0 14px 0 0;
        }

        .head-left-word {
          font-size: 14px;
          color: #333;
          line-height: 20px;
        }
      }

      .head-right {
        font-size: 18px;
        color: rgba(102, 102, 102, 1);
        line-height: 30px;
      }
    }

    .choose-pay {
      padding: 147px 0 0 0;
      position: relative;

      .choose-style-img {
        position: absolute;
        top: 50px;
        left: -10px;
      }

      .pay-style-box {
        width: 1079px;
        height: 88px;
        padding: 0 24px 0 46px;
        border-bottom: 1px solid rgba(204, 204, 204, 0.29);
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        cursor: pointer;

        .style-box-left {
          display: flex;
          align-items: center;

          .style-chekbox {
            width: 22px;
            height: 22px;
            border: 1px solid #ccc;
          }

          .style-chekbox-checked {
            width: 22px;
            height: 22px;
            border: 1px solid #dd2619;
            text-align: center;
          }

          .style-img {
            width: auto;
            height: 29px;
          }

          .pay-style-name {
            font-size: 20px;
            font-weight: 500;
            color: #333;
            line-height: 28px;
          }
          /deep/ .ivu-checkbox-inner {
            width: 18px;
            height: 18px;
            &:after {
              top: 3px;
              left: 6px;
            }
          }
        }
        .style-box-right {
          font-size: 14px;
          color: rgba(51, 51, 51, 1);
        }
      }

      .pay-style-box-checked {
        box-shadow: 0px 1px 4px 0px rgba(142, 136, 136, 0.27);
      }
    }

    .pay-now {
      width: 100px;
      height: 40px;
      background: rgba(221, 38, 25, 1);
      border-radius: 2px;
      margin: 106px auto 0 auto;
      font-size: 16px;
      font-weight: 500;
      color: rgba(255, 255, 255, 1);
      line-height: 40px;
      text-align: center;
      display: block;
    }
  }
}

/deep/ .ivu-modal {
  border-radius: 12px;
  border: 7px solid rgba(0, 0, 0, 0.14);

  /deep/ .ivu-modal-header {
    padding-top: 33px;
    font-size: 24px;
    font-weight: 600;
    border-bottom: 0px;
    font-family: PingFangSC-Regular, PingFang SC;
    color: rgba(0, 0, 0, 1);
    line-height: 24px;
    letter-spacing: 3px;
    text-align: center;

    .ivu-modal-header p,
    .ivu-modal-header-inner {
      height: 24px;
      font-size: 24px;
    }
  }

  .ivu-modal-body {
    height: 240px;
    padding: 22px 20px 34px 20px;
    text-align: center;

    p {
      margin: 40px 0 0 0;
      font-size: 24px;
      font-weight: 500;
    }

    .message-first {
      margin: 20px 0 0 0;
      font-size: 18px;
      color: #666;
    }

    .message-second {
      margin: 20px 0 0 0;
      font-size: 14px;
      color: #666;
    }
  }

  .ivu-modal-footer {
    display: none;
  }
}

.timeClock {
  display: inline-block;
  width: 95px;
  text-align: center;
  color: #dd2619;
  font-size: 14px;
}
</style>
