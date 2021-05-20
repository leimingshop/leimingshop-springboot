<template>
  <div class="logistics-wrapper">
    <div class="logistics-list">
      <div class="logistics-inner">
        <p class="p1">
          查看物流
          <span @click="confirm" style="cursor:pointer">x</span>
        </p>
        <div class="logistics-top">
          <div class="logistop-left">
            <img :src="$imgURL+logisticsList.goodsPicture" alt />
            <span>{{logisticsList.goodsNum}}件商品</span>
          </div>
          <div class="logistop-right">
            <p>
              配送企业：{{logisticsList.companyName}}
              <span v-show="!logisticsList.companyName">暂无</span>
            </p>
            <p>
              快递单号：{{logisticsList.nu}}
              <span v-show="!logisticsList.nu">暂无</span>
            </p>
            <p>
              联系电话：{{logisticsList.companyPhone}}
              <span v-show="!logisticsList.companyPhone">暂无</span>
            </p>
          </div>
        </div>
        <div class="bottom-wrapper">
          <ul class="logistics-bottom">
            <li v-for="(item,index) in logisticsList.data" :key="index">
              <div class="line"></div>
              <div class="li-left">
                <span class="circle"></span>
              </div>
              <div class="li-right">
                <p class="context">
                  <span>{{item.context}}</span>
                </p>
                <p class="time">{{item.time}}</p>
              </div>
            </li>
          </ul>
        </div>
        <div @click="confirm" class="confirm">确定</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {
      logisticsList: {},
      myTracksImgUrl: ""
    };
  },
  watch: {},
  computed: {},
  methods: {
    confirm() {
      this.$store.state.myOrders.logisticsFlag = false;
    }
  },
  created() {},
  mounted() {
    this.logisticsList = this.$store.state.myOrders.logisticsList;
  }
};
</script>
<style scoped lang="scss">
.logistics-wrapper {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  z-index: 1000;
}
.logistics-list {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #ffffff;
  border: 10px solid rgba(0, 0, 0, 0.2);
  border-radius: 10px;
}
.logistics-inner {
  border-radius: 10px;
  padding: 10px 60px 20px 60px;
  .p1 {
    font-size: 24px;
    color: #424242;
    font-weight: 500;
    text-align: center;
    position: relative;
    align-items: center;
    span {
      position: absolute;
      right: -30px;
      color: #9c9c9c;
      font-size: 24px;
    }
  }
  .logistics-top {
    width: 100%;
    background: rgba(248, 248, 248, 1);
    display: flex;
    padding: 15px 31px;
    align-items: center;
    margin-top: 10px;
    border: 1px solid #cccccc;
    border-bottom: 0;
    .logistop-left {
      width: 68px;
      height: 68px;
      position: relative;
      img {
        width: 68px;
        height: 68px;
      }
      span {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 1);
        opacity: 0.55;
        text-align: center;
        font-size: 12px;
        color: #ffffff;
      }
    }
    .logistop-right {
      margin-left: 20px;
      font-size: 15px;
      color: #4c4c4c;
    }
  }
}
.bottom-wrapper {
  padding: 20px 30px 20px 30px;
  border: 1px solid #cccccc;
}
.logistics-bottom {
  height: 322px;
  overflow: auto;
  padding-right: 20px;
  li {
    padding-bottom: 20px;
    display: flex;
    position: relative;
    .line {
      position: absolute;
      height: 100%;
      width: 1px;
      border-right: 1px solid #eeeeee;
      top: 8px;
      left: 2px;
      z-index: -1;
    }
    .li-left {
      width: 30px;
      .circle {
        width: 5px;
        height: 5px;
        border-radius: 50%;
        background: rgba(204, 204, 204, 1);
        display: block;
        margin-top: 8px;
      }
    }
    .li-right {
    }
  }
}
//滚动条的样式
::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 0;
}
::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 10px;
  height: 10px;
}
::-webkit-scrollbar-thumb {
  cursor: pointer;
  border-radius: 5px;
  background: rgba(0, 0, 0, 0.25);
}
.confirm {
  width: 96px;
  height: 34px;
  text-align: center;
  line-height: 34px;
  color: #ffffff;
  background: #dd2619;
  margin: 0 auto;
  margin-top: 20px;
}
.time {
  font-size: 13px;
  color: #999999;
  margin-top: 10px;
}
.context {
  font-size: 14px;
  color: #333333;
}
</style>
