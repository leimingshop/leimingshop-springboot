<!-- 常规搜索结果页 不显示大类‘分类集合’ -->
<!-- 活动搜索结果页 不显示大类‘所有集合’ -->

<template>
  <div :class="['search-goods-result', { activity: activity }]">
    <div class="inner">
      <div class="banner-subject">
        <Carousel
          :autoplay-speed="3000"
          easing="none"
          :autoplay="subjectImg.length > 111"
          @on-click="actCarousel"
          v-model="carouselVal"
          loop
          :radius-dot="true"
        >
          <CarouselItem v-for="(item, index) in subjectImg" :key="index">
            <div class="demo-carousel">
              <img style="height: 470px; width: 100%" :src="$imgURL + item" />
            </div>
          </CarouselItem>
        </Carousel>
      </div>
      <!-- 内容 -->
      <div class="p-content">
        <div class="product-wrap">
          <!-- 商品列表 -->
          <div class="goods-wrap" v-loading="subjectLoading">
            <template v-if="subjectGoodsList.length != 0">
              <goods-comp
                v-for="item in subjectGoodsList"
                :key="item.id"
                :thumbnailVisible="goodsCard.thumbnailVisible"
                :otherInfoVisible="goodsCard.otherInfoVisible"
                :goodsId="item.id"
                :specId="item.specId"
                :specSellPrice="item.specSellPrice"
                :goodsLabels="item.goodsLabels"
                :goodsMainPicture="item.goodsMainPicture"
                :goodsName="item.goodsName"
                :goodsSubTitle="item.goodsSubTitle"
                :storeType="item.storeType"
                :storeName="item.storeName"
                :storeId="item.storeId"
                :evaluateCount="item.evaluateCount"
                :goodsSaleNum="item.saleNum"
              ></goods-comp>
            </template>
            <!-- 搜索 - 无数据 -->
            <!--                         <search-no-data v-else></search-no-data>-->
          </div>

          <!-- 分页 -->
          <paging
            v-if="
              subjectGoodsList.length != 0 && subjectTotal > subjectParam.limit
            "
            class="paging"
            :totalCount="subjectTotal"
            :pageSize="subjectParam.limit"
            v-model="pageNo"
          />
          <div v-else style="height: 50px"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import goodsComp from "@/components/common/goodsSubject";
import paging from "@/components/common/paging";
import searchNoData from "@/components/05.searchResult/05-02.searchNoData";
import { mapState, mapMutations, mapActions } from "vuex";

export default {
  name: "subject", // 搜索商品结果
  components: {
    goodsComp,
    paging,
    searchNoData,
  },
  data() {
    return {
      activity: null,
      goodsCard: {
        thumbnailVisible: false,
        otherInfoVisible: true,
      },
      carouselVal: 0,
    };
  },

  computed: {
    ...mapState("subject", [
      "subjectGoodsList",
      "subjectTotal",
      "subjectLoading",
      "subjectParam",
      "subjectImg",
    ]),
    pageNo: {
      get() {
        return this.$store.state.subject.subjectParam.pageNo;
      },
      set(val) {
        this.changePage(val);
      },
    },
  },
  watch: {
    $route: {
      immediate: true,
      handler(newVal, oldVal) {
        console.log(this);
        console.log(this.$store.state);
        this.$store.state.subject.subjectParam.id = this.$route.query.id;
        this.getSubjectData();
      },
    },
  },

  methods: {
    ...mapActions("subject", ["getSubjectData", "changePage"]),
    actCarousel(val) {
      console.log(val);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/scss/modules/goods-list-comp.scss";
</style>

<style lang="scss" scoped>
.banner-subject {
  width: 100%;
  min-height: 470px;
  margin-bottom: 10px;
  img {
    max-width: 100%;
  }
  .demo-carousel {
    width: 100%;
  }
}
.search-goods-result {
  background: #f6f6f6;
  width: 100%;
}

/deep/ .product-filtration {
  margin-bottom: 10px;
  .ivu-checkbox-wrapper {
    vertical-align: -8px !important;
  }
}
/deep/ .basic-style h6 {
  font-size: 13px !important;
}
/deep/ .ivu-checkbox {
  margin-left: 6px !important;
}
/deep/ .basic-style .handle-btn {
  top: 10px !important;
}
/deep/ .goods-comp.column-style {
  margin-right: 10px;
}
/deep/ .goods-comp.column-style .ivu-card-body {
  padding: 20px 16px 18px !important;
}
/deep/ .goods-wrap[data-v-691257c8] .goods-comp .p-tag {
  top: 20px;
  left: 16px;
}
/deep/ .ivu-checkbox-focus {
  box-shadow: none !important;
}
/deep/ .ivu-checkbox-checked .ivu-checkbox-inner {
  background-color: #ffffff !important;
}
/deep/ .ivu-checkbox-checked .ivu-checkbox-inner {
  &::after {
    border: 1px solid #dd2619 !important;
    border-top: 0 !important;
    border-left: 0 !important;
  }
}
// /deep/ .ivu-page .ivu-page-disabled .ivu-icon {
//   color: #3a3a3a !important;
// }

// /deep/ .ivu-page-options-elevator input {
//   border-radius: 0px !important;
// }
// /deep/ .ivu-btn {
//   border-radius: 0px !important;
// }
// /deep/ .ivu-page-item-active {
//   background: #dd2619 !important;
//   a {
//     color: #ffffff !important;
//   }
// }
// /deep/ .paging-comp {
//   padding: 0px !important;
// }
/deep/ .confirm-btn {
  margin-bottom: 2px !important;
}
// /deep/ .ivu-page[data-v-ffdbe760] .ivu-page-item {
//   a {
//     line-height: 15px;
//     margin: 4px 0 !important;
//   }
// }
// /deep/ .confirm-btn {
//   margin-bottom: 10px !important;
// }
// /deep/ .ivu-page[data-v-ffdbe760] .ivu-page-item {
//   a {
//     line-height: 15px;
//     margin: 4px 0 !important;
//   }
// }
</style>
