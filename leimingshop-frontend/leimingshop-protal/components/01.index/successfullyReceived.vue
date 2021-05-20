<template>
  <div class="container">
    <div class="artImg"><img src="/img/01.index/Fill.png" alt="" /></div>
    <p class="artP1">恭喜您，领取成功</p>
    <p class="artP2">可以前往我的优惠券查看详情</p>
    <div class="artClose artCursor" @click="$emit('closeSuccessShow')">
      关闭
    </div>
    <div class="artCloseTime">{{ timeClose }}秒后自动关闭</div>
    <!-- </Modal> -->
  </div>
</template>
<script>
export default {
  name: "loginPopup",
  props: {
    topHeight: {
      //顶部显示高度
      type: Number,
      default: 100,
      required: false,
    },
    modal1: {
      //成功弹框控制器
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      timeClose: 5,
    };
  },
  watch: {
    modal1(newVal, oldVal) {
      if (newVal) {
        this.timeClose = 5;
        this.actClose();
      }
    },
  },
  mounted() {},
  destroyed() {},
  methods: {
    actClose() {
      this.timer = setInterval(() => {
        if (this.timeClose > 0) {
          this.timeClose--;
        } else {
          this.$emit("closeSuccessShow");
          clearInterval(this.timer);
          this.timer = null;
          //跳转的页面写在此处
          // this.$router.push({
          // 	path: ''
          // });
        }
      }, 1000);
    },
  },
};
</script>
<style lang="scss" scoped>
.artCursor {
  cursor: pointer;
}

.container {
  background-color: #ffffff;
}

.artImg {
  margin: 30px 0 20px 0;

  img {
    width: 60px;
    height: 60px;
  }
}

.artP1 {
  margin-bottom: 10px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 600;
  color: #222222;
  line-height: 14px;
}

.artP2 {
  margin-bottom: 20px;
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
  line-height: 12px;
}

.artClose {
  width: 80px;
  height: 30px;
  border-radius: 22px;
  border: 1px solid #cccccc;
  margin: 0 auto 30px auto;
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #666666;
  line-height: 30px;
}

.artCloseTime {
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #666666;
  line-height: 12px;
  text-align: left;
}
</style>
