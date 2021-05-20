<template>
    <div class="changePhoneContent">
        <Steps :current="current">
            <Step content="验证身份"></Step>
            <Step content="修改邮箱"></Step>
            <Step content="完成更换"></Step>
        </Steps>
        <!--有邮箱地址-->
        <!-- v-if="email != null"-->
        <div class="content">
            <div class="changePhone-text">已绑定的邮箱:{{ email }}</div>
            <div class="changePhone-small">若该邮箱已无法使用请联系客服</div>
            <div class="forgot">
                <p class="forgot-text">图形验证码：</p>
                <i-Input
                    v-model="captchaCode"
                    maxlength="2"
                    placeholder="输入图形验证码"
                ></i-Input>
                <img
                    :src="captcha"
                    class="fromFootP"
                    @click="changeCaptcha"
                    alt
                />
                <!-- <p style="min-width:100px;line-height:34px;">看不清？<a @click="changeCaptcha" style="color:#D40D2B">换一张</a></p> -->
            </div>
            <!-- <div class="fromFoot">
                <img :src="captcha" class="fromFootP">
                <p>看不清？<a @click="changeCaptcha" style="color:#D40D2B">换一张</a></p>
            </div> -->
            <div class="forgot">
                <p class="forgot-text">验证码：</p>
                <i-Input
                    v-model="code"
                    placeholder="邮箱验证码"
                    maxlength="6"
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
        <!-- <Button type="primary" @click="nextChanges(captchaCode)" v-if="email == null" class="content-btn">下一步</Button> -->
    </div>
</template>
<script>
    import { verifyCaptcha } from "@/api/api_11forgotMessage";
    import {
        mailInfo,
        changemail,
        changemailCode,
    } from "@/api/api_06-10-01securitySetting.js";
    const TIME_COUNT = 60;
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        data() {
            return {
                current: 0,
                showCode: 1,
                codeType: 0,
                code: "",
                timer: null,
                emailnew: "",
                email: "",
                count: "",
                disabled: true,
                captchaCode: "",
            };
        },
        computed: {
            ...mapState("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "captcha",
            ]),
        },
        created() {
            this.getData();
            this.$store.dispatch("register/getCaptcha");
            this.changeCaptcha();
        },
        watch: {
            code(val, oldval) {
                if (val && val.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
            email(val, oldval) {
                if (val && val.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
            captchaCode(val, oldval) {
                if (val.length > 0 && val) {
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
                console.log("更换图片");
                this.getCaptcha();
            },
            nextChange(captchaCode, code) {
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
                            this.changeCaptcha();
                            this.captchaCode = "";
                        } else if (res.code == 200) {
                            this.$emit("childFn1", "changeEmail02");
                        } else {
                            this.$Message.info(res.msg);
                            this.changeCaptcha();
                            this.captchaCode = "";
                        }
                    })
                    .catch((err) => console.log(err));
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
            getCodeEmail() {
                if (this.captchaCode == null || "" == this.captchaCode) {
                    this.$Message.error("请输入图形验证码");
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
                                codeType: 0,
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
        // padding: 50px 0 0;

        .changePhone-text {
            font-size: 14px;
            font-weight: 500;
            margin: 30px 0 10px;
            text-align: center;
            font-family: PingFangSC-Medium, PingFang SC;
            color: #333333;
        }

        .changePhone-small {
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #a3a3a3;
            margin: 0 0 30px;
            text-align: center;
        }
    }

    .ivu-btn ivu-btn-primary {
        width: 140px;
        height: 34px;
        background: linear-gradient(
            90deg,
            rgba(222, 42, 28, 1) 0%,
            rgba(255, 78, 3, 1) 100%
        );
        border-radius: 22px;
    }

    .forgot {
        width: 405px;
        height: 34px;
        margin: 0 auto 20px;
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
        height: 44px;
        line-height: 44px;
        margin: 0 26px 41px 94px;
        text-align: center;
        display: flex;
        justify-content: center;

        .fromFootP {
            width: 140px;
            height: 44px;
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
        width: 120px;
        height: 44px;
        color: #ffffff;
        font-size: 15px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        background: linear-gradient(270deg, #f04e36 0%, #dd2619 100%);
        border-radius: 22px;
        margin: 0 135px;
    }

    /deep/ .ivu-input {
        height: 34px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
    }

    /deep/ .formBtn {
        height: 34px;
        margin: 0 0 0 10px;
        border: 1px solid rgba(204, 204, 204, 1);
        border-radius: 2px;
        background: #ffffff;
        color: #cccccc;
    }

    /deep/ .ivu-input-wrapper {
        width: 348px;
    }
    /deep/ .ivu-steps-item.ivu-steps-status-process .ivu-steps-content {
        color: #2ac68aff;
    }
    /deep/ .ivu-steps .ivu-steps-head-inner {
        margin-left: 15px;
    }
    /deep/ .ivu-steps-horizontal .ivu-steps-content {
        font-size: 14px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        margin-top: 12px;
    }
    /deep/ .ivu-steps .ivu-steps-tail {
        width: 90% !important;
        left: 35px !important;
    }
</style>
