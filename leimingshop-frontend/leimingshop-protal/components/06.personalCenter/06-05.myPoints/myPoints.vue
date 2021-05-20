<template>
  <div class="myDiv">
    <div class="my-points">
      <div class="currentpoints">
        <div class="currentpoints-left">
          <div class="current-left">
            <img src="/img/06.personalCenter/points.png" alt />
          </div>
          <div class="current-right">
            <p>
              当前积分：{{ MyPointsDetailList.availablePoint | valueConver }}
            </p>
            <span>做任务赚积分</span>
          </div>
        </div>
        <div class="currentpoints-right">
          <div class="accumulation">
            <p class="plist-another">
              {{ MyPointsDetailList.availablePoint | valueConver }}
            </p>
            <span class="after">累积获得</span>
          </div>
          <div class="accumulation">
            <P class="plist">{{
              MyPointsDetailList.totalUsed | valueConver
            }}</P>
            <span class="after">累积使用</span>
          </div>
          <div class="accumulation">
            <p class="plist">
              {{ MyPointsDetailList.payOrders | valueConver }}
            </p>
            <span class="after">支付订单数</span>
          </div>
          <div class="cumulative-savings">
            <div class="plist">
              <span class="money">￥</span>
              <span>{{ MyPointsDetailList.totalSave | valueConver }}</span>
            </div>
            <span class="after">累积节省</span>
          </div>
        </div>
      </div>
      <div class="recentdetails">
        <div class="recentdetails1">
          <div class="recentdetails1-left">近期明细</div>
          <div class="recentdetails1-right" id="lookMore">
            <span @click="seeAll">查看全部</span>
            <Icon type="ios-arrow-forward" />
          </div>
        </div>
        <div class="recentdetails2">
          <ul>
            <li
              v-for="(item, index) in MyPointsOneWeek"
              :key="index"
              v-show="item.pointType == 1"
            >
              <div>
                <p class="recent1">{{ item.pointDesc }}</p>
                <p class="recent2">{{ item.createDate }}</p>
              </div>
              <div class="daylogin">
                <span v-if="item.pointType >= 0">+</span>
                <span v-else>-</span>
                {{ item.pointValue }}
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
export default {
  components: {},
  props: {},
  data() {
    return {};
  },
  watch: {},
  computed: {
    ...mapState("myPoints", ["MyPointsDetailList", "MyPointsOneWeek"]),
  },
  methods: {
    seeAll() {
      this.$emit("seeAllType");
    },
  },
  filters: {
    valueConver(data) {
      return data == null ? 0 : data;
    },
  },
  created() {},
  mounted() {},
};
</script>
<style scoped lang="scss">
.myDiv {
}
.integration-center {
  width: 1000px;
}
.intecenter-top-left {
  display: flex;
  justify-content: flex-start;
}
.integra-center-top {
  background: #fff;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #dd2619;
}
.intecenter-top-right {
  display: flex;
  align-items: center;
  margin-right: 20px;
  span {
    color: #a3a3a3;
    font-size: 14px;
    margin-left: 5px;
  }
}
.base {
  width: 120px;
  text-align: center;
  height: 55px;
  line-height: 55px;
  cursor: pointer;
  font-size: 15px;
  color: #666666;
}
.current {
  color: #fff;
  width: 120px;
  text-align: center;
  height: 55px;
  line-height: 55px;
  background: #dd2619;
}
.ivu-icon {
  color: #dd2619;
  vertical-align: baseline;
}
.ivu-date-picker-rel {
  color: #515a6e;
  a {
    color: #515a6e;
  }
}
.currentpoints {
  display: flex;
  justify-content: space-between;
  // border: 2px solid rgba(228, 228, 228, 1);
  background: #ffffff;
  padding: 32px 0;
}
.currentpoints-left {
  width: 457px;
  height: 60px;
  border-right: 1px solid #ebebeb;
  text-align: center;
  background: #ffffff;
  display: flex;
  justify-content: left;
  span:first-child {
    margin-top: 10px;
    display: block;
  }
  .span2 {
    margin-top: 10px;
    display: block;
    font-size: 24px;
    color: red;
  }
  i {
    display: block;
    margin-top: 10px;
    margin-right: 0;
  }
  .current-left {
    margin-left: 65px;
  }
}
.currentpoints-right {
  display: flex;
  // justify-content: space-around;
  margin-left: 70px;
  width: 100%;
  align-items: center;
}
.accumulation {
  width: 100px;
  height: 60px;
  text-align: center;
}
.plist {
  text-align: center;
  height: 21px;
  line-height: 21px;
  margin: 5px 0 10px;
  font-size: 26px;
  color: #555555;
  font-weight: 600;
  font-family: PingFangSC-Semibold, PingFang SC;
}

.plist-another {
  text-align: center;
  font-size: 26px;
  color: #dd2619;
  font-weight: 600;
  font-family: PingFangSC-Medium, PingFang SC;
  height: 21px;
  line-height: 21px;
  margin: 5px 0 10px;
}
.cumulative-savings {
  text-align: center;
  width: 100px;
  height: 60px;
  div {
    icon {
      color: #555555;
      vertical-align: baseline;
    }
    span {
      text-align: center;
      font-size: 26px;
      color: #555555;
      font-weight: 600;
      margin-left: -8px;
    }
  }
}
.recentdetails {
  margin-top: 10px;
  background: #ffffff;
  padding: 0px 40px 30px 40px;
}
.recentdetails1 {
  // padding-bottom: 20px;
  height: 54px;
  line-height: 54px;
  display: flex;
  justify-content: space-between;
}
.recentdetails2 li {
  display: flex;
  justify-content: space-between;
  // height: 77px;
  padding: 20px 0;
  border-bottom: 1px solid #ebebeb;
  // padding-bottom: 30px;
  align-items: center;
}
.daylogin {
  margin-right: 100px;
  color: #dd2619;
  font-size: 20px;
  font-family: PingFangSC-regular, PingFang SC;
  font-weight: 400;
  span {
    margin-right: -5px;
  }
}
.usage {
  background: #ffffff;
  padding: 20px 40px 40px 40px;
}
.usage1 {
  display: flex;
  justify-content: space-between;
  padding-bottom: 10px;
  font-size: 18px;
  font-weight: 400px;
  color: #2b2b2b;
  padding-bottom: 20px;
}
.usage2 {
  margin-top: 10px;
  li {
    display: flex;
    justify-content: space-between;
    margin-top: 10px;
    border-bottom: 1px solid rgba(228, 228, 228, 1);
    padding-bottom: 10px;
    align-items: center;
  }
}
.usage2-div {
  margin-right: 20px;
  color: #dd2619;
  font-size: 20px;
}
.usage3 {
  text-align: center;
  margin-top: 30px;
}
.current-right {
  margin-left: 12px;
  text-align: left;
  width: 198px;
  margin-right: 29px;
  p {
    text-align: left;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #262626;
    margin-bottom: 10px;
  }
  span {
    color: #e2270b;
    font-size: 12px;
    border: 1px solid #dd2619;
    border-radius: 10px;
    padding: 2px 10px;
  }
}
.after {
  height: 12px;
  line-height: 12px;
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #717171;
}
.cumulative-savings div span.money {
  font-size: 12px;
  margin-right: 10px;
}
.recentdetails1-left {
  font-size: 14px;
  color: #333333;
  font-weight: 500;
  font-family: PingFangSC-Medium, PingFang SC;
}
.recentdetails1-right {
  cursor: pointer;
  font-size: 12px;
  font-weight: 400;
  font-family: PingFangSC-Regular, PingFang SC;
  color: #666666;
  .ivu-icon {
    color: #666666;
    margin-left: -5px;
  }
  &:hover {
    color: #dd2619;
    .ivu-icon {
      color: #dd2619;
    }
  }
}
.recent1 {
  height: 14px;
  line-height: 14px;
  font-size: 14px;
  color: #222222;
  font-weight: 500;
  font-family: PingFangSC-Medium, PingFang SC;
}
.recent2 {
  height: 12px;
  line-height: 12px;
  font-size: 12px;
  color: #999999;
  font-weight: 400;
  margin-top: 10px;
  font-family: PingFangSC-Regular, PingFang SC;
}
.current-span2 {
  color: #dd2619;
}
.usage1-span1 {
  margin-right: 20px;
}
</style>