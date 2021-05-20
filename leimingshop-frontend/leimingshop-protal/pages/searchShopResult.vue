<template>
  <div class="search-shop-result">
    <!-- 顶部搜索部分 -->
    <searchPart :borderFullWidth="true"></searchPart>

    <div class="inner">
      <!-- 面包屑 -->
      <breadcrumb1 :list="breadcrumb.list" />
      <div class="s-content">
        <!-- 店铺筛选过滤工具栏 -->
        <shop-filtration
          v-if="totalCount != 0"
          :totalCount="totalCount"
          @handleFilterData="handleFilterData"
        ></shop-filtration>

        <!-- 店铺列表 -->
        <div class="shop-wrap" v-loading="dataLoading">
          <shop-comp
            v-for="(item, index) in storeVOList"
            :key="index"
            :storeId="item.id"
            :storeName="item.storeName"
            :storeType="item.storeType"
            :storeLogo="item.storeLogo"
            :storeGradeName="item.storeGradeName"
            :storeStar="item.storeStar"
          ></shop-comp>

          <!-- 筛选 - 无数据 -->
          <filtration-no-data
            v-if="totalCount == 0 && !dataLoading"
            :noDataText="filterNoDataText"
          ></filtration-no-data>
        </div>

        <!-- 分页 -->
        <paging
          v-if="storeVOList.length != 0 && totalCount > pageSize"
          class="paging"
          :totalCount="totalCount"
          :pageSize="pageSize"
          v-model="pageNo"
        />
        <div v-else style="height: 60px"></div>
      </div>
    </div>
  </div>
</template>

<script>
// 搜索头部
import searchPart from "@/components/common/searchPartWhite.vue";

import shopFiltration from "@/components/05.searchResult/shopFiltration";
import shopComp from "@/components/05.searchResult/shopComp";
import paging from "@/components/common/paging";

import filtrationNoData from "@/components/03.goodsClass/03-01.goodsClassification/filtrationNoData";
import breadcrumb1 from "@/components/common/breadcrumb1.vue";

import { mapState, mapMutations, mapActions } from "vuex";

export default {
  name: "searchShopResult", // 搜索店铺结果
  head() {
    return {
      title: "店铺搜索",
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
      breadcrumb: {
        separator: ">",
        list: [
          {
            title: "首页",
            toPath: "/",
          },
          {
            title: "搜索结果",
            toPath: "",
          },
          {
            title: this.$route.query.keyWord || "全部",
            toPath: "",
          },
        ],
      },

      filterData: {
        sortField: "", // {'' : '综合'}, {'saleNum': '销量'}, { 'specSellPrice': '价格'}
        sortType: "DESC", // {'DESC': '降序' }, {'ASC': '升序'}
      }, // 过滤字段

      filterNoDataText: "对不起，没有搜索到相关店铺",
    };
  },

  components: {
    searchPart,
    shopFiltration,
    shopComp,
    paging,
    filtrationNoData,
    breadcrumb1,
  },

  computed: {
    ...mapState("searchShopResult", [
      "storeVOList",
      "dataLoading",
      "totalCount",
      "pageSize",
    ]),

    pageNo: {
      get() {
        return this.$store.state.searchShopResult.pageNo;
      },
      set(val) {
        this.HANDLE_CHANGE_PAGENO(val);
      },
    },

    searchDataForm() {
      return {
        keyword: this.$route.query.keyWord
          ? decodeURIComponent(this.$route.query.keyWord)
          : "", // 搜索关键字
        pageSize: this.pageSize, // 每页显示条数
        pageNo: this.pageNo, // 第几页
        ...this.filterData, // 过滤字段
      };
    },
  },

  watch: {
    searchDataForm: {
      immediate: true,
      deep: true,
      handler(newVal, oldVal) {
        // currentPage没变，但是查询数据有变动，currentPage 恢复默认
        if (oldVal && newVal.pageNo != 1 && newVal.pageNo == oldVal.pageNo) {
          return this.HANDLE_CHANGE_PAGENO(1);
        }

        if (oldVal && newVal.keyword != oldVal.keyword) {
          // 初始化
          this.HANDLE_CHANGE_PAGENO(1);
          this.$set(
            this.breadcrumb.list[2],
            "title",
            this.$route.query.keyWord || "全部"
          );
        }

        this.handleSearchShop(newVal);
      },
    },
  },

  methods: {
    ...mapMutations("searchShopResult", ["HANDLE_CHANGE_PAGENO"]),

    ...mapActions("searchShopResult", ["handleSearchShop"]),

    // 筛选数据处理
    handleFilterData(filterData) {
      // Object.assign({}, filterData) 防止价格输入后 双向绑定实时刷新
      this.filterData = Object.assign({}, filterData);
    },
  },

  destroyed() {
    this.HANDLE_CHANGE_PAGENO(1);
  },
};
</script>

<style lang="scss" scoped>
$primary-color: #dd2619;

.ivu-breadcrumb {
  width: 1200px;
  height: 44px;
  line-height: 40px;
  margin: 0 auto;

  /deep/ span {
    color: #333333;
    font-size: 13px;

    &:last-child {
      font-weight: normal;
    }

    .ivu-breadcrumb-item-separator {
      margin: 0 4px;
      /*font-family: simsun;*/
    }
  }
}

.shop-wrap {
  min-height: 330px;
  overflow: hidden;
}

.search-shop-result {
  background: #f6f6f6;

  .inner {
    width: 1200px;
    overflow: hidden;
    margin: 0 auto;
  }

  /deep/ .shop-comp {
    width: 232px;
    float: left;
    margin: 10px 10px 0 0;

    &:nth-of-type(5n) {
      margin-right: 0;
    }

    &:hover {
      .openShopBtn {
        color: #ffffff;
      }
    }
  }
}
</style>
