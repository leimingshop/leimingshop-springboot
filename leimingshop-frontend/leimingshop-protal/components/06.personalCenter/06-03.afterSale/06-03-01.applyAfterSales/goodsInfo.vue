<template>
    <div class="blockWrap goods-info" v-loading="orderInfoLoading">
        <h4 v-text="title"></h4>

        <Card :bordered="true" class="">
            <div class="card-head">
                <p v-for="(item, key) in tHead" :key="key" v-text="item"></p>
            </div>

            <div class="card-content" v-if="orderInfo">
                <div
                    class="g-info"
                    v-lazy-container="{
                        selector: 'img',
                        error: '/img/common/loading/200_200.png',
                        loading: '/img/common/loading/200_200.png',
                    }"
                >
                    <img
                        class="g-image"
                        :data-src="
                            $options.filters.filterImgUrl(orderInfo.goodsImage)
                        "
                        @click="handleGoodsDetail"
                    />

                    <div class="g-content">
                        <div
                            class="g-name"
                            v-text="orderInfo.goodsName"
                            :title="orderInfo.goodsName"
                            @click="handleGoodsDetail"
                        ></div>

                        <div
                            class="g-specifications"
                            :title="orderInfo.specAttrValueName"
                            v-text="orderInfo.specAttrValueName"
                        ></div>
                    </div>
                </div>
                <p class="price">
                    ￥<span v-text="orderInfo.specPrice"></span>
                </p>
                <p class="number">×<span v-text="orderInfo.goodsNum"></span></p>
                <p class="totolPrice">
                    ￥<span
                        v-text="orderInfo.specPrice * orderInfo.goodsNum"
                    ></span>
                </p>
                <p class="actualPaidPrice">
                    ￥<span v-text="orderInfo.specPayPrice"></span>
                </p>
            </div>
        </Card>
    </div>
</template>

<script>
    export default {
        name: "gInfo",
        data() {
            return {
                title: "售后申请",
                tHead: {
                    goodsInfo: "商品信息",
                    price: "单价",
                    number: "数量",
                    totolPrice: "小计",
                    actualPaidPrice: "实付",
                },
            };
        },

        props: {
            orderInfo: {
                type: Object,
                default: () => null,
            },

            orderInfoLoading: {
                type: Boolean,
                default: false,
            },
        },

        components: {},
        created() {},
        computed: {
            formValidate() {
                return {
                    orderGoodsId: this.$route.query.orderGoodsId, //订单项ID
                    orderId: this.orderInfo.orderId, //订单项ID
                    orderSn: this.orderInfo.orderSn, //订单项ID
                };
            },
        },
        watch: {},

        mounted() {},
        methods: {
            // 商品详情页
            handleGoodsDetail() {
                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId: this.orderInfo.goodsId,
                        specId: this.orderInfo.specId,
                    },
                });

                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .ivu-card {
        height: 164px;
    }

    .card-head {
        height: 44px;
        background: #f6f6f6;
        color: #666666;
        font-size: 14px;
        padding: 0 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        p {
            min-width: 70px;
            &:nth-of-type(1) {
                width: 340px;
            }
        }
    }

    .card-content {
        padding: 30px 30px 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        p {
            min-width: 70px;
        }
    }

    .g-info {
        display: flex;
        .g-image {
            width: 68px;
            height: 68px;
            cursor: pointer;
            &:hover {
                border: 1px solid $primary-color;
            }
        }
        .g-content {
            width: 270px;
            padding-left: 15px;
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        .g-name {
            color: #333333;
            font-size: 16px;
            display: -webkit-box;
            -webkit-line-clamp: 2; /*规定超过两行的部分截断*/
            -webkit-box-orient: vertical;
            overflow: hidden;
            word-break: break-all; /*在任何地方换行*/
            transition: color 0.3s;
            cursor: pointer;
            &:hover {
                color: $primary-color;
            }
        }
        .g-specifications {
            color: #999999;
            font-size: 13px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }
</style>
