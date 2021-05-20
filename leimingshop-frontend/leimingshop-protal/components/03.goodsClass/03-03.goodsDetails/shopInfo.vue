<template>
    <div class="shop-info" v-if="shopInfo">
        <div class="shop-name">
            <span
                class="tips"
                v-text="shopInfo.storeType == '1' ? '自营' : '普通'"
            ></span>

            <span
                class="name-word"
                v-text="shopInfo.storeName"
                :title="shopInfo.storeName"
                @click="handleShopHome"
                @mouseover="mouseOver"
                @mouseout="mouseOut"
            ></span>

            <img :src="shopRed" alt="" />
        </div>

        <div class="shop-level">
            <span>店铺星级：</span>
            <Rate
                v-model="shopInfo.findEvaluateStoreDTO.storeLevel"
                disabled
                custom-icon="rate-icon"
            />
        </div>
        <div class="shop-level">
            <span>商品评价：</span>
            <span v-text="shopInfo.findEvaluateStoreDTO.goodsEvaluate"></span>
        </div>
        <div class="shop-level">
            <span>物流履约：</span>
            <span
                v-text="shopInfo.findEvaluateStoreDTO.logisticsEvaluate"
            ></span>
        </div>
        <div class="shop-level">
            <span>售后服务：</span>
            <span
                v-text="shopInfo.findEvaluateStoreDTO.afterSaleEvaluate"
            ></span>
        </div>
        <div class="shop-info-btn">
            <div
                class="info-btn"
                v-if="shopIsCollect == 0"
                @click="handleCollectShop('collect')"
            >
                <img
                    src="/img/03.goodsClass/03-03.goodsDetail/collect-shop.png"
                    alt=""
                />
                关注店铺
            </div>

            <div
                class="info-btn"
                v-else
                @click="handleCollectShop('cancelCollect')"
            >
                <img
                    src="/img/03.goodsClass/03-03.goodsDetail/collected.png"
                    alt=""
                />
                取消关注
            </div>

            <div class="info-btn" @click="handleShopHome">
                <img
                    src="/img/03.goodsClass/03-03.goodsDetail/shop.png"
                    alt=""
                />
                进店逛逛
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "shopInfo",
        data() {
            return {
                shopRed:"/img/03.goodsClass/03-03.goodsDetail/server.png",
            };
        },

        props: {
            loginFlag: {
                type: Number,
                default: 0,
            },

            shopInfo: {
                type: Object,
                default: null,
            },

            shopIsCollect: {
                type: Number | Boolean,
                default: 0,
            },
        },

        watch: {},

        watch: {},

        methods: {
            // 关注店铺
            handleCollectShop(param) {
                if (this.loginFlag == 0) {
                    this.$emit("handleShowLoginModal");
                    return;
                }

                this.$emit("handleCollectShop", param);
            },

            // 进入店铺首页
            handleShopHome() {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: { storeId: this.shopInfo.id },
                });
                window.open(routeUrl.href, "_blank");
            },
            mouseOut:function(){
                this.shopRed = "/img/03.goodsClass/03-03.goodsDetail/server.png"
            },
            mouseOver:function(){
                this.shopRed = "/img/03.goodsClass/03-03.goodsDetail/server-message.png"
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .shop-info {
        width: 100%;
        height: 250px;
        background-color: #ffffff;
        .shop-name {
            padding: 0 30px;
            border-bottom: 1px solid #ebebeb;
            margin: 0 0 12px 0;
            line-height: 54px;
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
                line-height: 54px;
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
        .shop-level {
            padding: 0 30px;
            font-size: 14px;
            color: #a3a3a3;
            line-height: 30px;
            span:last-child {
                color: #3a3a3a;
                font-weight: 500 !important;
            }
        }
        .shop-info-btn {
            padding: 0 30px;
            margin: 12px 0 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            .info-btn {
                width: 114px;
                height: 32px;
                background-color: #fff;
                border: 1px solid rgba(204, 204, 204, 1);
                border-radius: 16px;
                font-size: 14px;
                line-height: 16px;
                cursor: pointer;
                transition: all 0.3s;
                display: flex;
                justify-content: center;
                align-items: center;
                color: #666666;
                &:hover {
                    // border-color: $primary-color;
                    //color: $primary-color;
                    font-weight: 600;
                    border-color: #888;
                }
                &:active {
                    opacity: 0.5;
                }
                &.cancelCollect {
                    border-color: $primary-color;
                    color: $primary-color;
                    font-weight: 600;
                }
                img {
                    vertical-align: -1px;
                    margin-right: 5px;
                    width: 14px;
                    height: 14px;
                }
            }
        }
    }
    /deep/ .ivu-rate-star-chart{
        margin-right: 4px !important;
    }
</style>
