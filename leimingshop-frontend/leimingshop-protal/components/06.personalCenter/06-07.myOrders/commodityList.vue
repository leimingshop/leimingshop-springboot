<template>
    <div class="order-list">
        <p class="p1">商品信息</p>
        <ul class="orderlist-content">
            <div class="layout content1">
                <span class="width1">商品信息</span>
                <span class="price">单价</span>
                <span class="count">数量</span>
                <span class="total">总计</span>
                <span class="operation">操作</span>
            </div>
            <li
                class="outer-surface"
                v-for="(item1,
                index1) in detailData.orderDetailGoodsListDTOList"
                :key="index1"
            >
                <ul class="layout-wrapper">
                    <li
                        class="layout layout-li"
                        v-for="(item2, index2) in item1.orderGoodsDTOList"
                        v-lazy-container="{
                            selector: 'img',
                            error: '/img/common/loading/200_200.png',
                            loading: '/img/common/loading/200_200.png',
                        }"
                    >
                        <div
                            class="width1 content2"
                            @click="goDetail(item2.goodsId, item2.specId)"
                        >
                            <div class="img">
                                <img
                                    :data-src="$imgURL + item2.goodsImage"
                                    alt
                                />
                            </div>
                            <div class="info">
                                <b
                                    :title="item2.goodsName"
                                    class="twoLineEllipsis"
                                >
                                    <!--                  <em-->
                                    <!--                    class="emem"-->
                                    <!--                    v-show="item2.activityType!=0 || item2.activityType"-->
                                    <!--                  >{{item2.activityType | filterOrderLabel}}</em>-->
                                    <span class="orderName">{{
                                        item2.goodsName
                                    }}</span>
                                </b>
                                <p>
                                    <span>{{ item2.specAttrValueName }}</span>
                                    数量：
                                    <span>{{ item2.goodsNum }}</span>
                                </p>
                            </div>
                        </div>
                        <div class="price1">
                            ￥
                            <span>{{ item2.specPrice }}</span>
                        </div>
                        <span class="count1">{{ item2.goodsNum }}</span>
                        <div class="total1">
                            ￥
                            <span>{{ item2.goodsTotalPrice }}</span>
                        </div>
                        <div class="button-group">
                            <Button
                                type="text"
                                @click="
                                    handlePurchase(item2.goodsNum, item2.specId)
                                "
                                class="red"
                                >立即购买</Button
                            >
                            <Button
                                type="text"
                                v-show="
                                    appStatus == 40 &&
                                    item2.evaluationStatus == 1
                                "
                                class="normal"
                                @click="
                                    handleEvaluateDetail(
                                        item2.orderId,
                                        item2.id
                                    )
                                "
                                >查看评价</Button
                            >
                            <Button
                                type="text"
                                v-show="
                                    appStatus == 40 &&
                                    item2.evaluationStatus == 0
                                "
                                class="normal"
                                @click="handleEvaluate(item2.orderId, item2.id)"
                                >评价商品</Button
                            >
                            <Button
                                type="text"
                                v-show="
                                    appStatus == 40 &&
                                    detailData.isAfterSale == 1
                                "
                                class="normal-another"
                                @click="handleAfterSale(item2.id)"
                                >申请售后</Button
                            >
                            <!-- 此处有问题，应该判断单个商品是否可以申请售后，例如单个商品已申请售后的情况 -->
                        </div>
                    </li>
                </ul>
            </li>
        </ul>
        <div class="total-info">
            <div class="totalinfo-wrapper">
                <ul class="totalinfo-left">
                    <li>商品总额：</li>
                    <li>运费：</li>
                    <li>积分抵扣：</li>
                    <!-- 积分抵扣目前是没有的-->
                    <li>优惠券：</li>
                    <li>促销优惠：</li>
                    <li class="lastp">实付金额：</li>
                </ul>
                <div class="totalinfo-right">
                    <li>￥{{ detailData.goodsAmount }}</li>
                    <li>￥{{ detailData.shippingFee }}</li>
                    <li>￥0.00</li>
                    <li>
                        <span v-show="detailData.couponAmount > 0">-</span>
                        ￥{{ detailData.couponAmount }}
                    </li>
                    <li>
                        <span
                            v-show="
                                detailData.preferentialPrice -
                                    detailData.couponAmount >
                                0
                            "
                            >-</span
                        >
                        ￥{{
                            (detailData.preferentialPrice -
                                detailData.couponAmount)
                                | twoPlaces
                        }}
                    </li>
                    <li class="lasts2">
                        <span class="lasts1">￥</span>
                        <span> {{ detailData.orderAmount }}</span>
                    </li>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { CartAgain } from "@/api/api_06-07-01personalMyOrders.js";
    export default {
        components: {},
        props: {
            detailData: {
                type: Object,
                required: true,
                default: function () {
                    return {};
                },
            },
        },
        data() {
            return {
                appStatus: -1,
                myTracksImgUrl: "",
            };
        },
        watch: {},
        computed: {},
        methods: {
            //立即购买
            async handlePurchase(goodsNum, specId) {
                // this.$router.push({
                //   name: "fillOrder",
                //   query: {
                //     number: goodsNum,
                //     specId: specId
                //   }
                // });
                let obj = {
                    saveCartDTO: [],
                };
                obj.saveCartDTO.push({
                    goodsNum: goodsNum,
                    specId: specId,
                });

                try {
                    let res = await CartAgain(obj);
                    console.log(res);
                    if (res && res.code == 200) {
                        // this.$Message.success("添加购物车成功");
                        this.$store.dispatch("main/actCartList"); //刷新一下购物车中的数据
                        this.$router.push({
                            path: "/shoppingCar",
                        });
                    }
                } catch (e) {}
            },
            //查看评价(单个)
            handleEvaluateDetail(orderId, orderGoodsId) {
                this.$router.push({
                    path: "/evaluationBackupsMore",
                    query: {
                        orderID: orderId,
                        orderGoodsId: orderGoodsId,
                    },
                });
            },
            //评价商品(单个)
            handleEvaluate(orderId, orderGoodsId) {
                this.$router.push({
                    path: "/shopEvaluationmore",
                    query: {
                        orderID: orderId,
                        orderGoodsId: orderGoodsId,
                    },
                });
            },
            //申请售后
            //跳转到售后申请
            handleAfterSale(orderGoodsId) {
                this.$router.push({
                    path: "/personalCenterBase/applyAfterSales",
                    query: {
                        orderGoodsId,
                    },
                });
            },
            //跳转到商品详情页
            goDetail(goodsId, specId) {
                // this.$router.push({
                //   name: "goodsDetails",
                //   query: {
                //     goodsId,
                //     specId
                //   }
                // });
                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId,
                        specId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
        },
        created() {},
        filters: {
            twoPlaces(value) {
                return value.toFixed(2);
            },
        },
        mounted() {
            this.appStatus = this.detailData.appStatus;
        },
    };
</script>
<style scoped lang="scss">
    @import "@/assets/scss/modules/order-comp.scss";
    .order-list {
        width: 100%;
        background: #ffffff;
        padding: 20px 40px 50px 40px;
        margin-top: 10px;
        .p1 {
            font-size: 18px;
            color: #3a3a3a;
            font-weight: 500;
            padding-bottom: 19px;
        }
    }
    .width1 {
        width: 303px;
    }
    .price {
        width: 141px;
        text-align: center;
    }
    .count {
        width: 141px;
        text-align: center;
    }
    .total {
        width: 141px;
        text-align: center;
    }
    .price1 {
        width: 141px;
        text-align: center;
        font-size: 14px;
        span {
            font-size: 20px;
            color: #333333;
            font-weight: 400px;
        }
    }
    .count1 {
        width: 141px;
        text-align: center;
        font-size: 20px;
        color: #333333;
    }
    .total1 {
        width: 141px;
        text-align: center;
        font-size: 14px;
        span {
            font-size: 20px;
            color: #333333;
            font-weight: 400px;
        }
    }
    .operation {
        width: 141px;
        text-align: center;
    }
    .layout {
        display: flex;
        align-items: center;
    }
    .orderlist-content {
        .content1 {
            background: #f6f6f6;
            text-align: center;
            padding: 15px 0px 15px 0;
            font-size: 14px;
            color: #717171;
        }
    }
    .img {
        width: 68px;
        height: 68px;
        margin-right: 15px;
        img {
            width: 68px;
            height: 68px;
        }
    }
    .info {
        em {
            width: 31px;
            height: 16px;
            background: linear-gradient(
                90deg,
                rgba(221, 41, 28, 1) 0%,
                rgba(255, 78, 2, 1) 100%
            );
            border-radius: 4px;
            color: #ffffff;
            text-align: center;
            line-height: 16px;
        }
        b {
            font-size: 16px;
            color: #3a3a3a;
            font-weight: 400;
        }
        p {
            font-size: 13px;
            color: #999999;
            margin-top: 10px;
        }
    }
    .button-group {
        width: 141px;
        text-align: center;
    }
    .red {
        color: #e2270b;
        font-size: 15px;
        font-weight: bold;
    }
    .normal {
        color: #717171;
        font-size: 15px;
    }
    .normal-another {
        color: #666666;
        font-size: 15px;
    }
    .total-info {
        display: flex;
        justify-content: flex-end;
        margin-top: 20px;
        margin-right: 120px;
    }
    .layout-li {
        padding: 31px 0px;
        border-bottom: 1px solid #eeeeee;
        .content2 {
            display: flex;
            align-items: center;
            cursor: pointer;
        }
    }
    .emem {
        font-style: normal;
        font-size: 12px;
        color: #ffffff;
        padding: 2px 4px;
        vertical-align: top;
    }
    .totalinfo-wrapper {
        display: flex;
        justify-content: space-between;
        .totalinfo-left {
            text-align: right;
            font-size: 14px;
            color: #666666;
            li {
                margin-top: 5px;
            }
            .lastp {
                font-size: 18px;
                color: #333333;
                font-weight: 500;
            }
        }
        .totalinfo-right {
            list-style: none;
            font-size: 14px;
            color: #717171;
            li {
                margin-top: 5px;
            }
            .lasts2 {
                font-size: 18px;
                color: #dd2619;
                .lasts1 {
                    font-size: 18px;
                    color: #dd2619;
                    margin-right: -7px;
                }
            }
        }
    }
</style>
