<template>
    <div>
        <Steps :current="current">
            <Step content="填写账户号"></Step>
            <Step content="验证身份"></Step>
            <Step content="设置新密码"></Step>
            <Step content="完成"></Step>
        </Steps>
        <div class="content">
            <!-- <div class="forgot">
                <p class="forgot-text">用户名</p>
                <i-Input v-model="username" placeholder="用户名" disabled></i-Input>
            </div> -->
            <div class="forgot">
                <p class="forgot-text">手机号：</p>
                <i-Input v-model="mobile" placeholder="手机号码" disabled></i-Input>
            </div>
            <div class="forgot">
                <p class="forgot-text">验证码：</p>
                <div class="forgot-foot">
                    <i-Input clearable v-model="code" placeholder="短信验证码"></i-Input>
                    <Button type="primary" class="formBtn" @click="getCode()" v-show="showCode == 1">发送验证码</Button>
                    <Button type="primary" class="formBtn" disabled v-show="showCode == 2">{{ count }}s后重新获取</Button>
                </div>
            </div>
        </div>
        <Button type="primary" :disabled="disabled" @click="clickCurrent" style="background: #d40d2b">下一步</Button>
    </div>
</template>
<script>
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import { mobileCode, registerCompareCode } from "@/api/api_11forgotMessage";
    const TIME_COUNT = 60;
    export default {
        data() {
            return {
                current: 2,
                code: "",
                count: "",
                timer: null,
                showCode: 1,
                disabled: true,
            };
        },
        props: ["mobile", "username"],
        computed: {
            ...mapState("forgotMessage", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "mobileCode",
            ]),
            ...mapActions("forgotMessage", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "getPhoneCode",
            ]),
        },
        methods: {
            clickCurrent() {
                registerCompareCode({
                        mobile: this.mobile,
                        code: this.code,
                    })
                    .then((res) => {
                        if (res.code == 200) {
                            this.$emit("childFn", "forgotCurrent03", this.mobile);
                        }
                    })
                    .catch((err) => console.log(error));
            },
            getCode() {
                mobileCode({
                        mobile: this.mobile,
                    })
                    .then((res) => {
                        if (res.code == 200) {
                            this.$Message.success("验证码已发送");
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
                        } else {}
                    })
                    .catch((err) => console.log(err));
            },
        },
        watch: {
            code(val, oldval) {
                if (val.length > 0) {
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
        flex-wrap: wrap;

        .forgot-text {
            width: 56px;
            min-width: 54px;
            height: 40px;
            line-height: 40px;
            font-size: 14px;
            color: #222;
            margin: 0 10px 0;
        }

        .forgot-foot {
            width: 348px;
            display: flex;

            /deep/ .ivu-input {
                width: 200px;
                padding: 4px 12px;
            }

            .formBtn {
                width: 197px;
                height: 40px;
                margin: 0 0 0 11px;
                background: #ffffff;
                border-color: #cccccc;
                color: #999999;
            }

            // .ivu-btn-primary[disabled]{
            //     color: #ffffff;
            //     // background: #D40D2B;
            //     font-size: 18px;
            // }
        }

        /deep/ .ivu-input-wrapper {
            width: 200px;
        }
    }

    /deep/ .ivu-steps {
        margin: 0 0 44px;
    }

    /deep/ .ivu-input-icon {
        line-height: 52px;
        right: 10px;
    }

    /deep/ .ivu-steps-content {
        font-size: 18px;
        color: #999999 !important;
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