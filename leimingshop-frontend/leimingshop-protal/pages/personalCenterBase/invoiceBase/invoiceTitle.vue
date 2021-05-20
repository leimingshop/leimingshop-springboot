<template>
    <div class="invoice">
        <div v-if="dataForm.length == 0" class="nullInvoice">
            <div class="invoiceCenter-add">
                <p class="content">(发票抬头最多5条，还能添加{{ count }}条)</p>
                <p
                    @click="addInvoice"
                    class="addInvoice"
                    v-if="dataForm.length < 5"
                >
                    ＋添加抬头
                </p>
            </div>
            <div>
                <img
                    src="/img/06.personalCenter/invoice@2x.png"
                    alt=""
                    style="width: 230px; height: 170px; margin: 125px 310px 0"
                />
            </div>
            <p class="nullInvoice-p">暂未添加发票抬头</p>
        </div>
        <modal
            v-model="modal"
            title="添加发票抬头"
            width="618px"
            :footer-hide="true"
            @on-ok="submit"
            :loading="loading"
            @on-cancel="cancel"
        >
            <div>
                 <Form
                    ref="dataList"
                    :model="dataList"
                    :label-colon="false"
                    :rules="invoiceRules"
                >
                    <FormItem label="单位名称" prop="company" class="modal-p">
                        <Input
                            v-model="dataList.company"
                            clearable
                            maxlength="40"
                            placeholder="请输入单位名称"
                        />
                    </FormItem>
                    <FormItem label="税号" prop="dutyParagraph">
                        <Input
                            v-model="dataList.dutyParagraph"
                            clearable
                            maxlength="20"
                            placeholder="请输入公司税号"
                        />
                    </FormItem>
                    <FormItem label="注册地址" prop="registeredAddress">
                        <Input
                            v-model="dataList.registeredAddress"
                            clearable
                            maxlength="40"
                            placeholder="请输入注册地址"
                        />
                    </FormItem>
                    <FormItem label="办公电话" prop="officePhone">
                        <Input
                            v-model="dataList.officePhone"
                            clearable
                            maxlength="11"
                            placeholder="请输入办公电话"
                        />
                    </FormItem>
                    <FormItem label="开户银行" prop="bankName">
                        <Input
                            v-model="dataList.bankName"
                            clearable
                            maxlength="20"
                            placeholder="请输入开户银行"
                        />
                    </FormItem>
                    <FormItem label="开户银行账号" prop="bankNumber">
                        <Input
                            v-model="dataList.bankNumber"
                            clearable
                            maxlength="20"
                            placeholder="请输入银行账号"
                            prop="bankNumber"
                        />
                    </FormItem>
                </Form>
            </div>
            <div class="modal-btn">
                <Button class="cancel" @click="cancel">取消</Button>
                <Button class="confirm" @click="submit">保存</Button>
            </div>
        </modal>
        <div class="invoiceCenter" v-if="dataForm.length > 0">
            <div class="invoiceCenter-add">
                <p class="content">
                    (发票抬头最多<span
                        style="
                            color: #dd2619;
                            font-size: 16px;
                            font-weight: 400;
                        "
                        >5</span
                    >条，还能添加{{ count }}条)
                </p>
                <p
                    @click="addInvoice"
                    class="addInvoice"
                    v-if="dataForm.length < 5"
                >
                    ＋添加抬头
                </p>
            </div>

            <div
                class="invoiceList"
                v-for="(item, index) in dataForm"
                :key="index"
            >
                <div class="invoiceList-one">
                    <div>
                        <span>单位名称：</span>
                        <span
                            style="
                                font-size: 14px;
                                font-weight: 400;
                                color: rgba(51, 51, 51, 1);
                            "
                            >{{ item.company }}</span
                        >
                    </div>
                    <Button v-if="item.defaultFlag == 1" class="invoiceList-btn"
                        >默认抬头</Button
                    >
                    <Button
                        v-if="item.defaultFlag == 0"
                        class="invoiceList-btn1"
                        @click="changeBtn(item.id)"
                        >设为默认</Button
                    >
                </div>
                <div class="invoiceList-two">
                    <div>
                        <span>公司税号：</span>
                        <span
                            style="
                                font-size: 14px;
                                font-weight: 400;
                                color: rgba(51, 51, 51, 1);
                            "
                            >{{ item.dutyParagraph }}</span
                        >
                    </div>
                    <div style="display: flex">
                        <p
                            class="invoiceList-twoWord"
                            @click="changeInvoice(item.id)"
                        >
                            编辑
                        </p>
                        <p
                            class="invoiceList-twoWord"
                            @click="handleConfirmBefore(item.id)"
                        >
                            删除
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <second-confirm
            v-show="modal1"
            :promptTitle="promptTitle"
            @handleConfirmEnd="handleConfirmEnd"
            @handleConfirm="deleteInvoice"
        ></second-confirm>
    </div>
</template>
<script>
    import { isNumWord } from "@/utils/validate";
    import secondConfirm from "@/components/06.personalCenter/06-07.myOrders/secondConfirm";
    import {
        memberInvoiceList,
        getInvoice,
        deleteInvoice,
        memberInvoiceDetail,
        memberInvoiceDefault,
        settingDefault,
    } from "@/api/api_06-13-06invoiceTitle.js";
    export default {
        components: {
            secondConfirm,
        },
        head() {
            return {
                title: "发票抬头",
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
            const validatePhone = (rule, value, callback) => {
                if (!/^1[34578]\d{9}$/.test(value)) {
                    callback("手机格式不正确");
                } else {
                    callback();
                }
            };
            const validatedutyParagraph = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error("税号不能为空"));
                } else if (
                    !/^[A-Z0-9]{15}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$/.test(value)
                ) {
                    callback("税号格式不正确");
                } else {
                    callback();
                }
            };
            return {
                promptTitle: "是否删除该发票抬头",
                modal: false,
                modal1: false,
                loading: false,
                validatePhone: "",
                validatedutyParagraph: "",
                company: "",
                id: "",
                modal1: false,
                dataList: {
                    company: "",
                    dutyParagraph: "",
                    registeredAddress: "",
                    officePhone: "",
                    bankName: "",
                    bankNumber: "",
                },
                count: 0,
                delId: "",
                dataForm: {
                    company: "",
                    dutyParagraph: "",
                    defaultFlag: "",
                },
                invoiceRules: {
                    company: [
                        {
                            required: true,
                            message: "单位名称不能为空",
                            trigger: "blur",
                        },
                    ],
                    dutyParagraph: [
                        {
                            required: true,
                            message: "单位税号不能为空",
                            trigger: "blur",
                            validator: (rule, value, callback) => {
                                let reg = /^[A-Z0-9]{15}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$/;
                                if (!reg.test(value)) {
                                    callback(new Error("税号格式错误"));
                                } else {
                                    callback();
                                }
                            },
                        },
                    ],
                    bankNumber: [
                        {
                            validator: (rule, value, callback) => {
                                let reg = /[0-9]{16,19}/;
                                if (!value) {
                                    callback();
                                } else if (!reg.test(value)) {
                                    callback(new Error("银行卡号格式错误"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        created() {
            this.getinvoice();
        },
        methods: {
            handleConfirmBefore(delId) {
                this.delId = delId;
                this.modal1 = true;
            },
            handleConfirmEnd() {
                this.modal1 = false;
            },
            cancel() {
                this.modal = false;
            },
            //设置默认发票
            changeBtn(id) {
                if (this.defaultFlag) {
                    this.defaultFlag = false;
                } else {
                    this.defaultFlag = true;
                }
                settingDefault({
                    invoiceId: id,
                })
                    .then((res) => {
                        this.dataForm = res.data;
                        this.getinvoice();
                    })
                    .catch((err) => console.log(err));
            },

            //编辑发票抬头
            changeInvoice(id) {
                this.modal = true;
                try {
                    var params = {
                        id: id,
                    };
                    memberInvoiceDetail(params)
                        .then((res) => {
                            if (res.code == 200) {
                                this.dataList = res.data;
                                this.id = res.data.id;
                                if (res.data.defaultFlag == 1) {
                                    this.defaultFlag = true;
                                } else {
                                    this.defaultFlag = false;
                                }
                                this.modal = true;
                            }
                        })
                        .catch((err) => console.log(err));
                } catch (e) {}
            },

            //删除发票抬头
            deleteInvoice() {
                try {
                    var ids = [];
                    ids.push(this.delId);

                    deleteInvoice(ids)
                        .then((res) => {
                            if (res.code == 200) {
                                this.getinvoice();
                                this.$message.success("删除成功");
                            }
                        })
                        .catch((err) => console.log(err));
                    this.modal1 = false;
                    this.getinvoice();
                } catch (e) {
                    // this.$message.info('删除失败')
                }
            },

            //获取发票抬头
            getinvoice() {
                memberInvoiceList()
                    .then((res) => {
                        if (res.code == 200) {
                            this.dataForm = res.data;
                            this.count = 5 - this.dataForm.length;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //添加发票抬头
            addInvoice() {
                this.dataList = {};
                this.modal = true;
            },
            //保存发票抬头
            submit(dataList) {
                // this.loading = true
                this.$refs.dataList.validate((valid) => {
                    if (valid) {
                        this.modal = true;
                        let params = {
                            company: this.dataList.company,
                            dutyParagraph: this.dataList.dutyParagraph,
                            registeredAddress: this.dataList.registeredAddress,
                            officePhone: this.dataList.officePhone,
                            bankName: this.dataList.bankName,
                            bankNumber: this.dataList.bankNumber,
                            defaultFlag: this.defaultFlag ? 1 : 0,
                            id: this.dataList.id ? this.dataList.id : "",
                        };
                        getInvoice(params)
                            .then((res) => {
                                if (res.code == 200) {
                                    this.$Message.success(
                                        this.dataList.id
                                            ? "编辑"
                                            : "新增" + "发票抬头成功"
                                    );
                                    this.getinvoice();
                                    this.modal = false;
                                }
                            })
                            .catch((error) => {
                                console.log(error);
                                this.modal = true;
                            });
                    } else {
                        this.$Message.error("请填写完整后提交");
                    }
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .invoice {
        width: 948px;
        height: 1005px;
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

        .invoiceCenter {
            width: 100%;
            height: 950px;
            padding: 0 40px;

            .invoiceList {
                width: 869px;
                height: 120px;
                border: 1px solid rgba(204, 204, 204, 1);
                margin: 0 0 39px 0;
                padding: 0 30px;
                display: flex;
                justify-content: center;
                flex-flow: column;

                .invoiceList-one {
                    width: 808px;
                    height: 30px;
                    line-height: 30px;
                    display: flex;
                    justify-content: space-between;

                    span {
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(102, 102, 102, 1);
                    }

                    .invoiceList-btn {
                        width: 71px;
                        height: 30px;
                        font-size: 12px;
                        padding: 0 12px;
                        color: #ffffff;
                        border-radius: 15px;
                        text-align: center;
                        background: linear-gradient(
                            270deg,
                            rgba(247, 70, 10, 1) 0%,
                            rgba(224, 45, 25, 1) 100%
                        );
                        border: none;
                    }
                }

                .invoiceList-btn1 {
                    width: 71px;
                    height: 30px;
                    font-size: 12px;
                    padding: 0 12px;
                    color: #777777;
                    background: #ebebeb;
                    border-radius: 15px;
                    text-align: center;
                    border: none;
                }

                .invoiceList-two {
                    width: 808px;
                    height: 20px;
                    line-height: 20px;
                    display: flex;
                    justify-content: space-between;

                    span {
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(102, 102, 102, 1);
                    }

                    .invoiceList-twoWord {
                        margin: 0 7px;
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(221, 38, 25, 1);
                        cursor: pointer;
                    }
                }
            }
        }
    }

    .invoiceCenter-add {
        height: 75px;
        line-height: 75px;
        display: flex;
        justify-content: space-between;

        .content {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
        }

        .addInvoice {
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: rgba(10, 81, 135, 1);
            cursor: pointer;
        }
    }

    .nullInvoice {
        width: 948px;
        padding: 0 40px;

        .nullInvoice-p {
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
            text-align: center;
        }
    }

    .modal-p {
        font-size: 14px;
        font-weight: 400;
        margin: 0 0 22px;
        color: #333333;
    }

    .cancel {
        width: 96px;
        height: 34px;
        background: #f5f5f5;
        color: #333333;
        font-weight: 400;
        letter-spacing: 1px;
    }

    .confirm {
        width: 96px;
        height: 34px;
        background: #dd2619;
        color: #ffffff;
        margin-left: 38px;
        font-weight: 400;
        letter-spacing: 1px;
    }

    .modal-btn {
        margin: 78px 0 0 0;
        text-align: center;
    }

    /deep/ .ivu-modal-header {
        width: 135px;
        height: 55px;
        font-size: 22px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        line-height: 30px;
        margin: 0 auto 36px;
        padding: 25px 0 0;
        color: rgba(51, 51, 51, 1);
        border-bottom: none;
    }

    /deep/ .ivu-modal-header-inner {
        font-size: 22px;
    }

    /deep/ .ivu-input {
        height: 38px;
        padding: 4px 14px;
    }

    /deep/ .ivu-input-wrapper {
        width: 327px;
        height: 38px;
        margin: 0 0 0 12px;
    }

    /deep/ .ivu-modal-body {
        padding: 0 95px;
        text-align: end;
    }

    /deep/ .ivu-modal-footer {
        border-top: none;
        padding: 67px 18px 49px 18px;
        text-align: center;
    }

    /deep/ .ivu-btn-text {
        background: #f5f5f5;
        border-radius: 2px;
        line-height: 32px;
        color: #333333;
    }

    /deep/ .ivu-btn {
        width: 96px;
        height: 34px;
        font-size: 15px;
        border: none;
    }

    /deep/ .ivu-form .ivu-form-item-label {
        width: 88px;
        height: 38px;
        line-height: 38px;
        padding: 0;
    }

    /deep/ .ivu-modal-content {
        width: 618px;
        height: 590px;
        text-align: center;
        margin: 78px 0 0 0;
        border-radius: 12px;
    }

    /deep/ .ivu-form-item-error-tip {
        left: 103px;
    }
    /deep/ .ivu-modal-header p,
    .ivu-modal-header-inner {
        color: #333333;
    }
</style>
