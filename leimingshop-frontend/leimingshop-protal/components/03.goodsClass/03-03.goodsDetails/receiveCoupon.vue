<template>
    <div class="receiveCoupon">
        <Modal
            width="455px"
            v-model="visible"
            :loading="loading"
            ok-text="关闭"
        >
            <div slot="header">
                <span class="h-title" v-text="title"></span>
            </div>

            <vue-scroll :ops="ops">
                <div class="content coupon-list">
                    <div
                        class="coupon-item"
                        v-for="(item, index) in couponList"
                        :key="item.id + index"
                    >
                        <div
                            class="c-price"
                            v-lazy:background-image="priceFaceImg"
                        >
                            <p class="c-faceValue">
                                <span>￥</span>
                                <span v-text="item.faceValue"></span>
                            </p>

                            <p class="c-limitAmount">
                                满<span v-text="item.limitAmount"></span>元可用
                            </p>

                            <!--  <img
                                v-if="item.couponsType == 0"
                                class="c-fullReduction"
                                :src="fullReductionImg"
                            /> -->
                        </div>

                        <div class="c-content">
                            <p class="store-name" v-text="item.storeName"></p>
                            <p class="use-range">
                                适配范围：{{
                                    hanldeActivityGoodsScope(
                                        item.activityGoodsScope
                                    )
                                }}
                            </p>
                            <p
                                class="c-indate"
                                v-if="
                                    !item.validityType || item.validityType == 0
                                "
                            >
                                有效期至：<span
                                    v-text="item.useEndDate || item.endDate"
                                ></span>
                            </p>
                            <p class="c-indate" v-if="item.validityType == 1">
                                有效期至：领取后<span
                                    v-text="item.validityDays"
                                ></span
                                >天
                            </p>
                        </div>

                        <div class="receive-btn">
                            <Button
                                v-if="
                                    item.overdueFlag === false &&
                                    item.isReceived == 0
                                "
                                type="primary"
                                :loading="
                                    couponReceiveLoading && checkIndex == index
                                "
                                @click="handleReceive(item.id, index)"
                                >领取</Button
                            >
                            <div
                                v-else-if="item.overdueFlag === true"
                                class="yetReceive"
                            >
                                <img src="/img/01.index/grayGuoqi.png" />
                            </div>
                            <div v-else class="yetReceive">
                                <img :src="receiveCouponImg" />
                            </div>
                        </div>
                    </div>
                </div>
            </vue-scroll>
        </Modal>
    </div>
</template>

<script>
    import { mapState, mapActions } from "vuex";

    export default {
        name: "receiveCoupon",
        data() {
            return {
                title: "领取优惠券",
                loading: false,
                visible: false,
                priceFaceImg: require("~/static/img/01.index/bgImg.png"),
                receiveCouponImg: require("~/static/img/01.index/receive-coupon.png"),
                fullReductionImg: require("~/static/img/01.index/fullReduction.jpg"),
                ops: {
                    rail: {
                        size: "3px",
                    },
                    bar: {
                        hoverStyle: true,
                        onlyShowBarOnScroll: false, //是否只有滚动的时候才显示滚动条
                        background: "#F3F3F3", //颜色
                        keepShow: true,
                        size: "3px",
                    },

                    scrollPanel: {
                        scrollingX: false,
                    },
                },
                checkIndex: 0, // 领取优惠券时，优惠券所在列表的下标
            };
        },
        props: {
            couponList: {
                type: Array,
                default: () => [],
            },
        },
        components: {},
        created() {},
        computed: {
            ...mapState("goodsDetails", ["couponReceiveLoading"]),
        },
        watch: {},
        mounted() {},
        methods: {
            ...mapActions("goodsDetails", ["handleCouponReceive"]),

            hanldeActivityGoodsScope(val) {
                let tempStr;

                switch (val) {
                    case 0:
                        tempStr = "全场通用";
                        break;

                    case 1:
                        tempStr = "指定店铺分类";
                        break;

                    case 2:
                        tempStr = "指定商品";
                        break;

                    case 3:
                        tempStr = "指定品牌";
                        break;
                }

                return tempStr;
            },

            handleReceive(couponId, checkIndex) {
                this.checkIndex = checkIndex;
                this.handleCouponReceive({ couponId, checkIndex });
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/modal.scss";
    /deep/ .ivu-modal {
        border: 4px solid rgba(51, 51, 51, 0.08);
        box-sizing: content-box;
        border-radius: 12px;
    }

    /deep/ .ivu-modal-body {
        max-height: 430px;
        padding-bottom: 10px;
        .coupon-list {
            max-height: 430px;
            margin: 0;
        }
    }

    /deep/ .ivu-modal-content {
        padding: 27px 20px 30px 30px;
        border-radius: 12px;

        .ivu-modal-close {
            right: 27px;
            top: 14px;
            font-size: 40px !important;
            .ivu-icon-ios-close {
                width: 40px;
                height: 40px;
                font-size: 40px !important;
            }
        }
        .ivu-modal-header {
            padding: 0;
            margin-bottom: 20px;
            .h-title {
                height: auto;
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #222222;
                line-height: 16px;
            }
        }
    }

    /deep/ .ivu-modal-footer {
        height: 85px;
        display: flex;
        justify-content: center;
        align-items: center;
        display: none;
        .ivu-btn-text {
            display: none;
        }
        .ivu-btn-primary {
            width: 100px;
            height: 40px;
            border-radius: 3px;
        }
    }

    .coupon-item {
        width: 395px;
        height: 100px;
        border-left: 0;
        background: #ffffff;
        margin-bottom: 10px;
        display: flex;
        justify-content: left;
        align-items: center;
        .c-price {
            width: 140px;
            height: 100%;
            background-size: 100% auto;
            background-repeat: no-repeat;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            position: relative;
        }
        .c-faceValue {
            span:first-child {
                font-size: 20px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #ffffff;
                line-height: 20px;
            }
            span:last-child {
                font-size: 26px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #ffffff;
                line-height: 26px;
            }
        }
        .c-limitAmount {
            margin-top: 5px;
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 500;
            color: #ffffff;
            line-height: 16px;
        }

        .c-fullReduction {
            width: 34%;
            position: absolute;
            right: 0;
            top: 0;
        }

        .c-content {
            width: 170px;
            height: 100%;
            padding-left: 10px;
            border: 1px solid #eeeeee;
            border-right: 0;
            display: flex;
            flex-direction: column;
        }

        .store-name {
            margin-top: 19px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: #222222;
            line-height: 14px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .use-range {
            margin-top: 15px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 12px;
        }

        .c-indate {
            margin-top: 9px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 12px;
        }

        .receive-btn {
            width: 78px;
            height: 100%;
            border: 1px solid #eeeeee;
            border-left: 0;
            margin-left: -1px;
            display: flex;
            align-items: center;
            position: relative;

            button {
                width: 70px;
                height: 30px;
                background: linear-gradient(90deg, #df2b1c 0%, #ff4e03 100%);
                border-radius: 22px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #ffffff;
                line-height: 30px;
                position: absolute;
                top: 51px;
                /deep/ .ivu-icon-ios-loading {
                    vertical-align: top;
                }
            }

            .yetReceive {
                width: 50px;
                height: 45px;
                position: absolute;
                right: -1px;
                top: -1px;
                img {
                    width: 100%;
                    height: 100%;
                }
            }
        }
    }
    .coupon-item:last-child {
        margin-bottom: 0px;
    }
</style>
