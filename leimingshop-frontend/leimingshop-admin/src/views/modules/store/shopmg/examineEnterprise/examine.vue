<template>
	<div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="showListFn"></Bread>
        <div v-loading="backScanLoading" style="margin-bottom:40px;">
            <el-form :inline="false"  label-width="150px">

                <!--资质证明表单-->
                <div class="secondForm">
                    <div class="companyNews" style="margin-bottom:20px;font-weight: 650;width:400px;">
                        公司信息
                        <el-form-item label="公司名称：" prop="companyName">
                            <el-input type="textarea"   :disabled="disabledVisible" v-model="dataInfo.companyName" placeholder="" clearable></el-input>
                            <span style="color: red;position: absolute;min-width: 150px">（须与营业执照一致）</span>
                        </el-form-item>
                        <el-form-item label="法人姓名：" prop="legalPersonName">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.legalPersonName" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="办公电话：" prop="companyPhone">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.companyPhone" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item  label="办公地址：" prop="companyAddressDetail">
                            <el-input  :disabled="disabledVisible" type="textarea" v-model="dataInfo.companyAddressDetail" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="公司税号：" prop="taxpayerId">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.taxpayerId" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="银行开户账号：" prop="bankAccountNumber">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.bankAccountNumber" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="开户银行：" prop="bankName">
                            <el-input type="textarea"   :disabled="disabledVisible" v-model="dataInfo.bankName" placeholder="" clearable></el-input>
                        </el-form-item>
                    </div>
                    <div class="proofNews" style="margin-bottom:20px;font-weight: 650;width:400px;">
                        资质证明
                        <el-form-item label="法人身份证(正反两面)：" prop="idCardNatPicture">
                            <div style="float: left">
                                <!-- <img-cropper
                                        ref="idCardNatPicture"
                                        :index="'idCardNatPicture'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.idCardNatPicture | filterImgUrl" alt="" style="width:100px;height:100px" @click="previewBigImg(dataInfo.idCardNatPicture,0)">
                                <!-- <div>身份证（国徽页）</div> -->
                            </div>
                            <div style="float: left">
                                <!-- <img-cropper
                                        ref="idCardPeoPicture"
                                        :index="'idCardPeoPicture'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.idCardPeoPicture | filterImgUrl" alt="" style="width:100px;height:100px;margin-left:10px" @click="previewBigImg(dataInfo.idCardPeoPicture,0)">
                                <!-- <div>身份证（人像页）</div> -->
                            </div>
                        </el-form-item>
                        <el-form-item label="三证合一营业执照：" prop="electronicBusinessLicense">
                            <div style="width:250px">
                                <!-- <img-cropper
                                        ref="cropperImg3"
                                        :index="'cropperImg3'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.electronicBusinessLicense | filterImgUrl" alt="" style="width:100px;height:100px" @click="previewBigImg(dataInfo.electronicBusinessLicense,0)">
                            </div>
                        </el-form-item>
                        <el-form-item label="纳税人资格证：" prop="taxpayerPicture">
                            <div style="width:250px">
                                <!-- <img-cropper
                                        ref="cropperImg4"
                                        :index="'cropperImg4'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.taxpayerPicture | filterImgUrl" alt="" style="width:100px;height:100px" @click="previewBigImg(dataInfo.taxpayerPicture,0)">
                            </div>
                        </el-form-item>
                        <el-form-item label="银行开户足许可证：" prop="bankAccountPicture">
                            <div style="width:250px">
                                <!-- <img-cropper
                                        ref="cropperImg5"
                                        :index="'cropperImg5'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.bankAccountPicture | filterImgUrl" alt="" style="width:100px;height:100px" @click="previewBigImg(dataInfo.bankAccountPicture,0)">
                            </div>
                        </el-form-item>
                    </div>
                </div>
            </el-form>

            <div>
                <h4>入驻审核</h4>
            </div>
            <el-form :inline="false" :model="dataForm" :rules="dataRule" ref="dataForm"   label-width="200px">
                <el-form-item label="审核结果：" prop="registerAuditStatus">
                    <!-- <el-radio v-model="registerAuditStatus" label="20">审核通过</el-radio>
                    <el-radio v-model="registerAuditStatus" label="30">审核拒绝</el-radio> -->
                    <el-radio-group v-model="dataForm.registerAuditStatus" :disabled="row.operationType=='look'">
                        <el-radio :label="20">审核通过</el-radio>
                        <el-radio :label="30">审核拒绝</el-radio>
                    </el-radio-group>

                </el-form-item>
                 <el-form-item label="备注：" prop="registerAuditCause">
                   <el-input
                       :disabled="row.operationType=='look'"
                        type="textarea"
                        :rows="2"
                        maxlength="100"
                        placeholder="请输入备注"
                        v-model="dataForm.registerAuditCause">
                    </el-input>
                </el-form-item>

                <div style="display: flex;justify-content: center;margin-top:20px" v-if="row.operationType=='examine'">
                        <el-button type="primary" size="mini" @click="dataFormSubmit('dataForm')"  :disabled="saveLoading" :loading="saveLoading">{{saveLoading ? "审核中···" : "确定"}}</el-button>
                        <el-button @click="showListFn"  size="mini" >取消</el-button>
                </div>
            </el-form>
        </div>

        <!--返回按钮-->
        <!-- <div style="margin-top: 40px;margin-bottom: 40px">
            <el-button type="primary" style="display:block;margin:0 auto" @click="showListFn()">返回</el-button>
        </div> -->

	</div>
</template>
<script>
import Bread from "@/components/bread";
import {storeAuditCompanyInfo,shopAuditUpdateStatus} from  "@/api/api.js"
export default {
	data () {
        var validateAuditCaus= (rule, value, callback) => {
            if (this.dataForm.registerAuditStatus==30 && this.dataForm.registerAuditCause=="") {
                return callback(new Error("审核拒绝必填!"))
            }
            return callback()
        }
		return {
            disabledVisible:true,//是否disabled
            breaddata: ["店铺管理", "店铺列表", "店铺信息"],
            row:'',
            dataInfo:"",//回显的数据,
            backScanLoading:false,
            saveLoading:false,// 提交数据loading
            registerAuditStatus:20,
            dataForm:{
                registerAuditStatus:20,//10 待审核 20 审核通过 30 审核拒绝 40 待提交 ,
                registerAuditCause:'', // 审核原因 ,
            },
            dataRule:{
                registerAuditStatus: [
                    // { required: true, message:"必选项不能为空", trigger: 'blur' },
                ],
                registerAuditCause: [
                    // { required: true, message:"必选项不能为空", trigger: 'blur' },
                    { validator: validateAuditCaus, trigger: 'blur' }
                ]
            }
		}
	},
	components:{
        Bread
	},
	methods:{
        init(row){
            this.row = row;
            this.backScan();
        },
             //大图预览带分页
            previewBigImg(images,index) {
                //string转数组
                var imagesArr = images?images.split(","):[];
                if(imagesArr.length==0){
                    return;
                }
                //  如果是绝对地址，不用加前缀(拼接地址)
                imagesArr.forEach((item2,index2)=>{
                    if (/http/.test(item2) || /data:image/.test(item2)) {
                    } else {
                        imagesArr[index2]  = window.SITE_CONFIG['imgURL'] + "" + item2;
                    }
                })
                this.$imagePreview({
                    images: imagesArr,
                    index:index,

                })
            },
        // 回显数据
        backScan(){
            var obj = {
               params:{
                   storeId:this.row.storeId,
                   createDate :this.row.createDate
               }
            }
            this.backScanLoading = true;
            storeAuditCompanyInfo(obj).then((res)=>{
                 this.backScanLoading = false;
                console.log("店铺详情");
                console.log(res);
                if(res.code==200){
                    this.dataInfo = res.data;
                    // 如果是查看的话，回显审核的情况
                    if(this.row.operationType=="look"){
                         this.dataForm.registerAuditStatus = res.data.authAuditStatus
                          this.dataForm.registerAuditCause = res.data.authAuditCause
                    }
                }
            })
        },
        // 显示列表页
        showListFn(){
            this.$emit("showListFn");
        },
        // 提交
        dataFormSubmit(formName) {
            if(this.saveLoading){
                return;
            }
            this.$refs[formName].validate(valid => {
                if (valid) {
                    var obj  = {
                        "auditType": 2,// 审核类型 1 店铺普通信息 2 店铺公司信息 3 店铺入住审核 ,
                        "id": this.row.id, //  审核资质或者公司信息ID ,
                        // "operator": "", //  操作人 ,
                        "registerAuditCause": this.dataForm.registerAuditCause, //  审核原因 ,
                        "registerAuditStatus": this.dataForm.registerAuditStatus, // 10 待审核 20 审核通过 30 审核拒绝 40 待提交 ,
                        "storeId": this.row.storeId?this.row.storeId:this.row.id // 店铺ID
                    }
                    this.saveLoading = true;
                    shopAuditUpdateStatus(obj).then((res)=>{
                        this.saveLoading = false;
                        console.log(res);
                        if(res.code==200){
                             this.$message.success(res.msg);
                             this.showListFn();
                        }else{
                            this.$message.error(res.msg);
                        }
                    })
                } else {
                    return false;
                }
            })
        },
	}
}
</script>
<style  lang="scss" scoped>
    // .formMode{
    //     border-bottom: 1px solid #ECEDF1;
    //     /deep/ .el-form-item__label{
    //         width: 150px !important;
    //     }
    //     .el-input{
    //         width: 250px !important;
    //     }
    //     .textarea{
    //         width: 250px !important;
    //     }
    //     &:first-child{
    //         padding-top: 0;
    //     }
    //     &:last-child{
    //         border: none;
    //     }
    //     .itemMode{
    //         display: flex;
    //         justify-content: space-between;
    //     }
    // }
    /*第二个表单 资质证明*/
    .secondForm{
        display: flex;
        justify-content: space-around;
        .el-textarea__inner{
            // width: 250px !important;
        }
        .el-form-item{
            // display: block;
        }
        .el-input{
            // width: 100% !important;
        }
        .el-textarea{
            // width: 108% !important;
        }
        .companyNews{
            /deep/ .el-form-item__label{
                // width: 150px !important;
            }
        }
        .proofNews{
            /deep/ .el-form-item__label{
                width: 180px !important;
            }
        }
    }
.businessScope{
    padding: 10px;
    max-height:350px;
    overflow-y: scroll;
    box-sizing: border-box;
    border-width: 1px;
    border-style: solid;
    border-color: rgba(217, 217, 217, 1);
    border-radius: 4px;
    box-shadow: 0px 0px 4px rgba(217, 217, 217, 0.988235294117647);
}
</style>
