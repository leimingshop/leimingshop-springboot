<template>
    <Modal
        v-model="visible"
        :mask-closable="false"
        width="523"
        class="modal-progress-tracking"
        v-loading="progressLoading"
        @on-ok="handleConfirm"
        @on-cancel="handleCancel"
    >
        <div slot="header">
            <span class="h-title" v-text="title"></span>
        </div>

        <div class="goods-info">
            <img
                class="g-image"
                :src="progressDetails.goodsImage | filterImgUrl"
                alt=""
            />
            <div class="g-content">
                <p>
                    申请时间：<span
                        class="g-apply-time"
                        v-text="progressDetails.createDate"
                    ></span>
                </p>
                <p>
                    服务单号：<span
                        class="g-aftersaleSn"
                        v-text="progressDetails.aftersaleSn"
                    ></span>
                </p>
                <p>
                    当前状态：<span
                        class="afterSale-status"
                        v-text="currentProcessText"
                    ></span>
                </p>
            </div>
        </div>

        <div class="steps-wrap">
            <Steps :current="currentStatus" direction="vertical">
                <Step
                    v-for="(item, index) in progressDetails.logList"
                    :key="item.id + index"
                    :title="handleProcessText(item.process)"
                    :icon="currentStatus == index ? 'custom-success' : 'custom'"
                >
                    <div slot="content">
                        <div class="operator">
                            经办人：<span v-text="item.creator"></span>
                        </div>
                        <div
                            class="custom-step-desc"
                            v-text="item.message"
                        ></div>
                    </div>
                </Step>
            </Steps>
        </div>

        <div slot="footer">
            <Button type="primary" class="confirmBtn" @click="visible = false"
                >确定</Button
            >
        </div>
    </Modal>
</template>

<script>
    import { mapState, mapActions } from "vuex";

    export default {
        name: "modalProgressTracking",
        data() {
            return {
                aftersaleSn: null, // 售后单号
                title: "进度跟踪",
                visible: false,
                currentStatus: 0, // 因为是倒序，所以选中第一项
            };
        },
        components: {},
        created() {},
        computed: {
            ...mapState("afterSalesExchange", [
                "progressDetails",
                "progressLoading",
            ]),

            currentProcessText() {
                if (!this.progressDetails || !this.progressDetails.logList) return;

                let currentProcess = this.progressDetails.logList[0].process;
                return this.handleProcessText(currentProcess);
            },
        },
        watch: {
            visible: {
                immediate: false,
                handler(newVal, oldVal) {
                    if (newVal) {
                        this.handleAftersaleProgress(this.aftersaleSn);
                    }
                },
            },
        },
        mounted() {},
        methods: {
            ...mapActions("afterSalesExchange", [
                "handleAftersaleProgress",
                "handleAftersaleApplyCancel",
            ]),

            handleConfirm() {},

            handleCancel() {},

            handleProcessText(type) {
                let processText;
                switch (type) {
                    case 0:
                        processText = "提交申请";
                        break;

                    case 1:
                        processText = "审核通过";
                        break;

                    case 2:
                        processText = "用户取消";
                        break;

                    case 3:
                        processText = "售后收货";
                        break;

                    case 4:
                        processText = "换货出库";
                        break;

                    case 5:
                        processText = "进行退款";
                        break;

                    case 6:
                        processText = "处理完成";
                        break;

                    case 7:
                        processText = "审核不通过";
                        break;

                    case 8:
                        processText = "仲裁中";
                        break;

                    case 9:
                        processText = "仲裁拒绝";
                        break;

                    default:
                        status = "申请审核中";
                }

                return processText;
            },
        },
    };
</script>

<style lang="scss" scoped>
    .goods-info {
        background: #f8f8f8;
        padding: 30px;
        // .g-image{

        // }
    }
    /deep/ .ivu-modal {
        border: 20px solid rgba(0, 0, 0, 0.15);
        border-radius: 28px;
        top: 0;
    }

    /deep/ .ivu-modal-mask {
        background-color: rgba(0, 0, 0, 0.14);
    }

    /deep/ .ivu-modal-wrap {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    /deep/ .ivu-modal-header {
        border-bottom: 0;
    }

    /deep/ .ivu-steps-main {
        text-align: left;
        padding: 0 0 0 12px;
        top: -7px;
    }

    /deep/ .ivu-modal-close {
        right: 30px;
        top: 26px;
    }

    /deep/ .ivu-modal-body {
        // border: 1px solid #D2D2D2;
        margin: 0 30px;
        padding: 0;
    }

    /deep/ .ivu-modal-footer {
        padding: 30px 0 40px;
        text-align: center;
        border-top: 0;
    }

    .h-title {
        display: block;
        width: 100%;
        height: 16px;
        line-height: 16px;
        font-size: 16px;
        text-align: center;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: bold;
        color: #222222;
    }

    .goods-info {
        display: flex;
        // border-bottom: 1px solid #D2D2D2;
        margin-bottom: -1px;
        .g-image {
            width: 68px;
            height: 68px;
            display: block;
            margin-right: 20px;
            object-fit: scale-down;
            border: 1px solid #eeeeee;
        }

        .g-content {
            font-size: 12px;
            color: #333333;
        }
    }

    .steps-wrap {
        margin: 22px 30px 20px;
    }

    .ivu-steps-item {
        /deep/ .ivu-icon-custom {
            width: 10px !important;
            height: 10px !important;
            background: url("/img/06.personalCenter/circlegray.png") center/cover;
            position: absolute;
            z-index: 2;
        }

        /deep/ .ivu-icon-custom-success {
            width: 10px !important;
            height: 10px !important;
            background: url("/img/06.personalCenter/circlered1.png") center/cover;
            position: absolute;
            z-index: 2;
        }

        /deep/ .ivu-steps-tail {
            left: 9px;
            top: 10px;
            z-index: 1;
        }

        /deep/ .ivu-steps-title {
            margin: 2px 0 5px 0;
            color: #222222;
            font-size: 12px !important;
        }

        /deep/ .operator {
            font-size: 12px;
            color: #666666;
            margin: 0 0 8px 0;
        }

        /deep/ .custom-step-desc {
            font-size: 12px;
            color: #999999;
        }

        /deep/ .ivu-steps-tail {
            padding: 0;
            & > i:after {
                background: #cccccc;
                position: absolute;
            }
        }

        /deep/ .ivu-steps-head {
            width: 10px;
            height: 10px;
        }
    }

    .confirmBtn {
        width: 60px;
        height: 30px;
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
        border-radius: 2px;
        font-size: 14px;
        margin: 0 auto;
        font-family: PingFangSC-Regular, PingFang SC;
        transition: opacity 0.3s;
        &:hover {
            opacity: 0.8;
        }
    }

    /deep/ .ivu-modal-header {
        padding: 30px 0 !important;
    }

    /deep/ .goods-info {
        padding: 20px 30px !important;
    }
    /deep/ .ivu-modal-close {
        top: 20px !important;
        right: 12px !important;
    }
    /deep/ .ivu-modal {
        border: 10px solid rgba(0, 0, 0, 0.15);
        border-radius: 10px;
    }
</style>
