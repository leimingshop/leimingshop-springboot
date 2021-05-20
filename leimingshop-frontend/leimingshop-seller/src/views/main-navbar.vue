<template>
  <nav class="aui-navbar" :class="`aui-navbar--${$store.state.navbarLayoutType}`">
    <div class="aui-navbar__header">
      <!-- <h1 class="aui-navbar__brand" @click="$router.push({ name: 'home' })">
        <a class="aui-navbar__brand-lg" href="javascript:;">{{ $t('brand.lg') }}</a>
        <a class="aui-navbar__brand-mini" href="javascript:;">{{ $t('brand.mini') }}</a>
      </h1>-->
      <img
        style="height:58px;width:204px;margin-top:11px;margin-bottom: 11px"
        src="@/assets/images/logo.png"
        alt=""
      >
    </div>
    <div class="aui-navbar__body arrow">
      <!-- <el-menu class="aui-navbar__menu mr-auto" mode="horizontal">
        <el-menu-item index="1" @click="$store.state.sidebarFold = !$store.state.sidebarFold">
          <svg class="icon-svg aui-navbar__icon-menu aui-navbar__icon-menu--switch" aria-hidden="true"><use xlink:href="#icon-outdent"></use></svg>
      </el-menu-item>-->
      <!-- <el-menu-item index="2" @click="refresh()">
          <svg class="icon-svg aui-navbar__icon-menu aui-navbar__icon-menu--refresh" aria-hidden="true"><use xlink:href="#icon-sync"></use></svg>
      </el-menu-item>-->
      <!-- </el-menu> -->
      <!-- 一级导航栏 -->
      <el-menu
        class="site-navbar__menu mr-auto HeaderBodyStyle artmenuleft"
        mode="horizontal"
        active-text-color="#1890FF"
        :default-active="activeName"
      >
        <!-- <el-menu-item index="home" @click="chooseItembtn('home')">
          <div class="el-menu-item_content">
            <svg slot="label" class="icon-svg aui-content--tabs-icon-nav" aria-hidden="true">
              <use xlink:href="#icon-home"></use>
            </svg>
            <span>首页</span>
          </div>
        </el-menu-item> -->
        <el-menu-item
          :index="'nav'+item.url"
          v-for="(item,index) in parentArr"
          @click="chooseItembtn(item)"
          :key="index"
        >
          <div class="el-menu-item_content">
            <svg class="icon-svg aui-sidebar__menu-icon" aria-hidden="true">
              <use :xlink:href="`#${item.icon}`"></use>
            </svg>
            <span>{{item.name}}</span>
          </div>
        </el-menu-item>
      </el-menu>
      <el-menu class="aui-navbar__menu aui-navbar__menu_right artmenuright" mode="horizontal">
        <!-- 语言切换 -->
        <!-- <el-menu-item index="1">
          <el-dropdown placement="bottom" :show-timeout="0">
            <el-button size="mini">{{ $t('_lang') }}</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="(val, key) in i18nMessages" :key="key" @click.native="$i18n.locale = key">{{ val._lang }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>-->
        <!-- 跳转链接 -->
        <!-- <el-menu-item index="2">
          <a href="//www.leimingshop.io/" target="_blank">
            <svg class="icon-svg aui-navbar__icon-menu" aria-hidden="true"><use xlink:href="#icon-earth"></use></svg>
          </a>
        </el-menu-item>-->
        <!-- 全屏 -->
        <!-- <el-menu-item index="3" @click="fullscreenHandle()">
          <svg class="icon-svg aui-navbar__icon-menu" aria-hidden="true"><use xlink:href="#icon-fullscreen"></use></svg>
        </el-menu-item>-->
        <el-menu-item index="2">
          <!-- trigger="click" -->
          <el-dropdown :show-timeout="0" placement="bottom" trigger="click">
            <span class="el-dropdown-link">
              <img
                style="width:20px;height:20px;"
                src="@/assets/images/yunyin.png"
              >
            </span>
            <el-dropdown-menu slot="dropdown" class="textCenter">
              <el-dropdown-item class="divided" >
                <div @click="toPc()">
                  PC店铺首页
                </div>

              </el-dropdown-item>
              <!-- <el-dropdown-item class="divided">平台自营</el-dropdown-item> -->
              <el-dropdown-item class="divided dimensionalCodeWrap" v-if="QRCode">
                <span>h5主页</span>
                <div class="dimensionalCodeContent">
                  <img
                    class="dimensionalCode"
                    :src="QRCode | filterImgUrl"
                    alt=""
                  >
                  <!-- <img class="dimensionalCodeLogo" src="~@/assets/img/avatar.png"> -->
                </div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
        <el-menu-item index="3">
          <!--  trigger="click" -->
          <el-dropdown :show-timeout="0" placement="bottom">
            <span class="el-dropdown-link">
              <img
                style="width:20px;height:20px;"
                src="@/assets/images/message.png"
              >
            </span>
            <el-dropdown-menu slot="dropdown" class="messageBox">
              <div class="messageTop">消息通知</div>

              <el-dropdown-item class="messageItem" v-for="(item,index) in messageList" v-if="item.count" :key="index">
                  <div  @click="goRouter(item.path,item.queryObj)">
                      <div class="messageItemTitle" :class="item.mainColor">{{item.title}}</div>
                      <div class="messageItemContent">{{item.count}}{{item.content}}</div>
                  </div>
              </el-dropdown-item>

            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
        <el-menu-item index="4" class="aui-navbar__avatar">
          <el-dropdown placement="bottom" :show-timeout="0">
            <span class="el-dropdown-link">
              <img style="width:40px;height:40px;" src="~@/assets/img/logo300.png" v-if="!$store.state.user.logo">
              <img style="width:40px;height:40px;" :src="$imgDomain + $store.state.user.logo" v-else>
              <span>{{ $store.state.user.name }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown" class="textCenter">
              <el-dropdown-item
                @click.native="updatePasswordHandle()"
              >{{ $t('updatePassword.title') }}</el-dropdown-item>
              <el-dropdown-item @click.native="logoutHandle()">{{ $t('logout') }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </nav>
</template>

<script>
import { messages } from "@/i18n";
import screenfull from "screenfull";
import UpdatePassword from "./main-navbar-update-password";
import { clearLoginInfo } from "@/utils";
import { messageList }  from "@/api/api.js"
export default {
  inject: ["refresh"],
  data() {
    return {
      i18nMessages: messages,
      updatePassowrdVisible: false,
      activeName: "home",
      firstin: false,
      storeId:this.$store.state.user.storeId,
      QRCode:this.$store.state.user.qrCode,
      messageList:[
        {title:'商品审核通过',count:0,content:'件商品已审核通过',mainColor:"cyanColor",path:'mggoods-list',
        queryObj:[{key:'activeName',value:'not'}] },

        {title:'退货申请提醒',count:0,content:'个退货申请需审核',mainColor:"redColor",path:'mgafter-aftersalelog',
        queryObj:[{key:'aftersaleType',value:0},{key:'result',value:3}] },

        {title:'商品审核拒绝',count:0,content:'件商品审核拒绝',mainColor:"greenColor",path:'mggoods-examine',
        queryObj:[{key:'goodsStatus',value:'20'}] },

        {title:'换货申请提醒',count:0,content:'个换货申请需审核',mainColor:"blackColor",path:'mgafter-aftersalelog',
        queryObj:[{key:'aftersaleType',value:1},{key:'result',value:3}] },

        {title:'库存预警提醒',count:0,content:'个商品你已到达库存预警值',mainColor:"cyanColor",path:'mggoods-stock',
        queryObj:[{key:'message',value:1}]},

        {title:'平台私信提醒',count:0,content:'条平台私信待查看',mainColor:"redColor",path:'content-message-index',
         queryObj:[{key:'messageType',value:'0'},{key:'activeName2',value:'0'}]},

        {title:'系统通知提醒',count:0,content:'条站内信待查看',mainColor:"greenColor",path:'content-message-index',
         queryObj:[{key:'messageType',value:'1'},{key:'status',value:'0'}]},
      ]
    };
  },
  props: ["parentArr"],
  components: {
    UpdatePassword
  },
  // 监听,当路由发生变化的时候执行
  watch: {
    // $route(to, from) {
    //   this.defaultTabhHandle();
    // }
  },
  mounted() {
    this.defaultTabhHandle();
    // 获取消息列表
    this.getMessageList();
  },
  methods: {
  	//刷新也会触发
    chooseItem(row) {
      if (row === "home") {
        this.$emit("setParentItem", { children: [] });
        this.$router.push({ name: "home" });
      } else {
        this.$emit("setParentItem", row);
      }
    },
    //只是点击顶部导航触发的
    chooseItembtn(row){
    	if (row === "home") {
        this.$emit("setParentItem", { children: [] });
        this.$router.push({ name: "home" });
        return;   //home  下没有children  报错
      } else {
        this.$emit("setParentItem", row);
      }
      this.getlastchildren(row);
    },
    toPc(){
      if(this.storeId){
          window.open(window.SITE_CONFIG['pcUrl']+'#/shopIndex/?storeId='+this.storeId)
      }else{
        window.open(window.SITE_CONFIG['pcUrl'])
      }
       
    },
    getlastchildren(row){
      if(row.children){
        if(row.children.length != 0){
          this.getlastchildren(row.children[0]);
        }
        // else{
        //   let newurl =  row.url.replace(/\//g,"-")
        //   this.$router.push({	name: newurl })
        // }
      }else{
        let newurl =  row.url.replace(/\//g,"-")
        this.$router.push({	name: newurl })
      }
    },

    // 全屏
    fullscreenHandle() {
      if (!screenfull.enabled) {
        return this.$message({
          message: this.$t("fullscreen.prompt"),
          type: "warning",
          duration: 500
        });
      }
      screenfull.toggle();
    },
    // 修改密码
    updatePasswordHandle() {
      this.updatePassowrdVisible = true;
      this.$nextTick(() => {
        this.$refs.updatePassowrd.init();
      });
    },
    // 退出
    logoutHandle() {
      this.$confirm(
        this.$t("prompt.info", { handle: this.$t("logout") }),
        this.$t("prompt.title"),
        {
          confirmButtonText: this.$t("confirm"),
          cancelButtonText: this.$t("cancel"),
          type: "warning"
        }
      )
        .then(() => {
          this.$http
            .post("/seller-api/logout")
            .then(({ data: res }) => {
              if (res.code !== 200) {
                return this.$message.error(res.msg);
              }
              clearLoginInfo();
              this.$router.push({ name: "login" });
              window.location.reload()
            })
            .catch(() => {});
        })
        .catch(() => {});
    },
    // 控制顶部导航高亮显示
    defaultTabhHandle() {
       var hash = window.location.hash;
      // if (/#\/home/.test(hash)) {
      //   // this.activeName = "home";
      //   // this.chooseItem("home");
      // } else {
      
        this.activeName ="nav"+hash.replace("#/","").split("-")[0];
        this.refreshAside(this.activeName);
      // }
      // var url = window.location.href;
      // if (/home/.test(url)) {
      //   // this.activeName = "home";
      //   // this.chooseItem("home");
      // } else if (/mggoods-/.test(url)) {
      //   this.activeName = "nav0";
      //   this.refreshAside(0);
      // } else if (/mgorder-order/.test(url) ) {
      //   this.activeName = "nav1";
      //   this.refreshAside(1);
      // } else if (/mgvip-/.test(url)) {
      //   this.activeName = "nav2";
      //   this.refreshAside(2);
      // } else if (/sys-/.test(url)) {
      //   this.activeName = "nav9";
      //   this.refreshAside(9);
      // } else if (/store-/.test(url)) {
      //   this.activeName = "nav3";
      //   this.refreshAside(3);
      // } else if (/mgoperative-/.test(url)) {
      //   this.activeName = "nav4";
      //   this.refreshAside(4);
      // } else if (/mgfinance/.test(url)) {
      //   this.activeName = "nav5";
      //   this.refreshAside(5);
      // } else if (/content/.test(url)) {
      //   this.activeName = "nav6";
      //   this.refreshAside(6);
      // } else if (/websitesetup-/.test(url)) {
      //   this.activeName = "nav7";
      //   this.refreshAside(7);
      // }
    },
    // refreshAside(index) {
    //   setTimeout(() => {
    //     this.chooseItem(this.parentArr[index]);
    //   }, 0);
    // },
    refreshAside(index) {
      setTimeout(() => {
        // 登录进来，默认选选中第一级第一个
          if(window.isloaingEntry){
              index = "nav"+window.SITE_CONFIG['dynamicMenuRoutes'][0].name.split("-")[0];
          }
          for(var i=0;i < this.parentArr.length;i++){
               if("nav"+this.parentArr[i].url == index || "navpersonal" == index || "navstorehome" == index){
                   console.log("匹配到了",this.parentArr[i].url,index);
                    this.chooseItem(this.parentArr[i]);
                  return;
              }
          }
      }, 10);
    },
    // 获取消息列表
    getMessageList(){
      messageList().then((res)=>{
          if(res.code==200){
              this.messageList[0].count = res.data.goodsAuditCount  // 商品审核数量
              this.messageList[1].count = res.data.saleReturnCount  // 退货申请数量
              this.messageList[2].count = res.data.goodsAuditRepulseCount  // 商品审核拒绝数量
              this.messageList[3].count = res.data.saleBarterCount  // 换货申请数量
              this.messageList[4].count = res.data.storageWarnCount  // 库存预警提醒
              this.messageList[5].count = res.data.privateMessageCount  // 平台私信提醒数量
              this.messageList[6].count = res.data.sysMessageCount  // 系统消息提醒数量
          }
      })
    },
    // 跳转路由
    goRouter(argu,queryObj){
      console.log(queryObj);
      if(!queryObj){
        this.$router.push({
          path: argu
        })
      }else{
        var obj = {}
        queryObj.forEach((item,index)=>{
            obj[item.key] = item.value
        })
        console.log(obj);
        this.$router.push({
          path: argu,
          query:obj
        })
      }

    },
  }
};
</script>
<style lang="scss" scoped>
@import "@/element-ui/theme-variables.scss";
.artmenuleft{
  /*width: 78%;*/
	/*display: flex;
	justify-content: space-between;*/
}
.artmenuright{
	/*width: 20%;*/
}
.arrow {
  ul {
    /*display: flex;*/
		justify-content: space-around;
    border-bottom: solid 0px #e6e6e6;
    li {
      color: white !important;
    }
  }
}
.aui-sidebar--fold {
  .aui-navbar__header {
    //  width: 64px;
  }
}

.aui-navbar{
  height: 80px;
}



.aui-navbar__header {
  width: 204px;
  height: 100%;
}
.aui-navbar__body {
  background: transparent !important;
}
.HeaderBodyStyle {
  border-bottom: solid 0px #e6e6e6;
  background: transparent;
  color: white;
  .el-menu-item {
    /* color: white; */
    width: 85px;
    height: 80px !important;
    // line-height: 100px !important;
    line-height: normal !important;
    padding: 0px;
    text-align: center;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    border-bottom: 0px solid transparent !important;
    .el-menu-item_content {
      display: flex;
      justify-content: space-between;
      overflow: hidden;
      align-items: center;
      flex-direction: column;
      height: 45px;
      img,
      svg {
        width: 22px !important;
        height: 22px !important;
        margin-right: 0px !important;
      }
      span {
        font-size: 16px;
        font-weight: 400;
      }
    }
  }
  .el-menu-item:hover {
    /* color: #3b3b3c !important; */
    // color: white !important;
    color: white !important;
    opacity: 0.7 !important;
    background-color: transparent !important;
  }
  .el-menu-item.is-active {
    color: white !important;
    opacity: 1 !important;
    border-bottom: 0px solid transparent !important;
    // background-color: rgba(15, 218, 179, 0.5) !important;
    background-color: rgba(50,65,148,1) !important;
  }
}

.aui-navbar__menu_right {
  width: 236px;
 	padding-right: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  img {
    vertical-align: middle;
  }
  .el-menu-item {
    color: white;
  }
  .el-menu-item:hover {
    background-color: transparent;
  }
  .el-dropdown,
  .el-icon-arrow-down {
    color: white;
    &:hover {
      color: white;
    }
  }
}

.el-dropdown-menu__item {
  color: #666666;
  &:hover {
    border-left: 4px solid $--color-primary;
    padding-left: 16px;
    color: #333333;
    background: #f5f5f5;
  }
}

.textCenter {
  text-align: center;
}
.divided {
  border-top: 1px solid rgba(245, 245, 245, 1);
}
.dimensionalCodeWrap {
  width: 156px;
  height: 133.6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  .dimensionalCodeContent {
    background-size: 70px 70px;
    position: relative;
    width: 70px;
    height: 70px;
    line-height: 70px;
    .dimensionalCode {
      width: 100%;
      height: 100%;
    }
    .dimensionalCodeLogo {
      width: 25px;
      height: 25px;
      position: absolute;
      top: 22.5px;
      right: 22.5px;
    }
  }
}

.messageBox {
  .messageTop {
    text-align: center;
    color: #666666ff;
    height: 30px;
    line-height: 20px;
  }
  .messageItem {
    width: 156px;
    height: 60px;
    border-top: 1px solid rgba(245, 245, 245, 1);
    display: flex;
    flex-direction: column;
    justify-content: center;
    .messageItemTitle {
      font-size: 14px;
      height: 20px;
      line-height: 20px;
    }
    .messageItemContent {
      width: 129px;
      font-size: 12px;
      height: 20px;
      line-height: 20px;
      color: #999999;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}

.cyanColor {
  color: #3F51AF;
}
.redColor {
  color: #d0051e;
}
.greenColor {
  color: #34ad4c;
}
.blackColor {
  color: #333333;
}
</style>
