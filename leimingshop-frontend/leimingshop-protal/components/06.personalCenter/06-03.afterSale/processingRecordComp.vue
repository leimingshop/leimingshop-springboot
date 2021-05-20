<template>
    <Card class="processing-record-comp">
        <div class="p-head">
            <p>申请时间：<span v-text="recordData.createDate"></span></p>
            <p class="orderSn" @click="handleOrderDetail">
                订单编号：<span v-text="recordData.orderSn"></span>
            </p>
            <p>
                <!-- <Icon
                    type="ios-checkmark-circle-outline"
                    size="16"
                    color="#666666"
                    style="font-weight: bold;"
                /> -->
                <span v-text="handleAuditStatus()"></span>
            </p>
        </div>

        <div class="p-content">
            <div class="p-goods-wrap">
                <div
                    class="p-goods-desc"
                    @click="handleGoodsDetail()"
                    v-lazy-container="{
                        selector: 'img',
                        error: '/img/common/loading/200_200.png',
                        loading: '/img/common/loading/200_200.png',
                    }"
                >
                    <img
                        class="p-image"
                        :data-src="handleImgPath(recordData.specMainPicture)"
                        alt=""
                    />

                    <p class="p-text-info">
                        <span
                            class="p-name"
                            v-text="recordData.goodsName"
                        ></span>
                        <span
                            class="p-attribute"
                            v-text="recordData.specAttrName"
                        ></span>
                    </p>
                </div>

                <div class="p-info">
                    <p class="p-num" style="height: 15px">
                        服务单号：<span v-text="recordData.aftersaleSn"></span>
                    </p>
                </div>

                <div class="p-operate">
                    <Button
                        v-for="item in operate"
                        :key="item.title"
                        :type="item.type"
                        v-text="item.title"
                        @click="item.handler()"
                    ></Button>
                </div>
            </div>
        </div>
    </Card>
</template>

<script>
    export default {
        name: "afterSaleComp", // 个人中心 - 售后申请 - 列表

        data() {
            return {
                operate: [
                    {
                        title: "查看详情",
                        handler: this.handleDetail,
                        type: "text",
                    },
                    {
                        title: "进度跟踪",
                        handler: this.handleProgress,
                        type: "text",
                    },
                ],
            };
        },

        props: {
            recordData: {
                type: Object,
                default: () => {},
            },
        },

        components: {},
        created() {},
        computed: {},
        watch: {},
        mounted() {},
        methods: {
            // 处理图片路径
            handleImgPath(path) {
                return `${this.$imgURL}${path}`;
            },

            // 订单审核状态
            handleAuditStatus() {
                let text;

                switch (this.recordData.auditStatus) {
                    case 1:
                        text = "商家审核中";
                        return text;
                    case 2:
                        text = "平台审核中";
                        return text;
                    case 3:
                        text = "已完成";
                        break;
                    case 4:
                        text = "已取消";
                        return text;
                }

                switch (this.recordData.auditResult) {
                    case 1:
                        text = "审核通过";
                        break;
                    case 2:
                        text = "审核拒绝";
                        return text;
                }

                switch (this.recordData.finalStatus) {
                    case 1:
                        text = "用户取消";
                        break;
                    case 2:
                        text = "退款失败";
                        break;
                    case 3:
                        text = "待退货入库";
                        break;
                    case 4:
                        text = "确认收货";
                        break;
                    case 5:
                        text = "退款中";
                        break;
                    case 6:
                        text = "退款成功";
                        break;
                    case 7:
                        text = "换货失败";
                        break;
                    case 8:
                        text = "待换货入库";
                        break;
                    case 9:
                        text = "换货出库中";
                        break;
                    case 10:
                        text = "换货成功";
                        break;
                    case null:
                        text = "审核拒绝";
                        break;
                }
                return text;
            },

            handleDetail() {
                this.$router.push({
                    path: "/personalCenterBase/afterSalesExchange",
                    query: {
                        aftersaleId: this.recordData.aftersaleSn,
                        aftersaleType: this.recordData.aftersaleType,
                    },
                });
            },

            handleProgress() {
                this.$emit("handleModalVisible", this.recordData.aftersaleSn);
            },

            // 跳转至订单详情页
            handleOrderDetail() {
                this.$router.push({
                    path: "/personalCenterBase/myOrdersDetail",
                    query: {
                        id: this.recordData.orderId,
                        appStatus: 40, // 0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成; ,
                    },
                });
            },

            // 商品详情页
            handleGoodsDetail() {
                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId: this.recordData.goodsId,
                        specId: this.recordData.specId,
                    },
                });

                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .processing-record-comp {
        margin-bottom: 20px;
    }

    /deep/ .ivu-card-body {
        padding: 0;
    }

    .p-head {
        width: 100%;
        height: 44px;
        line-height: 44px;
        padding: 0 30px;
        background: #f6f6f6;
        border: 1px solid #e8e8e8;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 14px;
        color: #222222;
        & > p {
            font-size: 14px;
            display: inline-block;
            color: #222222;
            margin-right: 20px;
            &:last-of-type {
                float: right;
                margin: 0 0 0 auto;
            }
        }

        .orderSn {
            &:hover {
                cursor: pointer;
                span {
                    color: $primary-color;
                }
            }
        }
    }

    .p-content {
        margin-top: -1px;
        padding: 10px 30px;

        .p-goods-wrap {
            width: 100%;
            min-height: 120px;
            margin-right: -1px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            text-align: center;
            &:last-of-type {
                border-bottom: 0;
            }

            .p-goods-desc {
                width: 306px;
                height: 68px;
                display: flex;
                justify-content: left;
                align-items: center;
                text-align: left;
                cursor: pointer;
                .p-image {
                    width: 68px;
                    height: 68px;
                    object-fit: scale-down;
                    border: 1px solid #f5f5f5;
                    transition: border 0.5s;
                    &:hover {
                        border: 1px solid $primary-color;
                    }
                }
                .p-text-info {
                    max-width: 240px;
                    height: 68px;
                    padding: 5px 0;
                    box-sizing: content-box;
                    display: flex;
                    flex-direction: column;
                    margin-left: 15px;
                }
                .p-name {
                    color: #333333;
                    font-size: 16px;
                    line-height: 20px;
                    letter-spacing: 1px;
                    margin-bottom: auto;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    /*在任何地方换行*/
                    -webkit-box-orient: vertical;
                    overflow: hidden;
                    word-break: break-all;
                    /*在任何地方换行*/
                    transition: color 0.3s;
                    &:hover {
                        color: $primary-color;
                    }
                }
                .p-attribute {
                    color: #999999;
                    font-size: 13px;
                    overflow: hidden;
                    white-space: nowrap;
                    word-break: break-all;
                    text-overflow: ellipsis;
                }
            }

            .p-info {
                color: #717171;
                font-size: 13px;
                flex-direction: column;
                text-align: left;
            }

            .p-operate {
                display: flex;
                flex-direction: column;
                .ivu-btn {
                    font-size: 13px;
                }
            }
        }
    }
</style>
