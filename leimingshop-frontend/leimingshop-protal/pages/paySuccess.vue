<template>
    <div class="to-pay">
        <div class="search-part">
            <searchPart></searchPart>
        </div>
        <!-- 面包屑 -->
        <breadcrumb1 :list="breadcrumbList" />

        <div class="to-pay-body">
            <div class="head-word">
                <img src="/img/01.index/success.png" class="head-left-icon" />
                <div>订单支付成功！</div>
            </div>
            <p class="serve-word">我们将尽快为您发货，收货期间请保持手机畅通</p>
            <div class="order-info">
                <span>支付方式</span>
                <span v-show="$route.query.payType == 1" class="order-info-mode"
                    >支付宝支付</span
                >
                <span v-show="$route.query.payType == 2" class="order-info-mode"
                    >微信支付</span
                >
            </div>
            <div class="order-info">
                <span>支付金额</span>
                <span style="color: #dd2619; font-weight: 500"
                    >￥{{ $route.query.price }}</span
                >
            </div>
            <div class="serve-message">
                温馨提示：雷铭商城不会以订单异常、系统升级为由要求您点击任何网址链接进行退款操作。
            </div>
            <div class="bottom-button">
                <a class="red-button" @click="$router.push({ path: '/' })"
                    >进入首页</a
                >
                <a class="gray-button" @click="gohandleDetail()">查看订单</a>
            </div>
        </div>
    </div>
</template>

<script>
    import { orderDetails } from "@/api/api_06-07-01personalMyOrders.js";
    import searchPart from "@/components/common/searchPartWhite.vue";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    import { mapState, mapActions } from "vuex";
    export default {
        head() {
            return {
                title: "订单支付成功",
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
                modal: false,
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
        computed: {
        //     ...mapState("searchGoodsResult", ["breadcrumbList"]),

            activitySearchForm() {
                return {
                    ...this.activity, // 活动
                    ...this.searchDataForm, // 搜索字段
                    ...this.filterData, // 过滤字段
                    ...this.priceData, // 价格字段
                };
            },
        },
        methods: {
            ok() {
                this.$Message.info("Clicked ok");
            },
            cancel() {
                this.$Message.info("Clicked cancel");
            },
            gohandleDetail() {
                var orderId = {
                    id: this.$route.query.orderId,
                };
                orderDetails(orderId)
                    .then((res) => {
                        if (res.code == 200) {
                            this.$router.push({
                                path: "/personalCenterBase/myOrdersDetail",
                                query: {
                                    id: orderId.id,
                                    appStatus: res.data.appStatus,
                                },
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },
        },
    };
</script>

<style lang="scss" scoped>
    .to-pay {
        width: 100%;
        background-color: #f6f6f6;
        margin: 0 auto;
        padding-bottom: 50px;

        .to-pay-body {
            width: 1200px;
            height: 650px;
            background-color: #fff;
            margin: 0 auto;
            overflow: hidden;
            position: relative;

            .head-word {
                width: 300px;
                margin: 140px auto 0 auto;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #222222;
                line-height: 14px;
                display: flex;
                flex-direction: column;
                align-items: center;

                img {
                    width: 60px;
                    height: 60px;
                    margin: 0 0 20px 0;
                }
            }

            .serve-word {
                width: 300px;
                margin: 12px auto 20px auto;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #666666;
                line-height: 12px;
                text-align: center;
            }

            .order-info {
                width: 610px;
                padding-left: 250px;
                margin: 12px auto 0 auto;
                display: flex;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #666666;
                line-height: 12px;

                .order-info-mode {
                    margin-left: 11px;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #222222;
                    line-height: 12px;
                }
            }

            .order-info span:last-child {
                margin: 0 0 0 11px;
                color: #333;
            }

            .serve-message {
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #666666;
                line-height: 12px;
                position: absolute;
                bottom: 20px;
                left: 20px;
            }

            .bottom-button {
                width: 150px;
                margin: 20px auto 0 auto;
                display: flex;
                justify-content: space-between;

                .red-button {
                    width: 70px;
                    height: 30px;
                    background: linear-gradient(90deg, #de2a1c 0%, #ff4e03 100%);
                    border-radius: 15px;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: #ffffff;
                    line-height: 30px;
                    text-align: center;
                }

                .red-button:hover {
                    background-color: #e45147;
                }

                .gray-button {
                    width: 70px;
                    height: 30px;
                    background: #f5f5f5;
                    border-radius: 15px;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: #666666;
                    line-height: 30px;
                    text-align: center;
                }

                .gray-button:hover {
                    background-color: #fafafa;
                }
            }
        }
    }
</style>
