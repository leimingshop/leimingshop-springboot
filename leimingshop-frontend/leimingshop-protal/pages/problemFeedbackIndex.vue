<template>
    <div class="problem-feedback">
        <div class="fill-feedback">
            <div class="profee-top">
                <div class="profee-top1">
                    <img
                        @click="$router.push('/')"
                        class="imgage"
                        :src="$imgURL + IndexSiteData.logoNow"
                        alt
                    />
                    <router-link
                        to="/"
                        tag="span"
                        style="
                            text-shadow: 0px 2px 6px rgba(221, 217, 217, 0.31);
                            color: rgba(221, 38, 25, 1);
                            cursor: pointer;
                            height: 24px;
                            line-height: 24px;
                            font-size: 17px;
                        "
                        >进入网站首页 ></router-link
                    >
                </div>
            </div>
            <div class="profee-bottom">
                <div class="profee-bottom1">
                    <!-- 面包屑 -->
                    <breadcrumb1 :list="breadcrumbList" />
                    <div class="profee-content" v-show="!feedbackSuccess">
                        <Form
                            ref="formInline"
                            :model="formInline"
                            :rules="ruleValidate"
                        >
                            <FormItem label="反馈类型" prop="feedbackType">
                                <Select
                                    v-model="formInline.feedbackType"
                                    style="width: 348px; height: 52px"
                                    placeholder="请选择反馈类型"
                                >
                                    <Option value="1">功能异常</Option>
                                    <Option value="2">优化建议</Option>
                                    <Option value="3">其他反馈</Option>
                                </Select>
                            </FormItem>
                            <FormItem
                                label="反馈内容"
                                prop="detailedDescription"
                            >
                                <div class="textarea">
                                    <Input
                                        v-model="formInline.detailedDescription"
                                        type="textarea"
                                        maxlength="500"
                                        show-word-limit
                                        :rows="6"
                                        :autosize="{ minRows: 7, maxRows: 7 }"
                                        placeholder="对我们网站、商品、服务，您有什么建议吗？你还希望我们为您提供哪些商品？请告诉我们... "
                                    ></Input>
                                </div>
                            </FormItem>
                            <!-- 上传图片部分 -->
                            <FormItem label="相关图片">
                                <!-- 外部定义 -->
                                <div class="personalContent-top">
                                    <!-- 主图可以添加多个图片 -->
                                    <div
                                        class="pictureShow"
                                        style="display: flex"
                                        v-viewer
                                    >
                                        <div :images="imgFiles" class="bigImg">
                                            <div
                                                class="moreImg"
                                                v-for="(imgsrc,
                                                index) in imgFiles"
                                                :key="index"
                                            >
                                                <a
                                                    class="delete-A"
                                                    @click.stop.prevent="
                                                        imgFiles.splice(
                                                            index,
                                                            1
                                                        );
                                                        imgSrcs.splice(
                                                            index,
                                                            1
                                                        );
                                                    "
                                                ></a>
                                                <img
                                                    class="indexImg"
                                                    :src="imgsrc"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 添加主图 -->
                                    <!-- v-show="imgFiles[index1].length<5" -->
                                    <div
                                        class="onContent"
                                        v-show="imgFiles.length < 5"
                                    >
                                        <Upload
                                            :max-size="50 * 1024"
                                            :format="['jpg', 'jpeg', 'png']"
                                            :on-exceeded-size="maxSize"
                                            :before-upload="handleUpload"
                                            action
                                        >
                                            <Button
                                                style="
                                                    width: 78px;
                                                    height: 78px;
                                                "
                                                @click="rememberIndex"
                                                >+</Button
                                            >
                                        </Upload>
                                    </div>
                                    <p
                                        style="
                                            display: inline-block;
                                            margin-left: 31px;
                                            margin-top: 50px;
                                        "
                                    >
                                        共
                                        <span style="color: red">{{
                                            imgFiles.length
                                        }}</span
                                        >张图片，还能上传
                                        <span style="color: red">{{
                                            5 - imgFiles.length
                                        }}</span
                                        >张
                                    </p>
                                </div>
                            </FormItem>

                            <FormItem label="手机号码" prop="mobileNumber">
                                <div class="phone-number">
                                    <Input
                                        v-model="formInline.mobileNumber"
                                        type="tel"
                                        placeholder="方便与您进行联系 "
                                        style="width: 348px; height: 52px"
                                        maxlength="11"
                                    />
                                    <span>
                                        <span
                                            style="
                                                height: 52px;
                                                margin-left: 20px;
                                                color: red;
                                            "
                                            >{{
                                                this.formInline.mobileNumber
                                                    .length
                                            }}</span
                                        >
                                        /
                                        <span style="color: red">11</span>
                                    </span>
                                </div>
                            </FormItem>
                        </Form>
                        <Button
                            style="
                                width: 145px;
                                height: 52px;
                                background: #dd2619;
                                font-size: 22px;
                                box-shadow: 0px 2px 4px 0px
                                    rgba(221, 38, 25, 0.38);
                                color: rgba(255, 255, 255, 1);
                                margin-left: 94px;
                                margin-top: 50px;
                                border: 0;
                            "
                            @click="handleSubmit('formInline')"
                            :disabled="disabled"
                            >提交反馈</Button
                        >
                    </div>

                    <!-- 反馈成功后跳出的弹窗 -->
                    <div class="feedsucc-wrapper" v-show="feedbackSuccess">
                        <div class="feedback-success">
                            <div class="feedback-success1">
                                <img
                                    src="/img/08.problemFeedback/success@1x.png"
                                    alt
                                />
                                <span>反馈提交成功</span>
                            </div>
                            <p>非常感谢您对雷铭商城的支持</p>
                            <router-link
                                class="feedback-success2"
                                tag="div"
                                to="/"
                                >进入首页</router-link
                            >
                        </div>
                    </div>
                </div>
            </div>
            <!-- 尾部 -->
            <div class="problem-footer">
                <ul>
                    <li>关于我们</li>
                    <li>帮助中心</li>
                    <li>售后服务</li>
                    <li>配送与验收</li>
                    <li>商务合作</li>
                    <li>企业采购</li>
                    <li>开发平台</li>
                    <li>搜索推荐</li>
                    <li>友情链接</li>
                </ul>
                <p>Copyright&nbsp;&nbsp;©2018&nbsp;&nbsp;北京雷铭智信科技有限公司 &nbsp; 本公司保留一切版权所有 &nbsp;|&nbsp;<a href="http://beian.miit.gov.cn/" target="_blank">京ICP备12000912号-1</a></p>
            </div>
        </div>
    </div>
</template>

<script>
    import { uploadPicBase64 } from "@/api/api_Common.js";
    import { saveFeedback } from "@/api/api_12problemFeedback.js";
    import { mapState, mapActions } from "vuex";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";

    export default {
        name: "problemFeedbackIndex",
        components: {
            breadcrumb1,
        },
        props: {},
        head() {
            return {
                title: "问题反馈",
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
            const validatePhone = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error("手机号不能为空"));
                } else if (!/^1[34578]\d{9}$/.test(value)) {
                    callback("手机号格式不正确");
                } else {
                    callback();
                }
            };
            return {
                isFileImg: true,
                addIndex: 0,
                feedbackSuccess: false,
                // 这里是需要给后端传递的参数
                formInline: {
                    feedbackType: "1", //类型
                    detailedDescription: "", //详细说明
                    imagesArr: "", //图片数组以逗号分隔开
                    mobileNumber: "", //手机号码
                },
                defaultList: [], //图片列表数据
                imgName: "",
                visible: false,
                imgFiles: [],
                imgSrcs: [],
                uploadList: [],
                validatePhone: "",
                disabled: false,
                ruleValidate: {
                    feedbackType: [
                        {
                            required: true,
                            message: "The name cannot be empty",
                            trigger: "blur",
                        },
                    ],
                    detailedDescription: [
                        {
                            required: true,
                            message: "请输入反馈内容",
                            trigger: "blur",
                        },
                        {
                            type: "string",
                            min: 5,
                            message: "反馈内容不能少于5个字",
                            trigger: "blur",
                        },
                    ],
                    mobileNumber: [
                        {
                            required: true,
                            trigger: "change blur",
                            validator: validatePhone,
                        },
                    ],
                },
                breadcrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "意见反馈",
                        toPath: "",
                    },
                ]),
            };
        },
        watch: {},
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "IndexSiteData",
            ]),
            ...mapActions("main", ["actIndexSite"]),
        },
        mounted() {
            this.actIndexSite();
        },
        methods: {
            // 提交反馈并且检验规则
            handleSubmit(name) {
                // 校验
                this.$refs[name].validate((valid) => {
                    console.log(valid);
                    if (valid) {
                        // 提交
                        this.disabled = true;
                        saveFeedback({
                            detailedDescription: this.formInline
                                .detailedDescription,
                            feedbackType: this.formInline.feedbackType * 1,
                            imagesArr: this.imgSrcs.join(","),
                            mobileNumber: this.formInline.mobileNumber,
                        })
                            .then((res) => {
                                // 回调
                                this.disabled = false;
                                if (res.code == 200) {
                                    this.$Message.success("我们已经收到您的反馈");
                                    this.feedbackSuccess = true;
                                }
                            })
                            .catch((error) => console.log(error));
                    } else {
                        this.$Message.error("请填写完整后提交");
                    }
                });
            },
            // 上传图片的相关事件 start
            rememberIndex(index) {
                this.addIndex = index;
            },
            maxSize(file) {
                this.$Message.success(`图片【${file.name}】大小超出5M，请重新上传`);
            },
            //处理图片转换成base64
            handleUpload(file) {
                let reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = (e) => {
                    this.baseFile = e.target.result;
                    this.picName = file.name;
                    this.isFileImg = !this.isFileImg;
                    this.uploadeImg();
                };
            },
            // 上传图片
            uploadeImg() {
                uploadPicBase64({
                    imgStr: this.baseFile,
                    pictureName: this.picName,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.imgFiles.push(this.baseFile);
                            this.imgSrcs.push(res.data.url);
                        }
                    })
                    .catch((err) => console.log(err));
                return false;
            },
        },
    };
</script>
<style lang="scss" scoped>
    .problem-feedback {
        width: 100%;
        margin: 0 auto;
        background: #fff;
    }

    .personal-center-nav {
        font-size: 13px;
        padding: 15px 0;
        color: rgba(58, 58, 58, 1);
        cursor: pointer;
    }

    .profee-top {
        width: 100%;
        background: #fff;
    }

    .profee-top1 {
        width: 1200px;
        background: #fff;
        margin: 25px auto;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .imgage {
            cursor: pointer;
        }

        a {
            color: red;
        }
    }

    .ivu-notice {
        top: 20%;
        left: 50%;
        transform: translateX(-50%);
    }

    .problem-feedback /deep/ {
        .ivu-form-item-error-tip {
            margin-top: 20px;
            margin-left: 98px;
        }

        .ivu-form-item-required:nth-child(1) {
            margin-bottom: 34px;

            .ivu-form-item-label {
                line-height: 32px;
            }
        }

        .ivu-form-item-required:nth-child(2) {
            margin-bottom: 39px;

            .ivu-form-item-label {
                line-height: 0;
            }

            .ivu-form-item-content {
                margin-bottom: 22px;

                .ivu-input-wrapper {
                    margin-top: 0px;
                }
            }
        }

        .ivu-form-item:nth-child(3) {
            margin-bottom: 35px;

            .ivu-form-item-label {
                line-height: 0px;
            }
        }

        .ivu-form-item-label {
            font-size: 18px;
            color: rgba(67, 67, 67, 1);
            line-height: 32px;
        }

        .textarea {
            .ivu-input-wrapper {
                width: 936px;
                height: 176px;
                margin-top: 18px;

                /deep/ textarea {
                    height: 176px;
                    max-height: 176px;
                    font-size: 16px;
                    text-align: justify;
                }

                span {
                    margin-bottom: 6px;
                    margin-right: 8px;
                }
            }
        }

        .ivu-select-selection {
            height: 52px;
            font-size: 16px;
            color: rgba(153, 153, 153, 1);

            .ivu-select-selected-value {
                height: 52px;
                line-height: 52px;
                font-size: 16px;
            }

            .ivu-select-placeholder {
                height: 52px;
                line-height: 52px;
            }
        }

        .phone-number {
            .ivu-input-wrapper {
                margin-top: 0;
                font-size: 16px;

                input {
                    height: 52px;
                }
            }
        }
    }

    .problem-feedback /deep/ .profee-bottom {
        width: 100%;
        margin: 0 auto;
        background: #f6f6f6;
        padding-bottom: 32px;

        .profee-bottom1 {
            width: 1200px;
            margin: 0 auto;
        }
    }

    .profee-content {
        width: 1200px;
        background: #fff;
        padding: 60px 101px 146px 61px;
    }

    // 图片添加
    .personalContent-top {
        display: flex;

        /deep/ .onContent {
            width: 78px;
            height: 78px;
        }

        .showOne {
            width: 78px;
            height: 78px;
            margin-right: 15px;
            border: 1px solid red;
            position: relative;
        }

        .showOne:first-child {
            margin-left: 12px;
        }

        .showOne:last-child {
            margin-right: 0;
        }

        .moreImg {
            position: relative;
            display: inline-block;
            width: 78px;
            height: 78px;
            margin-right: 15px;
            border: 1px solid #f5f5f5;
            border-radius: 2px;
            border: 1px solid rgba(245, 245, 245, 1);
            padding: 4px;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .moreImg:first-child {
            margin-left: 12px;
        }

        .moreImg .delete-A {
            width: 20px;
            height: 20px;
            display: block;
            position: absolute;
            top: -10px;
            right: -10px;
            background: url("/img/06.personalCenter/delete.png") no-repeat
                center/cover;
        }

        .moreImg .delete-A:hover {
            width: 20px;
            height: 20px;
            display: block;
            position: absolute;
            top: -10px;
            right: -10px;
            background: url("/img/06.personalCenter/delete_hover.png") no-repeat
                center/cover;
        }
    }

    // 图片上传样式开始
    /deep/ .ivu-upload-select {
        margin-left: 12px;
    }

    // 上传图片 start
    .demo-upload-list:first-child {
        margin-left: 10px;
    }

    .picture-content {
        margin-top: 14px;
    }

    .demo-upload-list {
        display: inline-block;
        width: 94px;
        height: 94px;
        padding: 4px;
        text-align: center;
        line-height: 60px;
        border: 1px solid #f5f5f5;
        border-radius: 4px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
        margin-right: 31px;
    }

    .demo-upload-list img {
        width: 100%;
        height: 100%;
    }

    .demo-upload-list-cover {
        display: none;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0, 0, 0, 0.6);
    }

    .demo-upload-list:hover .demo-upload-list-cover {
        display: block;
    }

    .demo-upload-list-cover i {
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin-top: 35px;
    }

    // 上传图片 start
    .feedsucc-wrapper {
        width: 1200px;
        background: #ffffff;
        position: relative;
        height: 792px;
    }

    .feedback-success {
        width: 600px;
        margin: 0 auto;
        text-align: center;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);

        .feedback-success1 {
            display: flex;
            align-items: center;
            justify-content: center;

            img {
                width: 46px;
                height: 46px;
            }

            span {
                font-size: 29px;
                color: #333333;
                margin-left: 7px;
                font-weight: bold;
            }
        }

        .feedback-success2 {
            width: 116px;
            height: 34px;
            border: 1px solid #d40d2b;
            font-size: 14px;
            color: #d40d2b;
            margin: 0 auto;
            line-height: 34px;
            margin-top: 41px;
            border-radius: 4px;
            cursor: pointer;
        }

        p {
            font-size: 18px;
            color: #666666;
            text-align: center;
            margin-top: 18px;
        }
    }

    .problem-footer {
        width: 100%;
        margin-top: 36px;
        background: #ffffff;

        ul {
            display: flex;
            margin-top: 31px;
            justify-content: center;

            li {
                font-size: 13px;
                color: #333333;
                margin-left: 34px;
            }
        }

        p {
            text-align: center;
            margin-top: 19px;
            padding-bottom: 22px;
        }
    }
</style>
