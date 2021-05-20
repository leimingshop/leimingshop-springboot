<template>
    <div>
        <!-- 已完成 -->
        <div class="orderdetail-head" v-loading="detailsLoading">
            <div class="header-left">
                <Card :bordered="false">
                    <div class="contact">
                        <div class="status">
                            <img
                                v-if="aftersaleApplyDetail.auditResult == 2"
                                src="/img/06.personalCenter/06-03.afterSale/error.png"
                                alt=""
                            />

                            <img
                                v-else
                                src="/img/06.personalCenter/06-03.afterSale/apply-success.png"
                                alt=""
                            />

                            <p v-text="statusText"></p>
                        </div>
                        <div class="head-main2">
                            <div>
                                <span
                                    >服务单号：{{
                                        aftersaleApplyDetail.aftersaleSn
                                    }}</span
                                >
                                <span
                                    >申请时间：{{
                                        aftersaleApplyDetail.createDate
                                    }}</span
                                >
                            </div>
                        </div>
                        <!-- <div class="head-right">
                            <div class="button">
                                <img src="@static/img/03.goodsClass/03-03.goodsDetail/server-message.png" alt="">
                                <span>联系客服</span>
                            </div>
                        </div> -->
                    </div>

                    <!-- <div class="split-line"></div> -->
                    <div style="padding: 0px 40px 30px">
                        <div class="myApply">
                            <aftersale-bar-apply
                                :aftersaleApplyDetail="aftersaleApplyDetail"
                                @handleStatusText="handleStatusText"
                            ></aftersale-bar-apply>
                        </div>
                        <div class="progress-tracking">
                            <div class="refund">
                                <span
                                    v-if="
                                        lastLogTime !== null &&
                                        lastLogTime !== ''
                                    "
                                    v-text="lastLogTime"
                                    style="margin-right: 15px"
                                ></span>
                                <span
                                    v-text="aftersaleApplyDetail.lastLog"
                                ></span>
                            </div>

                            <div class="button-group">
                                <!--  auditStatus 1:商家审核中,2:平台审核中,3:已完成,4:已取消 -->
                                <Button
                                    type="default"
                                    class="progress-view-btn"
                                    @click="handleModalVisible"
                                    >进度跟踪</Button
                                >
                                <img
                                    src="/img/06.personalCenter/twins.png"
                                    @click="handleModalVisible"
                                    alt
                                />

                                <Button
                                    v-if="
                                        aftersaleApplyDetail.auditStatus == 1 ||
                                        aftersaleApplyDetail.auditStatus == 2
                                    "
                                    type="default"
                                    class="cancel-apply-btn"
                                    @click="handleCancelApply"
                                    >取消申请</Button
                                >
                                <!-- 售后物流状态（1为买家待发货,2为卖家待收货,3卖家已收货,4为买家待收货,5为已完成） -->

                                <div
                                    class="logistics-btn-group"
                                    v-if="aftersaleApplyDetail.auditStatus == 3"
                                >
                                    <Button
                                        v-if="
                                            aftersaleApplyDetail.logisticsStatus ==
                                            1
                                        "
                                        type="default"
                                        @click="handleLogistics('save')"
                                        >上传物流信息</Button
                                    ><!-- logisticsStatus 1:买家待发货 -->

                                    <Button
                                        v-if="
                                            aftersaleApplyDetail.logisticsStatus ==
                                                2 ||
                                            aftersaleApplyDetail.logisticsStatus ==
                                                3
                                        "
                                        type="default"
                                        @click="handleLogistics('view')"
                                        >查看物流信息</Button
                                    >

                                    <Button
                                        v-if="
                                            aftersaleApplyDetail.logisticsStatus ==
                                            4
                                        "
                                        type="default"
                                        :loading="confirmReceiptLoading"
                                        @click="handleConfirmReceipt()"
                                        >确认收货</Button
                                    >
                                </div>
                            </div>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
        <!-- 服务单信息 -->
        <div class="serviceList" v-loading="detailsLoading">
            <div class="message">服务单信息</div>
            <!-- <i-table stripe :columns="columns1" :data="data1"></i-table> -->
            <div class="table">
                <ul>
                    <li>
                        <span class="table-left">服务单号</span>
                        <span
                            class="table-right"
                            v-text="aftersaleApplyDetail.aftersaleSn"
                        ></span>
                    </li>
                    <li>
                        <span class="table-left">申请时间</span>
                        <span
                            class="table-right"
                            v-text="aftersaleApplyDetail.createDate"
                        ></span>
                    </li>
                    <li>
                        <span class="table-left">服务类型</span>
                        <span
                            class="table-right exchange"
                            v-text="aftersaleType"
                        ></span>
                    </li>
                    <li>
                        <span class="table-left">{{ aftersaleType }}原因</span>
                        <span
                            class="table-right"
                            v-text="aftersaleApplyDetail.aftersaleReason"
                        ></span>
                    </li>
                    <li>
                        <span class="table-left">提交数量</span>
                        <span
                            class="table-right"
                            v-text="aftersaleApplyDetail.goodsNum"
                        ></span>
                    </li>
                    <li>
                        <span class="table-left">联系人</span>
                        <span
                            class="table-right"
                            v-text="aftersaleApplyDetail.contacts"
                        ></span>
                    </li>
                    <li>
                        <span class="table-left">联系电话</span>
                        <span
                            class="table-right"
                            v-text="aftersaleApplyDetail.contactsPhone"
                        ></span>
                    </li>
                </ul>
            </div>
        </div>

        <!-- 收获地址、问题描述、提交凭证等 -->
        <div class="orderdetail-footer" v-loading="detailsLoading">
            <!-- 收货地址 -->
            <div
                class="receiptAddress"
                v-if="aftersaleApplyDetail.aftersaleType == '1'"
            >
                <div class="message">收货地址</div>
                <div class="receipt">
                    <ul>
                        <li>
                            <span>收货人：</span>
                            <span v-text="aftersaleApplyDetail.receiver"></span>
                        </li>
                        <li>
                            <span>联系方式：</span>
                            <span
                                v-text="aftersaleApplyDetail.receiverPhone"
                            ></span>
                        </li>
                        <li>
                            <span>收货地址：</span>
                            <span
                                v-text="aftersaleApplyDetail.receiverAddress"
                            ></span>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- 问题描述 -->
            <div
                class="problemDescription"
                v-if="aftersaleApplyDetail.aftersaleExplain"
                style="margin-bottom: 0px"
            >
                <div class="message">问题描述</div>
                <div
                    class="problem"
                    v-text="aftersaleApplyDetail.aftersaleExplain"
                ></div>
            </div>
            <!-- 提交凭证 -->
            <div class="submitVoucher">
                <div class="message">提交凭证</div>
                <div class="voucherImages">
                    <ul v-viewer>
                        <li
                            v-for="(item, index) in aftersaleApplyPics"
                            :key="item + index"
                            v-lazy-container="{
                                selector: 'img',
                                error: '/img/common/loading/200_200.png',
                                loading: '/img/common/loading/200_200.png',
                            }"
                        >
                            <img :data-src="item" alt="" />
                        </li>
                    </ul>
                </div>
            </div>
            <!-- 商家审核结果 -->
            <div class="auditMessage">
                <div class="message">商家审核结果</div>
                <div class="auditText">
                    <!-- <div v-text="aftersaleApplyDetail.sellerAudit"></div> -->
                    <div class="business">
                        <span class="bus">审核结果：</span>
                        <span v-if="busMessage !== ''">
                            <span v-text="busMessage"></span>
                        </span>
                        <span v-else-if="busMessage == ''">
                            <span class="bus">暂无</span>
                        </span>
                    </div>

                    <!-- <div class="business">
                        <span class="bus">备注：{{(busRemarks !== '')?busRemarks:'暂无'}}</span>
                        <span class="bus">备注：</span>
                        <span class="bus" v-if="busRemarks !== ''"  v-text="busRemarks"></span>
                        <span class="bus" v-else-if="busRemarks == '' || busRemarks == null ">暂无</span>
                    </div> -->

                    <div class="business">
                        <span class="remarks">备注： </span>
                        <!-- <span class="findingAudit" v-text="busRemarks"></span> -->
                        <span class="explain">{{
                            busRemarks !== "" ? busRemarks : "暂无"
                        }}</span>
                    </div>
                </div>
            </div>
            <!-- 平台审核结果 -->
            <div class="examineResult" v-if="aftersaleApplyDetail.adminAudit">
                <div class="message">平台审核结果</div>
                <div
                    class="result success"
                    v-if="aftersaleApplyDetail.auditResult == 1"
                >
                    <!-- {{aftersaleApplyDetail.adminAudit}} -->
                    <div class="platform">
                        <span class="findingAudit">审核结果：</span>
                        <span
                            class="platform-content"
                            v-text="platformMessage"
                        ></span>
                    </div>
                    <div class="platform">
                        <span class="remarks">备注： </span>
                        <span
                            class="findingAudit"
                            v-text="platformRemarks"
                        ></span>
                    </div>
                </div>

                <div
                    class="result error"
                    v-if="aftersaleApplyDetail.auditResult == 2"
                >
                    <div class="platform">
                        <span class="findingAudit">审核结果：</span>
                        <span class="fail" v-text="platformMessage"></span>
                    </div>
                    <div class="platform">
                        <span class="remarks">备注： </span>
                        <span
                            class="findingAudit"
                            v-text="platformRemarks"
                        ></span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 进度跟踪 -->
        <modal-progress-tracking
            ref="modalProgressTracking"
        ></modal-progress-tracking>

        <!-- 物流信息 -->
        <modal-logistics-info
            ref="modalLogisticsInfo"
            :aftersaleSn="aftersaleApplyDetail.aftersaleSn"
            :aftersaleType="aftersaleApplyDetail.aftersaleType"
        ></modal-logistics-info>

        <!-- 取消申请 -->
        <modal-cancel-apply ref="modalCancelApply"></modal-cancel-apply>

        <!-- 确认收货 -->
        <model-confirm-receipt
            ref="modelConfirmReceipt"
        ></model-confirm-receipt>
    </div>
</template>

<script>
    import { mapState, mapMutations, mapActions } from "vuex";
    import aftersaleBarApply from "@/components/06.personalCenter/06-03.afterSale/06-03-03.afterSalesExchange/aftersaleBarApply"; //详情页步骤条部分
    import modalProgressTracking from "@/components/06.personalCenter/06-03.afterSale/modalProgressTracking";
    import modalCancelApply from "@/components/06.personalCenter/06-03.afterSale/06-03-03.afterSalesExchange/modalCancelApply";
    import modelConfirmReceipt from "@/components/06.personalCenter/06-03.afterSale/06-03-03.afterSalesExchange/modelConfirmReceipt";
    import modalLogisticsInfo from "@/components/06.personalCenter/06-03.afterSale/06-03-03.afterSalesExchange/modalLogisticsInfo";

    export default {
        components: {
            aftersaleBarApply,
            modalProgressTracking,
            modalCancelApply,
            modelConfirmReceipt,
            modalLogisticsInfo,
        },

        head() {
            return {
                title: "服务单换货、退货",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        props: {},

        data() {
            return {
                statusText: "", // 审核状态 已通过的文本
                busMessage: "", // 商家留言
                busRemarks: "", // 商家备注
                platformMessage: "", // 平台留言
                platformRemarks: "", // 平台备注
            };
        },

        computed: {
            ...mapState("afterSalesExchange", [
                "aftersaleApplyDetail",
                "detailsLoading",
            ]),

            aftersaleType() {
                let temp;

                switch (this.aftersaleApplyDetail.aftersaleType) {
                    case 0:
                        temp = "退货";
                        break;

                    case 1:
                        temp = "换货";
                        break;
                }

                return temp;
            },

            lastLogTime() {
                let tempList = this.aftersaleApplyDetail.detailDTOList;
                if (tempList) return tempList[tempList.length - 1].createDate;
            },

            aftersaleApplyPics() {
                let tempImgList = this.aftersaleApplyDetail.aftersalePics;
                if (!tempImgList) return;

                tempImgList = tempImgList.split(",");
                return tempImgList.map((item, index) => {
                    return `${this.$imgURL}${item}`;
                });
            },
        },

        created() {
            let params = {
                aftersaleId: this.$route.query.aftersaleId,
                aftersaleType: this.$route.query.aftersaleType,
            };

            this.handleAftersaleApplyDetail(params);
        },

        updated() {
            // 获取商家当前内容
            var business = this.aftersaleApplyDetail.sellerAudit;
            if (!business) return;
            // 取出第一个冒号出现的下标
            var colonBefore = business.indexOf("：");
            // 截取第一个冒号之前的内容
            var busMessage = business.substring(0, colonBefore);
            // 截取第一个冒号之后的内容
            var busRemarks = business.substring(colonBefore + 1);
            this.busMessage = busMessage;
            this.busRemarks = busRemarks;

            //获取平台当前内容
            var platform = this.aftersaleApplyDetail.adminAudit;
            if (platform) {
                var colonAfter = platform.indexOf("：");
                var platformMessage = platform.substring(0, colonAfter);
                var platformRemarks = platform.substring(colonAfter + 1);
                this.platformMessage = platformMessage;
                this.platformRemarks = platformRemarks;
            }
        },

        methods: {
            ...mapActions("afterSalesExchange", ["handleAftersaleApplyDetail"]),

            // 显示进度跟踪弹窗
            handleModalVisible() {
                this.$refs["modalProgressTracking"].visible = true;
                this.$refs[
                    "modalProgressTracking"
                ].aftersaleSn = this.aftersaleApplyDetail.aftersaleSn;
            },

            // 取消申请
            handleCancelApply() {
                this.$refs["modalCancelApply"].visible = true;
            },

            // 物流信息
            async handleLogistics(type) {
                this.$refs["modalLogisticsInfo"].visible = true;
                this.$refs["modalLogisticsInfo"].type = type;

                await this.$nextTick();

                if (type == "view") {
                    this.$refs["modalLogisticsInfo"].formValidate = {
                        logisticsCompany: this.aftersaleApplyDetail
                            .logisticsCompany,
                        logisticsNumber: this.aftersaleApplyDetail.logisticsNumber,
                        logisticsContactsPhone: this.aftersaleApplyDetail
                            .logisticsContactsPhone,
                    };
                }
            },

            // 确认收货
            handleConfirmReceipt() {
                this.$refs["modelConfirmReceipt"].visible = true;
            },

            // 售后审核状态文字
            handleStatusText(text) {
                this.statusText = text;
            },
        },
    };
</script>

<style lang="scss" scoped>
    /* 公共样式 */
    .message {
        height: 54px;
        line-height: 54px;
        font-size: 14px;
        font-weight: 600;
        color: rgba(51, 51, 51, 1);
        font-family: PingFangSC-Regular, PingFang SC;
    }

    /* 头部样式 */
    .orderdetail-head {
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .header-left {
            width: 100%;
        }
    }

    .ivu-card-body {
        padding: 30px;
    }

    .contact {
        display: flex;
        align-items: center;
        justify-content: left;
        width: 100%;
        // height: 64px;
        margin-bottom: 30px;
        padding: 30px 50px;
        box-shadow: 0px 2px 6px 0px rgba(0, 0, 0, 0.1);
        .status {
            width: 60px;
            text-align: center;

            img {
                width: 36px;
            }

            p {
                font-size: 14px;
                color: rgba(66, 66, 66, 1);
            }
        }

        .head-main2 {
            margin-left: 71px;

            span {
                font-size: 14px;
                color: rgba(102, 102, 102, 1);
                margin-right: 15px;
            }
        }

        .head-right {
            .button {
                width: 120px;
                height: 44px;
                line-height: 44px;
                background: linear-gradient(
                    90deg,
                    rgba(222, 42, 28, 1) 0%,
                    rgba(255, 78, 3, 1) 100%
                );
                border-radius: 22px;
                text-align: center;

                img {
                    vertical-align: text-top;
                    background: rgba(255, 255, 255, 1);
                    border-radius: 8px;
                }

                span {
                    font-size: 15px;
                    color: rgba(255, 255, 255, 1);
                }
            }
        }
    }

    .split-line {
        height: 1px;
        width: 100%;
        background: #cccccc;
        position: absolute;
        left: 0;
    }

    .myApply {
        padding-top: 10px;
        /deep/ .steps-box {
            z-index: 1;
        }
    }

    .progress-tracking {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 42px;
        line-height: 42px;
        background: #ff9c9519;
        margin-top: 12px;
        // margin-bottom: 20px;
        padding-right: 38px;

        .refund {
            margin-left: 30px;

            span {
                font-size: 12px;
                color: rgba(51, 51, 51, 1);
                // margin-right: 15px;
            }
        }

        .button-group {
            display: flex;
            justify-content: center;
            align-items: center;

            button {
                padding: 0 10px;
                height: 26px;
                border-radius: 13px;
                font-size: 12px;
                color: #e72900ff;
                border: none !important;
                transition: opacity 0.3s;
                background: #fff5f5;
                // &:hover {
                //     opacity: .6;
                // }
            }

            .progress-view-btn {
                // margin-right: 20px;
            }

            .logistics-btn-group {
                display: flex;
                justify-content: center;
                align-items: center;
            }
        }
    }

    /*服务单信息*/
    .serviceList {
        margin-top: 10px;
        width: 100%;
        background: rgba(255, 255, 255, 1);
        border-radius: 0px;
        padding: 0 40px;
        padding-bottom: 32px;

        .table {
            width: 100%;
            border-radius: 5px;
            box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.16);

            ul {
                li {
                    height: 44px;
                    line-height: 44px;
                    border-top: 1px solid rgba(245, 245, 245, 1);

                    .table-left {
                        opacity: 0.8;
                        text-align: center;
                        display: inline-block;
                        width: 120px;
                        font-size: 12px;
                        color: #222222;
                        font-weight: 400;
                        background: rgba(248, 248, 248, 1);
                    }

                    .table-right {
                        display: inline-block;
                        width: 698px;
                        box-sizing: border-box;
                        padding-left: 38px;
                        font-size: 12px;
                        color: #333333;
                        font-weight: 400;
                    }

                    .exchange {
                        font-size: 12px;
                        font-weight: 500;
                    }
                }

                li:first-child {
                    border-top: 0;
                }
            }
        }
    }

    /*收获地址、问题描述、提交凭证等样式*/
    .orderdetail-footer {
        margin-top: 10px;
        padding: 0 40px;
        background: rgba(255, 255, 255, 1);
        padding-bottom: 40px;

        /*收货地址*/
        .receiptAddress {
            margin-bottom: 20px;

            .receipt {
                border: 1px solid rgba(204, 204, 204, 1);
                padding: 0 30px;
                width: 100%;
                margin-bottom: 20px;

                ul {
                    li {
                        // height: 44px;
                        line-height: 44px;
                        font-size: 14px;
                        font-weight: 400;
                        color: rgba(51, 51, 51, 1);

                        span:first-child {
                            margin-right: 15px;
                        }
                    }

                    li:first-child {
                        span:first-child {
                            display: inline-block;
                            width: 69px;
                        }
                    }
                }
            }
        }

        /*问题描述*/
        .problemDescription {
            margin-bottom: 15px;

            .problem {
                word-break: break-all;
                border: 1px solid rgba(204, 204, 204, 1);
                padding: 16px 21px;
                font-size: 12px;
                font-weight: 400;
                color: #222222;
                line-height: 30px;
            }
        }

        /*提交凭证*/
        .submitVoucher {
            margin-bottom: 10px;

            .voucherImages {
                ul {
                    width: 100%;
                    word-wrap: break-word;
                    word-break: normal;

                    li {
                        display: inline-block;
                        width: 90px;
                        height: 90px;
                        margin-right: 10px;
                        margin-bottom: 10px;
                        cursor: pointer;

                        img {
                            width: 100%;
                            height: 100%;
                            border: 1px solid #efefef;
                            transition: border 0.3s;
                            object-fit: scale-down;

                            &:hover {
                                border: 1px solid #dd2619;
                            }
                        }
                    }
                }
            }
        }

        /*商家审核结果*/
        .auditMessage {
            margin-bottom: 20px;

            .auditText {
                border: 1px solid rgba(204, 204, 204, 1);
                padding: 20px;
                font-size: 12px;
                font-weight: 400;
                color: rgba(20, 177, 26, 1);
                line-height: 24px;
                .business {
                    width: 100%;
                    font-size: 12px;
                    font-weight: 400;
                    font-family: PingFangSC-Regular, PingFang SC;
                    line-height: 30px;
                    word-wrap: break-word;
                    .bus {
                        color: rgba(51, 51, 51, 1);
                    }

                    .remarks {
                        float: left;
                        color: rgba(51, 51, 51, 1);
                    }
                    .explain {
                        margin-left: 5px;
                        color: rgba(51, 51, 51, 1);
                    }

                    span {
                        line-height: 28px;
                    }
                }
            }
        }

        /* 平台审核结果*/
        .examineResult {
            .result {
                word-break: break-all;
                border: 1px solid rgba(204, 204, 204, 1);
                padding: 19px 18px 22px 21px;
                font-size: 16px;
                color: rgba(20, 177, 26, 1);
                font-weight: 400;
                .platform {
                    width: 100%;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    line-height: 30px;
                    word-wrap: break-word;
                    .findingAudit {
                        color: rgba(51, 51, 51, 1);
                    }
                    .platform-content {
                        color: rgba(20, 177, 26, 1);
                    }
                    .remarks {
                        color: rgba(51, 51, 51, 1);
                    }
                    .fail {
                        color: #dd2619;
                    }
                    span {
                        line-height: 28px;
                    }
                }

                &.error {
                    color: #dd2619;
                }
            }
        }
    }
    /deep/ .steps-box .Article-steps .step .title {
        font-size: 14px !important;
    }
    /deep/ .steps-box .line {
        background: #f0f0f0 !important;
        width: 86% !important;
        left: 62px !important;
    }
    /deep/ .progress-tracking .button-group button {
        padding: 0px 4px !important;
    }
    /deep/ .cancel-apply-btn {
        margin-left: 15px;
    }
    /deep/ .overall {
        padding: 0px !important;
    }
    /deep/ .ivu-card-body {
        padding: 0px !important;
    }
    /deep/ .ivu-modal-close .ivu-icon-ios-close {
        &:hover {
            color: #dd2619;
        }
    }
    /deep/ .ivu-modal {
        border: 6px solid rgba(0, 0, 0, 0.15) !important;
    }
</style>
