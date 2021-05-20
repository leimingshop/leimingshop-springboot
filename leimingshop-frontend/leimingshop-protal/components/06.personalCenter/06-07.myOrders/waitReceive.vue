<template>
    <div class="wait-receive">
        <Card class="order-info">
            <div class="o-head">
                <p class="o-time">下单时间：{{ data.createDate }}</p>
                <p class="o-num" @click="handleDetail">
                    订单编号：{{ data.orderSn }}
                </p>
                <p
                    class="o-shopName"
                    @click="handleShopHome"
                    style="width: 260px"
                >
                    店铺名称：<span :title="data.storeName">{{
                        data.storeName
                    }}</span>
                </p>
            </div>

            <div class="o-content">
                <div class="o-goods-list" :style="{ width: thead[0].width }">
                    <div
                        class="o-goods-desc"
                        v-for="(item, index) in data.orderGoodsDTOList"
                        :key="index"
                        @click="goDetail(item.goodsId, item.specId)"
                        v-lazy-container="{
                            selector: 'img',
                            error: '/img/common/loading/200_200.png',
                            loading: '/img/common/loading/200_200.png',
                        }"
                    >
                        <img
                            class="g-image"
                            :data-src="$imgURL + item.goodsImage"
                            alt
                        />
                        <p class="p-info">
                            <span
                                class="g-name twoLineEllipsis"
                                :title="item.goodsName"
                            >
                                <!--                                <em class="emem"-->
                                <!--                                    v-show="item.activityType!=0 || item.activityType">{{item.activityType | filterOrderLabel}}</em>-->
                                <span class="orderName">{{
                                    item.goodsName
                                }}</span>
                            </span>
                            <span class="g-attribute"
                                >{{ item.specAttrValueName }} 数量：{{
                                    item.goodsNum
                                }}</span
                            >
                        </p>
                    </div>
                </div>

                <div
                    id="o-consignee-name"
                    :style="{ width: thead[1].width }"
                    :title="data.trueName"
                >
                    {{ data.trueName }}
                </div>

                <div class="o-freight" :style="{ width: thead[2].width }">
                    <!-- <span>
            <i>¥</i>
            {{ data.orderAmount }}
          </span> -->
                    <div
                        class="p-price"
                        v-html="$options.filters.filterPrice(data.orderAmount)"
                    ></div>
                    <em>含运费：¥{{ data.shippingFee }}</em>
                    <em>{{ data.paymentName }}支付</em>
                </div>

                <div class="o-status" :style="{ width: thead[3].width }">
                    <span v-show="data.appStatus == 20">{{
                        status.title.titleOne
                    }}</span>
                    <span v-show="data.appStatus == 30">{{
                        status.title.titleTwo
                    }}</span>
                    <Button
                        type="text"
                        @click="handleLogistics"
                        v-show="data.appStatus == 30"
                        >查看物流</Button
                    >
                </div>

                <div class="o-operate" :style="{ width: thead[4].width }">
                    <Button
                        type="text"
                        @click="handleConfirmBefore"
                        v-show="data.appStatus == 30"
                        >确认收货</Button
                    >
                    <Button type="text" @click="handleDetail">查看详情</Button>
                    <Button type="text" @click="handleAgain">再次购买</Button>
                </div>
            </div>
        </Card>
        <!--确认收货二次确认框-->
        <second-confirm
            v-show="modal1"
            :promptTitle="promptTitle"
            @handleConfirmEnd="handleConfirmEnd"
            :orderID="data.orderSn"
            @handleConfirm="handleConfirm"
        ></second-confirm>
    </div>
</template>

<script>
    import {
        OrderConfirmReceipt,
        CartAgain,
        orderLogistics,
    } from "@/api/api_06-07-01personalMyOrders.js";
    import { mapActions } from "vuex";
    import secondConfirm from "./secondConfirm";
    export default {
        name: "waitReceive", // 个人中心 - 我的订单 - 待收货

        data() {
            return {
                modal1: false,
                promptTitle: "请确认您是否已收到货物？",
                orderId: "",
                status: {
                    title: {
                        titleOne: "待发货",
                        titleTwo: "待收货",
                    },
                },
            };
        },

        props: {
            thead: {
                type: Array,
                default: [],
            },
            data: {
                type: Object,
                required: false,
                default() {
                    return {};
                },
            },
        },

        components: {
            secondConfirm,
        },
        created() {},
        computed: {},
        watch: {},
        mounted() {},
        methods: {
            ...mapActions("main", ["actCartList"]),
            // 查看物流
            handleLogistics() {
                //有物流信息的固定id  id: "1257230560298844160"
                let _that = this;
                orderLogistics({
                    id: this.data.id,
                })
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.$store.commit("myOrders/getLogisticsList", {
                                logisticsList: res.data,
                                _that: _that,
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },
            handleConfirmBefore() {
                this.modal1 = true;
            },
            handleConfirmEnd() {
                this.modal1 = false;
            },
            //确认收货
            async handleConfirm() {
                try {
                    let res = await OrderConfirmReceipt(this.data.id);
                    if (res && res.code == 200) {
                        this.$Message.success("确认收货成功");

                        this.$emit("getDataList");
                        this.modal1 = false;
                        // this.$router.push({
                        //     path: "/shopEvaluationmore",
                        //     query: {
                        //         orderID: this.data.id,
                        //     },
                        // });
                    }
                } catch (e) {}
            },
            // 进入店铺首页
            handleShopHome() {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: {
                        storeId: this.data.storeId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
            //查看详情
            handleDetail() {
                this.$router.push({
                    path: "/personalCenterBase/myOrdersDetail",
                    query: {
                        id: this.data.id,
                    },
                });
            },
            //再次购买
            async handleAgain() {
                let obj = {
                    saveCartDTO: [],
                };
                this.data.orderGoodsDTOList.forEach((element) => {
                    obj.saveCartDTO.push({
                        goodsNum: element.goodsNum,
                        specId: element.specId,
                    });
                });
                try {
                    let res = await CartAgain(obj);
                    if (res && res.code == 200) {
                        this.actCartList();
                        this.$Message.success("添加购物车成功");
                        this.$emit("getDataList");
                        this.$router.push({
                            path: "/shoppingCar",
                        });
                    }
                } catch (e) {}
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
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/order-comp.scss";
</style>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .o-operate {
        button {
            font-size: 13px;
            color: #666666;

            &:nth-child(1) {
                font-size: 14px;
                font-weight: 600;
                color: #dd2619;
            }

            &:nth-child(2) {
                margin: 4px 0 -8px;
            }
            &:hover {
                color: #dd2619;
            }
        }
    }

    //确认收货二次确认弹窗
    .mask-wrapper {
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.3);
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 1000;

        .mask-content {
            background: #ffffff;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 300px;
            border-radius: 5px;

            .p1 {
                text-align: right;
                padding: 5px 15px 0 0;
                font-size: 18px;
            }

            .p2 {
                text-align: center;
                margin-top: 15px;
            }

            .button-group {
                display: flex;
                justify-content: center;
                margin-top: 30px;
                margin-bottom: 20px;

                .button-left {
                    background: #f5f5f5;
                    width: 100px;
                    text-align: center;
                    padding: 5px 0;
                    border-radius: 3px;
                    cursor: pointer;
                }

                .button-right {
                    background: #f36d69;
                    width: 100px;
                    text-align: center;
                    padding: 5px 0;
                    color: #ffffff;
                    margin-left: 40px;
                    border-radius: 3px;
                    cursor: pointer;
                }
            }
        }
    }
    .o-num:hover {
        color: #dd2619;
    }
    .p-price {
        color: #222222;
    }
</style>
