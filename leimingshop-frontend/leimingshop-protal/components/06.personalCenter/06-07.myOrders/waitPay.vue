<template>
    <div class="wait-pay">
        <Card class="order-info" style="width: 864px">
            <div class="o-head">
                <p class="o-time">下单时间：{{ data.createDate }}</p>
                <p class="o-num" @click="handleDetail">
                    订单编号：{{ data.orderSn }}
                </p>
                <p
                    class="o-shopName arttextoverflow"
                    :title="data.storeName"
                    @click="handleShopHome"
                    v-if="data.storeId != 0"
                >
                    店铺名称：
                    <span :title="data.storeName">{{ data.storeName }}</span>
                </p>
                <p
                    class="o-shopName arttextoverflow"
                    :title="data.storeName"
                    v-else
                >
                    店铺名称：
                    <span :title="data.storeName">{{ data.storeName }}</span>
                </p>
                <p class="o-deadline" v-if="data.appStatus == 10 && timeClock">
                    付款截止：
                    <span v-if="timeClock">剩余{{ timeClock }}</span>
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
                                <!--                <em-->
                                <!--                  class="emem"-->
                                <!--                  v-show="item.activityType!=0 || item.activityType"-->
                                <!--                >{{item.activityType | filterOrderLabel}}</em>-->
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
                    <em>{{ data.paymentName }}</em>
                </div>

                <div
                    class="o-status"
                    :style="{ width: thead[3].width }"
                    style="color: #dd2619; font-size: 12px; font-weight: 400"
                >
                    {{ status.title }}
                </div>

                <div class="o-operate" :style="{ width: thead[4].width }">
                    <Button
                        v-for="item in operate"
                        :key="item.title"
                        :type="item.type"
                        v-text="item.title"
                        @click="item.handler"
                    ></Button>
                </div>
            </div>
        </Card>
    </div>
</template>

<script>
    import { OrderCancel } from "@/api/api_06-07-01personalMyOrders.js";
    // import reasonPopup from "@/components/06.personalCenter/06-07.myOrders/reasonPopup.vue";
    import { OrderCancelReason } from "@/api/api_06-07-01personalMyOrders.js";
    export default {
        name: "waitPay", // 个人中心 - 我的订单 - 待付款
        data() {
            return {
                reasonList: [],
                order: "",
                status: {
                    title: "待付款",
                },

                operate: [
                    {
                        title: "立即付款",
                        handler: this.handlePay,
                        type: "text",
                    },

                    {
                        title: "查看详情",
                        handler: this.handleDetail,
                        type: "text",
                    },

                    {
                        title: "取消订单",
                        handler: this.handleCancel,
                        type: "text",
                    },
                ],
                timeClock: "", //付款截止时间
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
            page: {
                type: Number,
                default: 0,
            },
        },
        components: {},
        created() {},
        computed: {},
        watch: {
            "data.currentTime"(val) {
                console.log(val);
            },
        },
        mounted() {
            if (this.data.cancalDate) {
                this.countTime(this.data.cancalDate, this.data.currentTime);
            }
            //获取到的时间
        },
        methods: {
            //去付款
            handlePay() {
                this.$router.push({
                    path: "/toPay",
                    query: { orderId: this.data.id },
                });
            },
            // 进入店铺首页
            handleShopHome() {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: { storeId: this.data.storeId },
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
            //计算付款剩余时间
            countTime(cancelDate, currentTime) {
                var dateBegin = currentTime; //将-转化为/，使用new Date
                var dateEnd = new Date(cancelDate.replace(/-/g, "/")); //获取当前时间
                var dateDiff = dateEnd.getTime() - dateBegin; //时间差的毫秒数
                var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000)); //计算出相差天数
                var leave1 = dateDiff % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
                var hours = Math.floor(leave1 / (3600 * 1000)); //计算出小时数
                //计算相差分钟数
                var leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
                var minutes = Math.floor(leave2 / (60 * 1000)); //计算相差分钟数
                //计算相差秒数
                var leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
                var seconds = Math.round(leave3 / 1000);
                // console.log(" 相差 " + dayDiff + "天 " + hours + "小时 " + minutes + " 分钟" + seconds + " 秒")
                // console.log(dateDiff + "时间差的毫秒数", dayDiff + "计算出相差天数", leave1 + "计算天数后剩余的毫秒数", hours + "计算出小时数",
                //     minutes + "计算相差分钟数", seconds + "计算相差秒数");
                var maxtime = leave1 / 1000; //一个小时，按秒计算，自己调整!
                var timer = setInterval(() => {
                    if (maxtime >= 0) {
                        hours = Math.floor(maxtime / 60 / 60);
                        minutes = Math.floor(maxtime / 60);
                        minutes = minutes % 60;
                        seconds = Math.floor(maxtime % 60);
                        this.timeClock =
                            hours + "时" + minutes + "分" + seconds + "秒";
                        --maxtime;
                    } else {
                        clearInterval(timer);
                        // this.$emit("getDataList");
                    }
                }, 1000);
            },
            //取消订单
            handleCancel() {
                OrderCancelReason({ type: 3 })
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.reasonList = res.data;
                            this.$store.commit("myOrders/getReasonList", {
                                reasonList: this.reasonList,
                                id: this.data.id,
                            });
                            this.$emit("childFn", this.order);
                        }
                    })
                    .catch((err) => console.log(err));
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
    $primary-color: #dd2619;

    .order-info {
        p.o-deadline {
            span {
                color: $primary-color;
            }
        }
    }

    .o-operate {
        button {
            font-size: 12px;
            color: #666666;
            &:nth-child(1) {
                height: 14px;
                line-height: 14px;
                font-size: 14px !important;
                font-weight: 600;
                color: #dd2619 !important;
            }
            &:nth-child(2) {
                // margin: 4px 0 -8px;
            }
            &:hover {
                color: #dd2619;
            }
        }
    }
    .wait-pay {
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .o-head {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
    }
    /deep/.order-info .o-head[data-v-f7cfbed2] {
        width: 864px !important;
    }
    /deep/ .orderTop[data-v-279bd86b] {
        margin: 20px 40px !important;
    }
    /deep/ .ivu-btn-text {
        color: #666666;
        font-size: 12px;
    }
    /deep/ .order-info .o-content > div.o-freight em {
        margin: 0 0 16px !important;
    }
    /deep/.order-info .o-content > div.o-freight span {
        margin-bottom: 10px !important;
    }
</style>
