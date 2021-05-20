<template>
    <!-- 新用户注册 -->
    <div class="newUserRegister">
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
                <div class="text">新用户注册</div>
                <Input
                    clearable
                    :maxlength="10"
                    v-model="memberName"
                    placeholder="设置用户名称"
                    class="long-input"
                />
                <Input
                    clearable
                    v-model="memberMobile"
                    placeholder="输入手机号码"
                    class="long-input"
                />
                <div class="long-input">
                    <Input
                        clearable
                        v-model="captchaCode"
                        placeholder="请输入图形验证码"
                        class="short-input"
                    />
                    <img
                        :src="captcha"
                        @click="$store.dispatch('register/getCaptcha')"
                        style="height: 40px"
                    />
                </div>
                <div class="long-input">
                    <Input
                        clearable
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
                <Input
                    v-model="memberPasswd"
                    type="password"
                    :maxlength="20"
                    password
                    placeholder="设置6至20位密码"
                    class="long-input"
                />
                <Input
                    v-model="reMemberPasswd"
                    type="password"
                    :maxlength="20"
                    password
                    placeholder="请再次输入登录密码"
                    class="long-input"
                />
                <div class="contentTitle">
                    <!-- <span>点击注册，表示您同意<router-link to="/treaty">《用户服务协议》</router-link></span> -->
                    <span
                        >点击注册，表示您同意
                        <span
                            style="
                                color: #0072ff;
                                font-size: 14px;
                                cursor: pointer;
                            "
                            @click="isShowModal = true"
                            >《雷铭商城用户协议》</span
                        ></span
                    >
                </div>
                <div class="contentBtn" @click="register()">
                    <Button type="primary">同意协议并注册</Button>
                </div>
            </div>
        </div>
        <div class="content-footer">
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

        <Modal
            class="users-agreement"
            width="950px"
            v-model="isShowModal"
            title="雷铭商城用户协议"
            :footer-hide="true"
            :transfer="false"
        >
            <div class="users-agreement-body">
                <vue-scroll :ops="ops">
                    <div v-html="noticeProtocol.data.docContent"></div>
                </vue-scroll>
            </div>
            <div class="contentBtn" @click="register()">
                <Button type="primary">同意协议并注册</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
    import logoImg from "@/components/common/logoImg.vue";
    import security from "@/utils/security.js";
    // import JsEncrypt from "jsencrypt";
    import { registerMobileCode, memberRegister } from "@/api/api_10register.js";
    import { mapState, mapActions, mapGetters } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    const TIME_COUNT = 60;
    export default {
        head() {
            return {
                title: "注册",
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
                //弹窗vue-scroll
                ops: {
                    vuescroll: {},
                    scrollPanel: {},
                    rail: {
                        background: "#ccc",
                        opacity: 0,
                        size: "10px",
                        specifyBorderRadius: false,
                        gutterOfEnds: null, //轨道距 x 和 y 轴两端的距离
                        gutterOfSide: "0", //距离容器的距离
                        keepShow: false, //是否即使 bar 不存在的情况下也保持显示
                        border: "none", //边框
                    },
                    bar: {
                        hoverStyle: true,
                        onlyShowBarOnScroll: false, //是否只有滚动的时候才显示滚动条
                        background: "#f3f3f3", //颜色
                    },
                },
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
                disabled: false,
                memberName: "",
                memberMobile: "",
                captchaCode: "",
                checkValidCode: "",
                memberPasswd: "",
                reMemberPasswd: "",
                count: "",
                timer: null,
                showCode: 0,
                isShowModal: false,
            };
        },
        components: {
            logoImg,
        },
        mounted() {
            this.$store.dispatch("register/getCaptcha");
            this.$store.dispatch("register/getNoticeProtocol");
        },
        computed: {
            ...mapState("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "captcha",
                "noticeProtocol",
            ]),
            ...mapActions("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "getCaptcha",
            ]),
            ...mapGetters("register", ["aptchaData"]),
        },
        methods: {
            getCode() {
                registerMobileCode({
                    mobile: this.memberMobile,
                    captcha: this.captchaCode,
                    uuid: this.$store.state.register.uuid,
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
                            this.$store.dispatch("register/getCaptcha");
                            this.$store.dispatch("register/getNoticeProtocol");
                        }
                    })
                    .catch((err) => console.log(err));
            },
            register() {
                this.isShowModal = false;
                const JsEncrypt = require("jsencrypt");
                // var c = /^[a-zA-Z]$/
                let publicKey = security.publicKey;
                var encrypt = new JSEncrypt();
                encrypt.setPublicKey(publicKey);
                if (this.memberName.length == 0) {
                    this.$Message.info("请输入用户名");
                } else if (!/^[a-zA-Z]/.test(this.memberName)) {
                    this.$Message.info("用户名需以字母开头");
                } else if (this.memberMobile.length == 0) {
                    this.$Message.info("请输入手机号码");
                } else if (this.captchaCode.length == 0) {
                    this.$Message.info("请输入图形验证码 ");
                } else if (this.checkValidCode.length == 0) {
                    this.$Message.info("请输入短信验证码");
                } else if (this.memberPasswd.length == "") {
                    this.$Message.info("密码不能为空");
                } else if (this.memberPasswd.length < 6) {
                    this.$Message.info("登录密码不能小于6位");
                }

                // else if (this.memberPasswd.length > 20) {
                //     this.$Message.info('密码位数不大于20位');
                // }
                else if (this.memberPasswd !== this.reMemberPasswd) {
                    this.$Message.info("两次密码输入不一致");
                } else {
                    memberRegister({
                        memberName: this.memberName,
                        memberMobile: this.memberMobile,
                        checkValidCode: this.checkValidCode,
                        memberSource: 0,
                        memberPasswd: encrypt.encrypt(this.memberPasswd),
                        loginType: 2,
                    })
                        .then((res) => {
                            if (res.code == 200) {
                                this.$router.push({
                                    name: "successRegister",
                                });
                            }
                        })
                        .catch((err) => console.log(error));
                }
            },
            // close(data) {
            //     if (data == 1) {
            //         this.isShowModal = true
            //     } else {
            //         this.isShowModal = false
            //     }
            // },
        },
        watch: {
            memberMobile(val, oldval) {
                if (val.length == 11 && this.captchaCode.length > 0) {
                    this.showCode = 1;
                }
            },
            captchaCode(val, oldval) {
                if (val.length > 0 && this.memberMobile.length == 11) {
                    this.showCode = 1;
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    // 新用户注册页面
    .newUserRegister {
        //顶部部分
        .home {
            width: 100%;
            background: #fff;
            background: #ffffff;
            box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.1);

            .home-head {
                width: 1200px;
                height: 100px;
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
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #666666;
                    line-height: 16px;

                    a {
                        color: #dd2619;
                    }
                }
            }
        }

        .register {
            margin: 0 0 150px;
        }

        .content {
            width: 979px;
            margin: 0 auto;
            text-align: center;

            .text {
                margin: 80px auto 40px auto;
                font-size: 20px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #222222;
                line-height: 20px;
            }

            .long-input {
                width: 348px;
                margin: 20px auto 0 auto;
                overflow: hidden;
                display: flex;
                justify-content: space-between;

                /deep/ .ivu-input-icon-clear {
                    line-height: 40px;
                }

                /deep/ .ivu-icon-ios-eye-outline,
                /deep/ .ivu-icon-ios-eye-off-outline {
                    line-height: 40px;
                }

                /deep/ .ivu-icon {
                    cursor: pointer;
                }

                /deep/ .ivu-input {
                    height: 40px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #222222;
                    line-height: 14px;
                    padding: 4px 12px;
                }

                .short-input {
                    width: 55%;
                    overflow: hidden;
                }

                /deep/ .ivu-btn-primary:nth-of-type(2),
                .ivu-btn-primary:nth-of-type(3) {
                    width: 137px;
                    height: 40px;
                    background: #3e3d46;
                    border-radius: 2px;
                    border: 0px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #ffffff;
                    line-height: 40px;
                }

                /deep/ .ivu-btn-primary {
                    width: 137px;
                    height: 40px;
                    background-color: #dddddd;
                    border-radius: 2px;
                    border: 0px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #ffffff;
                    line-height: 40px;
                }

                img {
                    width: 40%;
                }

                /deep/ .ivu-input:hover {
                    height: 40px;
                    font-size: 14px;
                    border: 1px solid #b7b7b7;
                    box-shadow: none;
                }

                /deep/ .ivu-input:focus {
                    height: 40px;
                    font-size: 14px;
                    border: 1px solid #b7b7b7;
                    box-shadow: none;
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

                /deep/ .ivu-input-suffix {
                    .ivu-icon {
                        font-size: 20px;
                    }
                }
            }

            .contentTitle {
                width: 348px;
                margin: 13px auto 70px auto;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #666666;
                line-height: 14px;
                text-align: left;
            }
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

        .content-footer {
            width: 100%;
            height: 98px;
            background-color: #ffffff;
            border-top: 1px solid #cccccc;
            position: fixed;
            bottom: 0px;
            z-index: 999;
            overflow: hidden;

            .friend-link {
                display: flex;
                align-items: flex-end;
                width: 908px;
                height: 43px;
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
                float: left;
            }

            .link-item {
                padding: 0px 15px;
                font-size: 13px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #222222;
                line-height: 13px;
                cursor: pointer;
            }

            .link-last-item {
                border: none;
            }

            .copyright {
                width: 100%;
                margin-top: 20px;
                font-size: 13px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #222222;
                line-height: 13px;
                text-align: center;
            }
            .copyright a {
              color: #222222;
             }
        }

        .users-agreement {
            /deep/ .ivu-modal-mask {
                background-color: rgba(0, 0, 0, 0.14);
            }

            /deep/ .ivu-modal {
                top: 172px;
            }

            /deep/ .ivu-modal-content {
                padding-bottom: 10px;
                border-radius: 0px;
                position: relative;
                box-shadow: 0 0 0;
                border: 4px solid rgba(51, 51, 51, 0.08);

                /deep/ .ivu-modal-close {
                    position: absolute;
                    top: 0;
                }

                /deep/ .ivu-modal-header {
                    height: 34px;
                    background: #f3f3f3;
                    padding: 0 20px;
                    border-top-left-radius: 0px;
                    border-top-right-radius: 0px;

                    .ivu-modal-header-inner {
                        height: 34px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: #222222;
                        line-height: 34px;
                    }
                }

                .ivu-modal-body {
                    padding: 17px 20px 40px 20px;

                    .users-agreement-body {
                        height: 407px;
                    }
                }
            }
        }
    }
</style>
