<template>
  <div class="product-recommendation">
    <Tabs v-model="tabPaneCheckIndex" @on-click="handleTabPaneCheck">
      <TabPane
        v-for="(ITEM, INDEX) in goodsList"
        :key="ITEM.title"
        :label="ITEM.title"
        :name="INDEX.toString()"
        v-loading="ITEM.loading"
      >
        <div
          class="good-body"
          v-if="ITEM.list.length != 0"
          v-for="(item, index) in ITEM.list"
          v-loading="item.loading"
        >
          <div
            class="img-body"
            @click="handleGoodsDetail(item.id, item.specId)"
            v-lazy-container="{
              selector: 'img',
              error: '/img/common/loading/200_200.png',
              loading: '/img/common/loading/200_200.png',
            }"
          >
            <img
              :data-src="
                (item.goodsMainPicture + '_200x200.png') | filterImgUrl
              "
              alt=""
            />
            <div
              class="labelName"
              v-if="item.labelName"
              v-text="item.labelName"
            ></div>
          </div>
          <div
            class="goods-name"
            v-text="item.goodsName"
            :title="item.goodsName"
            @click="handleGoodsDetail(item.id, item.specId)"
          ></div>

          <div
            class="goods-sec-name"
            v-text="item.goodsSubTitle"
            :title="item.goodsSubTitle"
          ></div>
          <div class="prize-num">
            <p v-html="$options.filters.filterPrice(item.specSellPrice)"></p>
          </div>
        </div>

        <div v-if="!ITEM.list || ITEM.list.length == 0">
          <filtration-no-data
            :noDataText="ITEM.noDataText"
          ></filtration-no-data>
        </div>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import filtrationNoData from "@/components/03.goodsClass/03-01.goodsClassification/filtrationNoData";
import { mapState, mapActions } from "vuex";

export default {
  components: {
    filtrationNoData,
  },

  data() {
    return {
      tabPaneCheckIndex: 0,
    };
  },

  props: {
    storeId: {
      type: String,
      default: "",
    },
  },

  computed: {
    ...mapState("goodsDetails", [
      "goodsHotList",
      "goodsLikeList",
      "goodsHotLoading",
      "goodsLikeLoading",
    ]),

    goodsList() {
      return [
        {
          title: "店铺热卖",
          list: this.goodsHotList,
          loading: this.goodsHotLoading,
          event: this.handleShopGoodsHot,
          noDataText: "暂无热卖商品",
        },

        {
          title: "同类推荐",
          list: this.goodsLikeList,
          loading: this.goodsLikeLoading,
          event: this.handleGoodsLike,
          noDataText: "暂无推荐商品",
        },
      ];
    },
  },

  watch: {
    storeId: {
      immediate: true,
      handler(newVal, oldVal) {
        if (newVal != oldVal) {
          this.handleTabPaneCheck(0);
        }
      },
    },
  },

  methods: {
    ...mapActions("goodsDetails", ["handleShopGoodsHot", "handleGoodsLike"]),

    handleTabPaneCheck(val) {
      if (!this.goodsList[val].list) {
        let params = {
          storeId: this.storeId,
        };
        this.goodsList[val].event(params);
      }
    },

    handleGoodsDetail(goodsId, specId) {
      let routeUrl = this.$router.resolve({
        name: "goodsDetails",
        query: { goodsId, specId },
      });

      window.open(routeUrl.href, "_blank");
    },
  },
};
</script>

<style lang="scss" scoped>
$primary-color: #dd2619;

.product-recommendation {
  width: 100%;
  min-height: 387px;
  background-color: #fff;
  margin: 10px 0 0 0;
  /deep/ .ivu-tabs {
    min-height: 387px;
  }

  /deep/ .ivu-tabs-bar {
    border-bottom: 0;
    margin-bottom: 0;
  }

  /deep/ .ivu-tabs-nav {
    width: 100%;
    padding: 0 30px;
    border-bottom: 1px solid #ebebeb;
    margin: 0 0 32px 0;
    line-height: 54px;
    display: flex;
    align-items: center;

    .ivu-tabs-ink-bar {
      display: none;
    }

    .ivu-tabs-tab {
      width: 50%;
      margin: 0 10px 0 0;
      font-size: 15px;
      padding: 0;
      font-weight: 500;
      color: #999;
      line-height: 54px;
      text-align: center;
      &.ivu-tabs-tab-active {
        color: #dd2619;
      }
    }
  }

  .good-body {
    width: 240px;
    padding: 0 30px 30px 30px;
    margin: 0 auto;

    .img-body {
      width: 180px;
      height: 180px;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      img {
        width: 100%;
        max-height: 100%;
      }
      .labelName {
        width: 56px;
        height: 24px;
        background: linear-gradient(
          90deg,
          rgba(221, 41, 28, 1) 0%,
          rgba(255, 78, 2, 1) 100%
        );
        font-size: 14px;
        color: #fff;
        line-height: 24px;
        text-align: center;
        position: absolute;
        left: 0;
        top: 0;
      }
    }

    .goods-name {
      margin: 20px 0 10px 0;
      font-size: 16px;
      font-weight: 500;
      color: #333;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      cursor: pointer;
      &:hover {
        color: $primary-color;
      }
    }

    .goods-sec-name {
      margin: 0 0 10px 0;
      font-size: 13px;
      font-weight: 500;
      color: #999;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .prize-num {
      color: #dd2619;
      span:nth-child(2) {
        font-size: 20px;
      }
    }
  }
}
</style>