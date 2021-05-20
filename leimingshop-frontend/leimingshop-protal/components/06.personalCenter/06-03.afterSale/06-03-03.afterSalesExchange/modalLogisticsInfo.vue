<template>
    <Modal
        v-model="visible"
        :mask-closable="false"
        width="369"
        :class="['modal-logistics-info', { view: type == 'view' }]"
    >
        <div slot="header">
            <span class="h-title" v-text="title"></span>
        </div>

        <Form
            class="content"
            ref="formValidate"
            :model="formValidate"
            :rules="ruleValidate"
            :disabled="type == 'view'"
            label-position="top"
        >
            <FormItem label="物流商：" prop="logisticsCompany">
                <Input
                    v-model="formValidate.logisticsCompany"
                    placeholder="请填写物流商"
                    size="large"
                ></Input>
            </FormItem>

            <FormItem label="物流单号：" prop="logisticsNumber">
                <Input
                    v-model="formValidate.logisticsNumber"
                    placeholder="请填写物流单号"
                    size="large"
                ></Input>
            </FormItem>

            <FormItem label="手机号码：" prop="logisticsContactsPhone">
                <Input
                    v-model="formValidate.logisticsContactsPhone"
                    placeholder="请填写手机号"
                    size="large"
                ></Input>
            </FormItem>
        </Form>

        <div slot="footer">
            <Button type="text" class="confirmBtn" @click="handleCancel"
                >取消</Button
            >

            <Button
                type="primary"
                class="confirmBtn"
                :loading="saveLogisticsLoading"
                @click="handleConfirm"
                >确定</Button
            >
        </div>
    </Modal>
</template>

<script>
    import { mapState, mapActions } from "vuex";
    import { isMobile } from "@/utils/validate";
    export default {
        name: "modalLogisticsInfo",

        props: {
            aftersaleSn: {
                type: String | Number,
                default: "",
            },

            aftersaleType: {
                type: String | Number,
                default: "",
            },
        },

        data() {
            return {
                visible: false,
                type: "save", // save 表单为编辑状态， view 表单为预览状态
                formValidate: {
                    logisticsCompany: "",
                    logisticsNumber: "",
                    logisticsContactsPhone: "",
                },
                ruleValidate: {
                    logisticsCompany: [
                        {
                            required: true,
                            message: "请填写物流商",
                            trigger: "blur",
                        },
                    ],
                    logisticsNumber: [
                        {
                            required: true,
                            message: "请填写物流单号",
                            trigger: "blur",
                        },
                    ],
                    logisticsContactsPhone: [
                        {
                            required: true,
                            message: "请填写手机号",
                            trigger: "blur",
                        },
                        {
                            validator: (rule, value, callback) => {
                                if (!isMobile(value))
                                    callback(new Error("手机号格式错误"));
                                else callback();
                            },
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        components: {},
        created() {},
        computed: {
            ...mapState("afterSalesExchange", [
                "saveLogisticsLoading", //上传中
            ]),

            title() {
                let text = this.type == "save" ? "上传" : "";
                return `${text}物流信息`;
            },
        },
        watch: {},
        mounted() {},
        methods: {
            ...mapActions("afterSalesExchange", ["handleSellerDeliverySave"]),

            // 校验表单
            async handleConfirm() {
                if (this.type == "view") {
                    return (this.visible = false);
                }

                const res = await this.$refs["formValidate"].validate();

                if (!res) {
                    setTimeout(() => {
                        var isError = document.getElementsByClassName(
                            "ivu-form-item-error"
                        );
                        if (isError[0].querySelector("input")) {
                            isError[0].querySelector("input").focus();
                        }
                    }, 1);
                    this.$Message.warning("请完善物流信息");
                    return false;
                }

                let params = {
                    aftersaleSn: this.aftersaleSn,
                    aftersaleType: this.aftersaleType,
                    ...this.formValidate,
                };
                const response = await this.handleSellerDeliverySave(params);
                if (response) this.visible = false;
            },

            handleCancel() {
                this.visible = false;
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/modal.scss";

    $primary-color: #dd2619;

    /deep/ .ivu-modal-body .content {
        margin: 0 25px;
    }
    /deep/ .ivu-modal-body {
        height: 150px !important;
    }

    /deep/ .ivu-modal-footer {
        // height: 0px;
        // line-height: 90px;
        border-top: 0;
        text-align: center;
        button {
            width: 60px;
            height: 30px;
            font-size: 14px;
            border-radius: 2px;
            margin-left: 0;
            &:nth-of-type(1) {
                background: #f5f5f5;
                color: #333333;
                margin-right: 38px;
                transition: background 0.5s;
                &:hover {
                    background: #cccccc;
                }
            }
        }
    }

    .view {
        /deep/ .ivu-modal-footer {
            .ivu-btn-text {
                display: none;
            }
        }
    }
    /deep/ .ivu-modal {
        height: 317px !important;
    }
    /deep/ .ivu-form-item {
        display: flex;
    }
    /deep/ .h-title {
        font-size: 16px !important;
        height: 16px !important;
        line-height: 16px !important;
        // padding: 30px 0 20px !important;
    }
    /deep/ .ivu-form-item-content {
        width: 230px;
        height: 34px;
    }
    /deep/ .ivu-input-large {
        height: 34px !important;
        font-size: 12px !important;
    }
    /deep/ .ivu-form-label-top .ivu-form-item-label {
        padding: 0px !important;
    }
    /deep/ .ivu-form .ivu-form-item-label {
        line-height: 34px !important;
        width: 85px !important;
    }
    /deep/ .ivu-modal-footer {
        line-height: 30px !important;
        padding: 30px 0 40px !important;
    }
    /deep/.ivu-modal-close {
        right: 15px !important;
        top: 10px !important;
    }
    /deep/ .ivu-modal-header {
        padding: 30px 0 20px 0px !important;
    }
    /deep/ .ivu-modal-footer button {
        &:nth-of-type(1) {
            margin-right: 26px !important;
        }
    }
    /deep/ .ivu-input {
        border-radius: 1px !important;
    }
</style>
