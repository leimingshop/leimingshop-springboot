<template>
    <Card class="separate">
        <div class="s-table-head">
            <p class="s-time">下单时间：{{ data.createDate }}</p>
            <p class="s-num">订单编号：{{ data.orderSn }}</p>
            <p class="s-prompt">
                您订单中商品在不同的库房或商家，故拆为以下订单分开配送，给您带来不便敬请谅解。
            </p>
        </div>
        <template v-for="item in data.orderChildrenList">
            <!-- 待付款 -->
            <wait-pay
                :thead="thead"
                v-show="item.appStatus == 10"
                :data="item"
                @getDataList="getDataList"
            ></wait-pay>
            <!-- 已取消-->
            <canceled
                :thead="thead"
                v-show="item.appStatus == 0"
                :data="item"
                @getDataList="getDataList"
            ></canceled>
            <!-- 待收货 子组件展示的数据取orderGoodsDtoList里的-->
            <wait-receive
                :thead="thead"
                v-show="item.appStatus == 20 || item.appStatus == 30"
                :data="item"
                @getDataList="getDataList"
            ></wait-receive>

            <!-- 已完成 子组件展示的数据取orderGoodsDtoList里的-->
            <completed
                :thead="thead"
                v-show="item.appStatus == 40"
                :data="item"
                @getDataList="getDataList"
            ></completed>
        </template>
    </Card>
</template>

<script>
    import waitPay from "./waitPay";
    import completed from "./completed";
    import canceled from "./canceled";
    import waitReceive from "./waitReceive";

    export default {
        name: "separate", // 订单中商品在不同的库房或商家，故拆单
        data() {
            return {};
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
            waitPay,
            completed,
            canceled,
            waitReceive,
        },
        created() {},
        computed: {},
        watch: {},
        mounted() {},
        methods: {
            getDataList() {
                this.$emit("getDataList");
            },
        },
    };
</script>

<style lang="scss" scoped>
    .separate {
        margin-top: 20px;
        margin-bottom: 10px;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
        &:hover {
            box-shadow: 0 1px 8px rgba(0, 0, 0, 0.2);
        }
        /deep/ .ivu-card-body {
            padding: 0;
        }
        /deep/ .order-info {
            margin-top: 0;
            box-shadow: none;
        }
    }

    .s-table-head {
        width: 100%;
        height: 80px;
        line-height: 24px;
        padding: 15px 30px;
        background: #ffffff;
        & > p {
            display: inline-block;
            &.s-time {
                color: #666666;
                margin-right: 20px;
            }
            &.s-num {
                color: #666666;
            }
            &.s-prompt {
                color: #999999;
            }
        }
    }
</style>
