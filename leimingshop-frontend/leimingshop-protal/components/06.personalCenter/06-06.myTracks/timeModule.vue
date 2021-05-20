<template>
  <div>
    <div class="earlier">
      <div class="today-top">
        <div id="circle3" class="circle">
          <div id="circle4" class="circle"></div>
        </div>
        <span>{{timeType}}</span>
        <span style="margin-left:20px" @click.stop="isShowMask=true">删除</span>
      </div>
      <div class="today-bottom">
        <ul>
          <li
            v-for="(item,index) in transmittedData"
            :key="index"
            @mouseenter="mouseenter(index)"
            @mouseleave="mouseleave(index)"
            @click="goDetailPage()"
          >
            <Icon
              type="ios-close"
              size="24"
              :class="{on:index===indexes}"
              @click.stop="isShowMask1=true"
            />
            <div class="label">{{item.label}}</div>
            <img :src="item.imgUrl" alt />
            <p class="title">{{item.title}}</p>
            <p class="explain">{{item.explain}}</p>
            <p class="price">{{item.price}}</p>
            <div class="eval-sales">
              <div class="eval-sales-left">{{item.evaluate}}</div>
              <div class="eval-sales-right">销量：{{item.salesVolume}}件</div>
            </div>
            <div class="type-name">
              <div class="shop-type">{{item.shopType}}</div>
              <div>{{item.shopName}}</div>
            </div>
          </li>
          <div class="placeholder"></div>
        </ul>
      </div>
    </div>
    <!-- 删除某个时间组的弹窗 -->
    <div class="taskAll" v-show="isShowMask">
      <div class="taskAllContainer">
        <Icon type="ios-close" size="24" @click="isShowMask=false" />
        <p>您确定要删除{{timeType}}商品记录吗</p>
        <div class="button-group">
          <div class="button-left" @click="isShowMask=false">取消</div>
          <div class="button-right" @click="deleteGroup">确定</div>
        </div>
      </div>
    </div>
    <!-- 删除某个时间组的某个产品的弹窗 -->
    <div class="taskAll" v-if="isShowMask1">
      <div class="taskAllContainer">
        <Icon type="ios-close" size="24" @click="isShowMask1=false" />
        <p>您确定要删除{{transmittedData[indexes].label}}商品记录吗</p>
        <div class="button-group">
          <div class="button-left" @click="isShowMask1=false">取消</div>
          <div class="button-right" @click="deleteSingle">确定</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    timeType: {
      type: String,
      default: ""
    },
    transmittedData: {
      type: Array,
      default: ""
    }
  },
  data() {
    return {
      isShowMask: false,
      isShowMask1: false,
      todayDataLength: false,
      indexes: "" //经过某个商品的index
    };
  },
  watch: {},
  computed: {},
  methods: {
    deleteGroup() {
      this.$emit("deleteGroup", this.timeType);
    },
    deleteSingle() {
      this.transmittedData.splice(this.indexes, 1);
      this.isShowMask1 = false;
      this.indexes = "";
    },
    mouseenter(indexes) {
      //鼠标经过事件

      this.indexes = indexes;
    },
    mouseleave(indexes) {
      //鼠标离开事件
      if (this.isShowMask1) {
        return;
      }
      this.indexes = "";
    },
    goDetailPage() {
      //跳转到商品详情页页面
      var _this = this;
      _this.$router.push({
        path: "/productDetails"
        // query: {
        //   DestructionStatue: 1
        // }
      });
    }
  },
  created() {},
  mounted() {}
};
</script>
<style lang="scss" scoped>
.earlier {
  margin-top: -5px;
}
.today-top {
  display: flex;
  align-items: center;
}
#circle3 {
  width: 20px;
  height: 20px;
  background: #999999;
  position: relative;
}

#circle4 {
  width: 10px;
  height: 10px;
  background: #d7d7d7;
  position: absolute;
  top: 50%;
  display: block;
  left: 50%;
  transform: translate(-50%, -50%);
}

.circle {
  border-radius: 50%;
  display: inline-block;
  margin-right: 20px;
}
.today-bottom {
  ul {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    padding: 40px 30px;
    border-left: 2px solid #f4f4f4;
    margin-left: 8px;
    margin-top: -5px;
    li {
      width: 240px;
      border: 1px solid #999999;
      text-align: center;
      padding-bottom: 10px;
      position: relative;
      i {
        position: absolute;
        right: 0;
        top: 0;
        background: #9999;
        display: none;
      }
      i.on {
        display: block;
      }
      img {
        width: 140px;
        height: 100px;
        margin-top: 30px;
      }
    }
  }
}
.eval-sales {
  display: flex;
  justify-content: space-between;
  padding: 0 10px;
}
.type-name {
  display: flex;
  justify-content: space-between;
  padding: 0 10px;
}
.shop-type {
  background: red;
  padding: 2px 10px;
  border-radius: 16px;
  color: #fff;
}
.taskAll {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
}
.taskAllContainer {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #fff;
  width: 340px;
  padding: 20px;
  i {
    float: right;
    cursor: pointer;
  }
  p {
    margin-top: 30px;
    text-align: center;
  }
}
.button-group {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
.button-left {
  width: 120px;
  background: #f5f5f5;
  text-align: center;
  padding: 10px 0;
}
.button-right {
  width: 120px;
  background: #f36d69;
  text-align: center;
  padding: 10px 0;
  margin-left: 20px;
  color: #fff;
  cursor: pointer;
}
.label {
  background: red;
  width: 70px;
  margin: 0 auto;
}
.placeholder {
  width: 280px;
  height: 0px;
}
</style>