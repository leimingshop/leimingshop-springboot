<template>
    <div>
        <PaymentList
            v-show="showPageNum == 1"
            ref="paymentListCompon"
            @chagePageNum="chagePageNum"
        ></PaymentList>
        <orderList
            v-if="showPageNum == 2"
            ref="orderListCompon"
            @chagePageNum="chagePageNum"
        ></orderList>
        <orderDetail
            v-if="showPageNum == 3"
            ref="orderDetailCompon"
            @chagePageNum="chagePageNum"
            @changePage="changePage"
            :orderDetBreaddata="orderDetBreaddata"
            :index="index"
        ></orderDetail>
    </div>
</template>

<script>
    import PaymentList from "./paymentList";
    import orderList from "./orderList";
    import orderDetail from "@/views/modules/mgorder/order/list/orderDet";
    export default {
        data() {
            return {
                showPageNum: 1,
                index: "2",
                paymentSn: "",
                orderDetBreaddata: ["财务管理", "支付管理", "交易记录", "订单详情"],
            };
        },
        components: {
            PaymentList,
            orderList,
            orderDetail,
        },
        methods: {
            chagePageNum(num, row) {
                this.showPageNum = num;
                this.$nextTick(() => {
                    if (this.showPageNum == 2) {
                        this.$refs.orderListCompon.init(row);
                    } else if (this.showPageNum == 3) {
                        this.$refs.orderDetailCompon.init(row);
                    }
                });
            },
            changePage() {
                this.showPageNum = 1;
            },
            // orderDetail(index) {
            //     this.$nextTick(()=>{
            //         this.$refs.paymentListCompon.handleEdit(index);
            //     })
            // }
        },
    };
</script>
