<template>
    <div>
        <Steps :current="current">
            <step content="填写账户号"></step>
            <step content="验证身份"></step>
            <step content="设置新密码"></step>
            <step content="完成"></step>
        </Steps>
        <div class="content">
            <div class="forgot">
                <p class="forgot-text">新的密码：</p>
                <i-Input clearable type="password" :maxlength="20" v-model="newPwd" placeholder="设置6至16位登录密码"></i-Input>
            </div>
            <div class="forgot">
                <p class="forgot-text">重复密码：</p>
                <i-Input clearable type="password" :maxlength="20" v-model="ConfirmPwd" placeholder="请再次输入登录密码"></i-Input>
            </div>
        </div>
        <Button type="primary" :disabled="disabled" @click="clickCurrent">下一步</Button>
    </div>
</template>
<script>
    import security from "@/utils/security.js";
    // import JsEncrypt from "jsencrypt";
    import { forgetPassword } from "@/api/api_11forgotMessage";
    export default {
        data() {
            return {
                current: 3,
                newPwd: "",
                ConfirmPwd: "",
                disabled: true,
            };
        },
        props: {
            mobile: {
                type: String,
            },
        },
        methods: {
            clickCurrent() {
                const JSEncrypt = require("jsencrypt");
                let publicKey = security.publicKey;
                var encrypt = new JSEncrypt.JSEncrypt();
                encrypt.setPublicKey(publicKey);
                if (this.newPwd !== this.ConfirmPwd) {
                    this.$Message.warning("两次密码输入不一致");
                    return;
                }
                forgetPassword({
                        mobile: this.mobile,
                        newPwd: encrypt.encrypt(this.newPwd),
                        ConfirmPwd: encrypt.encrypt(this.ConfirmPwd),
                    })
                    .then((res) => {
                        if (res.code !== 200) {
                            this.$Message.info(res.msg);
                        } else {
                            this.$emit("childFn", "forgotCurrent04");
                        }
                    })
                    .catch((err) => console.log(err));
            },
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
    };
</script>
<style lang="scss" scoped>
    .content {
        padding: 0 62px;
    }

    .forgot {
        margin: 0 auto 24px;
        display: flex;

        .forgot-text {
            width: 72px;
            min-width: 72px;
            height: 40px;
            line-height: 40px;
            font-size: 14px;
            color: #222;
            margin: 0 10px 0;
        }
    }

    /deep/ .ivu-steps {
        margin: 0 0 44px;
    }

    /deep/ .ivu-btn-primary {
        margin: 0 166px;
    }

    /deep/ .ivu-input-icon {
        line-height: 52px;
        right: 10px;
    }

    /deep/ .ivu-steps-content {
        font-size: 18px;
        color: #999999;
    }

    // /deep/ .ivu-steps-item {
    //     position: relative;
    //     height: 51px;
    //     overflow: visible;
    // }
    // /deep/ .ivu-steps-content {
    //     width: 160px;
    //     text-align: center;
    //     position: absolute;
    //     left: -100px;
    // }
</style>