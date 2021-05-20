<template>
    <div class="container">
        <!-- 顶部搜索部分 -->
        <searchPart :isNeedCupping="false" :borderFullWidth="true"></searchPart>
        <div class="product-main-part" v-loading="detailsLoading">
            <productMainPart
                v-if="goodsDetails"
                ref="productMainPart"
                :loginFlag="loginFlag"
                :goodsDetails="goodsDetails"
                :reduceList="reduceList"
                :couponList="couponList"
                :goodsSpecList="goodsSpecList"
                :goodsSpecNameValue="goodsSpecNameValue"
                :addCartLoading="addCartLoading"
                :cartBuyNowLoading="cartBuyNowLoading"
                :goodsFavNum="goodsFavNum"
                :goodsIsCollect="goodsIsCollect"
                @handleAddCart="handleAddCart"
                @handleGoodsCollection="handleGoodsCollection"
                @handleGoodsCancelCollection="handleGoodsCancelCollection"
                @handleGoodsDetails="handleGoodsDetails"
                @handleShowLoginModal="handleShowLoginModal"
            ></productMainPart>
        </div>

        <cupping :SwiperHeight="SwiperHeight" :seizeaseatHeight="0">
            <div class="cupping-body" v-show="mainPartCupping">
                <div class="cupping-box">
                    <div class="main-part-left" v-loading="shopInfoLoading">
                        <div class="shop-name" v-if="shopInfo">
                            <span
                                class="tips"
                                v-text="
                                    shopInfo.storeType == '1' ? '自营' : '普通'
                                "
                            ></span>

                            <span
                                class="name-word"
                                v-text="shopInfo.storeName"
                                :title="shopInfo.storeName"
                                @click="handleShopHome"
                            ></span>

                            <img
                                src="/img/03.goodsClass/03-03.goodsDetail/server.png"
                                alt
                            />
                        </div>
                    </div>

                    <div class="main-part-right">
                        <div class="router-title">
                            <div class="router-title-box" @click="reScroll()">
                                <a
                                    href="javascript:;"
                                    v-for="(item, index) in tabs"
                                    :key="item"
                                    :class="{ active: index == cardTabVal }"
                                    v-text="item"
                                    @click="handleFixedBtnTab(index)"
                                ></a>
                            </div>
                            <Button
                                v-if="
                                    goodsDetails && goodsDetails.goodsShow == 1
                                "
                                type="primary"
                                class="join-cart"
                                @click="handleAddCart()"
                                >加入购物车</Button
                            >
                            <!-- 商品上架状态显示 -->
                        </div>
                    </div>
                </div>
            </div>
        </cupping>

        <!-- 商品详情下方内容 -->
        <div class="main-part-content">
            <div class="main-part-left">
                <div v-loading="shopInfoLoading" class="shop-info-wrap">
                    <shopInfo
                        :loginFlag="loginFlag"
                        :shopInfo="shopInfo"
                        :shopIsCollect="shopIsCollect"
                        @handleCollectShop="handleCollectShop"
                        @handleShowLoginModal="handleShowLoginModal"
                    ></shopInfo>
                </div>

                <productRecommendation
                    :storeId="goodsDetails && goodsDetails.goodsVO.storeId"
                ></productRecommendation>
            </div>

            <Tabs
                type="card"
                class="main-part-right"
                :animated="false"
                v-model="cardTabVal"
                v-loading="detailsLoading"
            >
                <template v-if="goodsDetails">
                    <TabPane
                        v-for="(tab, index) in tabs"
                        :key="index"
                        :label="tab"
                        :name="index.toString()"
                        :class="{ height_0: cardTabVal != index }"
                    >
                        <image-and-words
                            v-if="index == 0"
                            :id="tab.id"
                            :goodsBoby="goodsDetails.mobileBody"
                        ></image-and-words>

                        <specifications
                            v-if="index == 1"
                            :id="tab.id"
                            :goodsAttrVOList="
                                goodsDetails.goodsVO.goodsAttrVOList
                            "
                        ></specifications>

                        <after-sale-service
                            v-if="index == 2"
                            :afterSale="goodsDetails.afterSale"
                        ></after-sale-service>

                        <user-evaluation
                            v-if="index == 3"
                            :goodsCommentLoading="goodsCommentLoading"
                            :goodsCommentList="goodsCommentList"
                            :commentAboutNum="commentAboutNum"
                            :commentTotalCount="commentTotalCount"
                            @handleCommentList="handleCommentList"
                        ></user-evaluation>
                    </TabPane>
                </template>
            </Tabs>
        </div>

        <!-- 登录 -->
        <login ref="login" class="login"></login>
    </div>
</template>

<script>
    import $ from "jquery";
    import searchPart from "@/components/common/searchPartWhite.vue"; //搜索头部
    import cupping from "@/components/01.index/cupping.vue"; //吸顶效果
    import productMainPart from "@/components/03.goodsClass/03-03.goodsDetails/productMainPart.vue"; //主要区域
    import shopInfo from "@/components/03.goodsClass/03-03.goodsDetails/shopInfo.vue";
    import productRecommendation from "@/components/03.goodsClass/03-03.goodsDetails/productRecommendation.vue";

    import imageAndWords from "./03-03-01.imageAndWords.vue";
    import specifications from "./03-03-02.specifications.vue";
    import afterSaleService from "./03-03-03.afterSaleService.vue";
    import userEvaluation from "./03-03-04.userEvaluation.vue";

    import login from "@/components/common/login 2.vue";

    import { mapState, mapMutations, mapActions } from "vuex";

    export default {
        name: "productDetails",
        components: {
            searchPart,
            productMainPart,
            shopInfo,
            cupping,
            productRecommendation,
            imageAndWords,
            specifications,
            afterSaleService,
            userEvaluation,
            login,
        },
        data() {
            return {
                mainPartCupping: false,
                SwiperHeight: 2000,
                tabs: ["图文详情", "规格参数", "售后服务", "商品评价"],
                cardTabVal: "0",
            };
        },
        computed: {
            ...mapState("goodsDetails", [
                "loginFlag",
                "goodsDetails",
                "reduceList",
                "couponList",
                "shopInfo",
                "shopIsCollect",
                "goodsSpecList",
                "goodsCommentList",
                "goodsFavNum",
                "goodsIsCollect",
                "commentTotalCount",
                "commentAboutNum",
                "goodsSpecNameValue",
                "goodsHotList",
                "goodsLikeList",
                "detailsLoading",
                "goodsHotLoading",
                "goodsLikeLoading",
                "goodsCollectLoading",
                "storeCollectLoading",
                "addCartLoading",
                "cartBuyNowLoading",
                "goodsCommentLoading",
                "shopInfoLoading",
            ]),
        },

        created() {
            //  触发 handleGoodsDetails，首次进入
            let params = {
                specId: this.$route.query.specId,
                noPopupMessage: true,
            };
            this.handleGoodsDetails(params);
        },

        watch: {
            commentTotalCount: {
                immediate: true,
                handler(newVal, oldVal) {
                    newVal = this.$options.filters.filterCommentNum(newVal);
                    this.$set(this.tabs, 3, `商品评价(${newVal})`);
                },
            },
        },

        filters: {
            filterCommentNum: (val) => {
                if (val < 10000) return val;
                else return (val = `${(val / 10000).toFixed(1)}万+`);
            },
        },

        mounted() {
            // 先给页面注册滚动事件
            $(document).on("scroll", this.scroll);

            // 新增浏览记录
            this.handleBrowseRecord({
                specId: this.$route.query.specId,
                noPopupMessage: true,
            });
        },

        methods: {
            ...mapMutations("goodsDetails", ["HANDLE_RESET_DETAILS"]),

            ...mapActions("goodsDetails", [
                "handleGoodsDetails",
                "handleGoodsComment",
                "handleAddToShoppingCart",
                "handleCartBuyNow",
                "handleStoreCollection",
                "handleStoreCancelCollection",
                "handleGoodsCollection",
                "handleGoodsCancelCollection",
                "handleGoodsCollection",
                "handleBrowseRecord",
            ]),

            scroll() {
                if ($(".main-part-content").length == 0) return;

                this.SwiperHeight = $(".main-part-content").offset().top;

                if ($(document).scrollTop() > this.SwiperHeight) {
                    this.mainPartCupping = true;
                } else {
                    this.mainPartCupping = false;
                }
            },

            reScroll() {
                $(document).scrollTop(this.SwiperHeight + 2);
            },

            // 加入购物车
            handleAddCart(params) {
                // 固定的tabs按钮点击回调
                if (!params) {
                    let temp = this.$refs.productMainPart;
                    params = {
                        goodsNum: temp.goodsNum,
                        specId: temp.addCartSpecId,
                    };
                }

                params.goodsNum = parseInt(params.goodsNum);
                this.handleAddToShoppingCart(params);
            },

            // 进入店铺首页
            handleShopHome() {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: { storeId: this.shopInfo.id },
                });
                window.open(routeUrl.href, "_blank");
            },

            // 关注店铺
            handleCollectShop(param) {
                let storeId = this.shopInfo.id;
                if (param == "collect") {
                    this.handleStoreCollection({ storeId });
                } else {
                    this.handleStoreCancelCollection([storeId]);
                }
            },

            // 固定的tabs按钮点击回调
            handleFixedBtnTab(index) {
                this.cardTabVal = index.toString();
                window.scrollTo(this.SwiperHeight, 0);
            },

            // 获取评论列表
            handleCommentList(params) {
                this.handleGoodsComment(params);
            },

            handleShowLoginModal() {
                this.$refs.login.modal1 = true;
            },
        },

        beforeDestroy() {
            this.HANDLE_RESET_DETAILS();
            $(document).off("scroll", this.scroll);
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .container {
        min-height: 600px;
        background-color: #f6f6f6;
        // padding-bottom: 80px;

        .product-main-part {
            width: 100%;
            // min-height: 790px;
            margin-bottom: 18px;
            background-color: #fff;
        }

        .cupping-body {
            width: 100%;
            height: 46px !important;
            background-color: #fff;
            border-bottom: 2px solid #dd2619;
            position: fixed;
            z-index: 4;
            top: 0;
            overflow: hidden;
            .cupping-box {
                width: 1200px;
                margin: 0 auto;
                display: flex;
                justify-content: space-between;
            }
        }

        .shop-name {
            padding: 0 30px;
            margin: 0 0 20px 0 !important;
            line-height: 44px;
            display: flex;
            align-items: center;
            .tips {
                min-width: 34px;
                height: 16px;
                background: linear-gradient(
                    90deg,
                    rgba(221, 41, 28, 1) 0%,
                    rgba(255, 78, 2, 1) 100%
                );
                padding: 0 4px;
                border-radius: 4px;
                margin: 0 4px 0 0;
                font-size: 12px;
                line-height: 16px;
                text-align: center;
                color: #ffffff;
            }
            .name-word {
                margin: 0 10px 0 0;
                font-size: 15px;
                font-weight: 500;
                color: #222;
                line-height: 44px !important;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                transition: all 0.3s;
                cursor: pointer;
                &:hover {
                    color: $primary-color;
                }
            }
        }

        .main-part-content {
            width: 1200px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;

            .shop-info-wrap {
                min-height: 245px;
                background: #fff;
            }
        }

        .main-part-left {
            width: 300px;
            margin-bottom: 60px;
        }

        .main-part-right {
            width: 890px;
            background-color: #fff;

            .height_0 {
                height: 0;
            }

            .router-title {
                display: flex;
                border-bottom: 1px solid #dd2619;
                .router-title-box {
                    display: flex;
                }
                a {
                    width: 160px;
                    height: 44px;
                    font-size: 15px;
                    color: #666;
                    line-height: 44px;
                    text-align: center;
                }

                .router-link-exact-active,
                a.active {
                    background-color: #dd2619;
                    color: #fff;
                }
                .join-cart {
                    width: 116px;
                    height: 36px;
                    margin: 4px 0 0 60px;
                }
            }

            &.ivu-tabs.ivu-tabs-card {
                padding-top: 55px;
                margin-bottom: 60px;
                /deep/ .ivu-tabs-bar {
                    margin: -55px 0 0;
                    width: 948px;
                    height: 44px;
                    background: #ffffff;
                    border-color: $primary-color;
                    box-sizing: content-box;
                    .ivu-tabs-nav-container,
                    .ivu-tabs-nav-scroll,
                    .ivu-tabs-nav,
                    .ivu-tabs-nav-wrap {
                        height: 44px;
                    }
                    .ivu-tabs-tab {
                        width: 160px;
                        height: 44px;
                        line-height: 44px;
                        padding: 0;
                        margin-right: 0;
                        border: 0;
                        border-radius: 0;
                        background: #ffffff;
                        color: #666666;
                        font-size: 15px;
                        text-align: center;
                        transition: all 0.1s;
                        &.ivu-tabs-tab-active {
                            background: $primary-color;
                            color: #ffffff;
                        }
                    }
                    /deep/ .ivu-input-wrapper {
                        width: 252px;
                        height: 36px;
                        margin: 8px 30px 0 0;
                        /deep/ .ivu-input {
                            height: 100%;
                        }
                        /deep/ .ivu-icon {
                            line-height: 36px;
                        }
                    }
                }

                /deep/ .ivu-tabs-content {
                    margin: 40px;
                }
            }
        }
    }

    .login {
        min-height: 0;
        padding: 0;
    }
    /deep/ .shop-info .shop-level {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(163, 163, 163, 1);
        margin-bottom: 14px;
        line-height: 14px !important;
    }
    /deep/ .shop-info .shop-info-btn {
        margin: 20px 0 0 0 !important;
    }
    /deep/ .shop-info {
        height: 245px !important;
    }
    /deep/ .shop-info .shop-name .name-word {
        line-height: 44px !important;
    }
    /deep/ .shop-info .shop-name {
        margin: 0 0 20px 0 !important;
    }
    /deep/ .shop-info .shop-level span {
        &:last-child {
            font-size: 14px;
            line-height: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 500 !important;
            color: rgba(58, 58, 58, 1) !important;
        }
    }
    /deep/ .product-recommendation .ivu-tabs-nav {
        margin: 0 0 30px 0 !important;
    }
    /deep/ .product-recommendation .good-body .img-body .labelName {
        left: 30px !important;
    }
    /deep/ .product-recommendation .good-body {
        padding: 0 30px 19px 30px !important;
    }
    /deep/ .product-recommendation .good-body .goods-name {
        font-size: 14px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: rgba(34, 34, 34, 1);
        line-height: 14px;
    }
    /deep/ .product-recommendation .good-body .goods-sec-name {
        font-size: 12px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400 !important;
        color: rgba(153, 153, 153, 1);
        line-height: 12px;
    }
    /deep/ .product-recommendation .good-body .prize-num {
        font-size: 20px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: rgba(221, 38, 25, 1);
        line-height: 20px;
    }
    /deep/ .container .main-part-left {
        margin-bottom: 60px !important;
    }
    /deep/ .custom-carousel-arrow {
        width: 26px !important;
        height: 26px !important;
        color: #666666 !important;
    }
    /deep/ .shop-info .shop-info-btn .info-btn {
        line-height: 20px !important;
    }
    /deep/ .container .prodcut-main-body .main-area .seckill-notice {
        margin: 20px 0 23px;
    }
</style>
