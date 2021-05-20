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
                    1、店铺入驻后，商家可以自主修改店铺信息，不过需要运营人员审核
                </div>
                <div class="iconSize">
                    2、用户修改信息后，点击提交。提交至运营人员审核。同时按钮变为审核中，不能二次提交
                </div>
                <div class="iconSize">
                    3、审核通过后，店铺信息自动更新为最新的店铺信息，且将数据同步到店铺列表的店铺信息中
                </div>
                <div class="iconSize">
                    4、审核拒绝后，可以再次修改和提交，并显示审核拒绝原因
                </div>
            </template>
        </el-alert>

        <el-form
            :inline="true"
            :model="dataForm"
            :rules="dataRule"
            ref="dataForm"
            label-width="200px"
            style="width: 90%; margin: auto"
        >
            <div>
                <div class="formMode">
                    <div style="margin-bottom: 20px; font-weight: 650">
                        店铺信息
                    </div>
                    <div class="itemMode">
                        <div>
                            <el-form-item label="店铺名称：" prop="storeName">
                                <el-input
                                    :disabled="disabledVisible"
                                    v-model="dataForm.storeName"
                                    maxlength="30"
                                    type="textarea"
                                    placeholder="请输入店铺名称"
                                    clearable
                                ></el-input>
                            </el-form-item>
                        </div>
                        <div style="margin-right: 150px">
                            <el-form-item
                                label="店铺联系人："
                                prop="storeLinkman"
                            >
                                <el-input
                                    :disabled="disabledVisible"
                                    v-model="dataForm.storeLinkman"
                                    maxlength="20"
                                    placeholder="请输入店铺联系人"
                                    clearable
                                ></el-input>
                            </el-form-item>
                        </div>
                    </div>
                    <div class="itemMode">
                        <div>
                            <div>
                                <el-form-item
                                    label="店铺类型："
                                    prop="storeType"
                                >
                                    <el-select
                                        :disabled="disabledVisible"
                                        v-model="dataForm.storeType"
                                        placeholder="店铺类型"
                                    >
                                        <el-option label="自营店铺" value="1"
                                            >自营店铺</el-option
                                        >
                                        <el-option label="普通店铺" value="2"
                                            >普通店铺</el-option
                                        >
                                    </el-select>
                                </el-form-item>
                            </div>
                            <div style="margin-right: 138px">
                                <el-form-item
                                    label="店铺联系电话："
                                    prop="linkmanPhone"
                                >
                                    <el-input
                                        :disabled="disabledVisible"
                                        v-model="dataForm.linkmanPhone"
                                        maxlength="20"
                                        placeholder="请输入店铺联系电话"
                                        clearable
                                    ></el-input>
                                </el-form-item>
                            </div>
                        </div>
                    </div>
                    <div class="itemMode">
                        <div>
                            <el-form-item label="店铺简介：" prop="storeIntro">
                                <el-input
                                    :disabled="disabledVisible"
                                    type="textarea"
                                    v-model="dataForm.storeIntro"
                                    maxlength="20"
                                    @input="changeInput()"
                                    placeholder="请输入店铺简介"
                                    clearable
                                ></el-input>
                            </el-form-item>
                        </div>
                        <!--storeLogo-->
                        <div style="margin-right: 150px">
                            <el-form-item label="店铺LOGO：" prop="storeLogo">
                                <div style="width: 250px">
                                    <div
                                        v-if="!disabledVisible"
                                        style="width: 100px"
                                        v-loading="imgLoading[0]"
                                    >
                                        <img-cropper
                                            ref="cropperImg1"
                                            :index="'0'"
                                            :imgWidth="'100px'"
                                            :imgHeight="'100px'"
                                            :cropImg="dataForm.storeLogo"
                                            @GiftUrlHandle="GiftUrlHandle"
                                        >
                                        </img-cropper>
                                    </div>
                                    <div v-else style="width: 100px">
                                        <img
                                            :src="
                                                dataForm.storeLogo
                                                    | filterImgUrl
                                            "
                                            alt=""
                                            style="width: 100px; height: 100px"
                                        />
                                    </div>
                                </div>
                            </el-form-item>
                        </div>
                        <!--分割线-->
                        <!-- <div style="width:392px"></div> -->
                    </div>
                </div>
                <div class="formMode" style="margin-top: 20px">
                    <div style="margin-bottom: 20px; font-weight: 650">
                        经营范围
                    </div>
                    <div style="display: flex; justify-content: space-between">
                        <!-- 左边区域 -->
                        <el-form-item style="widdth: 400px; margin-left: 55px">
                            <div
                                class="box"
                                style="
                                    max-height: 300px;
                                    overflow: auto;
                                    width: 300px;
                                "
                            >
                                <el-tree
                                    :disabled="disabledVisible"
                                    v-loading="getClassLoading"
                                    ref="tree"
                                    :data="data"
                                    show-checkbox
                                    node-key="id"
                                    :default-expanded-keys="expandedKeys"
                                    :default-checked-keys="checkedKeys"
                                    :props="defaultProps"
                                >
                                </el-tree>
                            </div>
                        </el-form-item>
                        <!-- 右边 区域 -->
                        <div
                            v-if="disabledVisible"
                            style="width: 400px; margin-right: 40px"
                        >
                            <div>
                                <el-form-item label="审核结果：">
                                    <span v-if="dataForm.infoAuditStatus == 10"
                                        >待审核</span
                                    >
                                    <span v-if="dataForm.infoAuditStatus == 20"
                                        >审核通过</span
                                    >
                                    <span v-if="dataForm.infoAuditStatus == 30"
                                        >审核拒绝</span
                                    >
                                </el-form-item>
                            </div>
                            <div>
                                <el-form-item
                                    v-if="dataForm.infoAuditStatus != 10"
                                    label="审核时间："
                                >
                                    <span>{{ dataForm.updateDate }}</span>
                                </el-form-item>
                            </div>
                            <div>
                                <el-form-item
                                    v-if="dataForm.infoAuditStatus != 10"
                                    label="备注："
                                >
                                    <span>{{ dataForm.infoAuditCause }}</span>
                                </el-form-item>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div
                style="display: flex; justify-content: center; margin-top: 20px"
            >
                <el-button
                    class="btn"
                    type="primary"
                    v-if="disabledVisible && $hasPermission('sys:store:update')"
                    size="mini"
                    @click="goEdit()"
                    :disabled="dataForm.infoAuditStatus == 10"
                >
                    <span v-if="dataForm.infoAuditStatus == 10">审核中</span>
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
        storebBasicInfo,
        storeUdateBasicInfo,
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
                data: [],
                expandedKeys: [],
                checkedKeys: [],
                dataForm: {
                    id: "",
                    linkmanPhone: "", // 联系人电话 ,
                    storeClassId: "", // 要么传null 要么数据 要么不传
                    storeId: "", //  店铺ID
                    storeIntro: "", // 店铺简介 ,
                    storeLinkman: "", //  店铺联系人 ,
                    storeLogo: "", // 店铺logo ,
                    storeName: "", //  店铺名称 ,
                    storeType: "", // 店铺类型（1:自营商户，2:普通商户） ,
                },
                dataRule: {
                    storeName: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    storeLinkman: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    storeType: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    linkmanPhone: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    storeIntro: [
                        {
                            required: true,
                            message: this.$t("validate.required"),
                            trigger: "blur",
                        },
                    ],
                    storeLogo: [
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
            // 获取树形数据
            this.getTreeData();
        },
        methods: {
            // 回显数据
            backScan() {
                storebBasicInfo().then((res) => {
                    console.log(res);
                    if (res.code == 200) {
                        Object.assign(this.dataForm, res.data);
                        this.dataForm.storeType = res.data.storeType + "";
                        this.expandedKeys = [];
                        this.checkedKeys = [];
                        if (res.data.storeGoodsClassDTO) {
                            res.data.storeGoodsClassDTO.forEach((item, index) => {
                                this.expandedKeys.push(item.id);
                                this.checkedKeys.push(item.id);
                            });
                        }
                    }
                });
            },
            // 获取树形结构数据
            getTreeData() {
                this.getClassLoading = true;
                allGoodsclass().then((res) => {
                    this.getClassLoading = false;
                    if (res.code == 200) {
                        this.data = res.data;
                        // 默认不能编辑勾选树形结构
                        this.changeTreeDisabled(true);
                    }
                });
            },
            GiftUrlHandle(imgUrl, index) {
                let that = this;
                let url = imgUrl;
                if (index == "0") {
                    // 1.店铺logo
                    that.dataForm.storeLogo = url;
                } else if (index == "1") {
                    //2.法人身份证（正面）
                    that.dataForm.idCardPeoPicture = url;
                } else if (index == "2") {
                    //2.法人身份证（反面）
                    that.dataForm.idCardNatPicture = url;
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
            //                     //2.法人身份证（正面）
            //                     that.dataForm.idCardPeoPicture = url;
            //                 } else if(index == '2'){
            //                     //2.法人身份证（反面）
            //                     that.dataForm.idCardNatPicture = url;
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
                if (this.dataForm.infoAuditStatus == 10) {
                    this.$message({
                        message: "正在审核中",
                        type: "error",
                        duration: 1500,
                    });
                    return;
                }
                this.disabledVisible = false;
                this.changeTreeDisabled(false);
            },
            // 树形结构禁用和启用
            changeTreeDisabled(boolArgu) {
                this.data.forEach((item, index) => {
                    item.disabled = boolArgu;
                    item.children &&
                        item.children.forEach((item2, index2) => {
                            item2.disabled = boolArgu;
                            item2.children &&
                                item2.children.forEach((item3, index3) => {
                                    item3.disabled = boolArgu;
                                });
                        });
                });
                this.data = [].concat(this.data);
                console.log(this.data);
            },
            // 提交修改
            submitEdit() {
                // debugger
                console.log(34434, this.dataForm.storeIntro.length);
                if (this.dataForm.storeIntro.length > 20) {
                    this.$message.error("店铺简介不能超过20个字数");
                    return;
                }
                if (this.saveLoading) {
                    return;
                }
                this.$refs["dataForm"].validate((valid) => {
                    if (valid) {
                        var checkedIds1 = this.$refs.tree.getCheckedKeys(); // 全选中
                        var checkedIds2 = this.$refs.tree.getHalfCheckedKeys(); //  半选中
                        var checkedIds = checkedIds1.concat(checkedIds2);
                        var obj = {
                            // "id": this.dataForm.id, //  主键ID ,
                            linkmanPhone: this.dataForm.linkmanPhone, // 联系人电话 ,
                            storeClassId: checkedIds, //  分类id
                            storeId: this.dataForm.storeId, //  店铺ID
                            storeIntro: this.dataForm.storeIntro, // 店铺简介 ,
                            storeLinkman: this.dataForm.storeLinkman, //  店铺联系人 ,
                            storeLogo: this.dataForm.storeLogo, // 店铺logo ,
                            storeName: this.dataForm.storeName, //  店铺名称 ,
                            storeType: this.dataForm.storeType, // 店铺类型（1:自营商户，2:普通商户） ,
                        };
                        this.saveLoading = true;
                        storeUdateBasicInfo(obj).then((res) => {
                            this.saveLoading = false;
                            if (res.code == 200) {
                                this.disabledVisible = true;
                                this.changeTreeDisabled(true);
                                this.expandedKeys = [];
                                this.checkedKeys = [];
                                this.backScan();
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
                        that.changeTreeDisabled(true);
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
    .box {
        box-sizing: border-box;
        border-width: 1px;
        border-style: solid;
        border-color: #d9d9d9;
        border-radius: 4px;
        -webkit-box-shadow: 0px 0px 4px rgba(217, 217, 217, 0.988235);
        box-shadow: 0px 0px 4px rgba(217, 217, 217, 0.988235);
        margin-bottom: 20px;
    }
    .formMode {
        border-bottom: 1px solid #ecedf1;
        /deep/ .el-form-item__label {
            width: 150px !important;
        }
        .el-input {
            width: 250px !important;
        }
        .el-textarea {
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
            .el-form-item {
                display: flex;
            }
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
