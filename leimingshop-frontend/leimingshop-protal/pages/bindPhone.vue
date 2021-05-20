<template>
    <div>
        <div class="home">
            <div class="home-head">
                <logoImg></logoImg>
                <span class="home-head-right"
                    >已有账号，<router-link to="/login"
                        >立即登录</router-link
                    ></span
                >
            </div>
        </div>
        <div class="register">
            <div class="content">
                <div class="text contentTitle">手机号绑定</div>
                <Input
                    v-model="memberMobile"
                    placeholder="输入手机号码"
                    class="long-input"
                />
                <div class="long-input">
                    <Input
                        v-model="checkValidCode"
                        placeholder="请输入短信验证码"
                        class="short-input"
                    />
                    <Button type="primary" disabled v-show="showCode == 0"
                        >发送验证码</Button
                    >
                    <Button
                        type="primary"
                        @click="getCode"
                        v-show="showCode == 1"
                        >发送验证码</Button
                    >
                    <Button type="primary" disabled v-show="showCode == 2"
                        >{{ count }}s后重新获取</Button
                    >
                </div>
                <div class="contentTitle">
                    <span
                        >点击绑定，表示您同意<router-link to="/treaty"
                            >《用户服务协议》</router-link
                        ></span
                    >
                </div>
                <div class="contentBtn" @click="register()">
                    <Button type="primary">同意协议并绑定账号</Button>
                </div>
            </div>
        </div>
        <div class="friend-link">
            <div class="friend-link-item">
                <ul>
                    <li v-for="(link, index) in moreLink" :key="index">
                        <span
                            :class="[
                                linkItemClass,
                                { 'link-last-item': index === 9 },
                            ]"
                            >{{ link }}</span
                        >
                    </li>
                </ul>
            </div>
        </div>
        <div class="copyright">
            <p>Copyright&nbsp;&nbsp;©2018&nbsp;&nbsp;北京雷铭智信科技有限公司 &nbsp; 本公司保留一切版权所有 &nbsp;|&nbsp;<a href="http://beian.miit.gov.cn/" target="_blank">京ICP备12000912号-1</a></p>
        </div>
    </div>
</template>
<script>
    import security from "@/utils/security.js";
    import JsEncrypt from "jsencrypt";
    import logoImg from "@/components/common/logoImg.vue";
    import { registerMobileCode, memberRegister } from "@/api/api_10register.js";
    import { mapState, mapGetters, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import Cookies from "js-cookie";
    const TIME_COUNT = 60;
    export default {
        inject: ["reload"],
        head() {
            return {
                title: "绑定手机号",
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
                dataList: {},
                linkItemClass: "link-item",
                moreLink: [
                    "关于我们",
                    "联系我们",
                    "联系客服",
                    "合作招商",
                    "商家帮助",
                    "营销中心",
                    "销售联盟",
                    "校园社区",
                    "风险监测",
                    "隐私政策",
                ],
                memberMobile: "",
                checkValidCode: "",
                count: "",
                timer: null,
                showCode: 0,
            };
        },
        components: {
            logoImg,
        },
        created() {
            this.$store.dispatch("register/getCaptcha");
        },
        computed: {
            ...mapGetters("login", ["codeState", "bindPhoneState"]),
        },
        methods: {
            ...mapActions("login", ["bindSendCode", "bindPhone"]),
            getCode() {
                this.bindSendCode(this.memberMobile);
            },
            timeClock() {
                if (!this.timer) {
                    this.count = TIME_COUNT;
                    this.showCode = 2;
                    this.timer = setInterval(() => {
                        if (this.count > 0 && this.count <= TIME_COUNT) {
                            this.count--;
                        } else {
                            this.showCode = 1;
                            clearInterval(this.timer);
                            this.timer = null;
                        }
                    }, 1000);
                }
            },
            register() {
                this.bindPhone({
                    mobile: this.memberMobile,
                    code: this.checkValidCode,
                });
            },
        },
        watch: {
            memberMobile(val, oldval) {
                if (val.length == 11) {
                    this.showCode = 1;
                }
            },
            codeState(val, oldval) {
                if (val.code !== 200) {
                    this.$Message.info(val.msg);
                } else {
                    this.timeClock();
                }
            },
            bindPhoneState(val, oldval) {
                if (val.code !== 200) {
                    this.$Message.info(val.msg);
                } else {
                    Cookies.set("auth", val.data.token);
                    this.$router.push({
                        name: "HomeIndex",
                    });
                    this.$nextTick(() => {
                        this.reload();
                    });
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    //顶部部分
    .home {
        width: 100%;
        background: #fff;
        box-shadow: 0px 2px 6px 0px rgba(221, 217, 217, 0.31);

        .home-head {
            width: 1200px;
            height: 110px;
            margin: 0 auto;
            display: flex;
            align-items: center;
            justify-content: space-between;
            overflow: hidden;

            .home-head-left {
                width: 219px;
                height: 59px;
            }

            .home-head-right {
                margin: 62px 0 24px 0;
                font-size: 17px;
                color: #666;

                a {
                    color: #dd2619;
                }
            }
        }
    }

    .register {
        padding: 90px 0 162px 0;
        background: #fbfbfb;
        border-bottom: 1px solid #cccccc;
    }

    .content {
        width: 979px;
        margin: 0 auto;
        background: #ffffff;
        text-align: center;
        padding: 87px 0 87px 0;
        border-top: 1px solid #d40d28;

        .text {
            font-size: 28px;
            font-weight: 600;
            color: #333333;
            text-align: center;
        }

        .long-input {
            width: 348px;
            margin: 0 auto 20px auto;
            display: flex;
            justify-content: space-between;

            /deep/ .ivu-input {
                height: 44px;
                font-size: 16px;
            }

            .short-input {
                width: 55%;
                overflow: hidden;
            }

            /deep/ .ivu-btn-primary {
                width: 40%;
                height: 44px;
            }

            img {
                width: 40%;
            }
        }
    }

    .contentTitle {
        width: 348px;
        margin: 0px auto 20px auto;
        text-align: left;
    }

    .contentBtn {
        width: 348px;
        height: 48px;
        margin: 0 auto;

        /deep/ .ivu-btn-primary {
            width: 348px;
            height: 48px;
            border-radius: 2px;
        }
    }

    .friend-link {
        display: flex;
        align-items: center;
        width: 908px;
        height: 30px;
        margin: 0px auto;
        color: #666;
        /* //   border-top: 1px solid #b2dfdb; */
    }

    .friend-link-item {
        margin: 0px auto;
    }

    .friend-link-item ul {
        list-style: none;
    }

    .friend-link-item li {
        padding: 5px 0px;
        float: left;
    }

    .link-item {
        padding: 0px 8px;
        cursor: pointer;
        border-right: 1px solid #ccc;
    }

    .link-last-item {
        border: none;
    }

    .copyright {
        width: 100%;
        color: #666;
        line-height: 30px;
        text-align: center;
    }
    .copyright a {
            color: #232323;
    }
</style>
