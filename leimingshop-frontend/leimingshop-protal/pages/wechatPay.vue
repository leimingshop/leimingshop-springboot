<template>
    <div class="wechet-pay">
        <!-- 顶部搜索部分 -->
        <searchPart></searchPart>
        <!-- 面包屑 -->
        <breadcrumb1 :list="breadcrumbList" />
        <div class="pay-body">
            <div class="pay-body-header">
                <span class="order-id" v-show="payState == 0"
                    >订单提交成功，请尽快付款！订单号：{{ orderSn }}</span
                >
                <span class="order-id" v-show="payState == 2"
                    >订单已过期或取消，请重新下单</span
                >
            </div>
            <div class="pay-title">
                <div class="pay-title-info">
                    <span class="pay-title-text">微信支付</span>
                    <div class="time-clock" v-show="payState == 0">
                        距离二维码过期还剩
                        <span>{{ timeClock }}</span
                        >，过期后请刷新界面重新获取二维码
                    </div>
                </div>
                <div class="orderPrice">
                    应付金额
                    <span>{{ orderPrice }}</span> 元
                </div>
            </div>
            <div class="pay-content">
                <div class="content-left">
                    <div class="QR-code-box">
                        <div
                            v-show="payState == 2 || timeClock == '0分0秒'"
                            class="QR-timeout"
                        >
                            二维码已失效
                        </div>
                        <img :src="QRCode" alt />
                    </div>
                    <img
                        src="/img/04.shoppingCar/scanning.png"
                        alt
                        class="use-weChat"
                    />
                    <a class="goOthers" @click="changePayStyle()"
                        >选择其他支付方式</a
                    >
                </div>
                <img class="phone" src="/img/04.shoppingCar/example.png" alt />
            </div>
        </div>
    </div>
</template>

<script>
    // 搜索头部
    import searchPart from "@/components/common/searchPartWhite.vue";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    import { wechatPay } from "@/api/api_04.shoppingCar.js";
    export default {
        head() {
            return {
                title: "微信支付",
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
                QRCode: "",
                orderSn: "",
                orderPrice: "",
                payState: "",
                timeClock: "",
                breadcrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "支付成功",
                        toPath: "",
                    },
                ]),
            };
        },
        components: {
            searchPart,
            breadcrumb1,
        },
        mounted() {
            this.getQR();
        },
        methods: {
            getQR() {
                wechatPay({
                    paySn: this.$route.query.paySn,
                    tradeType: "NATIVE",
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.QRCode = res.data.payCodeBase64;
                            this.orderSn = res.data.orderSn;
                            this.orderPrice = res.data.orderPrice;
                            this.payState = res.data.payState;
                            this.timeMachine();
                            if (res.data.payState == 0) {
                                //订单未支付
                                var maxtime = 120; //秒计算，自己调整!
                                var hours = "";
                                var minutes = "";
                                var seconds = "";
                                var timer = setInterval(() => {
                                    if (maxtime >= 0) {
                                        hours = Math.floor(maxtime / 60 / 60);
                                        minutes = Math.floor(maxtime / 60);
                                        seconds = Math.floor(maxtime % 60);
                                        this.timeClock =
                                            minutes + "分" + seconds + "秒";
                                        --maxtime;
                                    } else {
                                        clearInterval(timer);
                                    }
                                }, 1000);
                            } else if (res.data.payState == 1) {
                                //订单已支付成功

                                this.$router.push({
                                    path: "/paySuccess",
                                    query: {
                                        price: this.orderPrice,
                                        payType: 2,
                                        orderId: this.$route.query.orderId,
                                    },
                                });
                            } else if (res.data.payState == 2) {
                                //订单已过期或取消
                                console.log("订单已取消");
                            }
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            timeMachine() {
                wechatPay({
                    paySn: this.$route.query.paySn,
                    tradeType: "NATIVE",
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.orderPrice = res.data.orderPrice;
                            if (res.data.payState == 0) {
                                //订单未支付
                                setTimeout(() => {
                                    this.timeMachine();
                                }, 3000);
                            } else if (res.data.payState == 1) {
                                //订单已支付成功
                                this.$router.push({
                                    path: "/paySuccess",
                                    query: {
                                        price: this.orderPrice,
                                        payType: 2,
                                        orderId: this.$route.query.orderId,
                                    },
                                });
                            } else if (res.data.payState == 2) {
                                //订单已过期或取消
                                console.log("订单已取消");
                            }
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            changePayStyle() {
                this.$router.push({
                    path: "/toPay",
                    query: {
                        orderId: this.$route.query.orderId,
                    },
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .wechet-pay {
        width: 100%;
        height: 955px;
        background-color: #f6f6f6;
        margin: 0 auto;

        .ivu-breadcrumb {
            width: 1200px;
            margin: 0 auto;
            line-height: 40px;
            span {
                color: #666666;
                font-size: 13px;
            }
        }

        .pay-body {
            width: 1200px;
            height: 610px;
            background: #fff;
            padding: 0 30px;
            margin: 0 auto 80px auto;

            .pay-body-header {
                height: 82px;
                display: flex;
                justify-content: space-between;
                align-items: center;

                .order-id {
                    font-family: PingFangSC-Regular, PingFang SC;
                    color: rgba(51, 51, 51, 1);
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #666666;
                    line-height: 14px;
                }

                div {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(102, 102, 102, 1);
                    line-height: 14px;

                    span {
                        font-size: 16px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: rgba(221, 38, 25, 1);
                        line-height: 16px;
                    }
                }
            }

            .pay-title {
                margin: 11px 0 0 0;
                font-size: 18px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(58, 58, 58, 1);
                line-height: 18px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                .pay-title-info {
                    display: flex;
                }
                .pay-title-text {
                    font-size: 16px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: #222222;
                    line-height: 18px;
                }
                div {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(58, 58, 58, 1);
                    line-height: 18px;
                    height: 18px;
                    span {
                        color: #dd2619;
                    }
                }
                .time-clock {
                    height: 18px;
                    font-size: 14px;
                    line-height: 20px;
                    vertical-align: bottom;
                    padding-left: 10px;
                }
                .orderPrice {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(34, 34, 34, 1);
                    line-height: 14px;
                    // align-items: flex-end;
                    span {
                        font-size: 16px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: rgba(221, 38, 25, 1);
                        line-height: 16px;
                    }
                }
            }

            .pay-content {
                width: 750px;
                margin: 30px auto 0 auto;
                display: flex;

                .content-left {
                    margin: 0 100px 0 0;

                    .QR-code-box {
                        width: 308px;
                        height: 308px;
                        background: rgba(255, 255, 255, 1);
                        border: 1px solid rgba(235, 235, 235, 1);
                        margin: 0 0 24px 0;
                        position: relative;

                        .QR-timeout {
                            width: 306px;
                            height: 306px;
                            position: absolute;
                            top: 0;
                            background-color: rgba(0, 0, 0, 0.8);
                            color: #ffffff;
                            font-size: 24px;
                            text-align: center;
                            line-height: 318px;
                        }

                        img {
                            width: 286px;
                            height: 286px;
                            display: block;
                            position: absolute;
                            top: 11px;
                            left: 10px;
                        }
                    }

                    .goOthers {
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(4, 145, 255, 1);
                        display: block;
                    }
                    .use-weChat {
                        width: 306px;
                    }
                }

                .phone {
                    width: 277px;
                    height: 354px;
                }
            }
        }
    }
</style>
