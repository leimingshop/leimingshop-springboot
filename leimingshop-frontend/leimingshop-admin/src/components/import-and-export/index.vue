<template>
    <div style="display: inline-block">
        <el-button
            v-if="importAndExportOptions && importAndExportOptions.exportUrl"
            class="btn"
            style="padding: 9px 20px"
            @click="exportExcel"
            ><img :src="exportSrc" style="width: 20px; height: 20px" alt=""
        /></el-button>
        <el-upload
            v-if="importAndExportOptions && importAndExportOptions.importUrl"
            style="display: inline-block"
            class="upload-demo"
            :class="
                importAndExportOptions && importAndExportOptions.exportUrl
                    ? 'marringLeft20'
                    : ''
            "
            ref="upload"
            :action="importAndExportOptions.importUrl"
            :on-success="uploadSuccess"
            :on-error="uploadError"
            :show-file-list="false"
            :headers="myHeaders"
            :before-upload="beforeAvatarUpload"
            name="file"
            :on-progress="handleProgress"
            @on-change="handleChange"
        >
            <el-button slot="trigger" class="btn" type="primary">{{
                uploadLoading ? "导入中..." : importAndExportOptions.importWord
            }}</el-button>
            <!-- <div slot="tip" class="el-upload__tip">只能上传excel格式视频，且不超过10M</div> -->
        </el-upload>
    </div>
</template>
<script>
    import Cookies from "js-cookie";
    import qs from "qs";
    export default {
        name: "bread",
        props: ["importAndExportOptions", "dataForm"],
        data() {
            return {
                myHeaders: {
                    // 'ADMIN-TOKEN':Cookies.get('ADMIN-TOKEN')
                }, //Cookies.get(teacher_token)
                uploadLoading: false,
                exportSrc: require("@/assets/img/export.png"),
            };
        },
        created() {
            this.myHeaders = { "ADMIN-TOKEN": Cookies.get("ADMIN-TOKEN") };
            console.log(this.importAndExportOptions);
        },
        methods: {
            // 导入
            importExcel() {
                // importRegister
            },
            // https://gitbook.cn/books/5d81cd6b90dbf8361802570f/index.html
            // 导出
            exportExcel() {
                // let url = ""
                // let kvArr = Object.entries(this.dataForm);
                // kvArr.forEach(v=>{
                //     if(Object.prototype.toString.call(v[1]) =='[object Object]'){
                //         arguments.callee(v[1]);
                //     }else{
                //         url += v.join('=')+'&'
                //     }
                // })
                // url  = url.substring(0,url.length-1);
                // url = this.importAndExportOptions.exportUrl + "?"+url;
                // window.open(url);

                var params = qs.stringify({
                    "ADMIN-TOKEN": Cookies.get("ADMIN-TOKEN"),
                    ...this.dataForm,
                });
                window.location.href = `${this.importAndExportOptions.exportUrl}?${params}`;
                //  window.open(`${this.importAndExportOptions.exportUrl}?${params}`);
            },
            // 导入之前
            beforeAvatarUpload(file) {
                this.$refs.upload.abort();
                // this.progress = 0;
                console.log(file);
                var reg = /^.*\.(?:xls|xlsx)$/i; //文件名可以带空格
                // const isExcel =  file.type=== "application/vnd.ms-excel";
                const isExcel = reg.test(file.name);
                const isLt2M = file.size / 1024 / 1024 < 500;

                if (!isExcel) {
                    this.$message.error("只能上传excel文件格式!");
                }
                if (!isLt2M) {
                    this.$message.error("导出的excel不能超过500MB!");
                }
                return isExcel && isLt2M;
                // return isLt2M;
            },
            // 导入过程中
            handleProgress(event, file, fileList) {
                this.uploadLoading = true;
                console.log([event, file, fileList]);

                if (file.percentage >= 90) {
                    this.progress = 90;
                } else {
                    this.progress = parseInt(file.percentage);
                }
            },
            // 导入成功
            uploadSuccess(response, file, fileList) {
                // console.log(file.per);
                let that = this;
                that.uploadLoading = false;
                if (response.code == "200") {
                    that.$message({
                        message: response.msg,
                        type: "success",
                        duration: 1500,
                    });
                    // that.dataFormSubmit();
                    that.$emit("getDataList");
                } else {
                    // that.progress = 0;
                    that.$message({
                        message: response.msg,
                        type: "error",
                        duration: 1500,
                    });
                }
            },
            // 导入失败回调
            uploadError(response, file, fileList) {
                let that = this;
                that.uploadLoading = false;
                console.log("上传文件失败response" + response);
                console.log("上传文件失败file" + file);
                console.log("上传文件失败fileList" + fileList);
                that.$message({
                    message: "导入失败，请重新导入",
                    type: "error",
                    duration: 1500,
                });
            },
            handleChange() {},
        },
    };
</script>
<style>
    .marringLeft20 {
        margin-left: 20px;
    }
</style>

