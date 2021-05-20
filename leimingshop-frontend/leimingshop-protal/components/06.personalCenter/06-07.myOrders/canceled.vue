<template>
    <div class="canceled">
        <Card class="order-info">
            <div class="o-head">
                <p class="o-time">下单时间：{{ data.createDate }}</p>
                <p class="o-num" @click="handleDetail">
                    订单编号：{{ data.orderSn }}
                </p>
                <!--多店铺待付款状态下店铺不跳转的条件：storId为0不跳转-->
                <p
                    class="o-shopName"
                    style="width: 260px"
                    @click="handleShopHome"
                    v-if="data.storeId != 0"
                >
                    店铺名称：
                    <span :title="data.storeName">{{ data.storeName }}</span>
                </p>
                <p class="o-shopName" style="width: 260px" v-else>
                    店铺名称：
                    <span :title="data.storeName">{{ data.storeName }}</span>
                </p>

                <span class="o-remove">
                    <div
                        @mouseout="mouseOut"
                        @mouseover="mouseOver"
                        class="personalSize"
                    >
                        <img
                            :src="trash"
                            alt
                            @click="remove.handler"
                            style="
                                cursor: pointer;
                                height: 14px;
                                width: 14px;
                                margin: 15px 0;
                            "
                        />
                        <div
                            v-text="remove.title"
                            @click="remove.handler"
                            class="delete"
                        ></div>
                    </div>
                </span>
            </div>

            <div class="o-content">
                <div class="o-goods-list" :style="{ width: thead[0].width }">
                    <div
                        class="o-goods-desc"
                        v-for="(item, index) in data.orderGoodsDTOList"
                        :key="index"
                        @click="goDetail(item.goodsId, item.specId)"
                        v-lazy-container="{
                            selector: 'img',
                            error: '/img/common/loading/200_200.png',
                            loading: '/img/common/loading/200_200.png',
                        }"
                    >
                        <img
                            class="g-image"
                            :data-src="$imgURL + item.goodsImage"
                            alt
                        />
                        <p class="p-info">
                            <span
                                class="g-name twoLineEllipsis"
                                :title="item.goodsName"
                            >
                                <!--                                <em class="emem"-->
                                <!--                                    v-show="item.activityType!=0 || item.activityType">{{item.activityType | filterOrderLabel}}</em>-->
                                <span class="orderName">{{
                                    item.goodsName
                                }}</span>
                            </span>
                            <span class="g-attribute"
                                >{{ item.specAttrValueName }} 数量：{{
                                    item.goodsNum
                                }}</span
                            >
                        </p>
                    </div>
                </div>

                <div
                    id="o-consignee-name"
                    :style="{ width: thead[1].width }"
                    :title="data.trueName"
                >
                    {{ data.trueName }}
                </div>

                <div class="o-freight" :style="{ width: thead[2].width }">
                    <!-- <span>
            <i>¥</i>
            {{ data.orderAmount }}
          </span> -->
                    <div
                        class="p-price"
                        v-html="$options.filters.filterPrice(data.orderAmount)"
                    ></div>
                    <em>含运费：¥{{ data.shippingFee }}</em>
                    <em>{{ data.paymentName }}</em>
                </div>

                <div class="o-status" :style="{ width: thead[3].width }">
                    {{ status.title }}
                </div>

                <div class="o-operate" :style="{ width: thead[4].width }">
                    <Button
                        v-for="item in operate"
                        :key="item.title"
                        :type="item.type"
                        v-text="item.title"
                        @click="item.handler()"
                    ></Button>
                </div>
            </div>
        </Card>
        <!--删除订单二次确认框-->
        <!-- <second-confirm v-show="modal1" :promptTitle="promptTitle" @handleConfirmEnd="handleConfirmEnd"
    @handleConfirm="handleRemove"></second-confirm>-->
        <Modal v-model="modal1" width="400" class="delete">
            <div style="text-align: center">
                <p style="font-size: 24px; color: #333">{{ promptTitle }}</p>
            </div>
            <div slot="footer" class="footerBtn">
                <Button
                    class="newBtn newBtn1"
                    style="width: 68px; background: #f5f5f5; margin-right: 48px"
                    size="default"
                    @click.stop="handleConfirmEnd"
                    >取消</Button
                >
                <Button
                    class="newBtn"
                    style="
                        width: 68px;
                        margin: 0 0 0 10px;
                        background: rgba(221, 38, 25, 1);
                        color: rgba(255, 255, 255, 1);
                    "
                    size="default"
                    @click="handleRemove"
                    >确定</Button
                >
            </div>
        </Modal>
    </div>
</template>

<script>
    import { OrderDelete, CartAgain } from "@/api/api_06-07-01personalMyOrders.js";
    import { mapActions } from "vuex";
    import secondConfirm from "./secondConfirm";
    import { OrderCancelReason } from "@/api/api_06-07-01personalMyOrders.js";
    export default {
        name: "canceled",
        data() {
            return {
                trash: "/img/06.personalCenter/trash1.png",
                modal1: false,
                promptTitle: "是否删除这个订单？",
                status: {
                    title: "已取消",
                },

                remove: {
                    title: "删除",
                    handler: this.handleRemoveBefore,
                    type: "text",
                },

                operate: [
                    {
                        title: "查看详情",
                        handler: this.handleDetail,
                        type: "text",
                    },
                    {
                        title: "再次购买",
                        handler: this.handleAgain,
                        type: "text",
                    },
                ],
            };
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
            secondConfirm,
        },
        created() {},
        computed: {},
        watch: {},
        mounted() {},
        methods: {
            ...mapActions("main", ["actCartList"]),
            handleRemoveBefore() {
                this.modal1 = true;
            },
            handleConfirmEnd() {
                this.modal1 = false;
            },
            //删除订单
            async handleRemove() {
                try {
                    let res = await OrderDelete(this.data.id);
                    if (res && res.code == 200) {
                        this.$Message.success("删除成功");
                        this.$emit("getDataList");
                        this.modal1 = false;
                    }
                } catch (e) {}
            },
            //鼠标悬浮图片变化
            mouseOut: function () {
                this.trash = "/img/06.personalCenter/trash1.png";
            },
            mouseOver: function () {
                this.trash = "/img/06.personalCenter/trashRed.png";
            },
            // 进入店铺首页
            handleShopHome() {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: {
                        storeId: this.data.storeId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
            //查看详情
            handleDetail() {
                this.$router.push({
                    path: "/personalCenterBase/myOrdersDetail",
                    query: {
                        id: this.data.id,
                    },
                });
            },
            //再次购买
            async handleAgain() {
                let obj = {
                    saveCartDTO: [],
                };
                this.data.orderGoodsDTOList.forEach((element) => {
                    obj.saveCartDTO.push({
                        goodsNum: 1,
                        specId: element.specId,
                    });
                });
                try {
                    let res = await CartAgain(obj);
                    if (res && res.code == 200) {
                        this.actCartList();
                        this.$Message.success("添加购物车成功");
                        this.$emit("getDataList");
                        this.$router.push({
                            path: "/shoppingCar",
                        });
                    }
                } catch (e) {}
            },
            //跳转到商品详情页
            goDetail(goodsId, specId) {
                // this.$router.push({
                //   name: "goodsDetails",
                //   query: {
                //     goodsId,
                //     specId
                //   }
                // });
                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId,
                        specId,
                    },
                });

                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/order-comp.scss";
    @import "@/assets/scss/modules/modal.scss";

    /deep/ .ivu-modal {
        border: 8px solid rgba(0, 0, 0, 0.15);
        border-radius: 10px;
    }
    .delete {
        /deep/ .ivu-modal-content {
            border-radius: 0;
        }
        /deep/ .ivu-modal-body {
            padding: 40px 16px 30px 16px;
        }
        /deep/ .ivu-modal-footer {
            border: none;
            height: 90px;
            .footerBtn {
                display: flex;
                justify-content: center;
                padding: 0;
            }
        }
        /deep/ .ivu-modal-close {
            right: 10px;
            top: 10px;
        }
    }
    .newBtn {
        width: 69px;
        height: 34px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 2px;
        &:hover {
            color: #333333;
        }
    }
    .newBtn1:hover {
        border-color: #dcdee2;
    }

    .o-status {
        font-size: 12px;
        color: #999999 !important;
    }

    /deep/ .mask-content {
        width: 378px !important;
        height: 200px;
        background: rgba(255, 255, 255, 1);
        border-radius: 6px;

        /deep/ .p2 {
            font-size: 24px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(51, 51, 51, 1);
            line-height: 33px;
            margin-left: 20px;
        }

        /deep/ .button-group {
            margin-top: 50px !important;
            font-size: 15px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;

            /deep/.button-left {
                width: 68px !important;
                height: 32px !important;
                background: rgba(245, 245, 245, 1) !important;
                border-radius: 1px !important;
                border: 1px solid rgba(221, 221, 221, 1) !important;
                color: rgba(51, 51, 51, 1) !important;
                line-height: 21px;
                letter-spacing: 1px;
                margin-right: 10px;
            }

            /deep/.button-right {
                width: 68px !important;
                height: 32px !important;
                background: rgba(221, 38, 25, 1) !important;
                border-radius: 1px !important;
                border: 1px solid rgba(221, 38, 25, 1) !important;
                color: rgba(255, 255, 255, 1) !important;
                line-height: 21px !important;
                letter-spacing: 1px;
            }
        }
    }
    .personalSize {
        display: flex;
        &:hover {
            color: #dd2619;
            cursor: pointer;
            .delete {
                color: #dd2619;
            }
        }
    }
    .o-operate {
        button {
            height: 24px;
            &:hover {
                color: #dd2619;
            }
        }
    }
</style>
