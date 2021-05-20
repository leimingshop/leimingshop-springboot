<template>
    <div class="myDiv">
        <order-detailhead
            :detailData="detailData"
            v-if="detailData.id"
        ></order-detailhead>
        <order-detailbar
            :detailData="detailData"
            v-if="detailData.id"
        ></order-detailbar>
        <order-detailinfo
            :detailData="detailData"
            v-if="detailData.id"
        ></order-detailinfo>
        <commodity-list
            :detailData="detailData"
            v-if="detailData.id"
        ></commodity-list>
        <reason-popup
            v-if="$store.state.myOrders.reasonList.length > 0"
        ></reason-popup>
    </div>
</template>

<script>
    import orderDetailhead from "@/components/06.personalCenter/06-07.myOrders/orderDetailHead"; //详情页头部
    import orderDetailbar from "@/components/06.personalCenter/06-07.myOrders/orderDetailBar"; //详情页步骤条部分
    import orderDetailinfo from "@/components/06.personalCenter/06-07.myOrders/orderDetailInfo"; //详情页地址信息部分
    import commodityList from "@/components/06.personalCenter/06-07.myOrders/commodityList"; //详情页商品信息部分
    import reasonPopup from "@/components/06.personalCenter/06-07.myOrders/reasonPopup.vue"; //取消订单的理由
    import { orderDetails } from "@/api/api_06-07-01personalMyOrders.js";
    export default {
        components: {
            orderDetailhead,
            orderDetailbar,
            orderDetailinfo,
            commodityList,
            reasonPopup,
        },
        props: {},
        head() {
            return {
                title: "订单详情",
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
                detailData: {}, //订单所有信息
            };
        },
        watch: {},
        computed: {},
        methods: {
            getOrderDetails(parameter) {
                //请求订单的常规信息
                orderDetails(parameter)
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.detailData = res.data;
                        }
                    })
                    .catch((err) => {});
            },
        },
        created() {},
        mounted() {
            this.getOrderDetails({ id: this.$route.query.id });
        },
    };
</script>
<style scoped lang="scss">
    //取消订单理由复选框的样式
    .myDiv /deep/ {
        .ivu-radio {
            // display: none;
        }
        .ivu-radio-group {
            margin-top: 12px;
        }
        .ivu-radio-border {
            border: 1px solid #dcdee2;
            border-radius: 4px;
            height: 32px;
            line-height: 42px;
            transition: border 0.2s ease-in-out;
            width: 228px;
            height: 42px;
            text-align: center;
            margin-top: 18px;
        }
    }
    /deep/ .personal-center-base {
        padding-bottom: 60px !important ;
    }
    /deep/ .totalinfo-wrapper .totalinfo-right .lasts2 {
        font-weight: bold !important;
    }
    /deep/ .orderdetail-head .head-right .button-left {
        width: 100px !important;
        height: 40px !important;
        font-size: 14px !important;
        line-height: 40px !important;
        padding: 0px !important;
        border-radius: 22px !important;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
    }
    /deep/ .orderdetail-head .head-right .button-right {
        width: 100px !important;
        height: 40px !important;
        font-size: 14px !important;
        line-height: 40px !important;
        padding: 0px !important;
        font-family: PingFangSC-Medium, PingFang SC;
        border-radius: 22px !important;
        font-weight: 500;
    }
    /deep/ .orderhead-wrapper {
        height: 120px !important;
        box-shadow: 0px 2px 6px 0px rgba(0, 0, 0, 0.1) !important;
    }
    /deep/ .orderDetailInfo .logistics-info .logistics-top {
        font-size: 14px !important;
        font-weight: 600 !important;
    }
    /deep/ .logistics-left {
        font-size: 12px !important;
        color: #666666 !important;
    }
    /deep/ .receive-info .receive1 {
        font-size: 14px !important;
        font-weight: 600;
    }
    /deep/ .invoice-info .receive1 {
        font-size: 14px !important;
        font-weight: 600;
    }
    /deep/ .invoice-info .receive2,
    .invoice-info .receive3,
    .invoice-info .receive4 {
        margin-top: 22px !important;
    }
    /deep/ .pay-method .pay-method1 {
        font-size: 14px !important;
        font-weight: 600 !important;
    }
    /deep/ .order-list .p1 {
        font-size: 14px !important;
        color: #333333 !important;
        font-weight: 600 !important;
    }
    /deep/ .pay-method {
        width: 100% !important;
    }
    /deep/ .orderlist-content .content1 {
        font-size: 12px !important;
        padding: 0px !important;
        height: 40px !important;
        line-height: 40px !important;
    }
    /deep/ .info b {
        font-size: 14px !important;
        color: #222222 !important;
    }
    /deep/ .price1 span {
        font-size: 14px !important;
        color: #000000 !important;
    }
    /deep/ .count1 {
        font-size: 14px !important;
        color: #222222 !important;
        font-family: PingFangSC-Regular, PingFang SC;
    }
    /deep/ .total1 span {
        font-size: 14px !important;
        color: #222222 !important;
    }
    /deep/ .red {
        font-size: 12px !important;
        font-family: PingFangSC-Medium, PingFang SC;
        color: #dd2619 !important;
    }
    /deep/ .layout-li {
        padding: 29px 0px !important;
    }
    /deep/ .orderdetail-head .head-main1 {
        font-size: 14px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
    }
    /deep/ .steps-box .line {
        left: 55px !important;
        width: 87%;
    }
    /deep/ .logistics-content .logistics-left li img {
        margin-right: 10px;
    }
    /deep/ .invoice-info {
        width: 49.5% !important;
    }
    /deep/ .receive-info {
        width: 49.5% !important;
    }
</style>
