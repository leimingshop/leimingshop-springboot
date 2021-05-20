<<<<<<< HEAD
<template>
    <div class="intent" v-loading="evaluatedLoading">
        <div
            class="completed"
            v-if="!evaluatedLoading && evaluateListObj.list"
            v-for="(item, index) in evaluateListObj.list"
            :key="index"
        >
            <Card :bordered="true" class="order-info">
                <div class="o-head">
                    <p>下单时间：{{ item.createDate }}</p>
                    <p
                        @click="
                            gohandleDetail(item.orderGoodsDTOList[0].orderId)
                        "
                    >
                        订单编号：<span>{{ item.orderSn }}</span>
                    </p>
                    <p
                        :title="item.storeName"
                        @click="handleShopHome(item.storeId)"
                    >
                        店铺名称：<span>{{ item.storeName }}</span>
                    </p>
                </div>
                <!-- 卡片下面部分 -->
                <div class="o-content">
                    <div class="o-goods-list">
                        <div
                            class="o-goods-desc"
                            v-for="(item1, index1) in item.orderGoodsDTOList"
                            :key="index1"
                            @click="
                                goshopDetailPage(item1.goodsId, item1.specId)
                            "
                        >
                            <div
                                class="details-img"
                                v-lazy-container="{
                                    selector: 'img',
                                    error:
                                        '/img/common/loading/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <img
                                    class="g-image"
                                    :data-src="$imgURL + item1.goodsImage"
                                    alt
                                />
                            </div>
                            <div class="youth">
                                <span class="g-name" :title="item1.goodsName">
                                    {{ item1.goodsName }}
                                </span>
                                <div class="g-attribute">
                                    <span :title="item1.specAttrValueName">{{
                                        item1.specAttrValueName
                                    }}</span>
                                    <span
                                        :style="
                                            item1.specAttrValueName !== null
                                                ? 'margin-left:6px;'
                                                : 'margin-left:0px'
                                        "
                                        >数量:{{ item1.goodsNum }}</span
                                    >
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="o-consignee-name" :title="item.buyerName">
                        {{ item.buyerName }}
                    </div>
                    <div class="o-freight">
                        <span>￥</span>
                        <span>{{
                            item.orderAmount.toString().split(".")[0]
                        }}</span>
                        <span
                            >.{{
                                item.orderAmount.toString().split(".").length >
                                1
                                    ? item.orderAmount.toString().split(".")[1]
                                    : "00"
                            }}</span
                        >
                    </div>
                    <div class="o-operate">
                        <p
                            class="right-p1"
                            @click="
                                goshopevaluate(
                                    item.orderGoodsDTOList[0].orderId
                                )
                            "
                        >
                            查看评价
                        </p>
                        <p
                            class="right-p2"
                            @click="
                                gohandleDetail(
                                    item.orderGoodsDTOList[0].orderId
                                )
                            "
                        >
                            查看详情
                        </p>
                    </div>
                </div>
            </Card>
        </div>

        <!-- 无数据的情况 -->
        <div
            class="no-data"
            v-if="!evaluatedLoading && evaluateListObj.list.length == 0"
        >
            <div class="no-data-main">
                <div class="no-data-main-content">
                    <img :src="'/img/06.personalCenter/noshop.png'" alt />
                    <div style="font-size: 16px; color: rgba(102, 102, 102, 1)">
                        暂无已评价商品
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { orderDetails } from "@/api/api_06-07-01personalMyOrders.js";
    import { goodsDetails } from "@/api/api_03.goodsClass.js";
    import { mapState } from "vuex";

    export default {
        name: "evaluationSheet", //个人中心评价晒单
        data() {
            return {
                pagingFla2: true,
            };
        },
        props: ["evaluatedLoading"],
        computed: {
            ...mapState("evaluationSheet", ["evaluateListObj"]),
        },
        mounted() {
            if (this.evaluateListObj.list.length == 0) {
                this.$emit("childOk", this.pagingFlag2);
            }
        },

        methods: {
            // 查看评价跳转到商品评价页
            goshopevaluate(orderID) {
                this.$router.push({
                    path: "/evaluationBackupsMore",
                    query: {
                        orderID: orderID,
                    },
                });
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
                    .catch((err) => {
                        console.log(err);
                    });
            },

            // 跳转商品详情页
            goshopDetailPage(goodsId, specId) {
                goodsDetails({
                    goodsId: goodsId,
                    specId: specId,
                })
                    .then((res) => {
                        console.log(res.data.goodsDetailsVO);
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
                    .catch((err) => {
                        console.log(err);
                    });
            },

            // 跳转店铺首页
            handleShopHome(storeId) {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: {
                        storeId: storeId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    a {
        text-decoration: none;
        color: #333;
    }

    .intent {
        padding: 40px;
    }

    .order-info {
        margin-bottom: 20px;
    }

    /deep/ .ivu-card-body {
        padding: 0;
    }

    .o-head {
        width: 100%;
        height: 50px;
        line-height: 50px;
        padding: 0 30px;
        background: #f9f9f9;
        display: flex;
        justify-content: left;
        align-items: center;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        & > p {
            color: #666666;
            margin-right: 15px;
        }
        p:nth-child(2) {
            cursor: pointer;
            span:hover {
                color: rgba(227, 42, 28, 1);
            }
        }
        p:last-child {
            cursor: pointer;
            margin-right: 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            span:hover {
                color: rgba(227, 42, 28, 1);
            }
        }
    }

    .o-content {
        display: flex;

        & > div {
            min-height: 122px;
            box-sizing: content-box;
            margin-right: -1px;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;

            &.o-goods-list {
                width: 453px;
                flex-direction: column;
                text-align: left;
                padding: 10px 0 10px 30px;
                box-sizing: border-box;
            }

            &.o-consignee-name {
                width: 190px;
                font-size: 14px;
                color: rgba(51, 51, 51, 1);
                font-weight: normal;
                margin-left: 20px;
                text-align: left;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 2;
            }

            &.o-freight {
                width: 210px;
                text-align: left;
                span {
                    font-size: 14px;
                    color: rgba(51, 51, 51, 1);
                }
                span:first-child {
                    margin-top: 4px;
                    font-weight: 400;
                }
                span:nth-child(2) {
                    font-size: 20px;
                    font-weight: 600;
                    display: inline-block;
                }
                span:last-child {
                    font-weight: 600;
                    margin-top: 6px;
                }
            }

            &.o-operate {
                width: 170px;

                .right-p1 {
                    font-size: 16px;
                    font-weight: 500;
                    color: rgba(231, 41, 0, 1);
                    margin-bottom: 10px;
                    cursor: pointer;
                }

                .right-p2 {
                    font-size: 13px;
                    font-weight: 400;
                    color: rgba(102, 102, 102, 1);
                    cursor: pointer;
                }
            }
        }
    }

    .o-goods-desc {
        width: 100%;
        padding: 20px 0;
        border-bottom: 1px solid #e8e8e8;
        display: flex;
        align-items: center;
        cursor: pointer;
        &:last-child {
            border-bottom: 0;
        }
    }

    .details-img {
        width: 68px;
        height: 68px;
        margin-right: 15px;
        img {
            width: 100%;
            height: 100%;
        }
    }

    .g-name {
        width: 223px;
        font-size: 16px;
        color: rgba(51, 51, 51, 1);
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        text-align: left;
        height: 44px;
        margin-bottom: 10px;
    }

    .g-name:hover {
        color: #e72900;
    }

    .g-attribute {
        width: 223px;
        text-align: left;
        font-size: 13px;
        color: rgba(153, 153, 153, 1);
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        // span:first-child{
        //     margin-right: 6px;
        // }
    }

    .o-operate {
        flex-direction: column;
    }

    .no-data {
        margin-top: 183px;
        .no-data-main {
            display: flex;
            justify-content: center;
            .no-data-main-content {
                display: felx;
                justify-content: center;
                width: 202px;
                height: 146px;
                img {
                    width: 100%;
                    height: 100%;
                }
                div {
                    text-align: center;
                    margin-top: 30px;
                }
            }
        }
    }
</style>
=======
<template>
    <div class="intent" v-loading="evaluatedLoading">
        <div
            class="completed"
            v-if="!evaluatedLoading && evaluateListObj.list"
            v-for="(item, index) in evaluateListObj.list"
            :key="index"
        >
            <Card :bordered="true" class="order-info">
                <div class="o-head">
                    <p>下单时间：{{ item.createDate }}</p>
                    <p
                        @click="
                            gohandleDetail(item.orderGoodsDTOList[0].orderId)
                        "
                    >
                        订单编号：<span>{{ item.orderSn }}</span>
                    </p>
                    <p
                        :title="item.storeName"
                        @click="handleShopHome(item.storeId)"
                    >
                        店铺名称：<span>{{ item.storeName }}</span>
                    </p>
                </div>
                <!-- 卡片下面部分 -->
                <div class="o-content">
                    <div class="o-goods-list">
                        <div
                            class="o-goods-desc"
                            v-for="(item1, index1) in item.orderGoodsDTOList"
                            :key="index1"
                            @click="
                                goshopDetailPage(item1.goodsId, item1.specId)
                            "
                        >
                            <div
                                class="details-img"
                                v-lazy-container="{
                                    selector: 'img',
                                    error:
                                        '/static/img/common/loading/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <img
                                    class="g-image"
                                    :data-src="$imgURL + item1.goodsImage"
                                    alt
                                />
                            </div>
                            <div class="youth">
                                <span class="g-name" :title="item1.goodsName">
                                    {{ item1.goodsName }}
                                </span>
                                <div class="g-attribute">
                                    <span :title="item1.specAttrValueName">{{
                                        item1.specAttrValueName
                                    }}</span>
                                    <span
                                        :style="
                                            item1.specAttrValueName !== null
                                                ? 'margin-left:6px;'
                                                : 'margin-left:0px'
                                        "
                                        >数量:{{ item1.goodsNum }}</span
                                    >
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="o-consignee-name" :title="item.buyerName">
                        {{ item.buyerName }}
                    </div>
                    <div class="o-freight">
                        <span>￥</span>
                        <span>{{
                            item.orderAmount.toString().split(".")[0]
                        }}</span>
                        <span
                            >.{{
                                item.orderAmount.toString().split(".").length >
                                1
                                    ? item.orderAmount.toString().split(".")[1]
                                    : "00"
                            }}</span
                        >
                    </div>
                    <div class="o-operate">
                        <p
                            class="right-p1"
                            @click="
                                goshopevaluate(
                                    item.orderGoodsDTOList[0].orderId
                                )
                            "
                        >
                            查看评价
                        </p>
                        <p
                            class="right-p2"
                            @click="
                                gohandleDetail(
                                    item.orderGoodsDTOList[0].orderId
                                )
                            "
                        >
                            查看详情
                        </p>
                    </div>
                </div>
            </Card>
        </div>

        <!-- 无数据的情况 -->
        <div
            class="no-data"
            v-if="!evaluatedLoading && evaluateListObj.list.length == 0"
        >
            <div class="no-data-main">
                <div class="no-data-main-content">
                    <img :src="'/img/06.personalCenter/noshop.png'" alt />
                    <div style="font-size: 16px; color: rgba(102, 102, 102, 1)">
                        暂无已评价商品
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { orderDetails } from "@/api/api_06-07-01personalMyOrders.js";
    import { goodsDetails } from "@/api/api_03.goodsClass.js";
    import { mapState } from "vuex";

    export default {
        name: "evaluationSheet", //个人中心评价晒单
        data() {
            return {
                pagingFla2: true,
            };
        },
        props: ["evaluatedLoading"],
        computed: {
            ...mapState("evaluationSheet", ["evaluateListObj"]),
        },
        mounted() {
            if (this.evaluateListObj.list.length == 0) {
                this.$emit("childOk", this.pagingFlag2);
            }
        },

        methods: {
            // 查看评价跳转到商品评价页
            goshopevaluate(orderID) {
                this.$router.push({
                    path: "/evaluationBackupsMore",
                    query: {
                        orderID: orderID,
                    },
                });
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
                    .catch((err) => {
                        console.log(err);
                    });
            },

            // 跳转商品详情页
            goshopDetailPage(goodsId, specId) {
                goodsDetails({
                    goodsId: goodsId,
                    specId: specId,
                })
                    .then((res) => {
                        console.log(res.data.goodsDetailsVO);
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
                    .catch((err) => {
                        console.log(err);
                    });
            },

            // 跳转店铺首页
            handleShopHome(storeId) {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: {
                        storeId: storeId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    a {
        text-decoration: none;
        color: #333;
    }

    .intent {
        padding: 40px 40px 0px;
    }

    .order-info {
        margin-bottom: 20px;
    }

    /deep/ .ivu-card-body {
        padding: 0;
    }

    .o-head {
        width: 100%;
        height: 50px;
        line-height: 50px;
        padding: 0 30px;
        background: #f9f9f9;
        display: flex;
        justify-content: left;
        align-items: center;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        & > p {
            color: #666666;
            margin-right: 15px;
        }
        p:nth-child(2) {
            cursor: pointer;
            span:hover {
                color: rgba(227, 42, 28, 1);
            }
        }
        p:last-child {
            cursor: pointer;
            margin-right: 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            span:hover {
                color: rgba(227, 42, 28, 1);
            }
        }
    }

    .o-content {
        display: flex;

        & > div {
            min-height: 122px;
            box-sizing: content-box;
            margin-right: -1px;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;

            &.o-goods-list {
                width: 453px;
                flex-direction: column;
                text-align: left;
                padding: 10px 0 10px 30px;
                box-sizing: border-box;
            }

            &.o-consignee-name {
                width: 190px;
                font-size: 14px;
                color: rgba(51, 51, 51, 1);
                font-weight: normal;
                margin-left: 20px;
                text-align: left;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 2;
            }

            &.o-freight {
                width: 210px;
                text-align: left;
                span {
                    font-size: 14px;
                    color: rgba(51, 51, 51, 1);
                }
                span:first-child {
                    margin-top: 4px;
                    font-weight: 400;
                }
                span:nth-child(2) {
                    font-size: 20px;
                    font-weight: 600;
                    display: inline-block;
                }
                span:last-child {
                    font-weight: 600;
                    margin-top: 6px;
                }
            }

            &.o-operate {
                width: 170px;

                .right-p1 {
                    font-size: 16px;
                    font-weight: 500;
                    color: rgba(231, 41, 0, 1);
                    margin-bottom: 10px;
                    cursor: pointer;
                }

                .right-p2 {
                    font-size: 13px;
                    font-weight: 400;
                    color: rgba(102, 102, 102, 1);
                    cursor: pointer;
                }
            }
        }
    }

    .o-goods-desc {
        width: 100%;
        padding: 20px 0;
        border-bottom: 1px solid #e8e8e8;
        display: flex;
        align-items: center;
        cursor: pointer;
        &:last-child {
            border-bottom: 0;
        }
    }

    .details-img {
        width: 68px;
        height: 68px;
        margin-right: 15px;
        img {
            width: 100%;
            height: 100%;
        }
    }

    .g-name {
        width: 223px;
        font-size: 16px;
        color: rgba(51, 51, 51, 1);
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        text-align: left;
        height: 44px;
        margin-bottom: 10px;
    }

    .g-name:hover {
        color: #e72900;
    }

    .g-attribute {
        width: 223px;
        text-align: left;
        font-size: 13px;
        color: rgba(153, 153, 153, 1);
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        // span:first-child{
        //     margin-right: 6px;
        // }
    }

    .o-operate {
        flex-direction: column;
    }

    .no-data {
        margin-top: 183px;
        .no-data-main {
            display: flex;
            justify-content: center;
            .no-data-main-content {
                display: felx;
                justify-content: center;
                width: 202px;
                height: 146px;
                img {
                    width: 100%;
                    height: 100%;
                }
                div {
                    text-align: center;
                    margin-top: 30px;
                }
            }
        }
    }
</style>
>>>>>>> db9b85181880ef2ea3306801b187f557b7325eca
