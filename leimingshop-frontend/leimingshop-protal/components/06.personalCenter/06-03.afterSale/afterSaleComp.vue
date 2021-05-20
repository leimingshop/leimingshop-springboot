<template>
    <Card class="after-sale-comp">
        <div class="a-head">
            <p>下单时间：<span v-text="applyData.createDate"></span></p>
            <p class="orderSn" @click="handleOrderDetail">
                订单编号：<span v-text="applyData.orderSn"></span>
            </p>
            <p>店铺名称：<span v-text="applyData.storeName"></span></p>
            <p v-if="applyData.isOutTime == 1">
                <Icon size="16" type="md-alert" />该商品已超过售后期
            </p>
        </div>

        <div class="a-content">
            <div
                class="a-goods-wrap"
                v-for="item in applyData.orderGoodsList"
                :key="item.id"
            >
                <div
                    class="a-goods-desc"
                    @click="handleGoodsDetail(item)"
                    v-lazy-container="{
                        selector: 'img',
                        error: '/img/common/loading/200_200.png',
                        loading: '/img/common/loading/200_200.png',
                    }"
                >
                    <img
                        class="g-image"
                        :data-src="handleImgPath(item.goodsImage)"
                        alt=""
                    />
                    <p class="a-text-info">
                        <span class="g-name" v-text="item.goodsName"></span>
                        <span
                            class="g-attribute"
                            v-text="item.specAttrValueName"
                        ></span>
                    </p>
                </div>

                <div
                    class="a-price"
                    v-html="$options.filters.filterPrice(item.specPayPrice)"
                ></div>

                <div class="a-operate">
                    <Button
                        :class="handleOperateClass(item.aftersaleQuantity)"
                        :type="operate.type"
                        v-text="handleOperateTitle(item.aftersaleQuantity)"
                        @click="
                            operate.handler(item.aftersaleQuantity, item.id)
                        "
                    ></Button>
                </div>
            </div>
        </div>
    </Card>
</template>

<script>
    // 测试账号 18401960032 123456
    export default {
        name: "afterSaleComp", // 个人中心 - 售后申请 - 列表

        data() {
            return {
                operate: {
                    title: "申请售后",
                    handler: this.handleAfterSale,
                    type: "primary",
                    disabled: false,
                },

                specSellPrice: "37599.0",
            };
        },

        props: {
            applyData: {
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

            // 操作按钮 - 标题
            handleOperateTitle(num) {
                let title = num > 0 ? "申请售后" : "已申请";
                return title;
            },

            // 操作按钮 - 类名
            handleOperateClass(num) {
                var dis = num == 0 ? "dis" : "";
                if (dis != "") return dis;

                var dis = this.applyData.isOutTime == 1 ? "dis" : "";
                return dis;
            },

            // 申请售后
            handleAfterSale(num, orderGoodsId) {
                // 是否超出售后申请期
                if (this.applyData.isOutTime == 1) return;

                // 可申请商品数量
                if (num == 0) return;

                this.$router.push({
                    path: "/personalCenterBase/applyAfterSales",
                    query: {
                        orderGoodsId,
                        goodsName: this.applyData.goodsName,
                        goodsId: this.applyData.goodsId,
                    },
                });
            },

            // 跳转至订单详情页
            handleOrderDetail() {
                this.$router.push({
                    path: "/personalCenterBase/myOrdersDetail",
                    query: {
                        id: this.applyData.id,
                        appStatus: 40, // 0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成; ,
                    },
                });
            },

            // 商品详情页
            handleGoodsDetail(goods) {
                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: { goodsId: goods.goodsId, specId: goods.specId },
                });

                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .after-sale-comp {
        margin-bottom: 20px;
    }

    /deep/ .ivu-card-body {
        padding: 0;
    }

    .a-head {
        width: 100%;
        height: 44px;
        line-height: 44px;
        padding: 0 30px;
        background: #f6f6f6;
        display: flex;

        & > p {
            // display: inline-block;
            color: #666666;
            margin-right: 20px;

            &:last-of-type {
                max-width: 300px;
                float: right;
                // margin: 0 0 0 auto;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
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

    .a-content {
        margin-top: -1px;
        padding: 10px 30px;

        .a-goods-wrap {
            width: 100%;
            min-height: 120px;
            border-bottom: 1px solid #ebebeb;
            margin-right: -1px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            text-align: center;

            &:last-of-type {
                border-bottom: 0;
            }

            .a-goods-desc {
                width: 385px;
                height: 80px;
                display: flex;
                justify-content: left;
                align-items: center;
                text-align: left;
                cursor: pointer;
                .g-image {
                    width: 68px;
                    height: 68px;
                    object-fit: scale-down;
                    border: 1px solid #f5f5f5;
                    transition: border 0.5s;
                    &:hover {
                        border: 1px solid $primary-color;
                    }
                }

                .a-text-info {
                    max-width: 300px;
                    height: 80px;
                    padding: 5px 0;
                    box-sizing: content-box;
                    display: flex;
                    flex-direction: column;
                    margin-left: 15px;
                }

                .g-name {
                    color: #333333;
                    font-size: 16px;
                    line-height: 20px;
                    letter-spacing: 1px;
                    margin-bottom: auto;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    /*规定超过两行的部分截断*/
                    -webkit-box-orient: vertical;
                    overflow: hidden;
                    word-break: break-all;
                    /*在任何地方换行*/
                    transition: color 0.3s;
                    &:hover {
                        color: $primary-color;
                    }
                }

                .g-attribute {
                    color: #999999;
                    font-size: 13px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }
            }

            .a-price {
                color: #333333;
            }

            .a-operate {
                width: 200px;

                .ivu-btn {
                    width: 90px;
                    height: 34px;
                    background: linear-gradient(
                        270deg,
                        rgba(253, 76, 5, 1) 0%,
                        rgba(222, 42, 27, 1) 100%
                    );
                    border-radius: 22px;
                    transition: all 0.3s;

                    &:hover {
                        opacity: 0.8;
                    }

                    &:active {
                        opacity: 1;
                    }

                    &.dis {
                        color: #999999;
                        border: 1px solid #999999;
                        background: #ffffff;
                        cursor: not-allowed;
                    }
                }
            }
        }
    }
</style>
