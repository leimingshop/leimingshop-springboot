<template>
  <div style="width: 100%; background: #eee">
    <search-part-white></search-part-white>
    <div class="base">
      <div class="personal-center-base">
        <!-- 面包屑 -->
        <breadcrumb1 :list="breadCrumbList" />
        <!-- 左侧栏和右侧栏 -->
        <div class="person-center-content">
          <div class="person-center-left">
            <!-- active-name="1-2" :open-names="['1']"  :open-names="openNames" @on-select="handleMenuSelect" -->
            <Menu ref="menus" width="100%" accordion>
              <Submenu
                v-for="(ITEM, INDEX) in classTreeList"
                :key="ITEM.acName"
                :name="`${INDEX}`"
              >
                <template slot="title">
                  <span class="select">
                    <img :src="'/img/06.personalCenter/helpcenter.png'" alt />
                  </span>
                  <span :title="ITEM.acName">{{ ITEM.acName }}</span>
                </template>

                <MenuItem
                  v-for="(item, index) in ITEM.children"
                  :key="item.id"
                  :name="`${INDEX}-${index}`"
                  :class="{ active: selectVal == item.id }"
                >
                  <span :title="item.acName" @click="change(item.id)">{{
                    item.acName
                  }}</span>
                </MenuItem>
              </Submenu>
            </Menu>
          </div>

          <!-- 子分类集合下面的列表 -->
          <div class="person-center-right">
            <div class="system">
              <div
                class="systemContent"
                v-for="(item1, index1) in myHelpList"
                :key="index1"
                v-if="
                  item1.acId !== '' &&
                  item1.acName !== '' &&
                  item1.articleTitle !== '' &&
                  item1.id !== ''
                "
              >
                <div class="systemContent-top" style="margin: 14px 0px 14px">
                  <div style="display: flex">
                    <p class="systemContent-notice">{{ item1.articleTitle }}</p>
                  </div>
                </div>
                <div class="systemContent-top">
                  <div class="systemContent-time" style="width: 726px">
                    <span v-html="item1.articleContent"></span>
                  </div>
                  <div class="systemContent-time1">
                    <span @click="modalDisplay(item1.id)">查看详情></span>
                  </div>
                </div>
              </div>

              <!-- 分页 -->
              <paging
                class="paging"
                :totalCount="totalCount"
                :pageNo="page"
                :current="page"
                :pageSize="limit"
                @handlePageChange="handlePageChange"
                v-if="myHelpList.length !== 0 && totalCount > limit"
              />

              <div v-else style="height: 60px"></div>
              <!-- 弹出模态框 -->
              <Modal v-model="modal" @on-cancel="cancel">
                <p slot="header">
                  <span>{{ itemData.articleTitle }}</span>
                </p>
                <div class="header-top">
                  <p v-html="itemData.articleContent"></p>
                </div>
                <div slot="footer">
                  <Button
                    type="error"
                    size="large"
                    long
                    @click="sure"
                    class="systemContent-btn"
                    >返回</Button
                  >
                </div>
              </Modal>
            </div>
            <!-- <threedetails :acId='acId'></threedetails> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getClassTreeList,
  getMyHelpList,
  getquestionDetails,
} from "@/api/api_06-14.helpCenter.js";

import paging from "@/components/common/paging";
import searchPartWhite from "@/components/common/searchPartWhite.vue";
import breadcrumb1 from "@/components/common/breadcrumb1.vue";

export default {
  name: "helpCenter",
  head() {
    return {
      title: "帮助中心问题分类",
      meta: [
        {
          hid: "description",
          name: "description",
          content: "My custom description",
        },
      ],
    };
  },
  components: {
    searchPartWhite,
    paging,
    breadcrumb1,
  },
  data() {
    return {
      breadCrumbList: Object.freeze([
        {
          title: "首页",
          toPath: "/",
        },
        {
          title: "帮助中心",
          toPath: "",
        },
      ]),
      classTreeList: [], // 分类树集合
      // 分类下的问题列表
      myHelpList: [
        {
          acId: "",
          acName: "",
          articleTitle: "",
          articleUrl: "",
          createDate: "",
          id: "",
          updateDate: "",
        },
      ],
      // 查看详情列表
      itemData: {
        acCode: 3,
        acId: "",
        articleContent: "",
        articleTitle: "",
      },
      modal: false,
      listLoading: false, //加载左侧是否显示loading
      pageSize: 10,
      page: 1, //当前页码
      limit: 10, //每页显示记录条数
      totalCount: 0, //当前数据总条数
      acId: "", //默认acId为空，用来接收父组件传递过来的id
      selectVal: "", // 菜单选中的id
      openNames: [], // 激活菜单的 name 值
    };
  },
  created() {},

  computed: {},

  watch: {
    page: {
      immediate: true,
      handler(newVal, oldVal) {
        if (newVal) {
          this.page = parseInt(newVal);
        }
      },
    },

    openNames: {
      immediate: true,
      async handler(newVal, oldVal) {
        await this.$nextTick();
        this.$refs.menus.updateOpened();
        this.$refs.menus.updateActiveName();
      },
    },
  },

  mounted() {
    this.getClassTreeListData();
  },

  methods: {
    // 分类树接口数据
    getClassTreeListData() {
      getClassTreeList()
        .then((res) => {
          if (res && res.code == 200) {
            this.classTreeList = res.data;
            //默认使用子分类第一个children的id请求接口
            if (this.classTreeList[this.$route.query.parentId].children == null)
              return;
            this.acId = this.classTreeList[
              this.$route.query.parentId
            ].children[0].id;

            this.getMyHelpListData(this.page, this.limit, this.acId);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    // 默认选中
    handleMenuSelect(name) {
      console.log(name);
      return;
      let tempArr = name.split("-");
      // console.log(tempArr)
      let tempStr = this.classTreeList[tempArr[0]];
      // ['children'][tempArr[1]]['id']

      // 判断取消选中，暂时关闭该功能
      // this.selectVal = this.selectVal == tempStr ? '' : tempStr
      // this.selectVal = tempStr
    },

    // 获取分类下的问题列表
    getMyHelpListData(page, limit, acId) {
      var obj = {
        page: page,
        limit: limit,
        acId: this.acId, //这里的acId传的是子分类下面的第一个
        // acId: acId    //这里传的也是子分类下面的第一个id
      };
      getMyHelpList(obj)
        .then((res) => {
          if (res && res.code == 200) {
            this.myHelpList = res.data.list;
            this.totalCount = res.data.total;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    // 查看详情模态框
    modalDisplay(id) {
      this.modal = true;
      // 根据接口数据只能传字符串id,不能使用对象({id})这种形式
      getquestionDetails(id)
        .then((res) => {
          if (res && res.code == 200) {
            this.itemData = res.data;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    // 返回
    sure() {
      this.modal = false;
      this.itemData = {
        acId: "",
        articleContent: "",
        articleTitle: "",
      };
      this.getMyHelpListData(this.page, this.limit);
    },

    // 关闭
    cancel() {
      this.getMyHelpListData(this.page, this.limit, this.acId);
    },

    // 切换分页
    handlePageChange(val) {
      this.page = val;
      this.getMyHelpListData(this.page, this.limit, this.acId);
    },

    // 通过切换,来获取对应id
    change(id) {
      this.acId = id;
      this.page = 1;
      this.getMyHelpListData(this.page, this.limit, this.acId);
    },
  },
};
</script>
<style lang="scss" scoped>
a {
  text-decoration: none;
  color: #333;
  font-size: 13px;
}

h2 {
  height: 24px;
  font-size: 16px;
  font-weight: 400;
  color: rgba(51, 51, 51, 1);
}

.shouye:hover {
  color: #e2270b;
}

.helpcenterProblem:hover {
  color: #e2270b;
}

.base {
  background: #f6f6f6;
}

.personal-center-base {
  width: 1200px;
  margin: 0 auto;
  padding-bottom: 80px;

  .personal-center-nav {
    padding: 15px 0;
    font-size: 13px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: rgba(51, 51, 51, 1);
    .shouye,
    .helpcenterProblem {
      font-size: 13px;
      cursor: pointer;
    }
  }

  .person-center-content {
    display: flex;
    justify-content: space-between;
  }
}

.person-center-left {
  width: 232px;
  background: rgba(255, 255, 255, 1);
}

.person-center-right {
  width: 948px;
  background: rgba(255, 255, 255, 1);

  .instructions {
    height: 64px;
    line-height: 64px;
    font-size: 16px;
    font-weight: 500;
    color: rgba(51, 51, 51, 1);
    margin-left: 48px;
  }

  .line {
    width: 100%;
    height: 1px;
    background: rgba(221, 38, 25, 1);
  }

  .couponIntroduce {
    margin: 44px 0px 70px 50px;

    .getCoupon {
      p {
        margin-top: 5px;
        font-size: 14px;
        font-weight: 400;
        color: rgba(102, 102, 102, 1);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .couponUsed {
      margin-top: 18px;
      width: 662px;
      .couponUse {
        p {
          margin-top: 5px;
          font-size: 14px;
          font-weight: 400;
          color: rgba(102, 102, 102, 1);
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }

    .invalid {
      width: 739px;
      margin-top: 18px;
      p {
        margin-top: 5px;
        font-size: 14px;
        font-weight: 400;
        color: rgba(102, 102, 102, 1);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}

.list-head {
  font-size: 16px;
  padding: 30px 0 19px 42px;
  font-weight: 500;
  color: rgba(34, 34, 34, 1);

  img {
    vertical-align: middle;
  }

  /deep/ .ivu-icon {
    font-size: 18px;
    color: #666666;
    padding-left: 15px;
  }
}

.detail-list {
  li {
    cursor: pointer;
    font-size: 14px;
    padding: 15px 0 15px 0;
    font-weight: 400;
    color: rgba(102, 102, 102, 1);

    span {
      width: 100%;
      display: inline-block;
      padding-left: 67px;
    }

    span.on {
      border-left: 3px solid red;
      padding-left: 57px;
    }
  }

  li.on {
    font-size: 14px;
    font-weight: 400;
    color: rgba(221, 38, 25, 1);
    background: #fef8f8;
  }
}

/deep/
  .ivu-menu-light.ivu-menu-vertical
  .ivu-menu-item-active:not(.ivu-menu-submenu):after {
  left: 0;
}

/deep/ .ivu-menu-submenu-title {
  padding: 30px 0 20px 42px;
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 600;
  color: rgba(34, 34, 34, 1);
  display: flex;
  align-items: center;

  .select {
    display: inline-block;
    width: 16px;
    height: 16px;
    margin-right: 10px;

    img {
      width: 100%;
      height: 100%;
      vertical-align: text-top;
    }
  }

  .ivu-menu-submenu-title-icon {
    top: 60%;
  }
}

/deep/ .ivu-menu-submenu-title:hover {
  color: rgba(34, 34, 34, 1);
}

/deep/ .ivu-menu-item {
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 600;
  padding-left: 0 !important;
  width: 100%;
  padding: 14px 0;

  span {
    display: inline-block;
    width: 100%;
    padding-left: 73px;
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: rgba(102, 102, 102, 1);
  }
}

/deep/ .ivu-menu {
  z-index: 0;
}

/deep/ .ivu-menu-submenu:last-child {
  margin-bottom: 30px;
}
/deep/
  .ivu-menu-light.ivu-menu-vertical
  .ivu-menu-item-active:not(.ivu-menu-submenu) {
  background: rgba(221, 38, 25, 0.03);
}
/deep/
  .ivu-menu-light.ivu-menu-vertical
  .ivu-menu-item-active:not(.ivu-menu-submenu)
  span {
  border-left: 3px solid #dd2619;
  display: inline-block;
  padding-left: 70px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 600;
  color: rgba(221, 38, 25, 1);
}

/deep/ .ivu-menu-vertical.ivu-menu-light:after {
  width: 0px;
}

/deep/
  .ivu-menu-light.ivu-menu-vertical
  .ivu-menu-item-active:not(.ivu-menu-submenu):after {
  background: none;
}

/deep/
  .ivu-menu-light.ivu-menu-vertical
  .ivu-menu-item-active:not(.ivu-menu-submenu):after {
  background: none;
}

.system {
  width: 948px;
  height: 1240px;
  padding: 20px 32px 0;

  .systemContent {
    min-height: 102px;
    display: flex;
    flex-flow: column;
    justify-content: center;
    border-bottom: 1px solid #ebebeb;

    .systemContent-top {
      display: flex;
      justify-content: space-between;

      .systemContent-notice {
        font-size: 16px;
        line-height: 16px;
        font-weight: 500;
        color: #333333;
      }

      .systemContent-time {
        font-size: 14px;
        line-height: 14px;
        font-weight: 400;
        color: #666666;
        span {
          display: inline-block;
          white-space: nowrap;
          width: 100%;
          height: 60px;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      .systemContent-time1 {
        font-size: 14px;
        font-weight: 400;
        color: #666666;
        line-height: 60px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        cursor: pointer;

        &:hover {
          color: #dd2619;
        }
      }
    }
    .systemContent-top:last-child {
      height: 60px;
      .systemContent-time {
        /deep/ span {
          display: flex;
          align-items: center;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          /deep/ p {
            display: inline-block;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            height: 16px;
          }
          /deep/ h1 {
            line-height: 60px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }
      }
      .systemContent-time1 {
        span {
          display: flex;
          align-items: center;
        }
      }
    }
  }
}

.systemContent-btn {
  width: 96px;
  height: 34px;
}

/deep/ .ivu-modal {
  width: 650px !important;
  height: 565px;
}

/deep/ .ivu-modal-content {
  height: 565px;
}

/deep/ .ivu-modal-body {
  width: 574px;
  height: 273px;
  margin: 0 auto;
  word-wrap: break-word;
  overflow-y: auto;
  .header-top {
    text-align: center;
    p:first-child {
      font-size: 14px;
      text-align: left;
    }
  }
}

/deep/ .ivu-modal-header {
  border-bottom: none;
  padding: 36px 16px 19px;
}

/deep/ .ivu-modal-header p:first-child {
  text-align: center;
  height: 75px;
  line-height: 55px;
  font-size: 16px;
  color: #333333 !important;
  display: flex;
  flex-flow: column;
  span {
    height: 25px;
    font-size: 18px;
  }
}

/deep/ .ivu-modal-footer {
  border-top: none;
  text-align: center;
  margin: 62px 0 0;
}
</style>
