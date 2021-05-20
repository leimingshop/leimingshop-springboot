<template>
    <div class="invoice" v-loading="invoiceLoading">
        <div v-if="dataList.length == 0" class="nullInvoice">
            <div>
                <!-- v-lazy-container="{ selector: 'img', error: '/static/img/common/loading/200_200.png', loading: '/static/img/common/loading/200_200.png' }" -->
                <img
                    src="/img/06.personalCenter/invoice@2x.png"
                    alt
                    style="width: 230px; height: 170px; margin: 125px 310px 0"
                />
            </div>
            <p class="nullInvoice-p">暂无发票</p>
        </div>

        <div class="invoiceContent" v-if="dataList.length > 0">
            <div class="invoiceContent-top">
                <div class="space" style="width: 341px; font-size: 14px">
                    订单详情
                </div>
                <div class="space" style="width: 130px; font-size: 14px">
                    发票类型
                </div>
                <div class="space" style="font-size: 14px">金额</div>
                <div class="space" style="font-size: 14px">开票状态</div>
                <div class="space" style="font-size: 14px">操作</div>
            </div>
            <div style="visibility">
                <div
                    class="invoiceContent-main"
                    v-for="(item, index) in dataList"
                    :key="index"
                >
                    <div class="invoiceContent-mainTop">
                        <span style="margin: 0 20px 0 0"
                            >下单时间：{{ item.createDate }}</span
                        >
                        <span
                            class="test"
                            style="margin: 0 20px 0 0"
                            @click="
                                handleDetail(
                                    item.id,
                                    item.appStatus,
                                    item.orderSn
                                )
                            "
                            >订单编号：{{ item.orderSn }}</span
                        >
                        <span
                            class="test"
                            @click="handleShopHome(item.storeId)"
                            :title="item.storeName"
                            >店铺名称：{{ item.storeName }}</span
                        >
                    </div>
                    <div style="display: flex">
                        <div class="invoiceContent-left">
                            <div
                                class="invoiceContent-leftPhoto"
                                v-for="(orderList,
                                indexs) in item.orderGoodsDTOList"
                                :key="indexs"
                            >
                                <div
                                    v-lazy-container="{
                                        selector: 'img',
                                        error:
                                            '/img/common/loading/200_200.png',
                                        loading:
                                            '/img/common/loading/200_200.png',
                                    }"
                                >
                                    <img
                                        :data-src="
                                            $imgURL + orderList.goodsImage
                                        "
                                        style="
                                            width: 68px;
                                            height: 68px;
                                            cursor: pointer;
                                        "
                                        @click="
                                            goDetailPage(
                                                orderList.specId,
                                                orderList.goodsId
                                            )
                                        "
                                    />
                                </div>
                                <div
                                    style="width: 302px; margin: 0 0 0 14px"
                                    @click="
                                        goDetailPage(
                                            orderList.specId,
                                            orderList.goodsId
                                        )
                                    "
                                >
                                    <div class="invoiceContent-leftp">
                                        <!-- <span class="hotSale" v-if="orderList.activityType == 3">秒杀</span>
                                        <span class="hotSale" v-if="orderList.activityType == 4">拼团</span>
                                        <span class="hotSale"
                    v-if="orderList.activityType !=3 && orderList.activityType !=4">热卖</span>-->
                                        {{ orderList.goodsName }}
                                    </div>
                                    <div
                                        class="introduce"
                                        style="
                                            display: flex;
                                            font-size: 13px;
                                            color: #999999;
                                        "
                                    >
                                        <span>
                                            <span
                                                :title="
                                                    orderList.specAttrValueName
                                                "
                                                >{{
                                                    orderList.specAttrValueName
                                                }}</span
                                            >
                                            <span
                                                >数量{{
                                                    orderList.goodsNum
                                                }}</span
                                            >
                                        </span>
                                        <!-- <span style="display:flex;margin:0 5px">数量{{orderList.goodsNum}}</span> -->
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="space" style="width: 130px">
                            <span
                                v-if="
                                    item.invoiceEvolve == 1 ||
                                    item.invoiceEvolve == 3
                                "
                                >{{
                                    item.memberInvoiceType === 1
                                        ? "电子发票"
                                        : item.memberInvoiceType === 2
                                        ? "纸质发票"
                                        : "增值税专项发票"
                                }}</span
                            >
                            <span
                                v-if="
                                    item.invoiceEvolve == 2 ||
                                    item.invoiceEvolve == 4
                                "
                                >{{
                                    item.storeInvoiceType === 1
                                        ? "电子发票"
                                        : item.storeInvoiceType === 2
                                        ? "纸质发票"
                                        : "增值税专项发票"
                                }}</span
                            >
                        </div>
                        <div
                            class="space"
                            style="display: flex; flex-flow: wrap"
                        >
                            ￥
                            <span style="font-size: 16px; font-weight: 600">{{
                                item.orderAmount
                            }}</span>
                        </div>
                        <div
                            class="space"
                            style="
                                color: #dd2619;
                                font-size: 16px;
                                font-wight: 400;
                            "
                        >
                            <span>{{
                                item.invoiceEvolve == 1
                                    ? "待开票"
                                    : item.invoiceEvolve == 2
                                    ? "已开票"
                                    : item.invoiceEvolve == 3
                                    ? "待换开"
                                    : item.invoiceEvolve == 4
                                    ? "已换开"
                                    : ""
                            }}</span>
                        </div>
                        <div class="space">
                            <Button
                                class="space-btn"
                                v-if="item.invoiceEvolve == null"
                                @click="
                                    toApply(
                                        item.id,
                                        item.orderSn,
                                        item.afterFlag
                                    )
                                "
                                >申请发票</Button
                            >
                            <Button
                                class="space-btn"
                                v-else
                                @click="toCheck(item.id, item.orderSn)"
                                >查看开票</Button
                            >
                            <span
                                class="space-p"
                                v-if="item.invoiceEvolve == 2"
                                :loading="loading"
                                @click="toApply(item.id, item.orderSn)"
                                >申请换开</span
                            >
                            <span
                                class="space-p"
                                @click="
                                    handleDetail(
                                        item.id,
                                        item.appStatus,
                                        item.orderSn
                                    )
                                "
                                >订单详情</span
                            >
                        </div>
                    </div>
                </div>
            </div>
            <!-- <div class="address-management3"> -->
            <!-- 分页 -->
            <paging
                class="paging"
                :totalCount="totalCount"
                :current="page"
                :pageSize="limit"
                @handlePageChange="handlePageChange"
                v-if="dataList.lenghth != 0 && totalCount > limit"
            />
            <div v-else style="height: 60px"></div>
            <!-- </div> -->
        </div>
    </div>
</template>
<script>
    import { invoiceAfter, invoiceCheck } from "@/api/api_06-13-06invoiceTitle.js";
    import paging from "@/components/common/paging";
    export default {
        name: "useInvoice",
        components: {
            paging,
        },
        head() {
            return {
                title: "我的发票",
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
                loading: false,
                invoiceLoading: true,
                id: "",
                dataList: {
                    id: "",
                    appStatus: "",
                    createDate: "",
                    orderSn: "",
                    storeName: "",
                    goodsImage: "",
                    goodsName: "",
                    specInfo: "",
                    goodsNum: "",
                    goodsTotalPrice: "",
                    paging: "",
                    invoiceEvolve: "",
                    memberInvoiceType: "",
                    storeInvoiceType: "",
                    orderId: "",
                    storeId: "",
                    orderSn: "",
                    afterFlag: "",
                },
                orderGoodsDTOList: {
                    goodsTotalPrice: "",
                },
                orderList: {},
                data: {
                    id: "",
                    appStatus: "",
                },
                totalCount: 1,
                page: 1,
                limit: 10,
            };
        },
        created() {
            this.getOrderList(1, 10);
        },
        // mounted(){
        //     if(this.$route.name == 'useInvoice'){
        //         this.personCenterTitle='我的发票'
        //         var SET_DISPLAY_STYLE_DATA = {
        //             index1Flag:2,
        //             index2Flag:0,
        //             personCenterTitle:'我的发票'
        //         }
        //         this.$store.commit('useInvoice/SET_DISPLAY_STYLE',SET_DISPLAY_STYLE_DATA );
        //     }
        // },
        methods: {
            // 分页
            handlePageChange(val) {
                this.page = val;
                this.getOrderList(val, this.limit);
            },
            getOrderList(page, limit) {
                //获取列表数据
                let obj = {
                    page: this.page,
                    limit: this.limit,
                };
                invoiceAfter(obj)
                    .then((res) => {
                        if (res.code == 200) {
                            this.dataList = res.data.list;
                            this.totalCount = res.data.total;
                            this.invoiceLoading = false;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            goDetailPage(specId, goodsId) {
                //跳转到商品详情页页面
                let newpage = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId: goodsId,
                        specId: specId,
                    },
                });
                window.open(newpage.href, "_blank");
            },
            // 进入店铺首页
            handleShopHome(storeId) {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: { storeId: storeId },
                });
                // console.log(this.dataList.storeId)
                window.open(routeUrl.href, "_blank");
            },
            //订单详情
            handleDetail(id, appStatus, orderSn) {
                console.log(this.id);
                this.$router.push({
                    path: "/personalCenterBase/myOrdersDetail",
                    query: {
                        id: id,
                        appStatus: appStatus,
                        orderSn: orderSn,
                    },
                });
            },
            //查看发票
            toCheck(id, orderSn) {
                let newpage = this.$router.resolve({
                    path: "/personalCenterBase/invoiceBase/verificationInvoice",
                    query: {
                        orderId: id,
                        orderSn: orderSn,
                    },
                });
                window.open(newpage.href, "_blank");
            },
            //申请发票
            toApply(id, orderSn, afterFlag) {
                // console.log(this.afterFlag)
                // afterFlag=this.afterFlag
                this.loading = true;
                if (afterFlag == 1) {
                    // console.log("该订单包含退换货商品，暂不支持申请开票或换开发票！")
                    this.$Message.warning({
                        content:
                            "该订单包含退换货商品，暂不支持申请开票或换开发票！",
                        duration: 2,
                    });
                    this.loading = false;
                } else {
                    invoiceCheck(id)
                        .then((res) => {
                            if (res.code == 200) {
                                let newpage = this.$router.resolve({
                                    path:
                                        "/personalCenterBase/invoiceBase/applyInvoice",
                                    query: {
                                        orderId: id,
                                        orderSn: orderSn,
                                    },
                                });
                                window.open(newpage.href, "_blank");
                            }
                        })
                        .catch((error) => {
                            console.log(error);
                        });
                    // let newpage = this.$router.resolve({
                    //     name: 'applyInvoice',
                    //     query: {
                    //         orderId: id,
                    //         orderSn: orderSn
                    //     }
                    // })
                    // window.open(newpage.href, '_blank');
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    .invoice {
        width: 948px;
        min-height: 600px;
        background: #ffffff;

        .invoiceTop {
            width: 948px;
            height: 55px;
            border-bottom: 1px solid #dd2619;
            display: flex;
            justify-content: space-between;

            .invoiceTop-li {
                width: 120px;
                height: 55px;
                line-height: 55px;
                font-size: 15px;
                font-weight: 500;
                color: #666666;
                text-align: center;
                cursor: pointer;
            }

            .invoiceTop-on {
                width: 120px;
                height: 55px;
                line-height: 55px;
                font-size: 15px;
                font-weight: 500;
                color: #ffffff;
                text-align: center;
                background: #dd2619;
            }

            .invoiceTop-left {
                width: 160px;
                font-size: 13px;
                color: #666666;
                padding: 28px 32px 10px 0;

                img {
                    width: 13px;
                    height: 13px;
                }
            }
        }

        .invoiceContent {
            padding: 40px 40px 0px;
            position: relative;

            .invoiceContent-top {
                height: 34px;
                line-height: 34px;
                background: #f0efef;
                margin: 0 0 13px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                display: flex;
            }

            .invoiceContent-main {
                margin: 20px 0 0 0;
                box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.16);

                .invoiceContent-mainTop {
                    height: 44px;
                    line-height: 44px;
                    background: #f6f6f6;
                    box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.16);
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    padding: 0 30px;
                    white-space: nowrap;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }

                .invoiceContent-left {
                    display: flex;
                    width: 341px;
                    flex-direction: column;
                    text-align: left;
                    padding: 10px 0 10px 30px;
                    -webkit-box-orient: vertical;
                    -webkit-box-direction: normal;
                    box-sizing: border-box;

                    .invoiceContent-leftPhoto {
                        width: 100%;
                        padding: 20px 0;
                        border-bottom: 1px solid #e8e8e8;
                        display: flex;
                        -webkit-box-align: center;
                        align-items: center;

                        &:last-child {
                            border-bottom: 0;
                        }
                    }

                    .invoiceContent-leftp {
                        height: 40px;
                        font-size: 16px;
                        font-weight: 500;
                        line-height: 20px;
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                        text-overflow: ellipsis;
                        overflow: hidden;
                    }
                }
            }
        }
    }

    .nullInvoice {
        width: 948px;
        height: 989px;
        padding: 0 40px;

        .nullInvoice-p {
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
            text-align: center;
        }
    }

    .hotSale {
        width: 31px;
        height: 16px;
        font-size: 12px;
        border: 1px solid #dd2619;
        color: #ffffff;
        background: #dd2619;
        border-radius: 4px;
    }

    .goods {
        height: 48px;
        margin: 0 0 4px 0;
        font-size: 16px;
        color: #333333;
        font-weight: 500;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        cursor: pointer;
    }

    .space {
        width: 131px;
        text-align: center;
        box-sizing: content-box;
        margin-right: -1px;
        display: -webkit-box;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
        -webkit-box-pack: center;
        -ms-flex-pack: center;
        justify-content: center;
        flex-flow: column;
        font-size: 14px;
        font-weight: 400;
        color: #333333;
        border-left: 1px solid #ebebeb;

        .space-btn {
            width: 83px;
            height: 32px;
            background: linear-gradient(
                270deg,
                rgba(253, 75, 3, 1) 0%,
                rgba(222, 42, 26, 1) 100%
            );
            border-radius: 16px;
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: rgba(255, 255, 255, 1);
            border: none;
        }

        .space-p {
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
            margin: 12px 0 0 0;
            cursor: pointer;
        }
    }

    .address-management3 {
        text-align: center;
        // margin-top: 20px;

        .ivu-page {
            position: relative;
        }
    }
    .test {
        cursor: pointer;
    }
    .test :hover {
        color: #dd2619;
    }
    /deep/.person-center-right {
        height: 1395px;
        background: #ffffff;
    }

    .introduce {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
    }
</style>
