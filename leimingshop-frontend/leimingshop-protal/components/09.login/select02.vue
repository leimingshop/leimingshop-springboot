<template>
    <div class="password-login">
        <div class="input-body">
            <div class="error-message">
                <div class="messagbe-content" v-show="tabErrorMessage.length > 0">
                    {{ tabErrorMessage }}
                </div>
            </div>
            <Input clearable v-model="mobile" placeholder="手机号码" class="long-input" />
            <div class="long-input">
                <Input clearable v-model="captchaCode" placeholder="请输入图形验证码" class="short-input" />
                <img class="captcha-img" :src="captcha" @click="$store.dispatch('login/getCaptcha')" />
            </div>
            <div class="long-input">
                <Input clearable v-model="code" placeholder="请输入短信验证码" class="short-input" />
                <Button type="primary" disabled v-show="showCode == 0">发送验证码</Button>
                <Button type="primary" @click="getCode" v-show="showCode == 1">发送验证码</Button>
                <Button type="primary" disabled v-show="showCode == 2">{{ count }}s后重新获取</Button>
            </div>

            <Button class="login-btn" type="primary" @click="login()" style="height: 46px; margin-bottom: 20px">登录</Button>
        </div>
        <div class="other-login">
            <a @click="goWechatLogin()">
                <img src="/img/09.login/weixin.png" alt />
                微信
            </a>
            <a class="go-register" @click="goRegister()">立即注册></a>
        </div>
    </div>
</template>

<script>
    import {
        memberMobileCode,
        authenticationMobile,
        getAddressThreelist, //请求地址数据
    } from "@/api/api_09login.js";
    import { CartMergeCart } from "@/api/api_04.shoppingCar.js";
    import { mapState, mapActions, mapMutations} from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import Cookies from "js-cookie";

    const TIME_COUNT = 60;
    export default {
        data() {
            return {
                mobile: "",
                captchaCode: "",
                code: "",
                count: "",
                timer: null,
                showCode: 0,
                tabErrorMessage: "",
            };
        },
        computed: {
            ...mapState("login", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "captcha",
            ]),
            ...mapActions("login", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "getCaptcha",
            ]),
        },
        created() {},
        activated() {
            this.$store.dispatch("login/getCaptcha");
        },
        methods: {
            ...mapMutations(["SET_AUTH"]),
            getCode() {
                memberMobileCode({
                        mobile: this.mobile,
                        captcha: this.captchaCode,
                        uuid: this.$store.state.login.uuid,
                        noPopupMessage: true,
                    })
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
                            this.$store.dispatch("login/getCaptcha");
                            this.tabErrorMessage = res.msg;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            login() {
                if (this.mobile.length == 0) {
                    this.tabErrorMessage = "请填写手机号";
                    return;
                } else if (
                    !/^[1](([3][0-9])|([4][0,1,4-9])|([5][0-3,5-9])|([6][2,5,6,7])|([7][0-8])|([8][0-9])|([9][0-3,5-9]))[0-9]{8}$/.test(
                        this.mobile
                    )
                ) {
                    this.tabErrorMessage = "请输入正确的手机号";
                    return;
                } else if (this.captchaCode.length == 0) {
                    this.tabErrorMessage = "请输入图形验证码";
                    return;
                } else if (this.code.length == 0) {
                    this.tabErrorMessage = "请输入短信验证码";
                    return;
                }
                authenticationMobile({
                        mobile: this.mobile,
                        code: this.code,
                        noPopupMessage: true,
                    })
                    .then((res) => {
                        console.log(res.code);
                        if (res.code !== 200) {
                            this.tabErrorMessage = res.msg;
                            console.log(res, res.msg, this.tabErrorMessage);
                            this.$store.dispatch("login/getCaptcha");
                        } else if (res.code == 200) {
                            // Cookies.set("auth", res.data.token);
                            this.SET_AUTH(res.data.token);
                            CartMergeCart({})
                                .then((res) => {
                                    if (Cookies.get("rediskey") !== undefined) {
                                        Cookies.removeItem("rediskey");
                                    }
                                    if (this.$route.name !== "login") {
                                            
                                        window.location.href = window.location.href;
                                        location.reload();
                                    } else {
                                        this.goPage("/");
                                    }
                                })
                                .catch((err) => console.log(err));
                        }
                    })
                    .catch((err) => console.log(err));
            },
            goWechatLogin() {
                var redirect_uri = encodeURIComponent(
                    window.location.protocol +
                    "//" +
                    window.location.host +
                    "/login"
                );
                var page_url =
                    "https://open.weixin.qq.com/connect/qrconnect?appid=wx2a06205c11b6d5ad&redirect_uri=" +
                    redirect_uri +
                    "&response_type=code&scope=snsapi_login#wechat_redirect";
                location.href = page_url;
            },
            goPage(path) {
                this.$router.push({
                    path: path,
                });
            },
            goRegister() {
                if (this.$route.name !== "Login") {
                    window.open(
                        window.location.protocol +
                        "//" +
                        window.location.host +
                        "/register"
                    );
                } else {
                    this.$router.push({
                        path: "/register",
                    });
                }
            },
        },
        watch: {
            mobile(val, oldval) {
                if (val.length == 11 && this.captchaCode.length > 0) {
                    this.showCode = 1;
                }
            },
            captchaCode(val, oldval) {
                if (val.length > 0 && this.mobile.length == 11) {
                    this.showCode = 1;
                }
            },
        },
    };
</script>

<style lang="scss" scoped>
    .password-login {
        .input-body {
            padding: 0 40px;
            border-bottom: 1px solid #f5f5f5;

            .login-btn {
                background-color: #dd2619;
                border-color: #dd2619;
            }
        }

        .error-message {
            width: 100%;
            height: 22px;
            margin-bottom: 6px;

            .messagbe-content {
                background: #fff0f0;
                padding-left: 11px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #da493f;
                line-height: 22px;
            }
        }

        .long-input {
            width: 100%;
            margin: 0 0 20px 0;
            display: flex;
            justify-content: space-between;

            /deep/ .ivu-input-icon {
                line-height: 40px;
            }

            /deep/ .ivu-input {
                height: 40px;
                padding: 9px 11px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #222222;
                line-height: 18px;
            }

            /deep/ input::-webkit-input-placeholder {
                /* WebKit browsers */
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 18px;
            }

            /deep/ input::-moz-placeholder {
                /* Mozilla Firefox 4 to 18 */
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 18px;
            }

            /deep/ input::-moz-placeholder {
                /* Mozilla Firefox 19+ */
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 18px;
            }

            /deep/ input::-ms-input-placeholder {
                /* Internet Explorer 10+ */
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 18px;
            }

            /deep/ .ivu-input:focus {
                height: 40px;
                font-size: 14px;
                border: 1px solid #b7b7b7;
                box-shadow: none;
            }

            /deep/ .ivu-input:hover {
                height: 40px;
                font-size: 14px;
                border: 1px solid #b7b7b7;
                box-shadow: none;
            }

            .short-input {
                width: 54%;
                overflow: hidden;
            }

            /deep/ .ivu-btn-primary {
                width: 40% !important;
                background-color: #dddddd;
                font-size: 16px;
            }

            /deep/ .ivu-btn-primary:nth-of-type(2) {
                width: 40% !important;
                background-color: #dd2619;
            }

            img {
                width: 40%;
                height: 40px;
                cursor: pointer;
            }
        }

        /deep/ .ivu-btn-primary {
            width: 325px;
            height: 40px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 40px;
        }

        .forget-password {
            font-size: 14px;
            color: #666;
            line-height: 53px;
            text-align: right;
            display: block;
        }

        .other-login {
            padding: 0 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            a {
                margin-top: 11px;
                color: #333;
                display: flex;
                align-items: center;

                img {
                    margin: 0 10px 0 0;
                }
            }

            .go-register {
                color: #dd2619;
            }
        }
    }
</style>
