<template>
  <div class="myDiv">
    <div class="usage">
      <div class="usage1">
        <div class="usage1-left">
          <Icon type="ios-calendar-outline" style="color:#333333;font-size:18px;"/>
          <!-- <span>{{currentDate}}</span> -->
          <span class="current-span1">当前积分：</span>
          <span class="current-span2">{{MyPointsDetailList.availablePoint | valueConver}}</span>
        </div>
        <div class="usage1-right">
          <span class="usage1-span1">获得：{{MyPointsDetailList.totalGet | valueConver}}</span>
          <span class="usage1-span2">使用：{{MyPointsDetailList.totalUsed | valueConver}}</span>
        </div>
      </div>
      <div class="usage2">
        <ul>
          <li v-for="(item,index) in detailedList" :key="index">
            <div>
              <p class="recent1">{{item.pointDesc}}</p>
              <p class="recent2">{{item.createDate}}</p>
            </div>
            <div class="usage2-div">
              <span v-if="item.pointType>=0">+</span>
              <span v-else>-</span>
              {{item.pointValue}}
            </div>
          </li>
        </ul>
      </div>
      <div class="usage3">
        <paging
          class="paging"
          :totalCount="total"
          :pageSize="pageSize"
          @handlePageChange="handlePageChange"
          v-if="detailedList.length != 0 && total > pageSize "
        />
        <div v-else style="height:60px;"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
import paging from "@/components/common/paging";
export default {
  components: {
    paging,
  },
  props: {},
  data() {
    return {};
  },
  watch: {},
  computed: {
    ...mapState("myPoints", [
      "MyPointsDetailList",
      "detailedList",
      "total",
      "pageSize",
    ]),
  },
  methods: {
    ...mapActions("myPoints", [
      "getPointsDetailsData",
      "getMyPointsDetailData",
    ]),
    handlePageChange(val) {
      this.getPointsDetailsData({
        page: val,
        limit: this.pageSize,
      });
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
// .myDiv {
// }
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
  font-size: 18px;
  color: #333333;
  // vertical-align: baseline;
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
  border: 2px solid rgba(228, 228, 228, 1);
  background: #ffffff;
  padding: 63px 0;
}
.currentpoints-left {
  width: 457px;
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
  justify-content: space-around;
  width: 100%;
  align-items: center;
}
.plist {
  text-align: center;
  font-size: 26px;
  color: #262626;
  font-weight: 600;
}
.plist-another {
  text-align: center;
  font-size: 26px;
  color: #dd2619;
  font-weight: 600;
}
.cumulative-savings {
  text-align: center;
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
    }
  }
}
.recentdetails {
  margin-top: 10px;
  background: #ffffff;
  padding: 20px 40px 0 20px;
}
.recentdetails1 {
  padding-bottom: 20px;
  display: flex;
  justify-content: space-between;
}
.recentdetails2 li {
  display: flex;
  justify-content: space-between;
  padding-top: 30px;
  border-bottom: 1px solid rgba(228, 228, 228, 1);
  padding-bottom: 30px;
  align-items: center;
}
.daylogin {
  margin-right: 20px;
  color: #dd2619;
  font-size: 20px;
}
.usage {
  background: #ffffff;
  padding: 20px 40px 0px 40px;
}
.usage1 {
  display: flex;
  justify-content: space-between;
  padding-bottom: 10px;
  padding-bottom: 20px;
  font-size: 12px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: bold;
  color: #222222;
}
.usage2 {
  // margin-top: 10px;
  li {
    // height: 77px;
    display: flex;
    justify-content: space-between;
    // margin-top: 10px;
    border-bottom: 1px solid #ebebeb;
    // padding-bottom: 10px;
    align-items: center;
    padding: 20px 0;
  }
}
.usage2-div {
  margin-right: 100px;
  font-family: PingFangSC-Regular,PingFang SC;
  color: #dd2619;
  font-size: 20px;
  span{
    margin-right: -5px;
  }
}
.usage3 {
  text-align: center;
  // margin-top: 30px;
}
.current-right {
  margin-left: 12px;
  text-align: left;
  p {
    font-size: 20px;
    color: #262626;
    font-weight: 500;
    text-align: left;
  }
  span {
    color: #dd2619;
    font-size: 12px;
    border: 1px solid #dd2619;
    border-radius: 10px;
    padding: 2px;
  }
}
.after {
  font-size: 14px;
  color: #717171;
}
.cumulative-savings div span.money {
  font-size: 12px;
}
.recentdetails1-left {
  font-size: 18px;
  color: #424242;
  font-weight: 500;
}
.recentdetails1-right {
  cursor: pointer;
  font-size: 14px;
  color: #666666;
  .ivu-icon {
    color: #666666;
  }
}
.recent1 {
  height: 14px;
  line-height: 14px;
  font-size: 14px;
  color: #222222; 
  font-family: PingFangSC-Medium, PingFang SC;;
  font-weight: 500;
}
.recent2 {
  height: 12px;
  line-height: 12px;
  font-size: 12px;
  color: #999999;
  font-family: PingFangSC-Regular,PingFang SC;
  font-weight: 400;
  margin-top: 10px;
  // margin-bottom: 20px;
}
.current-span2 {
  color: #dd2619;
}
.usage1-span1 {
  margin-right: 20px;
}
</style>