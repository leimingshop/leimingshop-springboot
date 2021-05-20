<template>
    <div class="changePhoneContent">
        <Steps :current="current">
            <Step content="验证身份"></Step>
            <Step content="修改手机号码"></Step>
            <Step content="完成更换"></Step>
        </Steps>
        <div class="content">
            <div class="forgot">
                <p class="forgot-text">手机号：</p>
                <i-Input
                    v-model="mobile"
                    placeholder="手机号码"
                    maxlength="11"
                    style="width: 343px"
                ></i-Input>
            </div>
            <div class="forgot">
                <p class="forgot-text">图形验证码：</p>
                <i-Input
                    v-model="captchaCode"
                    placeholder="输入图形验证码"
                    maxlength="2"
                    style="width: 343px"
                ></i-Input>
                <img
                    :src="captcha"
                    class="fromFootP"
                    @click="changeCaptcha"
                    alt
                />
                <!-- <p style="min-width:100px;line-height:34px">看不清？<a @click="changeCaptcha" style="color:#D40D2B">换一张</a></p> -->
            </div>
            <!-- <div class="fromFoot">
                <img :src="captcha" class="fromFootP">
                <p>看不清？<a @click="changeCaptcha" style="color:#D40D2B">换一张</a></p>
            </div> -->
            <div class="forgot">
                <p class="forgot-text">验证码：</p>
                <i-Input
                    v-model="code"
                    placeholder="短信验证码"
                    maxlength="6"
                    style="width: 213px"
                ></i-Input>
                <Button
                    type="primary"
                    class="formBtn"
                    @click="getCodeMobile"
                    v-show="showCode == 1"
                    style="color: #dd2619; border-color: #dd2619 !important"
                    >发送验证码</Button
                >
                <Button
                    type="primary"
                    class="formBtn"
                    disabled
                    v-show="showCode == 2"
                    >{{ count }}s后重新获取</Button
                >
            </div>
        </div>
        <Button type="primary" @click="nextChange(code)" class="content-btn"
            >下一步</Button
        >
    </div>
</template>
<script>
    import { verifyCaptcha } from "@/api/api_11forgotMessage";
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import { mobileCode, verifyCode } from "@/api/api_06-10-01securitySetting.js";
    const TIME_COUNT = 60;
    export default {
        data() {
            return {
                current: 1,
                mobile: "",
                codeType: 1,
                showCode: 1,
                disabled: true,
                code: "",
                count: "",
                captchaCode: "",
                timer: null,
                rule: {
                    mobile: [
                        {
                            required: true,
                            message: "手机号码不能为空",
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        computed: {
            ...mapState("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "captcha",
            ]),
        },
        created() {
            this.$store.dispatch("register/getCaptcha");
            this.changeCaptcha();
        },
        watch: {
            code(val, oldval) {
                if (val.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
            mobile(val, oldval) {
                if (val && val.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
        },
        methods: {
            ...mapActions("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "getCaptcha",
            ]),
            changeCaptcha() {
                // this.$store.dispatch('register/getCaptcha')
                this.getCaptcha();
            },
            getCodeMobile() {
                if (this.captchaCode == null || "" == this.captchaCode) {
                    this.$Message.error("请输入图形验证码");
                    this.changeCaptcha();
                    return;
                }
                verifyCaptcha({
                    uuid: this.$store.state.register.uuid,
                    captcha: this.captchaCode,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            let obj = {
                                mobile: this.mobile,
                                codeType: 1,
                            };
                            mobileCode(obj)
                                .then((res) => {
                                    if (res.code != 200) {
                                        // this.$Message.info(res.msg);
                                        this.changeCaptcha();
                                        this.captchaCode = "";
                                    } else {
                                        if (!this.timer) {
                                            this.count = TIME_COUNT;
                                            this.showCode = 2;
                                            this.timer = setInterval(() => {
                                                if (
                                                    this.count > 0 &&
                                                    this.count <= TIME_COUNT
                                                ) {
                                                    this.count--;
                                                } else {
                                                    this.showCode = 1;
                                                    clearInterval(this.timer);
                                                    this.timer = null;
                                                }
                                            }, 1000);
                                        }
                                    }
                                })
                                .catch((err) => console.log(err));
                        }
                        this.changeCaptcha();
                    })
                    .catch((err) => console.log(err));
            },
            nextChange() {
                if (!this.mobile) {
                    this.$Message.info("请输入手机号");
                    return;
                }
                if (!this.captchaCode) {
                    this.$Message.info("请输入图形验证码");
                    return;
                }
                if (!this.code) {
                    this.$Message.info("请输入验证码");
                    return;
                }

                verifyCode({
                    mobile: this.mobile,
                    code: this.code,
                    codeType: this.codeType,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.$emit("childFn", "changePhone03");
                        } else {
                            this.changeCaptcha();
                            this.captchaCode = "";
                            this.code = "";
                            this.showCode = 1;
                        }
                    })
                    .catch((err) => console.log(err));
            },
        },
    };
</script>
<style lang="scss" scoped>
    .content {
        padding: 50px 0 0;
        .changePhone-text {
            height: 14px;
            line-height: 14px;
            font-size: 14px;
            font-weight: 500;
            margin: 30px 0 10px;
            color: #333333;
            text-align: center;
            font-family: PingFangSC-Medium, PingFang SC;
        }

        .changePhone-small {
            font-size: 13px;
            color: #a3a3a3;
            margin: 0 0 30px;
            text-align: center;
        }
    }
    .forgot {
        width: 405px;
        height: 34px;
        margin: 0 auto 24px;
        display: flex;

        .forgot-text {
            width: 85px;
            min-width: 85px;
            height: 34px;
            line-height: 34px;
            font-size: 14px;
            color: #222222;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            // margin: 0 14px 0 0;
            text-align: end;
        }

        .forgot-btn {
            width: 120px;
            height: 34px;
            // background: #3e3d46;
            border-radius: 2px;
            color: #999999;
            margin: 0 0 0 10px;
        }
        img {
            width: 102px;
            height: 34px;
            border: 1px solid #dcdee2;
            margin-left: 10px;
        }
    }

    .fromFoot {
        height: 44px;
        line-height: 44px;
        margin: 0 35px 41px 94px;
        text-align: center;
        display: flex;
        justify-content: center;

        .fromFootP {
            width: 102px;
            height: 44px;
            background: #ffffff;
            border: 1px solid #dcdee2;
            margin-left: 10px;
        }

        p {
            font-size: 18px;
            color: #3e3d46;
            margin: 0 0 0 16px;

            span {
                color: #d40d2b;
            }
        }
    }
    .content-btn {
        width: 120px;
        height: 44px;
        color: #ffffff;
        background: linear-gradient(270deg, #f04e36 0%, #dd2619 100%);
        border-radius: 22px;
        font-size: 15px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        margin: 0 135px;
    }
    /deep/
        .ivu-steps-item.ivu-steps-status-finish
        .ivu-steps-head-inner
        > .ivu-steps-icon,
    .ivu-steps-item.ivu-steps-status-finish .ivu-steps-head-inner span {
        color: #ffffff;
        background: #2ac68a;
        border-radius: 50%;
        border-color: #2ac68a !important;
    }
    /deep/ .ivu-steps-item.ivu-steps-status-finish .ivu-steps-head-inner {
        border-color: #2ac68a !important;
    }
    /deep/ .forgot .forgot-text[data-v-2632ae15] {
        width: 72px;
        height: 44px;
        line-height: 44px;
        font-size: 16px;
        font-weight: 400;
    }
    /deep/ .ivu-input {
        height: 34px;
        border-radius: 1px !important;
        border-color: none !important;
        &:hover {
            border-color: #dd2619 !important;
        }
        &:focus {
            box-shadow: none !important;
            border-color: #dd2619 !important;
        }
    }
    /deep/ .ivu-input-wrapper {
        widows: 343px !important;
    }
    /deep/ .formBtn {
        height: 34px;
        margin: 0 0 0 10px;
        border: 1px solid rgba(204, 204, 204, 1);
        border-radius: 2px;
        background: #ffffff;
        color: #cccccc;
    }
    /deep/ .ivu-steps-item.ivu-steps-status-finish .ivu-steps-content {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #2ac68a;
        margin-top: 12px;
    }
    /deep/ .ivu-steps .ivu-steps-head-inner {
        margin-left: 15px;
        :nth-of-type(2) {
            margin-left: 25px;
        }
    }
    .ivu-steps .ivu-steps-tail {
        width: 70% !important;
        padding: 0 !important;
        left: 65px !important;
    }
    /deep/ .ivu-btn-primary {
        border-color: #ffffff !important;
        &:hover {
            border-color: #ffffff !important;
        }
    }
</style>
