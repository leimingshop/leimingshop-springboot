<template>
  <div class="wrapper">
    <div class="overall" v-show="appStatus != 0">
      <!-- 步骤条盒子 -->
      <div class="steps-box">
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
          <!-- 每步部分  未取消状态-->
          <span
            class="step"
            v-for="(i, index) in stepList"
            :key="index"
            :class="
              activeIndex == i.stepIndex || i.stepIndex <= activeIndex
                ? 'step-active'
                : ''
            "
          >
            <p class="title">{{ i.title }}</p>
            <img
              style="margin-top: 15px"
              src="/img/06.personalCenter/circlegray.png"
              alt
              v-show="index >= activeIndex"
            />
            <img
              style="margin-top: 10px"
              src="/img/06.personalCenter/circlered.png"
              alt
              v-show="activeIndex == 5 || index < activeIndex - 1"
            />
            <img
              style="margin-top: 10px"
              src="/img/06.personalCenter/circlered1.png"
              alt
              v-show="activeIndex < 5 && index == activeIndex - 1"
            />

            <p style="font-size: 12px; color: #999999" v-show="i.time">
              {{ i.time }}
            </p>
            <p style="font-size: 13px; color: #ffffff" v-show="!i.time">
              2020-06-01 14:00:46
            </p>
          </span>
        </div>
      </div>
    </div>
    <div class="overall" v-show="appStatus == 0">
      <!-- 步骤条盒子 -->
      <div class="steps-box" style="width: 34%">
        <!-- 步骤条 -->
        <div
          class="Article-steps"
          :class="stepList.length <= activeIndex ? '' : ''"
        >
          <!-- 步骤条背景进度条 -->
          <div class="line" style="width: 60%">
            <span class="plan" style="width: 100%"></span>
          </div>
          <!-- 每步部分  取消状态-->
          <span
            class="step"
            v-for="(i, index) in stepList1"
            :key="index"
            :class="
              activeIndex == i.stepIndex || i.stepIndex <= activeIndex
                ? 'step-active'
                : ''
            "
            v-show="appStatus == 0"
          >
            <p class="title" style="color: #dd2619">{{ i.title }}</p>
            <img
              style="margin-top: 10px"
              src="/img/06.personalCenter/circlered.png"
              alt
            />
            <!-- 每步部分  取消状态-->
            <p style="font-size: 12px; color: #999999">{{ i.time }}</p>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    detailData: {
      type: Object,
      required: true,
      default: function () {
        return {};
      },
    },
  },
  data() {
    return {
      appStatus: 10,
      orderTimeDTOList: [], //进度条的状态表
      //当前位置
      activeIndex: 1,
      //步骤条步数 -未取消状态
      stepList: [
        {
          stepIndex: 1,
          title: "1、提交订单",
          time: "",
        },
        {
          stepIndex: 2,
          title: "2、付款成功",
          time: "",
        },
        {
          stepIndex: 3,
          title: "3、商品出库",
          time: "",
        },
        {
          stepIndex: 4,
          title: "4、等待收货",
          time: "",
        },
        {
          stepIndex: 5,
          title: "5、交易完成",
          time: "",
        },
      ],
      //步骤条步数 -取消状态
      stepList1: [
        {
          stepIndex: 1,
          title: "1、提交订单",
          tiem: "",
        },
        {
          stepIndex: 2,
          title: "2、已取消",
          time: "",
        },
      ],
    };
  },
  methods: {
    //点击下一步
    nextStep() {
      this.activeIndex += 1;
    },
    //点击上一步
    lastStep() {
      this.activeIndex -= 1;
    },
    filterStatus() {
      switch (parseInt(this.appStatus)) {
        case 10:
          this.activeIndex = 1;
          break;
        case 20:
          this.activeIndex = 2;
          break;
        case 30:
          this.activeIndex = 3;
          break;
        case 40:
          this.activeIndex = 5;
          break;
        default:
          this.activeIndex = 0;
      }
    },
  },
  watch: {},
  mounted() {
    this.appStatus = this.detailData.appStatus;
    this.filterStatus();
    setTimeout(() => {
      this.orderTimeDTOList = this.detailData.orderTimeDTOList;
      this.orderTimeDTOList.forEach((item, index) => {
        this.stepList[index].time = item.operationDate;
        if (index == 0) {
          this.stepList1[0].time = item.operationDate;
        } else {
          this.stepList1[1].time = item.operationDate;
        }
      });
    }, 300);
  },
};
</script>

<style lang="scss" scoped>
.wrapper {
  width: 100%;
  margin-top: 10px;
}
.overall {
  background: #ffffff;
  width: 100%;
  padding: 40px 30px 30px;
}
.steps-box {
  user-select: none;
  width: 100%;
  height: auto;
  position: relative;
  // <!-- 步骤条背景进度条 -->
  .line {
    display: block;
    margin: 0 auto;
    position: absolute;
    top: 32px;
    left: 50px;
    background: #f0f0f0;
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
    width: 100%;
    display: flex;
    justify-content: space-between;

    .step {
      text-align: center;
      z-index: 10;
      .title {
        font-size: 14px;
        font-weight: 400;
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
        // clip-path: polygon(50% 0, 100% 85%, 0 85%);
        // color: white;
        // font-weight: bold;
        // transition: 0.5s;
        .num {
          transition: 0.5s;
          display: inline-block;
        }
      }
    }
  }

  //当前所在位置样式
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

  //全部完成样式
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
  //对应内容
  .Article-content {
    padding: 20px;
    .btn {
      width: 150px;
      display: block;
      margin: 0 auto;
      margin-bottom: 10px;
      background: #2d7df5;
      color: white;
      padding: 10px;
      border-radius: 5px;
      cursor: pointer;
    }
    .content {
      padding: 20px;
    }
  }
}
</style>
