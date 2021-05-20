<template>
    <div class="upload-wrap">
        <ul class="upload-list" v-viewer>
            <li
                v-for="(item, index) in uploadPicList"
                :key="item.url"
                v-lazy-container="{
                    selector: 'img',
                    error: '/img/common/loading/200_200.png',
                    loading: '/img/common/loading/200_200.png',
                }"
            >
                <img
                    :data-src="item.url | filterImgUrl"
                    title="点击图片预览"
                    alt
                />
                <Icon type="md-close" @click="handleRemoveImg(index)" />
            </li>
        </ul>

        <Upload
            v-show="uploadPicList.length < limit"
            ref="upload"
            :show-upload-list="false"
            :format="format"
            :accept="accept"
            :max-size="1024 * maxSize"
            :on-exceeded-size="handleMaxSize"
            :before-upload="handleUpload"
            :multiple="multiple"
            type="drag"
            action
        >
            <!-- max-size : 单位（kb） -->
            <div class="add-image-btn">
                <Spin fix v-if="uploadLoading"></Spin>
            </div>
        </Upload>
    </div>
</template>

<script>
    import { uploadPicBase64 } from "@/api/api_Common";

    export default {
        name: "uploadFile",

        props: {
            fileType: {
                type: String,
                default: "image",
            }, // 文件类型

            uploadPicList: {
                type: Array,
                default: [],
            }, // 文件上传列表

            maxSize: {
                type: Number,
                default: 100,
            }, // 文件大小限制 单位 (M)

            limit: {
                type: Number,
                default: 1,
            }, // 文件数量限制 单位 (张)

            multiple: {
                type: Boolean,
                default: true,
            }, // 是否可以多选文件
        },

        model: {
            prop: "uploadPicList",
            event: "handleUploadSuccess",
        },

        computed: {
            format() {
                switch (this.fileType) {
                    case "image":
                        return ["jpg", "jpeg", "png"];
                    default:
                        // ...
                        break;
                }
            }, // 支持的文件类型，与 accept 不同的是，format 是识别文件的后缀名，accept 为 input 标签原生的 accept 属性，会在选择文件时过滤，可以两者结合使用

            accept() {
                switch (this.fileType) {
                    case "image":
                        return "image/bmp, image/gif, image/jpg, image/jpeg, image/png";
                    default:
                        // ...
                        break;
                }
            }, // 接受上传的文件类型
        },

        data() {
            return {
                uploadLoading: false, // 文件上传状态
            };
        },

        mounted() {
            this.handleResetUpload();
        },

        methods: {
            // 重构 iview upload 的组件方法
            handleResetUpload() {
                this.$refs["upload"].upload = (file) => {
                    //  一次选中5张 or 累计选择5张
                    let tempNum = document.querySelector(".ivu-upload-input").files
                        .length;
                    if (
                        tempNum > this.limit ||
                        tempNum + this.uploadPicList.length > this.limit
                    ) {
                        this.$Message.destroy();
                        this.$Message.warning(
                            `最多可上传${this.limit}张图片，请重新选择`
                        );
                        return;
                    }
                    this.handleUpload(file);
                };
            },

            // 上传图片的回调
            async handleUpload(file) {
                let imgFile = {
                    imgStr: await this.handleFileToBase64(file),
                    pictureName: file.name,
                };

                // file.size  单位(B)
                if (file.size > 1024 * 1024 * this.maxSize)
                    return this.handleMaxSize(file);

                this.uploadLoading = true;
                const res = await uploadPicBase64(imgFile);
                this.uploadLoading = false;

                if (res && res.code == 200) {
                    let tempArr = this.uploadPicList;
                    tempArr.push(res.data);
                    this.$emit("handleUploadSuccess", tempArr);
                }

                return false;
            },

            // file对象转base64
            handleFileToBase64(file) {
                let that = this,
                    reader = new FileReader();
                reader.readAsDataURL(file);
                return new Promise((resolve, reject) => {
                    reader.onload = function (e) {
                        //这里是一个异步，所以获取数据不好获取在实际项目中，就用new Promise解决
                        if (this.result) {
                            resolve(this.result);
                        } else {
                            reject("err");
                        }
                    };
                });
            },

            // 从已上传的图片列表移除图片
            handleRemoveImg(index) {
                this.uploadPicList.splice(index, 1);
            },

            // 上传文件文件超出限制
            handleMaxSize(file) {
                this.$Message.warning(`图片【${file.name}】大小超出5M，请重新上传`);
            },
        },
    };
</script>

<style lang="scss" scoped>
    .upload-wrap {
        display: flex;
        /deep/ .ivu-upload {
            display: inline-block;
            width: 78px;
            height: 78px;
            .add-image-btn {
                width: 100%;
                height: 100%;
                background: url("/img/06.personalCenter/06-03.afterSale/add-image-btn.png")
                    center;
                &:hover {
                    background: url("/img/06.personalCenter/06-03.afterSale/add-image-redbtn.png")
                        center;
                }
            }
        }

        p {
            &.hint-1 {
                font-size: 14px;
                color: #666666;
            }
            &.hint-2 {
                font-size: 12px;
                color: #999999;
                margin-top: -10px;
            }
        }

        .upload-list {
            li {
                width: 78px;
                height: 78px;
                display: inline-block;
                border: 1px solid #efefef;
                margin-right: 10px;
                position: relative;
                &:hover {
                    .ivu-icon {
                        opacity: 1;
                    }
                }
                img {
                    width: 100%;
                    height: 100%;
                    object-fit: scale-down;
                    border: 1px solid #efefef;
                    transition: border 0.3s;
                    cursor: pointer;
                    &:hover {
                        border: 1px solid #dd2619;
                    }
                }
                .ivu-icon {
                    width: 18px;
                    height: 18px;
                    line-height: 18px;
                    border-bottom-left-radius: 4px;
                    background: #999999;
                    color: #ffffff;
                    //   opacity: 0;
                    transition: opacity 0.3s;
                    position: absolute;
                    right: -7px;
                    top: -7px;
                    border-radius: 50%;
                    cursor: pointer;
                    &:hover {
                        background: #dd2619;
                    }
                }
            }
        }
    }
</style>
