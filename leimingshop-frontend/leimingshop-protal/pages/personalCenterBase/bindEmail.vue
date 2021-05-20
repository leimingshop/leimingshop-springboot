<template>
    <div class="changePhone">
        <div class="changePhoneTop">
            <span>绑定邮箱</span>
        </div>
        <div class="line"></div>
        <div class="changeContent">
            <div style="height: 558px">
                <!--绑定邮箱-->
                <div class="content">
                    <!-- <div class="changePhone-text">绑定邮箱地址</div> -->
                    <div class="forgot">
                        <p class="forgot-text">邮箱地址：</p>
                        <i-Input
                            v-model="email"
                            placeholder="邮箱地址"
                        ></i-Input>
                    </div>
                    <div class="forgot">
                        <p class="forgot-text">验证码：</p>
                        <i-Input
                            v-model="captchaCode"
                            placeholder="输入图形验证码"
                            @blur="sendEmail"
                        ></i-Input>
                        <!-- <div class="fromFoot"> -->
                        <img
                            :src="captcha"
                            class="fromFootP"
                            @click="changeCaptcha"
                            style="
                                margin-left: 10px;
                                border: 1px solid #dddddd;
                                border-radius: 3px;
                            "
                            alt
                        />
                        <!-- <p style="display: flex; min-width: 100px; line-height: 34px">
              看不清？<a @click="changeCaptcha" style="color: #d40d2b"
                >换一张</a
              >
            </p> -->
                        <!-- </div> -->
                    </div>
                    <!-- <div class="fromFoot">
                    <img :src="captcha" class="fromFootP">
                    <p>看不清？<a @click="changeCaptcha" style="color:#D40D2B">换一张</a></p>
                </div> -->
                </div>
                <!-- <Button type="primary" @click="nextChange(code)" v-if="email != null" class="content-btn">下一步</Button> -->
                <div style="text-align: center">
                    <Button
                        type="primary"
                        @click="nextChanges(captchaCode)"
                        class="content-btn"
                        >提交</Button
                    >
                </div>
            </div>
            <div class="changeContent-footer">
                <ul>
                    <li class="change-p">温馨提示</li>
                    <li class="change-text">
                        • 为保障您的帐号安全，变更重要信息需要身份验证
                    </li>
                    <li class="change-text">
                        • 若有疑问请联系在线客服或拨打400-0000-000（周一至周日
                        8:00-18:00）
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
    import { verifyCaptcha } from "@/api/api_11forgotMessage";
    import { mobileBind } from "@/api/api_06-10-01securitySetting.js";
    import { mapState } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        data() {
            return {
                email: "",
                captchaCode: "",
                showCode: 1,
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
        head() {
            return {
                title: "绑定邮箱",
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
        },
        watch: {
            email(val, oldval) {
                if (val && val.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
        },
        methods: {
            sendEmail: function () {
                var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                if (this.email != "" && !regEmail.test(this.email)) {
                    this.$Message({
                        message: "邮箱格式不正确",
                        type: "error",
                    });
                    this.email = "";
                }
            },
            changeCaptcha() {
                this.$store.dispatch("register/getCaptcha");
            },
            nextChanges(captchaCode) {
                var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                if (this.email == "" || !regEmail.test(this.email)) {
                    this.$Message.error("请输入正确的邮箱地址");
                    this.changeCaptcha();
                    this.captchaCode = "";
                    return;
                }
                verifyCaptcha({
                    uuid: this.$store.state.register.uuid,
                    captcha: this.captchaCode,
                })
                    .then((res) => {
                        if (res.code == 500) {
                            this.$Message.info("请输入验证码");
                        } else if (res.code == 200) {
                            mobileBind({
                                email: this.email,
                            }).then((res) => {
                                if (res.code == 200) {
                                    // this.$Message.info(res.msg);
                                }
                                this.$router.push({
                                    path: "/personalCenterBase/securitySetting",
                                });
                            }).catch(err=>console.log(err));
                        } else {
                            // this.$Message.info(res.msg);
                            this.changeCaptcha();
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
    .changePhone {
        background: #eeeeee;
    }
    .changePhoneTop {
        width: 948px;
        height: 54px;
        line-height: 54px;
        font-size: 14px;
        color: #333333;
        background: #ffffff;
        font-family: PingFangSC-Medium, PingFang SC;
        padding: 0 40px;
    }
    .line {
        background: #ebebeb;
        height: 1px;
    }
    .changePhoneContent {
        width: 411px;
        margin: 0 auto;
        .changePhone-text {
            font-size: 18px;
            font-weight: 500;
            margin: 40px 0 10px;
            text-align: center;
        }
        .changePhone-small {
            font-size: 16px;
            color: #999999;
            margin: 0 0 40px;
            text-align: center;
        }
    }
    .changeContent {
        width: 948px;
        height: 864px;
        background: #ffffff;
        // margin: 10px 0 0 0 ;
        padding: 100px 40px 0 40px;
    }
    .changeContent-footer {
        height: 168px;
        background-color: #ffffff;
        padding: 40px 0;
        border-top: 1px solid #ebebeb;
        .change-p {
            font-size: 14px;
            font-weight: 500;
            margin: 0 0 15px;
            color: #333333;
        }
        .change-text {
            font-size: 13px;
            font-weight: 400;
            margin: 0 0 10px;
            color: #717171;
        }
    }
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
        margin: 0 auto 24px;
        display: flex;
        .forgot-text {
            width: 80px;
            min-width: 80px;
            height: 34px;
            line-height: 34px;
            font-size: 14px;
            color: #333;
            text-align: right;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #222222;
            // margin: 0 14px 0 0;
        }
        .forgot-btn {
            width: 120px;
            height: 44px;
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
        height: 40px;
        color: #ffffff;
        background: linear-gradient(270deg, #f04e36 0%, #dd2619 100%);
        border-radius: 25px;
        margin: 0 135px;
        font-size: 15px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        margin-top: 6px;
    }
    /deep/ .ivu-input {
        height: 34px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #222222;
        border-color: none;
        &:hover {
            border-color: #dd2619 !important;
        }
        &:focus {
            box-shadow: none !important;
            border-color: #dd2619 !important;
        }
        &:active {
            border-color: none !important;
        }
    }
    /deep/ .formBtn {
        height: 44px;
        margin: 0 0 0 10px;
        border: 1px solid rgba(204, 204, 204, 1);
        border-radius: 2px;
        background: #ffffff;
        color: #cccccc;
    }
    /deep/ .ivu-input-wrapper {
        width: 348px;
    }
    /deep/ .ivu-steps-horizontal .ivu-steps-content {
        padding: 0;
    }
    /deep/ .ivu-steps-item.ivu-steps-status-process .ivu-steps-head-inner {
        border-color: #2ac68a;
        background-color: #2ac688;
    }
    /deep/ .ivu-steps .ivu-steps-head-inner {
        width: 24px;
        height: 24px;
        line-height: 22px;
        background: #dddddd;
    }

    /deep/.ivu-steps-item.ivu-steps-status-process .ivu-steps-tail > i {
        background: #82d38d;
    }
    /deep/ .person-center-right[data-v-4fc7d81c] {
        background: rgb(238, 238, 238);
    }
    // /deep/ .ivu-input-wrapper{
    //     width: 213px;
    // }
    .ivu-btn-primary {
        &:hover {
            border-color: #dd2619 !important;
        }
    }
</style>
