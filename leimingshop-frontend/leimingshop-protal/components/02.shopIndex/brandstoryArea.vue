<template>
  <div class="brandstoryArea warp-con">
    <div
      class="brandstoryArea-item backgroundcolorFFF"
      v-for="item in 3"
      :key="item"
    >
      <img src="/img/01.index/3.jpg" alt="" />
      <div class="brandstoryArea-con">
        <div class="brandstoryArea-con-left">
          <p class="arttextoverflow">5个给春天的生活新提案</p>
          <p class="arttextoverflow">餐厨起居洗护好物</p>
        </div>
        <div class="brandstoryArea-con-right">339.90元起</div>
      </div>
      <div class="brandstoryArea-item-text artMultilineTextOverflow">
        从小我就看我奶奶把用从小我就看我奶奶把用从小我就看我奶奶把用从小我就看我奶奶把用完的超市塑料袋整整齐齐叠放好，等以后需要再拿出来用。
        现在奶奶已经不在了，塑料袋也要收费了。
        奶奶不紧不慢，把袋子认真折好的场景，一直在脑海里挥之不去。
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "brandstoryArea",
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
.artMultilineTextOverflow {
  overflow: hidden;
  word-break: break-all;
  word-wrap: break-word;
  text-overflow: ellipsis;
  -o-text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
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
.brandstoryArea {
  height: 410px;
  overflow: hidden;
  display: flex;
  .brandstoryArea-item {
    margin-right: 11px;
    &:nth-of-type(3n) {
      margin-right: 0;
    }
    & > img {
      display: block;
      width: 100%;
      height: 230px;
    }
    .brandstoryArea-con {
      padding: 20px 30px 14px;
      display: flex;
      justify-content: space-between;
      .brandstoryArea-con-left {
        width: 200px;
        p:nth-of-type(1) {
          font-size: 16px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 600;
          color: rgba(51, 51, 51, 1);
          line-height: 16px;
        }
        p:nth-of-type(2) {
          margin: 10px 0 0 0;
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
        }
      }
      .brandstoryArea-con-right {
        font-size: 24px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: rgba(221, 38, 25, 1);
        line-height: 33px;
      }
    }
    .brandstoryArea-item-text {
      width: 392px;
      padding: 0 30px;
    }
  }
}
</style>

