<template>
    <div style="width: 100%; background: #eee">
        <div class="personl-top" style="background: #ffffff">
            <personal-top></personal-top>
        </div>
        <div class="personal-center-base">
            <p class="personal-center-nav">
                <router-link
                    to="/"
                    tag="span"
                    class="evaluationSheet"
                    style="cursor: pointer"
                    >首页</router-link
                >
                <span style="font-size: 13px; color: #3a3a3a">></span>
                <router-link
                    to="/personalCenterBase/evaluationSheet"
                    tag="span"
                    class="evaluationSheet"
                    style="cursor: pointer"
                    >评价晒单</router-link
                >
            </p>

            <!-- 多评价商品时间 -->
            <div
                class="person-center-header"
                v-show="orderlineGoodsList.length > 1"
            >
                <span
                    >下单时间：{{ orderlineGoodsList[0].createOrderDate }}</span
                >
                <span @click="gohandleDetail(orderlineGoodsList[0].orderId)">
                    订单编号：
                    <span class="orderno">{{
                        orderlineGoodsList[0].orderSn
                    }}</span>
                </span>
            </div>

            <!-- 查看单商品评价 -->
            <div
                class="person-center-content-once"
                v-for="(item, index) in orderlineGoodsList"
                :key="index"
                v-show="orderlineGoodsList.length < 2"
            >
                <!-- 左侧 -->
                <div class="person-center-left-once">
                    <div class="person-center-top">
                        <div
                            class="click"
                            @click="
                                goshopsDetailPage(
                                    orderlineGoodsList[0].goodsId,
                                    orderlineGoodsList[0].goodsSpecId
                                )
                            "
                        >
                            <div
                                class="person-img"
                                v-lazy-container="{
                                    selector: 'img',
                                    error: '/img/common/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <!-- <span></span> -->
                                <img
                                    :data-src="$imgURL + item.specMainPicture"
                                    alt
                                />
                            </div>
                            <div class="text-explain">{{ item.goodsName }}</div>
                            <div class="explain">{{ item.goodsSubTitle }}</div>
                            <div class="price">
                                <span>￥</span>
                                <span>{{
                                    item.goodsPrice.toString().split(".")[0]
                                }}</span>
                                <span
                                    >.{{
                                        item.goodsPrice.toString().split(".")
                                            .length > 1
                                            ? item.goodsPrice
                                                  .toString()
                                                  .split(".")[1]
                                            : "00"
                                    }}</span
                                >
                            </div>
                        </div>
                        <div
                            class="jump"
                            @click="
                                gohandleDetail(orderlineGoodsList[0].orderId)
                            "
                        >
                            <div class="price-line"></div>
                            <div class="person-center-bottom">
                                <p>评价人数：{{ item.num }}条</p>
                                <p v-show="orderlineGoodsList.length < 2">
                                    订单编号：{{
                                        orderlineGoodsList[0].orderSn
                                    }}
                                </p>
                                <p v-show="orderlineGoodsList.length < 2">
                                    下单时间：{{
                                        orderlineGoodsList[0].createOrderDate
                                    }}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 右侧 -->
                <div class="person-center-right">
                    <div class="sheet-right">
                        <div class="shop-level">
                            <Rate
                                v-model="item.evaluateScores"
                                disabled
                                custom-icon="rate-icon"
                            />
                            <span style="font-size: 14px"
                                >{{ item.evaluateScores }}分</span
                            >
                        </div>
                        <span class="time">{{ item.createDate }}</span>
                    </div>
                    <div class="textcontent">{{ item.evaluateContent }}</div>
                    <div class="pictureShow" style="display: flex">
                        <!-- 查看评价遍历图片预览功能 -->
                        <div
                            :images="item.evaluateImg"
                            v-lazy-container="{
                                selector: 'img',
                                error: '/img/common/loading/200_200.png',
                                loading: '/img/common/loading/200_200.png',
                            }"
                        >
                            <img
                                v-for="(item1, index1) in item.evaluateImg"
                                :data-src="$imgURL + item1"
                                :key="index1"
                                class="showOne"
                                @click="active(item1, index1)"
                            />
                            <img
                                :data-src="$imgURL + item.evaluateImg[picture]"
                                style="
                                    width: 300px;
                                    height: 300px;
                                    margin-top: 10px;
                                "
                                v-if="
                                    item.evaluateImg !== '' &&
                                    item.evaluateImg !== null
                                "
                                alt
                            />
                            <!-- <img :data-src="$imgURL + castemo" style="width:300px;height:300px" alt /> -->
                        </div>
                    </div>

                    <div class="line" v-if="item.replyContent !== null"></div>

                    <div class="answer">
                        <span
                            class="answer-logo"
                            v-if="item.replyContent != null"
                            v-lazy-container="{
                                selector: 'img',
                                error: '/img/common/loading/200_200.png',
                                loading: '/img/common/loading/200_200.png',
                            }"
                        >
                            <img :data-src="$imgURL + item.storeLogo" alt />
                        </span>
                        <div class="answer-right">
                            <div
                                class="answer-top"
                                v-if="item.replyContent != null"
                            >
                                <span class="store"
                                    >{{ item.storeName }}回复：</span
                                >
                                <span class="time">{{ item.replyDate }}</span>
                            </div>
                            <div class="answer-bottom">
                                {{ item.replyContent }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 查看多商品评价 -->
            <div
                class="person-center-content"
                v-for="(item, index) in orderlineGoodsList"
                :key="index + '-value'"
                v-show="orderlineGoodsList.length > 1"
            >
                <!-- 左侧 -->
                <div class="person-center-left">
                    <div class="person-center-top">
                        <div
                            class="click"
                            @click="
                                goshopsDetailPage(
                                    item.goodsId,
                                    item.goodsSpecId
                                )
                            "
                        >
                            <div
                                class="person-img"
                                v-lazy-container="{
                                    selector: 'img',
                                    error: '/img/common/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <img
                                    :data-src="$imgURL + item.specMainPicture"
                                    alt
                                />
                            </div>

                            <div class="text-explain">{{ item.goodsName }}</div>
                            <div class="explain">{{ item.goodsSubTitle }}</div>
                            <div class="price">
                                <span>￥</span>
                                <span>{{
                                    item.goodsPrice.toString().split(".")[0]
                                }}</span>
                                <span
                                    >.{{
                                        item.goodsPrice.toString().split(".")
                                            .length > 1
                                            ? item.goodsPrice
                                                  .toString()
                                                  .split(".")[1]
                                            : "00"
                                    }}</span
                                >
                            </div>
                            <div
                                class="inline"
                                v-show="orderlineGoodsList.length < 2"
                            ></div>
                            <div class="person-center-bottom">
                                <p>评价人数：{{ item.num }}条</p>
                                <p v-show="orderlineGoodsList.length < 2">
                                    订单编号：{{
                                        orderlineGoodsList[0].orderSn
                                    }}
                                </p>
                                <p v-show="orderlineGoodsList.length < 2">
                                    下单时间：{{
                                        orderlineGoodsList[0].createOrderDate
                                    }}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="center-line"></div>
                <!-- 右侧 -->
                <div class="person-center-right">
                    <div class="sheet-right">
                        <div class="shop-level">
                            <Rate
                                v-model="item.evaluateScores"
                                disabled
                                custom-icon="rate-icon"
                            />
                            <span style="font-size: 14px"
                                >{{ item.evaluateScores }}分</span
                            >
                        </div>
                        <span class="time">{{ item.createDate }}</span>
                    </div>
                    <div class="textcontent">{{ item.evaluateContent }}</div>
                    <div class="pictureShow" style="display: flex">
                        <!-- 查看评价遍历图片预览功能 -->
                        <div
                            :images="item.evaluateImg"
                            v-lazy-container="{
                                selector: 'img',
                                error: '/img/common/loading/200_200.png',
                                loading: '/img/common/loading/200_200.png',
                            }"
                        >
                            <img
                                v-for="(item1, index1) in item.evaluateImg"
                                :data-src="$imgURL + item1"
                                :key="index1"
                                class="showOne"
                            />
                        </div>
                    </div>
                    <div class="line" v-if="item.replyContent !== null"></div>
                    <div class="answer">
                        <span
                            class="answer-logo"
                            v-if="item.replyContent != null"
                        >
                            <img :data-src="$imgURL + item.storeLogo" alt />
                        </span>
                        <div class="answer-right">
                            <div
                                class="answer-top"
                                v-if="item.replyContent != null"
                            >
                                <span class="store"
                                    >{{ item.storeName }}回复：</span
                                >
                                <span class="time">{{ item.replyDate }}</span>
                            </div>
                            <div class="answer-bottom">
                                {{ item.replyContent }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { goodsDetails } from "@/api/api_03.goodsClass.js";
    import { orderDetails } from "@/api/api_06-07-01personalMyOrders.js";
    import { getLookEvaluated } from "@/api/api_06-02.evaluationSheet.js";

    import { CellGroup } from "iview";

    import personalTop from "@/components/06.personalCenter/common/personalTop.vue";

    export default {
        name: "evaluationBackupsMore",
        components: {
            personalTop,
        },
        data() {
            return {
                orderId: this.$route.query.orderID ? this.$route.query.orderID : "",
                valueText: 0,
                picture: 0,
                castemo: "",
                header: false,
                bigimg: false,
                orderlineGoodsList: [
                    {
                        browseNum: "0",
                        createDate: "",
                        evaluateExplain: null,
                        evaluateImage: "",
                        evaluateImg: [],
                        evaluateRemark: null,
                        evaluateScores: 5,
                        evaluateState: 0,
                        goodsId: "",
                        evaluateContent: "",
                        goodsName: "",
                        goodsPrice: "",
                        goodsSpecId: "",
                        goodsSubTitle: "",
                        id: "",
                        isAnonymous: 0,
                        likeCount: "0",
                        memberAvatar: "",
                        memberGrade: null,
                        memberId: "",
                        // memberName: "",
                        nickName: null,
                        num: 1,
                        orderGoodsId: "",
                        orderId: "",
                        orderSn: "",
                        readState: 0,
                        replyContent: null,
                        replyDate: null,
                        specInfo: "",
                        specMainPicture: "",
                        storeId: "",
                        storeLogo: null,
                        storeName: "",
                        change: false,
                    },
                ],
            };
        },

        computed: {},
        mounted() {
            this.getLookEvaluatedList();
        },
        methods: {
            active(item1, index1) {
                this.picture = index1;
                this.castemo = item1;
                console.log(8888, this.picture);
                console.log(4444, this.castemo);
            },

            getLookEvaluatedList() {
                getLookEvaluated(this.orderId)
                    .then((res) => {
                        console.log(res);
                        if (res && res.code == 200) {
                            this.orderlineGoodsList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },

            /**
             * 查看评价点击跳转商品详情页
             */

            goshopsDetailPage(goodsId, goodsSpecId) {
                goodsDetails({
                    goodsId: goodsId,
                    specId: goodsSpecId,
                })
                    .then((res) => {
                        console.log(res);
                        console.log(res.data);
                        if (res.code == 200) {
                            this.$router.push({
                                path: "/goodsDetails",
                                query: {
                                    goodsId: res.data.goodsDetailsVO.goodsId,
                                    specId: res.data.goodsDetailsVO.goodsVO.specId,
                                },
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },

            // 查看详情跳转到订单详情页
            gohandleDetail(orderId) {
                orderDetails({
                    id: orderId,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.$router.push({
                                path: "/personalCenterBase/myOrdersDetail",
                                query: {
                                    id: orderId,
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
    .personal-center-base {
        width: 1200px;
        margin: 0 auto;
        padding-bottom: 48px;

        .personal-center-nav {
            font-size: 13px;
            padding: 15px 0;
            color: #333333;
            cursor: pointer;
        }

        .person-center-header {
            width: 100%;
            height: 44px;
            line-height: 44px;
            background: rgba(255, 255, 255, 1);
            margin-bottom: 10px;
            padding: 0 30px;
            // cursor: pointer;

            span {
                margin-right: 20px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
            }
            span:last-child {
                cursor: pointer;
            }
            span:last-child .orderno:hover {
                color: #e72900;
            }
        }

        .person-center-content {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            background: rgba(255, 255, 255, 1);
        }

        .person-center-content-once {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }
    }

    // 面包屑导航
    .evaluationSheet:hover {
        color: #e2270b;
    }

    // 查看单商品左侧
    .person-center-left-once {
        width: 232px;
        height: 615px;
        background: rgba(255, 255, 255, 1);
        box-sizing: border-box;
        display: flex;
        justify-content: center;
        margin-right: 10px;

        .person-center-top {
            width: 100%;
            text-align: left;
            // padding: 32px;
            // padding: 30px 30px 0;

            .person-img {
                width: 172px;
                height: 172px;
                margin: auto;

                span {
                    position: absolute;
                    left: 0;
                    top: 0;
                    width: 56px;
                    height: 24px;
                }

                img {
                    width: 100%;
                    height: 100%;
                }
            }

            .click {
                cursor: pointer;
                padding: 30px 30px 0;
                .text-explain {
                    width: 169px;
                    margin-top: 20px;
                    font-size: 14px;
                    font-family: PingFangSC-Medium, PingFang SC;
                    font-weight: 600;
                    color: #222222;
                    // margin-left: 37px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }

                .text-explain:hover {
                    color: #e72900;
                }

                .explain {
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #999999;
                    // margin-left: 37px;
                    width: 140px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    margin-top: 10px;
                }

                .price {
                    // margin-left: 37px;
                    color: rgba(221, 38, 25, 1);
                    margin-top: 10px;
                    span {
                        font-size: 14px;
                        color: rgba(221, 38, 25, 1);
                    }
                    span:nth-child(2) {
                        font-weight: bold;
                        font-size: 18px;
                        display: inline-block;
                        margin: 0 -4px;
                        font-family: PingFangSC-Semibold, PingFang SC;
                    }
                    span:last-child {
                        font-weight: 600;
                    }
                }
            }

            .jump {
                cursor: pointer;
            }

            .inline {
                width: 248px;
                height: 1px;
                background: rgba(235, 235, 235, 1);
                margin: 29px 0 30px 0;
                margin-left: 15px;
            }

            .person-center-bottom {
                // width: 177px;
                margin-left: 30px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #222222;
                // padding-left: 17px;

                p {
                    // margin-left: 18px;
                    height: 12px;
                    line-height: 12px;
                    margin-bottom: 15px;
                    // width: 177px;
                }
            }
        }
    }

    // 查看多商品左侧
    .person-center-left {
        width: 300px;
        height: 534px;
        background: rgba(255, 255, 255, 1);
        box-sizing: border-box;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 10px;

        .person-center-top {
            text-align: left;
            padding: 32px;
            // margin-bottom: 30px;

            .person-img {
                width: 180px;
                height: 180px;
                margin: auto;

                span {
                    position: absolute;
                    left: 0;
                    top: 0;
                    width: 56px;
                    height: 24px;
                }

                img {
                    width: 100%;
                    height: 100%;
                }
            }

            .click {
                cursor: pointer;
                .text-explain {
                    width: 193px;
                    margin-top: 15px;
                    font-size: 16px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: rgba(58, 58, 58, 1);
                    margin-left: 42px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }

                .text-explain:hover {
                    color: #e72900;
                }

                .explain {
                    font-size: 13px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    margin-left: 42px;
                    width: 140px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    margin-top: 2px;
                }

                .price {
                    margin-left: 40px;
                    margin-top: 10px;
                    color: rgba(221, 38, 25, 1);
                    margin-bottom: 20px;
                    span {
                        font-size: 14px;
                        color: rgba(221, 38, 25, 1);
                    }
                    span:nth-child(2) {
                        font-weight: bold;
                        font-size: 20px;
                        display: inline-block;
                        margin: 0 -4px;
                    }
                    span:last-child {
                        font-weight: 600;
                    }
                }
            }

            .inline {
                width: 248px;
                height: 1px;
                background: rgba(235, 235, 235, 1);
                margin: 29px 0 30px 0;
                margin-left: 15px;
            }

            .person-center-bottom {
                width: 264px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                padding-left: 24px;

                p {
                    margin-left: 18px;
                    height: 25px;
                    line-height: 25px;
                    width: 227px;
                }
            }
        }
    }

    .price-line {
        width: 100%;
        height: 1px;
        background: rgba(235, 235, 235, 1);
        margin: 30px 0;
        // margin-left: 14px;
    }

    .center-line {
        width: 1px;
        margin-top: 31px;
        margin-bottom: 35px;
        background: rgba(235, 235, 235, 1);
    }

    .person-center-right {
        width: 948px;
        background: rgba(255, 255, 255, 1);
        padding: 30px 40px;
        box-sizing: border-box;

        .sheet-right {
            .shop-level {
                display: inline-block;
            }

            .time {
                float: right;
                width: 144px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(153, 153, 153, 1);
            }
        }

        .textcontent {
            width: 100%;
            margin-top: 10px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(119, 119, 119, 1);
            word-wrap: break-word;
            word-break: normal;
            text-align: justify;
        }

        .pictureShow {
            margin-top: 10px;
            .showOne {
                display: inline-block;
                width: 90px;
                height: 90px;
                // padding: 5px;
                margin-right: 10px;

                img {
                    width: 100%;
                    height: 100%;
                }
            }
            .showOne:hover {
                // border: 1px solid rgba(221, 38, 25, 1);
            }
        }

        .bigImg {
            margin-top: 10px;
            width: 235px;
            height: 236px;
            border: 1px dashed #ccc;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .line {
            height: 1px;
            background: rgba(235, 235, 235, 1);
            margin-bottom: 25px;
            margin-top: 25px;
        }

        .answer {
            width: 100%;
            height: 40px;
            display: flex;
            margin-bottom: 60px;

            .answer-logo {
                width: 5%;
                height: 40px;
                // border: 1px dashed #ccc;
                margin-right: 8px;

                img {
                    width: 100%;
                    height: 100%;
                }
            }

            .answer-right {
                width: 95%;

                .answer-top {
                    display: flex;
                    justify-content: space-between;

                    .store {
                        font-size: 15px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: rgba(66, 66, 66, 1);
                    }

                    .time {
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(153, 153, 153, 1);
                    }
                }

                .answer-bottom {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(113, 113, 113, 1);
                }
            }
        }
    }
    .enlarge {
        width: 300px;
        height: 300px;
        border: 1px solid #ebebeb;
    }
</style>
