<template>
    <div class="apply-after-sales">
        <!-- 订单商品信息 -->
        <goods-info
            ref="goodsInfo"
            :orderInfo="orderInfo"
            :orderInfoLoading="orderInfoLoading"
        ></goods-info>

        <!-- 申请信息 -->
        <apply-info
            ref="applyInfo"
            :applyNumMax="orderInfo && parseInt(orderInfo.aftersaleQuantity)"
            :aftersalePicList="aftersalePicList"
            :applyReasonList="applyReasonList"
            :applyReasonLoading="applyReasonLoading"
            @handleRadioChange="handleRadioChange"
            @handleOrderApplyReason="handleOrderApplyReason"
            @HANDLE_RESET_PICLIST="HANDLE_RESET_PICLIST"
        ></apply-info>

        <!-- 地址信息 -->
        <shopping-address
            v-if="afterSaleType == 1"
            ref="shoppingAddress"
            :afterSaleAddress="afterSaleAddress"
            @handleVisibleModalAddressList="handleVisibleModalAddressList"
            @handleVisibleModalEditAddress="handleVisibleModalEditAddress"
        ></shopping-address>

        <!-- 联系用户信息 -->
        <concat-way ref="concatWay" :orderInfo="orderInfo"></concat-way>

        <!-- 提交申请 -->
        <div class="confirm-submit-wrap">
            <Button :loading="applySaveLoading" @click="confirmBtn.handler()">
                {{ confirmBtn.title }}
            </Button>
            <div class="hint">
                <h6 v-text="hint.title"></h6>
                <ul>
                    <li
                        v-for="(item, index) in hint.list"
                        :key="item.slice(0, 3)"
                        v-text="item"
                    ></li>
                </ul>
            </div>
        </div>

        <!-- 收获地址列表弹窗 -->
        <modal-address-list
            ref="modalAddressList"
            :addressList="addressList"
            :addressListLoading="addressListLoading"
            :memberAddressId="memberAddressId"
            @HANDEL_AFTER_ADDRESS="HANDEL_AFTER_ADDRESS"
            @HANDEL_MEMBER_ADDRESS="HANDEL_MEMBER_ADDRESS"
        ></modal-address-list>

        <!-- 新增 or 修改地址 -->
        <modal-edit-address
            ref="modalEditAddress"
            :staticData="!memberAddressId"
            @getAction="handleGetAction"
            @handleEditAfterSaleAddress="HANDEL_AFTER_ADDRESS"
        ></modal-edit-address>
        <!-- handleEditAfterSaleAddress 修改售后默认地址回调 -->
    </div>
</template>

<script>
    import goodsInfo from "@/components/06.personalCenter/06-03.afterSale/06-03-01.applyAfterSales/goodsInfo";
    import applyInfo from "@/components/06.personalCenter/06-03.afterSale/06-03-01.applyAfterSales/applyInfo";
    import shoppingAddress from "@/components/06.personalCenter/06-03.afterSale/06-03-01.applyAfterSales/shoppingAddress";
    import concatWay from "@/components/06.personalCenter/06-03.afterSale/06-03-01.applyAfterSales/concatWay";
    import modalAddressList from "@/components/06.personalCenter/06-03.afterSale/06-03-01.applyAfterSales/modalAddressList";
    import modalEditAddress from "@/components/04.shoppingCar/adressModal";

    import { mapState, mapMutations, mapActions } from "vuex";

    export default {
        name: "applyAfterSales",
        head() {
            return {
                title: "申请售后",
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
                orderGoodsId: this.$route.query.orderGoodsId,
                confirmBtn: {
                    title: "确认提交",
                    handler: this.handleConfirm,
                },
                hint: {
                    title: "温馨提示：",
                    list: [
                        "• 商品寄回地址将在审核通过后以短信形式告知，或在申请记录中查询。",
                        "• 提交服务单后，售后专员可能与您电话沟通，请保持手机畅通。",
                        "• 退货处理成功后退款金额将原路返回到您的支付账户中。",
                    ],
                },
                visibleModalAddressList: false,
                afterSaleType: "0", // 0 退货 1 换货
            };
        },
        components: {
            goodsInfo,
            applyInfo,
            shoppingAddress,
            concatWay,
            modalAddressList,
            modalEditAddress,
        },
        created() {
            this.handleAddressList();
        },
        computed: {
            ...mapState("applyAfterSales", [
                "orderInfo",
                "aftersalePicList",
                "applyReasonList",
                "addressList",
                "afterSaleAddress",
                "memberAddress",
                "memberAddressId",
                "orderInfoLoading",
                "applySaveLoading",
                "applyReasonLoading",
                "addressListLoading",
            ]),
        },
        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    if (!this.orderGoodsId) return;
                    this.handleOrderInfo(this.orderGoodsId);
                },
            },
        },
        mounted() {},
        methods: {
            ...mapMutations("applyAfterSales", [
                "HANDLE_RESET_PICLIST",
                "HANDEL_AFTER_ADDRESS",
                "HANDEL_MEMBER_ADDRESS",
            ]),

            ...mapActions("applyAfterSales", [
                "handleOrderInfo",
                "handleOrderApplyReason",
                "handleAfterSaleApplySave",
                "handleAddressList",
            ]),

            // 退货 or 换货
            handleRadioChange(type) {
                this.afterSaleType = type;
            },

            // 确认提交
            async handleConfirm() {
                // 校验申请表单信息
                const res = await this.$refs["applyInfo"].handleValidate();
                const concat = await this.$refs["concatWay"].handleConcat();

                if (!res) return;
                if (!concat) return;

                let applyInfo = {
                    ...this.$refs["goodsInfo"].formValidate, // 订单商品信息
                    ...this.$refs["applyInfo"].formValidate, // 售后申请信息
                    ...this.$refs["concatWay"].formValidate, // 用户联系方式
                };

                if (this.afterSaleType == "1") {
                    // 换货
                    applyInfo = Object.assign(applyInfo, this.afterSaleAddress); // 用户收货地址信息
                }

                // 保存成功页所需
                let title = this.handleSuccessApply(applyInfo.aftersaleType);

                // 数据保存接口
                this.handleAfterSaleApplySave({ applyInfo, title });
            },

            // 提交成功页title
            handleSuccessApply(type) {
                let title;

                switch (type) {
                    case "0":
                        title = "申请退货";
                        break;

                    case "1":
                        title = "申请换货";
                        break;
                }

                return title;
            },

            //  显示地址列表弹窗
            handleVisibleModalAddressList() {
                this.$refs["modalAddressList"].visible = true;
            },

            // 修改地址表单弹窗
            handleVisibleModalEditAddress() {
                this.$refs["modalEditAddress"].adressStatus = true;

                // 切换过收货地址, memberAddressId就有值
                // 用户收货地址 || 直接获取到的订单售后收货地址
                let afterAddress;

                if (this.memberAddressId) {
                    afterAddress = this.memberAddress;
                } else {
                    afterAddress = {
                        trueName: this.orderInfo.contacts,
                        mobPhone: this.orderInfo.contactsPhone,
                        ...this.orderInfo,
                    };
                }

                this.$refs["modalEditAddress"].adressFn(afterAddress);
            },

            // 修改地址列表中的地址回调
            handleGetAction(obj) {
                // 刷新售后订单商品收货地址信息
                this.HANDEL_AFTER_ADDRESS(obj);

                // 刷新地址列表
                this.handleAddressList();
            },
        },
    };
</script>

<style lang="scss" scoped>
    /deep/ .blockWrap {
        background: #ffffff;
        padding: 0 40px 30px;
        margin-bottom: 10px;
        h4 {
            color: #333333;
            font-weight: bold;
            height: 58px;
            line-height: 58px;
        }

        /deep/ .ivu-card-body {
            padding: 0;
        }
    }

    .confirm-submit-wrap {
        padding: 40px 80px;
        background: #ffffff;
        button {
            width: 120px;
            height: 44px;
            background: linear-gradient(
                270deg,
                rgba(240, 78, 54, 1) 0%,
                rgba(221, 38, 25, 1) 100%
            );
            border-radius: 22px;
            font-size: 15px;
            color: #ffffff;
            opacity: 1;
            margin: 0 0 40px 94px;
            transition: opacity 0.5s;
            &:hover {
                opacity: 0.7;
            }
            &:active {
                opacity: 1;
            }
        }

        .hint {
            h6 {
                font-size: 14px;
                color: #333333;
                margin-bottom: 10px;
            }
            ul {
                font-size: 13px;
                color: #666666;
            }
        }
    }
    /deep/ .card-head {
        height: 40px !important;
        line-height: 40px;
        font-size: 12px !important;
    }
    /deep/ .card-head p {
        text-align: center;
        &:nth-of-type(1) {
            text-align: center;
        }
    }
    /deep/ .g-info .g-name {
        font-size: 14px !important;
    }
    /deep/ .card-content p {
        text-align: center;
        font-size: 15px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
    }
    /deep/ .ivu-select {
        height: 34px !important;
        /deep/ .ivu-select-placeholder {
            height: 34px !important;
            line-height: 34px !important;
        }
    }
    /deep/ .ivu-form-item-label {
        height: 34px !important;
        line-height: 15px !important;
    }
    /deep/ .apply-number .hint {
        line-height: 50px !important;
    }
    /deep/ .ivu-form .ivu-form-item-label {
        font-size: 12px !important;
        font-weight: 400;
        color: #333333 !important;
        padding: 10px 27px 10px 0 !important;
    }

    /deep/ .ivu-input {
        border-radius: 2px !important;
    }
    /deep/ .concatWay input {
        height: 34px !important;
    }
    /deep/ .confirm-submit-wrap {
        padding: 40px 172px !important;
    }
    /deep/ .confirm-submit-wrap button {
        margin: 0 0 30px 0px !important;
    }
    /deep/ .confirm-submit-wrap .hint ul {
        font-size: 12px !important;
        li {
            margin-bottom: 11px;
        }
    }
    /deep/ .confirm-submit-wrap .hint h6 {
        margin-bottom: 16px !important;
    }
    /deep/ .textarea .ivu-input {
        color: #333333;
    }
</style>
