<template>
  <div class="wrapper">
    <div class="overall">
      <!-- 步骤条盒子 -->
      <div class="steps-box" v-if="stepList">
        <!-- 步骤条 -->
        <div
          class="Article-steps"
          :class="stepList.length <= activeIndex ? '' : ''"
        >
          <!-- 步骤条背景进度条 -->
          <div class="line">
            <span
              class="plan"
              :style="`width:${
                activeIndex * (100 / (stepList.length - 1)) -
                100 / (stepList.length - 1) / 2 +
                1
              }%`"
            ></span>
          </div>
          <!-- 每步部分 -->
          <span
            class="step"
            v-for="(item, index) in stepList"
            :key="index"
            :class="{ 'step-active': activeIndex >= index + 1 }"
          >
            <p class="title" v-text="item"></p>

            <img
              style="margin-top: 5px"
              src="/img/06.personalCenter/circlegray.png"
              alt=""
              v-show="index >= activeIndex"
            />
            <img
              src="/img/06.personalCenter/circlered.png"
              alt=""
              v-show="activeIndex == stepList.length || index < activeIndex - 1"
            />
            <img
              src="/img/06.personalCenter/circlered1.png"
              alt=""
              v-show="activeIndex < stepList.length && index == activeIndex - 1"
            />
            <p
              style="font-size: 12px; color: #999999"
              v-if="createDataList[index]"
              v-text="createDataList[index]"
            ></p>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    aftersaleApplyDetail: {
      type: Object,
      default: () => {},
    },
  },

  data() {
    return {
      //当前位置
      activeIndex: 1, // 步骤条步数
      stepListSalesReturn: ["提交申请", "售后审核", "售后收货", "退款", "完成"],
      stepListAftersalesCancel: ["提交申请", "售后审核", "取消申请"],
      stepListExchangeGoods: [
        "提交申请",
        "售后审核",
        "售后收货",
        "换货出库",
        "确认收货",
        "完成",
      ],
      createDataList: [],
    };
  },

  watch: {
    aftersaleApplyDetail: {
      immediate: true,
      handler(newVal, oldVal) {
        if (newVal.detailDTOList) {
          this.createDataList = [];
          this.handleAuditPass(newVal.aftersaleType);
          this.handleActiveIndex(newVal.detailDTOList);
        }
      },
    },

    stepList: {
      immediate: true,
      handler(newVal, oldVal) {
        if (!newVal) return;

        let parentStatusText;

        if (this.activeIndex == newVal.length) {
          parentStatusText = newVal[this.activeIndex - 1];
        } else {
          parentStatusText = newVal[this.activeIndex - 2];
        }

        this.$emit("handleStatusText", parentStatusText);
      },
    },
  },

  computed: {
    stepList() {
      // 用户取消申请
      if (this.aftersaleApplyDetail.auditStatus == 4) {
        this.$set(this.createDataList, 2, this.createDataList[1]);
        this.$set(this.createDataList, 1, "");
        return this.stepListAftersalesCancel;
      }

      // 退货
      if (this.aftersaleApplyDetail.aftersaleType == 0) {
        return this.stepListSalesReturn;
      }

      // 换货
      if (this.aftersaleApplyDetail.aftersaleType == 1) {
        return this.stepListExchangeGoods;
      }
    },
  },

  methods: {
    // 进度条状态
    handleActiveIndex(param) {
      // 审核拒绝，直接返回
      if (this.aftersaleApplyDetail.auditResult == 2) {
        return (this.activeIndex = 3);
      }

      // 获取当前售后单状态
      param.forEach((item, index) => {
        this.$set(this.createDataList, index, item.createDate);
        if (item.currentState == 1) {
          this.activeIndex = index + 2;
          return;
        }
      });
    },

    // 审核拒绝
    handleAuditPass(type) {
      let text;
      switch (this.aftersaleApplyDetail.auditResult) {
        case 1:
          text = "审核通过";
          break;
        case 2:
          text = "审核拒绝";
          break;
        default:
          text = "售后审核";
      }

      switch (type) {
        case 0:
          this.$set(this.stepListSalesReturn, 1, text);
          break;
        case 1:
          this.$set(this.stepListExchangeGoods, 1, text);
          break;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrapper {
  width: 100%;
}
.overall {
  background: #ffffff;
  width: 100%;
  padding: 30px;
}
.steps-box {
  user-select: none;
  width: 100%;
  height: auto;
  position: relative;
  /* 步骤条背景进度条 */
  .line {
    display: block;
    margin: 0 auto;
    position: absolute;
    top: 22px;
    left: 50px;
    background: #ccc;
    width: 87%;
    height: 2px;
    overflow: hidden;
    .plan {
      position: absolute;
      top: 0;
      left: 0;
      height: 2px;
      transition: 0.5s;
      background: red;
    }
  }
  .Article-steps {
    display: flex;
    justify-content: space-between;
    .step {
      width: 122px;
      display: inline-block;
      text-align: center;
      z-index: 1000;
      .title {
        font-size: 15px;
        font-weight: 500;
        color: #424242;
        margin-top: 5px;
        margin-top: -10px;
      }
      .step-num {
        width: 25px;
        height: 25px;
        display: inline-block;
        line-height: 50px;
        background: #c0c0c0;
        border-radius: 50%;
        .num {
          transition: 0.5s;
          display: inline-block;
        }
      }
    }
  }

  /*当前所在位置样式*/
  .step-active {
    .step-num {
      background: #2d7df5 !important;
      transform: rotate(90deg);
      .num {
        transform: rotate(-90deg);
      }
    }
    .title {
      color: #dd2619 !important;
    }
  }

  .times:first-child {
    margin-top: 0;
  }

  /*全部完成样式*/
  .step-over {
    .plan {
      background: #91f062 !important;
    }
    .step-num {
      background: #67c23a !important;
    }
    .title {
      color: #67c23a !important;
    }
  }
}
</style>
