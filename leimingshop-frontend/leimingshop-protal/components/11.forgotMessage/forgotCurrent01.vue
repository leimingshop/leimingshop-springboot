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
                <p class="forgot-text">手机号</p>
                <i-Input v-model="mobile" placeholder="手机号码"></i-Input>
            </div>
            <div class="forgot">
                <p class="forgot-text">验证码</p>
                <i-Input v-model="captchaCode" placeholder="输入图形验证码"></i-Input>
            </div> -->
            <Form ref="forgotForm" :model="forgotForm" :label-colon="false" :rules="ruleValidate" label-position="right" :label-width="300">
                <FormItem label="手机号：" prop="mobile">
                    <Input clearable v-model="forgotForm.mobile" :maxlength="11" placeholder="请填写正确的11位手机号码" />
                </FormItem>
                <FormItem label="验证码：" prop="captchaCode">
                    <Input clearable v-model="forgotForm.captchaCode" :maxlength="5" placeholder="请填写正确的验证码" />
                </FormItem>
            </Form>
            <div class="fromFoot">
                <img :src="$store.getters['register/aptchaData']" class="fromFootP" />
                <p>
                    看不清？<a @click="changeCaptcha()" style="color: #d40d2b">换一张</a>
                </p>
            </div>
        </div>

        <Button type="primary" @click="clickCurrent('forgotForm')">下一步</Button>
    </div>
</template>
<script>
    import { verifyRegister, verifyCaptcha } from "@/api/api_11forgotMessage";
    import { mapState, mapActions, mapGetters } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        data() {
            const validatePhone = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error("手机号不能为空"));
                } else if (!/^1[3456789]\d{9}$/.test(value)) {
                    callback("手机号格式不正确");
                } else {
                    callback();
                }
            };
            const validateCaptchaCode = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error("验证码不能为空"));
                } else {
                    callback();
                }
            };
            return {
                current: 1,
                forgotForm: {
                    mobile: "",
                    captchaCode: "",
                },
                disabled: true,
                ruleValidate: {
                    mobile: [
                    {
                        required: true,
                        validator: validatePhone,
                        trigger: "blur",
                    }, ],
                    captchaCode: [
                    {
                        required: true,
                        validator: validateCaptchaCode,
                        trigger: "blur",
                    }, ],
                },
            };
        },
        computed: {
            ...mapState("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "captcha",
            ]),
            ...mapGetters({
                aptchaData: "register/aptchaData",
            }),
        },
        created() {
            this.changeCaptcha();
            console.log(this.aptchaData);
        },
        methods: {
            ...mapActions("register", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "getCaptcha",
            ]),
            changeCaptcha() {
                this.$store.dispatch("register/getCaptcha");
                console.log("更换图片");
            },
            clickCurrent(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        verifyRegister({
                                memberMobile: this.forgotForm.mobile,
                                loginType: 2,
                            })
                            .then((response) => {
                                if (response.code == 200) {
                                    verifyCaptcha({
                                            uuid: this.$store.state.register.uuid,
                                            captcha: this.forgotForm.captchaCode,
                                        })
                                        .then((res) => {
                                            if (res.code == 500) {
                                                this.$Message.info("请输入验证码");
                                            } else if (res.code == 200) {
                                                var dataObj = {
                                                    componentName: "forgotCurrent02",
                                                    mobile: this.forgotForm.mobile,
                                                    username: response.data.nickName,
                                                };
                                                this.$emit("childFn", dataObj);
                                            } else {
                                                this.changeCaptcha();
                                            }
                                        })
                                        .catch((err) => console.log(err));
                                }
                            })
                            .catch((err) => console.log(err));
                    } else {
                        this.$Message.error("请将信息补充完整");
                    }
                });
                // verifyRegister({
                //     memberMobile: this.mobile,
                //     loginType: 2
                // }).then(response => {
                //     if (response.code == 200) {
                //         verifyCaptcha({
                //             uuid: this.$store.state.register.uuid,
                //             captcha: this.captchaCode
                //         }).then(res => {
                //             if (res.code == 500) {
                //                 this.$Message.info('请输入验证码');
                //             } else if (res.code == 200) {
                //                 var dataObj = {
                //                     componentName: 'forgotCurrent02',
                //                     mobile: this.mobile,
                //                     username: response.data.nickName
                //                 }
                //                 this.$emit('childFn', dataObj)
                //             } else {
                //                 this.changeCaptcha()
                //             }
                //         }).catch(error => {
                //             console.log(error)
                //         })
                //     }
                // }).catch(error => {
                //     console.log(error)
                // })
            },
        },
        watch: {
            mobile(val, oldval) {
                if (val.length == 11 && this.captchaCode.length > 0) {
                    this.disabled = false;
                } else {
                    this.disabled = true;
                }
            },
            captchaCode(val, oldval) {
                if (val.length > 0 && this.mobile.length == 11) {
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

    /deep/ .ivu-form-item:after {
        width: 0px !important;
    }

    /deep/ .ivu-form-item-label {
        width: 60px !important;
        margin: 0 10px 0 0 !important;
        text-align: left;
    }

    /deep/ .ivu-form-item-label:before {
        content: none;
    }

    /deep/ .ivu-form-item-content {
        margin-left: 0 !important;

        .ivu-input {
            width: 350px;
        }
    }

    /deep/ .ivu-btn-primary {
        font-size: 16px;
    }

    /deep/ .ivu-steps-content {
        font-size: 18px;
        color: #999999;
    }

    /deep/ .ivu-steps-item.ivu-steps-status-process .ivu-steps-content {
        color: #999999;
    }

    .forgot {
        margin: 0 auto 24px;
        display: flex;

        .forgot-text {
            width: 54px;
            min-width: 54px;
            height: 52px;
            line-height: 52px;
            font-size: 14px;
            color: #222;
            margin: 0 10px 0;
        }
    }

    .fromFoot {
        height: 40px;
        line-height: 40px;
        margin: 0 57px 30px 71px;
        text-align: center;
        display: flex;

        .fromFootP {
            width: 162px;
            height: 40px;
            background: #ffffff;
            border: 1px solid rgba(204, 204, 204, 1);
        }

        p {
            font-size: 14px;
            color: #3e3d46;
            margin: 0 0 0 10px;

            span {
                color: #d40d2b;
            }
        }
    }

    /deep/ .ivu-steps {
        margin: 0 0 44px;
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