<template>
  <div class="promptPage" v-show="promptShow">
    <!-- 提交成功或失败的页面，提示语可配 -->
    <div class="popup-wrapper">
      <div class="poput">
        <div>
          <img
            v-show="successOrFail == 0"
            src="/img/06.personalCenter/fail.png"
            alt
            style="width: 36px"
          />
          <img
            v-show="successOrFail == 1"
            src="/img/06.personalCenter/success@1x.png"
            alt
            style="width: 36px"
          />
        </div>
        <p>{{ titpMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    promptShow: {
      type: Boolean,
      default: false,
    },
    successOrFail: {
      type: Number,
      default: 0, //0是失败 1是成功
    },
    titpMessage: {
      type: String,
      default: "我是提示语提示语", //成功、失败
    },
  },
  data() {
    return {};
  },
  watch: {},
  computed: {},
  methods: {},
  created() {},
  mounted() {},
};
</script>
<style scoped lang="scss">
.promptPage {
}
.popup-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(1, 1, 1, 0.13);
  .poput {
    padding: 0 30px;
    background: #ffffff;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border: 4px solid rgba(0, 0, 0, 0.2);
    border-radius: 6px;
    div {
      margin-top: 30px;
      text-align: center;
    }
    p {
      font-size: 22px;
      color: #333333;
      font-weight: 400px;
      text-align: center;
      padding-bottom: 30px;
      margin-top: 25px;
    }
  }
}
</style>