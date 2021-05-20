<template>
    <div class="changePhoneContent">
        <Steps :current="current">
            <Step content="验证身份"></Step>
            <Step content="修改手机号码"></Step>
            <Step content="完成更换" style="text-align: center"></Step>
        </Steps>
        <div class="content">
            <div class="changePhone-text">已绑定的手机:{{ memberMobile }}</div>
            <div class="changePhone-small">若该手机号已无法使用请联系客服</div>
            <div class="forgot">
                <p class="forgot-text">图形验证码：</p>
                <i-Input
                    v-model="captchaCode"
                    placeholder="输入图形验证码"
                    :maxlength="2"
                    style="width: 343px"
                ></i-Input>
                <div class="fromFoot">
                    <img
                        :src="captcha"
                        class="fromFootP"
                        @click="changeCaptcha"
                        alt
                    />
                    <!-- <div style="display: flex; width: 115px"> -->
                    <!-- <p>
              看不清？<a @click="changeCaptcha" style="color: #d40d2b"
                >换一张</a
              >
            </p> -->
                    <!-- </div> -->
                </div>
            </div>

            <div class="forgot">
                <p class="forgot-text">验证码：</p>
                <i-Input
                    v-model="code"
                    placeholder="短信验证码"
                    :maxlength="6"
                ></i-Input>
                <!-- <Button type="primary" class="formBtn" disabled v-show="showCode==0">发送验证码</Button> -->
                <Button
                    type="primary"
                    class="formBtn"
                    style="color: #dd2619; border-color: #dd2619"
                    @click="getCode()"
                    v-show="showCode == 1"
                    >发送验证码</Button
                >
                <Button
                    type="primary"
                    class="formBtn"
                    disabled
                    v-show="showCode == 2"
                    style="border-color：none"
                    >{{ count }}s后重新获取</Button
                >
            </div>
        </div>
        <Button
            type="primary"
            :disabled="disabled"
            @click="next"
            class="content-btn"
            >下一步</Button
        >
    </div>
</template>
<script>
    import { verifyCaptcha } from "@/api/api_11forgotMessage";
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import { homeMember } from "@/api/api_06-08-01personalData.js";
    import { mobileCode, verifyCode } from "@/api/api_06-10-01securitySetting.js";
    const TIME_COUNT = 60;
    export default {
        data() {
            return {
                current: 0,
                showCode: 1,
                codeType: 0,
                memberMobile: "",
                captchaCode: "",
                code: "",
                timer: null,
                memberName: "",
                count: "",
                disabled: true,
                content: "",
                loading: "",
            };
        },
        computed: {
            ...mapState("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "captcha",
            ]),
        },
        created() {
            this.getHomeData();
            this.$store.dispatch("register/getCaptcha");
        },
        watch: {
            code(val, oldval) {
                if (val.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
            memberMobile(val, oldval) {
                if (val && val.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
        },
        methods: {
            changeCaptcha() {
                this.$store.dispatch("register/getCaptcha");
            },
            next() {
                if (!this.captchaCode) {
                    this.$Message.info("请输入图形验证码");
                    return;
                }
                if (!this.code) {
                    this.$Message.info("请输入验证码");
                    return;
                }
                verifyCode({
                    mobile: this.memberMobile,
                    code: this.code,
                    codeType: this.codeType,
                    noPopupMessage: true,
                })
                    .then((res) => {
                        if (res.code !== 200) {
                            // this.$Message.info("验证码错误1")
                            this.changeCaptcha();
                            this.captchaCode = "";
                        } else {
                            this.$emit("childFn", "changePhone02");
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //获取个人信息
            getHomeData(id) {
                var obj = {
                    id: id,
                };
                homeMember(obj)
                    .then((res) => {
                        this.memberMobile = res.data.memberMobile;
                    })
                    .catch((err) => console.log(err));
            },
            getCode() {
                if (this.captchaCode == null || "" == this.captchaCode) {
                    this.$Message.info("请输入图形验证码");
                    this.changeCaptcha();
                    this.captchaCode = "";
                    return;
                }
                verifyCaptcha({
                    uuid: this.$store.state.register.uuid,
                    captcha: this.captchaCode,
                })
                    .then((res) => {
                        console.log(res);
                        if (res.code == 200) {
                            let obj = {
                                mobile: this.memberMobile,
                                codeType: 0,
                            };
                            mobileCode(obj)
                                .then((res) => {
                                    if (res.code == 200) {
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
                                    } else {
                                        // this.$Message.info("图形验证码不正确");
                                        // noPopupMessage:true
                                    }
                                })
                                .catch((err) => console.log(err));
                        } else {
                            // this.$Message.info("图形验证码不正确");
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
            height: 12px;
            line-height: 12px;
            font-size: 12px;
            color: #a3a3a3;
            font-weight: 400;
            font-family: PingFangSC-Regular, PingFang SC;
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
        border-color: none !important;
        &:hover {
            border-color: none !important;
        }
    }

    .forgot {
        width: 405px;
        height: 34px;
        // margin: 0 auto 50px;
        margin-bottom: 24px;
        display: flex;

        .forgot-text {
            // width: 80px;
            min-width: 90px;
            height: 34px;
            line-height: 34px;
            font-size: 14px;
            color: #222222;
            font-family: PingFangSC-Regular;
            // margin: 0 14px 0;
            text-align: right;
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
        // margin: 0px 0px 24px 94px;
        text-align: center;
        display: flex;
        justify-content: center;
        margin-left: 10px;

        .fromFootP {
            width: 102px;
            height: 34px;
            background: #ffffff;
            border: 1px solid rgba(204, 204, 204, 1);
        }

        p {
            font-size: 14px;
            color: #333333;
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
        border-color: #ffffff !important;
        border-radius: 25px;
        margin: 6px 135px;
        &:hover {
            border-color: #ffffff !important;
        }
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

    /deep/ .formBtn {
        height: 34px;
        margin: 0 0 0 10px;
        // border: 1px solid #dd2619;
        border-radius: 2px;
        background: #ffffff;
        // color: #dd2619;
    }
    /deep/.ivu-steps-item.ivu-steps-status-process .ivu-steps-head-inner {
        margin-left: 15px;
    }
    /deep/ .ivu-steps .ivu-steps-head-inner {
        margin-left: 20px;
        margin-right: 25px;
    }
    /deep/ .ivu-steps .ivu-steps-tail {
        width: 90% !important;
        left: 45px !important;
    }
    /deep/ .ivu-steps-horizontal .ivu-steps-item:not(:first-child) .ivu-steps-head {
        padding-left: 18px;
    }
    /deep/ .ivu-steps .ivu-steps-head-inner {
        border: none;
    }
    /deep/ .ivu-steps-item.ivu-steps-status-wait .ivu-steps-head-inner span {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #666666 !important;
    }

    /deep/ .ivu-btn-primary {
        border-color: none !important;
        &:hover {
            border-color: none !important;
        }
    }
</style>
