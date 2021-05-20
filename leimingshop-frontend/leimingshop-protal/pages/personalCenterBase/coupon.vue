<template>
    <div class="integration-center">
        <div class="integra-center-top">
            <ul class="intecenter-top-left" id="cut">
                <li
                    class="base"
                    @click="coupon(0)"
                    :class="{ current: iscurrent == 0 }"
                >
                    未使用
                </li>
                <li
                    class="base"
                    @click="coupon(1)"
                    :class="{ current: iscurrent == 1 }"
                >
                    已使用
                </li>
                <li
                    class="base"
                    @click="coupon(2)"
                    :class="{ current: iscurrent == 2 }"
                >
                    已过期
                </li>
            </ul>
            <ul class="intecenter-top-right" id="cut">
                <!-- 右侧按钮 -->
                <div class="receive">
                    <span class="receive-button" @click="collectCoupons"
                        >领券中心</span
                    >
                </div>
            </ul>
        </div>

        <div class="integra-center-bottom" id="cutContent">
            <!-- 未使用 -->
            <div class="no-used">
                <div class="no-used-top">
                    <span
                        @click="
                            $router.push('/personalCenterBase/couponExplain')
                        "
                        style="cursor: pointer"
                        @mouseover="mouseover"
                        @mouseout="mouseout"
                    >
                        <img :src="wenhao" alt />
                        <span>优惠券使用说明</span>
                    </span>
                </div>
                <!-- 下方图片 -->
                <div class="no-used-bottom">
                    <!-- 优惠券左侧部分 -->
                    <div
                        class="no-used-left"
                        v-for="(item, index) in couponList"
                        :key="index"
                    >
                        <div
                            class="left-img"
                            v-if="item.couponsState == 1"
                            v-lazy:background-image="
                                '/img/04.shoppingCar/coupon1.png'
                            "
                        >
                            <p class="artMoney1">
                                <span :style="{ fontSize: '16px' }">￥</span>
                                <span
                                    :style="{
                                        fontSize: '36px',
                                        marginLeft: '-5px',
                                    }"
                                    >{{ item.faceValue }}</span
                                >
                            </p>
                            <p class="artMoney2">
                                满{{ item.limitAmount }}元可用
                            </p>
                        </div>
                        <div
                            class="left-img"
                            v-else-if="item.couponsState == 2"
                            v-lazy:background-image="
                                '/img/04.shoppingCar/coupon2.png'
                            "
                        >
                            <p class="artMoney1">
                                <span :style="{ fontSize: '16px' }">￥</span>
                                <span
                                    :style="{
                                        fontSize: '34px',
                                        marginLeft: '-5px',
                                    }"
                                    >{{ item.faceValue }}</span
                                >
                            </p>
                            <p class="artMoney2">
                                满{{ item.limitAmount }}元可用
                            </p>
                        </div>
                        <div
                            class="left-img"
                            v-else-if="item.couponsState == 3"
                            v-lazy:background-image="
                                '/img/04.shoppingCar/coupon2.png'
                            "
                        >
                            <p class="artMoney1">
                                <span :style="{ fontSize: '16px' }">￥</span>
                                <span
                                    :style="{
                                        fontSize: '34px',
                                        marginLeft: '-5px',
                                    }"
                                    >{{ item.faceValue }}</span
                                >
                            </p>
                            <p class="artMoney2">
                                满{{ item.limitAmount }}元可用
                            </p>
                        </div>
                        <!-- 右侧文字 -->
                        <div class="right-text">
                            <div
                                class="right-text-top"
                                style="display: inline-block; margin-left: 13px"
                            >
                                <div class="storeName" :title="item.storeName">
                                    <p class="official">{{ item.storeName }}</p>
                                </div>
                                <div class="range">
                                    <div class="shop-top">
                                        <p style="margin-bottom: 5px">
                                            适用范围：{{
                                                item.activityGoodsScope == 0
                                                    ? "全场通用"
                                                    : item.activityGoodsScope ==
                                                      1
                                                    ? "指定店铺分类"
                                                    : item.activityGoodsScope ==
                                                      2
                                                    ? "指定商品"
                                                    : "指定品牌"
                                            }}
                                        </p>
                                        <p>有效期至：{{ item.endDate }}</p>
                                        <!-- 后期优化时时用 -->
                                        <!-- <p class="artdate"><span>有效期至：{{data.validityType ==0 ?data.useEndDate : '领取后'+data.validityDays+'天' }}</span></p>  -->
                                    </div>

                                    <div
                                        class="use"
                                        @click="openDia(index)"
                                        v-if="item.couponsState == 1"
                                    >
                                        去使用
                                    </div>
                                    <div
                                        class="use"
                                        v-if="item.couponsState == 2"
                                        @click="goOrderDetails(item.orderId)"
                                    >
                                        查看
                                    </div>
                                </div>
                            </div>
                            <!-- 已使用 -->
                            <span class="toUsed" v-if="item.couponsState == 2">
                                <img src="/img/01.index/grayUsed.png" alt />
                            </span>
                            <!-- 已过期 -->
                            <span
                                class="toUsed"
                                v-else-if="item.couponsState == 3"
                            >
                                <img src="/img/01.index/grayGuoqi.png" alt="" />
                            </span>
                            <!-- 正常的 -->
                            <span class="full" v-if="item.couponsState == 1">
                                <!-- 优惠券类型标识 -->
                                <img
                                    v-if="
                                        item.couponsType == 0 &&
                                        item.couponsFlag === null
                                    "
                                    :src="'/img/04.shoppingCar/full.png'"
                                    alt
                                />
                                <img
                                    v-else-if="
                                        item.couponsType == 1 &&
                                        item.couponsFlag === null
                                    "
                                    :src="'/img/01.index/grayfullSubtraction.png'"
                                    alt
                                />

                                <!-- 优惠券自领取6小时内显示标识 -->
                                <img
                                    v-else-if="item.couponsFlag == 0"
                                    :src="'/img/04.shoppingCar/new.png'"
                                    alt=""
                                />
                                <img
                                    v-else-if="item.couponsFlag == 1"
                                    :src="'/img/04.shoppingCar/willExpire.png'"
                                    alt
                                />
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 模态框优化 -->
        <div class="reason-wrapper" v-show="modal1">
            <div class="shadow">
                <div class="reason-popup">
                    <div class="inner">
                        <div class="inner-top">
                            <span class="sign">
                                <!-- 优惠券类型标识 -->
                                <img
                                    v-if="
                                        couponInfo.couponsType == 0 &&
                                        couponInfo.couponsFlag === null
                                    "
                                    :src="'/img/04.shoppingCar/full.png'"
                                    alt
                                />
                                <img
                                    v-else-if="
                                        couponInfo.couponsType == 1 &&
                                        couponInfo.couponsFlag === null
                                    "
                                    :src="'/img/01.index/fullReduction.jpg'"
                                    alt
                                />

                                <!-- 优惠券自领取6小时内显示标识 -->
                                <img
                                    v-else-if="couponInfo.couponsFlag == 0"
                                    :src="'/img/04.shoppingCar/bigNew.png'"
                                    alt=""
                                />
                                <img
                                    v-else-if="couponInfo.couponsFlag == 1"
                                    :src="'/img/04.shoppingCar/willExpire.png'"
                                    alt
                                />
                            </span>
                            <div class="shopName">
                                {{
                                    couponInfo.activityGoodsScope == 0
                                        ? "全场通用"
                                        : couponInfo.activityGoodsScope == 1
                                        ? "指定店铺分类"
                                        : couponInfo.activityGoodsScope == 2
                                        ? "指定商品"
                                        : "指定品牌"
                                }}
                            </div>
                            <div class="money-center">
                                <span>￥</span>
                                <span>{{ couponInfo.faceValue }}</span>
                            </div>
                            <p>满{{ couponInfo.limitAmount }}元可用</p>
                        </div>
                        <div class="inner-bottom">
                            <ul>
                                <li>
                                    适用平台：
                                    <span
                                        class="inner-content"
                                        :title="couponInfo.storeName"
                                        >{{ couponInfo.storeName }}</span
                                    >
                                </li>
                                <li>
                                    有效期至：
                                    <span class="inner-content">{{
                                        couponInfo.endDate
                                    }}</span>
                                </li>
                                <li>
                                    详细说明：
                                    <span class="inner-content">{{
                                        couponInfo.activityGoodsScope == 0
                                            ? "全场通用"
                                            : couponInfo.activityGoodsScope == 1
                                            ? "指定店铺分类"
                                            : couponInfo.activityGoodsScope == 2
                                            ? "指定商品"
                                            : "指定品牌"
                                    }}</span>
                                </li>
                            </ul>
                        </div>
                        <div class="button-group">
                            <div
                                class="btn1"
                                @click="goCouponsGoods(couponInfo.activityId)"
                            >
                                立即使用
                            </div>
                        </div>
                    </div>
                </div>

                <div class="error" @click="delateShadow">
                    <img src="/img/04.shoppingCar/modal-delete.png" alt />
                </div>
            </div>
        </div>

        <!-- 无数据的情况 -->
        <div class="no-data" v-if="this.couponList.length == 0">
            <div class="no-data-main">
                <div class="no-data-main-content">
                    <img src="/img/04.shoppingCar/coupon.png" alt />
                    <div
                        style="
                            font-size: 14px;
                            color: #222222;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            margin-top: -25px;
                        "
                    >
                        无相关优惠券
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { getCouponList, getshopCoupons } from "@/api/api_06-13.coupon.js";
    export default {
        name: "coupon",
        head() {
            return {
                title: "优惠券",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        props: {
            topHeight: {
                type: Number,
                default: 200,
                required: false,
            },
        },
        data() {
            return {
                wenhao: "/img/06.personalCenter/wenhao.png",
                modal1: false, //默认弹出框
                // 优惠券列表数据
                couponList: [
                    {
                        activityGoodsScope: 0,
                        activityId: "",
                        couponsState: 1,
                        couponsType: 0,
                        createDate: "",
                        endDate: "",
                        faceValue: "",
                        id: "",
                        limitAmount: "",
                        orderId: null,
                        orderSn: null,
                        startDate: "",
                        storeId: "",
                        storeName: "",
                        useDate: null,
                    },
                ],
                // 模态框列表数据
                couponInfo: {
                    activityGoodsScope: 0,
                    activityId: "",
                    couponsState: 1,
                    couponsFlag: "",
                    couponsType: 0,
                    createDate: "",
                    endDate: "",
                    faceValue: "",
                    id: "",
                    limitAmount: "",
                    orderId: null,
                    orderSn: null,
                    startDate: "",
                    storeId: "",
                    storeName: "",
                    useDate: null,
                },
                iscurrent: 0,
            };
        },

        created() {},
        mounted() {
            this.coupon(0);
        },

        methods: {
            // 向后台传递data参数，用来接好收当前优惠券状态
            coupon(data) {
                this.iscurrent = data;
                getCouponList({
                    couponsState: data,
                })
                    .then((res) => {
                        console.log(res);
                        if (res.code !== 200) {
                            this.$Message.info(res.mag);
                        } else {
                            this.couponList = res.data;
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            mouseover() {
                this.wenhao = "/img/06.personalCenter/wenhaored.png";
            },
            mouseout() {
                this.wenhao = "/img/06.personalCenter/wenhao.png";
            },
            openDia(data) {
                this.modal1 = true;
                this.couponInfo = this.couponList[data];
            },
            //立即使用去商品搜索
            goCouponsGoods(data) {
                this.$router.push({
                    path: "/searchCouponsGoods",
                    query: {
                        activityId: data,
                    },
                });
            },

            // 删除弹窗
            delateShadow(data) {
                this.modal1 = false;
            },

            // 查看跳转订单详情页
            goOrderDetails(orderId) {
                this.$router.push({
                    path: "/personalCenterBase/myOrdersDetail",
                    query: {
                        id: orderId,
                    },
                });
            },

            collectCoupons() {
                this.$router.push({
                    path: "/couponCenter",
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .integration-center {
        width: 1000px;
        background: #fff;
        height: 100%;
    }

    .intecenter-top-left {
        display: flex;
        justify-content: flex-start;
    }

    .integra-center-top {
        background: #eee;
        border-bottom: 0.9px solid rgba(221, 38, 25, 1);
        display: flex;
        justify-content: space-between;
        background: rgba(255, 255, 255, 1);
    }

    .intecenter-top-right {
        display: flex;
        align-items: center;
        margin-right: 40px;
    }

    .base {
        width: 120px;
        text-align: center;
        height: 44px;
        line-height: 44px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 500;
        color: rgba(113, 113, 113, 1);
        border-bottom: 0;
        cursor: pointer;
    }

    .current {
        background: rgba(221, 38, 25, 1);
        color: rgba(255, 255, 255, 1);
    }

    .delateBox {
        width: 20px;
        height: 23px;
        line-height: 23px;
        border: 1px solid #666;
        text-align: center;
        margin: 30px auto;
        cursor: pointer;
    }

    .no-used,
    .used,
    .expired {
        margin-right: 40px;
        text-align: right;
    }

    .no-used-top {
        height: 53px;
        line-height: 53px;
        font-size: 12px;

        img {
            vertical-align: middle;
            width: 14px;
            height: 14px;
            margin-top: -2px;
        }

        span {
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
        }

        span:hover {
            color: #e2270b;
        }
    }

    .no-used-bottom {
        display: flex;
        justify-content: space-between;
        align-content: space-between;
        flex-wrap: wrap;
        padding-left: 40px;

        .no-used-left,
        .no-used-right {
            position: relative;
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;

            .left-img {
                display: inline-block;
                width: 126px;
                height: 100px;
                color: rgba(255, 255, 255, 1);
                background-size: 126px 114px;
                position: relative;

                .artMoney1 {
                    width: 126px;
                    font-weight: 500;
                    text-align: center;
                    margin-top: 8px;
                }

                .artMoney2 {
                    text-align: center;
                    font-size: 14px;
                }

                .overdue {
                    display: inline-block;
                    width: 50px;
                    height: 50px;
                    position: absolute;
                    top: 0;
                    right: 0;

                    img {
                        width: 100%;
                        height: 100%;
                    }
                }
            }

            .right-text {
                box-sizing: border-box;
                // padding: 10px 20px 10px 10px;
                text-align: left;
                border: 1px solid rgba(235, 235, 235, 1);
                position: relative;
                width: 282px;

                .official {
                    // margin-top: 25px;
                    width: 180px;
                    text-overflow: ellipsis;
                    overflow: hidden;
                    white-space: ellipsis;

                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: rgba(51, 51, 51, 1);
                    line-height: 19px;
                }

                .storeName {
                    width: 180px;
                    margin-top: 19px;
                    white-space: nowrap;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }

                .toUsed {
                    position: absolute;
                    display: inline-block;
                    width: 59px;
                    height: 55px;
                    right: -1px;
                    top: -1px;

                    // border: 1px solid #666;
                    img {
                        width: 100%;
                        height: 100%;
                    }
                }

                // img {
                //     width: 50px;
                //     height: 50px;
                //     position: absolute;
                //     top: 0;
                //     right: 0;
                // }
                .full {
                    display: inline-block;
                    width: 50px;
                    height: 50px;
                    position: absolute;
                    top: 0;
                    right: -1px;
                    background: url("/img/04.shoppingCar/full.png");
                }

                .willExpire {
                    display: inline-block;
                    width: 50px;
                    height: 50px;
                    position: absolute;
                    top: 0;
                    right: 0;
                    background: url("/img/04.shoppingCar/willExpire.png");
                }

                .range {
                    margin-top: 11px;
                    display: flex;
                    justify-content: space-between;

                    .shop-top {
                        p {
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #666666;
                        }
                    }

                    .use {
                        width: 70px;
                        height: 30px;
                        line-height: 30px;
                        border-radius: 22px;
                        border: 1px solid #dd2619;
                        margin-left: 48px;
                        margin-top: 5px;
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(221, 38, 25, 1);
                        // -webkit-background-clip:text;
                        // -webkit-text-fill-color:transparent;
                        text-align: center;
                        cursor: pointer;
                        &:hover {
                            background: #dd2619;
                            color: #ffffff;
                        }
                    }
                }
            }
        }
    }

    .receive {
        .receive-button {
            display: inline-block;
            width: 80px;
            height: 30px;
            line-height: 30px;
            text-align: center;
            background: linear-gradient(
                270deg,
                rgba(247, 69, 10, 1) 0%,
                rgba(224, 44, 25, 1) 100%
            );
            box-shadow: 0px 2px 4px 0px rgba(247, 69, 10, 0.22);
            border-radius: 15px;
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(255, 255, 255, 1);
            cursor: pointer;
        }
    }

    .no-data {
        margin-top: 130px;
        .no-data-main {
            display: flex;
            justify-content: center;
            .no-data-main-content {
                display: felx;
                justify-content: center;
                width: 161px;
                height: 161px;
                img {
                    width: 100%;
                    height: 100%;
                }
                div {
                    text-align: center;
                    margin-top: -15px;
                    margin-left: 10px;
                }
            }
        }
    }

    // 优化模态框
    .reason-wrapper /deep/ {
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.1);
        position: fixed;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        z-index: 1000;
        .shadow {
            position: relative;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 330px;
            height: 322px;
            background: rgba(0, 0, 0, 0.1401);
            border-radius: 12px;
            display: flex;
            justify-content: center;
            align-items: center;
            .reason-popup {
                width: 322px;
                height: 310px;
                background: rgba(255, 255, 255, 1);
                border-radius: 12px;
                .inner {
                    width: 322px;
                    height: 310px;
                    background: rgba(255, 255, 255, 1);
                    border-radius: 12px;
                    box-sizing: border-box;
                    .inner-top {
                        position: relative;
                        box-sizing: border-box;
                        width: 322px;
                        height: 122px;
                        text-align: center;
                        background: url("/img/04.shoppingCar/BJ.png") no-repeat;
                        border-radius: 12px 12px 0px 0px;
                        .sign {
                            position: absolute;
                            top: -2px;
                            right: -2px;
                            width: 50px;
                            height: 50px;
                        }
                        .shopName {
                            padding-top: 16px;
                            font-size: 16px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 600;
                            color: rgba(51, 51, 51, 1);
                        }
                        .money-center {
                            // padding-top: 15px;
                            span:first-child {
                                font-size: 26px;
                                font-family: PingFangSC-Semibold, PingFang SC;
                                font-weight: 600;
                                color: rgba(238, 59, 16, 1);
                            }
                            span:last-child {
                                font-size: 30px;
                                font-family: PingFangSC-Semibold, PingFang SC;
                                font-weight: 600;
                                color: rgba(238, 59, 16, 1);
                                margin-left: -5px;
                            }
                        }
                        p {
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: rgba(253, 76, 4, 1);
                        }
                    }
                    .inner-bottom {
                        width: 100%;
                        ul {
                            padding: 21px 19px 0 30px;
                            list-style-position: inside;
                            list-style-type: disc;
                            li {
                                width: 384px;
                                font-size: 12px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 400;
                                color: rgba(51, 51, 51, 1);
                                white-space: nowrap;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                margin-bottom: 10px;
                                .inner-content {
                                    text-align: left;
                                }
                            }
                        }
                    }
                    .button-group {
                        width: 100%;
                        display: flex;
                        justify-content: center;
                        .btn1 {
                            width: 80px;
                            height: 30px;
                            background: linear-gradient(
                                90deg,
                                #dd291c 0%,
                                #ff4e02 100%
                            );
                            border-radius: 2px;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: rgba(255, 255, 255, 1);
                            letter-spacing: 1px;
                            line-height: 30px;
                            text-align: center;
                            margin-top: 20px;
                            cursor: pointer;
                        }
                    }
                }
            }
            .error {
                position: absolute;
                bottom: 300px;
                cursor: pointer;
                right: -20px;
            }
        }
    }
</style>
