<template>
  <div class="index">
    <!-- 顶部搜索部分 -->
    <search-second-level
      :isNeedCupping="true"
      :myCoupon="false"
      :isShop="true"
    ></search-second-level>
    <!-- <div class="banner-box">
            <img v-lazy="'/static/img/' + cropImg" :key="'/static/img/' + cropImg" class="banner" />
        </div> -->
    <router-view :StoreInfoStoreIdData="StoreInfoStoreIdData"></router-view>
  </div>
</template>

<script>
import indexCart from "@/components/01.index/indexCart.vue"; //购物车
import { mapState, mapActions, mapMutations } from "vuex";
export default {
  components: {
    indexCart,
  },
  head() {
    return {
      title: "店铺 - 首页",
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
      value: "",
      cropImg: "",
      advKey: "pcStoreIndex", //pcIndex pc首页 storeIndex 店铺首页
      storeId: this.$route.query.storeId, //店铺id
      searchType: [
        {
          value: "0",
          label: "商品",
        },
        {
          value: "1",
          label: "店铺",
        },
      ],
      searchVal: "",
      searchTypeVal: "0",
    };
  },
  computed: {
    ...mapState("main", ["StoreInfoStoreIdData"]),
  },
  mounted() {
    let obj = {
      advKey: this.advKey, //pcIndex pc首页 storeIndex 店铺首页
    };
    if (this.storeId) {
      obj.storeId = this.storeId; //店铺Id
      this.actStoreInfoStoreId({ storeId: this.storeId });
    }
  },
  methods: {
    ...mapActions("main", ["actStoreInfoStoreId"]),
    actToDetail() {
      if (this.searchTypeVal == 0)
        this.$router.push(
          `/searchGoodsResult?keyWord=${encodeURIComponent(this.searchVal)}`
        );
      else {
        this.$router.push(
          `/searchShopResult?keyWord=${encodeURIComponent(this.searchVal)}`
        );
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.container {
  background-color: #eee;
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
.index {
  width: 100%;
  background-color: #f6f6f6;

  .banner-box {
    width: 100%;
    height: 550px;
    display: flex;
    align-items: center;
    overflow: hidden;

    .banner {
      width: 100%;
      height: auto;
    }
  }
}
</style>
