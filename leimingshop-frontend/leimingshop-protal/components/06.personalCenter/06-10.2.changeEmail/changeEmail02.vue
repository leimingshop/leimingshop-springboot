<template>
    <div class="changePhoneContent">
        <Steps :current="current">
            <Step content="验证身份"></Step>
            <Step content="修改邮箱"></Step>
            <Step content="完成更换"></Step>
        </Steps>
        <div class="content">
            <div class="forgot">
                <p class="forgot-text">邮箱地址：</p>
                <i-Input
                    type="email"
                    v-model="email"
                    placeholder="输入新的邮箱地址"
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
                    placeholder="邮箱验证"
                    style="width: 213px"
                    maxlength="6"
                    @blur="sendEmail"
                ></i-Input>
                <Button
                    type="primary"
                    class="formBtn"
                    @click="getCodeEmail()"
                    v-show="showCode == 1"
                    style="color: #dd2619; border-color: #dd2619"
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
        <Button
            type="primary"
            @click="nextChange(captchaCode, code)"
            v-if="email != null"
            class="content-btn"
            >下一步</Button
        >
    </div>
</template>
<script>
    import { verifyCaptcha } from "@/api/api_11forgotMessage";
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import {
        changemail,
        changemailCode,
    } from "@/api/api_06-10-01securitySetting.js";
    const TIME_COUNT = 60;
    export default {
        data() {
            return {
                current: 1,
                email: "",
                codeType: 1,
                showCode: 1,
                disabled: true,
                code: "",
                count: "",
                captchaCode: "",
                timer: null,
                rule: {
                    email: [
                        {
                            required: true,
                            message: "邮箱不能为空",
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
            email(val, oldval) {
                if (val.length > 0) {
                    this.showCode = 1;
                }
            },
        },
        methods: {
            ...mapActions("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "getCaptcha",
            ]),
            changeCaptcha() {
                console.log("更换图片");
                this.getCaptcha();
            },
            sendEmail: function () {
                var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                if (this.email == "" && !regEmail.test(this.email)) {
                    this.$Message({
                        message: "邮箱格式不正确",
                        type: "error",
                    });
                    this.email = "";
                }
            },
            getData() {
                mailInfo()
                    .then((res) => {
                        if (res.code == 200) {
                            this.email = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },

            nextChange(captchaCode, code) {
                if (!this.email) {
                    this.$Message.info("请输入邮箱地址");
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
                changemailCode({
                    email: this.email,
                    code: this.code,
                    codeType: this.codeType,
                })
                    .then((res) => {
                        if (res.code == 500) {
                            this.$Message.info("请输入验证码");
                        } else if (res.code == 200) {
                            this.$emit("childFn1", "changeEmail03");
                        } else {
                            this.$Message.info(res.msg);
                            this.changeCaptcha();
                            this.captchaCode = "";
                            this.code = "";
                            this.showCode = 1;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            getCodeEmail() {
                var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                if (this.email == "" || !regEmail.test(this.email)) {
                    this.$Message.error("请输入正确的邮箱地址");
                    return;
                }
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
                                email: this.email,
                                codeType: 1,
                            };
                            changemail(obj)
                                .then((res) => {
                                    if (res.code != 200) {
                                        this.$Message.info(res.msg);
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
                        } else {
                            this.changeCaptcha();
                            this.captchaCode = "";
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
            font-size: 18px;
            font-weight: 500;
            margin: 40px 0 10px;
            text-align: center;
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
            width: 100px;
            min-width: 100px;
            height: 34px;
            line-height: 34px;
            font-size: 14px;
            font-family: PingFangSC-Regualr, PingFang SC;
            font-weight: 400;
            color: #222222;
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
    }
    .fromFoot {
        height: 34px;
        line-height: 34px;
        margin: 0 35px 41px 94px;
        text-align: center;
        display: flex;
        justify-content: center;

        .fromFootP {
            width: 140px;
            height: 34px;
            background: #ffffff;
            border: 1px solid rgba(204, 204, 204, 1);
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
        width: 140px;
        height: 44px;
        color: #ffffff;
        font-size: 15px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        background: linear-gradient(270deg, #f04e36 0%, #dd2619 100%);
        border-radius: 22px;
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

    /deep/ .ivu-steps .ivu-steps-head-inner {
        margin-left: 15px;
    }

    /deep/ .ivu-input {
        height: 34px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
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

    /deep/ .ivu-steps-horizontal .ivu-steps-content {
        font-size: 14px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        margin-top: 12px;
    }

    /deep/ .ivu-steps-item.ivu-steps-status-process .ivu-steps-content {
        color: #2ac68aff;
    }
    /deep/ .ivu-steps-item.ivu-steps-status-finish .ivu-steps-content {
        color: #2ac68aff;
    }
    /deep/ .ivu-steps-item.ivu-steps-status-finish .ivu-steps-tail {
        i {
            &::after {
                background: #82d38dff;
            }
        }
    }
    /deep/ .ivu-steps .ivu-steps-tail {
        width: 90%;
        padding: 0px !important;
        left: 30px;
    }
</style>
