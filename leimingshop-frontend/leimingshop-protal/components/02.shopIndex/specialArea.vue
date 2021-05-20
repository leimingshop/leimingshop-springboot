<template>
  <div class="specialArea warp-con">
    <div class="specialArea-item backgroundcolorFFF" v-for="item in 3">
      <img src="/img/01.index/3.jpg" alt="" />
      <div class="specialArea-con">
        <div class="specialArea-con-left">
          <p class="arttextoverflow">5个给春天的生活新提案</p>
          <p class="arttextoverflow">餐厨起居洗护好物</p>
          <p>
            <span><img src="/img/02.shopIndex/collection.png" alt="" />100</span
            ><span><img src="/img/02.shopIndex/look.png" alt="" />100</span
            ><span><img src="/img/02.shopIndex/chat.png" alt="" />100</span>
          </p>
        </div>
        <div class="specialArea-con-right">339.90元起</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "specialArea",
  props: {
    seckillGoodsData: {
      type: Object,
      required: true,
      default: {},
    },
    titleIsShow: {
      type: Boolean,
      required: false,
      default: true,
    },
  },
  data() {
    return {};
  },
  watch: {},
  computed: {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.arttextoverflow {
  white-space: nowrap;
  overflow: hidden;
  word-break: break-all;
  word-wrap: break-word;
  text-overflow: ellipsis;
  -o-text-overflow: ellipsis;
}
img {
  display: inline-block;
}
.warp-con {
  width: 1200px;
  margin: 0 auto;
}
.backgroundcolorFFF {
  background-color: #fff;
}
.con-left {
  width: 232px;
}
.con-right {
  flex-grow: 1;
}
.artCursor {
  cursor: pointer;
}
.specialArea {
  height: 344px;
  overflow: hidden;
  display: flex;
  .specialArea-item {
    margin-right: 11px;
    &:nth-of-type(3n) {
      margin-right: 0;
    }
    & > img {
      display: block;
      width: 100%;
      height: 230px;
    }
    .specialArea-con {
      padding: 20px 30px 20px;
      display: flex;
      justify-content: space-between;
      .specialArea-con-left {
        width: 200px;
        p:nth-of-type(1) {
          font-size: 16px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 600;
          color: rgba(51, 51, 51, 1);
          line-height: 16px;
        }
        p:nth-of-type(2) {
          margin: 10px 0 16px 0;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: rgba(153, 153, 153, 1);
          line-height: 14px;
        }
        p:nth-of-type(3) {
          span {
            display: inline-block;
            margin-right: 18px;
          }
          span img {
            float: left;
            margin-top: 3px;
            margin-right: 4px;
          }
        }
      }
      .specialArea-con-right {
        padding-top: 20px;
        font-size: 24px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: rgba(221, 38, 25, 1);
        line-height: 33px;
      }
    }
  }
}
</style>

