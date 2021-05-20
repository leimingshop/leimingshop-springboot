<template>
    <div class="invoice">
        <div class="verification">
            <div class="verificationTop">
                <div>
                    发票进度：
                    <span
                        style="color: #dd2619"
                        v-if="this.invoiceInfo.invoiceEvolve == 1"
                        >已申请</span
                    >
                    <span
                        style="color: #dd2619"
                        v-if="this.invoiceInfo.invoiceEvolve == 2"
                        >已开具</span
                    >
                    <span
                        style="color: #dd2619"
                        v-if="this.invoiceInfo.invoiceEvolve == 3"
                        >待换开</span
                    >
                    <span
                        style="color: #dd2619"
                        v-if="this.invoiceInfo.invoiceEvolve == 4"
                        >已换开</span
                    >
                </div>
            </div>
            <div
                style="width: 381px; margin: 12px 165px 0 185px"
                v-show="invoiceInfo.invoiceEvolve < 3"
            >
                <Steps :current="invoiceInfo.invoiceEvolve">
                    <Step content="申请开票"></Step>
                    <Step content="商户核验"></Step>
                    <Step content="发票开具"></Step>
                </Steps>
            </div>
            <div
                style="width: 381px; margin: 12px 165px 0 185px"
                v-show="invoiceInfo.invoiceEvolve > 2"
            >
                <Steps :current="invoiceInfo.invoiceEvolve - 2">
                    <Step content="申请开票"></Step>
                    <Step content="商户核验"></Step>
                    <Step content="发票开具"></Step>
                </Steps>
            </div>
        </div>
        <div class="verification">
            <div class="invoiceData">
                <div
                    style="
                        width: 3px;
                        height: 14px;
                        margin-right: 3px;
                        border: 1px solid #dd2619;
                        background: #dd2619;
                    "
                ></div>
                发票信息
            </div>
            <div class="invoice-p">
                <div class="invoiceList">
                    <span class="invoice-row"
                        >发票类型：
                        <span v-if="invoiceInfo.invoiceType == 1"
                            >电子普通发票</span
                        >
                        <span v-if="invoiceInfo.invoiceType == 2"
                            >纸质普通发票</span
                        >
                        <span v-if="invoiceInfo.invoiceType == 3"
                            >增值税专项发票</span
                        >
                        <span v-if="invoiceInfo.invoiceType == 0"
                            >不开发票</span
                        >
                    </span>
                    <span class="invoice-row"
                        >发票内容：
                        <span
                            class="invoiceItemContent"
                            v-if="invoiceInfo.invoiceContent == 1"
                            >商品明细</span
                        >
                        <span
                            class="invoiceItemContent"
                            v-if="invoiceInfo.invoiceContent == 2"
                            >商品类别</span
                        >
                    </span>
                    <span class="invoice-row"
                        >发票抬头：
                        <span
                            class="invoiceItemContent"
                            v-if="invoiceInfo.companyType == 1"
                            >个人</span
                        >
                        <span
                            class="invoiceItemContent"
                            v-if="invoiceInfo.companyType == 2"
                            >单位</span
                        >
                    </span>
                </div>
                <span class="invoice-rows" v-show="invoiceInfo.companyType == 1"
                    >个人名称：{{ invoiceInfo.personalName }}</span
                >

                <span class="invoice-rows" v-show="invoiceInfo.companyType == 2"
                    >单位名称：{{ invoiceInfo.company }}</span
                >
                <span class="invoice-rows" v-show="invoiceInfo.companyType == 2"
                    >公司税号：{{ invoiceInfo.dutyParagraph }}</span
                >
                <span class="invoice-rows" v-show="invoiceInfo.companyType == 2"
                    >注册地址：{{ invoiceInfo.registeredAddress }}</span
                >
                <span class="invoice-rows" v-show="invoiceInfo.companyType == 2"
                    >办公电话：{{ invoiceInfo.officePhone }}</span
                >
                <span class="invoice-rows" v-show="invoiceInfo.companyType == 2"
                    >开户银行：{{ invoiceInfo.bankName }}</span
                >
                <span
                    style="margin: 24px 0 0 0px"
                    v-show="invoiceInfo.companyType == 2"
                    >银行账号：{{ invoiceInfo.bankNumber }}</span
                >
            </div>
        </div>
        <div class="verification">
            <div class="invoiceData">
                <div
                    style="
                        width: 3px;
                        height: 14px;
                        margin-right: 3px;
                        border: 1px solid #dd2619;
                        background: #dd2619;
                    "
                ></div>
                订单信息
            </div>
            <div class="invoice-p">
                <span class="invoice-rows"
                    >订单编号：{{ invoiceInfo.orderSn }}</span
                >
                <span class="invoice-rows"
                    >下单时间：{{ invoiceInfo.createOrderDate }}</span
                >
            </div>
        </div>
        <div class="verification">
            <div class="invoiceData">
                <div
                    style="
                        width: 3px;
                        height: 14px;
                        margin-right: 3px;
                        border: 1px solid #dd2619;
                        background: #dd2619;
                    "
                ></div>
                收件信息
            </div>
            <div class="invoice-p">
                <span class="invoice-rows"
                    >收票人手机：{{ invoiceInfo.addresseePhone }}</span
                >
                <span
                    class="invoice-rows"
                    v-show="
                        invoiceInfo.invoiceType == 1 &&
                        invoiceInfo.addresseeMail.length > 0
                    "
                    >收票人邮箱：{{ invoiceInfo.addresseeMail }}</span
                >
                <span
                    class="invoice-rows"
                    v-show="
                        (invoiceInfo.invoiceType == 2 ||
                            invoiceInfo.invoiceType == 3) &&
                        invoiceInfo.detailedAddress.length > 0
                    "
                    >收票人姓名：{{ invoiceInfo.addresseeName }}</span
                >
                <span
                    class="invoice-rows"
                    v-show="
                        (invoiceInfo.invoiceType == 2 ||
                            invoiceInfo.invoiceType == 3) &&
                        invoiceInfo.detailedAddress.length > 0
                    "
                    >收票人地址：{{ invoiceInfo.addresseeAddress }}</span
                >
                <span
                    class="invoice-rows"
                    v-show="
                        (invoiceInfo.invoiceType == 2 ||
                            invoiceInfo.invoiceType == 3) &&
                        invoiceInfo.detailedAddress.length > 0
                    "
                    >详细地址：{{
                        invoiceInfo.addresseeAddress +
                        " " +
                        invoiceInfo.detailedAddress
                    }}</span
                >
                <a
                    v-show="
                        invoiceInfo.invoiceType == 1 &&
                        (invoiceInfo.invoiceEvolve == 2 ||
                            invoiceInfo.invoiceEvolve == 4)
                    "
                    :href="$imgURL + invoiceInfo.fileUrl"
                    target="_blank"
                    download=""
                    style="width: 80px; line-height: 70px"
                    >下载发票</a
                >
            </div>
        </div>
        <div
            class="verification"
            v-show="
                (invoiceInfo.invoiceEvolve == 2 ||
                    invoiceInfo.invoiceEvolve == 4) &&
                (invoiceInfo.storeInvoiceType == 2 ||
                    invoiceInfo.storeInvoiceType == 3)
            "
        >
            <div class="invoiceData">
                <div
                    style="
                        width: 3px;
                        height: 14px;
                        margin-right: 3px;
                        border: 1px solid #dd2619;
                        background: #dd2619;
                    "
                ></div>
                开票信息
            </div>
            <div class="invoice-p">
                <span class="invoice-rows"
                    >发票代码：{{ invoiceInfo.invoiceCode }}</span
                >
                <span class="invoice-rows"
                    >发票号码：{{ invoiceInfo.invoiceNumber }}</span
                >
                <span class="invoice-rows"
                    >开票时间：{{ invoiceInfo.invoiceDate }}</span
                >
                <span class="invoice-rows"
                    >物流商：{{ invoiceInfo.logisticsCompanies }}</span
                >
                <span class="invoice-rows"
                    >物流单号：{{ invoiceInfo.logisticsNumber }}</span
                >
            </div>
        </div>
        <div class="notes">
            注：发票金额为实际支付金额，不包含优惠券、满减活动及虚拟资产等抵消金额
        </div>
        <div style="padding: 0 200px; display: flex; justify-content: center">
            <Button class="invoice-footerBtn" @click="goInvoiceList()"
                >返回</Button
            >
            <Button
                v-if="
                    invoiceInfo.invoiceType == 1 &&
                    (invoiceInfo.invoiceEvolve == 2 ||
                        invoiceInfo.invoiceEvolve == 4)
                "
                type="primary"
                class="invoice-footerBtn2"
                @click="modal = true"
                >发送邮箱</Button
            >
            <Button
                class="invoice-footerBtn3"
                @click="goChangeInvoice()"
                v-if="this.invoiceInfo.invoiceEvolve == 2"
                >换开申请</Button
            >
        </div>
        <Modal
            v-model="modal"
            @on-ok="handleSubmit('formInline')"
            :loading="loading"
            @on-cancel="modal = false"
            title="发送邮箱"
        >
            <div
                class="email-body"
                style="height: 100px; padding: 30px 0 0 50px"
            >
                <!-- <span style="margin-right: 10px">收件邮箱</span>
        <Input
          v-model="email"
          placeholder="请输入邮箱号码"
          style="width: 300px"
        /> -->
                <i-form
                    ref="formInline"
                    :model="formInline"
                    :label-width="80"
                    :rules="ruleInline"
                >
                    <Form-item label="收件邮箱" prop="email">
                        <Input
                            v-model="formInline.email"
                            placeholder="请输入邮箱号码"
                            style="width: 300px"
                        />
                    </Form-item>
                </i-form>
            </div>
        </Modal>
    </div>
</template>
<script>
    import {
        orderInvoice,
        sendMail,
        invoiceCheck,
    } from "@/api/api_06-13-06invoiceTitle.js";
    export default {
        head() {
            return {
                title: "发票核验",
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
                invoiceInfo: {
                    addresseeAddress: "", //收票人地址（空格分隔） ,
                    addresseeMail: "", //收票人邮箱 ,
                    addresseeName: "", //收票人名称 ,
                    addresseePhone: "", //收票人手机 ,
                    applyDate: "", //申请时间 ,
                    bankName: "", //开户银行 ,
                    bankNumber: 1, //银行账号 ,
                    changeFlag: 1, //是否换开（默认0：未换开、1：换开） ,
                    company: "", //企业名称（公司抬头） ,
                    companyType: 1, //用户提交抬头类型（默认0：不开票、1：个人、2：单位） ,
                    detailedAddress: "", //收票人详细地址 ,
                    dutyParagraph: "", //企业税号 ,
                    fileUrl: "", //发票文件地址（url） ,
                    invoiceCode: "", //发票代码 ,
                    invoiceDate: "", //开票时间 ,
                    invoiceEvolve: 1, //开票进度（1：待开票、2：已开票、3：待换开、4：已换开） ,
                    invoiceNumber: "", //发票号码 ,
                    logisticsCompanies: "", //物流公司 ,
                    logisticsNumber: "", //物流单号 ,
                    memberInvoiceContent: 1, //用户提交发票内容（1：商品明细、2：商品类别） ,
                    memberInvoiceType: 1, //用户提交发票类型(1：电子、2：纸质、3：增值税) ,
                    officePhone: "", //办公电话 ,
                    orderSn: "", //订单编号
                    personalName: "", //个人名称（个人发票使用） ,
                    registeredAddress: "", //注册地址 ,
                    storeId: 1, //店铺ID ,
                    storeInvoiceContent: 1, //商家开具发票内容（1：商品明细、2：商品类别） ,
                    storeInvoiceType: 1, //商家开具发票类型(1：电子、2：纸质、3：增值税)
                },
                modal: false,
                formInline: {
                    email: "",
                },
                ruleInline: {
                    email: [
                        { required: true, message: "请填写邮箱", trigger: "blur" },
                        {
                            type: "email",
                            message: "邮箱格式不正确",
                            trigger: "blur",
                        },
                    ],
                },
                loading: true,
            };
        },
        created() {
            this.getOrderInvoice();
        },
        methods: {
            handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.sendEmail();
                    } else {
                        this.closeEmailLoading();
                    }
                });
            },
            closeEmailLoading() {
                setTimeout(() => {
                    this.loading = false;
                    this.$nextTick(() => {
                        this.loading = true;
                    });
                });
            },
            getOrderInvoice() {
                orderInvoice(this.$route.query.orderId)
                    .then((res) => {
                        if (res.code == 200) {
                            this.invoiceInfo = res.data;
                            this.invoiceInfo = {
                                ...this.invoiceInfo,
                                //    changeFlag:1,
                                //    invoiceEvolve:1,
                                //   companyType:1,
                                //   storeInvoiceType:1,
                            };
                            this.invoiceInfo.company =
                                this.invoiceInfo.company || "";
                            this.invoiceInfo.dutyParagraph =
                                this.invoiceInfo.dutyParagraph || "";
                            this.invoiceInfo.registeredAddress =
                                this.invoiceInfo.registeredAddress || "";
                            this.invoiceInfo.officePhone =
                                this.invoiceInfo.officePhone || "";
                            this.invoiceInfo.bankName =
                                this.invoiceInfo.bankName || "";
                            this.invoiceInfo.bankNumber =
                                this.invoiceInfo.bankNumber || "";
                            this.invoiceInfo.personalName =
                                this.invoiceInfo.personalName || "";
                            this.invoiceInfo.addresseePhone =
                                this.invoiceInfo.addresseePhone || "";
                            this.invoiceInfo.addresseeMail =
                                this.invoiceInfo.addresseeMail || "";
                            this.invoiceInfo.addresseeAddress =
                                this.invoiceInfo.addresseeAddress || "";
                            this.invoiceInfo.detailedAddress =
                                this.invoiceInfo.detailedAddress || "";

                            if (
                                this.invoiceInfo.storeInvoiceType &&
                                this.invoiceInfo.storeInvoiceType !== null
                            ) {
                                this.invoiceInfo.invoiceType = this.invoiceInfo.storeInvoiceType;
                            } else {
                                this.invoiceInfo.invoiceType = this.invoiceInfo.memberInvoiceType;
                            }

                            if (this.invoiceInfo.storeInvoiceContent) {
                                this.invoiceInfo.invoiceContent = this.invoiceInfo.storeInvoiceContent;
                            } else {
                                this.invoiceInfo.invoiceContent = this.invoiceInfo.memberInvoiceContent;
                            }
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            goInvoiceList() {
                this.$router.push({
                    path: "/personalCenterBase/invoiceBase/useInvoice",
                });
            },
            sendEmail() {
                sendMail({
                    id: this.invoiceInfo.id,
                    email: this.formInline.email,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.modal = false;
                            this.$Message.success(res.msg);
                        }
                    })
                    .catch((error) => {
                        this.closeEmailLoading();
                        console.log(error);
                    });
            },
            goChangeInvoice() {
                invoiceCheck(this.$route.query.orderId)
                    .then((res) => {
                        if (res.code == 200) {
                            let newpage = this.$router.resolve({
                                path:
                                    "/personalCenterBase/invoiceBase/applyInvoice",
                                query: {
                                    orderId: this.$route.query.orderId,
                                    orderSn: this.orderSn,
                                },
                            });
                            window.open(newpage.href, "_blank");
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .invoice {
        width: 948px;
        // height: 100%;
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

        .verification {
            // height: 122px;
            padding: 41px 103px;

            .verificationTop {
                margin: 20px 30px 12px;
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
            }

            .invoiceData {
                font-size: 16px;
                font-weight: 400;
                color: #333333;
                display: flex;
                line-height: 16px;
                margin: 28px 0 20px 30px;
            }

            .invoiceList {
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
                display: flex;
                justify-content: space-between;
                // margin: 0 0 24px 0;
            }
        }

        .verification:first-child {
            padding: 41px 103px 0px;
        }
        .verification:nth-child(2) {
            padding: 28px 103px 0px;
        }
        .verification:nth-child(3) {
            padding: 52px 103px 0;
            .invoiceData {
                margin: 0px 0 0px 30px;
            }
        }
        .verification:nth-child(4) {
            padding: 52px 103px 0px;
            .invoiceData {
                margin: 0px 0 0px 30px;
            }
        }
    }

    .invoice-p {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(102, 102, 102, 1);
        display: flex;
        flex-flow: column;
        padding: 0 0 0 56px;
    }

    .invoice-rows {
        margin: 24px 0 0;
        text-indent: -5em;
        margin-left: 5em;
    }

    .download {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(28, 146, 255, 1);
        padding: 0 133px;
    }

    .notes {
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(221, 38, 25, 1);
        margin: 10px 0 0;
        padding: 0 133px;
    }

    .invoice-footerBtn {
        width: 96px;
        height: 34px;
        background: rgba(245, 245, 245, 1);
        border-radius: 1px;
        margin: 44px 38px 60px 0;
    }

    .invoice-footerBtn2 {
        width: 96px;
        height: 34px;
        border-radius: 1px;
        margin: 44px 0 60px 0;
    }

    .invoice-footerBtn3 {
        width: 96px;
        height: 34px;
        background: rgba(245, 245, 245, 1);
        border-radius: 1px;
        margin: 44px 0 60px 38px;
    }

    /deep/ .ivu-steps-content {
        margin: 12px 0 0;
        padding-left: 0;
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(51, 51, 51, 1);
    }
    /deep/ .ivu-form-item-error-tip {
        font-size: 12px;
    }
</style>
