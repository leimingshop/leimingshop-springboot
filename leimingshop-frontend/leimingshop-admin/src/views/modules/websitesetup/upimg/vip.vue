<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <!--    <div class="mod-oss__oss">-->
        <!-- <el-form :inline="true" :model="dataForm">
          <el-form-item>
            <el-button type="primary" @click="uploadHandle()">{{ $t('oss.upload') }}</el-button>
          </el-form-item>
        </el-form> -->
        <div class="formControlArea">
            <div></div>
            <div style="display: flex">
                <mainTipsMessage></mainTipsMessage>
            </div>
        </div>
        <el-alert
            title="操作提示"
            type="warning"
            @close="$store.commit('showListMessage')"
            v-show="$store.state.listMessageFlag"
        >
            <template slot="title">
                <div class="iconSize">操作提示：</div>
                <div class="iconSize">1、该配置为新注册会员的默认头像</div>
            </template>
        </el-alert>
        <el-form
            ref="ruleForm"
            label-width="130px"
            class="demo-ruleForm artinputcontent"
        >
            <el-form-item label="会员默认头像：">
                <!-- <div class="pcCoverUrl imgUrl"> -->
                <!-- :aspectRatio="1/1" -->
                <div style="width: 100px; height: 100px" v-loading="uploading">
                    <img-cropper
                        ref="cropperImg1"
                        :index="'1'"
                        :imgWidth="'100px'"
                        :imgHeight="'100px'"
                        :cropImg="avatar"
                        @GiftUrlHandle="GiftUrlHandle"
                    ></img-cropper>
                </div>
                <p>
                    新注册用户默认头像，图片只支持jpg/png格式上传,文件不得超过50kb
                </p>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    // import mixinViewModule from '@/mixins/view-module'
    import Bread from "@/components/bread";
    import imgCropper from "@/components/upload/model-photo-cropper2";

    import { getAvatars, setAvatars, uploadPicBase64 } from "@/api/api.js";

    export default {
        //   mixins: [mixinViewModule],
        data() {
            return {
                //   mixinViewModuleOptions: {
                //     getDataListURL: '/admin-api/oss/file/page',
                //     getDataListIsPage: true,
                //     deleteURL: '/admin-api/oss/file',
                //     deleteIsBatch: true
                //   },
                breaddata: ["系统管理", "会员头像设置"],
                dataForm: {},
                avatar: "",
                uploading: false,
            };
        },
        components: {
            Bread,
            imgCropper,
        },
        mounted() {
            this.getData();
        },
        methods: {
            //   获取数据
            getData() {
                getAvatars().then((res) => {
                    if (res.code == 200) {
                        this.avatar = res.data.avatar;
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
            setData() {
                var obj = {
                    avatar: this.avatar,
                };
                this.uploading = true;
                setAvatars(obj).then((res) => {
                    this.uploading = false;
                    if (res.code == 200) {
                        this.$message.success(res.msg);
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
            //上传图片
            // uploadPic(base64) {
            //     const params = {"imgStr": base64};
            //     const that = this;
            //     this.uploading = true;
            //     return new Promise(function (resolve) {
            //         uploadPicBase64(params).then(res => {
            //             that.uploading = false
            //             if (res && res.code == "200") {
            //                 var url = res.data.url
            //                 that.avatar = url;
            //                 that.setData();
            //                 // that.currentIndex = -1;//不能这样写，防止网络延迟
            //                 resolve("true")
            //             } else {
            //                 that.uploading = false
            //                 // that.currentIndex = -1;//不能这样写，防止网络延迟
            //                 resolve("false")
            //             }
            //         })
            //     });
            // },
            GiftUrlHandle(imgUrl) {
                let that = this;
                let url = imgUrl;
                that.avatar = url;
                that.setData();
            },
        },
    };
</script>
