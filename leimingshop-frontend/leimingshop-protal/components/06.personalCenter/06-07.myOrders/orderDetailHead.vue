<template>
    <div class="orderhead-wrapper">
        <div class="orderdetail-head">
            <div class="head-left">
                <div>
                    <img
                        src="/img/06.personalCenter/cancel.png"
                        alt
                        v-show="appStatus == 0"
                    />
                    <img
                        src="/img/06.personalCenter/time.png"
                        alt
                        v-show="appStatus == 10"
                    />
                    <img
                        src="/img/06.personalCenter/finish.png"
                        alt
                        v-show="appStatus == 40"
                    />
                    <img
                        src="/img/06.personalCenter/wait.png"
                        alt
                        v-show="appStatus == 20 || appStatus == 30"
                    />
                    <p v-show="appStatus == 10" style="state-style">等待付款</p>
                    <p v-show="appStatus == 20" style="state-style">等待发货</p>
                    <p v-show="appStatus == 30" style="state-style">等待收货</p>
                    <p v-show="appStatus == 40" style="state-style">交易完成</p>
                    <p v-show="appStatus == 0" style="state-style">订单取消</p>
                </div>
                <div class="head-main1" v-show="appStatus == 10">
                    <div>
                        <p>订单编号：{{ detailData.orderSn }}</p>
                        <p>
                            下单时间：
                            <span class="time1">{{
                                detailData.createDate
                            }}</span>
                            <span>付款截止：</span>
                            <span
                                v-if="timeClock"
                                style="color: #dd2619; font-weight: 500"
                                >剩余{{ timeClock }}</span
                            >
                            <span v-else>剩余00时00分00秒</span>
                        </p>
                    </div>
                </div>
                <div class="head-main2" v-show="appStatus != 10">
                    <div>
                        <p>
                            订单编号：{{ detailData.orderSn }}
                            <span style="margin-left: 20px">下单时间：</span>
                            <span class="time2">{{
                                detailData.createDate
                            }}</span>
                        </p>
                    </div>
                </div>
            </div>
            <div class="head-right">
                <!-- 待付款-->
                <div
                    class="button-left"
                    v-show="appStatus == 10"
                    @click="toPay"
                >
                    立即付款
                </div>
                <div
                    class="button-right"
                    v-show="appStatus == 10"
                    @click="cancelOrder"
                >
                    取消订单
                </div>
                <!-- 待发货 -->
                <div
                    class="button-left"
                    v-show="appStatus == 20"
                    @click="handleAgain"
                >
                    再次购买
                </div>
                <!-- 待收货 -->
                <div
                    class="button-left"
                    v-show="appStatus == 30"
                    @click="handleConfirmBefore"
                >
                    确认收货
                </div>
                <div
                    class="button-right"
                    v-show="appStatus == 30"
                    @click="handleAgain"
                >
                    再次购买
                </div>
                <!-- 待评价 -->
                <div
                    class="button-left"
                    v-show="appStatus == 40 && detailData.evaluateStatus == 0"
                    @click="evaluate"
                >
                    评价商品
                </div>
                <div
                    class="button-right"
                    v-show="appStatus == 40"
                    @click="afterSales"
                >
                    申请售后
                </div>
                <!-- 交易完成 -->
                <div
                    class="button-right"
                    v-show="appStatus == 40 && detailData.evaluateStatus == 1"
                    @click="handleEvaluateDetail"
                >
                    查看评价
                </div>
            </div>
        </div>
        <!--确认收货二次确认框-->
        <!-- <second-confirm
      v-show="modal1"
      :promptTitle="promptTitle"
      @handleConfirmEnd="handleConfirmEnd"
      @handleConfirm="handleConfirm"
    ></second-confirm> -->
        <Modal v-model="modal1" width="400" class="delete">
            <div style="text-align: center">
                <p style="font-size: 24px; color: #333">{{ promptTitle }}</p>
            </div>
            <div slot="footer" class="footerBtn">
                <Button
                    class="newBtn newBtn1"
                    style="width: 68px; background: #f5f5f5; margin-right: 48px"
                    size="default"
                    @click.stop="handleConfirmEnd"
                    >取消</Button
                >
                <Button
                    class="newBtn"
                    style="
                        width: 68px;
                        margin: 0 0 0 10px;
                        background: rgba(221, 38, 25, 1);
                        color: rgba(255, 255, 255, 1);
                    "
                    size="default"
                    @click="handleConfirm"
                    >确定</Button
                >
            </div>
        </Modal>
    </div>
</template>

<script>
    import {
        OrderCancelReason,
        CartAgain,
        OrderConfirmReceipt,
    } from "@/api/api_06-07-01personalMyOrders.js";
    import { on, off, handleScroll, debounce } from "@/utils/dom";
    import secondConfirm from "./secondConfirm";
    export default {
        components: {
            secondConfirm,
        },
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
                modal1: false,
                promptTitle: "是否确认收货",
                appStatus: -1,
                id: "",
                orderGoodsDTOList: {}, //商品的相关信息
                timeClock: "",
                reasonList: [],
            };
        },
        watch: {},
        computed: {},
        methods: {
            //去支付
            toPay() {
                this.$router.push({
                    path: "/toPay",
                    query: { orderId: this.detailData.id },
                });
            },
            countTime(cancelDate, currentTime) {
                console.log("当前时间" + currentTime);
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
                        if (this.detailData.appStatus == 10) {
                            this.$router.push({
                                path: "/personalCenterBase/myOrders",
                            });
                        }
                    }
                }, 1000);
            },
            //取消订单
            cancelOrder() {
                console.log(123);
                OrderCancelReason({ type: 3 })
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.reasonList = res.data;
                            this.$store.commit("myOrders/getReasonList", {
                                reasonList: this.reasonList,
                                id: this.detailData.id,
                            });
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            //去评价
            evaluate() {
                this.$router.push({
                    path: "/shopEvaluationmore",
                    query: {
                        orderID: this.detailData.id,
                    },
                });
            },
            //申请售后
            afterSales() {
                this.$router.push({
                    path: "/personalCenterBase/afterSale",
                    query: {
                        orderSn: this.detailData.orderSn,
                    },
                });
            },
            //再次购买
            async handleAgain() {
                let obj = {
                    saveCartDTO: [],
                };
                this.detailData.orderDetailGoodsListDTOList.forEach(
                    (item1, index1) => {
                        item1.orderGoodsDTOList.forEach((item2, index2) => {
                            obj.saveCartDTO.push({
                                goodsNum: item2.goodsNum,
                                specId: item2.specId,
                            });
                        });
                    }
                );
                try {
                    let res = await CartAgain(obj);
                    if (res && res.code == 200) {
                        // this.$Message.success("添加购物车成功");
                        this.$store.dispatch("main/actCartList"); //刷新一下购物车中的数据
                        this.$router.push({
                            path: "/shoppingCar",
                        });
                    }
                } catch (e) {}
            },
            //查看评价
            handleEvaluateDetail() {
                this.$router.push({
                    path: "/evaluationBackupsMore",
                    query: {
                        orderID: this.detailData.id,
                    },
                });
            },
            handleConfirmBefore() {
                this.modal1 = true;
                window.scroll(0, 0);
                document.body.style.overflow = "hidden";
            },
            handleConfirmEnd() {
                window.scroll(0, 0);
                this.modal1 = false;
                document.body.style.overflow = "auto";
            },
            //确认收货
            async handleConfirm() {
                document.body.style.overflow = "auto";
                try {
                    let res = await OrderConfirmReceipt(this.detailData.id);
                    if (res && res.code == 200) {
                        this.$router.push({
                            path: "/personalCenterBase/myOrders",
                        });
                        this.$Message.success("确认收货成功");
                        this.$emit("getDataList");
                        this.modal1 = false;
                    }
                } catch (e) {}
            },
            //让表头固定在顶部
            handleAffix() {
                let el = document.querySelector(".orderhead-wrapper");
                let style =
                    "width:948px;position: fixed; top: 100px; z-index: 1000; transform: translateZ(0);box-shadow:0px 2px 6px 0px rgba(0,0,0,0.1);";
                handleScroll(el, 230, style);
            },
        },
        created() {},
        mounted() {
            this.appStatus = this.detailData.appStatus;
            this.id = this.$route.query.id;
            setTimeout(() => {
                this.orderGoodsDTOList = this.detailData.orderDetailGoodsListDTOList[0].orderGoodsDTOList[0];
                // console.log("头部信息"+this.detailData);
                this.countTime(
                    this.detailData.cancelDate,
                    this.detailData.currentTime
                );
            }, 300);
            on(window, "scroll", debounce(this.handleAffix, 10));
        },
        beforeDestroy() {
            off(window, "scroll", this.handleAffix);
        },
    };
</script>
<style scoped lang="scss">
    @import "@/assets/scss/modules/order-comp.scss";
    @import "@/assets/scss/modules/modal.scss";

    /deep/ .ivu-modal {
        border: 8px solid rgba(0, 0, 0, 0.15);
        border-radius: 10px;
    }
    .delete {
        /deep/ .ivu-modal-content {
            border-radius: 0;
        }
        /deep/ .ivu-modal-body {
            padding: 40px 16px 30px 16px;
        }
        /deep/ .ivu-modal-footer {
            border: none;
            height: 90px;
            .footerBtn {
                display: flex;
                justify-content: center;
                padding: 0;
            }
        }
        /deep/ .ivu-modal-close {
            right: 10px;
            top: 10px;
        }
    }
    .newBtn {
        width: 69px;
        height: 34px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 2px;
        &:hover {
            color: #333333;
        }
    }
    .newBtn1:hover {
        border-color: #dcdee2;
    }
    .orderdetail-head {
        width: 948px;
        background: #ffffff;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 30px 40px 30px 0;
        .head-left {
            margin-left: 50px;
            text-align: center;
            display: flex;
            align-items: center;
            img {
                width: 36px;
            }
            p {
                font-size: 14px;
                color: #666666;
            }
        }
        .head-main1 {
            margin-left: 40px;
            font-size: 16px;
            color: #666666;
            text-align: left;
            p:nth-child(2) {
                margin-top: 6px;
            }
            .time1 {
                margin-right: 30px;
            }
        }
        .head-main2 {
            margin-left: 60px;
        }
        .head-right {
            display: flex;
            .button-left {
                width: 120px;
                background: linear-gradient(
                    90deg,
                    rgba(222, 42, 28, 1) 0%,
                    rgba(255, 78, 3, 1) 100%
                );
                font-size: 15px;
                color: #ffffff;
                padding: 12px 0;
                text-align: center;
                border-radius: 30px;
                margin-left: 20px;
                cursor: pointer;
                &:hover {
                    opacity: 0.8;
                }
            }
            .button-right {
                width: 120px;
                border: 1px solid #999999;
                font-size: 15px;
                color: #999999;
                padding: 12px 0;
                text-align: center;
                border-radius: 30px;
                margin-left: 10px;
                cursor: pointer;
                &:hover {
                    opacity: 0.8;
                }
            }
        }
    }
    .state-style {
        font-size: 16px;
        color: #424242;
    }
    .orderhead-wrapper {
        width: 100%;
        height: 102px;
    }
</style>
