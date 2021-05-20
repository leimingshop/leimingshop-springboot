<template>
    <div>
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
                <div class="iconSize">
                    1、店铺入驻后，商家可以自主修改企业资质信息，不过需要运营人员审核
                </div>
                <div class="iconSize">
                    2、用户修改信息后，点击提交。提交至运营人员审核。同时按钮变为审核中，不能二次提交
                </div>
                <div class="iconSize">
                    3、审核通过后，企业资质自动更新为最新的店铺信息，且将数据同步到店铺列表的店铺信息中
                </div>
                <div class="iconSize">
                    4、审核拒绝后，可以再次修改和提交，并显示审核拒绝原因
                </div>
            </template>
        </el-alert>

        <!--资质证明表单-->
        <el-form
            :inline="true"
            :model="dataForm"
            :rules="dataRule"
            ref="dataForm"
            label-width="200px"
            style="width: 100%; margin: auto"
        >
            <div class="secondForm">
                <div
                    class="companyNews"
                    style="margin-bottom: 20px; font-weight: 650"
                >
                    公司信息
                    <el-form-item label="公司名称：" prop="companyName">
                        <el-input
                            :disabled="disabledVisible"
                            type="textarea"
                            v-model="dataForm.companyName"
                            placeholder="输入建议"
                            maxlength="30"
                            clearable
                        ></el-input>
                        <div
                            style="
                                color: red;
                                position: absolute;
                                min-width: 150px;
                                margin-left: 205px;
                                margin-top: -35px;
                            "
                        >
                            （须与营业执照一致）
                        </div>
                    </el-form-item>
                    <el-form-item label="法人姓名：" prop="legalPersonName">
                        <el-input
                            :disabled="disabledVisible"
                            v-model="dataForm.legalPersonName"
                            placeholder="输入建议"
                            maxlength="20"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="办公电话：" prop="companyPhone">
                        <el-input
                            :disabled="disabledVisible"
                            v-model="dataForm.companyPhone"
                            placeholder="输入建议"
                            maxlength="20"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="办公地址："
                        prop="companyAddressDetail"
                    >
                        <el-input
                            :disabled="disabledVisible"
                            type="textarea"
                            v-model="dataForm.companyAddressDetail"
                            placeholder="输入建议"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="公司税号：" prop="taxpayerId">
                        <el-input
                            :disabled="disabledVisible"
                            v-model="dataForm.taxpayerId"
                            placeholder="输入建议"
                            maxlength="20"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="银行开户账号："
                        prop="bankAccountNumber"
                    >
                        <el-input
                            :disabled="disabledVisible"
                            v-model="dataForm.bankAccountNumber"
                            placeholder="输入建议"
                            maxlength="20"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="开户银行：" prop="bankName">
                        <el-input
                            :disabled="disabledVisible"
                            type="textarea"
                            v-model="dataForm.bankName"
                            placeholder="输入建议"
                            maxlength="30"
                            clearable
                        ></el-input>
                    </el-form-item>

                    <el-form-item label="审核结果：">
                        <span v-if="dataForm.authAuditStatus == 10"
                            >待审核</span
                        >
                        <span v-if="dataForm.authAuditStatus == 20"
                            >审核通过</span
                        >
                        <span v-if="dataForm.authAuditStatus == 30"
                            >审核拒绝</span
                        >
                    </el-form-item>

                    <el-form-item
                        v-if="dataForm.authAuditStatus != 10"
                        label="审核时间："
                    >
                        <span>{{ dataForm.updateDate }}</span>
                    </el-form-item>

                    <el-form-item
                        v-if="dataForm.authAuditStatus != 10"
                        label="备注："
                    >
                        <el-input
                            :disabled="true"
                            type="textarea"
                            :rows="4"
                            v-model="dataForm.authAuditCause"
                        >
                        </el-input>
                    </el-form-item>
                </div>
                <div
                    class="proofNews"
                    style="margin-bottom: 20px; font-weight: 650"
                >
                    资质证明
                    <el-form-item
                        label="法人身份证（正反两面）："
                        prop="idCardNatPicture"
                    >
                        <div style="float: left">
                            <div style="width: 100px" v-loading="imgLoading[1]">
                                <img-cropper
                                    v-if="!disabledVisible"
                                    ref="idCardNatPicture"
                                    :index="'1'"
                                    :imgWidth="'129px'"
                                    :imgHeight="'86px'"
                                    :cropImg="dataForm.idCardNatPicture"
                                    @GiftUrlHandle="GiftUrlHandle"
                                    :canDelete="'1'"
                                    :needHover="'1'"
                                    :type="'0'"
                                    @delteteImg="delteteImg('idCardNatPicture')"
                                >
                                </img-cropper>
                                <img
                                    v-else
                                    :src="
                                        dataForm.idCardNatPicture | filterImgUrl
                                    "
                                    alt=""
                                    style="width: 100px; height: 100px"
                                />
                            </div>
                            <div style="text-align: center">
                                身份证（国徽页）
                            </div>
                        </div>
                        <div style="float: left; margin-left: 10px">
                            <div style="width: 100px" v-loading="imgLoading[2]">
                                <img-cropper
                                    v-if="!disabledVisible"
                                    ref="idCardPeoPicture"
                                    :index="'2'"
                                    :imgWidth="'129px'"
                                    :imgHeight="'86px'"
                                    :cropImg="dataForm.idCardPeoPicture"
                                    @GiftUrlHandle="GiftUrlHandle"
                                    :canDelete="'1'"
                                    :needHover="'1'"
                                    :type="'0'"
                                    @delteteImg="delteteImg('idCardPeoPicture')"
                                >
                                </img-cropper>
                                <img
                                    v-else
                                    :src="
                                        dataForm.idCardPeoPicture | filterImgUrl
                                    "
                                    alt=""
                                    style="width: 100px; height: 100px"
                                />
                            </div>
                            <div style="text-align: center">
                                身份证（人像页）
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item
                        label="三证合一营业执照："
                        prop="electronicBusinessLicense"
                    >
                        <div style="width: 250px">
                            <div style="width: 100px" v-loading="imgLoading[3]">
                                <img-cropper
                                    v-if="!disabledVisible"
                                    ref="cropperImg3"
                                    :index="'3'"
                                    :imgWidth="'100px'"
                                    :imgHeight="'100px'"
                                    :cropImg="
                                        dataForm.electronicBusinessLicense
                                    "
                                    @GiftUrlHandle="GiftUrlHandle"
                                    :canDelete="'1'"
                                    :needHover="'1'"
                                    :type="'0'"
                                    @delteteImg="
                                        delteteImg('electronicBusinessLicense')
                                    "
                                >
                                </img-cropper>
                                <img
                                    v-else
                                    :src="
                                        dataForm.electronicBusinessLicense
                                            | filterImgUrl
                                    "
                                    alt=""
                                    style="width: 100px; height: 100px"
                                />
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="纳税人资格证：" prop="taxpayerPicture">
                        <div style="width: 250px">
                            <div style="width: 100px" v-loading="imgLoading[4]">
                                <img-cropper
                                    v-if="!disabledVisible"
                                    ref="cropperImg4"
                                    :index="'4'"
                                    :imgWidth="'100px'"
                                    :imgHeight="'100px'"
                                    :cropImg="dataForm.taxpayerPicture"
                                    @GiftUrlHandle="GiftUrlHandle"
                                    :canDelete="'1'"
                                    :needHover="'1'"
                                    :type="'0'"
                                    @delteteImg="delteteImg('taxpayerPicture')"
                                >
                                </img-cropper>
                                <img
                                    v-else
                                    :src="
                                        dataForm.taxpayerPicture | filterImgUrl
                                    "
                                    alt=""
                                    style="width: 100px; height: 100px"
                                />
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item
                        label="银行开户许可证："
                        prop="bankAccountPicture"
                    >
                        <div style="width: 250px">
                            <div style="width: 100px" v-loading="imgLoading[5]">
                                <img-cropper
                                    v-if="!disabledVisible"
                                    ref="cropperImg5"
                                    :index="'5'"
                                    :imgWidth="'100px'"
                                    :imgHeight="'100px'"
                                    :cropImg="dataForm.bankAccountPicture"
                                    @GiftUrlHandle="GiftUrlHandle"
                                    :canDelete="'1'"
                                    :needHover="'1'"
                                    :type="'0'"
                                    @delteteImg="
                                        delteteImg('bankAccountPicture')
                                    "
                                    >>
                                </img-cropper>
                                <img
                                    v-else
                                    :src="
                                        dataForm.bankAccountPicture
                                            | filterImgUrl
                                    "
                                    alt=""
                                    style="width: 100px; height: 100px"
                                />
                            </div>
                        </div>
                    </el-form-item>
                </div>
            </div>
            <div
                style="display: flex; justify-content: center; margin-top: 20px"
            >
                <el-button
                    class="btn"
                    type="primary"
                    v-if="
                        disabledVisible &&
                        $hasPermission('sys:store:company:update')
                    "
                    size="mini"
                    @click="goEdit()"
                    :disabled="dataForm.authAuditStatus == 10"
                >
                    <span v-if="dataForm.authAuditStatus == 10">审核中</span>
                    <span v-else>修改</span>
                </el-button>
                <el-button
                    class="btn"
                    type="primary"
                    v-if="!disabledVisible"
                    size="mini"
                    @click="submitEdit()"
                    :loading="saveLoading"
                    >{{ saveLoading ? "提交中" : "提交" }}</el-button
                >
                <el-button
                    class="btn"
                    size="mini"
                    v-if="!disabledVisible"
                    @click="cancleEdit()"
                    >取消</el-button
                >
            </div>
        </el-form>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import imgCropper from "@/components/upload/model-photo-cropper2";
    import {
        allGoodsclass,
        storebCompanyInfo,
        storeUdateCompanyInfo,
        uploadPicBase64,
    } from "@/api/api";
    export default {
        data() {
            return {
                disabledVisible: true, //是否disabled
                imgLoading: [false, false, false, false, false, false],
                saveLoading: false,
                defaultProps: {
                    children: "children",
                    label: "gcName",
                },
                getClassLoading: false, // 获取树形结构loading
                dataForm: {
                    id: "",
                    storeId: "", //  店铺ID
                    companyName: "", //  名称 ,
                    legalPersonName: "", // 法人名称 ,
                    companyPhone: "", // 电话
                    companyAddressDetail: "", // 办公地址
                    taxpayerId: "", // 纳税人识别号 ,
                    bankAccountNumber: "", //  开户行账号 ,
                    bankName: "", // 开户行名称 ,
                    idCardNatPicture: "", //  份证，反面 ,
                    idCardPeoPicture: "", // 份证，正面 ,
                    electronicBusinessLicense: "", //.  营业执照电子版 ,
                    taxpayerPicture: "", // 人资格证
                    bankAccountPicture: "", //银行开户许可证 ,
                },
                dataRule: {
                    companyName: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    legalPersonName: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    companyPhone: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    companyAddressDetail: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    taxpayerId: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    bankAccountNumber: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    bankName: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    idCardNatPicture: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    idCardPeoPicture: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    electronicBusinessLicense: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    taxpayerPicture: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    bankAccountPicture: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        components: {
            Bread,
            imgCropper,
        },
        mounted() {
            // 数据回显
            this.backScan();
        },
        methods: {
            // 回显数据
            backScan() {
                storebCompanyInfo().then((res) => {
                    console.log(res);
                    if (res.code == 200) {
                        Object.assign(this.dataForm, res.data);
                    }
                });
            },
            //    删除图片
            delteteImg(key) {
                this.dataForm[key] = "";
            },
            GiftUrlHandle(imgUrl, index) {
                let that = this;
                let url = imgUrl;
                // that.$set(that.imgLoading,index,that.imgLoading[index])
                if (index == "0") {
                    // 1.店铺logo
                    that.dataForm.storeLogo = url;
                } else if (index == "1") {
                    //2.法人身份证（反面）
                    that.dataForm.idCardNatPicture = url;
                } else if (index == "2") {
                    //2.法人身份证（正面）
                    that.dataForm.idCardPeoPicture = url;
                } else if (index == "3") {
                    //3.三证合一营业执照
                    that.dataForm.electronicBusinessLicense = url;
                } else if (index == "4") {
                    //4.纳税人资格证
                    that.dataForm.taxpayerPicture = url;
                } else {
                    // 5.银行开户许可证
                    that.dataForm.bankAccountPicture = url;
                }
                // this.uploadPic(val,index);
            },
            // uploadPic(base64,index){
            //     let that = this;
            //     const params = { "imgStr": base64 };
            //     return new Promise(function(resolve){
            //         that.imgLoading[index] = true;
            //         that.$set(that.imgLoading,index,that.imgLoading[index])
            //         uploadPicBase64(params).then(res =>{
            //             that.imgLoading[index] = false;
            //             that.$set(that.imgLoading,index,that.imgLoading[index])
            //             if(res && res.code == "200"){
            //                 var url = res.data.url
            //                 if(index == '0'){
            //                     // 1.店铺logo
            //                     that.dataForm.storeLogo = url;
            //                 }else if(index == '1'){
            //                     //2.法人身份证（反面）
            //                     that.dataForm.idCardNatPicture = url;
            //                 } else if(index == '2'){
            //                     //2.法人身份证（正面）
            //                     that.dataForm.idCardPeoPicture = url;

            //                 }else if(index == '3'){
            //                     //3.三证合一营业执照
            //                     that.dataForm.electronicBusinessLicense = url;
            //                 }else if(index == '4'){
            //                     //4.纳税人资格证
            //                     that.dataForm.taxpayerPicture = url;
            //                 }else{
            //                     // 5.银行开户许可证
            //                     that.dataForm.bankAccountPicture = url;
            //                 }
            //                 resolve("true")
            //             }else {
            //                 that.$message({
            //                     message:res.msg,
            //                     type: 'error',
            //                     duration: 1500,
            //                 })
            //                 resolve("false")
            //             }
            //         })
            //     });
            // },
            // 修改
            goEdit() {
                if (this.dataForm.authAuditStatus == 10) {
                    this.$message({
                        message: "正在审核中",
                        type: "error",
                        duration: 1500,
                    });
                    return;
                }
                this.disabledVisible = false;
            },
            // 提交修改
            submitEdit() {
                if (this.saveLoading) {
                    return;
                }
                this.$refs["dataForm"].validate((valid) => {
                    if (valid) {
                        var obj = {
                            bankAccountNumber: this.dataForm.bankAccountNumber, //  开户行账号 ,
                            bankAccountPicture: this.dataForm.bankAccountPicture, //银行开户许可证 ,
                            bankName: this.dataForm.bankName, // 开户行名称 ,
                            companyAddressDetail: this.dataForm
                                .companyAddressDetail, // 办公地址
                            companyName: this.dataForm.companyName, //  名称 ,
                            companyPhone: this.dataForm.companyPhone, // 电话
                            electronicBusinessLicense: this.dataForm
                                .electronicBusinessLicense, //.  营业执照电子版 ,
                            // "id": this.dataForm.id,
                            idCardNatPicture: this.dataForm.idCardNatPicture, //  份证，反面 ,
                            idCardPeoPicture: this.dataForm.idCardPeoPicture, // 份证，正面 ,
                            legalPersonName: this.dataForm.legalPersonName, // 法人名称 ,
                            storeId: this.dataForm.storeId, //  店铺ID
                            taxpayerId: this.dataForm.taxpayerId, // 纳税人识别号 ,
                            taxpayerPicture: this.dataForm.taxpayerPicture, // 人资格证
                        };
                        this.saveLoading = true;
                        storeUdateCompanyInfo(obj).then((res) => {
                            this.saveLoading = false;
                            if (res.code == 200) {
                                this.disabledVisible = true;
                                this.backScan();
                                this.dataForm.authAuditStatus = 10;
                                this.$message.success(res.msg);
                            } else {
                                this.$message.error(res.msg);
                            }
                        });
                    }
                });
            },
            // 取消修改
            cancleEdit() {
                let that = this;
                this.$confirm("是否确认取消修改？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        that.disabledVisible = true;
                        //  回显数据
                        that.backScan();
                    })
                    .catch(() => {
                        // this.$message({type: "info",message: "已取消"});
                    });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .formMode {
        border-bottom: 1px solid #ecedf1;
        /deep/ .el-form-item__label {
            width: 150px !important;
        }
        .el-input {
            width: 250px !important;
        }
        .textarea {
            width: 250px !important;
        }
        &:first-child {
            padding-top: 0;
        }
        &:last-child {
            border: none;
        }
        .itemMode {
            display: flex;
            justify-content: space-between;
        }
    }
    /*第二个表单 资质证明*/
    .secondForm {
        display: flex;
        justify-content: space-around;
        .el-textarea__inner {
            width: 250px !important;
        }
        .el-form-item {
            display: block;
        }
        .el-input {
            width: 100% !important;
        }
        .el-textarea {
            width: 108% !important;
        }
        .companyNews {
            /deep/ .el-form-item__label {
                width: 150px !important;
            }
        }
        .proofNews {
            /deep/ .el-form-item__label {
                width: 180px !important;
            }
        }
    }
    /deep/ .el-tree__empty-text {
        margin-left: 20px;
        width: 100px;
    }
</style>
