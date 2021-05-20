<template>
  <div class="enjoygoodthings4 warp-con">
    <div class="enjoygoodthings4-item backgroundcolorFFF">
      <div class="enjoygoodthings4-item-left">
        <img src="/img/01.index/3.jpg" alt="" />
      </div>
      <div class="enjoygoodthings4-item-right">
        <div
          class="enjoygoodthings4-item-right-con"
          v-for="item in 3"
          :key="item"
        >
          <div>
            <p class="arttextoverflow">极有家</p>
            <p class="arttextoverflow">过我想要的生活</p>
          </div>
          <img src="/img/01.index/3.jpg" alt="" />
        </div>
      </div>
    </div>
    <div class="enjoygoodthings4-item backgroundcolorFFF">
      <div class="enjoygoodthings4-item-left">
        <img src="/img/01.index/3.jpg" alt="" />
      </div>
      <div class="enjoygoodthings4-item-right">
        <div
          class="enjoygoodthings4-item-right-con"
          v-for="item in 3"
          :key="item"
        >
          <div>
            <p class="arttextoverflow">极有家</p>
            <p class="arttextoverflow">过我想要的生活</p>
          </div>
          <img src="/img/01.index/3.jpg" alt="" />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "enjoyGoodThings3",
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
.enjoygoodthings4 {
  height: 420px;
  overflow: hidden;
  display: flex;
  .enjoygoodthings4-item {
    width: 594px;
    display: flex;
    margin-right: 13px;
    padding: 29px 34px 31px 30px;
    &:nth-of-type(2n) {
      margin-right: 0;
    }
    .enjoygoodthings4-item-left img {
      width: 270px;
      height: 360px;
      margin-right: 12px;
    }
    .enjoygoodthings4-item-right {
      width: 252px;
      .enjoygoodthings4-item-right-con {
        display: flex;
        height: 120px;
        div {
          width: 110px;
          margin-left: 27px;
        }
        div p:nth-of-type(1) {
          margin: 35px 0 14px 0;
          font-size: 20px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: rgba(51, 51, 51, 1);
          line-height: 20px;
        }
        div p:nth-of-type(2) {
          font-size: 12px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: rgba(153, 153, 153, 1);
          line-height: 20px;
        }
        img {
          margin-top: 11px;
          width: 113px;
          height: 95px;
        }
      }
    }
  }
}
</style>

