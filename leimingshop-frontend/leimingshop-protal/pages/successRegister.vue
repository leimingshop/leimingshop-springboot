<template>
  <div>
    <div class="home">
      <div class="home-head">
        <logoImg></logoImg>
        <router-link to="/" class="home-head-right">进入网站首页></router-link>
      </div>
    </div>
    <div class="main">
      <div class="content">
        <div>
          <div class="content-text">
            <img src="/img/09.login/register_success.png" />
            <span>恭喜注册成功</span>
          </div>
          <div class="content-text1"><span>请妥善保管您的账号信息</span></div>
        </div>
        <div>
          <a class="content-btn" @click="goPage('/login')">去登录</a>
        </div>
      </div>
    </div>
    <div class="content-footer">
      <div class="friend-link">
        <div class="friend-link-item">
          <ul>
            <li v-for="(link, index) in moreLink" :key="index">
              <span
                :class="[linkItemClass, { 'link-last-item': index === 9 }]"
                >{{ link }}</span
              >
            </li>
          </ul>
        </div>
      </div>
      <div class="copyright">
        <p>Copyright&nbsp;&nbsp;©2018&nbsp;&nbsp;北京雷铭智信科技有限公司 &nbsp; 本公司保留一切版权所有 &nbsp;|&nbsp;<a href="http://beian.miit.gov.cn/" target="_blank">京ICP备12000912号-1</a></p>
      </div>
    </div>
  </div>
</template>
<script>
import logoImg from "@/components/common/logoImg.vue";
export default {
  head() {
    return {
      title: "注册成功",
      meta: [
        {
          hid: "description",
          name: "description",
          content: "My custom description",
        },
      ],
    };
  },
  data() {
    return {
      linkItemClass: "link-item",
      moreLink: [
        "关于我们",
        "联系我们",
        "联系客服",
        "合作招商",
        "商家帮助",
        "营销中心",
        "销售联盟",
        "校园社区",
        "风险监测",
        "隐私政策",
      ],
    };
  },
  components: {
    logoImg,
  },
  methods: {
    goPage(path) {
      this.$router.push({
        path: path,
      });
    },
  },
};
</script>
<style lang="scss" scoped>
//顶部部分
.home {
  width: 100%;
  background: #fff;
  background: #ffffff;
  box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.1);

  .home-head {
    width: 1200px;
    height: 100px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    overflow: hidden;

    .home-head-left {
      width: 219px;
      height: 59px;
    }

    .home-head-right {
      margin: 62px 0 24px 0;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #666666;
      line-height: 16px;

      a {
        color: #dd2619;
      }
    }
  }
}

.main {
  .content {
    width: 979px;
    margin: 177px auto 0 auto;
    text-align: center;
    background: #ffffff;

    .content-text {
      line-height: 46px;
      font-size: 29px;
      font-weight: 500;
      color: #333333;
      img {
        width: 60px;
        height: 60px;
        margin: 0 auto 20px auto;
        display: block;
      }
      span {
        display: block;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: #222222;
        line-height: 14px;
      }
    }

    .content-text1 {
      margin: 10px auto 20px auto;
      font-size: 12px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      line-height: 12px;
    }

    .content-btn {
      width: 80px;
      height: 30px;
      margin: 0 6px;
      background: #ffffff;
      border: 1px solid #cccccc;
      border-radius: 15px;
      margin: 0 auto;
      display: block;
      font-size: 12px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #666666;
      line-height: 28px;
      &:hover {
        background: #dd2619;
        border: 0;
        color: #ffffff;
        line-height: 30px;
      }
    }
  }
}

.content-footer {
  width: 100%;
  height: 98px;
  background-color: #ffffff;
  border-top: 1px solid #cccccc;
  position: fixed;
  bottom: 0px;
  z-index: 999;
  overflow: hidden;

  .friend-link {
    display: flex;
    align-items: flex-end;
    width: 908px;
    height: 43px;
    margin: 0px auto;
    color: #666;
    /* //   border-top: 1px solid #b2dfdb; */
  }

  .friend-link-item {
    margin: 0px auto;
  }

  .friend-link-item ul {
    list-style: none;
  }

  .friend-link-item li {
    float: left;
  }

  .link-item {
    padding: 0px 15px;
    font-size: 13px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #222222;
    line-height: 13px;
    cursor: pointer;
  }

  .link-last-item {
    border: none;
  }

  .copyright {
    width: 100%;
    margin-top: 20px;
    font-size: 13px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #222222;
    line-height: 13px;
    text-align: center;
  }
   .copyright a {
      color: #222222;
    }
}
</style>
