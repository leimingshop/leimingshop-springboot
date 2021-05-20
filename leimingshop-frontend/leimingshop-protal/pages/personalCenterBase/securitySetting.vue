<template>
    <div>
        <div class="securityTop">
            <span>您当前的账号:{{ memberName }}</span>
        </div>
        <div class="secrity">
            <!--登录密码-->
            <div class="securityContent">
                <div class="securityOut">
                    <img
                        class="securityImg"
                        src="/img/06.personalCenter/securityPassword@2x.png"
                        alt=""
                    />
                    <div class="securityCenter">
                        <p class="securityText">登录密码</p>
                        <p class="securityWord">
                            建议您定期更改密码，设置安全性高的密码可以使账号更安全
                        </p>
                    </div>
                </div>
                <div class="securityOn">
                    <Button class="securityBtn" @click="modal = true"
                        >修改</Button
                    >
                </div>
            </div>
            <Modal v-model="modal" title="修改密码" @on-cancel="cancel">
                <div style="margin: 0 0 24px; display: flex">
                    <span class="modalWord">原始密码：</span>
                    <Input
                        type="password"
                        :maxlength="16"
                        v-model="oldPwd"
                        placeholder="请输入原密码"
                    ></Input>
                </div>
                <div style="margin: 0 0 24px; display: flex">
                    <span class="modalWord">新密码：</span>
                    <Input
                        type="password"
                        v-model="newPwd"
                        :maxlength="16"
                        placeholder="设置6至16位登录密码"
                    ></Input>
                </div>
                <div style="margin: 0 0 24px; display: flex">
                    <span class="modalWord">重复密码：</span>
                    <Input
                        type="password"
                        v-model="ConfirmPwd"
                        :maxlength="16"
                        placeholder="请再次输入登录密码"
                    ></Input>
                </div>
                <div slot="footer">
                    <Button
                        type="error"
                        size="large"
                        long
                        :loading="loading"
                        @click="cancel"
                        class="footer-btn"
                        >取消</Button
                    >
                    <Button
                        type="error"
                        size="large"
                        long
                        :loading="loading"
                        @click="StartChange"
                        class="determine"
                        >确定</Button
                    >
                </div>
            </Modal>
            <!--修改手机号-->
            <div class="securityContent">
                <div class="securityOut">
                    <img
                        class="securityImg"
                        src="/img/06.personalCenter/securityPhone@2x.png"
                        alt=""
                    />
                    <div class="securityCenter">
                        <p class="securityText">安全手机</p>
                        <p class="securityWord">
                            安全手机可以用于登录账户，重置密码或其他安全验证
                        </p>
                    </div>
                </div>
                <div class="securityOn">
                    <Button
                        class="securityBtn"
                        @click="$router.push('/personalCenterBase/changePhone')"
                        >更换</Button
                    >
                </div>
            </div>
            <!--修改邮箱-->
            <div class="securityContent">
                <div class="securityOut">
                    <img
                        class="securityImg"
                        src="/img/06.personalCenter/securityEmail@2x.png"
                        alt=""
                    />
                    <div class="securityCenter">
                        <p class="securityText">绑定邮箱</p>
                        <p class="securityWord">
                            验证后，可用于快速找回登录密码，接收重要消息提醒
                        </p>
                    </div>
                </div>
                <div class="securityOn">
                    <!-- email   "" -->
                    <Button
                        class="securityBtn"
                        v-if="email != null && email != ''"
                        @click="$router.push('/changeEmail')"
                        >更换</Button
                    >
                    <Button
                        class="securityBtn"
                        v-if="email == null || email == ''"
                        @click="$router.push('/personalCenterBase/bindEmail')"
                        >绑定</Button
                    >
                </div>
            </div>
            <!--微信绑定-->
            <div class="securityContent">
                <div class="securityOut">
                    <img
                        class="securityImg"
                        src="/img/06.personalCenter/securityWei@2x.png"
                        alt=""
                    />
                    <div class="securityCenter">
                        <p class="securityText">绑定微信</p>
                        <p class="securityWord">绑定微信可以用于扫码登录账号</p>
                    </div>
                </div>
                <div class="securityOn">
                    <Button class="securityBtn" @click="goWechatLogin()"
                        >绑定</Button
                    >
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import security from "@/utils/security.js";
    // import JsEncrypt from "jsencrypt";
    import { homeMember } from "@/api/api_06-08-01personalData.js";
    import { changePassword, mailInfo } from "@/api/api_06-10-01securitySetting.js";
    export default {
        head() {
            return {
                title: "安全设置",
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
                oldPwd: "",
                newPwd: "",
                ConfirmPwd: "",
                memberName: "",
                message: "",
                pwdFlag: "",
                email: "",
                modal: false,
                isFileImg: true,
                loading: false,
            };
        },
        props: {
            mobile: {
                type: String,
            },
        },
        created() {
            this.getHomeData();
            this.getEmail();
        },
        watch: {
            newPwd(val, oldval) {
                if (
                    val.length < 16 &&
                    val.length > 5 &&
                    this.ConfirmPwd.length < 16 &&
                    this.ConfirmPwd.length > 5
                ) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
            ConfirmPwd(val, oldval) {
                if (
                    val.length < 16 &&
                    val.length > 5 &&
                    this.newPwd.length < 16 &&
                    this.newPwd.length > 5
                ) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
        },
        methods: {
            getEmail() {
                mailInfo()
                    .then((res) => {
                        this.email = res.data;
                    })
                    .catch((err) => console.log(err));
            },
            clearInputOne(e) {
                // console.log(e.detail.value)
                // this.oldPwd = e.detail.value;
            },
            clearInputTwo(e) {
                // this.newPwd = e.detail.value;
            },
            clearInpuThr(e) {
                // this.ConfirmPwd = e.detail.value;
            },
            //更改密码弹窗
            StartChange() {
                const JSEncrypt = require("jsencrypt");
                let publicKey = security.publicKey;
                var encrypt = new JSEncrypt.JSEncrypt();
                encrypt.setPublicKey(publicKey);
                if (this.newPwd !== this.ConfirmPwd) {
                    this.$Message.error("请确认重复密码，重复密码与新密码不一致");
                    this.ConfirmPwd = [];
                    return;
                }
                changePassword({
                    oldPwd: encrypt.encrypt(this.oldPwd),
                    newPwd: encrypt.encrypt(this.newPwd),
                    ConfirmPwd: encrypt.encrypt(this.ConfirmPwd),
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.$Message.success(res.msg);
                            this.modal = false;
                            this.oldPwd = "";
                            this.newPwd = "";
                            this.ConfirmPwd = "";
                        } else {
                            //  this.$Message.info(res.msg);
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },

            cancel() {
                this.$Message.info("取消修改");
                this.oldPwd = "";
                this.newPwd = "";
                this.ConfirmPwd = "";
                this.modal = false;
            },

            //获取个人信息
            getHomeData(id) {
                var obj = {
                    id: id,
                };
                homeMember(obj)
                    .then((res) => {
                        this.memberName = res.data.memberName;
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
        },
    };
</script>
<style lang="scss" scoped>
    .securityTop {
        width: 948px;
        height: 55px;
        line-height: 55px;
        font-size: 14px;
        color: #333333;
        background: #ffffff;
        border-bottom: 1px solid #ebebeb;
        font-weight: 500;
        font-family: PingFangSC-Medium, PingFang SC;
        padding: 0 40px;
    }
    .secrity {
        width: 948px;
        height: 1005px;
        background: #ffffff;
        // margin: 13px 0 0 0;
        padding: 10px 40px;
    }
    .securityContent {
        height: 140px;
        display: flex;
        justify-content: space-between;
        // line-height: 120px;
        border-bottom: 1px solid #ebebeb;
        .securityOut {
            width: 450px;
            display: flex;
        }
        .securityImg {
            width: 38px;
            height: 38px;
            margin: 51px 20px 51px 0;
            border-radius: 6px;
        }
        .securityCenter {
            margin: 48px 0px 50px;
            .securityText {
                font-size: 14px;
                font-family: PingFangSC-Medium, PingFang SC;
                font-weight: 500;
                color: #222222;
                margin-bottom: 5px;
            }
            .securityWord {
                font-size: 12px;
                font-weight: 400;
                font-family: PingFangSC-Regular, PingFang SC;
                color: #999999;
                margin-top: 5px;
            }
        }
        .securityBtn {
            width: 100px;
            height: 40px;
            background: #ffffff;
            border-radius: 22px;
            border: 1px solid #999999;
            color: #666666;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            &:hover {
                border: 1px solid #dd2619;
                background: #dd2619;
                color: #ffffff;
            }
        }
    }
    .securityOn {
        margin: 48px 40px;
    }
    .securityFoot {
        width: 868px;
        height: 150px;
        margin: 600px auto 15px;
        padding: 20px;
        background: #ebebeb;
        .securition {
            font-size: 18px;
            color: #333333;
            font-weight: 500;
        }
        .securityFoot-ul {
            font-size: 13px;
            margin: 10px 0;
            color: #999999;
        }
    }
    .modalWord {
        width: 70px;
        font-size: 14px;
        color: #222222;
        font-weight: 400;
        font-family: PingFangSC-Regular, PingFang SC;
        text-align: right;
        line-height: 34px;
    }
    /deep/ .ivu-modal-header {
        padding: 31px 16px 22px;
        text-align: center;
        border-bottom: none;
    }
    /deep/ .ivu-modal-footer {
        padding: 0 0 45px;
        text-align: center;
        border-top: none;
    }
    /deep/ .ivu-modal {
        width: 390px !important;
        // height: 318px;
        // background: #FFFFFF;
        border-radius: 12px;
        border: 4px solid rgba(51, 51, 51, 0.08);
        top: 30%;
    }
    /deep/ .ivu-modal-header p,
    .ivu-modal-header-inner {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 600;
        color: #333333;
    }
    /deep/ .ivu-modal-body {
        width: 310px;
        padding: 0;
        margin: 0 auto;
    }
    /deep/ .ivu-input {
        height: 34px;
        padding: 4px 14px;
        border: 1px solid #cccccc;
        font-size: 12px !important;
        box-shadow: none !important;
        &:hover {
            box-shadow: none !important;
        }
    }
    /deep/ .ivu-input-wrapper {
        width: 239px;
        height: 34px;
    }
    .footer-btn {
        width: 60px;
        height: 30px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        background: #f6f6f6;
        border-radius: 2px;
        border: none;
    }
    /deep/ .ivu-modal-mask {
        opacity: 0.14 !important;
    }
    .determine {
        width: 60px;
        height: 30px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        color: #ffffff;
        font-weight: 400;
        border-radius: 2px;
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
    }
    /deep/ .ivu-modal-close .ivu-icon-ios-close {
        &:hover {
            color: #dd2619;
        }
    }
    /deep/ .oneStyle .ivu-badge-count {
        line-height: 16px !important;
    }
</style>
